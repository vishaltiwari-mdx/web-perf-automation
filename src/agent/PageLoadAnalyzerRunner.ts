#!/usr/bin/env ts-node
/**
 * PageLoadAnalyzerRunner — interactive CLI entry point.
 *
 * Usage:
 *   # Interactive prompt
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts
 *
 *   # Non-interactive with inline URLs (comma-separated)
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts -- "https://a.com,https://b.com"
 *
 *   # Non-interactive with a URLs file (one URL per line)
 *   npx ts-node src/agent/PageLoadAnalyzerRunner.ts -- "/path/to/urls.txt"
 *
 *   # Via npm script
 *   npm run analyze
 */
import * as readline from 'readline';
import * as fs from 'fs';
import { PageLoadAnalyzerAgent } from './PageLoadAnalyzerAgent';

async function main(): Promise<void> {
  console.info('╔══════════════════════════════════════════════════════════╗');
  console.info('║         Web Performance Analyzer — TypeScript CLI        ║');
  console.info('╚══════════════════════════════════════════════════════════╝');

  // ── Non-interactive: check command-line argument ─────────────────────────
  const arg = process.argv.slice(2).join(' ').trim();
  if (arg) {
    await runWithArg(arg);
    return;
  }

  // ── Interactive mode ─────────────────────────────────────────────────────
  console.info('');
  console.info('Enter a URL, a comma-separated list of URLs, or a path to a .txt file');
  console.info('with one URL per line.  Press Enter to confirm. Type "quit" to exit.');
  console.info('');

  const rl = readline.createInterface({
    input:  process.stdin,
    output: process.stdout,
  });

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
  // Could be a file path
  if (fs.existsSync(arg)) {
    const lines = fs.readFileSync(arg, 'utf8').split('\n').map(l => l.trim()).filter(l => l && !l.startsWith('#'));
    if (lines.length === 0) {
      console.warn('File is empty or contains only comments.');
      return;
    }
    console.info(`Loaded ${lines.length} URL(s) from file: ${arg}`);
    await analyzeUrls(lines);
    return;
  }

  // Comma-separated URLs
  const urls = arg.split(',').map(u => u.trim()).filter(u => u.length > 0);
  await analyzeUrls(urls);
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
