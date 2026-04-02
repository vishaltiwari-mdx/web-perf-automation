package com.perforce.performance.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * PageLoadAnalyzerRunner — Interactive console launcher for the Page Load Analyzer Agent.
 *
 * Run this class directly (e.g. from your IDE or via Maven exec plugin):
 *
 *   mvn exec:java -Dexec.mainClass="com.perforce.performance.agent.PageLoadAnalyzerRunner"
 *
 * The runner will ask:
 *   1. Enter URLs directly (comma-separated)
 *   2. Load from a .txt file (URLs comma-separated inside the file)
 *
 * Output per run:
 *   • Individual HTML report per URL  →  reports/page-load-{name}-{ts}.html
 *   • Combined HTML report (all URLs, no screenshots)  →  reports/combined-performance-report-{ts}.html
 *   • Combined Excel report (all URLs)  →  reports/combined-analysis-{ts}.xlsx
 */
public class PageLoadAnalyzerRunner {

    private static final Logger log = LoggerFactory.getLogger(PageLoadAnalyzerRunner.class);

    public static void main(String[] args) {
        printBanner();

        List<String> urls = resolveUrls(args);

        if (urls == null) {
            // No args / system properties — fall back to interactive console
            Scanner scanner = new Scanner(System.in);
            urls = promptForUrls(scanner);
            scanner.close();
        }

        if (urls.isEmpty()) {
            System.out.println("[ERROR] No URLs provided. Exiting.");
            System.exit(1);
        }

        System.out.println();
        System.out.println("  URLs to analyze (" + urls.size() + "):");
        for (int i = 0; i < urls.size(); i++) {
            System.out.printf("    %d. %s%n", i + 1, urls.get(i));
        }
        System.out.println();
        System.out.println("  Starting analysis...");
        System.out.println("─".repeat(62));

        long start = System.currentTimeMillis();

        List<PageLoadAnalyzerAgent.PerformanceAnalysisResult> results =
                urls.size() == 1
                        ? List.of(PageLoadAnalyzerAgent.analyze(urls.get(0)))
                        : PageLoadAnalyzerAgent.analyzeAll(urls);

        long elapsed = System.currentTimeMillis() - start;

        printFinalSummary(results, elapsed);
    }

    // ─── Non-interactive URL resolution ──────────────────────────────────────

    /**
     * Tries to resolve URLs without user interaction. Checks (in order):
     *   1. Program arg (exec.args) — file path, e.g. C:/Users/vishal/urls.txt
     *   2. Program arg (exec.args) — direct URLs, e.g. https://a.com,https://b.com
     *   3. System property -DurlsFile=C:/Users/vishal/urls.txt
     *   4. System property -Durls=https://a.com,https://b.com
     *
     * Returns {@code null} if none of the above are present (caller falls back to interactive).
     */
    private static List<String> resolveUrls(String[] args) {
        // 1 & 2: program argument (from -Dexec.args)
        if (args != null && args.length > 0) {
            String arg = String.join(",", args).trim();
            if (!arg.isBlank()) {
                java.io.File f = new java.io.File(arg);
                if (f.exists() && f.isFile()) {
                    System.out.println("  Using file from argument: " + arg);
                    return loadFromFile(arg);
                }
                System.out.println("  Using URLs from argument.");
                return parseCommaSeparated(arg);
            }
        }

        // 3: -DurlsFile=...  (no dot — avoids Maven misinterpreting as plugin:goal)
        String fileProp = System.getProperty("urlsFile");
        if (fileProp != null && !fileProp.isBlank()) {
            System.out.println("  Using file from -DurlsFile: " + fileProp);
            return loadFromFile(fileProp.trim());
        }

        // 4: -Durls=...
        String urlsProp = System.getProperty("urls");
        if (urlsProp != null && !urlsProp.isBlank()) {
            System.out.println("  Using URLs from -Durls property.");
            return parseCommaSeparated(urlsProp.trim());
        }

        return null; // nothing found — use interactive prompt
    }

    private static List<String> loadFromFile(String filePath) {
        filePath = filePath.replaceAll("^[\"']|[\"']$", ""); // strip surrounding quotes
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        if (!java.nio.file.Files.exists(path)) {
            System.out.println("  [ERROR] File not found: " + filePath);
            return List.of();
        }
        try {
            String content = java.nio.file.Files.readString(path).trim();
            List<String> urls = parseCommaSeparated(content);
            System.out.println("  Loaded " + urls.size() + " URL(s) from: " + filePath);
            return urls;
        } catch (java.io.IOException e) {
            System.out.println("  [ERROR] Could not read file: " + e.getMessage());
            return List.of();
        }
    }

    // ─── URL input prompts ────────────────────────────────────────────────────

    private static List<String> promptForUrls(Scanner scanner) {
        System.out.println("  How would you like to provide URLs?");
        System.out.println();
        System.out.println("    1  →  Enter URLs directly (comma-separated)");
        System.out.println("    2  →  Load from a .txt file (URLs comma-separated inside)");
        System.out.println();
        System.out.print("  Your choice [1/2]: ");

        String choice = scanner.nextLine().trim();

        if ("1".equals(choice)) {
            return promptDirectUrls(scanner);
        } else if ("2".equals(choice)) {
            return promptFileUrls(scanner);
        } else {
            System.out.println("  [WARN] Unrecognised choice '" + choice + "'. Falling back to direct input.");
            return promptDirectUrls(scanner);
        }
    }

    private static List<String> promptDirectUrls(Scanner scanner) {
        System.out.println();
        System.out.println("  Enter one or more URLs separated by commas.");
        System.out.println("  Example: https://example.com, https://example.com/about");
        System.out.println();
        System.out.print("  URLs: ");

        String input = scanner.nextLine().trim();
        return parseCommaSeparated(input);
    }

    private static List<String> promptFileUrls(Scanner scanner) {
        System.out.println();
        System.out.println("  Enter the path to the .txt file containing URLs.");
        System.out.println("  URLs inside the file must be separated by commas.");
        System.out.println("  Example file content:");
        System.out.println("    https://example.com, https://example.com/about, https://example.com/contact");
        System.out.println();
        System.out.print("  File path: ");

        String filePath = scanner.nextLine().trim();

        // Strip surrounding quotes that shells/file pickers sometimes add
        filePath = filePath.replaceAll("^[\"']|[\"']$", "");

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("  [ERROR] File not found: " + filePath);
            return List.of();
        }
        if (!Files.isReadable(path)) {
            System.out.println("  [ERROR] File is not readable: " + filePath);
            return List.of();
        }

        try {
            String content = Files.readString(path).trim();
            List<String> urls = parseCommaSeparated(content);
            System.out.println("  Loaded " + urls.size() + " URL(s) from: " + filePath);
            return urls;
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not read file: " + e.getMessage());
            log.error("Failed to read URL file: {}", filePath, e);
            return List.of();
        }
    }

    // ─── Final summary ────────────────────────────────────────────────────────

    private static void printFinalSummary(
            List<PageLoadAnalyzerAgent.PerformanceAnalysisResult> results, long elapsedMs) {

        System.out.println();
        System.out.println("═".repeat(62));
        System.out.println("  ANALYSIS COMPLETE");
        System.out.printf("  Total time: %.1f seconds%n", elapsedMs / 1000.0);
        System.out.println("─".repeat(62));
        System.out.printf("  %-4s  %-28s  %-10s  %s%n", "#", "Page", "Overall", "Report");
        System.out.println("─".repeat(62));

        for (int i = 0; i < results.size(); i++) {
            PageLoadAnalyzerAgent.PerformanceAnalysisResult r = results.get(i);
            System.out.printf("  %-4d  %-28s  %-10s  %s%n",
                    i + 1,
                    truncate(r.getMetrics().getPageName(), 28),
                    r.getMetrics().getOverallStatus(),
                    r.getHtmlReportPath());
        }

        if (!results.isEmpty() && results.get(0).getExcelReportPath() != null) {
            System.out.println("─".repeat(62));
            System.out.println("  Combined Excel : " + results.get(0).getExcelReportPath());
        }

        System.out.println("═".repeat(62));
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static List<String> parseCommaSeparated(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         PAGE LOAD PERFORMANCE ANALYZER AGENT            ║");
        System.out.println("║                                                          ║");
        System.out.println("║  Metrics: Load · TTFB · FCP · LCP · TTI · CLS · INP    ║");
        System.out.println("║  Output : Individual HTML · Combined HTML · Excel        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}