/**
 * LighthouseMetrics — data structures for Google Lighthouse audit results.
 */

export interface LighthouseOpportunity {
  id:            string;
  title:         string;
  description:   string;
  savingsMs?:    number;    // potential time savings in milliseconds
  savingsBytes?: number;    // potential byte savings
  displayValue?: string;    // Lighthouse-formatted display value
}

export interface LighthouseDiagnostic {
  id:            string;
  title:         string;
  description:   string;
  displayValue?: string;
  score:         number | null;  // 0–1 or null if informational
}

export interface LighthouseMetrics {
  // Category scores (0–100)
  performanceScore:   number;
  accessibilityScore: number;
  bestPracticesScore: number;
  seoScore:           number;

  // Lab metrics (milliseconds unless noted)
  firstContentfulPaint:   number;  // FCP
  largestContentfulPaint: number;  // LCP
  speedIndex:             number;
  timeToInteractive:      number;  // TTI
  totalBlockingTime:      number;  // TBT
  cumulativeLayoutShift:  number;  // unitless score
  firstMeaningfulPaint:   number;  // FMP (deprecated but informative)
  serverResponseTime:     number;  // server response time (TTFB-like)

  // Diagnostic metrics
  mainThreadWork: number;  // total main thread blocking time (ms)
  bootupTime:     number;  // JS parse + compile + eval (ms)
  domSize:        number;  // DOM node count

  // Savings estimates
  renderBlockingResourcesSavingsMs: number;
  unusedJavascriptBytes:            number;
  unusedCssBytes:                   number;

  // Opportunities — failing audits that have potential performance savings
  opportunities: LighthouseOpportunity[];

  // Diagnostics — informational audits in the performance category
  diagnostics: LighthouseDiagnostic[];

  // Paths to the saved Lighthouse report files
  htmlReportPath?: string;
  jsonReportPath?: string;

  auditTime: number;
}
