import PropertiesReader from 'properties-reader';
import * as path from 'path';
import * as fs from 'fs';

/**
 * ConfigReader — singleton that loads config.properties from the project root.
 * Drop-in equivalent of the Java ConfigReader.
 */
export class ConfigReader {
  private static instance: ConfigReader;
  private reader: PropertiesReader.Reader;

  private constructor() {
    // Look for config.properties in the working directory (project root)
    const candidates = [
      path.resolve(process.cwd(), 'config.properties'),
      path.resolve(__dirname, '../../config.properties'),
      path.resolve(__dirname, '../../../config.properties'),
    ];
    const found = candidates.find(p => fs.existsSync(p));
    if (!found) {
      throw new Error(
        `config.properties not found. Tried:\n  ${candidates.join('\n  ')}`
      );
    }
    this.reader = PropertiesReader(found);
  }

  static getInstance(): ConfigReader {
    if (!ConfigReader.instance) {
      ConfigReader.instance = new ConfigReader();
    }
    return ConfigReader.instance;
  }

  get(key: string): string {
    const value = this.reader.get(key);
    if (value === null || value === undefined) {
      throw new Error(`Property not found: ${key}`);
    }
    return String(value).trim();
  }

  getOrDefault(key: string, defaultValue: string): string {
    const value = this.reader.get(key);
    if (value === null || value === undefined) return defaultValue;
    return String(value).trim();
  }

  getInt(key: string, defaultValue: number): number {
    const raw = this.getOrDefault(key, String(defaultValue));
    const parsed = parseInt(raw, 10);
    return isNaN(parsed) ? defaultValue : parsed;
  }

  getBoolean(key: string, defaultValue: boolean): boolean {
    return this.getOrDefault(key, String(defaultValue)).toLowerCase() === 'true';
  }

  // ── Convenience getters ────────────────────────────────────────────────────

  getBaseUrl(): string      { return this.get('app.base.url'); }
  getLoginUrl(): string     { return this.getOrDefault('app.login.url', this.getBaseUrl() + '/login'); }
  getUsername(): string     { return this.get('app.username'); }
  getPassword(): string     { return this.get('app.password'); }
  getBrowser(): string      { return this.getOrDefault('browser', 'chrome'); }
  isHeadless(): boolean     { return this.getBoolean('browser.headless', false); }
  getPageLoadTimeout(): number { return this.getInt('timeout.pageload', 30); }
  getImplicitWait(): number    { return this.getInt('timeout.implicit', 10); }
  getPerformanceRuns(): number { return this.getInt('performance.runs', 3); }
  getReportPath(): string      { return this.getOrDefault('report.output.path', 'reports'); }
  getScreenshotPath(): string  {
    return this.getOrDefault('report.screenshot.path', this.getReportPath() + '/screenshots');
  }
}
