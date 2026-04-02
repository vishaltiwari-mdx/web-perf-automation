package com.perforce.performance.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.perforce.performance.config.ConfigReader;
import com.perforce.performance.metrics.PerformanceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * HtmlReportManager - Generates a rich HTML report using ExtentReports.
 * Report includes per-page metrics, benchmark status, and a summary table.
 */
public class HtmlReportManager {

    private static final Logger log = LoggerFactory.getLogger(HtmlReportManager.class);
    private static ExtentReports extent;
    private static String reportPath;

    public static void init() {
        ConfigReader config = ConfigReader.getInstance();
        String outputDir = config.getReportPath();

        new File(outputDir).mkdirs();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        reportPath = outputDir + "/performance-report-" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Web Performance Report");
        spark.config().setReportName("Performance Benchmark Report - " + timestamp);
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        spark.config().setCss(getCustomCss());

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework",  "Selenium + TestNG + ExtentReports");
        extent.setSystemInfo("Browser",    config.getBrowser());
        extent.setSystemInfo("Base URL",   config.getBaseUrl());
        extent.setSystemInfo("Headless",   String.valueOf(config.isHeadless()));
        extent.setSystemInfo("Runs/Page",  String.valueOf(config.getPerformanceRuns()));

        log.info("HTML Report initialized: {}", reportPath);
    }

    /**
     * Initializes an individual per-page HTML report.
     * File: reports/page-load-{safeName}-{timestamp}.html
     */
    public static void initForPage(String pageLabel) {
        ConfigReader config = ConfigReader.getInstance();
        String outputDir = config.getReportPath();
        new File(outputDir).mkdirs();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        String safeName  = pageLabel.replaceAll("[^a-zA-Z0-9._-]", "_");
        reportPath = outputDir + "/page-load-" + safeName + "-" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Page Load Analysis – " + pageLabel);
        spark.config().setReportName("Page Load Analysis: " + pageLabel + " — " + timestamp);
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        spark.config().setCss(getCustomCss());

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework", "Selenium + ExtentReports");
        extent.setSystemInfo("Browser",   config.getBrowser());
        extent.setSystemInfo("Headless",  String.valueOf(config.isHeadless()));

        log.info("Per-page HTML report initialized: {}", reportPath);
    }

    /**
     * Initializes a combined HTML report (all URLs, no screenshots).
     * File: reports/combined-performance-report-{timestamp}.html
     */
    public static void initCombined() {
        ConfigReader config = ConfigReader.getInstance();
        String outputDir = config.getReportPath();
        new File(outputDir).mkdirs();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        reportPath = outputDir + "/combined-performance-report-" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Combined Performance Report");
        spark.config().setReportName("Combined Performance Report — " + timestamp);
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        spark.config().setCss(getCustomCss());

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework",   "Selenium + ExtentReports");
        extent.setSystemInfo("Browser",     config.getBrowser());
        extent.setSystemInfo("Report Type", "Combined — All URLs");

        log.info("Combined HTML report initialized: {}", reportPath);
    }

    /** Returns the path of the most recently initialized report file. */
    public static String getReportPath() {
        return reportPath;
    }

    public static void addMetrics(String testName, PerformanceMetrics metrics) {
        ExtentTest test = extent.createTest(testName);

        Status overallStatus = statusToExtent(metrics.getOverallStatus());
        test.log(overallStatus, "Overall Status: <b>" + metrics.getOverallStatus() + "</b>");
        test.log(Status.INFO, "URL: " + metrics.getPageUrl());

        attachScreenshot(test, metrics);

        // Core Web Vitals Table
        test.log(Status.INFO, buildCoreWebVitalsTable(metrics));

        // Navigation Timing Table
        test.log(Status.INFO, buildNavigationTimingTable(metrics));

        // Resource Info
        test.log(Status.INFO, buildResourceTable(metrics));

        log.info("Report entry added for: {}", testName);
    }

    /**
     * Same as {@link #addMetrics} but omits the screenshot — used in the combined report.
     */
    public static void addMetricsNoScreenshot(String testName, PerformanceMetrics metrics) {
        ExtentTest test = extent.createTest(testName);

        Status overallStatus = statusToExtent(metrics.getOverallStatus());
        test.log(overallStatus, "Overall Status: <b>" + metrics.getOverallStatus() + "</b>");
        test.log(Status.INFO, "URL: " + metrics.getPageUrl());

        test.log(Status.INFO, buildCoreWebVitalsTable(metrics));
        test.log(Status.INFO, buildNavigationTimingTable(metrics));
        test.log(Status.INFO, buildResourceTable(metrics));

        log.info("Combined report entry added for: {}", testName);
    }

    public static void addSummaryTable(List<PerformanceMetrics> metricsList) {
        ExtentTest summary = extent.createTest("📊 Performance Summary - All Pages");
        summary.log(Status.INFO, buildSummaryTable(metricsList));
        summary.log(Status.INFO, buildBenchmarkReference());
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            log.info("HTML Report saved: {}", reportPath);
        }
    }

    // ─── Table Builders ───────────────────────────────────────────────────────

    private static String buildCoreWebVitalsTable(PerformanceMetrics m) {
        return "<h4>🔬 Core Web Vitals</h4>"
             + "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;width:100%'>"
             + "<tr style='background:#2d2d2d;color:white'>"
             + "<th>Metric</th><th>Value</th><th>Target</th><th>Status</th></tr>"
             + row("LCP (Largest Contentful Paint)", fmt(m.getLargestContentfulPaint()) + " ms", "≤ 2500 ms", m.getLcpStatus())
             + row("FCP (First Contentful Paint)",   fmt(m.getFirstContentfulPaint())   + " ms", "≤ 1800 ms", m.getFcpStatus())
             + row("TTI (Time to Interactive)",      fmt(m.getTimeToInteractive())       + " ms", "≤ 3800 ms", m.getTtiStatus())
             + row("CLS (Cumulative Layout Shift)",  String.format("%.4f", m.getCumulativeLayoutShift()), "≤ 0.1",     m.getClsStatus())
             + row("TTFB (Time to First Byte)",      fmt(m.getTimeToFirstByte())        + " ms", "≤ 800 ms",  m.getTtfbStatus())
             + row("INP (Interaction to Next Paint)", fmt(m.getInteractionToNextPaint()) + " ms", "≤ 200 ms", "INFO")
             + row("TBT (Total Blocking Time)",      fmt(m.getTotalBlockingTime())       + " ms", "≤ 200 ms", "INFO")
             + "</table>";
    }

    private static String buildNavigationTimingTable(PerformanceMetrics m) {
        String agGridRow = m.getAgGridLoadTime() > 0
                ? highlightRow("AG-Grid Spinner Time", fmt(m.getAgGridLoadTime()) + " ms",
                        m.getAgGridLoadTime() < 1000 ? "#1a3a1a" : m.getAgGridLoadTime() < 3000 ? "#3a3a1a" : "#3a1a1a")
                : infoRow("AG-Grid Spinner Time", "N/A (no grid on this page)");
        return "<h4>⏱️ Navigation Timing</h4>"
             + "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;width:100%'>"
             + "<tr style='background:#2d2d2d;color:white'>"
             + "<th>Metric</th><th>Value</th></tr>"
             + infoRow("DNS Lookup Time",       fmt(m.getDnsLookupTime())        + " ms")
             + infoRow("TCP Connection Time",   fmt(m.getTcpConnectionTime())    + " ms")
             + infoRow("Redirect Time",         fmt(m.getRedirectTime())         + " ms")
             + infoRow("Request Time",          fmt(m.getRequestTime())          + " ms")
             + infoRow("Response Time",         fmt(m.getResponseTime())         + " ms")
             + infoRow("DOM Interactive",       fmt(m.getDomInteractiveTime())   + " ms")
             + infoRow("DOM Content Loaded",    fmt(m.getDomContentLoadedTime()) + " ms")
             + infoRow("Full Page Load",        fmt(m.getPageLoadTime())         + " ms")
             + agGridRow
             + "</table>";
    }

    private static String buildResourceTable(PerformanceMetrics m) {
        long sizeKb = m.getTransferSize() / 1024;
        return "<h4>📦 Resource Summary</h4>"
             + "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;width:100%'>"
             + "<tr style='background:#2d2d2d;color:white'><th>Metric</th><th>Value</th></tr>"
             + infoRow("Total Resources", String.valueOf(m.getTotalResources()))
             + infoRow("Transfer Size",   sizeKb + " KB")
             + "</table>";
    }

    private static String buildSummaryTable(List<PerformanceMetrics> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>📊 All Pages - Performance Summary</h3>")
          .append("<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;width:100%;font-size:13px'>")
          .append("<tr style='background:#1a1a2e;color:white;text-align:center'>")
          .append("<th>Page</th><th>Load Time</th><th>FCP</th><th>LCP</th><th>TTI</th>")
          .append("<th>CLS</th><th>TTFB</th><th>AG-Grid</th><th>Resources</th><th>Transfer</th><th>Overall</th></tr>");

        for (PerformanceMetrics m : list) {
            String bg = "GOOD".equals(m.getOverallStatus()) ? "#1a3a1a"
                      : "POOR".equals(m.getOverallStatus()) ? "#3a1a1a" : "#3a3a1a";
            sb.append("<tr style='background:").append(bg).append(";text-align:center'>")
              .append("<td>").append(m.getPageName()).append("</td>")
              .append("<td>").append(fmt(m.getPageLoadTime())).append(" ms</td>")
              .append("<td>").append(fmt(m.getFirstContentfulPaint())).append(" ms</td>")
              .append("<td>").append(fmt(m.getLargestContentfulPaint())).append(" ms</td>")
              .append("<td>").append(fmt(m.getTimeToInteractive())).append(" ms</td>")
              .append("<td>").append(String.format("%.3f", m.getCumulativeLayoutShift())).append("</td>")
              .append("<td>").append(fmt(m.getTimeToFirstByte())).append(" ms</td>")
              .append("<td>").append(m.getAgGridLoadTime() > 0 ? fmt(m.getAgGridLoadTime()) + " ms" : "—").append("</td>")
              .append("<td>").append(m.getTotalResources()).append("</td>")
              .append("<td>").append(m.getTransferSize() / 1024).append(" KB</td>")
              .append("<td><b>").append(m.getOverallStatus()).append("</b></td>")
              .append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private static String buildBenchmarkReference() {
        return "<h4>📏 Google Benchmark Reference</h4>"
             + "<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;width:60%'>"
             + "<tr style='background:#2d2d2d;color:white'><th>Metric</th><th>GOOD</th><th>NEEDS IMPROVEMENT</th><th>POOR</th></tr>"
             + "<tr><td>LCP</td><td>≤ 2500ms</td><td>2500–4000ms</td><td>&gt; 4000ms</td></tr>"
             + "<tr><td>FCP</td><td>≤ 1800ms</td><td>1800–3000ms</td><td>&gt; 3000ms</td></tr>"
             + "<tr><td>TTI</td><td>≤ 3800ms</td><td>3800–7300ms</td><td>&gt; 7300ms</td></tr>"
             + "<tr><td>CLS</td><td>≤ 0.1</td><td>0.1–0.25</td><td>&gt; 0.25</td></tr>"
             + "<tr><td>TTFB</td><td>≤ 800ms</td><td>800–1800ms</td><td>&gt; 1800ms</td></tr>"
             + "<tr><td>Page Load</td><td>≤ 3000ms</td><td>3000–6000ms</td><td>&gt; 6000ms</td></tr>"
             + "</table>";
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static String row(String metric, String value, String target, String status) {
        String color = "GOOD".equals(status) ? "#1a3a1a" : "POOR".equals(status) ? "#3a1a1a" : "INFO".equals(status) ? "#1a2a3a" : "#3a3a1a";
        String badge = "GOOD".equals(status) ? "🟢 GOOD" : "POOR".equals(status) ? "🔴 POOR" : "INFO".equals(status) ? "ℹ️" : "🟡 NEEDS IMPROVEMENT";
        return "<tr style='background:" + color + "'><td>" + metric + "</td><td><b>" + value + "</b></td><td>" + target + "</td><td>" + badge + "</td></tr>";
    }

    private static String infoRow(String label, String value) {
        return "<tr><td>" + label + "</td><td><b>" + value + "</b></td></tr>";
    }

    private static String highlightRow(String label, String value, String bgColor) {
        return "<tr style='background:" + bgColor + "'><td><b>" + label + "</b></td><td><b>" + value + "</b></td></tr>";
    }

    private static String fmt(double ms) {
        return ms > 0 ? String.format("%.0f", ms) : "N/A";
    }

    private static Status statusToExtent(String status) {
        if ("GOOD".equals(status))             return Status.PASS;
        if ("POOR".equals(status))             return Status.FAIL;
        if ("NEEDS IMPROVEMENT".equals(status)) return Status.WARNING;
        return Status.INFO;
    }

    private static String getCustomCss() {
        return "body { font-family: 'Segoe UI', Arial, sans-serif; } "
             + "table { width: 100%; } "
             + ".badge-success { background: #28a745; } "
             + ".badge-danger  { background: #dc3545; }";
    }

    private static void attachScreenshot(ExtentTest test, PerformanceMetrics metrics) {
        String screenshotPath = metrics.getScreenshotPath();
        if (screenshotPath == null || screenshotPath.isBlank()) {
            return;
        }

        try {
            Path reportDir = Paths.get(ConfigReader.getInstance().getReportPath()).toAbsolutePath();
            Path absoluteScreenshot = reportDir.resolve(screenshotPath).normalize();
            if (!Files.exists(absoluteScreenshot)) {
                log.warn("Screenshot not found for report entry: {}", absoluteScreenshot);
                return;
            }

            String relativePath = screenshotPath.replace('\\', '/');
            test.addScreenCaptureFromPath(relativePath, "Page Screenshot");
            test.log(Status.INFO,
                    "<div style='margin:10px 0'><a href='" + relativePath +
                    "' target='_blank'>Open Screenshot</a><br/>" +
                    "<img src='" + relativePath +
                    "' alt='Page Screenshot' style='margin-top:8px;max-width:700px;border:1px solid #444;border-radius:4px'/>" +
                    "</div>");
        } catch (Exception e) {
            log.warn("Unable to attach screenshot for {}: {}", metrics.getPageName(), e.getMessage());
        }
    }
}
