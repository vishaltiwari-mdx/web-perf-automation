/**
 * DynamicUrlTest — Playwright test driven by TARGET_URLS env var.
 *
 * Report strategy:
 *   • Each URL gets its own HTML report named after the page
 *     (e.g. performance-report-ip_catalog-<ts>.html)
 *   • When > 1 URL is analyzed a combined report is also produced
 *     (performance-report-combined-<ts>.html)
 *   • Lighthouse reports are written to reports/lighthouse/ and are NOT
 *     affected by this grouping
 *
 * WHY this file manages its own report lifecycle (beforeAll / afterAll):
 *   Playwright runs globalSetup + globalTeardown in the MAIN process and each
 *   test file in a separate WORKER process.  Module-level state (HtmlReport,
 *   LogManager) is NOT shared across those process boundaries.
 *
 * Env vars:
 *   TARGET_URLS    comma-separated URLs  (required)
 *   BATCH_ID       unique run ID so server can locate the results JSON
 *   METRICS_TYPE   'playwright' | 'both'  (default: 'both')
 *   TEST_USERNAME  credential override (falls back to config.properties)
 *   TEST_PASSWORD  credential override (falls back to config.properties)
 */

import * as fs   from 'fs';
import * as path from 'path';
import { test, expect } from './fixtures/performanceFixtures';
import { LoginPage }           from '../src/pages/LoginPage';
import { LighthouseCollector } from '../src/metrics/LighthouseCollector';
import { ConfigReader }        from '../src/config/ConfigReader';
import { PerformanceMetrics }  from '../src/metrics/PerformanceMetrics';
import { ExcelReportManager }  from '../src/reports/ExcelReportManager';
import * as HtmlReportManager  from '../src/reports/HtmlReportManager';
import * as LogManager         from '../src/utils/LogManager';
import { BrowserManager }      from '../src/driver/BrowserManager';
import { NavigationRouter }    from '../src/pages/NavigationRouter';
import { MetricsCollector }    from '../src/metrics/MetricsCollector';

// ── Worker-local state ────────────────────────────────────────────────────────

interface UrlResult {
  metrics:    PerformanceMetrics;
  htmlReport: string;   // path to this URL's individual HTML report
}

const collected: UrlResult[] = [];

// ── Report lifecycle — runs entirely in the worker process ────────────────────

test.beforeAll(async () => {
  const config    = ConfigReader.getInstance();
  const reportDir = config.getReportPath();

  LogManager.init(reportDir);

  // Custom metric selection — comma-separated keys (e.g. "LCP,CLS,API")
  const selected = (process.env['SELECTED_METRICS'] ?? '')
    .split(',').map(s => s.trim()).filter(Boolean);
  HtmlReportManager.setSelectedMetrics(selected.length ? selected : null);

  console.info('[DynamicUrlTest] ══ Worker initialised ════════════════════════');
  console.info(`[DynamicUrlTest] TARGET_URLS      : ${process.env['TARGET_URLS'] ?? '(none)'}`);
  console.info(`[DynamicUrlTest] METRICS_TYPE     : ${process.env['METRICS_TYPE'] ?? 'both (default)'}`);
  console.info(`[DynamicUrlTest] SELECTED_METRICS : ${selected.length ? selected.join(', ') : '(all)'}`);
  console.info(`[DynamicUrlTest] BATCH_ID         : ${process.env['BATCH_ID'] ?? '(none)'}`);
});

test.afterAll(async () => {
  if (collected.length === 0) {
    console.warn('[DynamicUrlTest] afterAll: no metrics collected — skipping report');
    LogManager.flush();
    return;
  }

  const config    = ConfigReader.getInstance();
  const batchId   = process.env['BATCH_ID'] ?? Date.now().toString();
  const allMetrics = collected.map(r => r.metrics);

  // ── Combined HTML report (only when > 1 URL) ────────────────────────────
  let combinedHtmlReport = collected[0].htmlReport;   // for single URL, point to its own report

  if (collected.length > 1) {
    HtmlReportManager.initCombined();
    allMetrics.forEach(m =>
      HtmlReportManager.addMetricsNoScreenshot(`${m.pageName} — Performance`, m),
    );
    HtmlReportManager.addSummaryTable(allMetrics);
    HtmlReportManager.flush();
    combinedHtmlReport = HtmlReportManager.getReportPath();
    console.info(`[DynamicUrlTest] Combined report → ${combinedHtmlReport}`);
  }

  // ── Excel (single file covering all URLs) ───────────────────────────────
  const excelPath = path.join(config.getReportPath(), `performance-report-${batchId}.xlsx`);
  await ExcelReportManager.generate(allMetrics, excelPath);

  // ── Flush log AFTER everything else ─────────────────────────────────────
  const logReport = LogManager.flush();

  // ── Results JSON — server.ts reads this after the Playwright process exits
  const resultsPath = path.join(config.getReportPath(), `dynamic-results-${batchId}.json`);
  fs.writeFileSync(resultsPath, JSON.stringify({
    htmlReport:  combinedHtmlReport,   // combined (or single) — used as the main dashboard link
    excelReport: excelPath,
    logReport,
    urls: collected.map(r => {
      const m = r.metrics;
      return {
        url:           m.pageUrl,
        name:          m.pageName,
        htmlReport:    r.htmlReport,                          // individual report for this URL
        overallStatus: m.overallStatus,
        lhScore:       m.lighthouseData?.performanceScore ?? null,
        lhReport:      m.lighthouseData?.htmlReportPath   ?? null,
        metrics: {
          'PLT (Page Load Time)':            Math.round(m.pageLoadTime),
          'FCP (First Contentful Paint)':    Math.round(m.firstContentfulPaint),
          'LCP (Largest Contentful Paint)':  Math.round(m.largestContentfulPaint),
          'TTI (Time to Interactive)':       Math.round(m.timeToInteractive),
          'CLS (Cumulative Layout Shift)':   m.cumulativeLayoutShift?.toFixed(4) ?? '0',
          'TTFB (Time to First Byte)':       Math.round(m.timeToFirstByte),
          'TBT (Total Blocking Time)':       Math.round(m.totalBlockingTime),
          'INP (Interaction to Next Paint)': Math.round(m.interactionToNextPaint),
          'DNS (DNS Lookup Time)':           Math.round(m.dnsLookupTime),
          'TCP (TCP Connection Time)':       Math.round(m.tcpConnectionTime),
          'DCL (DOM Content Loaded)':        Math.round(m.domContentLoadedTime),
          'AGG (AG-Grid Spinner Time)':      m.agGridLoadTime > 0 ? Math.round(m.agGridLoadTime) : 'N/A',
        },
      };
    }),
  }, null, 2), 'utf8');

  console.info(`[DynamicUrlTest] Excel report → ${excelPath}`);
  console.info(`[DynamicUrlTest] Results JSON → ${resultsPath}`);
  collected.forEach(r =>
    console.info(`[DynamicUrlTest] Report [${r.metrics.pageName}] → ${r.htmlReport}`),
  );
});

// ── Main test ─────────────────────────────────────────────────────────────────

test('Dynamic URL Performance', async () => {
  const config      = ConfigReader.getInstance();
  const rawUrls     = (process.env['TARGET_URLS'] ?? '').split(',').map(u => u.trim()).filter(Boolean);
  const metricsType = (process.env['METRICS_TYPE'] ?? 'both') as 'playwright' | 'both' | 'custom';
  const testUser    = process.env['TEST_USERNAME'] ?? config.getUsername();
  const testPass    = process.env['TEST_PASSWORD'] ?? config.getPassword();
  const timeoutMs   = config.getPageLoadTimeout() * 1000;

  if (rawUrls.length === 0) {
    console.warn('[DynamicUrlTest] ⚠ TARGET_URLS is not set — nothing to analyze');
    return;
  }

  // ── Analyze each URL in its own browser session ───────────────────────────
  for (let i = 0; i < rawUrls.length; i++) {
    const url      = rawUrls[i];
    const pageName = deriveName(url);
    const isSignin = /signin|login|security\//i.test(url);

    console.info(`\n[DynamicUrlTest] ── [${i + 1}/${rawUrls.length}] ${pageName} ──────`);
    console.info(`[DynamicUrlTest] URL: ${url}`);

    // Individual HTML report for this URL
    HtmlReportManager.initForPage(pageName);

    // Launch a fresh browser for this URL
    const session   = await BrowserManager.launch();
    const { page }  = session;
    const router    = new NavigationRouter(page);
    const collector = new MetricsCollector(page);

    try {
      if (isSignin) {
        console.info('[DynamicUrlTest] Signin page — measuring directly');
        await collector.injectWebVitalsObserver();
        await router.navigateAndPrepare(url);

      } else {
        // Step 1: Signin
        console.info(`[DynamicUrlTest] Step 1 — Signin: ${config.getLoginUrl()}`);
        await router.navigateAndPrepare(config.getLoginUrl());

        // Step 2: Login
        console.info(`[DynamicUrlTest] Step 2 — Login as '${testUser}'`);
        await new LoginPage(page, timeoutMs).login(testUser, testPass);

        // Step 3: Wait for redirect off signin path
        console.info('[DynamicUrlTest] Step 3 — Waiting for redirect...');
        await page.waitForURL(
          u => !u.href.includes('signin') && !u.href.includes('login') && !u.href.includes('security/'),
          { timeout: timeoutMs },
        ).catch(() => console.warn('[DynamicUrlTest] ⚠ Login redirect timed out'));

        await router.waitForCurrentPage();
        console.info(`[DynamicUrlTest] Post-login URL: ${page.url()}`);

        // Step 4: Inject observer BEFORE navigating (captures LCP/CLS from first paint)
        await collector.injectWebVitalsObserver();

        // Step 5: Navigate via Navigation Plan
        console.info('[DynamicUrlTest] Step 5 — Navigating via Navigation Plan');
        await router.navigateAndPrepare(url);
      }

      // Collect Playwright metrics
      const metrics = await collector.collect(pageName);
      metrics.agGridLoadTime  = router.getLastAgGridLoadTime();
      metrics.actionToLoadMs  = router.getLastActionToLoadTime();

      // Optional Lighthouse audit (runs its own Chrome — no port conflict)
      if (metricsType === 'both') {
        console.info('[DynamicUrlTest] Running Lighthouse audit...');
        try {
          const cookies   = await page.context().cookies();
          const outputDir = path.join(config.getReportPath(), 'lighthouse');
          metrics.lighthouseData = await LighthouseCollector.run(url, { cookies, outputDir, pageName });
          console.info(`[DynamicUrlTest] Lighthouse score: ${metrics.lighthouseData.performanceScore}`);
        } catch (e) {
          console.warn(`[DynamicUrlTest] Lighthouse skipped: ${e}`);
        }
      }

      // Write individual HTML report for this URL
      HtmlReportManager.addMetrics(`${pageName} — Performance`, metrics);
      HtmlReportManager.addSummaryTable([metrics]);
      HtmlReportManager.flush();
      const individualReport = HtmlReportManager.getReportPath();

      collected.push({ metrics, htmlReport: individualReport });

      console.info(`[DynamicUrlTest] ✓ ${pageName} | Load: ${Math.round(metrics.pageLoadTime)} ms | FCP: ${Math.round(metrics.firstContentfulPaint)} ms | LCP: ${Math.round(metrics.largestContentfulPaint)} ms | ${metrics.overallStatus}`);
      console.info(`[DynamicUrlTest] Individual report → ${individualReport}`);

    } catch (e) {
      console.error(`[DynamicUrlTest] ✗ ${pageName}: ${e}`);
    } finally {
      console.info(`[DynamicUrlTest] Closing browser for: ${pageName}`);
      await BrowserManager.close(session).catch(() => {});
    }

    // 500 ms pause between URLs (skip after the last one)
    if (i < rawUrls.length - 1) {
      console.info('[DynamicUrlTest] Waiting 500 ms before next URL...');
      await new Promise(resolve => setTimeout(resolve, 500));
    }
  }

  expect(collected.length, 'At least one URL must be analyzed successfully').toBeGreaterThan(0);
});

// ── Helper ────────────────────────────────────────────────────────────────────

function deriveName(url: string): string {
  try {
    const u = new URL(url);
    const h = u.hash.replace(/^#\//, '').split('?')[0];
    if (h) return h.replace(/\//g, ' / ');
    const p = u.pathname.replace(/\/$/, '').replace(/.*\//, '');
    return p || u.hostname;
  } catch {
    return url;
  }
}
