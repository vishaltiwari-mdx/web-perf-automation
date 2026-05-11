import { defineConfig } from '@playwright/test';

// Config is read lazily at test time, not at import time, to avoid issues
// with the config file being loaded before node_modules are installed.
export default defineConfig({
  testDir: './tests',
  globalSetup: './tests/fixtures/globalSetup.ts',
  globalTeardown: './tests/fixtures/globalTeardown.ts',
  timeout: 180_000,
  expect: { timeout: 30_000 },
  reporter: [
    ['html', { outputFolder: 'playwright-report', open: 'never' }],
    ['list'],
  ],
  use: {
    browserName: 'chromium',
    // headless is read from config.properties at runtime via fixture
    launchOptions: {
      args: [
        '--start-maximized',
        '--disable-extensions',
        '--disable-popup-blocking',
        '--disable-notifications',
        '--no-sandbox',
        '--disable-dev-shm-usage',
        '--enable-precise-memory-info',
      ],
    },
    viewport: null,
    actionTimeout: 30_000,
  },
  projects: [
    { name: 'full-suite',  testMatch: '**/WebPerformanceTest.spec.ts' },
    { name: 'helix-pages', testMatch: '**/HelixPagePerformanceTest.spec.ts' },
    { name: 'analyzer',    testMatch: '**/PageLoadAnalyzerTest.spec.ts' },
    // Spawned by the dashboard server (npm run start → Analyze button)
    { name: 'dynamic',     testMatch: '**/DynamicUrlTest.spec.ts' },
  ],
});
