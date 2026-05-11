import * as path from 'path';
import { BrowserManager } from '../driver/BrowserManager';
import { NavigationRouter } from '../pages/NavigationRouter';
import { MetricsCollector } from '../metrics/MetricsCollector';
import { LighthouseCollector } from '../metrics/LighthouseCollector';
import { NavigationAgent } from './NavigationAgent';
import { LoginPage } from '../pages/LoginPage';
import { PerformanceMetrics } from '../metrics/PerformanceMetrics';
import * as HtmlReportManager from '../reports/HtmlReportManager';
import { ExcelReportManager } from '../reports/ExcelReportManager';
import { ConfigReader } from '../config/ConfigReader';
import * as LogManager from '../utils/LogManager';

export interface PerformanceAnalysisResult {
  metrics: PerformanceMetrics;
  htmlReportPath: string;
  excelReportPath: string;
}

/**
 * PageLoadAnalyzerAgent — standalone agent that analyzes URLs without depending
 * on the Playwright Test fixture lifecycle.  Manages its own browser session.
 *
 * Usage:
 *   const result = await PageLoadAnalyzerAgent.analyze('https://example.com');
 *   const results = await PageLoadAnalyzerAgent.analyzeAll(['https://a.com', 'https://b.com']);
 */
/** Set to true before calling analyze/analyzeAll to skip the Lighthouse step. */
export let skipLighthouseAudit = false;
export function setSkipLighthouse(skip: boolean): void { skipLighthouseAudit = skip; }

/** Optional credential override — if set, used instead of config.properties values. */
let customCredentials: { username: string; password: string } | null = null;
export function setCustomCredentials(creds: { username: string; password: string } | null): void {
  customCredentials = creds;
}

export class PageLoadAnalyzerAgent {
  private PageLoadAnalyzerAgent() {}

  // ── Single URL ─────────────────────────────────────────────────────────────

  static async analyze(url: string, pageName?: string): Promise<PerformanceAnalysisResult> {
    const name      = pageName ?? derivePageName(url);
    const reportDir = ConfigReader.getInstance().getReportPath();
    LogManager.init(reportDir);

    console.info(`=== PageLoadAnalyzerAgent: ${name} [${url}] ===`);

    HtmlReportManager.initForPage(name);

    const metrics = await runMeasurement(url, name);

    HtmlReportManager.addMetrics(`Page Load Analysis: ${name}`, metrics);
    HtmlReportManager.addSummaryTable([metrics]);
    HtmlReportManager.flush();
    const htmlPath = HtmlReportManager.getReportPath();

    const ts         = tsStamp();
    const excelPath  = path.join(reportDir, `page-load-${safeName(name)}-${ts}.xlsx`);
    await ExcelReportManager.generate([metrics], excelPath);

    logConsoleSummary(metrics);
    console.info(`=== Analysis complete. Overall: ${metrics.overallStatus} ===`);
    const logPath = LogManager.flush();
    console.info(`=== Log saved: ${logPath} ===`);
    return { metrics, htmlReportPath: htmlPath, excelReportPath: excelPath };
  }

  // ── Multiple URLs ──────────────────────────────────────────────────────────

  static async analyzeAll(urls: string[]): Promise<PerformanceAnalysisResult[]> {
    const valid     = urls.map(u => u.trim()).filter(u => u.length > 0);
    const reportDir = ConfigReader.getInstance().getReportPath();
    LogManager.init(reportDir);

    console.info('╔══════════════════════════════════════════════════════════╗');
    console.info(`║   PageLoadAnalyzerAgent — ${valid.length} URL(s) to analyze`);
    console.info('╚══════════════════════════════════════════════════════════╝');

    const batchTs    = tsStamp();
    const results:    PerformanceAnalysisResult[] = [];
    const allMetrics: PerformanceMetrics[]         = [];

    for (let i = 0; i < valid.length; i++) {
      const url      = valid[i];
      const pageName = derivePageName(url);
      console.info(`[${i + 1}/${valid.length}] Analyzing: ${pageName} — ${url}`);

      HtmlReportManager.initForPage(pageName);
      const metrics = await runMeasurement(url, pageName);
      HtmlReportManager.addMetrics(`Page Load Analysis: ${pageName}`, metrics);
      HtmlReportManager.addSummaryTable([metrics]);
      HtmlReportManager.flush();
      const individualHtml = HtmlReportManager.getReportPath();

      allMetrics.push(metrics);
      logConsoleSummary(metrics);
      results.push({ metrics, htmlReportPath: individualHtml, excelReportPath: '' });
    }

    // Combined HTML
    HtmlReportManager.initCombined();
    allMetrics.forEach(m => {
      HtmlReportManager.addMetricsNoScreenshot(`Page Load Analysis: ${m.pageName}`, m);
    });
    HtmlReportManager.addSummaryTable(allMetrics);
    HtmlReportManager.flush();
    const combinedHtml = HtmlReportManager.getReportPath();

    // Combined Excel
    const combinedExcel = path.join(reportDir, `combined-analysis-${batchTs}.xlsx`);
    await ExcelReportManager.generate(allMetrics, combinedExcel);

    const logPath = LogManager.flush();

    console.info('══════════════════════════════════════════════════════════');
    console.info(`  All ${allMetrics.length} URL(s) analyzed.`);
    console.info(`  Combined HTML  : ${combinedHtml}`);
    console.info(`  Combined Excel : ${combinedExcel}`);
    console.info(`  Run Log        : ${logPath}`);
    console.info('══════════════════════════════════════════════════════════');

    return results.map(r => ({ ...r, excelReportPath: combinedExcel }));
  }
}

// ── Core measurement ─────────────────────────────────────────────────────────────

async function runMeasurement(url: string, pageName: string): Promise<PerformanceMetrics> {
  const config  = ConfigReader.getInstance();
  const session = await BrowserManager.launch();
  const { page } = session;

  try {
    const router    = new NavigationRouter(page);
    const collector = new MetricsCollector(page);

    const targetHashPath = NavigationRouter.extractHashPath(url);
    const isSigninPage   =
      targetHashPath.includes('signin') ||
      targetHashPath.includes('login')  ||
      targetHashPath.startsWith('security');

    if (isSigninPage) {
      // Signin page — measure it directly (no auth loop needed)
      console.info('[agent] Signin page — measuring directly');
      await collector.injectWebVitalsObserver();
      await page.goto(url, { waitUntil: 'domcontentloaded', timeout: config.getPageLoadTimeout() * 1000 });
      await router.waitForCurrentPage();
    } else {
      // ── Step 1: Always start from the signin page ──────────────────────────
      const loginUrl = config.getLoginUrl();
      console.info(`[agent] → Signin: ${loginUrl}`);
      await page.goto(loginUrl, { waitUntil: 'domcontentloaded', timeout: config.getPageLoadTimeout() * 1000 });
      await router.waitForCurrentPage();

      // ── Step 2: Login ──────────────────────────────────────────────────────
      console.info(`[agent] Logging in as '${customCredentials?.username || config.getUsername()}'`);
      const loginPage = new LoginPage(page, config.getPageLoadTimeout() * 1000);
      const username = customCredentials?.username || config.getUsername();
      const password = customCredentials?.password || config.getPassword();
      await loginPage.login(username, password);

      // ── Step 3: Wait for login redirect to complete ────────────────────────
      // waitForCurrentPage() uses URL-stability polling which returns after the
      // first stable interval — too fast if the login AJAX is still in flight.
      // Explicitly wait until the URL leaves the signin/login/security path.
      console.info('[agent] Waiting for post-login redirect...');
      const timeoutMs = config.getPageLoadTimeout() * 1000;
      try {
        await page.waitForURL(
          u => !u.href.includes('signin') && !u.href.includes('login') && !u.href.includes('security/'),
          { timeout: timeoutMs },
        );
      } catch {
        const stuck = page.url();
        console.warn(`[agent] ⚠ Login redirect timed out — still on "${stuck}"`);
        console.warn(`[agent]   Check credentials in config.properties or enter them in the Navigation Plan.`);
      }
      console.info(`[agent] Post-login URL: ${page.url()}`);

      // ── Step 4: Wait for home page readiness ──────────────────────────────
      console.info('[agent] Waiting for home page...');
      await router.waitForCurrentPage();
      console.info(`[agent] Home ready — ${page.url()}`);

      // ── Step 5: Inject observer BEFORE navigating to target ────────────────
      // Observer must be active from the very first paint of the target page.
      await collector.injectWebVitalsObserver();

      // ── Step 6: Navigate to target via UI actions only (strict — no goto) ──
      console.info(`[agent] Navigating to target via UI actions: ${url}`);
      console.info(`[agent] ${NavigationAgent.describeNavigation(url)}`);
      const navResult = await NavigationAgent.navigateTo(
        page, url, timeoutMs, /* strict= */ true,
      );
      navResult.log.forEach(line => console.info(line));

      // ── Step 7: Wait for page readiness ────────────────────────────────────
      await router.waitForCurrentPage();

      // ── Step 7b: Verify we landed on the target, not back on signin ────────
      const landedUrl = page.url();
      if (landedUrl.includes('signin') || landedUrl.includes('login') || landedUrl.includes('security/')) {
        console.warn(`[agent] ⚠ Auth redirect after navigation: landed on "${landedUrl}"`);
        console.warn(`[agent]   Expected target: "${url}"`);
        console.warn(`[agent]   Cause: invalid credentials, session timeout, or missing permissions.`);
      }
    }

    const metrics = await collector.collect(pageName);
    metrics.agGridLoadTime = router.getLastAgGridLoadTime();
    metrics.actionToLoadMs = router.getLastActionToLoadTime();

    // Extract session cookies for authenticated Lighthouse audits
    const sessionCookies = await session.context.cookies();

    // Run Lighthouse audit (launches its own Chrome — no port conflict with Playwright)
    if (!skipLighthouseAudit) try {
      const outputDir = path.join(config.getReportPath(), 'lighthouse');
      metrics.lighthouseData = await LighthouseCollector.run(url, {
        cookies:   sessionCookies,
        outputDir,
        pageName,
      });
    } catch (lhErr) {
      console.warn(`[agent] Lighthouse audit skipped: ${lhErr}`);
    }

    return metrics;
  } finally {
    await BrowserManager.close(session);
  }
}

// ── URL helpers ────────────────────────────────────────────────────────────────

export function derivePageName(url: string): string {
  try {
    const u = new URL(url);
    const host = u.hostname;
    const hashPath = NavigationRouter.extractHashPath(url);
    if (hashPath) {
      const last = NavigationRouter.extractSegment(url);
      if (last) return `${host} / ${last.charAt(0).toUpperCase()}${last.slice(1)}`;
    }
    const p = u.pathname.replace(/\/$/, '').replace(/.*\//, '');
    if (p) return `${host}/${p}`;
    return host || url;
  } catch {
    return url;
  }
}

// ── Console summary ────────────────────────────────────────────────────────────

function logConsoleSummary(m: PerformanceMetrics): void {
  console.info('┌──────────────────────────────────────────────────────────┐');
  console.info('│             PAGE LOAD PERFORMANCE ANALYSIS               │');
  console.info('├──────────────────────────────────────────────────────────┤');
  console.info(`│  URL       : ${m.pageUrl}`);
  console.info(`│  Page Name : ${m.pageName}`);
  console.info('├──────────────────────────┬────────────┬──────────────────┤');
  console.info('│  Metric                  │   Value    │  Status          │');
  console.info('├──────────────────────────┼────────────┼──────────────────┤');
  console.info(`│  Page Load Time          │ ${rpad(fmt(m.pageLoadTime), 7)} ms │  ${m.pageLoadStatus}`);
  console.info(`│  TTFB                    │ ${rpad(fmt(m.timeToFirstByte), 7)} ms │  ${m.ttfbStatus}`);
  console.info(`│  FCP                     │ ${rpad(fmt(m.firstContentfulPaint), 7)} ms │  ${m.fcpStatus}`);
  console.info(`│  LCP                     │ ${rpad(fmt(m.largestContentfulPaint), 7)} ms │  ${m.lcpStatus}`);
  console.info(`│  TTI                     │ ${rpad(fmt(m.timeToInteractive), 7)} ms │  ${m.ttiStatus}`);
  console.info(`│  CLS                     │ ${rpad(m.cumulativeLayoutShift.toFixed(4), 7)}    │  ${m.clsStatus}`);
  console.info('├──────────────────────────┼────────────┼──────────────────┤');
  console.info(`│  DNS Lookup              │ ${rpad(fmt(m.dnsLookupTime), 7)} ms │`);
  console.info(`│  TCP Connect             │ ${rpad(fmt(m.tcpConnectionTime), 7)} ms │`);
  console.info(`│  DOM Interactive         │ ${rpad(fmt(m.domInteractiveTime), 7)} ms │`);
  console.info(`│  DOM Content Loaded      │ ${rpad(fmt(m.domContentLoadedTime), 7)} ms │`);
  console.info(`│  Total Resources         │ ${rpad(String(m.totalResources), 7)}    │`);
  console.info(`│  Transfer Size           │ ${rpad(String(Math.round(m.transferSize / 1024)), 7)} KB │`);
  console.info('├──────────────────────────┴────────────┴──────────────────┤');
  console.info(`│  OVERALL STATUS : ${m.overallStatus}`);

  // ── Lighthouse section (only when audit ran) ─────────────────────────────
  const lh = m.lighthouseData;
  if (lh) {
    console.info('├──────────────────────────────────────────────────────────┤');
    console.info('│                  LIGHTHOUSE AUDIT                        │');
    console.info('├──────────────────────────┬────────────┬──────────────────┤');
    console.info(`│  Performance Score       │ ${rpad(String(lh.performanceScore), 7)}    │  ${scoreLabel(lh.performanceScore)}`);
    console.info(`│  Accessibility Score     │ ${rpad(String(lh.accessibilityScore), 7)}    │  ${scoreLabel(lh.accessibilityScore)}`);
    console.info(`│  Best Practices Score    │ ${rpad(String(lh.bestPracticesScore), 7)}    │  ${scoreLabel(lh.bestPracticesScore)}`);
    console.info(`│  SEO Score               │ ${rpad(String(lh.seoScore), 7)}    │  ${scoreLabel(lh.seoScore)}`);
    console.info('├──────────────────────────┼────────────┼──────────────────┤');
    console.info(`│  LH FCP                  │ ${rpad(fmt(lh.firstContentfulPaint), 7)} ms │`);
    console.info(`│  LH LCP                  │ ${rpad(fmt(lh.largestContentfulPaint), 7)} ms │`);
    console.info(`│  LH Speed Index          │ ${rpad(fmt(lh.speedIndex), 7)} ms │`);
    console.info(`│  LH TTI                  │ ${rpad(fmt(lh.timeToInteractive), 7)} ms │`);
    console.info(`│  LH TBT                  │ ${rpad(fmt(lh.totalBlockingTime), 7)} ms │`);
    console.info(`│  LH CLS                  │ ${rpad(lh.cumulativeLayoutShift.toFixed(4), 7)}    │`);
    if (lh.opportunities.length > 0) {
      console.info('├──────────────────────────┴────────────┴──────────────────┤');
      console.info('│  Top Opportunities                                        │');
      lh.opportunities.slice(0, 5).forEach(op => {
        const savings = op.savingsMs ? ` (save ~${Math.round(op.savingsMs)} ms)` : '';
        console.info(`│    • ${op.title}${savings}`);
      });
    }
    console.info('├──────────────────────────────────────────────────────────┤');
    console.info(`│  LH Report : ${lh.htmlReportPath}`);
  }

  console.info('└──────────────────────────────────────────────────────────┘');
}

function scoreLabel(score: number): string {
  if (score >= 90) return 'GOOD';
  if (score >= 50) return 'NEEDS IMPROVEMENT';
  return 'POOR';
}

function fmt(ms: number): string {
  return ms > 0 ? Math.round(ms).toString() : 'N/A';
}

function rpad(s: string, width: number): string {
  return s.padEnd(width);
}

function safeName(name: string): string {
  return name.replace(/[^a-zA-Z0-9._-]/g, '_');
}

function tsStamp(): string {
  return new Date().toISOString().replace(/[:.]/g, '-').replace('T', '_').slice(0, 23);
}
