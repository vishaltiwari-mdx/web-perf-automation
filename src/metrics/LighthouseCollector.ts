import * as fs   from 'fs';
import * as path from 'path';
import { LighthouseMetrics, LighthouseOpportunity, LighthouseDiagnostic } from './LighthouseMetrics';

export interface LighthouseCookie {
  name:   string;
  value:  string;
  domain: string;
}

export interface LighthouseRunOptions {
  cookies?:   LighthouseCookie[];
  outputDir?: string;
  pageName?:  string;
}

/**
 * LighthouseCollector — runs Google Lighthouse audits against a URL.
 *
 * Manages its own Chrome instance (separate from Playwright) so there are
 * no port conflicts.  Session cookies extracted from Playwright context can
 * be forwarded so Lighthouse can audit authenticated pages.
 */
export class LighthouseCollector {
  static async run(url: string, options: LighthouseRunOptions = {}): Promise<LighthouseMetrics> {
    // Dynamic imports — lighthouse v12 is ESM-only; dynamic import() works from CJS context.
    const chromeLauncherMod = await import('chrome-launcher');
    const launch = (chromeLauncherMod as any).launch ?? (chromeLauncherMod as any).default?.launch;
    const lighthouseMod = await import('lighthouse');
    const lighthouse: (...args: any[]) => Promise<any> =
      (lighthouseMod as any).default ?? (lighthouseMod as any);

    const outputDir = options.outputDir ?? path.join('reports', 'lighthouse');
    fs.mkdirSync(outputDir, { recursive: true });

    const safeName = (options.pageName ?? url).replace(/[^a-zA-Z0-9._-]/g, '_').slice(0, 80);
    const ts = new Date().toISOString().replace(/[:.]/g, '-').replace('T', '_').slice(0, 23);
    const htmlReportPath = path.join(outputDir, `lh-${safeName}-${ts}.html`);
    const jsonReportPath = path.join(outputDir, `lh-${safeName}-${ts}.json`);

    // Build Cookie header string from Playwright session cookies
    const cookieHeader = (options.cookies ?? [])
      .filter(c => c.name && c.value)
      .map(c => `${c.name}=${c.value}`)
      .join('; ');

    // Launch a dedicated Chrome (headless) — different port from Playwright
    const chrome = await launch({
      chromeFlags: [
        '--headless=new',
        '--no-sandbox',
        '--disable-gpu',
        '--disable-dev-shm-usage',
        '--disable-extensions',
      ],
    });

    try {
      console.info(`[Lighthouse] Auditing ${url} on port ${chrome.port} …`);

      const runnerResult = await lighthouse(url, {
        port:       chrome.port,
        output:     ['html', 'json'] as const,
        logLevel:   'error' as const,
        extraHeaders: cookieHeader ? { Cookie: cookieHeader } : undefined,
        onlyCategories: ['performance', 'accessibility', 'best-practices', 'seo'],
        formFactor: 'desktop',
        screenEmulation: {
          mobile:            false,
          width:             1920,
          height:            1080,
          deviceScaleFactor: 1,
          disabled:          false,
        },
        throttling: {
          // No throttling — measure actual desktop speed
          rttMs:                  0,
          throughputKbps:         0,
          cpuSlowdownMultiplier:  1,
          requestLatencyMs:       0,
          downloadThroughputKbps: 0,
          uploadThroughputKbps:   0,
        },
      });

      if (!runnerResult) {
        throw new Error(`Lighthouse returned no result for ${url}`);
      }

      const { lhr, report } = runnerResult as { lhr: any; report: string | string[] };

      // Persist HTML and JSON reports
      const reports = Array.isArray(report) ? report : [report, ''];
      if (reports[0]) fs.writeFileSync(htmlReportPath, reports[0], 'utf8');
      if (reports[1]) fs.writeFileSync(jsonReportPath, reports[1], 'utf8');

      const perfScore = Math.round((lhr.categories?.performance?.score ?? 0) * 100);
      console.info(`[Lighthouse] Done — Performance: ${perfScore}  HTML: ${htmlReportPath}`);

      return extractMetrics(lhr, htmlReportPath, jsonReportPath);
    } finally {
      // chrome-launcher may throw EPERM on Windows when removing the temp profile dir.
      // Suppress cleanup errors — the audit result is already captured above.
      try { await chrome.kill(); } catch { /* ignore cleanup errors on Windows */ }
    }
  }
}

// ── Extraction helpers ─────────────────────────────────────────────────────────

function extractMetrics(lhr: any, htmlReportPath: string, jsonReportPath: string): LighthouseMetrics {
  const numVal = (id: string): number => {
    const a = lhr.audits?.[id];
    return typeof a?.numericValue === 'number' ? a.numericValue : 0;
  };

  const catScore = (cat: string): number => {
    const s = lhr.categories?.[cat]?.score;
    return typeof s === 'number' ? Math.round(s * 100) : 0;
  };

  // Separate opportunity audits from diagnostic audits in the performance category
  const perfRefs: Array<{ id: string; group?: string }> =
    lhr.categories?.performance?.auditRefs ?? [];

  const opportunityIds = new Set(
    perfRefs.filter(r => r.group === 'load-opportunities').map(r => r.id),
  );
  const diagnosticIds = new Set(
    perfRefs.filter(r => r.group === 'diagnostics').map(r => r.id),
  );

  const opportunities: LighthouseOpportunity[] = [];
  for (const id of opportunityIds) {
    const audit = lhr.audits?.[id];
    if (!audit || audit.score === 1) continue;  // skip passing audits
    const savingsMs    = audit.details?.overallSavingsMs    ?? 0;
    const savingsBytes = audit.details?.overallSavingsBytes ?? 0;
    opportunities.push({
      id,
      title:        String(audit.title       ?? id),
      description:  String(audit.description ?? ''),
      savingsMs:    savingsMs    > 0 ? Math.round(savingsMs)    : undefined,
      savingsBytes: savingsBytes > 0 ? Math.round(savingsBytes) : undefined,
      displayValue: audit.displayValue != null ? String(audit.displayValue) : undefined,
    });
  }

  const diagnostics: LighthouseDiagnostic[] = [];
  for (const id of diagnosticIds) {
    const audit = lhr.audits?.[id];
    if (!audit) continue;
    diagnostics.push({
      id,
      title:        String(audit.title       ?? id),
      description:  String(audit.description ?? ''),
      displayValue: audit.displayValue != null ? String(audit.displayValue) : undefined,
      score:        typeof audit.score === 'number' ? audit.score : null,
    });
  }

  return {
    performanceScore:   catScore('performance'),
    accessibilityScore: catScore('accessibility'),
    bestPracticesScore: catScore('best-practices'),
    seoScore:           catScore('seo'),

    firstContentfulPaint:   numVal('first-contentful-paint'),
    largestContentfulPaint: numVal('largest-contentful-paint'),
    speedIndex:             numVal('speed-index'),
    timeToInteractive:      numVal('interactive'),
    totalBlockingTime:      numVal('total-blocking-time'),
    cumulativeLayoutShift:  numVal('cumulative-layout-shift'),
    firstMeaningfulPaint:   numVal('first-meaningful-paint'),
    serverResponseTime:     numVal('server-response-time'),

    mainThreadWork: numVal('mainthread-work-breakdown'),
    bootupTime:     numVal('bootup-time'),
    domSize:        numVal('dom-size'),

    renderBlockingResourcesSavingsMs:
      lhr.audits?.['render-blocking-resources']?.details?.overallSavingsMs ?? 0,
    unusedJavascriptBytes:
      lhr.audits?.['unused-javascript']?.details?.overallSavingsBytes ?? 0,
    unusedCssBytes:
      lhr.audits?.['unused-css-rules']?.details?.overallSavingsBytes ?? 0,

    opportunities,
    diagnostics,
    htmlReportPath,
    jsonReportPath,
    auditTime: Date.now(),
  };
}
