import { Page } from '@playwright/test';
import * as path from 'path';
import * as fs from 'fs';

/**
 * ScreenshotManager — captures page screenshots and returns report-relative paths.
 */
export class ScreenshotManager {
  private ScreenshotManager() {}

  static async capture(
    page: Page,
    pageName: string,
    screenshotDir: string,
    reportDir: string,
  ): Promise<string | null> {
    try {
      const absDir = path.resolve(screenshotDir);
      fs.mkdirSync(absDir, { recursive: true });

      const timestamp = new Date()
        .toISOString()
        .replace(/[:.]/g, '-')
        .replace('T', '_')
        .slice(0, 23);
      const fileName  = `${ScreenshotManager.sanitize(pageName)}_${timestamp}.png`;
      const targetFile = path.join(absDir, fileName);

      await page.screenshot({ path: targetFile, fullPage: false });

      const absReport = path.resolve(reportDir);
      let rel: string;
      try {
        rel = path.relative(absReport, targetFile);
      } catch {
        rel = targetFile;
      }
      return rel.replace(/\\/g, '/');
    } catch (e) {
      console.warn(`Failed to capture screenshot for ${pageName}: ${e}`);
      return null;
    }
  }

  private static sanitize(v: string): string {
    if (!v || !v.trim()) return 'page';
    return v.replace(/[^a-zA-Z0-9._-]/g, '_');
  }
}
