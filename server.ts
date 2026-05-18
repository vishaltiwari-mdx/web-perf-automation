/**
 * server.ts — Web Performance Analyzer Dashboard
 * Run: npm run start  →  browser opens automatically at http://localhost:4000
 *
 * Clicking "Analyze" in the UI spawns a real Playwright test
 * (tests/DynamicUrlTest.spec.ts, project=dynamic) so every analysis uses
 * the same fixture stack, NavigationRouter, and Navigation Plan
 * (navigation-routes.md) as the manual test suite.
 */
import express, { Request, Response } from 'express';
import * as path   from 'path';
import * as fs     from 'fs';
import { spawn, exec } from 'child_process';
import { LighthouseCollector } from './src/metrics/LighthouseCollector';
import { NavigationAgent }     from './src/agent/NavigationAgent';

const app  = express();
const PORT = parseInt(process.env['PORT'] ?? '4000', 10);

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));
app.use('/reports', express.static(path.join(__dirname, 'reports')));

let analysisRunning = false;

// ── GET /api/routes ───────────────────────────────────────────────────────────
app.get('/api/routes', (_req, res) => {
  try {
    NavigationAgent.reload();
    res.json(NavigationAgent.listRoutes());
  } catch (e) {
    res.status(500).json({ error: String(e) });
  }
});

// ── GET /api/status ───────────────────────────────────────────────────────────
app.get('/api/status', (_req, res) => res.json({ running: analysisRunning }));

// ── GET /api/reports/list ─────────────────────────────────────────────────────
app.get('/api/reports/list', (_req, res) => {
  const dir = path.join(__dirname, 'reports');
  if (!fs.existsSync(dir)) return void res.json([]);
  const files = fs.readdirSync(dir)
    .filter(f => f.endsWith('.html') && !f.startsWith('.'))
    .map(f => ({
      name:  f,
      url:   `/reports/${encodeURIComponent(f)}`,
      mtime: fs.statSync(path.join(dir, f)).mtime.getTime(),
    }))
    .sort((a, b) => b.mtime - a.mtime)
    .slice(0, 30);
  res.json(files);
});

// ── POST /api/routes/save — update (or insert) a route block in the .md file ──
app.post('/api/routes/save', (req: Request, res: Response) => {
  const { pattern, description, isDynamic, steps } = req.body as {
    pattern: string; description: string; isDynamic?: boolean;
    steps: Array<{ selectorType: string; selector: string; description: string }>;
  };
  if (!pattern || typeof pattern !== 'string') {
    return void res.status(400).json({ error: 'pattern is required' });
  }
  try {
    const raw     = fs.existsSync(NavigationAgent.ROUTES_FILE)
      ? fs.readFileSync(NavigationAgent.ROUTES_FILE, 'utf8') : '';
    const block   = buildRouteBlock(pattern, description, !!isDynamic, steps ?? []);
    const updated = replaceRouteBlock(raw, pattern, block);
    fs.writeFileSync(NavigationAgent.ROUTES_FILE, updated, 'utf8');
    NavigationAgent.reload();
    res.json({ ok: true });
  } catch (e) {
    res.status(500).json({ error: String(e) });
  }
});

// ── POST /api/routes/add — append a new entry to navigation-routes.md ─────────
app.post('/api/routes/add', (req: Request, res: Response) => {
  const { hashPath, description, isDynamic } = req.body as {
    hashPath: string; description?: string; isDynamic?: boolean;
  };
  if (!hashPath || typeof hashPath !== 'string') {
    return void res.status(400).json({ error: 'hashPath is required' });
  }
  const pattern = hashPath.toLowerCase().replace(/^[#/\s]+/, '').replace(/\/+$/, '');
  const desc    = description?.trim() || `${pattern} page`;
  const block   = isDynamic
    ? `\n## ${pattern}/ [dynamic]\n> ${desc} — variable segment in URL, no fixed menu path\n\n---\n`
    : `\n## ${pattern}\n> ${desc}\n\n---\n`;
  try {
    fs.appendFileSync(NavigationAgent.ROUTES_FILE, block, 'utf8');
    NavigationAgent.reload();
    res.json({ ok: true, pattern: isDynamic ? `${pattern}/` : pattern });
  } catch (e) {
    res.status(500).json({ error: String(e) });
  }
});

// ── POST /api/analyze — SSE stream ────────────────────────────────────────────
app.post('/api/analyze', async (req: Request, res: Response) => {
  if (analysisRunning) {
    return void res.status(429).json({
      error: 'An analysis is already in progress. Please wait.',
    });
  }

  const { urls = [], metricsType = 'both', flow = '', selectedMetrics = [], credentials } = req.body as {
    urls: string[];
    metricsType: 'playwright' | 'lighthouse' | 'both' | 'custom';
    flow: string;
    selectedMetrics?: string[];
    credentials?: { username: string; password: string };
  };

  if (metricsType === 'custom' && (!selectedMetrics || selectedMetrics.length === 0)) {
    return void res.status(400).json({
      error: 'Custom analysis requires at least one selected metric.',
    });
  }

  const cleanUrls = urls.map((u: string) => u.trim()).filter(Boolean);
  if (!cleanUrls.length) {
    return void res.status(400).json({ error: 'At least one URL is required.' });
  }

  // ── SSE setup ─────────────────────────────────────────────────────────────
  res.setHeader('Content-Type',      'text/event-stream');
  res.setHeader('Cache-Control',     'no-cache');
  res.setHeader('Connection',        'keep-alive');
  res.setHeader('X-Accel-Buffering', 'no');
  res.flushHeaders();

  const emit = (data: object) => {
    if (!res.writableEnded) res.write(`data: ${JSON.stringify(data)}\n\n`);
  };

  analysisRunning = true;

  const cleanup = () => {
    analysisRunning = false;
  };

  try {
    // Resolve URLs — if a custom flow is provided, parse it into URL list
    let targetUrls = cleanUrls;
    if (flow.trim()) {
      const flowUrls = parseFlow(flow, cleanUrls[0]);
      if (flowUrls.length) {
        emit({ t: 'log', lvl: 'info', msg: `Navigation flow: ${flowUrls.length} step(s) parsed` });
        flowUrls.forEach(u => emit({ t: 'log', lvl: 'info', msg: `  • ${u}` }));
        targetUrls = flowUrls;
      }
      // Persist any new paths to navigation-routes.md for future reference
      const saved = autoSaveFlowPaths(flow);
      if (saved.length) {
        emit({ t: 'log', lvl: 'info',
          msg: `💾 Saved ${saved.length} new route(s) to navigation-routes.md: ${saved.join(', ')}` });
      }
    }

    emit({ t: 'start', total: targetUrls.length, metricsType });

    // Emit url_start chips for all targets so the UI shows progress immediately
    targetUrls.forEach((url, i) => {
      emit({ t: 'url_start', url, name: deriveName(url), index: i + 1, total: targetUrls.length });
    });

    // ── Lighthouse-only path (no Playwright browser) ───────────────────────
    if (metricsType === 'lighthouse') {
      const results: ResultEntry[] = [];
      for (let i = 0; i < targetUrls.length; i++) {
        const url  = targetUrls[i];
        const name = deriveName(url);
        emit({ t: 'log', lvl: 'info', msg: `[Lighthouse] Auditing: ${name}` });
        try {
          const lh = await LighthouseCollector.run(url, {
            pageName:  name,
            outputDir: path.join('reports', 'lighthouse'),
          });
          const r: ResultEntry = {
            url, name, lhReport: lh.htmlReportPath, lhScore: lh.performanceScore,
          };
          results.push(r);
          emit({ t: 'url_done', ...r });
        } catch (e) {
          emit({ t: 'url_err', url, msg: String(e) });
        }
      }
      emit({ t: 'complete', results });
      return;
    }

    // ── Playwright (± Lighthouse) — spawn real Playwright test ────────────
    const batchId = Date.now().toString();
    emit({ t: 'log', lvl: 'info', msg: `[Server] Starting Playwright test suite (batch ${batchId})` });
    emit({ t: 'log', lvl: 'info', msg: `[Server] Project: dynamic | URLs: ${targetUrls.length}` });
    emit({ t: 'log', lvl: 'info', msg: `[Server] Navigation Plan: ${NavigationAgent.ROUTES_FILE}` });

    const results = await spawnPlaywright(targetUrls, batchId, metricsType, selectedMetrics ?? [], credentials, emit);

    results.forEach(r => emit({ t: 'url_done', ...r }));
    emit({ t: 'complete', results });

  } catch (e) {
    emit({ t: 'error', msg: String(e) });
  } finally {
    cleanup();
    if (!res.writableEnded) res.end();
  }
});

// ── Playwright spawn helper ────────────────────────────────────────────────────

function spawnPlaywright(
  urls:            string[],
  batchId:         string,
  metricsType:     'playwright' | 'both' | 'custom',
  selectedMetrics: string[],
  credentials:     { username: string; password: string } | undefined,
  emit:            (data: object) => void,
): Promise<ResultEntry[]> {
  return new Promise((resolve, reject) => {
    const env: NodeJS.ProcessEnv = {
      ...process.env,
      TARGET_URLS:      urls.join(','),
      BATCH_ID:         batchId,
      METRICS_TYPE:     metricsType,
      ...(selectedMetrics.length ? { SELECTED_METRICS: selectedMetrics.join(',') } : {}),
      ...(credentials?.username ? { TEST_USERNAME: credentials.username } : {}),
      ...(credentials?.password ? { TEST_PASSWORD: credentials.password } : {}),
      FORCE_COLOR:      '0',
    };

    // Use npx playwright so it resolves from node_modules regardless of global installs
    const child = spawn(
      'npx',
      ['playwright', 'test', '--project=dynamic', '--reporter=list'],
      { env, shell: true, cwd: __dirname },
    );

    child.stdout?.on('data', (chunk: Buffer) => {
      chunk.toString().split('\n')
        .map(l => l.trim()).filter(Boolean)
        .forEach(line => emit({ t: 'log', lvl: 'info', msg: line }));
    });

    child.stderr?.on('data', (chunk: Buffer) => {
      chunk.toString().split('\n')
        .map(l => l.trim()).filter(Boolean)
        .forEach(line => {
          // Playwright writes normal progress output to stderr — treat as info
          // unless it looks like an actual error
          const lvl = /error|fail|exception/i.test(line) ? 'warn' : 'info';
          emit({ t: 'log', lvl, msg: line });
        });
    });

    child.on('error', (err) => {
      emit({ t: 'log', lvl: 'error', msg: `[Server] Failed to start Playwright: ${err.message}` });
      reject(err);
    });

    child.on('close', (code) => {
      emit({ t: 'log', lvl: code === 0 ? 'info' : 'warn',
        msg: `[Server] Playwright exited (code ${code})` });

      // Read results JSON written by DynamicUrlTest afterAll hook
      const resultsFile = path.join(__dirname, 'reports', `dynamic-results-${batchId}.json`);
      if (fs.existsSync(resultsFile)) {
        try {
          const data = JSON.parse(fs.readFileSync(resultsFile, 'utf8')) as DynamicResults;
          emit({ t: 'log', lvl: 'info', msg: `[Server] HTML report  : ${data.htmlReport}` });
          emit({ t: 'log', lvl: 'info', msg: `[Server] Excel report : ${data.excelReport}` });
          if (data.logReport) {
            emit({ t: 'log', lvl: 'info', msg: `[Server] Run log      : ${data.logReport}` });
          }
          resolve(data.urls.map(u => ({
            url:           u.url,
            name:          u.name,
            htmlReport:    u.htmlReport || data.htmlReport,  // individual report, fall back to combined
            excelReport:   data.excelReport,
            lhReport:      u.lhReport   ?? undefined,
            lhScore:       u.lhScore    ?? undefined,
            overallStatus: u.overallStatus,
            metrics:       u.metrics,
          })));
          return;
        } catch (e) {
          emit({ t: 'log', lvl: 'warn', msg: `[Server] Could not parse results JSON: ${e}` });
        }
      } else {
        emit({ t: 'log', lvl: 'warn', msg: '[Server] Results JSON not found — tests may have failed' });
      }

      if (code !== 0) {
        reject(new Error(`Playwright exited with code ${code}`));
      } else {
        resolve([]);
      }
    });
  });
}

// ── Types ─────────────────────────────────────────────────────────────────────

interface ResultEntry {
  url: string; name: string;
  htmlReport?:   string;
  excelReport?:  string;
  lhReport?:     string;
  lhScore?:      number;
  overallStatus?: string;
  metrics?:      Record<string, string | number>;
}

interface DynamicResults {
  htmlReport:  string;
  excelReport: string;
  logReport:   string;
  urls: Array<{
    url: string; name: string; overallStatus: string;
    htmlReport: string;           // individual report for this URL
    lhScore: number | null; lhReport: string | null;
    metrics: Record<string, string | number>;
  }>;
}

// ── Route markdown helpers ─────────────────────────────────────────────────────

function buildRouteBlock(
  pattern: string, description: string, isDynamic: boolean,
  steps: Array<{ selectorType: string; selector: string; description: string }>,
): string {
  const heading   = isDynamic ? `## ${pattern.replace(/\/?$/, '/')} [dynamic]` : `## ${pattern}`;
  const stepLines = steps.map((s, i) => `${i + 1}. ${s.selectorType}: \`${s.selector}\` — ${s.description}`);
  return [heading, `> ${description}`, '', ...stepLines, '', '---'].join('\n');
}

function replaceRouteBlock(content: string, pattern: string, newBlock: string): string {
  const lines    = content.split('\n');
  const cleanPat = pattern.toLowerCase().replace(/\s*\[dynamic\]\s*$/, '').replace(/\/+$/, '');
  let start = -1, end = -1;

  for (let i = 0; i < lines.length; i++) {
    const m = lines[i].match(/^##\s+(.+?)(?:\s+\[dynamic\])?\s*$/);
    if (m) {
      const lp = m[1].trim().toLowerCase().replace(/\/+$/, '');
      if (lp === cleanPat) { start = i; }
      else if (start !== -1) { end = i - 1; break; }
    }
    if (start !== -1 && /^-{3,}$/.test(lines[i].trim())) { end = i; break; }
  }

  if (start === -1) return content.trimEnd() + '\n\n' + newBlock + '\n';
  if (end   === -1) end = lines.length - 1;
  return [...lines.slice(0, start), newBlock, ...lines.slice(end + 1)].join('\n');
}

// ── Flow save helper ───────────────────────────────────────────────────────────

/**
 * Inspects each path in the textarea flow text and appends any that are not
 * already registered in navigation-routes.md as a dynamic route entry.
 * Returns the list of newly-added patterns so callers can log them.
 */
function autoSaveFlowPaths(flowText: string): string[] {
  if (!flowText.trim()) return [];

  const lines = flowText.split(/[\n,]+/).map(l => l.trim()).filter(Boolean);
  const paths = lines.map(line => {
    if (line.startsWith('http')) {
      try {
        const u = new URL(line);
        return u.hash.replace(/^#\//, '').split('?')[0];
      } catch { return ''; }
    }
    return line.replace(/^[#/\s]+/, '').replace(/\/+$/, '');
  }).filter(Boolean);

  const existing = new Set(
    NavigationAgent.listRoutes().map(r => r.route.replace(/\/+$/, '')),
  );
  const toAdd = paths.filter(p => !existing.has(p) && !existing.has(p + '/'));

  for (const p of toAdd) {
    const desc  = p.replace(/\//g, ' › ') + ' page';
    const block = `\n## ${p}/ [dynamic]\n> ${desc} — saved from custom flow\n\n---\n`;
    fs.appendFileSync(NavigationAgent.ROUTES_FILE, block, 'utf8');
  }
  if (toAdd.length) NavigationAgent.reload();
  return toAdd;
}

// ── POST /api/steps/ai-parse — Claude converts prose description → atomic steps ──
app.post('/api/steps/ai-parse', async (req: Request, res: Response) => {
  res.setHeader('Content-Type', 'application/json');
  const { text, url } = req.body as { text: string; url?: string };
  if (!text?.trim()) return void res.json({ steps: [] });
  try {
    const { StepParserAgent } = await import('./src/agent/StepParserAgent');
    const steps = await new StepParserAgent().parseDescription(text, url);
    // Return formatted "type: `selector` — label" strings so the textarea shows editable steps
    res.json({
      steps: steps.map(s => `${s.selectorType}: \`${s.selector}\` — ${s.description}`),
    });
  } catch (e) {
    const msg = e instanceof Error ? e.message : String(e);
    res.status(500).json({ error: msg });
  }
});

// ── POST /api/flow/save — persist new paths from the flow textarea ─────────────
app.post('/api/flow/save', (req: Request, res: Response) => {
  const { flow } = req.body as { flow: string };
  if (!flow || typeof flow !== 'string') {
    return void res.status(400).json({ error: 'flow is required' });
  }
  try {
    const saved = autoSaveFlowPaths(flow);
    res.json({ ok: true, saved, count: saved.length });
  } catch (e) {
    res.status(500).json({ error: String(e) });
  }
});

// ── URL helpers ────────────────────────────────────────────────────────────────

function parseFlow(flow: string, baseUrl?: string): string[] {
  const origin = (() => { try { return baseUrl ? new URL(baseUrl).origin : ''; } catch { return ''; } })();
  return flow.split(/[\n,]+/).map(l => l.trim()).filter(Boolean).map(line => {
    if (line.startsWith('http')) return line;
    return origin ? `${origin}/#/${line.replace(/^[#/\s]+/, '')}` : line;
  });
}

function deriveName(url: string): string {
  try {
    const u = new URL(url);
    const h = u.hash.replace(/^#\//, '').split('?')[0];
    return h ? h.replace(/\//g, ' › ') : u.hostname;
  } catch { return url; }
}

// ── Auto-open browser ─────────────────────────────────────────────────────────

function openBrowser(url: string): void {
  const cmd = process.platform === 'win32'  ? `start "" "${url}"`
            : process.platform === 'darwin' ? `open "${url}"`
            : `xdg-open "${url}"`;
  exec(cmd, (err) => {
    if (err) console.warn(`[Server] Could not auto-open browser: ${err.message}`);
  });
}

// ── Start ─────────────────────────────────────────────────────────────────────
app.listen(PORT, () => {
  const url = `http://localhost:${PORT}`;
  console.info(`\n  ╔═══════════════════════════════════════════╗`);
  console.info(`  ║  Web Performance Analyzer                 ║`);
  console.info(`  ║  Open → ${url}              ║`);
  console.info(`  ╚═══════════════════════════════════════════╝\n`);
  openBrowser(url);
});
