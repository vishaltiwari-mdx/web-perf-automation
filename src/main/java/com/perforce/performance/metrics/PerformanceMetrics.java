package com.perforce.performance.metrics;

/**
 * PerformanceMetrics - Data model holding all collected metrics for a single page navigation.
 */
public class PerformanceMetrics {

    private String pageName;
    private String pageUrl;
    private long timestamp;

    // ── Navigation Timing (milliseconds) ──────────────────────────────────────
    private double dnsLookupTime;
    private double tcpConnectionTime;
    private double timeToFirstByte;          // TTFB
    private double domContentLoadedTime;     // DCL
    private double pageLoadTime;             // Full load
    private double domInteractiveTime;
    private double redirectTime;
    private double requestTime;
    private double responseTime;

    // ── Core Web Vitals ────────────────────────────────────────────────────────
    private double largestContentfulPaint;   // LCP  (target: ≤ 2500ms)
    private double firstContentfulPaint;     // FCP  (target: ≤ 1800ms)
    private double cumulativeLayoutShift;    // CLS  (target: ≤ 0.1)
    private double interactionToNextPaint;   // INP  (target: ≤ 200ms)
    private double firstInputDelay;          // FID  (target: ≤ 100ms)
    private double totalBlockingTime;        // TBT  (target: ≤ 200ms)

    // ── Resource Metrics ──────────────────────────────────────────────────────
    private int totalResources;
    private long transferSize;               // bytes
    private double speedIndex;

    // ── Time to Interactive ───────────────────────────────────────────────────
    private double timeToInteractive;        // TTI  (target: ≤ 3800ms)

    // ── AG-Grid Spinner Time ──────────────────────────────────────────────────
    private double agGridLoadTime;           // ms spinner was visible (0 = no grid on page)

    // ── Benchmark Status ──────────────────────────────────────────────────────
    private String lcpStatus;
    private String fcpStatus;
    private String clsStatus;
    private String ttfbStatus;
    private String pageLoadStatus;
    private String ttiStatus;
    private String overallStatus;

    // ── Screenshot Path ───────────────────────────────────────────────────────
    private String screenshotPath;

    public PerformanceMetrics() {
        this.timestamp = System.currentTimeMillis();
    }

    // ─── Benchmark Evaluation ─────────────────────────────────────────────────

    public void evaluateBenchmarks() {
        this.lcpStatus       = evaluateLCP(largestContentfulPaint);
        this.fcpStatus       = evaluateFCP(firstContentfulPaint);
        this.clsStatus       = evaluateCLS(cumulativeLayoutShift);
        this.ttfbStatus      = evaluateTTFB(timeToFirstByte);
        this.pageLoadStatus  = evaluatePageLoad(pageLoadTime);
        this.ttiStatus       = evaluateTTI(timeToInteractive);

        // Overall = worst of all
        boolean allGood = "GOOD".equals(lcpStatus) && "GOOD".equals(fcpStatus)
                && "GOOD".equals(clsStatus) && "GOOD".equals(ttfbStatus)
                && "GOOD".equals(pageLoadStatus) && "GOOD".equals(ttiStatus);
        boolean anyPoor = "POOR".equals(lcpStatus) || "POOR".equals(fcpStatus)
                || "POOR".equals(clsStatus) || "POOR".equals(ttfbStatus)
                || "POOR".equals(pageLoadStatus) || "POOR".equals(ttiStatus);
        this.overallStatus = allGood ? "GOOD" : anyPoor ? "POOR" : "NEEDS IMPROVEMENT";
    }

    // Google / Industry Benchmarks
    private String evaluateLCP(double lcp) {
        if (lcp <= 0) return "N/A";
        return lcp <= 2500 ? "GOOD" : lcp <= 4000 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    private String evaluateFCP(double fcp) {
        if (fcp <= 0) return "N/A";
        return fcp <= 1800 ? "GOOD" : fcp <= 3000 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    private String evaluateCLS(double cls) {
        if (cls < 0) return "N/A";
        return cls <= 0.1 ? "GOOD" : cls <= 0.25 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    private String evaluateTTFB(double ttfb) {
        if (ttfb <= 0) return "N/A";
        return ttfb <= 800 ? "GOOD" : ttfb <= 1800 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    private String evaluatePageLoad(double load) {
        if (load <= 0) return "N/A";
        return load <= 3000 ? "GOOD" : load <= 6000 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    private String evaluateTTI(double tti) {
        if (tti <= 0) return "N/A";
        return tti <= 3800 ? "GOOD" : tti <= 7300 ? "NEEDS IMPROVEMENT" : "POOR";
    }

    // ─── Getters / Setters ────────────────────────────────────────────────────

    public String getPageName()                    { return pageName; }
    public void setPageName(String pageName)       { this.pageName = pageName; }

    public String getPageUrl()                     { return pageUrl; }
    public void setPageUrl(String pageUrl)         { this.pageUrl = pageUrl; }

    public long getTimestamp()                     { return timestamp; }
    public void setTimestamp(long timestamp)       { this.timestamp = timestamp; }

    public double getDnsLookupTime()               { return dnsLookupTime; }
    public void setDnsLookupTime(double v)         { this.dnsLookupTime = v; }

    public double getTcpConnectionTime()           { return tcpConnectionTime; }
    public void setTcpConnectionTime(double v)     { this.tcpConnectionTime = v; }

    public double getTimeToFirstByte()             { return timeToFirstByte; }
    public void setTimeToFirstByte(double v)       { this.timeToFirstByte = v; }

    public double getDomContentLoadedTime()        { return domContentLoadedTime; }
    public void setDomContentLoadedTime(double v)  { this.domContentLoadedTime = v; }

    public double getPageLoadTime()                { return pageLoadTime; }
    public void setPageLoadTime(double v)          { this.pageLoadTime = v; }

    public double getDomInteractiveTime()          { return domInteractiveTime; }
    public void setDomInteractiveTime(double v)    { this.domInteractiveTime = v; }

    public double getRedirectTime()                { return redirectTime; }
    public void setRedirectTime(double v)          { this.redirectTime = v; }

    public double getRequestTime()                 { return requestTime; }
    public void setRequestTime(double v)           { this.requestTime = v; }

    public double getResponseTime()                { return responseTime; }
    public void setResponseTime(double v)          { this.responseTime = v; }

    public double getLargestContentfulPaint()      { return largestContentfulPaint; }
    public void setLargestContentfulPaint(double v){ this.largestContentfulPaint = v; }

    public double getFirstContentfulPaint()        { return firstContentfulPaint; }
    public void setFirstContentfulPaint(double v)  { this.firstContentfulPaint = v; }

    public double getCumulativeLayoutShift()       { return cumulativeLayoutShift; }
    public void setCumulativeLayoutShift(double v) { this.cumulativeLayoutShift = v; }

    public double getInteractionToNextPaint()      { return interactionToNextPaint; }
    public void setInteractionToNextPaint(double v){ this.interactionToNextPaint = v; }

    public double getFirstInputDelay()             { return firstInputDelay; }
    public void setFirstInputDelay(double v)       { this.firstInputDelay = v; }

    public double getTotalBlockingTime()           { return totalBlockingTime; }
    public void setTotalBlockingTime(double v)     { this.totalBlockingTime = v; }

    public int getTotalResources()                 { return totalResources; }
    public void setTotalResources(int v)           { this.totalResources = v; }

    public long getTransferSize()                  { return transferSize; }
    public void setTransferSize(long v)            { this.transferSize = v; }

    public double getSpeedIndex()                  { return speedIndex; }
    public void setSpeedIndex(double v)            { this.speedIndex = v; }

    public double getTimeToInteractive()             { return timeToInteractive; }
    public void setTimeToInteractive(double v)      { this.timeToInteractive = v; }

    public double getAgGridLoadTime()               { return agGridLoadTime; }
    public void setAgGridLoadTime(double v)         { this.agGridLoadTime = v; }

    public String getLcpStatus()                   { return lcpStatus; }
    public String getFcpStatus()                   { return fcpStatus; }
    public String getClsStatus()                   { return clsStatus; }
    public String getTtfbStatus()                  { return ttfbStatus; }
    public String getPageLoadStatus()              { return pageLoadStatus; }
    public String getTtiStatus()                   { return ttiStatus; }
    public String getOverallStatus()               { return overallStatus; }

    public String getScreenshotPath()             { return screenshotPath; }
    public void setScreenshotPath(String value)   { this.screenshotPath = value; }

    @Override
    public String toString() {
        return String.format(
            "[%s] Load=%.0fms | FCP=%.0fms | LCP=%.0fms | TTI=%.0fms | CLS=%.3f | TTFB=%.0fms | AG-Grid=%.0fms | Overall=%s",
            pageName, pageLoadTime, firstContentfulPaint, largestContentfulPaint,
            timeToInteractive, cumulativeLayoutShift, timeToFirstByte, agGridLoadTime, overallStatus
        );
    }
}
