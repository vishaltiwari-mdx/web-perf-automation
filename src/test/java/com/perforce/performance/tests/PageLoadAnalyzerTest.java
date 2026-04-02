package com.perforce.performance.tests;

import com.perforce.performance.agent.PageLoadAnalyzerAgent;
import com.perforce.performance.agent.PageLoadAnalyzerAgent.PerformanceAnalysisResult;
import com.perforce.performance.config.ConfigReader;
import com.perforce.performance.driver.DriverManager;
import com.perforce.performance.metrics.PerformanceMetrics;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PageLoadAnalyzerTest — TestNG entry point for the Page Load Performance Analyzer Agent.
 *
 * Accepts one or more comma-separated URLs via the {@code target.url} parameter.
 *
 * Examples:
 *
 *   Single URL (Maven):
 *     mvn test -Dtest=PageLoadAnalyzerTest "-Dtarget.url=https://example.com"
 *
 *   Multiple URLs (Maven):
 *     mvn test -Dtest=PageLoadAnalyzerTest "-Dtarget.url=https://a.com,https://b.com"
 *
 *   Via testng.xml:
 *     <parameter name="target.url" value="https://a.com, https://b.com"/>
 *
 * For each URL a separate HTML report is generated (with screenshot).
 * A combined HTML report (no screenshots) and a combined Excel report are also produced.
 */
public class PageLoadAnalyzerTest extends BaseTest {

    @Test(description = "Analyze page load performance for one or more URLs")
    @Parameters("target.url")
    public void testPageLoadAnalysis(@Optional("") String targetUrl) {

        // Resolve URLs: parameter → system property → config base URL
        if (targetUrl == null || targetUrl.isBlank()) {
            targetUrl = System.getProperty("target.url", "");
        }
        if (targetUrl.isBlank()) {
            targetUrl = ConfigReader.getInstance().getBaseUrl();
            log.info("No target URL supplied — using base URL from config: {}", targetUrl);
        }

        // Parse comma-separated list
        List<String> urls = Arrays.stream(targetUrl.split(","))
                .map(String::trim)
                .filter(u -> !u.isBlank())
                .collect(Collectors.toList());

        log.info("Page Load Analyzer: {} URL(s) → {}", urls.size(), urls);

        // BaseTest.setUp() already initialised a driver we won't use — release it
        DriverManager.quitDriver();

        if (urls.size() == 1) {
            // ── Single URL ────────────────────────────────────────────────────
            PerformanceAnalysisResult result = PageLoadAnalyzerAgent.analyze(urls.get(0));
            assertMetrics(result.getMetrics());

        } else {
            // ── Multiple URLs ─────────────────────────────────────────────────
            List<PerformanceAnalysisResult> results = PageLoadAnalyzerAgent.analyzeAll(urls);

            Assert.assertFalse(results.isEmpty(), "analyzeAll should return at least one result");

            for (PerformanceAnalysisResult r : results) {
                assertMetrics(r.getMetrics());
            }

            long poorCount = results.stream()
                    .filter(r -> "POOR".equals(r.getMetrics().getOverallStatus()))
                    .count();
            if (poorCount > 0) {
                log.warn("{} out of {} URLs have POOR overall performance.", poorCount, results.size());
            }
        }
    }

    // ─── Assertions ───────────────────────────────────────────────────────────

    private void assertMetrics(PerformanceMetrics m) {
        Assert.assertNotNull(m.getOverallStatus(),
                "Overall status should not be null for: " + m.getPageUrl());
        Assert.assertTrue(m.getPageLoadTime() > 0,
                "Page load time should be > 0 ms [" + m.getPageUrl() + "]");
        Assert.assertTrue(m.getTimeToFirstByte() > 0,
                "TTFB should be > 0 ms [" + m.getPageUrl() + "]");
        Assert.assertTrue(m.getFirstContentfulPaint() > 0,
                "FCP should be > 0 ms [" + m.getPageUrl() + "]");

        if ("POOR".equals(m.getLcpStatus())) {
            log.warn("LCP POOR ({} ms) for: {}", (long) m.getLargestContentfulPaint(), m.getPageUrl());
        }
        if ("POOR".equals(m.getTtiStatus())) {
            log.warn("TTI POOR ({} ms) for: {}", (long) m.getTimeToInteractive(), m.getPageUrl());
        }
    }
}