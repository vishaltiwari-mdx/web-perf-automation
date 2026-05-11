import { chromium, Browser, BrowserContext, Page } from '@playwright/test';
import { ConfigReader } from '../config/ConfigReader';

export interface BrowserSession {
  browser: Browser;
  context: BrowserContext;
  page: Page;
}

/**
 * BrowserManager — standalone Playwright browser lifecycle manager.
 * Used by PageLoadAnalyzerAgent (outside of Playwright Test fixtures).
 * Tests use the built-in `page` fixture instead.
 */
export class BrowserManager {
  private BrowserManager() {}

  static async launch(): Promise<BrowserSession> {
    const config = ConfigReader.getInstance();
    const headless = config.isHeadless();

    const browser = await chromium.launch({
      headless,
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

    const context = await browser.newContext({ viewport: null });
    const page = await context.newPage();
    page.setDefaultTimeout(config.getPageLoadTimeout() * 1000);

    return { browser, context, page };
  }

  static async close(session: BrowserSession): Promise<void> {
    await session.browser.close();
  }
}
