import { FullConfig } from '@playwright/test';
import * as HtmlReportManager from '../../src/reports/HtmlReportManager';
import * as LogManager         from '../../src/utils/LogManager';
import { ConfigReader }        from '../../src/config/ConfigReader';

export default async function globalSetup(_config: FullConfig): Promise<void> {
  const reportDir = ConfigReader.getInstance().getReportPath();
  LogManager.init(reportDir);
  HtmlReportManager.init();
}
