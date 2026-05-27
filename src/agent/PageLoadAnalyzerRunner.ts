#!/usr/bin/env ts-node
/**
 * PageLoadAnalyzerRunner — CLI entry point for the standalone analyzer.
 *
 * Usage:
 *   # Interactive prompt
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts
 *
 *   # Inline URLs (comma- or newline-separated, positional arg)
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts "https://a.com,https://b.com"
 *
 *   # File input (one URL per line, OR comma-separated, OR mixed)
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts ./urls.txt
 *
 *   # Explicit flags
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts --file ./urls.txt
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts --urls "https://a.com,https://b.com"
 *
 *   # Via npm scripts
 *   npm run analyze                          # interactive
 *   npm run analyze:file -- ./urls.txt       # file shortcut
 *   npm run analyze:urls -- "a.com,b.com"    # csv shortcut
 *
 * Notes on the URL file format:
 *   - One URL per line, comma-separated on a line, or any mix are all accepted.
 *   - Lines starting with '#' are treated as comments and ignored.
 *   - Blank lines are ignored.
 */
import * as readline from 'readline';
import * as fs from 'fs';
import * as path from 'path';
import { PageLoadAnalyzerAgent } from './PageLoadAnalyzerAgent';

interface CliArgs {
  file?:  string;
  urls?:  string;
  rest:   string[];
  help:   boolean;
}

function printHelp(): void {
  console.info(`
Web Performance Analyzer — CLI

USAGE
  ts-node src/agent/PageLoadAnalyzerRunner.ts [options] [positional]

POSITIONAL
  <urls-or-file>            A comma-separated list of URLs, OR a path to a file
                            containing URLs. Auto-detected by whether the value
                            resolves to an existing file.

OPTIONS
  --file <path>             Path to a URLs file (newline and/or comma separated).
                            Lines starting with '#' are treated as comments.
  --urls <csv>              Comma-separated list of URLs.
  -h, --help                Show this help and exit.

EXAMPLES
  npm run analyze                                    # interactive prompt
  npm run analyze -- ./urls.txt                      # positional file
  npm run analyze:file -- ./urls.txt                 # explicit file flag
  npm run analyze:urls -- "https://a.com,https://b.com"
  ts-node src/agent/PageLoadAnalyzerRunner.ts --file C:\\path\\to\\urls.txt
`);
}

function parseArgs(argv: string[]): CliArgs {
  const out: CliArgs = { rest: [], help: false };
  for (let i = 0; i < argv.length; i++) {
    const a = argv[i];
    if (a === '-h' || a === '--help')      out.help = true;
    else if (a === '--file' && argv[i + 1]) out.file = argv[++i];
    else if (a === '--urls' && argv[i + 1]) out.urls = argv[++i];
    else if (a.startsWith('--'))           console.warn(`Ignoring unknown flag: ${a}`);
    else                                    out.rest.push(a);
  }
  return out;
}

/** Parse file content into a URL list. Accepts newline-separated, comma-separated, or mixed. */
function parseUrlFile(filePath: string): string[] {
  const raw = fs.readFileSync(filePath, 'utf8');
  return raw
    .split(/[\n,]+/)
    .map(s => s.trim())
    .filter(s => s && !s.startsWith('#'));
}

async function main(): Promise<void> {
  console.info('╔══════════════════════════════════════════════════════════╗');
  console.info('║         Web Performance Analyzer — TypeScript CLI        ║');
  console.info('╚══════════════════════════════════════════════════════════╝');

  const cli = parseArgs(process.argv.slice(2));
  if (cli.help) { printHelp(); return; }

  // ── Flag-driven paths first (explicit beats positional) ──────────────────
  if (cli.file) {
    await runWithFile(cli.file);
    return;
  }
  if (cli.urls) {
    await analyzeUrls(splitCsv(cli.urls));
    return;
  }

  // ── Positional: file path OR inline csv ──────────────────────────────────
  const positional = cli.rest.join(' ').trim();
  if (positional) {
    await runWithArg(positional);
    return;
  }

  // ── Interactive mode ─────────────────────────────────────────────────────
  console.info('');
  console.info('Enter a URL, a comma-separated list of URLs, or a path to a .txt file.');
  console.info('Files may use newlines, commas, or both. "#" starts a comment line.');
  console.info('Press Enter to confirm. Type "quit" to exit.');
  console.info('');

  const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
  const prompt = (): Promise<string> =>
    new Promise(resolve => rl.question('Enter URL(s) or file path: ', resolve));

  while (true) {
    const input = (await prompt()).trim();
    if (!input || input.toLowerCase() === 'quit' || input.toLowerCase() === 'exit') {
      console.info('Exiting...');
      rl.close();
      break;
    }
    await runWithArg(input);
  }
}

async function runWithArg(arg: string): Promise<void> {
  const resolved = path.resolve(arg);
  if (fs.existsSync(resolved) && fs.statSync(resolved).isFile()) {
    await runWithFile(resolved);
    return;
  }
  await analyzeUrls(splitCsv(arg));
}

async function runWithFile(filePath: string): Promise<void> {
  const resolved = path.resolve(filePath);
  if (!fs.existsSync(resolved)) {
    console.error(`File not found: ${resolved}`);
    return;
  }
  const urls = parseUrlFile(resolved);
  if (urls.length === 0) {
    console.warn(`File is empty or contains only comments: ${resolved}`);
    return;
  }
  console.info(`Loaded ${urls.length} URL(s) from file: ${resolved}`);
  urls.forEach((u, i) => console.info(`  ${i + 1}. ${u}`));
  await analyzeUrls(urls);
}

function splitCsv(s: string): string[] {
  return s.split(/[,\n]+/).map(u => u.trim()).filter(Boolean);
}

async function analyzeUrls(urls: string[]): Promise<void> {
  if (urls.length === 0) {
    console.warn('No valid URLs provided.');
    return;
  }

  try {
    if (urls.length === 1) {
      const result = await PageLoadAnalyzerAgent.analyze(urls[0]);
      const lh = result.metrics.lighthouseData;
      console.info(`\nHTML       : ${result.htmlReportPath}`);
      console.info(`Excel      : ${result.excelReportPath}`);
      if (lh) {
        console.info(`LH Report  : ${lh.htmlReportPath}`);
        console.info(`LH Score   : Performance=${lh.performanceScore}  Accessibility=${lh.accessibilityScore}  Best-Practices=${lh.bestPracticesScore}  SEO=${lh.seoScore}`);
      }
    } else {
      const results = await PageLoadAnalyzerAgent.analyzeAll(urls);
      results.forEach(r => {
        const lh = r.metrics.lighthouseData;
        console.info(`\n[${r.metrics.pageName}]`);
        console.info(`  HTML      : ${r.htmlReportPath}`);
        console.info(`  Excel     : ${r.excelReportPath}`);
        if (lh) {
          console.info(`  LH Report : ${lh.htmlReportPath}`);
          console.info(`  LH Score  : Performance=${lh.performanceScore}  Accessibility=${lh.accessibilityScore}  Best-Practices=${lh.bestPracticesScore}  SEO=${lh.seoScore}`);
        }
      });
    }
  } catch (e) {
    console.error('Analysis failed:', e);
  }
}

main().catch(e => {
  console.error('Fatal error:', e);
  process.exit(1);
});
