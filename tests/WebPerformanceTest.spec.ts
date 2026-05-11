/**
 * WebPerformanceTest — main performance test suite.
 *
 * Equivalent to the Java WebPerformanceTest.java (TestNG → Playwright Test).
 *
 * Tests:
 *   1. Login page performance
 *   2. Full app flow (Login → Home → configured screens)
 *   3. Multi-run benchmark (N runs, averages)
 *
 * Configure screens in config.properties:
 *   app.screens.names=Home,IP catalog
 *   app.screens.urls=http://host/#/home,http://host/#/ip/catalog
 */
import { test, expect } from './fixtures/performanceFixtures';
import { ConfigReader } from '../src/config/ConfigReader';
import { LoginPage } from '../src/pages/LoginPage';
import * as HtmlReportManager from '../src/reports/HtmlReportManager';
import { ExcelReportManager } from '../src/reports/ExcelReportManager';
import { PerformanceMetrics } from '../src/metrics/PerformanceMetrics';

const config = ConfigReader.getInstance();

test.describe('Web Performance Benchmarks', () => {

  // ── Test 1: Login page ──────────────────────────────────────────────────────

  test('Login page performance', async ({ router, collector }) => {
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(config.getLoginUrl());

    const metrics = await collector.collect('Login Page');
    metrics.agGridLoadTime = router.getLastAgGridLoadTime();
    HtmlReportManager.addMetrics('Login Page Performance', metrics);

    expect(metrics).toBeTruthy();
    expect(metrics.pageUrl).toBeTruthy();
  });

  // ── Test 2: Full app flow ───────────────────────────────────────────────────

  test('Full app flow performance', async ({ page, router, collector }) => {
    const allMetrics: PerformanceMetrics[] = [];

    // STEP 1: Login page
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(config.getLoginUrl());

    const loginMetrics = await collector.collect('Login Page');
    loginMetrics.agGridLoadTime = router.getLastAgGridLoadTime();
    allMetrics.push(loginMetrics);
    HtmlReportManager.addMetrics('Login Page', loginMetrics);

    // STEP 2: Perform login
    const loginPage = new LoginPage(page, config.getPageLoadTimeout() * 1000);
    await loginPage.login(config.getUsername(), config.getPassword());

    // STEP 3: Home page (post-login redirect)
    await collector.injectWebVitalsObserver();
    await router.waitForCurrentPage();

    const homeMetrics = await collector.collect('Home Page');
    homeMetrics.agGridLoadTime = router.getLastAgGridLoadTime();
    allMetrics.push(homeMetrics);
    HtmlReportManager.addMetrics('Home Page (Post-Login)', homeMetrics);
    expect(homeMetrics.pageLoadTime).toBeGreaterThan(0);

    // STEP 4: Additional configured screens
    const screenNames = config.getOrDefault('app.screens.names', '').split(',');
    const screenUrls  = config.getOrDefault('app.screens.urls',  '').split(',');

    for (let i = 0; i < screenNames.length && i < screenUrls.length; i++) {
      const name = screenNames[i].trim();
      const url  = screenUrls[i].trim();
      if (!name || !url) continue;

      await collector.injectWebVitalsObserver();
      await router.navigateAndPrepare(url);

      const screenMetrics = await collector.collect(name);
      screenMetrics.agGridLoadTime = router.getLastAgGridLoadTime();
      allMetrics.push(screenMetrics);
      HtmlReportManager.addMetrics(name, screenMetrics);
    }

    // STEP 5: Reports
    HtmlReportManager.addSummaryTable(allMetrics);
    await ExcelReportManager.generate(
      allMetrics,
      `${config.getReportPath()}/performance-report.xlsx`,
    );

    const poorCount = allMetrics.filter(m => m.overallStatus === 'POOR').length;
    if (poorCount > 0) {
      console.warn(`${poorCount} page(s) have POOR performance — review the HTML report`);
    }

    expect(allMetrics.length).toBeGreaterThan(0);
  });

  // ── Test 3: Multi-run benchmark ─────────────────────────────────────────────

  test('Multi-run performance benchmark', async ({ page, router, collector }) => {
    const runs       = config.getPerformanceRuns();
    const runMetrics: PerformanceMetrics[] = [];

    for (let run = 1; run <= runs; run++) {
      await collector.injectWebVitalsObserver();
      await router.navigateAndPrepare(config.getLoginUrl());

      const m = await collector.collect(`Login Page - Run ${run}`);
      m.agGridLoadTime = router.getLastAgGridLoadTime();
      runMetrics.push(m);
      HtmlReportManager.addMetrics(`Benchmark Run ${run}`, m);

      if (run < runs) {
        await page.waitForTimeout(1000);
        await page.reload({ waitUntil: 'domcontentloaded' });
      }
    }

    const avg = (fn: (m: PerformanceMetrics) => number) =>
      runMetrics.reduce((s, m) => s + fn(m), 0) / runs;

    const avgLoad = avg(m => m.pageLoadTime);
    const avgFcp  = avg(m => m.firstContentfulPaint);
    const avgLcp  = avg(m => m.largestContentfulPaint);
    const avgCls  = avg(m => m.cumulativeLayoutShift);
    const avgTtfb = avg(m => m.timeToFirstByte);

    console.info(`=== BENCHMARK AVERAGES (${runs} runs) ===`);
    console.info(`Avg Page Load : ${Math.round(avgLoad)} ms`);
    console.info(`Avg FCP       : ${Math.round(avgFcp)} ms`);
    console.info(`Avg LCP       : ${Math.round(avgLcp)} ms`);
    console.info(`Avg CLS       : ${avgCls.toFixed(3)}`);
    console.info(`Avg TTFB      : ${Math.round(avgTtfb)} ms`);

    await ExcelReportManager.generate(
      runMetrics,
      `${config.getReportPath()}/benchmark-multirun.xlsx`,
    );

    expect(avgLoad).toBeGreaterThan(0);
  });
});
