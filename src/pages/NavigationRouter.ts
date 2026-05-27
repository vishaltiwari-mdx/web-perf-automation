import { Page } from '@playwright/test';
import { ConfigReader } from '../config/ConfigReader';
import { NavigationAgent } from '../agent/NavigationAgent';

/**
 * NavigationRouter — URL-to-handler dispatcher for page-readiness waits.
 *
 * Matching strategy (tries in order):
 *   1. Full hash path   e.g. "ip/catalog", "security/signin"
 *   2. Last URL segment e.g. "catalog", "signin"
 *   3. Default DOM-ready wait (document.readyState === 'complete')
 *
 * Built-in handlers cover the Helix IPLM screens documented in CLAUDE.md.
 * Add custom handlers via `register()` for any additional pages.
 *
 * Usage:
 *   const router = new NavigationRouter(page);
 *   await router.navigateAndPrepare(url);    // navigate + wait
 *   await router.waitForCurrentPage();       // wait only (after redirect)
 *   router.getLastAgGridLoadTime();          // ms AG-Grid spinner was visible
 */
export class NavigationRouter {
  private readonly page:     Page;
  private readonly timeout:  number;
  private readonly handlers: Map<string, (page: Page) => Promise<void>> = new Map();
  private lastAgGridLoadTimeMs  = 0;
  private lastActionToLoadMs    = 0;

  constructor(page: Page) {
    this.page    = page;
    this.timeout = ConfigReader.getInstance().getPageLoadTimeout() * 1000;
    this.registerBuiltinHandlers();
  }

  // ── Public API ───────────────────────────────────────────────────────────────

  /**
   * Register a custom readiness handler for a URL path or last segment.
   * The handler must perform all waits before page measurement begins.
   */
  register(pathOrSegment: string, handler: (page: Page) => Promise<void>): void {
    this.handlers.set(key(pathOrSegment), handler);
  }

  /**
   * Register a custom click-action for a URL path.
   * Stored in a local map consulted by navigateAndPrepare() before falling
   * back to NavigationAgent (navigation-routes.md).
   */
  registerClickAction(pathOrSegment: string, action: (page: Page) => Promise<void>): void {
    this.clickActions.set(key(pathOrSegment), action);
  }

  /** Cached value snapshotted right after dispatch() — kept for back-compat. */
  getLastAgGridLoadTime(): number {
    return this.lastAgGridLoadTimeMs;
  }

  /**
   * Live re-read from the page's MutationObserver. Call this AFTER the
   * metrics collector has finished waiting for the page to be fully ready,
   * so the spinner has had time to disappear and accumulate its full duration.
   */
  async readAgGridLoadTimeLive(): Promise<number> {
    const ms = await this.readAgGridLoadTime();
    if (ms > this.lastAgGridLoadTimeMs) {
      this.lastAgGridLoadTimeMs = ms;
    }
    return this.lastAgGridLoadTimeMs;
  }

  /** Wall-clock ms from the moment the navigation action fired to page-ready. */
  getLastActionToLoadTime(): number {
    return this.lastActionToLoadMs;
  }

  /**
   * Navigate to `url` via UI click-actions where available, then execute
   * the matching readiness handler.  Start performance collection immediately
   * after this call returns.
   */
  async navigateAndPrepare(url: string): Promise<void> {
    const hashPath = NavigationRouter.extractHashPath(url);
    const segment  = NavigationRouter.extractSegment(url);
    console.info(`[NavigationRouter] navigateAndPrepare → ${url}`);

    this.lastAgGridLoadTimeMs = 0;
    this.lastActionToLoadMs   = 0;
    await this.injectAgGridSpinnerTracker();

    // ── Start wall-clock timer before the navigation action fires ─────────
    const t0 = Date.now();

    // 1. Check local click-action overrides first
    const localAction = this.clickActions.get(key(hashPath)) ?? this.clickActions.get(key(segment));
    if (localAction) {
      await localAction(this.page);
    } else {
      // 2. Delegate to NavigationAgent (navigation-routes.md + goto fallback)
      const result = await NavigationAgent.navigateTo(this.page, url, this.timeout, false);
      result.log.forEach(line => console.info(line));

      // NavigationAgent skips login/signin pages — handle them directly
      if (result.method === 'skipped') {
        await this.page.goto(url, { waitUntil: 'domcontentloaded', timeout: this.timeout });
      }
    }

    await this.dispatch(hashPath, segment, url);
    this.lastAgGridLoadTimeMs = await this.readAgGridLoadTime();

    // ── Stop timer after readiness handler confirms page is fully ready ───
    this.lastActionToLoadMs = Date.now() - t0;
    console.info(`[NavigationRouter] action-to-ready: ${this.lastActionToLoadMs} ms`);
  }

  /**
   * Without navigating, wait for the page the browser is currently on.
   * Call this after a programmatic redirect (e.g. post-login).
   * Internally polls until the URL stabilises before dispatching.
   */
  async waitForCurrentPage(): Promise<void> {
    const url      = await this.waitForUrlStable();
    const hashPath = NavigationRouter.extractHashPath(url);
    const segment  = NavigationRouter.extractSegment(url);
    console.info(`[NavigationRouter] waitForCurrentPage (path='${hashPath}', segment='${segment}')`);

    this.lastAgGridLoadTimeMs = 0;
    await this.injectAgGridSpinnerTracker();
    await this.dispatch(hashPath, segment, url);
    this.lastAgGridLoadTimeMs = await this.readAgGridLoadTime();
  }

  // ── Static URL utilities ──────────────────────────────────────────────────────

  /**
   * Returns the path after the `#/` fragment, lower-cased and stripped.
   *   http://host/#/ip/catalog?foo=bar  →  "ip/catalog"
   *   http://host/#/security/signin      →  "security/signin"
   *   http://host/app/reports            →  "app/reports"
   */
  static extractHashPath(url: string): string {
    if (!url?.trim()) return '';
    try {
      const hashIdx = url.indexOf('#');
      let path = hashIdx >= 0 ? url.slice(hashIdx + 1) : new URL(url).pathname;
      if (!path) return '';
      // strip leading slash, query string, trailing slash
      path = path.replace(/^\/+/, '').split('?')[0].replace(/\/+$/, '');
      return path.toLowerCase().trim();
    } catch {
      return '';
    }
  }

  /**
   * Returns only the last non-empty segment of the URL path/hash.
   *   http://host/#/ip/catalog  →  "catalog"
   *   http://host/#/home        →  "home"
   */
  static extractSegment(url: string): string {
    const path = NavigationRouter.extractHashPath(url);
    if (!path) return '';
    const parts = path.split('/');
    for (let i = parts.length - 1; i >= 0; i--) {
      const s = parts[i].trim();
      if (s) return s;
    }
    return '';
  }

  // ── Built-in handlers ─────────────────────────────────────────────────────────

  private registerBuiltinHandlers(): void {
    // ── Sign-in / Login ──────────────────────────────────────────────────────
    const signinHandler = async (_page: Page) => {
      console.info('[handler] signin — waiting for login form');
      const helixInput   = this.page.locator("xpath=//input[contains(@class,'signin-page__card__username-input')]");
      const genericInput = this.page.locator("input[type='text'],input[type='email'],input[type='password'],input[name='username']");
      try {
        await helixInput.or(genericInput).first().waitFor({ state: 'visible', timeout: this.timeout });
      } catch {
        await this.waitForDomReady();
      }
      console.info('[handler] signin — ready');
    };
    this.handlers.set('security/signin', signinHandler);
    this.handlers.set('signin',          signinHandler);
    this.handlers.set('login',           signinHandler);

    // ── Home / Dashboard ─────────────────────────────────────────────────────
    const homeHandler = async (_page: Page) => {
      console.info('[handler] home — waiting for dashboard');
      const ipsButton = this.page.locator("xpath=//a[contains(@class,'dashboard-ips-button')]");
      const appHeader = this.page.locator('.app-header,.app-header__navbar,.navbar');
      try {
        await ipsButton.or(appHeader).first().waitFor({ state: 'visible', timeout: this.timeout });
      } catch {
        await this.waitForDomReady();
      }
      await this.waitForSpinnersToDisappear();
      console.info('[handler] home — ready');
    };
    this.handlers.set('home',      homeHandler);
    this.handlers.set('dashboard', homeHandler);

    // ── IP Catalog ───────────────────────────────────────────────────────────
    this.handlers.set('ip/catalog', async (_page: Page) => {
      console.info('[handler] ip/catalog');
      await this.waitForPageName('IP catalog');
      await this.waitForAgGridSpinner();
      console.info('[handler] ip/catalog — ready');
    });

    // ── Library Catalog ──────────────────────────────────────────────────────
    this.handlers.set('library/catalog', async (_page: Page) => {
      console.info('[handler] library/catalog');
      await this.waitForPageName('Library catalog');
      await this.waitForAgGridSpinner();
      console.info('[handler] library/catalog — ready');
    });

    // ── Library Management ───────────────────────────────────────────────────
    this.handlers.set('library/manage', async (_page: Page) => {
      await this.waitForPageNameContains('Library');
      await this.waitForSpinnersToDisappear();
    });

    // ── Advanced Search ──────────────────────────────────────────────────────
    const searchHandler = async (_page: Page) => {
      await this.waitForPageName('Advanced search');
    };
    this.handlers.set('query/search', searchHandler);
    this.handlers.set('search',       searchHandler);

    // ── Labels Management ─────────────────────────────────────────────────────
    this.handlers.set('labels/manage', async (_page: Page) => {
      await this.waitForPageNameContains('Labels');
      await this.waitForSpinnersToDisappear();
    });

    // ── Property Management ───────────────────────────────────────────────────
    this.handlers.set('property/manage', async (_page: Page) => {
      await this.waitForPageNameContains('Property');
      await this.waitForSpinnersToDisappear();
    });

    // ── Property Sets Management ──────────────────────────────────────────────
    this.handlers.set('propertysets/manage', async (_page: Page) => {
      await this.waitForPageNameContains('Property sets');
      await this.waitForSpinnersToDisappear();
    });

    // ── Create IP ─────────────────────────────────────────────────────────────
    this.handlers.set('ip/create', async (_page: Page) => {
      await this.waitForPageNameContains('Create IP');
    });
  }

  // ── Local click-action registry ───────────────────────────────────────────────

  private readonly clickActions: Map<string, (page: Page) => Promise<void>> = new Map();

  // ── Dispatch ──────────────────────────────────────────────────────────────────

  private async dispatch(hashPath: string, segment: string, url: string): Promise<void> {
    let handler = this.handlers.get(key(hashPath));
    if (!handler && segment !== hashPath) {
      handler = this.handlers.get(key(segment));
    }

    if (handler) {
      console.info(`[NavigationRouter] handler for [${hashPath || segment}]`);
      try {
        await handler(this.page);
      } catch (e) {
        console.warn(`[NavigationRouter] handler threw — DOM ready fallback: ${e}`);
        await this.waitForDomReady();
      }
    } else {
      console.info(`[NavigationRouter] no handler for '${hashPath}' / '${segment}' — DOM ready`);
      await this.waitForDomReady();
    }

    console.info(`[NavigationRouter] ready → ${url}`);
  }

  // ── Wait helpers ──────────────────────────────────────────────────────────────

  private async waitForDomReady(): Promise<void> {
    try {
      await this.page.waitForFunction(
        () => document.readyState === 'complete',
        { timeout: this.timeout },
      );
    } catch { /* ignore */ }
    // Let SPA finish pending renders
    try {
      await this.page.waitForFunction(
        () => {
          const jq = (window as any).jQuery;
          return typeof jq === 'undefined' || jq.active === 0;
        },
        { timeout: 5_000 },
      );
    } catch { /* ignore */ }
  }

  private async waitForPageName(name: string): Promise<void> {
    const locator = this.page.locator(
      `xpath=//div[contains(@class,'page-name')][normalize-space()='${name}']`,
    );
    try {
      await locator.waitFor({ state: 'visible', timeout: this.timeout });
    } catch {
      console.warn(`[handler] page-name '${name}' not found — DOM ready fallback`);
      await this.waitForDomReady();
    }
  }

  private async waitForPageNameContains(partial: string): Promise<void> {
    const locator = this.page.locator(
      `xpath=//div[contains(@class,'page-name')][contains(normalize-space(),'${partial}')]`,
    );
    try {
      await locator.waitFor({ state: 'visible', timeout: this.timeout });
    } catch {
      console.warn(`[handler] page-name containing '${partial}' not found — DOM ready fallback`);
      await this.waitForDomReady();
    }
  }

  private async waitForAgGridSpinner(): Promise<void> {
    // AG-Grid emits multiple overlay variants depending on theme/version:
    //   .ag-overlay-loading-wrapper   — standard wrapper
    //   .ag-overlay-loading-center    — centre overlay text
    //   .ag-loading-text              — Helix custom overlay
    const spinner = this.page.locator(
      '.ag-overlay-loading-wrapper, .ag-overlay-loading-center, .ag-loading-text',
    );
    try {
      const appeared = await spinner.first()
        .waitFor({ state: 'visible', timeout: 5_000 })
        .then(() => true)
        .catch(() => false);
      if (appeared) {
        await spinner.first().waitFor({ state: 'hidden', timeout: 30_000 });
      }
    } catch { /* spinner already gone */ }
  }

  private async waitForSpinnersToDisappear(): Promise<void> {
    const spinner = this.page.locator(
      '.spinner,.loading-spinner,.ngx-spinner,[data-test="loading-spinner"]',
    );
    try {
      const count = await spinner.count();
      if (count > 0) {
        await spinner.first().waitFor({ state: 'hidden', timeout: 15_000 });
      }
    } catch { /* ignore */ }
  }

  // ── AG-Grid spinner tracker ───────────────────────────────────────────────────

  /**
   * Injects a MutationObserver (window.__agGridSpinnerTracker) that measures
   * the exact ms the AG-Grid "Loading" text is visible.
   */
  private async injectAgGridSpinnerTracker(): Promise<void> {
    try {
      await this.page.evaluate(() => {
        (window as any).__agGridSpinnerMs    = 0;
        (window as any).__agGridSpinnerStart = null;

        if ((window as any).__agGridSpinnerObs) {
          (window as any).__agGridSpinnerObs.disconnect();
        }

        const SPINNER_SELECTOR =
          '.ag-overlay-loading-wrapper, .ag-overlay-loading-center, .ag-loading-text';

        const obs = new MutationObserver(() => {
          const els = document.querySelectorAll(SPINNER_SELECTOR);
          const visible = Array.from(els).some(
            el => (el as HTMLElement).offsetParent !== null,
          );
          if (visible && !(window as any).__agGridSpinnerStart) {
            (window as any).__agGridSpinnerStart = Date.now();
          } else if (!visible && (window as any).__agGridSpinnerStart) {
            (window as any).__agGridSpinnerMs += Date.now() - (window as any).__agGridSpinnerStart;
            (window as any).__agGridSpinnerStart = null;
          }
        });
        obs.observe(document.documentElement, {
          childList: true, subtree: true, attributes: true,
        });
        (window as any).__agGridSpinnerObs = obs;
      });
    } catch { /* page may not be open yet — safe to ignore */ }
  }

  private async readAgGridLoadTime(): Promise<number> {
    try {
      return await this.page.evaluate(
        () => (window as any).__agGridSpinnerMs ?? 0,
      );
    } catch {
      return 0;
    }
  }

  // ── URL stability ─────────────────────────────────────────────────────────────

  private async waitForUrlStable(): Promise<string> {
    let prev = this.page.url();
    for (let i = 0; i < 30; i++) {
      await this.page.waitForTimeout(200);
      const current = this.page.url();
      if (current === prev) return current;
      prev = current;
    }
    return this.page.url();
  }
}

function key(s: string): string {
  return s ? s.toLowerCase().trim() : '';
}
