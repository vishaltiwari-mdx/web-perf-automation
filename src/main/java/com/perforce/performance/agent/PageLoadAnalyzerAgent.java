package com.perforce.performance.agent;

import com.perforce.performance.config.ConfigReader;
import com.perforce.performance.driver.DriverManager;
import com.perforce.performance.metrics.MetricsCollector;
import com.perforce.performance.metrics.PerformanceMetrics;
import com.perforce.performance.pages.LoginPage;
import com.perforce.performance.pages.NavigationRouter;
import com.perforce.performance.reports.ExcelReportManager;
import com.perforce.performance.reports.HtmlReportManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PageLoadAnalyzerAgent — Analyzes one or many URLs and produces per-URL HTML reports
 * plus a single combined HTML report (no screenshots) and a combined Excel report.
 *
 * Single URL:
 *   PerformanceAnalysisResult r = PageLoadAnalyzerAgent.analyze("https://example.com");
 *
 * Multiple URLs:
 *   List<PerformanceAnalysisResult> results =
 *       PageLoadAnalyzerAgent.analyzeAll(List.of("https://a.com", "https://b.com"));
 *
 * Metrics captured: Load Time · TTFB · FCP · LCP · TTI · CLS · INP · TBT · DNS · TCP · Resources
 */
public class PageLoadAnalyzerAgent {

    private static final Logger log = LoggerFactory.getLogger(PageLoadAnalyzerAgent.class);

    private PageLoadAnalyzerAgent() {}

    // ─── Single URL ───────────────────────────────────────────────────────────

    /** Analyze one URL — page name derived from hostname + path. */
    public static PerformanceAnalysisResult analyze(String url) {
        return analyze(url, derivePageName(url));
    }

    /** Analyze one URL with an explicit page label. */
    public static PerformanceAnalysisResult analyze(String url, String pageName) {
        log.info("=== PageLoadAnalyzerAgent: {} [{}] ===", pageName, url);

        HtmlReportManager.initForPage(pageName);

        PerformanceMetrics metrics = runMeasurement(url, pageName);

        HtmlReportManager.addMetrics("Page Load Analysis: " + pageName, metrics);
        HtmlReportManager.addSummaryTable(Collections.singletonList(metrics));
        HtmlReportManager.flush();
        String htmlPath = HtmlReportManager.getReportPath();

        String timestamp  = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String excelPath  = "reports" + File.separator + "page-load-" + safeName(pageName) + "-" + timestamp + ".xlsx";
        ExcelReportManager.generate(Collections.singletonList(metrics), excelPath);

        logConsoleSummary(metrics);
        log.info("=== Analysis complete. Overall: {} ===", metrics.getOverallStatus());

        return new PerformanceAnalysisResult(metrics, htmlPath, excelPath);
    }

    // ─── Multiple URLs ────────────────────────────────────────────────────────

    /**
     * Analyze a list of URLs.
     * <ul>
     *   <li>Each URL gets its own HTML report (with screenshot).</li>
     *   <li>A combined HTML report (no screenshots) is created for all URLs.</li>
     *   <li>A single combined Excel report covers all URLs.</li>
     * </ul>
     *
     * @param urls List of URLs to analyze (blank entries are skipped).
     * @return     One {@link PerformanceAnalysisResult} per URL, each carrying the individual
     *             HTML path, and the shared combined Excel path.
     */
    public static List<PerformanceAnalysisResult> analyzeAll(List<String> urls) {
        List<String> valid = urls.stream()
                .map(String::trim)
                .filter(u -> !u.isBlank())
                .collect(Collectors.toList());

        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║   PageLoadAnalyzerAgent — {} URL(s) to analyze           ", valid.size());
        log.info("╚══════════════════════════════════════════════════════════╝");

        String batchTimestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        List<PerformanceAnalysisResult> results   = new ArrayList<>();
        List<PerformanceMetrics>        allMetrics = new ArrayList<>();

        int total = valid.size();
        for (int i = 0; i < total; i++) {
            String url      = valid.get(i);
            String pageName = derivePageName(url);
            log.info("[{}/{}] Analyzing: {} — {}", i + 1, total, pageName, url);

            // ── Individual HTML report ────────────────────────────────────────
            HtmlReportManager.initForPage(pageName);

            PerformanceMetrics metrics = runMeasurement(url, pageName);

            HtmlReportManager.addMetrics("Page Load Analysis: " + pageName, metrics);
            HtmlReportManager.addSummaryTable(Collections.singletonList(metrics));
            HtmlReportManager.flush();
            String individualHtmlPath = HtmlReportManager.getReportPath();

            allMetrics.add(metrics);
            logConsoleSummary(metrics);

            results.add(new PerformanceAnalysisResult(metrics, individualHtmlPath, null /* filled below */));
        }

        // ── Combined HTML report (no screenshots) ─────────────────────────────
        HtmlReportManager.initCombined();
        for (PerformanceMetrics m : allMetrics) {
            HtmlReportManager.addMetricsNoScreenshot("Page Load Analysis: " + m.getPageName(), m);
        }
        HtmlReportManager.addSummaryTable(allMetrics);
        HtmlReportManager.flush();
        String combinedHtmlPath = HtmlReportManager.getReportPath();

        // ── Combined Excel report ─────────────────────────────────────────────
        String combinedExcelPath = "reports" + File.separator
                + "combined-analysis-" + batchTimestamp + ".xlsx";
        ExcelReportManager.generate(allMetrics, combinedExcelPath);

        log.info("══════════════════════════════════════════════════════════");
        log.info("  All {} URL(s) analyzed.", allMetrics.size());
        log.info("  Combined HTML  : {}", combinedHtmlPath);
        log.info("  Combined Excel : {}", combinedExcelPath);
        log.info("══════════════════════════════════════════════════════════");

        // Attach combined paths to every result
        return results.stream()
                .map(r -> new PerformanceAnalysisResult(r.getMetrics(), r.getHtmlReportPath(), combinedExcelPath))
                .collect(Collectors.toList());
    }

    // ─── Core measurement ─────────────────────────────────────────────────────

    /**
     * Navigates to {@code url}, handles login if the app redirects to the sign-in
     * page, then waits for the target page to be fully ready before collecting metrics.
     *
     * Flow for authenticated pages (e.g. {@code #/home}, {@code #/ip/catalog}):
     *   1. driver.get(url)  →  app redirects to login
     *   2. Detect login redirect (hash path contains "signin" / "login" / "security")
     *   3. Enter credentials from config.properties and click submit
     *   4. router.waitForCurrentPage() — waits for home page fully ready (header interactive)
     *   5. Inject WebVitals observer (active from the next navigation's first paint)
     *   6. router.navigateAndPrepare(url) — AppNavigator clicks header button(s), then
     *      readiness handler waits for page heading + spinners gone
     *   7. Collect and return metrics
     *
     * Flow for public pages (e.g. the login page itself):
     *   1. driver.get(url)
     *   2. Inject WebVitals observer
     *   3. Reload so observer is active from first paint
     *   4. NavigationRouter.waitForCurrentPage()
     *   5. Collect and return metrics
     */
    private static PerformanceMetrics runMeasurement(String url, String pageName) {
        ConfigReader config = ConfigReader.getInstance();
        WebDriver driver = DriverManager.initDriver();
        try {
            NavigationRouter router    = new NavigationRouter(driver);
            MetricsCollector collector = new MetricsCollector(driver);

            // ── Step 1: navigate to target URL ────────────────────────────────
            driver.get(url);
            String landedUrl  = waitUrlStable(driver);
            String landedPath = NavigationRouter.extractHashPath(landedUrl);

            boolean redirectedToLogin = landedPath.contains("signin")
                    || landedPath.contains("login")
                    || landedPath.startsWith("security");

            if (redirectedToLogin) {
                // ── Step 2: perform login ──────────────────────────────────────
                log.info("[agent] Redirected to login — logging in as '{}'", config.getUsername());
                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(config.getUsername(), config.getPassword());

                // ── Step 3: wait for post-login home page to be fully ready ───
                // This ensures the app header (Catalogs / Administration menus)
                // is interactive before AppNavigator tries to click any button.
                // The home handler waits for: title "Home" + IPs button visible + spinners gone.
                log.info("[agent] Waiting for home page to be fully ready...");
                router.waitForCurrentPage();
                log.info("[agent] Home page ready — URL: {}", driver.getCurrentUrl());

                // ── Step 4: inject observer BEFORE navigating to target ────────
                // Observer must be active so LCP/FCP/CLS events on the target
                // page are captured from the very first render frame.
                collector.injectWebVitalsObserver();

                // ── Step 5: click-based navigation to target ───────────────────
                // AppNavigator performs the same button clicks a real user would:
                //   #/home        → product logo click (already there, no-op via URL)
                //   #/ip/catalog  → Catalogs menu → "IP catalog"
                //   #/library/catalog → Catalogs menu → "Library catalog"
                //   #/query/search    → Advanced search link
                //   #/library/manage  → Administration menu → "Library management"
                //   … etc.
                // NavigationRouter then waits for the page heading + spinner gone.
                log.info("[agent] Navigating to target page: {}", url);
                router.navigateAndPrepare(url);

            } else {
                // ── Public page: inject observer, reload, wait for ready ───────
                collector.injectWebVitalsObserver();
                driver.navigate().refresh();
                router.waitForCurrentPage();
            }

            PerformanceMetrics metrics = collector.collect(pageName);
            metrics.setAgGridLoadTime(router.getLastAgGridLoadTime());
            return metrics;

        } finally {
            DriverManager.quitDriver();
        }
    }

    /** Polls until the URL stops changing (up to ~10 s) and returns the stable URL. */
    private static String waitUrlStable(WebDriver driver) {
        String prev = driver.getCurrentUrl();
        for (int i = 0; i < 15; i++) {
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            String cur = driver.getCurrentUrl();
            if (cur.equals(prev)) return cur;
            prev = cur;
        }
        return driver.getCurrentUrl();
    }

    // ─── Console summary ──────────────────────────────────────────────────────

    private static void logConsoleSummary(PerformanceMetrics m) {
        log.info("┌──────────────────────────────────────────────────────────┐");
        log.info("│             PAGE LOAD PERFORMANCE ANALYSIS               │");
        log.info("├──────────────────────────────────────────────────────────┤");
        log.info("│  URL       : {}", m.getPageUrl());
        log.info("│  Page Name : {}", m.getPageName());
        log.info("├──────────────────────────┬────────────┬──────────────────┤");
        log.info("│  Metric                  │   Value    │  Status          │");
        log.info("├──────────────────────────┼────────────┼──────────────────┤");
        log.info("│  Page Load Time          │ {} ms │  {}", rpad(fmt(m.getPageLoadTime()), 7),       pad(m.getPageLoadStatus()));
        log.info("│  TTFB                    │ {} ms │  {}", rpad(fmt(m.getTimeToFirstByte()), 7),    pad(m.getTtfbStatus()));
        log.info("│  FCP                     │ {} ms │  {}", rpad(fmt(m.getFirstContentfulPaint()), 7), pad(m.getFcpStatus()));
        log.info("│  LCP                     │ {} ms │  {}", rpad(fmt(m.getLargestContentfulPaint()), 7), pad(m.getLcpStatus()));
        log.info("│  TTI                     │ {} ms │  {}", rpad(fmt(m.getTimeToInteractive()), 7),  pad(m.getTtiStatus()));
        log.info("│  CLS                     │ {}    │  {}", rpad(String.format("%.4f", m.getCumulativeLayoutShift()), 7), pad(m.getClsStatus()));
        log.info("├──────────────────────────┼────────────┼──────────────────┤");
        log.info("│  DNS Lookup              │ {} ms │                  │", rpad(fmt(m.getDnsLookupTime()), 7));
        log.info("│  TCP Connect             │ {} ms │                  │", rpad(fmt(m.getTcpConnectionTime()), 7));
        log.info("│  DOM Interactive         │ {} ms │                  │", rpad(fmt(m.getDomInteractiveTime()), 7));
        log.info("│  DOM Content Loaded      │ {} ms │                  │", rpad(fmt(m.getDomContentLoadedTime()), 7));
        log.info("│  Total Resources         │ {}    │                  │", rpad(String.valueOf(m.getTotalResources()), 7));
        log.info("│  Transfer Size           │ {} KB │                  │", rpad(String.valueOf(m.getTransferSize() / 1024), 7));
        log.info("├──────────────────────────┴────────────┴──────────────────┤");
        log.info("│  OVERALL STATUS : {}", m.getOverallStatus());
        log.info("└──────────────────────────────────────────────────────────┘");
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static String fmt(double ms) {
        return ms > 0 ? String.format("%.0f", ms) : "N/A";
    }

    private static String rpad(String s, int width) {
        return String.format("%-" + width + "s", s);
    }

    private static String pad(String status) {
        return status == null ? "N/A             │" : String.format("%-16s│", status);
    }

    static String derivePageName(String url) {
        try {
            java.net.URI uri = new java.net.URI(url);
            String host = uri.getHost();

            // Prefer the hash path for SPA-style URLs (e.g. #/ip/catalog → "ip/catalog")
            String hashPath = NavigationRouter.extractHashPath(url);
            if (!hashPath.isEmpty()) {
                String last = NavigationRouter.extractSegment(url);
                if (!last.isBlank()) {
                    // Capitalise first letter for readability (e.g. "home" → "Home")
                    String label = Character.toUpperCase(last.charAt(0)) + last.substring(1);
                    return host + " / " + label;
                }
            }

            // Fall back to last regular path segment
            String path = uri.getPath();
            if (path != null && !path.isEmpty() && !path.equals("/")) {
                String last = path.replaceAll("/$", "").replaceAll(".*/", "");
                if (!last.isBlank()) return host + "/" + last;
            }

            return host != null ? host : url;
        } catch (Exception e) {
            return url;
        }
    }

    private static String safeName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    // ─── Result DTO ───────────────────────────────────────────────────────────

    /**
     * Holds metrics and report file paths for one analyzed URL.
     */
    public static class PerformanceAnalysisResult {

        private final PerformanceMetrics metrics;
        private final String htmlReportPath;
        private final String excelReportPath;

        public PerformanceAnalysisResult(PerformanceMetrics metrics,
                                         String htmlReportPath,
                                         String excelReportPath) {
            this.metrics        = metrics;
            this.htmlReportPath = htmlReportPath;
            this.excelReportPath = excelReportPath;
        }

        public PerformanceMetrics getMetrics()        { return metrics; }
        public String getHtmlReportPath()             { return htmlReportPath; }
        public String getExcelReportPath()            { return excelReportPath; }

        @Override
        public String toString() {
            return String.format("PerformanceAnalysisResult{page=%s, overall=%s, html=%s}",
                    metrics.getPageName(), metrics.getOverallStatus(), htmlReportPath);
        }
    }
}