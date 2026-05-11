/**
 * HelixPagePerformanceTest — targeted performance tests for three specific pages:
 *
 *   1. Sign-in  →  http://vishal.mdx.perforce.com:3000/#/security/signin
 *   2. Home     →  http://vishal.mdx.perforce.com:3000/#/home
 *   3. IP Catalog → http://vishal.mdx.perforce.com:3000/#/ip/catalog
 *
 * URLs are read from config.properties (app.base.url).
 *
 * Run all:   npx playwright test tests/HelixPagePerformanceTest.spec.ts
 * Run one:   npx playwright test --grep "IP Catalog"
 */
import { test, expect } from './fixtures/performanceFixtures';
import { ConfigReader } from '../src/config/ConfigReader';
import { LoginPage } from '../src/pages/LoginPage';
import * as HtmlReportManager from '../src/reports/HtmlReportManager';

const config = ConfigReader.getInstance();
const BASE    = config.getBaseUrl();

const SIGNIN_URL     = `${BASE}/#/security/signin`;
const HOME_URL       = `${BASE}/#/home`;
const IP_CATALOG_URL = `${BASE}/#/ip/catalog`;

test.describe('Helix IPLM Page Performance', () => {

  // ── Test 1: Sign-in page ────────────────────────────────────────────────────

  test('Sign-in page performance', async ({ router, collector }) => {
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(SIGNIN_URL);

    const metrics = await collector.collect('Sign-in Page');
    metrics.agGridLoadTime = router.getLastAgGridLoadTime();
    HtmlReportManager.addMetrics('Sign-in Page Performance', metrics);

    expect(metrics).toBeTruthy();
    expect(metrics.pageLoadTime).toBeGreaterThan(0);
    expect(metrics.timeToFirstByte).toBeGreaterThan(0);
    expect(metrics.firstContentfulPaint).toBeGreaterThan(0);
  });

  // ── Test 2: Home page (requires login) ─────────────────────────────────────

  test('Home page performance', async ({ page, router, collector }) => {
    // Step 1: Sign-in page
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(SIGNIN_URL);

    // Step 2: Authenticate
    await new LoginPage(page, config.getPageLoadTimeout() * 1000)
      .login(config.getUsername(), config.getPassword());

    // Step 3: Inject observer early, then wait for post-login home page
    await collector.injectWebVitalsObserver();
    await router.waitForCurrentPage();

    const metrics = await collector.collect('Home Page');
    metrics.agGridLoadTime = router.getLastAgGridLoadTime();
    HtmlReportManager.addMetrics('Home Page Performance', metrics);

    expect(metrics).toBeTruthy();
    expect(metrics.pageLoadTime).toBeGreaterThan(0);
    expect(metrics.firstContentfulPaint).toBeGreaterThan(0);
  });

  // ── Test 3: IP Catalog (requires login) ────────────────────────────────────

  test('IP Catalog page performance', async ({ page, router, collector }) => {
    // Step 1: Sign-in page
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(SIGNIN_URL);

    // Step 2: Authenticate
    await new LoginPage(page, config.getPageLoadTimeout() * 1000)
      .login(config.getUsername(), config.getPassword());

    // Step 3: Wait for post-login home page
    await router.waitForCurrentPage();

    // Step 4: Inject observer BEFORE navigating to IP Catalog
    await collector.injectWebVitalsObserver();
    await router.navigateAndPrepare(IP_CATALOG_URL);

    const metrics = await collector.collect('IP Catalog');
    metrics.agGridLoadTime = router.getLastAgGridLoadTime();
    HtmlReportManager.addMetrics('IP Catalog Performance', metrics);

    expect(metrics).toBeTruthy();
    expect(metrics.pageLoadTime).toBeGreaterThan(0);
    expect(metrics.firstContentfulPaint).toBeGreaterThan(0);
  });
});
