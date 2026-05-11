/**
 * StepParserAgent — converts plain-text navigation descriptions into proper
 * CSS / XPath Playwright click steps.
 *
 * Strategy:
 *   1. Rule-based parser  — deterministic, no network, covers all known Helix IPLM flows
 *   2. OLLAMA fallback    — local open-source LLM for unknown descriptions
 *      Default endpoint:   http://localhost:11434  (override: OLLAMA_HOST env var)
 *      Default model:      llama3.2                (override: OLLAMA_MODEL env var)
 *
 * Selector library sourced from phi_web_automation page objects:
 *   HeaderPage.java · HeaderSubMenuPage.java · IpCatalogPage.java
 *   GridTablePage.java · IpDetailsPage.java · all Management pages
 */

import * as fs   from 'fs';
import * as path from 'path';
import type { NavigationStep }  from './NavigationAgent';
import { PageObjectAnalyzer }   from './PageObjectAnalyzer';

const ROUTES_FILE   = path.resolve(__dirname, '../../navigation-routes.md');
const OLLAMA_HOST   = process.env['OLLAMA_HOST']  ?? 'http://localhost:11434';
const OLLAMA_MODEL  = process.env['OLLAMA_MODEL'] ?? 'llama3.2';

// ─────────────────────────────────────────────────────────────────────────────
//  Selector constants (from phi_web_automation page objects)
// ─────────────────────────────────────────────────────────────────────────────

const SEL = {
  // Header — HeaderPage.java
  catalogsBtn:    { t: 'css',   s: `div[data-testid='ui-dropdown-menu-catalog'] > button`,  d: 'Open Catalogs dropdown'          },
  adminBtn:       { t: 'css',   s: `div[data-testid='ui-dropdown-menu-admin'] > button`,    d: 'Open Administration dropdown'    },
  createBtn:      { t: 'css',   s: `div[data-testid='ui-dropdown-menu-create'] > button`,   d: 'Open Create dropdown'            },
  cartIcon:       { t: 'css',   s: `li[data-testid='shopping-cart'] > a`,                   d: 'Click Shopping Cart icon'        },
  homeLogo:       { t: 'css',   s: `.app-header__navbar__brand a.navbar-brand`,             d: 'Click product logo (go Home)'    },

  // Catalog submenu — HeaderPage.java / HeaderSubMenuPage.java
  ipCatalogItem:  { t: 'xpath', s: `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']`,         d: 'Click IP catalog item'          },
  libCatalogItem: { t: 'xpath', s: `//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']`, d: 'Click Library catalog item'    },
  advSearchItem:  { t: 'xpath', s: `//a[@data-testid='menu-item-link']//span[text()='Advanced search']`,                d: 'Click Advanced search item'     },

  // Administration submenu — HeaderSubMenuPage.java
  libMgmt:        { t: 'css',   s: `.menu-admin__library-manage a[data-testid='menu-item-link']`,    d: 'Click Libraries management link'       },
  labelsMgmt:     { t: 'css',   s: `.menu-admin__labels-manage a[data-testid='menu-item-link']`,     d: 'Click Labels management link'          },
  propMgmt:       { t: 'css',   s: `.menu-admin__property-manage a[data-testid='menu-item-link']`,   d: 'Click Properties management link'      },
  propSetMgmt:    { t: 'css',   s: `.menu-admin__propertysets a[data-testid='menu-item-link']`,      d: 'Click Property Sets management link'   },
  qfMgmt:         { t: 'css',   s: `.menu-admin__queryfolder-manage a[data-testid='menu-item-link']`,d: 'Click Query Folders management link'   },
  geoMgmt:        { t: 'css',   s: `.menu-admin__geofencing-manage a[data-testid='menu-item-link']`, d: 'Click Geofencing management link'      },

  // Create submenu
  newIp:          { t: 'css',   s: `.menu-new__create-ip`, d: 'Click New IP option' },

  // IP Details tabs — IpDetailsPage.java
  tabInfo:        { t: 'css',   s: `[data-testid='ip-details-info-tab']`,        d: 'Click Info tab'        },
  tabProperties:  { t: 'css',   s: `[data-testid='ip-details-properties-tab']`,  d: 'Click Properties tab'  },
  tabPermissions: { t: 'css',   s: `[data-testid='ip-details-permissions-tab']`, d: 'Click Permissions tab' },
  tabHooks:       { t: 'css',   s: `[data-testid='ip-details-hooks-tab']`,       d: 'Click Hooks tab'       },
  tabContents:    { t: 'css',   s: `[data-testid='contents-tab']`,               d: 'Click Contents tab'    },
  tabHierarchy:   { t: 'css',   s: `[data-testid='hierarchy-tab']`,              d: 'Click Hierarchy tab'   },
} as const;

type SelEntry = { t: string; s: string; d: string };

function step(e: SelEntry): NavigationStep {
  return { selectorType: e.t as 'css' | 'xpath' | 'text', selector: e.s, description: e.d };
}

function rowStep(ipName: string): NavigationStep {
  return {
    selectorType: 'xpath',
    selector:     `//div[@col-id='name']//*[@title='${ipName}']`,
    description:  `Click ${ipName} row in IP catalog table`,
  };
}

// ─────────────────────────────────────────────────────────────────────────────

export class StepParserAgent {

  // ── Main entry point ──────────────────────────────────────────────────────

  async parseDescription(description: string, targetUrl?: string): Promise<NavigationStep[]> {
    const ipName  = targetUrl ? this.extractIpName(targetUrl)   : '';
    const urlHash = targetUrl ? this.extractHash(targetUrl)      : '';

    // 1. Rule-based — instant, no network, covers all known Helix IPLM flows
    const ruled = this.ruleBased(description, urlHash, ipName);
    if (ruled && ruled.length > 0) return ruled;

    // 2. Rule-based gave no match — scan phi_web_automation pageobject folder live
    //    and pass the extracted selectors to OLLAMA for unknown flows
    const poContext = PageObjectAnalyzer.isAvailable()
      ? PageObjectAnalyzer.contextFor(urlHash)
      : '';

    try {
      return await this.ollamaFallback(description, targetUrl ?? '', ipName, poContext);
    } catch (e) {
      throw new Error(
        `Could not generate steps.\n` +
        `• Rule-based: no pattern matched for "${urlHash}"\n` +
        `• OLLAMA: not running or unreachable (start with: ollama serve, model: ${OLLAMA_MODEL})\n` +
        `Cause: ${e}`,
      );
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  //  Rule-based parser — covers all known Helix IPLM navigation patterns
  // ─────────────────────────────────────────────────────────────────────────

  private ruleBased(desc: string, urlHash: string, ipName: string): NavigationStep[] | null {
    const d = desc.toLowerCase();
    const steps: NavigationStep[] = [];

    // ── Home / Dashboard ───────────────────────────────────────────────────
    if (this.has(d, ['home', 'dashboard', 'logo', 'product logo']) ||
        urlHash === 'home' || urlHash === 'dashboard') {
      return [step(SEL.homeLogo)];
    }

    // ── Shopping Cart ──────────────────────────────────────────────────────
    if (this.has(d, ['shopping cart', 'cart']) || urlHash === 'shoppingcart') {
      return [step(SEL.cartIcon)];
    }

    // ── Create New IP ──────────────────────────────────────────────────────
    if ((this.has(d, ['create', 'new ip']) || urlHash === 'ip/create')) {
      return [step(SEL.createBtn), step(SEL.newIp)];
    }

    // ── IP Details page: ip/{name}/tab ─────────────────────────────────────
    //    Navigate: Catalogs → IP catalog → click row → (optional) tab
    const ipDetailMatch = urlHash.match(/^ip\/([^/]+)\/([^/?]+)/);
    if (ipDetailMatch) {
      const name = ipName || ipDetailMatch[1];
      const tab  = ipDetailMatch[2].toLowerCase();
      steps.push(step(SEL.catalogsBtn));
      steps.push(step(SEL.ipCatalogItem));
      steps.push(rowStep(name));
      const tabStep = this.tabStep(tab, d);
      if (tabStep) steps.push(tabStep);
      return steps;
    }

    // ── IP Catalog (plain) ─────────────────────────────────────────────────
    if (this.has(d, ['ip catalog', 'ipcatalog', 'ip_catalog']) || urlHash === 'ip/catalog') {
      // If IP name also mentioned — go to catalog and click the row
      if (ipName) {
        return [step(SEL.catalogsBtn), step(SEL.ipCatalogItem), rowStep(ipName)];
      }
      return [step(SEL.catalogsBtn), step(SEL.ipCatalogItem)];
    }

    // ── Library Catalog ────────────────────────────────────────────────────
    if (this.has(d, ['library catalog']) || urlHash === 'library/catalog') {
      return [step(SEL.catalogsBtn), step(SEL.libCatalogItem)];
    }

    // ── Advanced Search ────────────────────────────────────────────────────
    if (this.has(d, ['advanced search', 'query search', 'query/search']) ||
        urlHash === 'query/search') {
      return [step(SEL.catalogsBtn), step(SEL.advSearchItem)];
    }

    // ── Administration pages ───────────────────────────────────────────────
    const isAdmin = this.has(d, ['admin', 'administration']) ||
                    /^(library\/manage|labels\/manage|property\/manage|propertysets|queryfolder|geofencing)/.test(urlHash);
    if (isAdmin) {
      steps.push(step(SEL.adminBtn));
      if (this.has(d, ['librar']) || urlHash.startsWith('library/manage'))   steps.push(step(SEL.libMgmt));
      else if (this.has(d, ['label'])  || urlHash.startsWith('labels'))       steps.push(step(SEL.labelsMgmt));
      else if (this.has(d, ['property set', 'propertyset']) ||
               urlHash.startsWith('propertysets'))                             steps.push(step(SEL.propSetMgmt));
      else if (this.has(d, ['propert']) || urlHash.startsWith('property'))    steps.push(step(SEL.propMgmt));
      else if (this.has(d, ['query folder', 'queryfolder']) ||
               urlHash.startsWith('queryfolder'))                              steps.push(step(SEL.qfMgmt));
      else if (this.has(d, ['geofenc']) || urlHash.startsWith('geofencing'))  steps.push(step(SEL.geoMgmt));
      if (steps.length > 1) return steps;
    }

    // ── Fallback: description mentions "catalog" + IP name in URL ──────────
    if (this.has(d, ['catalog']) && ipName) {
      return [step(SEL.catalogsBtn), step(SEL.ipCatalogItem), rowStep(ipName)];
    }

    return null; // No rule matched — hand off to OLLAMA
  }

  // ── Map URL tab segment → NavigationStep ─────────────────────────────────
  private tabStep(tab: string, desc: string): NavigationStep | null {
    if (tab === 'details' || tab === 'info'  || desc.includes('info tab'))        return step(SEL.tabInfo);
    if (tab === 'properties' || desc.includes('properties tab'))                  return step(SEL.tabProperties);
    if (tab === 'permissions' || desc.includes('permissions'))                    return step(SEL.tabPermissions);
    if (tab === 'hooks'       || desc.includes('hooks'))                          return step(SEL.tabHooks);
    if (tab === 'contents'    || desc.includes('contents'))                       return step(SEL.tabContents);
    if (tab === 'hierarchy'   || desc.includes('hierarchy'))                      return step(SEL.tabHierarchy);
    if (tab === 'usage'       || desc.includes('usage'))
      return { selectorType: 'text', selector: 'Usage', description: 'Click Usage tab' };
    return null;
  }

  // ── Keyword helper ────────────────────────────────────────────────────────
  private has(desc: string, keywords: string[]): boolean {
    return keywords.some(k => desc.includes(k));
  }

  // ─────────────────────────────────────────────────────────────────────────
  //  OLLAMA fallback — local open-source LLM (llama3.2 / mistral / etc.)
  // ─────────────────────────────────────────────────────────────────────────

  private async ollamaFallback(
    description: string, targetUrl: string, ipName: string, poContext = '',
  ): Promise<NavigationStep[]> {
    const ipHint  = ipName ? `\nExtracted IP name from URL: "${ipName}" — substitute for {IP_NAME} in row-click XPath` : '';
    const urlHint = targetUrl ? `\nTarget URL: ${targetUrl}` : '';
    const poHint  = poContext ? `\n\nADDITIONAL SELECTORS from phi_web_automation pageobject scan:\n${poContext}` : '';

    const prompt =
`You are a Playwright automation assistant for Helix IPLM.
Convert the navigation description into click steps using ONLY these selectors.
NEVER use page.goto(). Every line must be a real UI click.

KNOWN SELECTORS:
css: \`div[data-testid='ui-dropdown-menu-catalog'] > button\` — Open Catalogs dropdown
xpath: \`//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']\` — Click IP catalog item
xpath: \`//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']\` — Click Library catalog item
xpath: \`//a[@data-testid='menu-item-link']//span[text()='Advanced search']\` — Click Advanced search item
css: \`div[data-testid='ui-dropdown-menu-admin'] > button\` — Open Administration dropdown
css: \`.menu-admin__library-manage a[data-testid='menu-item-link']\` — Click Libraries link
css: \`.menu-admin__labels-manage a[data-testid='menu-item-link']\` — Click Labels link
css: \`.menu-admin__property-manage a[data-testid='menu-item-link']\` — Click Properties link
css: \`div[data-testid='ui-dropdown-menu-create'] > button\` — Open Create dropdown
css: \`.menu-new__create-ip\` — Click New IP option
css: \`li[data-testid='shopping-cart'] > a\` — Click Shopping Cart icon
css: \`.app-header__navbar__brand a.navbar-brand\` — Click product logo (go Home)
xpath: \`//div[@col-id='name']//*[@title='{IP_NAME}']\` — Click {IP_NAME} row in IP catalog table
css: \`[data-testid='ip-details-info-tab']\` — Click Info tab
css: \`[data-testid='ip-details-properties-tab']\` — Click Properties tab
css: \`[data-testid='hierarchy-tab']\` — Click Hierarchy tab
css: \`[data-testid='contents-tab']\` — Click Contents tab

OUTPUT FORMAT — one line per step, nothing else:
css: \`selector\` — label
xpath: \`selector\` — label
text: \`visible text\` — label${urlHint}${ipHint}${poHint}

Description: "${description}"`;

    const resp = await fetch(`${OLLAMA_HOST}/api/generate`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify({ model: OLLAMA_MODEL, prompt, stream: false }),
    });

    if (!resp.ok) throw new Error(`OLLAMA ${resp.status}: ${await resp.text()}`);

    const data = await resp.json() as { response?: string };
    return this.parseOutput(data.response ?? '', ipName);
  }

  // ─────────────────────────────────────────────────────────────────────────
  //  Output parser — handles css:/xpath:/text: lines with backticks
  // ─────────────────────────────────────────────────────────────────────────

  private parseOutput(text: string, ipName: string): NavigationStep[] {
    return text.split('\n').map(l => l.trim()).filter(Boolean).flatMap(line => {
      const clean = line.replace(/^[\d.\-*•]+\s*/, '').trim();
      const m = clean.match(/^(css|xpath|text):\s*`([^`]+)`\s*[—–\-]+\s*(.*)$/i);
      if (m) {
        const sel = ipName ? m[2].replace(/\{IP_NAME\}/gi, ipName) : m[2];
        return [{ selectorType: m[1].toLowerCase() as 'css'|'xpath'|'text', selector: sel.trim(),
                  description: (m[3].trim() || sel).replace(/\{IP_NAME\}/gi, ipName) }];
      }
      if (clean.length > 0 && clean.length < 80 && !clean.includes('`'))
        return [{ selectorType: 'text' as const, selector: clean, description: clean }];
      return [];
    });
  }

  // ─────────────────────────────────────────────────────────────────────────
  //  URL helpers
  // ─────────────────────────────────────────────────────────────────────────

  /** Extract hash path from a full URL: "http://host/#/ip/catalog" → "ip/catalog" */
  private extractHash(url: string): string {
    try   { return new URL(url).hash.replace(/^#\//, '').split('?')[0]; }
    catch { return url.replace(/^[#/\s]+/, '').split('?')[0]; }
  }

  /** Extract IP/library name from URL path: "ip/ARM.cortex2/details" → "ARM.cortex2" */
  private extractIpName(url: string): string {
    const hash = this.extractHash(url);
    const m = hash.match(/^(?:ip|ipv|library)\/([^/?]+)/);
    return m ? decodeURIComponent(m[1]) : '';
  }

  // ─────────────────────────────────────────────────────────────────────────
  //  Detection helper (used by NavigationAgent at runtime)
  // ─────────────────────────────────────────────────────────────────────────

  /** True when a saved text step looks like a prose description, not an atomic label */
  static isDescription(step: NavigationStep): boolean {
    if (step.selectorType !== 'text') return false;
    const s = step.selector.toLowerCase();
    return (
      step.selector.length > 40 ||
      /\b(then|navigate|go to|click to|user|page\b|able to|displaying|butten|button|catalog|once we)\b/.test(s)
    );
  }

  /** Read existing navigation-routes.md steps — used only for context display, not parsing */
  readRoutesContext(): string {
    if (!fs.existsSync(ROUTES_FILE)) return '';
    const seen = new Set<string>();
    return fs.readFileSync(ROUTES_FILE, 'utf8').split('\n')
      .flatMap(line => {
        const m = line.match(/^\d+\.\s+(css|xpath|text):\s+`([^`]+)`\s+[—–-]+\s+(.+)$/i);
        if (!m) return [];
        const e = `${m[1]}: \`${m[2]}\` — ${m[3].trim()}`;
        return seen.has(e) ? [] : (seen.add(e), [e]);
      })
      .join('\n');
  }
}
