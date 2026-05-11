import { test as base, Page } from '@playwright/test';
import { NavigationRouter } from '../../src/pages/NavigationRouter';
import { MetricsCollector } from '../../src/metrics/MetricsCollector';
import { ConfigReader } from '../../src/config/ConfigReader';

type PerformanceFixtures = {
  router: NavigationRouter;
  collector: MetricsCollector;
};

/**
 * Extended test with `router` and `collector` fixtures.
 * Import this `test` instead of the base Playwright `test` in spec files.
 */
export const test = base.extend<PerformanceFixtures>({
  // Override headless from config.properties at runtime
  launchOptions: async ({}, use) => {
    const config = ConfigReader.getInstance();
    await use({
      headless: config.isHeadless(),
      args: [
        '--start-maximized',
        '--disable-extensions',
        '--disable-popup-blocking',
        '--disable-notifications',
        '--no-sandbox',
        '--disable-dev-shm-usage',
        '--enable-precise-memory-info',
      ],
    });
  },

  router: async ({ page }, use) => {
    await use(new NavigationRouter(page));
  },

  collector: async ({ page }, use) => {
    await use(new MetricsCollector(page));
  },
});

export { expect } from '@playwright/test';
