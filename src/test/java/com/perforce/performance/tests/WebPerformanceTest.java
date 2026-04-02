package com.perforce.performance.tests;

import com.perforce.performance.config.ConfigReader;
import com.perforce.performance.metrics.MetricsCollector;
import com.perforce.performance.metrics.PerformanceMetrics;
import com.perforce.performance.pages.NavigationRouter;
import com.perforce.performance.pages.LoginPage;
import com.perforce.performance.reports.ExcelReportManager;
import com.perforce.performance.reports.HtmlReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * WebPerformanceTest — Main performance test suite.
 *
 * Navigation contract
 * ───────────────────
 * Every page navigation goes through {@link NavigationRouter#navigateAndPrepare(String)}
 * or {@link NavigationRouter#waitForCurrentPage()}.  The router:
 *
 *   1. Extracts the hash path from the URL   (e.g. "ip/catalog", "security/signin")
 *   2. Looks up a registered handler for that path (or last segment as fallback)
 *   3. Runs the handler to completion — waits for heading, spinners gone, DOM ready
 *   4. Returns  →  performance measurement starts here
 *
 * The Web Vitals observer is injected BEFORE navigation so LCP/CLS events
 * are captured from the very first paint.
 *
 * Built-in handlers (Helix IPLM locators):
 *   security/signin     login form visible
 *   home / dashboard    title contains "Home" + IPs button visible
 *   ip/catalog          "IP catalog" heading + AG-Grid spinner gone
 *   library/catalog     "Library catalog" heading + spinner gone
 *   library/manage      "Library" heading + spinner gone
 *   query/search        "Advanced search" heading
 *   labels/manage       "Labels" heading
 *   property/manage     "Property" heading
 *   propertysets/manage "Property sets" heading
 *
 * Adding a custom handler for a new screen:
 *   In buildRouter() add:
 *     router.register("my-screen", d ->
 *         new WebDriverWait(d, Duration.ofSeconds(20))
 *             .until(ExpectedConditions.visibilityOfElementLocated(
 *                 By.cssSelector(".my-screen-header"))));
 *
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║ CONFIGURE: Update config.properties with your app details    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */
public class WebPerformanceTest extends BaseTest {

    private final ConfigReader config = ConfigReader.getInstance();

    // ── Router factory ────────────────────────────────────────────────────────

    /**
     * Build and configure the NavigationRouter.
     * Register custom screen handlers here for any URL in app.screens.urls.
     *
     * Example — wait for a custom "ip-catalog" table before measuring:
     *
     *   router.register("ip/catalog", d -> {
     *       new WebDriverWait(d, Duration.ofSeconds(20))
     *           .until(ExpectedConditions.visibilityOfElementLocated(
     *               By.xpath("//div[contains(@class,'page-name')][normalize-space()='IP catalog']")));
     *   });
     */
    private NavigationRouter buildRouter() {
        NavigationRouter router = new NavigationRouter(driver);

        // ── Add custom screen handlers below ──────────────────────────────────
        // router.register("ip/catalog",          d -> { ... });
        // router.register("library/catalog",     d -> { ... });
        // router.register("query/search",        d -> { ... });
        // ─────────────────────────────────────────────────────────────────────

        return router;
    }

    // ── Test 1: Login page performance ────────────────────────────────────────

    @Test(description = "Measure performance of the Login page load")
    public void testLoginPagePerformance() {
        NavigationRouter router    = buildRouter();
        MetricsCollector collector = new MetricsCollector(driver);

        log.info("STEP 1 — Login page: {}", config.getLoginUrl());

        // Inject observer BEFORE navigation so first-paint events are captured
        collector.injectWebVitalsObserver();

        // Router: navigate + wait for signin input visible
        router.navigateAndPrepare(config.getLoginUrl());

        PerformanceMetrics metrics = collector.collect("Login Page");
        metrics.setAgGridLoadTime(router.getLastAgGridLoadTime());
        HtmlReportManager.addMetrics("Login Page Performance", metrics);

        Assert.assertNotNull(metrics, "Metrics should not be null");
        log.info("Login Page → {}", metrics);
    }

    // ── Test 2: Full flow ─────────────────────────────────────────────────────

    @Test(description = "Full flow: Login → Home → Additional Screens")
    public void testFullAppFlowPerformance() {
        List<PerformanceMetrics> allMetrics = new ArrayList<>();
        NavigationRouter  router    = buildRouter();
        MetricsCollector  collector = new MetricsCollector(driver);

        // ── STEP 1: Login page ─────────────────────────────────────────────
        log.info("STEP 1 — Loading Login page");
        collector.injectWebVitalsObserver();
        router.navigateAndPrepare(config.getLoginUrl());

        PerformanceMetrics loginMetrics = collector.collect("Login Page");
        loginMetrics.setAgGridLoadTime(router.getLastAgGridLoadTime());
        allMetrics.add(loginMetrics);
        HtmlReportManager.addMetrics("Login Page", loginMetrics);

        // ── STEP 2: Perform login ──────────────────────────────────────────
        log.info("STEP 2 — Logging in as: {}", config.getUsername());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(config.getUsername(), config.getPassword());

        // ── STEP 3: Home page (post-login redirect) ────────────────────────
        // Inject observer immediately after login click so LCP/CLS events
        // for the home page are captured from the first render frame.
        collector.injectWebVitalsObserver();

        // waitForCurrentPage() polls until the URL stabilises (redirect done),
        // then runs the matching handler — for "#/home" that waits for the
        // dashboard IPs button and confirms the title contains "Home".
        log.info("STEP 3 — Waiting for post-login home page");
        router.waitForCurrentPage();

        PerformanceMetrics homeMetrics = collector.collect("Home Page");
        homeMetrics.setAgGridLoadTime(router.getLastAgGridLoadTime());
        allMetrics.add(homeMetrics);
        HtmlReportManager.addMetrics("Home Page (Post-Login)", homeMetrics);
        Assert.assertTrue(homeMetrics.getPageLoadTime() > 0, "Home page load time should be > 0");

        // ── STEP 4: Additional screens ─────────────────────────────────────
        String[] screenNames = config.get("app.screens.names", "").split(",");
        String[] screenUrls  = config.get("app.screens.urls",  "").split(",");

        for (int i = 0; i < screenNames.length && i < screenUrls.length; i++) {
            String name = screenNames[i].trim();
            String url  = screenUrls[i].trim();
            if (name.isEmpty() || url.isEmpty()) continue;

            String path = NavigationRouter.extractSegment(url);
            log.info("STEP {} — Screen '{}' (path: '{}')  →  {}", (4 + i), name, path, url);

            // Inject observer BEFORE navigating so Web Vitals are captured
            // from the very first paint on the destination page.
            collector.injectWebVitalsObserver();

            // Router: navigate + wait for this screen's page-name heading
            //         and spinner to disappear, THEN return.
            router.navigateAndPrepare(url);

            PerformanceMetrics screenMetrics = collector.collect(name);
            screenMetrics.setAgGridLoadTime(router.getLastAgGridLoadTime());
            allMetrics.add(screenMetrics);
            HtmlReportManager.addMetrics(name, screenMetrics);
        }

        // ── STEP 5: Reports ────────────────────────────────────────────────
        log.info("STEP 5 — Generating reports");
        HtmlReportManager.addSummaryTable(allMetrics);
        ExcelReportManager.generate(allMetrics, config.getReportPath() + "/performance-report.xlsx");

        for (PerformanceMetrics m : allMetrics) log.info("RESULT: {}", m);

        long poorCount = allMetrics.stream()
                .filter(m -> "POOR".equals(m.getOverallStatus())).count();
        if (poorCount > 0) {
            log.warn("{} page(s) have POOR performance — review the HTML report", poorCount);
        }

        Assert.assertTrue(allMetrics.size() > 0, "At least one page should have been measured");
    }

    // ── Test 3: Multi-run benchmark ───────────────────────────────────────────

    @Test(description = "Multi-run performance benchmark (averages over N runs)")
    public void testPerformanceBenchmarkMultiRun() {
        int runs = config.getPerformanceRuns();
        log.info("Benchmark: {} runs for Login page", runs);

        NavigationRouter router    = buildRouter();
        MetricsCollector collector = new MetricsCollector(driver);
        List<PerformanceMetrics> runMetrics = new ArrayList<>();

        for (int run = 1; run <= runs; run++) {
            log.info("Benchmark run {}/{}", run, runs);

            collector.injectWebVitalsObserver();

            // Router handles navigation AND confirms page ready before collection
            router.navigateAndPrepare(config.getLoginUrl());

            PerformanceMetrics m = collector.collect("Login Page - Run " + run);
            m.setAgGridLoadTime(router.getLastAgGridLoadTime());
            runMetrics.add(m);
            HtmlReportManager.addMetrics("Benchmark Run " + run, m);

            if (run < runs) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                driver.navigate().refresh();
            }
        }

        double avgLoad = runMetrics.stream().mapToDouble(PerformanceMetrics::getPageLoadTime).average().orElse(0);
        double avgFcp  = runMetrics.stream().mapToDouble(PerformanceMetrics::getFirstContentfulPaint).average().orElse(0);
        double avgLcp  = runMetrics.stream().mapToDouble(PerformanceMetrics::getLargestContentfulPaint).average().orElse(0);
        double avgCls  = runMetrics.stream().mapToDouble(PerformanceMetrics::getCumulativeLayoutShift).average().orElse(0);
        double avgTtfb = runMetrics.stream().mapToDouble(PerformanceMetrics::getTimeToFirstByte).average().orElse(0);

        log.info("=== BENCHMARK AVERAGES ({} runs) ===", runs);
        log.info("Avg Page Load : {} ms", String.format("%.0f", avgLoad));
        log.info("Avg FCP       : {} ms", String.format("%.0f", avgFcp));
        log.info("Avg LCP       : {} ms", String.format("%.0f", avgLcp));
        log.info("Avg CLS       : {}",    String.format("%.3f", avgCls));
        log.info("Avg TTFB      : {} ms", String.format("%.0f", avgTtfb));

        ExcelReportManager.generate(runMetrics, config.getReportPath() + "/benchmark-multirun.xlsx");
        Assert.assertTrue(avgLoad > 0, "Average page load time should be > 0");
    }
}
