import ExcelJS from 'exceljs';
import * as fs from 'fs';
import * as path from 'path';
import { PerformanceMetrics } from '../metrics/PerformanceMetrics';
import { ApiRequestMetric } from '../metrics/ApiRequestMetric';

// ── Color palette (mirrors Java Apache POI RGB values) ────────────────────────
const COLOR_TITLE_BG   = 'FF1E1E3C'; // dark navy
const COLOR_HEADER_BG  = 'FF2D2D64'; // dark blue
const COLOR_WHITE      = 'FFFFFFFF';
const COLOR_GOOD       = 'FF228B22'; // forest green
const COLOR_NEEDS      = 'FFFFA500'; // orange
const COLOR_POOR       = 'FFDC143C'; // crimson
const COLOR_NA         = 'FF808080'; // gray
const COLOR_BLUE_LINK  = 'FF0000FF';

/**
 * ExcelReportManager — generates a formatted .xlsx performance report.
 * Mirrors the Java Apache POI implementation.
 */
export class ExcelReportManager {
  private ExcelReportManager() {}

  static async generate(metricsList: PerformanceMetrics[], filePath: string): Promise<void> {
    const dir = path.dirname(filePath);
    fs.mkdirSync(dir, { recursive: true });

    const wb = new ExcelJS.Workbook();

    // ── Sheet 1: Performance Report ──────────────────────────────────────────
    const sheet1 = wb.addWorksheet('Performance Report');
    sheet1.getColumn(1).width  = 22;   // Page Name
    sheet1.getColumn(2).width  = 30;   // URL
    for (let i = 3; i <= 15; i++) sheet1.getColumn(i).width = 16;
    sheet1.getColumn(16).width = 22;   // Action → Page Ready
    sheet1.getColumn(17).width = 18;   // Overall Status
    sheet1.getColumn(18).width = 18;   // Screenshot

    // Title row
    const titleRow = sheet1.addRow([
      `Web Application Performance Report — Generated: ${new Date().toLocaleString()}`,
    ]);
    sheet1.mergeCells(titleRow.number, 1, titleRow.number, 18);
    applyTitleStyle(titleRow.getCell(1));

    sheet1.addRow([]); // blank

    // Headers
    const headers = [
      'Page Name', 'URL',
      'Load Time (ms)', 'FCP (ms)', 'LCP (ms)', 'TTI (ms)', 'CLS', 'TTFB (ms)',
      'DNS (ms)', 'TCP (ms)', 'DCL (ms)', 'DOM Interactive (ms)',
      'Resources', 'Transfer Size (KB)', 'AG-Grid Spinner (ms)',
      'Action → Page Ready (ms)', 'Overall Status', 'Screenshot',
    ];
    const headerRow = sheet1.addRow(headers);
    headerRow.eachCell(cell => applyHeaderStyle(cell));

    // Data rows
    metricsList.forEach(m => {
      const rowData = sheet1.addRow([
        m.pageName,
        m.pageUrl,
        Math.round(m.pageLoadTime),
        Math.round(m.firstContentfulPaint),
        Math.round(m.largestContentfulPaint),
        Math.round(m.timeToInteractive),
        m.cumulativeLayoutShift,
        Math.round(m.timeToFirstByte),
        Math.round(m.dnsLookupTime),
        Math.round(m.tcpConnectionTime),
        Math.round(m.domContentLoadedTime),
        Math.round(m.domInteractiveTime),
        m.totalResources,
        Math.round(m.transferSize / 1024),
        m.agGridLoadTime > 0 ? Math.round(m.agGridLoadTime) : 'N/A',
        m.actionToLoadMs > 0 ? Math.round(m.actionToLoadMs) : 'N/A',
        m.overallStatus,
        m.screenshotPath ? 'View Screenshot' : 'N/A',
      ]);

      applyDataStyle(rowData.getCell(1));
      applyDataStyle(rowData.getCell(2));
      applyStatusStyle(rowData.getCell(3),  m.pageLoadStatus);
      applyStatusStyle(rowData.getCell(4),  m.fcpStatus);
      applyStatusStyle(rowData.getCell(5),  m.lcpStatus);
      applyStatusStyle(rowData.getCell(6),  m.ttiStatus);
      applyStatusStyle(rowData.getCell(7),  m.clsStatus);
      applyStatusStyle(rowData.getCell(8),  m.ttfbStatus);
      for (let c = 9; c <= 14; c++) applyDataStyle(rowData.getCell(c));

      // AG-Grid spinner (col 15)
      const agStatus = m.agGridLoadTime <= 0 ? 'N/A'
                     : m.agGridLoadTime < 1000 ? 'GOOD'
                     : m.agGridLoadTime < 3000 ? 'NEEDS IMPROVEMENT' : 'POOR';
      applyStatusStyle(rowData.getCell(15), agStatus);
      rowData.getCell(15).numFmt = '0';

      // CLS format
      rowData.getCell(7).numFmt = '0.0000';

      // Action → Page Ready (col 16)
      const a2lStatus = m.actionToLoadMs <= 0    ? 'N/A'
                      : m.actionToLoadMs < 3000   ? 'GOOD'
                      : m.actionToLoadMs < 8000   ? 'NEEDS IMPROVEMENT' : 'POOR';
      applyStatusStyle(rowData.getCell(16), a2lStatus);

      applyStatusStyle(rowData.getCell(17), m.overallStatus);

      // Screenshot hyperlink (col 18)
      const screenshotCell = rowData.getCell(18);
      if (m.screenshotPath) {
        applyLinkStyle(screenshotCell);
        try {
          const absPath = path.resolve(m.screenshotPath);
          screenshotCell.value = {
            text: 'View Screenshot',
            hyperlink: absPath.startsWith('file') ? absPath : `file:///${absPath.replace(/\\/g, '/')}`,
          };
        } catch {
          applyDataStyle(screenshotCell);
          screenshotCell.value = m.screenshotPath;
        }
      } else {
        applyDataStyle(screenshotCell);
        screenshotCell.value = 'N/A';
      }
    });

    // ── Sheet 2: Benchmark Reference ────────────────────────────────────────
    const sheet2 = wb.addWorksheet('Benchmark Reference');
    sheet2.getColumn(1).width = 28;
    sheet2.getColumn(2).width = 14;
    sheet2.getColumn(3).width = 22;
    sheet2.getColumn(4).width = 14;

    const refHeader = sheet2.addRow(['Metric', 'GOOD', 'NEEDS IMPROVEMENT', 'POOR']);
    refHeader.eachCell(cell => applyHeaderStyle(cell));

    const benchmarks: [string, string, string, string][] = [
      ['LCP (Largest Contentful Paint)', '≤ 2500 ms',  '2500–4000 ms', '> 4000 ms'],
      ['FCP (First Contentful Paint)',   '≤ 1800 ms',  '1800–3000 ms', '> 3000 ms'],
      ['TTI (Time to Interactive)',      '≤ 3800 ms',  '3800–7300 ms', '> 7300 ms'],
      ['CLS (Cumulative Layout Shift)',  '≤ 0.1',      '0.1–0.25',     '> 0.25'],
      ['TTFB (Time to First Byte)',      '≤ 800 ms',   '800–1800 ms',  '> 1800 ms'],
      ['Page Load Time',                 '≤ 3000 ms',  '3000–6000 ms', '> 6000 ms'],
      ['INP (Interaction to Next Paint)','≤ 200 ms',   '200–500 ms',   '> 500 ms'],
      ['TBT (Total Blocking Time)',      '≤ 200 ms',   '200–600 ms',   '> 600 ms'],
    ];
    benchmarks.forEach(([metric, good, needs, poor]) => {
      const r = sheet2.addRow([metric, good, needs, poor]);
      applyDataStyle(r.getCell(1));
      applyColorCell(r.getCell(2), COLOR_GOOD);
      applyColorCell(r.getCell(3), COLOR_NEEDS);
      applyColorCell(r.getCell(4), COLOR_POOR);
    });

    // ── Sheet 3: Slow API Requests ────────────────────────────────────────────
    const sheet3 = wb.addWorksheet('Slow API Requests (>1s)');
    sheet3.getColumn(1).width = 22;
    sheet3.getColumn(2).width = 60;
    sheet3.getColumn(3).width = 16;
    sheet3.getColumn(4).width = 16;

    const apiTitle = sheet3.addRow([
      `Slow API Requests (duration >= 1 000 ms) — Generated: ${new Date().toLocaleString()}`,
    ]);
    sheet3.mergeCells(apiTitle.number, 1, apiTitle.number, 4);
    applyTitleStyle(apiTitle.getCell(1));

    const allSlow: Array<{ pageName: string; api: ApiRequestMetric }> = [];
    metricsList.forEach(m => {
      (m.slowApiRequests ?? []).forEach(api => allSlow.push({ pageName: m.pageName, api }));
    });

    if (allSlow.length === 0) {
      const noData = sheet3.addRow(['No API requests exceeded 1 000 ms on any measured page.']);
      sheet3.mergeCells(noData.number, 1, noData.number, 4);
      applyColorCell(noData.getCell(1), COLOR_GOOD);
    } else {
      const apiHeader = sheet3.addRow(['Page Name', 'API URL', 'Duration (ms)', 'Request Type']);
      apiHeader.eachCell(cell => applyHeaderStyle(cell));
      allSlow.forEach(({ pageName, api }) => {
        const r = sheet3.addRow([
          pageName,
          api.url,
          Math.round(api.durationMs),
          api.type,
        ]);
        applyDataStyle(r.getCell(1));
        applyDataStyle(r.getCell(2));
        applyStatusStyle(r.getCell(3), api.durationMs >= 3000 ? 'POOR' : 'NEEDS IMPROVEMENT');
        applyDataStyle(r.getCell(4));
      });
    }

    // ── Sheet 4: Lighthouse Scores ────────────────────────────────────────────
    const hasLighthouse = metricsList.some(m => m.lighthouseData != null);

    if (hasLighthouse) {
      const sheet4 = wb.addWorksheet('Lighthouse Scores');
      const lhColWidths = [22, 16, 16, 16, 12, 14, 14, 14, 12, 12, 10, 18, 18, 16, 10, 20, 18, 16, 30];
      lhColWidths.forEach((w, i) => { sheet4.getColumn(i + 1).width = w; });

      const lhTitle = sheet4.addRow([
        `Lighthouse Performance Audit — Generated: ${new Date().toLocaleString()}`,
      ]);
      sheet4.mergeCells(lhTitle.number, 1, lhTitle.number, lhColWidths.length);
      applyTitleStyle(lhTitle.getCell(1));
      sheet4.addRow([]);

      const lhHeaders = [
        'Page Name',
        'Perf Score', 'Accessibility', 'Best Practices', 'SEO',
        'FCP (ms)', 'LCP (ms)', 'Speed Index (ms)', 'TTI (ms)', 'TBT (ms)', 'CLS',
        'Server Response (ms)', 'Main Thread (ms)', 'JS Bootup (ms)', 'DOM Size',
        'Render Blocking Savings (ms)', 'Unused JS (KB)', 'Unused CSS (KB)',
        'Full LH Report',
      ];
      const lhHeaderRow = sheet4.addRow(lhHeaders);
      lhHeaderRow.eachCell(cell => applyHeaderStyle(cell));

      metricsList.forEach(m => {
        const lh = m.lighthouseData;
        if (!lh) {
          const r = sheet4.addRow([m.pageName, ...Array(lhHeaders.length - 1).fill('N/A')]);
          applyDataStyle(r.getCell(1));
          return;
        }

        const lhRow = sheet4.addRow([
          m.pageName,
          lh.performanceScore,
          lh.accessibilityScore,
          lh.bestPracticesScore,
          lh.seoScore,
          Math.round(lh.firstContentfulPaint),
          Math.round(lh.largestContentfulPaint),
          Math.round(lh.speedIndex),
          Math.round(lh.timeToInteractive),
          Math.round(lh.totalBlockingTime),
          lh.cumulativeLayoutShift,
          Math.round(lh.serverResponseTime),
          Math.round(lh.mainThreadWork),
          Math.round(lh.bootupTime),
          Math.round(lh.domSize),
          Math.round(lh.renderBlockingResourcesSavingsMs),
          Math.round(lh.unusedJavascriptBytes / 1024),
          Math.round(lh.unusedCssBytes / 1024),
          lh.htmlReportPath ? 'Open Report' : 'N/A',
        ]);

        applyDataStyle(lhRow.getCell(1));

        // Score columns (cols 2–5): color by score threshold
        for (let c = 2; c <= 5; c++) {
          const s = lhRow.getCell(c).value as number;
          applyColorCell(lhRow.getCell(c),
            s >= 90 ? COLOR_GOOD : s >= 50 ? COLOR_NEEDS : COLOR_POOR);
        }

        // Lab metric columns (6–15)
        for (let c = 6; c <= 15; c++) applyDataStyle(lhRow.getCell(c));
        lhRow.getCell(11).numFmt = '0.0000'; // CLS

        // Savings columns (16–18): highlight if > 0
        [16, 17, 18].forEach(c => {
          const v = lhRow.getCell(c).value as number;
          applyColorCell(lhRow.getCell(c), v > 0 ? COLOR_NEEDS : COLOR_GOOD);
        });

        // Report hyperlink (col 19)
        const lhLinkCell = lhRow.getCell(19);
        if (lh.htmlReportPath) {
          applyLinkStyle(lhLinkCell);
          try {
            const abs = path.resolve(lh.htmlReportPath);
            lhLinkCell.value = {
              text:      'Open Report',
              hyperlink: abs.startsWith('file') ? abs : `file:///${abs.replace(/\\/g, '/')}`,
            };
          } catch {
            applyDataStyle(lhLinkCell);
            lhLinkCell.value = lh.htmlReportPath;
          }
        } else {
          applyDataStyle(lhLinkCell);
        }
      });

      // Opportunities summary sub-sheet
      const sheet5 = wb.addWorksheet('LH Opportunities');
      sheet5.getColumn(1).width = 22;
      sheet5.getColumn(2).width = 35;
      sheet5.getColumn(3).width = 40;
      sheet5.getColumn(4).width = 16;
      sheet5.getColumn(5).width = 16;

      const oppHeader = sheet5.addRow(['Page', 'Opportunity', 'Description', 'Time Savings (ms)', 'Byte Savings (KB)']);
      oppHeader.eachCell(cell => applyHeaderStyle(cell));

      metricsList.forEach(m => {
        if (!m.lighthouseData) return;
        m.lighthouseData.opportunities.forEach(op => {
          const r = sheet5.addRow([
            m.pageName,
            op.title,
            op.description,
            op.savingsMs    ?? 'N/A',
            op.savingsBytes != null ? Math.round(op.savingsBytes / 1024) : 'N/A',
          ]);
          applyDataStyle(r.getCell(1));
          applyDataStyle(r.getCell(2));
          applyDataStyle(r.getCell(3));
          applyStatusStyle(r.getCell(4), op.savingsMs && op.savingsMs > 500 ? 'POOR' : 'NEEDS IMPROVEMENT');
          applyDataStyle(r.getCell(5));
        });
      });

      if (!metricsList.some(m => (m.lighthouseData?.opportunities.length ?? 0) > 0)) {
        const noOpp = sheet5.addRow(['No failing opportunities found across all pages.']);
        sheet5.mergeCells(noOpp.number, 1, noOpp.number, 5);
        applyColorCell(noOpp.getCell(1), COLOR_GOOD);
      }
    }

    // ── Save ──────────────────────────────────────────────────────────────────
    await wb.xlsx.writeFile(filePath);
    console.info(`[Excel] Report saved: ${filePath}`);
  }
}

// ── Style helpers ──────────────────────────────────────────────────────────────

function applyTitleStyle(cell: ExcelJS.Cell): void {
  cell.font = { bold: true, size: 14, color: { argb: COLOR_WHITE } };
  cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: COLOR_TITLE_BG } };
  cell.alignment = { horizontal: 'left', vertical: 'middle' };
}

function applyHeaderStyle(cell: ExcelJS.Cell): void {
  cell.font  = { bold: true, color: { argb: COLOR_WHITE } };
  cell.fill  = { type: 'pattern', pattern: 'solid', fgColor: { argb: COLOR_HEADER_BG } };
  cell.alignment = { horizontal: 'center', vertical: 'middle' };
  cell.border = { bottom: { style: 'thin' } };
}

function applyDataStyle(cell: ExcelJS.Cell): void {
  cell.border = {
    top:    { style: 'thin' },
    bottom: { style: 'thin' },
    left:   { style: 'thin' },
    right:  { style: 'thin' },
  };
  cell.alignment = { vertical: 'middle' };
}

function applyColorCell(cell: ExcelJS.Cell, argb: string): void {
  cell.fill  = { type: 'pattern', pattern: 'solid', fgColor: { argb } };
  cell.font  = { bold: true, color: { argb: COLOR_WHITE } };
  cell.border = { top: { style: 'thin' }, bottom: { style: 'thin' } };
  cell.alignment = { horizontal: 'center', vertical: 'middle' };
}

function applyStatusStyle(cell: ExcelJS.Cell, status: string): void {
  const argb = status === 'GOOD' ? COLOR_GOOD
             : status === 'NEEDS IMPROVEMENT' ? COLOR_NEEDS
             : status === 'POOR' ? COLOR_POOR
             : COLOR_NA;
  applyColorCell(cell, argb);
}

function applyLinkStyle(cell: ExcelJS.Cell): void {
  applyDataStyle(cell);
  cell.font = { color: { argb: COLOR_BLUE_LINK }, underline: true };
}
