import { Page, Request } from '@playwright/test';
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
interface TrackedRequest {
  url:          string;
  resourceType: string;   // 'xhr' | 'fetch' | 'document' | 'script' | 'stylesheet' | ...
  durationMs:   number;
  transferSize: number;
}

export class MetricsCollector {
  private page: Page;
  private config: ConfigReader;
  // Playwright-tracked network responses — populated regardless of whether the
  // SPA calls performance.clearResourceTimings(). Source of truth for the
  // Resource Summary and Slow API Requests tables.
  private trackedRequests: TrackedRequest[] = [];
  // Wall-clock fallback: Playwright's request.timing() returns -1 for cached
  // / 304 responses on many builds. We start our own timer on 'request' and
  // close it on 'response' so we always have a duration.
  private requestStartedAt: Map<Request, number> = new Map();

  constructor(page: Page) {
    this.page = page;
    this.config = ConfigReader.getInstance();
    page.addInitScript(WEB_VITALS_SCRIPT).catch(() => {});

    page.on('request', (req) => {
      this.requestStartedAt.set(req, Date.now());
    });

    // Push synchronously on 'response' (early, reliable). Helix SPAs clear
    // performance.getEntriesByType('resource'), so we MUST capture via
    // Playwright events instead — those happen out-of-process.
    page.on('response', (resp) => {
      const req     = resp.request();
      const startTs = this.requestStartedAt.get(req) ?? Date.now();
      let durationMs = Math.max(0, Date.now() - startTs);

      // Prefer Playwright's precise timing when available
      try {
        const t = req.timing();
        if (t && t.responseEnd >= 0 && t.startTime >= 0 && t.responseEnd > t.startTime) {
          durationMs = t.responseEnd - t.startTime;
        }
      } catch { /* timing not available — keep wall-clock fallback */ }

      // Transfer size from Content-Length header (fast, sync-ish). We don't
      // call resp.body() / req.sizes() here because those can stall the
      // event queue for large responses.
      let transferSize = 0;
      try {
        const cl = resp.headers()['content-length'];
        if (cl) transferSize = parseInt(cl, 10) || 0;
      } catch { /* headers not available */ }

      this.trackedRequests.push({
        url:          req.url(),
        resourceType: req.resourceType(),
        durationMs,
        transferSize,
      });
      this.requestStartedAt.delete(req);
    });

    page.on('requestfailed', (req) => {
      this.requestStartedAt.delete(req);
    });
  }

  /**
   * Injects the Web Vitals PerformanceObserver into the current page context.
   * Call this BEFORE navigation so LCP/CLS events are captured from first paint.
   *
   * Also resets the network tracker so the upcoming navigation starts from a
   * clean slate — only resources fetched by the TARGET page are counted, not
   * the login flow that preceded it.
   */
  async injectWebVitalsObserver(): Promise<void> {
    this.trackedRequests = [];
    this.requestStartedAt.clear();
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
  // Source of truth: trackedRequests (Playwright network events), NOT the
  // in-page performance.getEntriesByType('resource') buffer — SPAs frequently
  // clear that buffer between grid renders.

  private async collectResourceMetrics(metrics: PerformanceMetrics): Promise<void> {
    metrics.totalResources = this.trackedRequests.length;
    metrics.transferSize   = this.trackedRequests.reduce((sum, r) => sum + (r.transferSize || 0), 0);

    const byType = this.trackedRequests.reduce<Record<string, number>>((acc, r) => {
      acc[r.resourceType] = (acc[r.resourceType] ?? 0) + 1;
      return acc;
    }, {});
    console.info(`[MetricsCollector] tracked ${this.trackedRequests.length} request(s) — ${
      Object.entries(byType).map(([k, v]) => `${k}=${v}`).join(', ') || 'none'
    }`);
  }

  // ── Slow API Requests ────────────────────────────────────────────────────────

  private async collectSlowApiRequests(metrics: PerformanceMetrics): Promise<void> {
    const THRESHOLD = 300;
    const apis = this.trackedRequests.filter(
      r => r.resourceType === 'xhr' || r.resourceType === 'fetch',
    );
    const slow = apis
      .filter(r => r.durationMs >= THRESHOLD)
      .sort((a, b) => b.durationMs - a.durationMs);

    metrics.slowApiRequests = slow.map(r => ({
      url:        r.url,
      durationMs: r.durationMs,
      type:       r.resourceType === 'fetch' ? 'fetch' : 'xmlhttprequest',
    }));

    console.info(`[MetricsCollector] api calls: ${apis.length} total, ${slow.length} slow (>= ${THRESHOLD} ms)`);
    if (slow.length > 0) {
      console.info(`[MetricsCollector] slowest: ${Math.round(slow[0].durationMs)} ms — ${slow[0].url}`);
    }
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────

  private async waitForPageReady(): Promise<void> {
    try {
      await this.page.waitForFunction(() => document.readyState === 'complete', { timeout: 30_000 });
    } catch {}

    // ── Wait for AG-Grid loading overlay to disappear (if present) ─────────────
    // The overlay is visible while the grid fetches its rows via XHR. Without
    // this wait we'd capture metrics mid-load and see 0 resources / 0 slow APIs.
    try {
      await this.page.waitForFunction(
        () => {
          const els = document.querySelectorAll(
            '.ag-overlay-loading-wrapper, .ag-overlay-loading-center, .ag-loading-text',
          );
          return !Array.from(els).some(el => (el as HTMLElement).offsetParent !== null);
        },
        { timeout: 30_000 },
      );
    } catch { /* no grid or overlay still visible — continue */ }

    // ── Wait for network to be quiet (no in-flight requests for ~500 ms) ──────
    // Playwright's 'networkidle' is more reliable than polling the in-page
    // resource buffer, which the Helix SPA clears on every grid render.
    try {
      await this.page.waitForLoadState('networkidle', { timeout: 15_000 });
    } catch { /* still active or timed out — continue */ }

    // Final small buffer for LCP/CLS observers to flush
    await this.page.waitForTimeout(500);
  }
}
