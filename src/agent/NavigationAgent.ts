import * as fs   from 'fs';
import * as path from 'path';
import { Page } from '@playwright/test';
import { NavigationRouter } from '../pages/NavigationRouter';
import { StepParserAgent }  from './StepParserAgent';

// ── Public types ───────────────────────────────────────────────────────────────

export interface NavigationStep {
  description:  string;
  selector:     string;
  selectorType: 'css' | 'xpath' | 'text';
}

export interface NavigationRoute {
  hashPattern: string;
  description: string;
  steps:       NavigationStep[];
  isDynamic:   boolean;
}

export interface NavigationResult {
  success:  boolean;
  method:   'ui-actions' | 'direct-url' | 'skipped';
  finalUrl: string;
  log:      string[];
  error?:   string;
}

/**
 * NavigationAgent — UI-action-based page navigation for Helix IPLM.
 *
 * Routes are loaded from `navigation-routes.md` at the project root.
 * Edit that file to add, remove, or change routes — no TypeScript changes needed.
 *
 * File format (each route block):
 *   ## <hash-pattern> [dynamic]   ← heading + optional [dynamic] flag
 *   > <description>               ← blockquote
 *   1. <type>: `<selector>` — <label>   ← ordered list of click steps
 *   ---                                 ← separator between routes
 */
export class NavigationAgent {

  // Path to the markdown file (relative to project root)
  static readonly ROUTES_FILE = path.resolve(__dirname, '../../navigation-routes.md');

  // Parsed route cache — loaded once per process
  private static _routes: NavigationRoute[] | null = null;

  // ── Route loading ──────────────────────────────────────────────────────────

  static get ROUTES(): NavigationRoute[] {
    if (!this._routes) {
      this._routes = this.loadRoutes();
    }
    return this._routes;
  }

  /** Force-reload routes from disk (useful in tests or when the file changes) */
  static reload(): void {
    this._routes = null;
  }

  private static loadRoutes(): NavigationRoute[] {
    if (!fs.existsSync(this.ROUTES_FILE)) {
      console.warn(`[NavigationAgent] Routes file not found: ${this.ROUTES_FILE}`);
      return [];
    }

    const content = fs.readFileSync(this.ROUTES_FILE, 'utf8');
    return this.parseMarkdown(content);
  }

  // ── Markdown parser ────────────────────────────────────────────────────────

  /**
   * Parse navigation-routes.md into NavigationRoute[].
   *
   * Grammar:
   *   ## <pattern> [dynamic]   → starts a new route block
   *   > <text>                 → route description (first blockquote wins)
   *   N. <type>: `<sel>` — <desc>  → click step (ordered list item)
   *   ---                      → ends current route block (optional)
   */
  static parseMarkdown(content: string): NavigationRoute[] {
    const routes: NavigationRoute[] = [];
    let current: NavigationRoute | null = null;

    for (const raw of content.split('\n')) {
      const line = raw.trimEnd();

      // ── Route heading: ## pattern [dynamic]
      const headingMatch = line.match(/^##\s+(.+?)(?:\s+\[dynamic\])?\s*$/);
      if (headingMatch) {
        if (current) routes.push(current);
        const pattern   = headingMatch[1].trim().toLowerCase().replace(/^\//, '');
        const isDynamic = line.includes('[dynamic]');
        current = { hashPattern: pattern, description: '', steps: [], isDynamic };
        continue;
      }

      if (!current) continue;

      // ── Description: > text
      if (line.startsWith('> ') && !current.description) {
        current.description = line.slice(2).trim();
        continue;
      }

      // ── Click step: N. type: `selector` — label
      // Matches:  1. css: `div[...]` — Open dropdown
      //           2. xpath: `//a[...]` — Click item
      //           3. text: `Catalogs` — Click Catalogs
      const stepMatch = line.match(
        /^\d+\.\s+(css|xpath|text):\s+`([^`]+)`\s+[—–-]+\s+(.+)$/i,
      );
      if (stepMatch) {
        current.steps.push({
          selectorType: stepMatch[1].toLowerCase() as 'css' | 'xpath' | 'text',
          selector:     stepMatch[2].trim(),
          description:  stepMatch[3].trim(),
        });
        continue;
      }

      // ── Section separator: --- (save current route if any)
      if (/^-{3,}$/.test(line.trim()) && current.description) {
        routes.push(current);
        current = null;
      }
    }

    // Push last block if file doesn't end with ---
    if (current?.description) routes.push(current);

    return routes;
  }

  // ── Core navigation method ─────────────────────────────────────────────────

  /**
   * Navigate to `url` using header menu clicks wherever possible.
   *
   * - Login/signin pages: returns immediately (not handled here)
   * - Known static routes: executes menu click steps from navigation-routes.md
   * - Dynamic / unknown routes: falls back to page.goto()
   */
  /**
   * Navigate to `url` using header menu clicks wherever possible.
   *
   * @param strict  When true, throws instead of falling back to page.goto() for
   *                unknown or failed static routes.  Dynamic routes always use
   *                page.goto() because no fixed menu path exists for them.
   */
  static async navigateTo(
    page: Page,
    url: string,
    timeoutMs = 15_000,
    strict    = false,
  ): Promise<NavigationResult> {
    const hashPath = NavigationRouter.extractHashPath(url);
    const log: string[] = [];

    // Never handle login pages
    if (this.isLoginPath(hashPath)) {
      return {
        success: false, method: 'skipped', finalUrl: page.url(),
        error: 'Login/signin pages are handled by LoginPage, not NavigationAgent',
        log: [],
      };
    }

    const route = this.findRoute(hashPath);

    // ── Known static route: execute menu clicks ──────────────────────────────
    if (route && !route.isDynamic && route.steps.length > 0) {
      log.push(`[NavigationAgent] Route: "${route.hashPattern}" — ${route.description}`);
      try {
        for (const step of route.steps) {
          // Expand prose descriptions into atomic steps via Claude at runtime
          let stepsToRun: typeof route.steps = [step];
          if (StepParserAgent.isDescription(step)) {
            try {
              const parsed = await new StepParserAgent().parseDescription(step.selector, url);
              if (parsed.length) {
                log.push(`  🤖 AI parsed "${step.selector.slice(0, 50)}…" → ${parsed.length} atomic steps`);
                stepsToRun = parsed;
              }
            } catch (e) {
              log.push(`  ⚠ AI parse failed (${e}), using original step`);
            }
          }

          for (const s of stepsToRun) {
            log.push(`  → ${s.description}  (${s.selectorType}: ${s.selector})`);
            const locator =
              s.selectorType === 'xpath' ? page.locator(`xpath=${s.selector}`) :
              s.selectorType === 'text'  ? page.getByText(s.selector, { exact: false }) :
              page.locator(s.selector);
            await locator.first().waitFor({ state: 'visible', timeout: timeoutMs });
            const urlBefore = page.url();
            await locator.first().click();
            await page.waitForTimeout(400); // dropdown open animation / SPA micro-render
            // If the click triggered page navigation, wait for AG-Grid to finish loading
            if (page.url() !== urlBefore) {
              await page.waitForLoadState('domcontentloaded', { timeout: timeoutMs }).catch(() => {});
              const spinner = page.locator(`xpath=//span[contains(@class,'ag-loading-text') and normalize-space()='Loading']`);
              try {
                const visible = await spinner.first().isVisible({ timeout: 2000 }).catch(() => false);
                if (visible) {
                  log.push(`  ⏳ Waiting for AG-Grid to finish loading…`);
                  await spinner.first().waitFor({ state: 'hidden', timeout: timeoutMs });
                }
              } catch { /* AG-Grid not on this page */ }
              await page.waitForTimeout(500); // SPA settle after data load
            }
          }
        }

        await page.waitForLoadState('domcontentloaded', { timeout: timeoutMs }).catch(() => {});
        await page.waitForTimeout(700); // SPA render settle
        const finalUrl = page.url();
        log.push(`  ✓ Navigation complete — ${finalUrl}`);
        return { success: true, method: 'ui-actions', finalUrl, log };
      } catch (e) {
        if (strict) {
          throw new Error(
            `[NavigationAgent] UI action failed for "${hashPath}" (strict mode). ` +
            `Verify selectors in navigation-routes.md. Cause: ${e}`,
          );
        }
        log.push(`  ✗ UI action failed (${e}) — falling back to direct URL`);
        // fall through to goto()
      }

    } else if (route?.isDynamic) {
      // Dynamic routes (variable IDs) have no fixed menu path — goto is the only option
      log.push(`[NavigationAgent] Dynamic route "${hashPath}" — no fixed menu path, using direct URL`);

    } else {
      // No route found in navigation-routes.md
      if (strict) {
        throw new Error(
          `[NavigationAgent] No navigation route found for "${hashPath}" (strict mode). ` +
          `Add an entry "## ${hashPath}" with click steps to navigation-routes.md.`,
        );
      }
      log.push(`[NavigationAgent] No registered route for "${hashPath}" — using direct URL`);
    }

    // ── Fallback / dynamic: direct URL navigation ───────────────────────────
    log.push(`  → page.goto(${url})`);
    await page.goto(url, { waitUntil: 'domcontentloaded', timeout: timeoutMs * 2 });
    await page.waitForTimeout(700);
    const finalUrl = page.url();
    log.push(`  ✓ Arrived at: ${finalUrl}`);
    return { success: true, method: 'direct-url', finalUrl, log };
  }

  // ── Introspection helpers ──────────────────────────────────────────────────

  /** Human-readable description of how to navigate to a URL */
  static describeNavigation(url: string): string {
    const hashPath = NavigationRouter.extractHashPath(url);

    if (this.isLoginPath(hashPath)) {
      return 'Login page — handled by LoginPage (not NavigationAgent)';
    }

    const route = this.findRoute(hashPath);
    if (!route) {
      return `"${hashPath}" — no registered route, will fall back to direct URL navigation`;
    }
    if (route.isDynamic) {
      return `"${hashPath}" — dynamic route (${route.description})\n  Will use direct URL navigation`;
    }

    const stepsText = route.steps
      .map((s, i) => `  ${i + 1}. [${s.selectorType}] ${s.selector}\n     ${s.description}`)
      .join('\n');
    return `${route.description}\nSteps:\n${stepsText}`;
  }

  /** All routes loaded from navigation-routes.md */
  static listRoutes(): Array<{
    route: string; description: string; steps: string[]; isDynamic: boolean;
  }> {
    return this.ROUTES.map(r => ({
      route:       r.hashPattern,
      description: r.description,
      steps:       r.steps.map(s => `${s.selectorType}: ${s.selector} — ${s.description}`),
      isDynamic:   r.isDynamic,
    }));
  }

  // ── Private helpers ────────────────────────────────────────────────────────

  private static findRoute(hashPath: string): NavigationRoute | undefined {
    const routes = this.ROUTES;
    // Sort longest first so specific patterns beat short prefixes
    const sorted = [...routes].sort((a, b) => b.hashPattern.length - a.hashPattern.length);
    const exact  = sorted.find(r => r.hashPattern === hashPath);
    if (exact) return exact;
    return sorted.find(r => r.isDynamic && hashPath.startsWith(r.hashPattern));
  }

  private static isLoginPath(path: string): boolean {
    return path.includes('signin') || path.includes('login') || path.startsWith('security');
  }
}
