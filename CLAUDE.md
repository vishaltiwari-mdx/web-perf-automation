# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

The project has been converted to **TypeScript + Playwright**. The original Java/Maven source remains under `src/main/` and `src/test/` for reference.

---

## Standard Navigation Practice (REQUIRED)

**Every analysis MUST follow this login-first flow — never navigate directly to a target URL.**

1. **Open the signin page first:**
   `http://vishal.mdx.perforce.com:3000/#/security/signin`

2. **Enter credentials** (read from `config.properties` — currently `admin` / `mdx`):
   - Username: `app.username` → `admin`
   - Password: `app.password` → `mdx`
   - The dashboard's per-run override fields take precedence only when BOTH are filled.

3. **Wait for home page** — do not proceed until the URL leaves the signin path and the home page is confirmed ready.

4. **Navigate to the target via UI actions** — use `NavigationAgent` with steps defined in `navigation-routes.md`. Never use `page.goto()` for the target page directly.

**Selector reference** (from `C:\Users\vishal.tiwari\Project\phi_web_automation\src`):

| Element | Selector |
|---|---|
| Username input | `xpath=//input[contains(@class,'signin-page__card__username-input')]` |
| Password input | `xpath=//input[contains(@class,'signin-page__card__password-input')]` |
| Submit button | `xpath=//button[contains(@class,'signin-page__card__submit-button')]` |
| Catalogs dropdown | `div[data-testid='ui-dropdown-menu-catalog'] > button` |
| IP catalog link | `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']` |
| Library catalog link | `//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']` |

**MCP browser automation:** `@playwright/mcp@latest` is registered in `.mcp.json` as the `playwright` server — use it for UI interactions when working interactively in Claude Code.

---

## MCP Server

The project ships a custom MCP server (`mcp-server.ts`) registered in `.mcp.json`.
Claude Code auto-starts it when you open this project. You can then ask Claude to
analyze URLs directly in chat without running any command.

**Available MCP tools:**

| Tool | Description |
|---|---|
| `analyze_url(url, page_name?)` | Full performance analysis of one URL — metrics + reports |
| `analyze_urls(urls[])` | Analyze multiple URLs — individual + combined reports |
| `get_benchmark_thresholds()` | Google Core Web Vitals threshold reference |

**Example prompts after opening this project:**
- *"Analyze https://example.com"*
- *"Run performance analysis on https://host/#/ip/catalog and https://host/#/home"*
- *"What are the Web Vitals benchmarks?"*

If you need to restart the MCP server manually:
```bash
npm run mcp
```

---

## TypeScript / Playwright Commands

```bash
# Install dependencies (first time)
npm install

# Install Playwright browser binaries
npx playwright install chromium

# Type-check without emitting
npm run typecheck

# Run all tests (all projects)
npm test

# Run a specific project (maps to testng.xml <test> blocks)
npm run test:full      # WebPerformanceTest
npm run test:helix     # HelixPagePerformanceTest
npm run test:analyze   # PageLoadAnalyzerTest

# Run a specific spec file
npx playwright test tests/HelixPagePerformanceTest.spec.ts

# Run tests matching a name pattern
npx playwright test --grep "IP Catalog"

# Run the Page Load Analyzer against arbitrary URLs
TARGET_URL="https://example.com" npx playwright test --project=analyzer
TARGET_URL="https://a.com,https://b.com" npx playwright test --project=analyzer

# Run the interactive CLI analyzer
npm run analyze
# Non-interactive with inline URLs:
npx ts-node src/agent/PageLoadAnalyzerRunner.ts -- "https://example.com,https://b.com"

# Run headless (for CI)
# Set browser.headless=true in config.properties

# Open Playwright built-in HTML report after a run
npm run report

# Generate Allure report from last run (requires allure CLI)
npm run allure:generate

# Serve Allure report in browser
npm run allure:serve
```

### Report locations after `npm test`
| Report | Path |
|---|---|
| Custom HTML (dark theme, color-coded) | `reports/performance-report-<timestamp>.html` |
| Excel (3 sheets) | `reports/performance-report.xlsx` / `reports/benchmark-multirun.xlsx` |
| Screenshots | `reports/screenshots/` |
| Playwright HTML report | `playwright-report/index.html` |
| Allure raw results | `reports/allure-results/` |

## Configuration

All runtime settings live in `config.properties` at the project root. Change these before running:

```properties
app.base.url=http://...           # base URL (no trailing slash)
app.login.url=http://...          # sign-in page URL
app.username / app.password       # test credentials
app.screens.names=Home,IP catalog # comma-separated labels for multi-screen test
app.screens.urls=http://...,http://... # matching URLs (used in Full app flow test)
browser.headless=false            # set true for CI
performance.runs=3                # repeats for multi-run benchmark
```

---

## Legacy Java / Maven Commands

```bash
# Build (compile only)
mvn compile

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=HelixPagePerformanceTest

# Run a single test method
mvn test -Dtest=HelixPagePerformanceTest#testIpCatalogPerformance

# Run the Page Load Analyzer against arbitrary URLs
mvn test -Dtest=PageLoadAnalyzerTest "-Dtarget.url=https://example.com"
mvn test -Dtest=PageLoadAnalyzerTest "-Dtarget.url=https://a.com,https://b.com"

# Run the interactive CLI analyzer (not a test — fires up a prompt)
mvn exec:java

# Generate Allure HTML report from last test run
mvn allure:report

# Serve Allure report in browser (starts local web server)
mvn allure:serve
```

### Report locations after `mvn test`
| Report | Path |
|---|---|
| ExtentReports HTML | `reports/performance-report-<timestamp>.html` |
| Excel | `reports/performance-report.xlsx` / `reports/benchmark-multirun.xlsx` |
| Screenshots | `reports/screenshots/` |
| Allure raw results | `target/allure-results/` |
| Allure HTML (after `allure:report`) | `target/allure-report/index.html` |

## Configuration (Java)

Java config lives in `src/test/resources/config.properties`. Change these before running:

```properties
app.base.url=http://...           # base URL (no trailing slash)
app.login.url=http://...          # sign-in page URL
app.username / app.password       # test credentials
app.screens.names=Home,IP catalog # comma-separated labels for multi-screen test
app.screens.urls=http://...,http://... # matching URLs (used in testFullAppFlowPerformance)
browser.headless=false            # set true for CI
performance.runs=3                # repeats for testPerformanceBenchmarkMultiRun
```

Only Chrome is supported — it is required for Chrome DevTools Protocol (CDP) metrics.

## Architecture

### TypeScript source layout

```
src/
  config/ConfigReader.ts          — singleton, reads config.properties
  driver/BrowserManager.ts        — standalone Playwright browser lifecycle (agent use)
  metrics/
    ApiRequestMetric.ts           — DTO for slow API requests
    PerformanceMetrics.ts         — metrics interface + evaluateBenchmarks()
    MetricsCollector.ts           — JS injection + collect()
  pages/
    BasePage.ts                   — abstract base with Playwright wait helpers
    LoginPage.ts                  — login form page object
    AppNavigator.ts               — click-action registry (mimics user navigation)
    NavigationRouter.ts           — readiness-wait registry + dispatch
  reports/
    HtmlReportManager.ts          — custom dark-theme HTML report (module-level state)
    ExcelReportManager.ts         — exceljs 3-sheet xlsx report
  utils/ScreenshotManager.ts      — page.screenshot() wrapper
  agent/
    PageLoadAnalyzerAgent.ts      — standalone URL analyzer (no test framework dep)
    PageLoadAnalyzerRunner.ts     — interactive CLI entry point
tests/
  fixtures/
    globalSetup.ts                — HtmlReportManager.init() before suite
    globalTeardown.ts             — HtmlReportManager.flush() after suite
    performanceFixtures.ts        — router + collector Playwright fixtures
  WebPerformanceTest.spec.ts      — 3 tests: login, full flow, multi-run
  HelixPagePerformanceTest.spec.ts — Sign-in, Home, IP Catalog
  PageLoadAnalyzerTest.spec.ts    — TARGET_URL env var → PageLoadAnalyzerAgent
```

### Measurement pipeline (the "hot path")

Every test follows the same pattern:

```typescript
await collector.injectWebVitalsObserver();   // inject JS observer BEFORE navigation
await router.navigateAndPrepare(url);        // navigate + wait until page is fully ready
const metrics = await collector.collect(label); // read browser APIs
HtmlReportManager.addMetrics(name, metrics); // append to HTML report
```

`injectWebVitalsObserver()` **must** be called before navigation. LCP and CLS events fire from first paint, so the PerformanceObserver must be registered before the navigation begins. `MetricsCollector` also calls `page.addInitScript()` in its constructor so the observer is automatically active for every full-page reload.

### Two-layer navigation system

Navigation is split across two classes that work together inside `NavigationRouter.navigateAndPrepare()`:

- **`AppNavigator`** — performs *click-based* navigation (mimics a real user: opens Catalogs dropdown → clicks IP catalog). It has a registry of `Map<path, async (page) => void>` for known pages. If no click action is registered for a URL, it falls back to `page.goto()`.
- **`NavigationRouter`** — handles *page readiness*. After clicking (or after `page.goto()`), it dispatches to a handler registered for the URL hash path (e.g. `"ip/catalog"`) that waits for the specific DOM condition that confirms the page is fully loaded (heading visible, AG-Grid spinner gone, etc.).

To add support for a new screen you typically need an entry in **both** classes:
- `AppNavigator.registerActions()` — the click sequence to reach the page
- `NavigationRouter.registerBuiltinHandlers()` — the wait condition confirming it is ready

You can also register them at test level:
```typescript
router.registerClickAction('my-screen', async (page) => { /* clicks */ });
router.register('my-screen', async (page) => { /* waits */ });
```

### AG-Grid load time

`NavigationRouter` injects a JavaScript `MutationObserver` (`window.__agGridSpinnerTracker`) at the start of every dispatch, before any readiness waits run. This records the exact milliseconds the AG-Grid "Loading" spinner was visible. The value is exposed via `router.getLastAgGridLoadTime()` and stored in `PerformanceMetrics.agGridLoadTime`.

### Metrics data model

`PerformanceMetrics` is a plain POJO. `MetricsCollector.collect()` populates it in four steps:
1. Navigation Timing API (TTFB, DNS, TCP, page load, DCL, DOM interactive)
2. Web Vitals from the pre-injected `window.__webVitals` observer (LCP, CLS, FID, INP) plus Performance API for FCP
3. Resource count and transfer size from `performance.getEntriesByType('resource')`
4. Slow API requests (XHR/fetch entries ≥ 1000 ms)

`metrics.evaluateBenchmarks()` applies Google's thresholds (GOOD / NEEDS IMPROVEMENT / POOR) and sets `overallStatus`.

### Test files

| File | Purpose |
|---|---|
| `tests/fixtures/globalSetup.ts` | Suite lifecycle: `HtmlReportManager.init()` |
| `tests/fixtures/globalTeardown.ts` | Suite lifecycle: `HtmlReportManager.flush()` |
| `tests/fixtures/performanceFixtures.ts` | Playwright fixtures: `router`, `collector` |
| `tests/WebPerformanceTest.spec.ts` | Full benchmark suite: login, full flow, multi-run |
| `tests/HelixPagePerformanceTest.spec.ts` | Targeted: Sign-in, Home, IP Catalog |
| `tests/PageLoadAnalyzerTest.spec.ts` | Arbitrary URLs via `TARGET_URL` env var |

### PageLoadAnalyzerAgent

A standalone agent (`src/agent/`) that does not depend on Playwright Test fixtures. It manages its own `BrowserManager` lifecycle and produces per-URL HTML reports plus a combined Excel report. Used by `PageLoadAnalyzerTest.spec.ts` and by `PageLoadAnalyzerRunner.ts` (the `npm run analyze` CLI entry point).

### Reporting

- **`HtmlReportManager`** (`src/reports/`) — custom dark-theme HTML writer. `init()` is called once per suite; `addMetrics()` appends a section; `flush()` writes the file. Uses module-level state (equivalent to Java static fields).
- **`ExcelReportManager`** (`src/reports/`) — writes an `.xlsx` file via `exceljs`. 3 sheets: Performance Report, Benchmark Reference, Slow API Requests. Mirrors the Java Apache POI implementation.
- **Playwright built-in HTML** — `playwright-report/index.html` generated automatically after every run.
