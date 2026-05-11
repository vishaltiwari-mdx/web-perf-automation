/**
 * MCP Server — Web Performance Automation
 *
 * Exposes the PageLoadAnalyzerAgent as MCP tools so Claude can analyze URLs
 * directly from the conversation without running a test or CLI command.
 *
 * Tools:
 *   analyze_url(url, pageName?)        — analyze one URL, return full metrics
 *   analyze_urls(urls)                 — analyze multiple URLs, return all metrics + combined reports
 *   get_benchmark_thresholds()         — return Google's Core Web Vitals thresholds (reference)
 *
 * Registered in .mcp.json so Claude Code starts it automatically.
 */
import { Server }              from '@modelcontextprotocol/sdk/server/index.js';
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js';
import {
  CallToolRequestSchema,
  ListToolsRequestSchema,
} from '@modelcontextprotocol/sdk/types.js';

import { PageLoadAnalyzerAgent } from './src/agent/PageLoadAnalyzerAgent';
import { LighthouseCollector }  from './src/metrics/LighthouseCollector';
import { LighthouseMetrics }    from './src/metrics/LighthouseMetrics';
import { NavigationAgent }      from './src/agent/NavigationAgent';
import { PerformanceMetrics }   from './src/metrics/PerformanceMetrics';

// ── Server setup ───────────────────────────────────────────────────────────────

const server = new Server(
  { name: 'web-perf-automation', version: '1.0.0' },
  { capabilities: { tools: {} } },
);

// ── Tool definitions ───────────────────────────────────────────────────────────

server.setRequestHandler(ListToolsRequestSchema, async () => ({
  tools: [
    {
      name: 'analyze_url',
      description:
        'Analyze web performance metrics for a single URL using Chrome + Playwright. ' +
        'Captures Core Web Vitals (LCP, FCP, CLS, TTI, TTFB, TBT, INP), Navigation Timing, ' +
        'resource counts, transfer size, AG-Grid spinner time, and slow API requests. ' +
        'Handles login redirect automatically using credentials from config.properties. ' +
        'Returns metrics, benchmark status (GOOD/NEEDS IMPROVEMENT/POOR), and report file paths.',
      inputSchema: {
        type: 'object' as const,
        properties: {
          url: {
            type: 'string',
            description: 'Full URL to analyze (e.g. https://example.com or http://host/#/ip/catalog)',
          },
          page_name: {
            type: 'string',
            description: 'Optional human-readable label for the page (auto-derived from URL if omitted)',
          },
        },
        required: ['url'],
      },
    },
    {
      name: 'analyze_urls',
      description:
        'Analyze web performance metrics for multiple URLs in sequence. ' +
        'Returns individual metrics for each URL plus paths to combined HTML and Excel reports.',
      inputSchema: {
        type: 'object' as const,
        properties: {
          urls: {
            type: 'array',
            items: { type: 'string' },
            description: 'List of URLs to analyze',
          },
        },
        required: ['urls'],
      },
    },
    {
      name: 'get_benchmark_thresholds',
      description: "Returns Google's Core Web Vitals benchmark thresholds for GOOD / NEEDS IMPROVEMENT / POOR.",
      inputSchema: { type: 'object' as const, properties: {}, required: [] },
    },
    {
      name: 'describe_navigation',
      description:
        'Describe how NavigationAgent will navigate to a given URL using UI actions ' +
        '(header menu clicks, dropdown items) instead of direct URL navigation. ' +
        'Returns the exact click sequence with CSS/XPath selectors derived from ' +
        'the phi_web_automation repo analysis. ' +
        'Useful before running analyze_url to understand how the page will be reached.',
      inputSchema: {
        type: 'object' as const,
        properties: {
          url: { type: 'string', description: 'Full URL to describe navigation for' },
        },
        required: ['url'],
      },
    },
    {
      name: 'list_navigation_routes',
      description:
        'List all registered navigation routes in NavigationAgent. ' +
        'Shows every known page (IP Catalog, Library Catalog, Advanced Search, ' +
        'all Administration pages, Shopping Cart, etc.) with the UI click steps ' +
        'required to reach each one. Also identifies dynamic routes that require ' +
        'direct URL navigation (e.g. IP detail pages with an FQN in the URL).',
      inputSchema: { type: 'object' as const, properties: {}, required: [] },
    },
    {
      name: 'lighthouse_audit',
      description:
        'Run a Google Lighthouse performance audit for a single URL. ' +
        'Returns category scores (Performance, Accessibility, Best Practices, SEO), ' +
        'all lab metrics (FCP, LCP, Speed Index, TTI, TBT, CLS, FMP, Server Response, ' +
        'Main Thread Work, JS Bootup, DOM Size), byte-savings estimates for unused JS/CSS, ' +
        'a list of failing opportunities with estimated savings, and diagnostic info. ' +
        'Also saves a full Lighthouse HTML report to disk. ' +
        'Does NOT require Playwright — runs its own Chrome instance directly.',
      inputSchema: {
        type: 'object' as const,
        properties: {
          url: {
            type: 'string',
            description: 'Full URL to audit (e.g. https://example.com)',
          },
          page_name: {
            type: 'string',
            description: 'Optional label for the page (auto-derived from URL if omitted)',
          },
        },
        required: ['url'],
      },
    },
  ],
}));

// ── Tool handlers ──────────────────────────────────────────────────────────────

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const { name, arguments: args } = request.params;

  // ── analyze_url ─────────────────────────────────────────────────────────────
  if (name === 'analyze_url') {
    const url      = String((args as Record<string, unknown>)['url'] ?? '');
    const pageName = (args as Record<string, unknown>)['page_name'] as string | undefined;

    if (!url) {
      return errorContent('url is required');
    }

    try {
      const result = await PageLoadAnalyzerAgent.analyze(url, pageName);
      return {
        content: [{
          type: 'text',
          text: formatSingleResult(result.metrics, result.htmlReportPath, result.excelReportPath),
        }],
      };
    } catch (e) {
      return errorContent(`Analysis failed for ${url}: ${e}`);
    }
  }

  // ── analyze_urls ────────────────────────────────────────────────────────────
  if (name === 'analyze_urls') {
    const raw  = (args as Record<string, unknown>)['urls'];
    const urls = Array.isArray(raw) ? (raw as string[]).filter(u => u && u.trim()) : [];

    if (urls.length === 0) {
      return errorContent('urls array is required and must be non-empty');
    }

    try {
      const results = await PageLoadAnalyzerAgent.analyzeAll(urls);
      const lines: string[] = [
        `Analyzed ${results.length} URL(s)\n`,
        '═'.repeat(60),
      ];
      results.forEach(r => {
        lines.push(formatSingleResult(r.metrics, r.htmlReportPath, r.excelReportPath));
        lines.push('─'.repeat(60));
      });
      return { content: [{ type: 'text', text: lines.join('\n') }] };
    } catch (e) {
      return errorContent(`Batch analysis failed: ${e}`);
    }
  }

  // ── get_benchmark_thresholds ────────────────────────────────────────────────
  if (name === 'get_benchmark_thresholds') {
    return {
      content: [{
        type: 'text',
        text: `Google Core Web Vitals Thresholds
${'═'.repeat(50)}
Metric        │ GOOD          │ NEEDS IMPROV. │ POOR
──────────────┼───────────────┼───────────────┼──────────────
LCP           │ ≤ 2500 ms     │ 2500–4000 ms  │ > 4000 ms
FCP           │ ≤ 1800 ms     │ 1800–3000 ms  │ > 3000 ms
TTI           │ ≤ 3800 ms     │ 3800–7300 ms  │ > 7300 ms
CLS           │ ≤ 0.1         │ 0.1–0.25      │ > 0.25
TTFB          │ ≤ 800 ms      │ 800–1800 ms   │ > 1800 ms
Page Load     │ ≤ 3000 ms     │ 3000–6000 ms  │ > 6000 ms
INP           │ ≤ 200 ms      │ 200–500 ms    │ > 500 ms
TBT           │ ≤ 200 ms      │ 200–600 ms    │ > 600 ms`,
      }],
    };
  }

  // ── describe_navigation ────────────────────────────────────────────────────
  if (name === 'describe_navigation') {
    const url = String((args as Record<string, unknown>)['url'] ?? '');
    if (!url) return errorContent('url is required');

    const description = NavigationAgent.describeNavigation(url);
    return { content: [{ type: 'text', text: `Navigation plan for: ${url}\n\n${description}` }] };
  }

  // ── list_navigation_routes ──────────────────────────────────────────────────
  if (name === 'list_navigation_routes') {
    const routes = NavigationAgent.listRoutes();
    const static_ = routes.filter(r => !r.isDynamic);
    const dynamic_ = routes.filter(r =>  r.isDynamic);

    const staticLines = static_.map(r => {
      const stepsText = r.steps.length > 0
        ? r.steps.map((s, i) => `      ${i + 1}. ${s}`).join('\n')
        : '      (no steps)';
      return `  /${r.route}\n    ${r.description}\n${stepsText}`;
    }).join('\n\n');

    const dynamicLines = dynamic_.map(r =>
      `  /${r.route}*\n    ${r.description} — direct URL navigation`,
    ).join('\n');

    return {
      content: [{
        type: 'text',
        text: `NavigationAgent — Registered Routes
${'═'.repeat(60)}

MENU-NAVIGABLE ROUTES (${static_.length})
These pages are reached by clicking header menus and dropdown items.
No direct URL navigation is used.

${staticLines}

DYNAMIC ROUTES (${dynamic_.length})
These pages have variable URL segments (IDs/FQNs) and cannot be
reached through a fixed menu path. NavigationAgent falls back to
page.goto() for these routes.

${dynamicLines}`,
      }],
    };
  }

  // ── lighthouse_audit ────────────────────────────────────────────────────────
  if (name === 'lighthouse_audit') {
    const url      = String((args as Record<string, unknown>)['url'] ?? '');
    const pageName = (args as Record<string, unknown>)['page_name'] as string | undefined;

    if (!url) {
      return errorContent('url is required');
    }

    try {
      const lh = await LighthouseCollector.run(url, {
        pageName: pageName ?? url,
        outputDir: 'reports/lighthouse',
      });
      return { content: [{ type: 'text', text: formatLighthouseResult(lh, url, pageName) }] };
    } catch (e) {
      return errorContent(`Lighthouse audit failed for ${url}: ${e}`);
    }
  }

  return errorContent(`Unknown tool: ${name}`);
});

// ── Formatters ─────────────────────────────────────────────────────────────────

function formatSingleResult(m: PerformanceMetrics, htmlPath: string, excelPath: string): string {
  const slowCount = (m.slowApiRequests ?? []).length;
  const agGrid = m.agGridLoadTime > 0 ? `${Math.round(m.agGridLoadTime)} ms` : 'N/A';

  const statusIcon = (s: string) =>
    s === 'GOOD' ? '✅' : s === 'POOR' ? '❌' : s === 'NEEDS IMPROVEMENT' ? '⚠️' : 'ℹ️';

  return `
Page:     ${m.pageName}
URL:      ${m.pageUrl}
Overall:  ${statusIcon(m.overallStatus)} ${m.overallStatus}

Core Web Vitals
  LCP:    ${fmt(m.largestContentfulPaint)} ms  ${statusIcon(m.lcpStatus)} ${m.lcpStatus}
  FCP:    ${fmt(m.firstContentfulPaint)} ms  ${statusIcon(m.fcpStatus)} ${m.fcpStatus}
  TTI:    ${fmt(m.timeToInteractive)} ms  ${statusIcon(m.ttiStatus)} ${m.ttiStatus}
  CLS:    ${m.cumulativeLayoutShift.toFixed(4)}     ${statusIcon(m.clsStatus)} ${m.clsStatus}
  TTFB:   ${fmt(m.timeToFirstByte)} ms  ${statusIcon(m.ttfbStatus)} ${m.ttfbStatus}
  INP:    ${fmt(m.interactionToNextPaint)} ms
  TBT:    ${fmt(m.totalBlockingTime)} ms

Navigation Timing
  DNS:          ${fmt(m.dnsLookupTime)} ms
  TCP:          ${fmt(m.tcpConnectionTime)} ms
  DOM Ready:    ${fmt(m.domContentLoadedTime)} ms
  Full Load:    ${fmt(m.pageLoadTime)} ms  ${statusIcon(m.pageLoadStatus)} ${m.pageLoadStatus}
  AG-Grid:      ${agGrid}

Resources
  Count:         ${m.totalResources}
  Transfer Size: ${Math.round(m.transferSize / 1024)} KB
  Slow APIs:     ${slowCount === 0 ? 'None' : `${slowCount} request(s) > 1s`}
${slowCount > 0 ? (m.slowApiRequests ?? []).map(a => `    • ${Math.round(a.durationMs)} ms — ${a.url}`).join('\n') : ''}
Reports
  HTML:  ${htmlPath}
  Excel: ${excelPath}
${m.lighthouseData ? `
Lighthouse Scores
  Performance:    ${m.lighthouseData.performanceScore.toString().padStart(3)}  ${statusIcon(m.lighthouseData.performanceScore >= 90 ? 'GOOD' : m.lighthouseData.performanceScore >= 50 ? 'NEEDS IMPROVEMENT' : 'POOR')}
  Accessibility:  ${m.lighthouseData.accessibilityScore.toString().padStart(3)}  ${statusIcon(m.lighthouseData.accessibilityScore >= 90 ? 'GOOD' : m.lighthouseData.accessibilityScore >= 50 ? 'NEEDS IMPROVEMENT' : 'POOR')}
  Best Practices: ${m.lighthouseData.bestPracticesScore.toString().padStart(3)}  ${statusIcon(m.lighthouseData.bestPracticesScore >= 90 ? 'GOOD' : m.lighthouseData.bestPracticesScore >= 50 ? 'NEEDS IMPROVEMENT' : 'POOR')}
  SEO:            ${m.lighthouseData.seoScore.toString().padStart(3)}  ${statusIcon(m.lighthouseData.seoScore >= 90 ? 'GOOD' : m.lighthouseData.seoScore >= 50 ? 'NEEDS IMPROVEMENT' : 'POOR')}
  Opportunities:  ${m.lighthouseData.opportunities.length === 0 ? 'None' : m.lighthouseData.opportunities.length + ' failing'}
  LH Report: ${m.lighthouseData.htmlReportPath ?? 'N/A'}` : ''}`.trim();
}

function fmt(ms: number): string {
  return ms > 0 ? Math.round(ms).toString().padStart(6) : '   N/A';
}

function formatLighthouseResult(lh: LighthouseMetrics, url: string, pageName?: string): string {
  const scoreIcon = (s: number) => s >= 90 ? '✅' : s >= 50 ? '⚠️' : '❌';
  const oppCount  = lh.opportunities.length;
  const oppLines  = oppCount === 0
    ? '  None (all passing)'
    : lh.opportunities
        .map(o => {
          const savings = o.savingsMs    != null ? `${o.savingsMs} ms`
                        : o.savingsBytes != null ? `${Math.round(o.savingsBytes / 1024)} KB`
                        : (o.displayValue ?? '');
          return `  • ${o.title}${savings ? ` — ${savings}` : ''}`;
        })
        .join('\n');

  const diagLines = lh.diagnostics.length === 0
    ? '  None'
    : lh.diagnostics
        .map(d => `  • ${d.title}${d.displayValue ? `: ${d.displayValue}` : ''}`)
        .join('\n');

  return `
Lighthouse Audit
URL:      ${url}
Page:     ${pageName ?? url}

Category Scores
  Performance:    ${lh.performanceScore.toString().padStart(3)}  ${scoreIcon(lh.performanceScore)}
  Accessibility:  ${lh.accessibilityScore.toString().padStart(3)}  ${scoreIcon(lh.accessibilityScore)}
  Best Practices: ${lh.bestPracticesScore.toString().padStart(3)}  ${scoreIcon(lh.bestPracticesScore)}
  SEO:            ${lh.seoScore.toString().padStart(3)}  ${scoreIcon(lh.seoScore)}

Lab Metrics
  FCP:            ${fmt(lh.firstContentfulPaint)} ms
  LCP:            ${fmt(lh.largestContentfulPaint)} ms
  Speed Index:    ${fmt(lh.speedIndex)} ms
  TTI:            ${fmt(lh.timeToInteractive)} ms
  TBT:            ${fmt(lh.totalBlockingTime)} ms
  CLS:            ${lh.cumulativeLayoutShift.toFixed(4)}
  FMP:            ${fmt(lh.firstMeaningfulPaint)} ms
  Server Resp:    ${fmt(lh.serverResponseTime)} ms
  Main Thread:    ${fmt(lh.mainThreadWork)} ms
  JS Bootup:      ${fmt(lh.bootupTime)} ms
  DOM Size:       ${Math.round(lh.domSize)} nodes

Byte Savings
  Unused JS:          ${lh.unusedJavascriptBytes > 0 ? Math.round(lh.unusedJavascriptBytes / 1024) + ' KB' : 'None'}
  Unused CSS:         ${lh.unusedCssBytes > 0 ? Math.round(lh.unusedCssBytes / 1024) + ' KB' : 'None'}
  Render Blocking:    ${lh.renderBlockingResourcesSavingsMs > 0 ? Math.round(lh.renderBlockingResourcesSavingsMs) + ' ms' : 'None'}

Opportunities (${oppCount})
${oppLines}

Diagnostics
${diagLines}

Report
  HTML: ${lh.htmlReportPath ?? 'N/A'}`.trim();
}

function errorContent(message: string) {
  return { content: [{ type: 'text' as const, text: `Error: ${message}` }], isError: true };
}

// ── Start ──────────────────────────────────────────────────────────────────────

async function main(): Promise<void> {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  // Log to stderr so it doesn't corrupt the stdio JSON-RPC stream
  process.stderr.write('[web-perf-automation MCP server] ready\n');
}

main().catch(err => {
  process.stderr.write(`[MCP server fatal] ${err}\n`);
  process.exit(1);
});
