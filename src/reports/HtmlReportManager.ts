import * as fs from 'fs';
import * as path from 'path';
import { PerformanceMetrics } from '../metrics/PerformanceMetrics';
import { ApiRequestMetric } from '../metrics/ApiRequestMetric';
import { LighthouseMetrics } from '../metrics/LighthouseMetrics';
import { ConfigReader } from '../config/ConfigReader';
import * as LogManager from '../utils/LogManager';

// ── Module-level state (mirrors Java static fields) ────────────────────────────

let reportPath    = '';
let reportSections: string[] = [];

// When non-null, only these metric keys (e.g. ['LCP','CLS','API']) appear in
// the report. When null, the full set is rendered (default behavior).
let selectedMetrics: Set<string> | null = null;

export function setSelectedMetrics(keys: string[] | null): void {
  selectedMetrics = keys && keys.length ? new Set(keys.map(k => k.toUpperCase())) : null;
}

function isMetricSelected(key: string): boolean {
  return !selectedMetrics || selectedMetrics.has(key);
}

function timestamp(): string {
  return new Date().toISOString().replace(/T/, ' ').replace(/\..+/, '');
}

function tsFileSafe(): string {
  return new Date().toISOString().replace(/[:.]/g, '-').replace('T', '_').slice(0, 23);
}

// ── Public API ─────────────────────────────────────────────────────────────────

export function init(): void {
  const config  = ConfigReader.getInstance();
  const outDir  = config.getReportPath();
  fs.mkdirSync(outDir, { recursive: true });
  reportPath     = path.join(outDir, `performance-report-${tsFileSafe()}.html`);
  reportSections = [];
  console.info(`[HtmlReport] Initialized: ${reportPath}`);
}

export function initForPage(pageLabel: string): void {
  const config   = ConfigReader.getInstance();
  const outDir   = config.getReportPath();
  fs.mkdirSync(outDir, { recursive: true });
  const safeName = pageLabel.replace(/[^a-zA-Z0-9._-]/g, '_');
  reportPath     = path.join(outDir, `performance-report-${safeName}-${tsFileSafe()}.html`);
  reportSections = [];
}

export function initCombined(): void {
  const config  = ConfigReader.getInstance();
  const outDir  = config.getReportPath();
  fs.mkdirSync(outDir, { recursive: true });
  reportPath     = path.join(outDir, `performance-report-combined-${tsFileSafe()}.html`);
  reportSections = [];
}

export function getReportPath(): string {
  return reportPath;
}

/** True when at least one metrics section has been added — used by globalTeardown
 *  to skip flushing an empty report when DynamicUrlTest manages its own lifecycle. */
export function hasSections(): boolean {
  return reportSections.length > 0;
}

export function addMetrics(testName: string, metrics: PerformanceMetrics): void {
  const statusColor = statusBg(metrics.overallStatus);
  let html = `
  <section class="test-section">
    <h2 class="test-name">${esc(testName)}</h2>
    <div class="status-badge" style="background:${statusColor}">Overall: ${metrics.overallStatus}</div>
    <p class="url-line">URL: <a href="${esc(metrics.pageUrl)}" target="_blank">${esc(metrics.pageUrl)}</a></p>
  `;
  html += attachScreenshot(metrics);
  html += buildCoreWebVitalsTable(metrics);
  html += buildNavigationTimingTable(metrics);
  html += buildResourceTable(metrics);
  html += buildSlowApiRequestsTable(metrics);
  if (metrics.lighthouseData) html += buildLighthouseSection(metrics.lighthouseData);
  html += '</section>';
  reportSections.push(html);
}

export function addMetricsNoScreenshot(testName: string, metrics: PerformanceMetrics): void {
  const statusColor = statusBg(metrics.overallStatus);
  let html = `
  <section class="test-section">
    <h2 class="test-name">${esc(testName)}</h2>
    <div class="status-badge" style="background:${statusColor}">Overall: ${metrics.overallStatus}</div>
    <p class="url-line">URL: <a href="${esc(metrics.pageUrl)}" target="_blank">${esc(metrics.pageUrl)}</a></p>
  `;
  html += buildCoreWebVitalsTable(metrics);
  html += buildNavigationTimingTable(metrics);
  html += buildResourceTable(metrics);
  html += buildSlowApiRequestsTable(metrics);
  if (metrics.lighthouseData) html += buildLighthouseSection(metrics.lighthouseData);
  html += '</section>';
  reportSections.push(html);
}

export function addSummaryTable(metricsList: PerformanceMetrics[]): void {
  reportSections.push(buildSummaryTable(metricsList));
  if (!selectedMetrics) reportSections.push(buildBenchmarkReference());
}

export function flush(): void {
  if (!reportPath) return;
  // Compute a relative path from the HTML file's directory to the log file
  // so the link works when the report is opened directly from reports/
  const absLog     = LogManager.getLogPath();
  const relLogPath = absLog
    ? path.relative(path.dirname(path.resolve(reportPath)), path.resolve(absLog)).replace(/\\/g, '/')
    : '';
  const logSection = buildLogSection(LogManager.getEntries(), relLogPath, absLog);
  const html = wrapHtml(reportSections.join('\n') + logSection);
  fs.writeFileSync(reportPath, html, 'utf8');
  console.info(`[HtmlReport] Saved: ${reportPath}`);
}

// ── Table builders ─────────────────────────────────────────────────────────────

function buildCoreWebVitalsTable(m: PerformanceMetrics): string {
  const rows: Array<[string, string]> = [
    ['LCP',  row('LCP (Largest Contentful Paint)',  fmt(m.largestContentfulPaint) + ' ms', '&le; 2500 ms', m.lcpStatus)],
    ['FCP',  row('FCP (First Contentful Paint)',    fmt(m.firstContentfulPaint)   + ' ms', '&le; 1800 ms', m.fcpStatus)],
    ['TTI',  row('TTI (Time to Interactive)',       fmt(m.timeToInteractive)       + ' ms', '&le; 3800 ms', m.ttiStatus)],
    ['CLS',  row('CLS (Cumulative Layout Shift)',   m.cumulativeLayoutShift.toFixed(4),    '&le; 0.1',     m.clsStatus)],
    ['TTFB', row('TTFB (Time to First Byte)',       fmt(m.timeToFirstByte)        + ' ms', '&le; 800 ms',  m.ttfbStatus)],
    ['INP',  row('INP (Interaction to Next Paint)', fmt(m.interactionToNextPaint) + ' ms', '&le; 200 ms', 'INFO')],
    ['TBT',  row('TBT (Total Blocking Time)',       fmt(m.totalBlockingTime)       + ' ms', '&le; 200 ms', 'INFO')],
  ];
  const visible = rows.filter(([k]) => isMetricSelected(k));
  if (visible.length === 0) return '';
  return `<h4>Core Web Vitals</h4>
  <table>
    <tr class="th-row"><th>Metric</th><th>Value</th><th>Target</th><th>Status</th></tr>
    ${visible.map(([, html]) => html).join('\n')}
  </table>`;
}

function buildNavigationTimingTable(m: PerformanceMetrics): string {
  const agGridRow = m.agGridLoadTime > 0
    ? highlightRow('AG-Grid Spinner Time', fmt(m.agGridLoadTime) + ' ms',
        m.agGridLoadTime < 1000 ? '#1a3a1a' : m.agGridLoadTime < 3000 ? '#3a3a1a' : '#3a1a1a')
    : infoRow('AG-Grid Spinner Time', 'N/A (no grid on this page)');

  // ['<key>', '<html>', <whether to keep when no filter group matches>]
  //   keyless rows ('') are only shown in unfiltered (default) mode
  const rows: Array<[string, string]> = [
    ['DNS', infoRow('DNS Lookup Time',     fmt(m.dnsLookupTime)      + ' ms')],
    ['TCP', infoRow('TCP Connection Time', fmt(m.tcpConnectionTime)  + ' ms')],
    ['',    infoRow('Redirect Time',       fmt(m.redirectTime)       + ' ms')],
    ['',    infoRow('Request Time',        fmt(m.requestTime)        + ' ms')],
    ['',    infoRow('Response Time',       fmt(m.responseTime)       + ' ms')],
    ['',    infoRow('DOM Interactive',     fmt(m.domInteractiveTime) + ' ms')],
    ['DCL', infoRow('DOM Content Loaded',  fmt(m.domContentLoadedTime) + ' ms')],
    ['PLT', infoRow('Full Page Load',      fmt(m.pageLoadTime)       + ' ms')],
    ['AGG', agGridRow],
    ['',    m.actionToLoadMs > 0
              ? highlightRow('Action → Page Ready', fmt(m.actionToLoadMs) + ' ms',
                  m.actionToLoadMs < 3000 ? '#1a2a3a' : m.actionToLoadMs < 8000 ? '#3a3a1a' : '#3a1a1a')
              : infoRow('Action → Page Ready', 'N/A')],
  ];
  const visible = rows.filter(([k]) => k ? isMetricSelected(k) : !selectedMetrics);
  if (visible.length === 0) return '';
  return `<h4>Navigation Timing</h4>
  <table>
    <tr class="th-row"><th>Metric</th><th>Value</th></tr>
    ${visible.map(([, html]) => html).join('\n')}
  </table>`;
}

function buildResourceTable(m: PerformanceMetrics): string {
  // Resource Summary has no metric-pill equivalent — hide it entirely in custom mode
  if (selectedMetrics) return '';
  const sizeKb = Math.round(m.transferSize / 1024);
  return `<h4>Resource Summary</h4>
  <table>
    <tr class="th-row"><th>Metric</th><th>Value</th></tr>
    ${infoRow('Total Resources', String(m.totalResources))}
    ${infoRow('Transfer Size',   sizeKb + ' KB')}
  </table>`;
}

function buildSlowApiRequestsTable(m: PerformanceMetrics): string {
  if (!isMetricSelected('API')) return '';
  const slowApis: ApiRequestMetric[] = m.slowApiRequests ?? [];
  if (slowApis.length === 0) {
    return `<h4>Slow API Requests (&gt; 1 second)</h4>
    <p style="color:#888;margin:4px 0">No API requests exceeded 1 second on this page.</p>`;
  }
  let rows = '';
  slowApis.forEach((api, i) => {
    const bg = api.durationMs >= 5000 ? '#5a0a0a' : api.durationMs >= 3000 ? '#3a1a1a' : '#3a2a1a';
    rows += `<tr style="background:${bg}">
      <td style="text-align:center">${i + 1}</td>
      <td style="word-break:break-all;font-size:12px">${esc(api.url)}</td>
      <td style="text-align:center"><b>${Math.round(api.durationMs)} ms</b></td>
      <td style="text-align:center">${esc(api.type)}</td>
    </tr>`;
  });
  return `<h4>Slow API Requests (&gt; 1 second) &nbsp;<span style="color:#ff6b6b;font-size:13px">${slowApis.length} found</span></h4>
  <table>
    <tr class="th-row" style="background:#3a1a1a"><th>#</th><th>API URL</th><th>Duration</th><th>Type</th></tr>
    ${rows}
  </table>`;
}

function buildSummaryTable(list: PerformanceMetrics[]): string {
  // Column config: [pill-key | '_always', <header html>, <cell renderer>]
  //   '_always' columns (Page, Overall) are never filtered.
  type Col = ['_always' | string, string, (m: PerformanceMetrics) => string];
  const cols: Col[] = [
    ['_always', 'Page',             m => `<td>${esc(m.pageName)}</td>`],
    ['PLT',    'Load Time',         m => `<td>${fmt(m.pageLoadTime)} ms</td>`],
    ['FCP',    'FCP',               m => `<td>${fmt(m.firstContentfulPaint)} ms</td>`],
    ['LCP',    'LCP',               m => `<td>${fmt(m.largestContentfulPaint)} ms</td>`],
    ['TTI',    'TTI',               m => `<td>${fmt(m.timeToInteractive)} ms</td>`],
    ['CLS',    'CLS',               m => `<td>${m.cumulativeLayoutShift.toFixed(3)}</td>`],
    ['TTFB',   'TTFB',              m => `<td>${fmt(m.timeToFirstByte)} ms</td>`],
    ['TBT',    'TBT',               m => `<td>${fmt(m.totalBlockingTime)} ms</td>`],
    ['INP',    'INP',               m => `<td>${fmt(m.interactionToNextPaint)} ms</td>`],
    ['DNS',    'DNS',               m => `<td>${fmt(m.dnsLookupTime)} ms</td>`],
    ['TCP',    'TCP',               m => `<td>${fmt(m.tcpConnectionTime)} ms</td>`],
    ['DCL',    'DCL',               m => `<td>${fmt(m.domContentLoadedTime)} ms</td>`],
    ['AGG',    'AG-Grid',           m => `<td>${m.agGridLoadTime > 0 ? fmt(m.agGridLoadTime) + ' ms' : '&mdash;'}</td>`],
    ['API',    'Slow APIs (&gt;1s)', m => {
      const c  = (m.slowApiRequests ?? []).length;
      const bg = c === 0 ? '#1a3a1a' : c <= 2 ? '#3a3a1a' : '#3a1a1a';
      return `<td style="background:${bg};font-weight:bold">${c === 0 ? '&mdash;' : c}</td>`;
    }],
    // Resources / Transfer have no pill — only shown when unfiltered
    ['_resources', 'Resources', m => `<td>${m.totalResources}</td>`],
    ['_resources', 'Transfer',  m => `<td>${Math.round(m.transferSize / 1024)} KB</td>`],
    ['_always',    'Overall',   m => `<td><b>${m.overallStatus}</b></td>`],
  ];

  const visible = cols.filter(([k]) =>
    k === '_always' ? true :
    k === '_resources' ? !selectedMetrics :
    isMetricSelected(k),
  );

  const header = visible.map(([, h]) => `<th>${h}</th>`).join('');
  const rows   = list.map(m => {
    const bg = m.overallStatus === 'GOOD' ? '#1a3a1a' : m.overallStatus === 'POOR' ? '#3a1a1a' : '#3a3a1a';
    return `<tr style="background:${bg};text-align:center">${visible.map(([, , render]) => render(m)).join('')}</tr>`;
  }).join('');

  return `<section class="test-section">
  <h3>Performance Summary — All Pages</h3>
  <table style="font-size:13px">
    <tr class="th-row" style="background:#1a1a2e;text-align:center">${header}</tr>
    ${rows}
  </table>
  </section>`;
}

function buildLighthouseSection(lh: LighthouseMetrics): string {
  const scoreColor = (s: number) =>
    s >= 90 ? '#28a745' : s >= 50 ? '#ffc107' : '#dc3545';

  const gauge = (score: number, label: string): string => {
    const color = scoreColor(score);
    const pct   = score;
    return `<div style="text-align:center;margin:0 14px">
      <div style="width:84px;height:84px;border-radius:50%;
        background:conic-gradient(${color} ${pct}%,#2d2d2d ${pct}%);
        display:flex;align-items:center;justify-content:center;margin:0 auto 6px">
        <div style="width:64px;height:64px;border-radius:50%;background:#0f2840;
          display:flex;align-items:center;justify-content:center;
          font-size:20px;font-weight:bold;color:${color}">${score}</div>
      </div>
      <div style="font-size:12px;color:#aaa">${label}</div>
    </div>`;
  };

  const labRows = [
    ['FCP',               fmtLh(lh.firstContentfulPaint),    '&le; 1800 ms'],
    ['LCP',               fmtLh(lh.largestContentfulPaint),  '&le; 2500 ms'],
    ['Speed Index',       fmtLh(lh.speedIndex),              '&le; 3400 ms'],
    ['TTI',               fmtLh(lh.timeToInteractive),       '&le; 3800 ms'],
    ['TBT',               fmtLh(lh.totalBlockingTime),       '&le; 200 ms'],
    ['CLS',               lh.cumulativeLayoutShift.toFixed(4), '&le; 0.1'],
    ['FMP',               fmtLh(lh.firstMeaningfulPaint),    'Informational'],
    ['Server Response',   fmtLh(lh.serverResponseTime),      '&le; 600 ms'],
    ['Main Thread Work',  fmtLh(lh.mainThreadWork),          'Informational'],
    ['JS Bootup Time',    fmtLh(lh.bootupTime),              'Informational'],
    ['DOM Size',          `${Math.round(lh.domSize)} nodes`,  '&le; 1500 nodes'],
  ].map(([m, v, t]) =>
    `<tr><td>${m}</td><td><b>${v}</b></td><td style="color:#888">${t}</td></tr>`,
  ).join('');

  let oppRows: string;
  if (lh.opportunities.length === 0) {
    oppRows = `<tr><td colspan="3" style="color:#4caf50;text-align:center">No failing opportunities — well done!</td></tr>`;
  } else {
    oppRows = lh.opportunities.map(op => {
      const savings = op.savingsMs    != null ? `${op.savingsMs} ms`
                    : op.savingsBytes != null ? `${Math.round(op.savingsBytes / 1024)} KB`
                    : (op.displayValue ?? '—');
      return `<tr style="background:#2a1e0a">
        <td><b>${esc(op.title)}</b></td>
        <td style="font-size:12px;color:#aaa">${esc(op.description)}</td>
        <td style="text-align:center;color:#ffc107;white-space:nowrap"><b>${savings}</b></td>
      </tr>`;
    }).join('');
  }

  let diagRows: string;
  if (lh.diagnostics.length === 0) {
    diagRows = `<tr><td colspan="3" style="color:#888;text-align:center">No diagnostics</td></tr>`;
  } else {
    diagRows = lh.diagnostics.map(d => {
      const sc = d.score === null ? '#888'
               : d.score >= 0.9  ? '#28a745'
               : d.score >= 0.5  ? '#ffc107'
               : '#dc3545';
      const val = d.displayValue ?? (d.score !== null ? Math.round(d.score * 100).toString() : '—');
      return `<tr>
        <td><b>${esc(d.title)}</b></td>
        <td style="font-size:12px;color:#aaa">${esc(d.description)}</td>
        <td style="text-align:center;color:${sc}">${esc(val)}</td>
      </tr>`;
    }).join('');
  }

  const savingsRow = (label: string, bytes: number) =>
    bytes > 0
      ? `<tr><td>${label}</td><td><b style="color:#ffc107">${Math.round(bytes / 1024)} KB</b></td></tr>`
      : `<tr><td>${label}</td><td><b style="color:#28a745">None</b></td></tr>`;

  const bytesSummary = `
    <h4 style="color:#ccc">Byte Savings Estimates</h4>
    <table style="width:50%">
      <tr class="th-row"><th>Category</th><th>Potential Savings</th></tr>
      ${savingsRow('Unused JavaScript', lh.unusedJavascriptBytes)}
      ${savingsRow('Unused CSS', lh.unusedCssBytes)}
      ${lh.renderBlockingResourcesSavingsMs > 0
        ? `<tr><td>Render-Blocking Resources</td><td><b style="color:#ffc107">${Math.round(lh.renderBlockingResourcesSavingsMs)} ms</b></td></tr>`
        : `<tr><td>Render-Blocking Resources</td><td><b style="color:#28a745">None</b></td></tr>`}
    </table>`;

  const lhLink = lh.htmlReportPath
    ? `<p style="margin-top:10px"><a href="${esc(lh.htmlReportPath.replace(/\\/g, '/'))}"
        target="_blank" style="color:#6fa3ef;font-weight:bold">
        &#128196; Open Full Lighthouse Report &nearr;</a></p>`
    : '';

  return `<div style="border:1px solid #2a4a6a;border-radius:6px;padding:16px;margin-top:16px;background:#0a1e30">
    <h4 style="color:#6fa3ef;margin:0 0 14px;font-size:16px">&#128270; Lighthouse Audit</h4>

    <div style="display:flex;justify-content:center;flex-wrap:wrap;gap:8px;margin-bottom:20px">
      ${gauge(lh.performanceScore,   'Performance')}
      ${gauge(lh.accessibilityScore, 'Accessibility')}
      ${gauge(lh.bestPracticesScore, 'Best Practices')}
      ${gauge(lh.seoScore,           'SEO')}
    </div>

    <h4 style="color:#ccc">Lab Metrics</h4>
    <table>
      <tr class="th-row"><th>Metric</th><th>Value</th><th>Target</th></tr>
      ${labRows}
    </table>

    ${bytesSummary}

    <h4 style="color:#ccc">Opportunities <span style="font-size:12px;color:#aaa">(failing audits with savings)</span></h4>
    <table>
      <tr class="th-row" style="background:#2a1800"><th>Opportunity</th><th>Description</th><th>Est. Savings</th></tr>
      ${oppRows}
    </table>

    <h4 style="color:#ccc">Diagnostics</h4>
    <table>
      <tr class="th-row"><th>Diagnostic</th><th>Description</th><th>Value</th></tr>
      ${diagRows}
    </table>
    ${lhLink}
  </div>`;
}

function fmtLh(ms: number): string {
  return ms > 0 ? `${Math.round(ms)} ms` : 'N/A';
}

// relLogPath — relative href used in the <a> link (e.g. "run-2026-04-16.log")
// absLogPath — absolute/full path shown as the link label for human readability
function buildLogSection(
  entries:    LogManager.LogEntry[],
  relLogPath: string,
  absLogPath: string,
): string {
  if (entries.length === 0) return '';

  const counts = { INFO: 0, WARN: 0, ERROR: 0, DEBUG: 0 };
  entries.forEach(e => { counts[e.level] = (counts[e.level] ?? 0) + 1; });

  const levelColor = (l: string) =>
    l === 'ERROR' ? '#ff6b6b' :
    l === 'WARN'  ? '#ffc107' :
    l === 'DEBUG' ? '#888'    : '#a0c4ff';

  const rowsBuf: string[] = [];
  entries.forEach((e, i) => {
    const color = levelColor(e.level);
    const bg    = e.level === 'ERROR' ? 'background:#2a0a0a'
                : e.level === 'WARN'  ? 'background:#2a2000'
                : i % 2 === 0         ? ''
                : 'background:#12192a';
    rowsBuf.push(
      `<tr style="${bg}">` +
      `<td style="color:#888;white-space:nowrap;font-size:11px">${esc(e.ts)}</td>` +
      `<td style="color:${color};font-weight:bold;text-align:center;white-space:nowrap">${e.level}</td>` +
      `<td style="font-family:monospace;font-size:12px;word-break:break-word">${esc(e.message)}</td>` +
      `</tr>`,
    );
  });

  // relLogPath is relative to the HTML file — browser can resolve it directly
  const fileLink = relLogPath
    ? `<a href="${esc(relLogPath)}" style="color:#6fa3ef;font-size:13px" target="_blank">&#128196; ${esc(absLogPath || relLogPath)}</a>`
    : '';

  return `
  <section class="test-section" id="log-section">
    <h2 class="test-name" style="cursor:pointer" onclick="toggleLog()">
      &#128221; Run Log
      <span style="font-size:13px;font-weight:normal;color:#888;margin-left:12px">
        <span style="color:#a0c4ff">${counts.INFO} INFO</span>
        &nbsp;|&nbsp;<span style="color:#ffc107">${counts.WARN} WARN</span>
        &nbsp;|&nbsp;<span style="color:#ff6b6b">${counts.ERROR} ERROR</span>
        &nbsp;&mdash;&nbsp;click to expand
      </span>
    </h2>
    <div id="log-body" style="display:none">
      ${fileLink ? `<p style="margin:4px 0 10px">${fileLink}</p>` : ''}
      <div style="overflow-x:auto;max-height:520px;overflow-y:auto;border:1px solid #333;border-radius:4px">
        <table style="font-size:12px;margin:0;width:100%">
          <tr class="th-row">
            <th style="width:195px">Timestamp</th>
            <th style="width:60px">Level</th>
            <th>Message</th>
          </tr>
          ${rowsBuf.join('\n')}
        </table>
      </div>
    </div>
    <script>
      function toggleLog() {
        var b = document.getElementById('log-body');
        if (b) b.style.display = b.style.display === 'none' ? 'block' : 'none';
      }
    </script>
  </section>`;
}

function buildBenchmarkReference(): string {
  return `<section class="test-section">
  <h4>Google Benchmark Reference</h4>
  <table style="width:60%">
    <tr class="th-row"><th>Metric</th><th>GOOD</th><th>NEEDS IMPROVEMENT</th><th>POOR</th></tr>
    <tr><td>LCP</td><td>&le; 2500ms</td><td>2500&ndash;4000ms</td><td>&gt; 4000ms</td></tr>
    <tr><td>FCP</td><td>&le; 1800ms</td><td>1800&ndash;3000ms</td><td>&gt; 3000ms</td></tr>
    <tr><td>TTI</td><td>&le; 3800ms</td><td>3800&ndash;7300ms</td><td>&gt; 7300ms</td></tr>
    <tr><td>CLS</td><td>&le; 0.1</td><td>0.1&ndash;0.25</td><td>&gt; 0.25</td></tr>
    <tr><td>TTFB</td><td>&le; 800ms</td><td>800&ndash;1800ms</td><td>&gt; 1800ms</td></tr>
    <tr><td>Page Load</td><td>&le; 3000ms</td><td>3000&ndash;6000ms</td><td>&gt; 6000ms</td></tr>
  </table>
  </section>`;
}

// ── Row helpers ────────────────────────────────────────────────────────────────

function row(metric: string, value: string, target: string, status: string): string {
  const color  = status === 'GOOD' ? '#1a3a1a' : status === 'POOR' ? '#3a1a1a' : status === 'INFO' ? '#1a2a3a' : '#3a3a1a';
  const badge  = status === 'GOOD' ? 'GOOD' : status === 'POOR' ? 'POOR' : status === 'INFO' ? 'INFO' : 'NEEDS IMPROVEMENT';
  return `<tr style="background:${color}"><td>${metric}</td><td><b>${value}</b></td><td>${target}</td><td>${badge}</td></tr>`;
}

function infoRow(label: string, value: string): string {
  return `<tr><td>${label}</td><td><b>${value}</b></td></tr>`;
}

function highlightRow(label: string, value: string, bg: string): string {
  return `<tr style="background:${bg}"><td><b>${label}</b></td><td><b>${value}</b></td></tr>`;
}

function attachScreenshot(m: PerformanceMetrics): string {
  if (!m.screenshotPath) return '';
  const rel = m.screenshotPath.replace(/\\/g, '/');
  return `<div class="screenshot-wrap">
    <a href="${rel}" target="_blank">Open Screenshot</a><br/>
    <img src="${rel}" alt="Page Screenshot" style="margin-top:8px;max-width:700px;border:1px solid #444;border-radius:4px"/>
  </div>`;
}

// ── Helpers ────────────────────────────────────────────────────────────────────

function fmt(ms: number): string {
  return ms > 0 ? Math.round(ms).toString() : 'N/A';
}

function esc(s: string): string {
  if (!s) return '';
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function statusBg(status: string): string {
  if (status === 'GOOD') return '#28a745';
  if (status === 'POOR') return '#dc3545';
  if (status === 'NEEDS IMPROVEMENT') return '#ffc107';
  return '#6c757d';
}

// ── HTML wrapper ───────────────────────────────────────────────────────────────

function wrapHtml(body: string): string {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <title>Web Performance Report</title>
  <style>
    * { box-sizing: border-box; }
    body {
      font-family: 'Segoe UI', Arial, sans-serif;
      background: #1a1a2e; color: #e0e0e0;
      margin: 0; padding: 20px;
    }
    h1 { color: #ffffff; border-bottom: 2px solid #444; padding-bottom: 10px; }
    h2.test-name { color: #a0c4ff; margin: 0 0 8px; }
    h3, h4 { color: #ccc; margin: 16px 0 6px; }
    .test-section {
      background: #16213e; border: 1px solid #333;
      border-radius: 6px; padding: 16px; margin-bottom: 20px;
    }
    .status-badge {
      display: inline-block; color: #fff; font-weight: bold;
      padding: 4px 12px; border-radius: 4px; margin-bottom: 8px;
    }
    .url-line { color: #888; font-size: 13px; margin: 4px 0 12px; }
    .url-line a { color: #6fa3ef; }
    table {
      width: 100%; border-collapse: collapse;
      margin-bottom: 12px; font-size: 14px;
    }
    td, th {
      border: 1px solid #444; padding: 6px 10px; vertical-align: middle;
    }
    .th-row { background: #2d2d2d; color: #fff; }
    th { font-weight: bold; }
    .screenshot-wrap { margin: 10px 0; }
    .screenshot-wrap a { color: #6fa3ef; }
    .report-header { background: #0f3460; padding: 16px; border-radius: 6px; margin-bottom: 20px; }
    .report-header p { margin: 4px 0; color: #aaa; }
  </style>
</head>
<body>
  <div class="report-header">
    <h1>Web Performance Report</h1>
    <p>Generated: ${timestamp()}</p>
    <p>Framework: Playwright + TypeScript</p>
  </div>
  ${body}
</body>
</html>`;
}
