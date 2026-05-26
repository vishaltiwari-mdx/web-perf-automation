import { ApiRequestMetric } from './ApiRequestMetric';
import { LighthouseMetrics } from './LighthouseMetrics';

export type BenchmarkStatus = 'GOOD' | 'NEEDS IMPROVEMENT' | 'POOR' | 'N/A';

/**
 * PerformanceMetrics — data model holding all collected metrics for a single page navigation.
 * Plain object (no class methods on instances); use module-level helpers.
 */
export interface PerformanceMetrics {
  pageName: string;
  pageUrl: string;
  timestamp: number;

  // Navigation Timing (ms)
  dnsLookupTime: number;
  tcpConnectionTime: number;
  timeToFirstByte: number;
  domContentLoadedTime: number;
  pageLoadTime: number;
  domInteractiveTime: number;
  redirectTime: number;
  requestTime: number;
  responseTime: number;

  // Core Web Vitals
  largestContentfulPaint: number;
  firstContentfulPaint: number;
  cumulativeLayoutShift: number;
  interactionToNextPaint: number;
  firstInputDelay: number;
  totalBlockingTime: number;

  // Resource
  totalResources: number;
  transferSize: number;  // bytes
  speedIndex: number;

  // TTI
  timeToInteractive: number;

  // AG-Grid spinner duration (ms, 0 = no grid on this page)
  agGridLoadTime: number;

  /**
   * Wall-clock time from the moment the navigation action fires (click or goto)
   * to when the page readiness handler confirms the page is fully interactive.
   * Measured by NavigationRouter.navigateAndPrepare() using Date.now().
   * Unlike pageLoadTime (which uses browser navigationStart), this captures
   * the full end-to-end latency including click-handler processing time.
   */
  actionToLoadMs: number;

  // Slow API requests (>= 300 ms)
  slowApiRequests: ApiRequestMetric[];

  // Benchmark statuses
  lcpStatus: BenchmarkStatus;
  fcpStatus: BenchmarkStatus;
  clsStatus: BenchmarkStatus;
  ttfbStatus: BenchmarkStatus;
  pageLoadStatus: BenchmarkStatus;
  ttiStatus: BenchmarkStatus;
  overallStatus: BenchmarkStatus;

  screenshotPath?: string;

  // Optional Lighthouse audit results (populated when running via PageLoadAnalyzerAgent)
  lighthouseData?: LighthouseMetrics;
}

// ── Factory ────────────────────────────────────────────────────────────────────

export function createEmptyMetrics(): PerformanceMetrics {
  return {
    pageName: '',
    pageUrl: '',
    timestamp: Date.now(),
    dnsLookupTime: 0,
    tcpConnectionTime: 0,
    timeToFirstByte: 0,
    domContentLoadedTime: 0,
    pageLoadTime: 0,
    domInteractiveTime: 0,
    redirectTime: 0,
    requestTime: 0,
    responseTime: 0,
    largestContentfulPaint: 0,
    firstContentfulPaint: 0,
    cumulativeLayoutShift: 0,
    interactionToNextPaint: 0,
    firstInputDelay: 0,
    totalBlockingTime: 0,
    totalResources: 0,
    transferSize: 0,
    speedIndex: 0,
    timeToInteractive: 0,
    agGridLoadTime: 0,
    actionToLoadMs: 0,
    slowApiRequests: [],
    lcpStatus: 'N/A',
    fcpStatus: 'N/A',
    clsStatus: 'N/A',
    ttfbStatus: 'N/A',
    pageLoadStatus: 'N/A',
    ttiStatus: 'N/A',
    overallStatus: 'N/A',
  };
}

// ── Benchmark evaluation ───────────────────────────────────────────────────────

export function evaluateBenchmarks(m: PerformanceMetrics): void {
  m.lcpStatus      = evalLCP(m.largestContentfulPaint);
  m.fcpStatus      = evalFCP(m.firstContentfulPaint);
  m.clsStatus      = evalCLS(m.cumulativeLayoutShift);
  m.ttfbStatus     = evalTTFB(m.timeToFirstByte);
  m.pageLoadStatus = evalPageLoad(m.pageLoadTime);
  m.ttiStatus      = evalTTI(m.timeToInteractive);

  const statuses = [m.lcpStatus, m.fcpStatus, m.clsStatus, m.ttfbStatus, m.pageLoadStatus, m.ttiStatus];
  const allGood = statuses.every(s => s === 'GOOD' || s === 'N/A');
  const anyPoor = statuses.some(s => s === 'POOR');
  m.overallStatus = anyPoor ? 'POOR' : allGood ? 'GOOD' : 'NEEDS IMPROVEMENT';
}

function evalLCP(v: number): BenchmarkStatus {
  if (v <= 0) return 'N/A';
  return v <= 2500 ? 'GOOD' : v <= 4000 ? 'NEEDS IMPROVEMENT' : 'POOR';
}
function evalFCP(v: number): BenchmarkStatus {
  if (v <= 0) return 'N/A';
  return v <= 1800 ? 'GOOD' : v <= 3000 ? 'NEEDS IMPROVEMENT' : 'POOR';
}
function evalCLS(v: number): BenchmarkStatus {
  if (v < 0) return 'N/A';
  return v <= 0.1 ? 'GOOD' : v <= 0.25 ? 'NEEDS IMPROVEMENT' : 'POOR';
}
function evalTTFB(v: number): BenchmarkStatus {
  if (v <= 0) return 'N/A';
  return v <= 800 ? 'GOOD' : v <= 1800 ? 'NEEDS IMPROVEMENT' : 'POOR';
}
function evalPageLoad(v: number): BenchmarkStatus {
  if (v <= 0) return 'N/A';
  return v <= 3000 ? 'GOOD' : v <= 6000 ? 'NEEDS IMPROVEMENT' : 'POOR';
}
function evalTTI(v: number): BenchmarkStatus {
  if (v <= 0) return 'N/A';
  return v <= 3800 ? 'GOOD' : v <= 7300 ? 'NEEDS IMPROVEMENT' : 'POOR';
}

// ── toString helper ────────────────────────────────────────────────────────────

export function metricsToString(m: PerformanceMetrics): string {
  return `[${m.pageName}] Load=${m.pageLoadTime.toFixed(0)}ms | FCP=${m.firstContentfulPaint.toFixed(0)}ms | LCP=${m.largestContentfulPaint.toFixed(0)}ms | TTI=${m.timeToInteractive.toFixed(0)}ms | CLS=${m.cumulativeLayoutShift.toFixed(3)} | TTFB=${m.timeToFirstByte.toFixed(0)}ms | AG-Grid=${m.agGridLoadTime.toFixed(0)}ms | Overall=${m.overallStatus}`;
}
