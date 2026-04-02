package com.perforce.performance.reports;

import com.perforce.performance.metrics.PerformanceMetrics;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.common.usermodel.HyperlinkType;

/**
 * ExcelReportManager - Generates a formatted Excel (.xlsx) performance report
 * with colour-coded cells based on benchmark thresholds.
 */
public class ExcelReportManager {

    private static final Logger log = LoggerFactory.getLogger(ExcelReportManager.class);

    public static void generate(List<PerformanceMetrics> metricsList, String filePath) {
        new File(filePath).getParentFile().mkdirs();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // ── Sheet 1: Full Report ───────────────────────────────────────
            XSSFSheet sheet = workbook.createSheet("Performance Report");
            sheet.setColumnWidth(0, 8000);
            for (int i = 1; i <= 14; i++) sheet.setColumnWidth(i, 4500);
            sheet.setColumnWidth(15, 5000);
            sheet.setColumnWidth(16, 5000);

            // Styles
            CellStyle titleStyle   = createTitleStyle(workbook);
            CellStyle headerStyle  = createHeaderStyle(workbook);
            CellStyle goodStyle    = createColorStyle(workbook, new XSSFColor(new byte[]{(byte)34,(byte)139,(byte)34}, null));
            CellStyle needsStyle   = createColorStyle(workbook, new XSSFColor(new byte[]{(byte)255,(byte)165,(byte)0}, null));
            CellStyle poorStyle    = createColorStyle(workbook, new XSSFColor(new byte[]{(byte)220,(byte)20,(byte)60}, null));
            CellStyle naStyle      = createColorStyle(workbook, new XSSFColor(new byte[]{(byte)128,(byte)128,(byte)128}, null));
            CellStyle dataStyle    = createDataStyle(workbook);
            CellStyle linkStyle    = createHyperlinkStyle(workbook);

            int rowIdx = 0;

            // Title
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Web Application Performance Report — Generated: "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));

            rowIdx++; // blank row

            // Headers
            String[] headers = {
                "Page Name", "URL",
                "Load Time (ms)", "FCP (ms)", "LCP (ms)", "TTI (ms)", "CLS", "TTFB (ms)",
                "DNS (ms)", "TCP (ms)", "DCL (ms)", "DOM Interactive (ms)",
                "Resources", "Transfer Size (KB)", "AG-Grid Spinner (ms)",
                "Overall Status", "Screenshot"
            };
            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            for (PerformanceMetrics m : metricsList) {
                Row row = sheet.createRow(rowIdx++);

                setCell(row, 0,  m.getPageName(),                          dataStyle);
                setCell(row, 1,  m.getPageUrl(),                           dataStyle);
                setNumCell(row, 2,  m.getPageLoadTime(),                   statusStyle(m.getPageLoadStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setNumCell(row, 3,  m.getFirstContentfulPaint(),           statusStyle(m.getFcpStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setNumCell(row, 4,  m.getLargestContentfulPaint(),         statusStyle(m.getLcpStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setNumCell(row, 5,  m.getTimeToInteractive(),              statusStyle(m.getTtiStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setFmtCell(row, 6,  m.getCumulativeLayoutShift(), "0.0000", statusStyle(m.getClsStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setNumCell(row, 7,  m.getTimeToFirstByte(),                statusStyle(m.getTtfbStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setNumCell(row, 8,  m.getDnsLookupTime(),                  dataStyle);
                setNumCell(row, 9,  m.getTcpConnectionTime(),              dataStyle);
                setNumCell(row, 10, m.getDomContentLoadedTime(),           dataStyle);
                setNumCell(row, 11, m.getDomInteractiveTime(),             dataStyle);
                setCell(row, 12,    String.valueOf(m.getTotalResources()), dataStyle);
                setNumCell(row, 13, m.getTransferSize() / 1024.0,          dataStyle);
                // AG-Grid spinner time — colour-coded: green <1s, amber <3s, red ≥3s (0 = N/A)
                CellStyle agStyle = m.getAgGridLoadTime() <= 0   ? naStyle
                                  : m.getAgGridLoadTime() < 1000 ? goodStyle
                                  : m.getAgGridLoadTime() < 3000 ? needsStyle : poorStyle;
                if (m.getAgGridLoadTime() > 0) {
                    setNumCell(row, 14, m.getAgGridLoadTime(), agStyle);
                } else {
                    setCell(row, 14, "N/A", naStyle);
                }
                setCell(row, 15,    m.getOverallStatus(),
                        statusStyle(m.getOverallStatus(), goodStyle, needsStyle, poorStyle, naStyle));
                setScreenshotCell(row, 16, m.getScreenshotPath(), workbook, linkStyle, dataStyle);
            }

            // ── Sheet 2: Benchmark Reference ──────────────────────────────
            XSSFSheet refSheet = workbook.createSheet("Benchmark Reference");
            addBenchmarkReference(refSheet, workbook, headerStyle, dataStyle, goodStyle, needsStyle, poorStyle);

            // Save
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            log.info("Excel report saved: {}", filePath);

        } catch (Exception e) {
            log.error("Failed to generate Excel report: {}", e.getMessage(), e);
        }
    }

    // ─── Benchmark Reference Sheet ────────────────────────────────────────────

    private static void addBenchmarkReference(XSSFSheet sheet, XSSFWorkbook wb,
            CellStyle headerStyle, CellStyle dataStyle,
            CellStyle goodStyle, CellStyle needsStyle, CellStyle poorStyle) {

        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 4000);

        Row h = sheet.createRow(0);
        setCell(h, 0, "Metric",             headerStyle);
        setCell(h, 1, "GOOD",               headerStyle);
        setCell(h, 2, "NEEDS IMPROVEMENT",  headerStyle);
        setCell(h, 3, "POOR",               headerStyle);

        String[][] benchmarks = {
            {"LCP (Largest Contentful Paint)", "≤ 2500 ms",  "2500–4000 ms", "> 4000 ms"},
            {"FCP (First Contentful Paint)",   "≤ 1800 ms",  "1800–3000 ms", "> 3000 ms"},
            {"TTI (Time to Interactive)",      "≤ 3800 ms",  "3800–7300 ms", "> 7300 ms"},
            {"CLS (Cumulative Layout Shift)",  "≤ 0.1",      "0.1–0.25",     "> 0.25"},
            {"TTFB (Time to First Byte)",      "≤ 800 ms",   "800–1800 ms",  "> 1800 ms"},
            {"Page Load Time",                 "≤ 3000 ms",  "3000–6000 ms", "> 6000 ms"},
            {"INP (Interaction to Next Paint)","≤ 200 ms",   "200–500 ms",   "> 500 ms"},
            {"TBT (Total Blocking Time)",      "≤ 200 ms",   "200–600 ms",   "> 600 ms"},
        };

        for (int i = 0; i < benchmarks.length; i++) {
            Row row = sheet.createRow(i + 1);
            setCell(row, 0, benchmarks[i][0], dataStyle);
            setCell(row, 1, benchmarks[i][1], goodStyle);
            setCell(row, 2, benchmarks[i][2], needsStyle);
            setCell(row, 3, benchmarks[i][3], poorStyle);
        }
    }

    // ─── Style Helpers ────────────────────────────────────────────────────────

    private static CellStyle statusStyle(String status, CellStyle good, CellStyle needs, CellStyle poor, CellStyle na) {
        if (status == null)                    return na;
        if ("GOOD".equals(status))             return good;
        if ("NEEDS IMPROVEMENT".equals(status)) return needs;
        if ("POOR".equals(status))             return poor;
        return na;
    }

    private static CellStyle createTitleStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)30,(byte)30,(byte)60}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setColor(new XSSFColor(new byte[]{(byte)255,(byte)255,(byte)255}, null));
        return style;
    }

    private static CellStyle createHeaderStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setColor(new XSSFColor(new byte[]{(byte)255,(byte)255,(byte)255}, null));
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)45,(byte)45,(byte)100}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createColorStyle(XSSFWorkbook wb, XSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = wb.createFont();
        font.setColor(new XSSFColor(new byte[]{(byte)255,(byte)255,(byte)255}, null));
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createDataStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createHyperlinkStyle(XSSFWorkbook wb) {
        CellStyle style = createDataStyle(wb);
        XSSFFont font = wb.createFont();
        font.setUnderline(FontUnderline.SINGLE);
        font.setColor(IndexedColors.BLUE.getIndex());
        style.setFont(font);
        return style;
    }

    private static void setCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        if (style != null) cell.setCellStyle(style);
    }

    private static void setNumCell(Row row, int col, double value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(Math.round(value));
        if (style != null) cell.setCellStyle(style);
    }

    private static void setFmtCell(Row row, int col, double value, String fmt, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        if (style != null) cell.setCellStyle(style);
    }

    private static void setScreenshotCell(Row row, int col, String screenshotPath,
                                          XSSFWorkbook workbook, CellStyle linkStyle, CellStyle dataStyle) {
        if (screenshotPath == null || screenshotPath.isBlank()) {
            setCell(row, col, "N/A", dataStyle);
            return;
        }

        Cell cell = row.createCell(col);
        cell.setCellValue("View Screenshot");
        cell.setCellStyle(linkStyle);

        CreationHelper helper = workbook.getCreationHelper();
        Hyperlink link = helper.createHyperlink(HyperlinkType.FILE);

        // POI validates hyperlink addresses as URIs — backslashes are illegal.
        // Build a file:/// URI with forward slashes so Excel can open the link on all platforms.
        try {
            String forwardSlash = screenshotPath.replace('\\', '/');
            // If it's already absolute (e.g. C:/...) prefix with file:///
            // If relative, resolve against the current working directory first
            java.io.File screenshotFile = new java.io.File(screenshotPath);
            String uri = screenshotFile.isAbsolute()
                    ? screenshotFile.toURI().toString()
                    : "file:///" + forwardSlash;
            link.setAddress(uri);
            cell.setHyperlink(link);
        } catch (Exception e) {
            // Path could not be converted to a URI — just show the path as plain text
            cell.setCellValue(screenshotPath);
            cell.setCellStyle(dataStyle);
        }
    }
}
