/**
 * PageLoadAnalyzerTest — analyzes arbitrary URLs.
 *
 * Equivalent to the Java PageLoadAnalyzerTest (TestNG @Parameters → env var).
 *
 * Usage:
 *   # Single URL
 *   TARGET_URL="https://example.com" npx playwright test --project=analyzer
 *
 *   # Multiple URLs (comma-separated)
 *   TARGET_URL="https://a.com,https://b.com" npx playwright test --project=analyzer
 *
 *   # Falls back to app.base.url from config.properties when TARGET_URL is unset
 *   npx playwright test --project=analyzer
 */
import { test, expect } from './fixtures/performanceFixtures';
import { ConfigReader } from '../src/config/ConfigReader';
import { PageLoadAnalyzerAgent } from '../src/agent/PageLoadAnalyzerAgent';

const config = ConfigReader.getInstance();

test('Page load analysis', async () => {
  const raw = process.env['TARGET_URL'] ?? config.getBaseUrl();
  const urls = raw.split(',').map(u => u.trim()).filter(u => u.length > 0);

  expect(urls.length).toBeGreaterThan(0);

  if (urls.length === 1) {
    const result = await PageLoadAnalyzerAgent.analyze(urls[0]);

    expect(result.metrics).toBeTruthy();
    expect(result.metrics.pageLoadTime).toBeGreaterThan(0);
    expect(result.metrics.timeToFirstByte).toBeGreaterThan(0);
    expect(result.metrics.firstContentfulPaint).toBeGreaterThan(0);

    if (result.metrics.largestContentfulPaint > 4000) {
      console.warn(`LCP is POOR (${result.metrics.largestContentfulPaint.toFixed(0)} ms) for ${urls[0]}`);
    }
    if (result.metrics.timeToInteractive > 7300) {
      console.warn(`TTI is POOR (${result.metrics.timeToInteractive.toFixed(0)} ms) for ${urls[0]}`);
    }
  } else {
    const results = await PageLoadAnalyzerAgent.analyzeAll(urls);

    expect(results.length).toBe(urls.length);
    results.forEach(r => {
      expect(r.metrics).toBeTruthy();
      expect(r.metrics.pageLoadTime).toBeGreaterThan(0);
    });
  }
});
