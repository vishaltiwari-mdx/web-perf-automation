import { FullConfig } from '@playwright/test';
import * as HtmlReportManager from '../../src/reports/HtmlReportManager';
import * as LogManager         from '../../src/utils/LogManager';

export default async function globalTeardown(_config: FullConfig): Promise<void> {
  // Only flush the HTML report if sections were actually added in this process.
  // When running --project=dynamic, DynamicUrlTest manages its own report lifecycle
  // inside the worker process (beforeAll / afterAll).  The main process never calls
  // addMetrics(), so hasSections() returns false and we avoid writing an empty report.
  if (HtmlReportManager.hasSections()) {
    HtmlReportManager.flush();
  }
  LogManager.flush();
}
