/**
 * PageObjectAnalyzer — scans the phi_web_automation Java page-object source tree
 * and extracts CSS / XPath selectors with their method/class context.
 *
 * Used by StepParserAgent to build a live selector library for any target URL,
 * replacing hard-coded strings with data read directly from the source of truth.
 */

import * as fs   from 'fs';
import * as path from 'path';

// Root of the phi_web_automation page-object package
const PHI_PO_ROOT = path.join(
  'C:\\Users\\vishal.tiwari\\Project\\phi_web_automation',
  'src\\main\\java\\com\\methodics\\phi\\pageobject',
);

export interface PoSelector {
  selectorType: 'css' | 'xpath';
  selector:     string;
  methodName:   string;   // nearest Java method name
  className:    string;   // Java class (file) name without .java
}

export class PageObjectAnalyzer {

  // In-memory cache so we only scan disk once per server start
  private static _cache: PoSelector[] | null = null;

  // ── Public API ─────────────────────────────────────────────────────────────

  /** All selectors extracted from every Java page-object file */
  static all(): PoSelector[] {
    if (!this._cache) this._cache = this.scanRoot();
    return this._cache;
  }

  /** Invalidate cache (e.g. after phi_web_automation is modified) */
  static reset(): void { this._cache = null; }

  /**
   * Return a formatted selector-library string relevant to the given URL hash.
   * Used to augment the OLLAMA / rule-based prompt with live data from the repo.
   */
  static contextFor(urlHash: string): string {
    const scored = this.all()
      .map(s => ({ s, score: this.score(s, urlHash.toLowerCase()) }))
      .filter(x => x.score > 0)
      .sort((a, b) => b.score - a.score);

    // Deduplicate by selector string, keep top 40
    const seen = new Set<string>();
    const lines: string[] = [];
    for (const { s } of scored) {
      if (seen.has(s.selector)) continue;
      seen.add(s.selector);
      lines.push(`${s.selectorType}: \`${s.selector}\` — ${s.methodName} (${s.className})`);
      if (lines.length >= 40) break;
    }
    return lines.join('\n');
  }

  /** Check whether the phi_web_automation source tree exists on this machine */
  static isAvailable(): boolean {
    return fs.existsSync(PHI_PO_ROOT);
  }

  // ── Scanning ───────────────────────────────────────────────────────────────

  private static scanRoot(): PoSelector[] {
    if (!this.isAvailable()) {
      console.warn(`[PageObjectAnalyzer] phi_web_automation not found at ${PHI_PO_ROOT}`);
      return [];
    }
    const results: PoSelector[] = [];
    for (const filePath of this.walkJava(PHI_PO_ROOT)) {
      const className = path.basename(filePath, '.java');
      const src       = fs.readFileSync(filePath, 'utf8');
      results.push(...this.extractFrom(src, className));
    }
    console.log(`[PageObjectAnalyzer] Loaded ${results.length} selectors from phi_web_automation`);
    return results;
  }

  private static *walkJava(dir: string): Generator<string> {
    for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
      const full = path.join(dir, entry.name);
      if (entry.isDirectory()) yield* this.walkJava(full);
      else if (entry.name.endsWith('.java')) yield full;
    }
  }

  // ── Selector extraction ────────────────────────────────────────────────────

  private static extractFrom(src: string, className: string): PoSelector[] {
    const out: PoSelector[] = [];
    const lines = src.split('\n');

    for (let i = 0; i < lines.length; i++) {
      const line = lines[i];

      // ── XPath: any string starting with //
      for (const m of line.matchAll(/["'](\/\/[^"'\n]{5,}?)["']/g)) {
        const sel = m[1].trim();
        if (!sel.includes('{') && !sel.includes('\\n') && sel.length < 300) {
          out.push({ selectorType: 'xpath', selector: sel,
                     methodName: this.nearestMethod(lines, i), className });
        }
      }

      // ── CSS via By.cssSelector("...")
      for (const m of line.matchAll(/cssSelector\s*\(\s*["']([^"'\n]{3,})["']/g)) {
        out.push({ selectorType: 'css', selector: m[1].trim(),
                   methodName: this.nearestMethod(lines, i), className });
      }

      // ── CSS via @FindBy(css = "...")
      for (const m of line.matchAll(/@FindBy\([^)]*css\s*=\s*["']([^"'\n]{3,})["']/g)) {
        out.push({ selectorType: 'css', selector: m[1].trim(),
                   methodName: this.nextFieldName(lines, i), className });
      }

      // ── CSS: data-testid attribute selectors
      for (const m of line.matchAll(/["'](\[data-testid=['"][^'"]+['"]\][^"']{0,80}?)["']/g)) {
        out.push({ selectorType: 'css', selector: m[1].trim(),
                   methodName: this.nearestMethod(lines, i), className });
      }

      // ── CSS: dot-prefixed class selectors (menu-*, app-*, etc.)
      for (const m of line.matchAll(/["'](\.[a-zA-Z][a-zA-Z0-9_\-]{2,}(?:[\s>+~.[\]:'()"=\-\w]*){0,5})["']/g)) {
        const sel = m[1].trim();
        if (sel.length >= 4 && !sel.includes('\n')) {
          out.push({ selectorType: 'css', selector: sel,
                     methodName: this.nearestMethod(lines, i), className });
        }
      }
    }

    return out;
  }

  // ── Context helpers ────────────────────────────────────────────────────────

  private static nearestMethod(lines: string[], pos: number): string {
    for (let i = pos; i >= Math.max(0, pos - 20); i--) {
      const m = lines[i].match(/(?:public|private|protected)\s+\S+\s+(\w+)\s*\(/);
      if (m) return m[1];
    }
    return '';
  }

  private static nextFieldName(lines: string[], pos: number): string {
    for (let i = pos + 1; i < Math.min(lines.length, pos + 5); i++) {
      const m = lines[i].match(/(?:private|public|protected)?\s+\w+\s+(\w+)\s*[;=]/);
      if (m) return m[1];
    }
    return '';
  }

  // ── Relevance scoring ──────────────────────────────────────────────────────

  private static score(s: PoSelector, hash: string): number {
    const sel = s.selector.toLowerCase();
    const cls = s.className.toLowerCase();
    let n = 0;

    // Always include header/navigation selectors — needed for any page
    if (cls.includes('header'))     n += 2;
    if (cls.includes('gridtable'))  n += 1;

    // Page-specific relevance
    if (hash.startsWith('ip/catalog')  && (sel.includes('ip-catalog') || sel.includes('catalog'))) n += 5;
    if (hash.startsWith('ip/')         && (cls.includes('ip') || sel.includes('col-id')))           n += 3;
    if (hash.startsWith('library/catalog') && sel.includes('library-catalog'))                       n += 5;
    if (hash.startsWith('library/')    && cls.includes('library'))                                   n += 3;
    if (hash.startsWith('query')       && (cls.includes('search') || cls.includes('query')))         n += 4;
    if (hash.startsWith('label')       && (cls.includes('label') || sel.includes('label')))          n += 4;
    if (hash.startsWith('property')    && (cls.includes('property') || sel.includes('property')))    n += 4;
    if (hash.startsWith('propertysets')&& sel.includes('propertyset'))                               n += 5;
    if (hash.startsWith('queryfolder') && (cls.includes('folder') || sel.includes('folder')))        n += 4;
    if (hash.startsWith('geofencing')  && (cls.includes('geo') || sel.includes('geo')))              n += 4;
    if (hash.startsWith('shoppingcart')&& (cls.includes('shopping') || sel.includes('shopping')))    n += 4;
    if (hash.startsWith('ip/create')   && (cls.includes('create') || sel.includes('create')))        n += 4;

    return n;
  }
}
