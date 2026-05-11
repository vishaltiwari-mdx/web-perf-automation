import { Page } from '@playwright/test';
import {
  PerformanceMetrics,
  createEmptyMetrics,
  evaluateBenchmarks,
} from './PerformanceMetrics';
import { ScreenshotManager } from '../utils/ScreenshotManager';
import { ConfigReader } from '../config/ConfigReader';

// ── Web Vitals observer script (identical to Java version) ────────────────────
const WEB_VITALS_SCRIPT = `
  window.__webVitals = { lcp: 0, cls: 0, fid: 0, inp: 0 };
  try {
    new PerformanceObserver((list) => {
      const entries = list.getEntries();
      const last = entries[entries.length - 1];
      window.__webVitals.lcp = last.startTime;
    }).observe({ type: 'largest-contentful-paint', buffered: true });
  } catch(e) {}
  try {
    let clsValue = 0;
    new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        if (!entry.hadRecentInput) clsValue += entry.value;
      }
      window.__webVitals.cls = clsValue;
    }).observe({ type: 'layout-shift', buffered: true });
  } catch(e) {}
  try {
    new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        window.__webVitals.fid = entry.processingStart - entry.startTime;
      }
    }).observe({ type: 'first-input', buffered: true });
  } catch(e) {}
  try {
    new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        if (entry.duration > (window.__webVitals.inp || 0)) {
          window.__webVitals.inp = entry.duration;
        }
      }
    }).observe({ type: 'event', buffered: true, durationThreshold: 16 });
  } catch(e) {}
`;

/**
 * MetricsCollector — collects browser performance metrics via Playwright's
 * page.evaluate() API.  Mirrors the Java MetricsCollector 1-to-1.
 *
 * Usage (same pattern as Java):
 *   await collector.injectWebVitalsObserver();   // BEFORE navigation
 *   await router.navigateAndPrepare(url);
 *   const metrics = await collector.collect('My Page');
 */
export class MetricsCollector {
  private page: Page;
  private config: ConfigReader;

  constructor(page: Page) {
    this.page = page;
    this.config = ConfigReader.getInstance();
    // Also register as init-script so the observer fires even on full page reloads.
    page.addInitScript(WEB_VITALS_SCRIPT).catch(() => {});
  }

  /**
   * Injects the Web Vitals PerformanceObserver into the current page context.
   * Call this BEFORE navigation so LCP/CLS events are captured from first paint.
   */
  async injectWebVitalsObserver(): Promise<void> {
    try {
      await this.page.evaluate(WEB_VITALS_SCRIPT);
    } catch (e) {
      console.warn('Could not inject Web Vitals observer:', e);
    }
  }

  /**
   * Waits for the page to be fully ready, then collects all performance metrics.
   */
  async collect(pageName: string): Promise<PerformanceMetrics> {
    await this.waitForPageReady();

    const metrics = createEmptyMetrics();
    metrics.pageName = pageName;
    metrics.pageUrl  = this.page.url();

    await this.collectNavigationTiming(metrics);
    await this.collectWebVitals(metrics);
    await this.collectResourceMetrics(metrics);
    await this.collectSlowApiRequests(metrics);

    const screenshotPath = await ScreenshotManager.capture(
      this.page,
      pageName,
      this.config.getScreenshotPath(),
      this.config.getReportPath(),
    );
    if (screenshotPath) metrics.screenshotPath = screenshotPath;

    evaluateBenchmarks(metrics);
    return metrics;
  }

  // ── Navigation Timing ────────────────────────────────────────────────────────

  private async collectNavigationTiming(metrics: PerformanceMetrics): Promise<void> {
    const navScript = `(() => {
      const t = performance.getEntriesByType('navigation')[0] || performance.timing;
      const nav = t.toJSON ? t.toJSON() : t;
      return {
        dnsLookup:        (nav.domainLookupEnd   || 0) - (nav.domainLookupStart  || 0),
        tcpConnect:       (nav.connectEnd         || 0) - (nav.connectStart       || 0),
        ttfb:             (nav.responseStart      || 0) - (nav.requestStart        || 0),
        requestTime:      (nav.responseStart      || 0) - (nav.requestStart        || 0),
        responseTime:     (nav.responseEnd        || 0) - (nav.responseStart      || 0),
        domInteractive:   (nav.domInteractive     || 0) - (nav.startTime          || nav.fetchStart || 0),
        domContentLoaded: (nav.domContentLoadedEventEnd || 0) - (nav.startTime    || nav.fetchStart || 0),
        pageLoad:         (nav.loadEventEnd       || 0) - (nav.startTime          || nav.fetchStart || 0),
        redirect:         (nav.redirectEnd        || 0) - (nav.redirectStart      || 0)
      };
    })()`;
    try {
      const t = await this.page.evaluate(navScript) as Record<string, number>;
      metrics.dnsLookupTime       = t.dnsLookup       || 0;
      metrics.tcpConnectionTime   = t.tcpConnect       || 0;
      metrics.timeToFirstByte     = t.ttfb             || 0;
      metrics.requestTime         = t.requestTime      || 0;
      metrics.responseTime        = t.responseTime     || 0;
      metrics.domInteractiveTime  = t.domInteractive   || 0;
      metrics.domContentLoadedTime = t.domContentLoaded || 0;
      metrics.pageLoadTime        = t.pageLoad         || 0;
      metrics.redirectTime        = t.redirect         || 0;
    } catch (e) {
      console.warn('Navigation timing collection failed:', e);
    }

    // Time to Interactive: max(domInteractive, end of last long task)
    const ttiScript = `(() => {
      const nav = performance.getEntriesByType('navigation')[0];
      const navStart = nav ? nav.startTime : 0;
      const domInteractive = nav ? (nav.domInteractive - navStart) : 0;
      const longTasks = performance.getEntriesByType('longtask') || [];
      let lastLongTaskEnd = 0;
      for (const task of longTasks) {
        const taskEnd = (task.startTime - navStart) + task.duration;
        if (taskEnd > lastLongTaskEnd) lastLongTaskEnd = taskEnd;
      }
      return Math.max(domInteractive, lastLongTaskEnd);
    })()`;
    try {
      metrics.timeToInteractive = (await this.page.evaluate(ttiScript) as number) || 0;
    } catch {}
  }

  // ── Core Web Vitals ──────────────────────────────────────────────────────────

  private async collectWebVitals(metrics: PerformanceMetrics): Promise<void> {
    // FCP via Performance API
    try {
      const fcp = await this.page.evaluate(`
        const entries = performance.getEntriesByName('first-contentful-paint');
        return entries.length > 0 ? entries[0].startTime : 0;
      `) as number;
      metrics.firstContentfulPaint = fcp || 0;
    } catch {}

    // LCP, CLS, FID, INP from pre-injected observer
    try {
      const vitals = await this.page.evaluate(`
        const v = window.__webVitals || {};
        return { lcp: v.lcp || 0, cls: v.cls || 0, fid: v.fid || 0, inp: v.inp || 0 };
      `) as Record<string, number>;
      metrics.largestContentfulPaint = vitals.lcp || 0;
      metrics.cumulativeLayoutShift  = vitals.cls || 0;
      metrics.firstInputDelay        = vitals.fid || 0;
      metrics.interactionToNextPaint = vitals.inp || 0;
    } catch {}

    // Total Blocking Time
    try {
      const tbt = await this.page.evaluate(`
        let tbt = 0;
        for (const entry of performance.getEntriesByType('longtask') || []) {
          const blocking = entry.duration - 50;
          if (blocking > 0) tbt += blocking;
        }
        return tbt;
      `) as number;
      metrics.totalBlockingTime = tbt || 0;
    } catch {}
  }

  // ── Resource Metrics ─────────────────────────────────────────────────────────

  private async collectResourceMetrics(metrics: PerformanceMetrics): Promise<void> {
    try {
      const res = await this.page.evaluate(`
        const resources = performance.getEntriesByType('resource');
        let totalSize = 0;
        resources.forEach(r => { totalSize += (r.transferSize || 0); });
        return { count: resources.length, size: totalSize };
      `) as { count: number; size: number };
      metrics.totalResources = res.count || 0;
      metrics.transferSize   = res.size  || 0;
    } catch {}
  }

  // ── Slow API Requests ────────────────────────────────────────────────────────

  private async collectSlowApiRequests(metrics: PerformanceMetrics): Promise<void> {
    const THRESHOLD = 1000;
    try {
      const raw = await this.page.evaluate(`
        var threshold = ${THRESHOLD};
        var resources = performance.getEntriesByType('resource');
        var slow = [];
        resources.forEach(function(r) {
          if ((r.initiatorType === 'xmlhttprequest' || r.initiatorType === 'fetch')
              && r.duration >= threshold) {
            slow.push({ url: r.name, duration: r.duration, type: r.initiatorType });
          }
        });
        slow.sort(function(a, b) { return b.duration - a.duration; });
        return slow;
      `) as Array<{ url: string; duration: number; type: string }>;

      if (raw && raw.length > 0) {
        metrics.slowApiRequests = raw.map(r => ({
          url:        String(r.url),
          durationMs: Number(r.duration),
          type:       String(r.type),
        }));
      }
    } catch {}
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────

  private async waitForPageReady(): Promise<void> {
    try {
      await this.page.waitForFunction(() => document.readyState === 'complete', { timeout: 30_000 });
    } catch {}
    // Extra wait for LCP/CLS observers to settle (mirrors Java Thread.sleep(1500))
    await this.page.waitForTimeout(1500);
  }
}
