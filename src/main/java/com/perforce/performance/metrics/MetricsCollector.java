package com.perforce.performance.metrics;

import com.perforce.performance.config.ConfigReader;
import com.perforce.performance.utils.ScreenshotManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * MetricsCollector - Collects performance metrics using the browser's Performance API
 * and Web Vitals JavaScript snippets via Chrome DevTools Protocol.
 *
 * Metrics collected:
 *  - Navigation Timing (TTFB, DNS, TCP, DOM load, Full load)
 *  - Core Web Vitals (LCP, FCP, CLS, INP, FID, TBT)
 *  - Resource counts and transfer sizes
 */
public class MetricsCollector {

    private static final Logger log = LoggerFactory.getLogger(MetricsCollector.class);

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final ConfigReader config;

    public MetricsCollector(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.config = ConfigReader.getInstance();
    }

    /**
     * Injects Web Vitals observer into the page BEFORE navigation.
     * Call this right after the page starts loading.
     */
    public void injectWebVitalsObserver() {
        String script =
            "window.__webVitals = { lcp: 0, cls: 0, fid: 0, inp: 0 };\n" +
            "try {\n" +
            "  new PerformanceObserver((list) => {\n" +
            "    const entries = list.getEntries();\n" +
            "    const last = entries[entries.length - 1];\n" +
            "    window.__webVitals.lcp = last.startTime;\n" +
            "  }).observe({ type: 'largest-contentful-paint', buffered: true });\n" +
            "} catch(e) {}\n" +
            "try {\n" +
            "  let clsValue = 0;\n" +
            "  new PerformanceObserver((list) => {\n" +
            "    for (const entry of list.getEntries()) {\n" +
            "      if (!entry.hadRecentInput) clsValue += entry.value;\n" +
            "    }\n" +
            "    window.__webVitals.cls = clsValue;\n" +
            "  }).observe({ type: 'layout-shift', buffered: true });\n" +
            "} catch(e) {}\n" +
            "try {\n" +
            "  new PerformanceObserver((list) => {\n" +
            "    for (const entry of list.getEntries()) {\n" +
            "      window.__webVitals.fid = entry.processingStart - entry.startTime;\n" +
            "    }\n" +
            "  }).observe({ type: 'first-input', buffered: true });\n" +
            "} catch(e) {}\n" +
            "try {\n" +
            "  new PerformanceObserver((list) => {\n" +
            "    for (const entry of list.getEntries()) {\n" +
            "      if (entry.duration > (window.__webVitals.inp || 0)) {\n" +
            "        window.__webVitals.inp = entry.duration;\n" +
            "      }\n" +
            "    }\n" +
            "  }).observe({ type: 'event', buffered: true, durationThreshold: 16 });\n" +
            "} catch(e) {}";
        try {
            js.executeScript(script);
        } catch (Exception e) {
            log.warn("Could not inject Web Vitals observer: {}", e.getMessage());
        }
    }

    /**
     * Waits for page to fully load, then collects all performance metrics.
     */
    public PerformanceMetrics collect(String pageName) {
        waitForPageReady();
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.setPageName(pageName);
        metrics.setPageUrl(driver.getCurrentUrl());

        collectNavigationTiming(metrics);
        collectWebVitals(metrics);
        collectResourceMetrics(metrics);

        String screenshotPath = ScreenshotManager.capture(
                driver,
                pageName,
                config.getScreenshotPath(),
                config.getReportPath());
        metrics.setScreenshotPath(screenshotPath);

        metrics.evaluateBenchmarks();

        log.info("Collected: {}", metrics);
        return metrics;
    }

    // ─── Navigation Timing ────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void collectNavigationTiming(PerformanceMetrics metrics) {
        String script =
            "const t = performance.getEntriesByType('navigation')[0] || performance.timing;\n" +
            "const nav = t.toJSON ? t.toJSON() : t;\n" +
            "return {\n" +
            "  dnsLookup:        (nav.domainLookupEnd   || 0) - (nav.domainLookupStart  || 0),\n" +
            "  tcpConnect:       (nav.connectEnd         || 0) - (nav.connectStart       || 0),\n" +
            "  ttfb:             (nav.responseStart      || 0) - (nav.requestStart        || 0),\n" +
            "  requestTime:      (nav.responseStart      || 0) - (nav.requestStart        || 0),\n" +
            "  responseTime:     (nav.responseEnd        || 0) - (nav.responseStart      || 0),\n" +
            "  domInteractive:   (nav.domInteractive     || 0) - (nav.startTime          || nav.fetchStart || 0),\n" +
            "  domContentLoaded: (nav.domContentLoadedEventEnd || 0) - (nav.startTime    || nav.fetchStart || 0),\n" +
            "  pageLoad:         (nav.loadEventEnd       || 0) - (nav.startTime          || nav.fetchStart || 0),\n" +
            "  redirect:         (nav.redirectEnd        || 0) - (nav.redirectStart      || 0)\n" +
            "};";
        try {
            Map<String, Object> timing = (Map<String, Object>) js.executeScript(script);
            metrics.setDnsLookupTime(toDouble(timing.get("dnsLookup")));
            metrics.setTcpConnectionTime(toDouble(timing.get("tcpConnect")));
            metrics.setTimeToFirstByte(toDouble(timing.get("ttfb")));
            metrics.setRequestTime(toDouble(timing.get("requestTime")));
            metrics.setResponseTime(toDouble(timing.get("responseTime")));
            metrics.setDomInteractiveTime(toDouble(timing.get("domInteractive")));
            metrics.setDomContentLoadedTime(toDouble(timing.get("domContentLoaded")));
            metrics.setPageLoadTime(toDouble(timing.get("pageLoad")));
            metrics.setRedirectTime(toDouble(timing.get("redirect")));
        } catch (Exception e) {
            log.warn("Navigation timing collection failed: {}", e.getMessage());
        }

        // Time to Interactive: max(domInteractive, end of last long task)
        String ttiScript =
            "const nav = performance.getEntriesByType('navigation')[0];\n" +
            "const navStart = nav ? nav.startTime : 0;\n" +
            "const domInteractive = nav ? (nav.domInteractive - navStart) : 0;\n" +
            "const longTasks = performance.getEntriesByType('longtask') || [];\n" +
            "let lastLongTaskEnd = 0;\n" +
            "for (const task of longTasks) {\n" +
            "  const taskEnd = (task.startTime - navStart) + task.duration;\n" +
            "  if (taskEnd > lastLongTaskEnd) lastLongTaskEnd = taskEnd;\n" +
            "}\n" +
            "return Math.max(domInteractive, lastLongTaskEnd);";
        try {
            metrics.setTimeToInteractive(toDouble(js.executeScript(ttiScript)));
        } catch (Exception e) {
            log.warn("TTI collection failed: {}", e.getMessage());
        }
    }

    // ─── Core Web Vitals ──────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void collectWebVitals(PerformanceMetrics metrics) {
        // FCP via Performance API
        String fcpScript =
            "const entries = performance.getEntriesByName('first-contentful-paint');\n" +
            "return entries.length > 0 ? entries[0].startTime : 0;";
        try {
            double fcp = toDouble(js.executeScript(fcpScript));
            metrics.setFirstContentfulPaint(fcp);
        } catch (Exception e) {
            log.warn("FCP collection failed: {}", e.getMessage());
        }

        // LCP, CLS, FID, INP from injected observer
        String vitalsScript =
            "const v = window.__webVitals || {};\n" +
            "return { lcp: v.lcp || 0, cls: v.cls || 0, fid: v.fid || 0, inp: v.inp || 0 };";
        try {
            Map<String, Object> vitals = (Map<String, Object>) js.executeScript(vitalsScript);
            metrics.setLargestContentfulPaint(toDouble(vitals.get("lcp")));
            metrics.setCumulativeLayoutShift(toDouble(vitals.get("cls")));
            metrics.setFirstInputDelay(toDouble(vitals.get("fid")));
            metrics.setInteractionToNextPaint(toDouble(vitals.get("inp")));
        } catch (Exception e) {
            log.warn("Web Vitals (observer) collection failed: {}", e.getMessage());
        }

        // Total Blocking Time (long tasks sum)
        String tbtScript =
            "let tbt = 0;\n" +
            "for (const entry of performance.getEntriesByType('longtask') || []) {\n" +
            "  const blocking = entry.duration - 50;\n" +
            "  if (blocking > 0) tbt += blocking;\n" +
            "}\n" +
            "return tbt;";
        try {
            metrics.setTotalBlockingTime(toDouble(js.executeScript(tbtScript)));
        } catch (Exception e) {
            log.warn("TBT collection failed: {}", e.getMessage());
        }
    }

    // ─── Resource Metrics ─────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void collectResourceMetrics(PerformanceMetrics metrics) {
        String script =
            "const resources = performance.getEntriesByType('resource');\n" +
            "let totalSize = 0;\n" +
            "resources.forEach(r => { totalSize += (r.transferSize || 0); });\n" +
            "return { count: resources.length, size: totalSize };";
        try {
            Map<String, Object> res = (Map<String, Object>) js.executeScript(script);
            metrics.setTotalResources(((Number) res.get("count")).intValue());
            metrics.setTransferSize(((Number) res.get("size")).longValue());
        } catch (Exception e) {
            log.warn("Resource metrics collection failed: {}", e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void waitForPageReady() {
        int maxWait = 30;
        int waited = 0;
        while (waited < maxWait) {
            try {
                String state = (String) js.executeScript("return document.readyState");
                if ("complete".equals(state)) break;
                Thread.sleep(500);
                waited++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                break;
            }
        }
        // Extra wait for LCP/CLS observers to settle
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private double toDouble(Object value) {
        if (value == null) return 0.0;
        return ((Number) value).doubleValue();
    }
}
