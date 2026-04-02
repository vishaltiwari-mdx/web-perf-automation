package com.perforce.performance.pages;

import com.perforce.performance.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * NavigationRouter — URL-to-handler dispatcher for page navigation.
 *
 * Each page in the app has a dedicated handler that performs all required
 * waits/checks to confirm the page is FULLY ready before handing control
 * back to the performance collector.  No measurement starts until the
 * handler completes.
 *
 * Matching strategy (tries in order):
 *   1. Full hash path   e.g.  "ip/catalog", "security/signin"
 *   2. Last URL segment e.g.  "catalog",    "signin"
 *   3. Default DOM-ready wait (document.readyState === 'complete')
 *
 * Built-in handlers (locators taken from the Helix IPLM page objects):
 *   security/signin  →  signin input visible
 *   home / dashboard →  page title contains "Home" + IPs button visible
 *   ip/catalog       →  "IP catalog" heading + AG-Grid spinner gone
 *   library/catalog  →  "Library catalog" heading + spinner gone
 *   library/manage   →  "Library management" heading
 *   query/search     →  "Advanced search" heading
 *   labels/manage    →  "Labels" heading
 *   property/manage  →  "Property" heading
 *   propertysets/manage → "Property sets" heading
 *
 * Add custom handlers for any other screen:
 *
 *   router.register("ip/catalog", d -> {
 *       // override or extend built-in behaviour
 *   });
 *
 *   router.register("reports", d -> {
 *       new WebDriverWait(d, Duration.ofSeconds(20))
 *           .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".report-table")));
 *   });
 */
public class NavigationRouter {

    private static final Logger log = LoggerFactory.getLogger(NavigationRouter.class);

    // ── Locators (Helix IPLM) ─────────────────────────────────────────────────
    private static final By SIGNIN_USERNAME =
            By.xpath("//input[contains(@class,'signin-page__card__username-input')]");
    private static final By SIGNIN_FALLBACK =
            By.cssSelector("input[type='text'],input[type='email'],input[type='password'],input[name='username']");

    private static final By DASHBOARD_IPS_BUTTON =
            By.xpath("//a[contains(@class,'dashboard-ips-button')]");
    private static final By APP_HEADER =
            By.cssSelector(".app-header,.app-header__navbar,.navbar");

    // page-name heading shared across all catalog/management pages
    private static final String PAGE_NAME_XPATH =
            "//div[contains(@class,'page-name')][normalize-space()='%s']";

    // AG-Grid loading spinner (IP Catalog, Library Catalog, etc.)
    private static final By AG_LOADING =
            By.xpath("//span[contains(@class,'ag-loading-text') and normalize-space()='Loading']");

    // Generic Vue/SPA spinner
    private static final By SPA_SPINNER =
            By.cssSelector(".spinner,.loading-spinner,.ngx-spinner,[data-test='loading-spinner']");

    // ─────────────────────────────────────────────────────────────────────────

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Map<String, Consumer<WebDriver>> handlers = new HashMap<>();
    private final AppNavigator appNavigator;

    /** Duration (ms) the AG-Grid spinner was visible during the last navigation. 0 if no grid. */
    private double lastAgGridLoadTime = 0;

    public NavigationRouter(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getInstance().getPageLoadTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        this.appNavigator = new AppNavigator(driver);
        registerBuiltinHandlers();
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    /**
     * Register a custom handler for a URL path or last segment.
     *
     * The handler must perform all waits that confirm the page is ready for
     * performance measurement and must NOT navigate away.
     *
     * @param pathOrSegment  Full hash path (e.g. {@code "ip/catalog"}) or last
     *                       segment (e.g. {@code "catalog"}).  Full path is checked
     *                       first; last segment is the fallback.
     * @param handler        Consumer that runs to completion before measurement.
     */
    public void register(String pathOrSegment, Consumer<WebDriver> handler) {
        handlers.put(key(pathOrSegment), handler);
        log.debug("Registered navigation handler for: [{}]", pathOrSegment);
    }

    /**
     * Register a custom click-action for a URL path (delegated to {@link AppNavigator}).
     * Use this to override or add click sequences for screens not covered by the
     * built-in AppNavigator actions.
     *
     * @param pathOrSegment  e.g. {@code "ip/catalog"} or {@code "catalog"}
     * @param action         Consumer that performs the button clicks to reach the page
     */
    public void registerClickAction(String pathOrSegment, java.util.function.Consumer<WebDriver> action) {
        appNavigator.register(pathOrSegment, action);
    }

    /**
     * Returns the time (ms) the AG-Grid spinner was visible during the last
     * {@link #navigateAndPrepare} or {@link #waitForCurrentPage} call.
     * Returns 0 if the last page had no AG-Grid or the spinner disappeared immediately.
     */
    public double getLastAgGridLoadTime() {
        return lastAgGridLoadTime;
    }

    /**
     * Navigate to {@code url}, then execute the matching page handler.
     * Returns only after the handler (or default DOM-ready wait) completes.
     * Start performance measurement immediately after this call.
     *
     * @param url  Full URL to navigate to.
     */
    public void navigateAndPrepare(String url) {
        String hashPath = extractHashPath(url);
        String segment  = extractSegment(url);
        log.info("Navigating → [{}]  (path='{}', segment='{}')", url, hashPath, segment);

        // Prefer click-based navigation (AppNavigator) over direct URL load.
        // Falls back to driver.get() when no click action is registered
        // (e.g. first load / login page / unknown screens).
        boolean clicked = appNavigator.navigateTo(hashPath, segment);
        if (!clicked) {
            log.info("No click action registered for '{}' — using direct URL navigation", hashPath.isEmpty() ? segment : hashPath);
            driver.get(url);
        }

        dispatch(hashPath, segment, url);
    }

    /**
     * Without navigating, wait for the page the browser is currently on.
     * Use this after a programmatic redirect (e.g. post-login).
     * Internally polls until the URL stabilises before dispatching.
     */
    public void waitForCurrentPage() {
        String url      = waitForUrlStable();
        String hashPath = extractHashPath(url);
        String segment  = extractSegment(url);
        log.info("Waiting for current page  (path='{}', segment='{}', url={})", hashPath, segment, url);
        dispatch(hashPath, segment, url);
    }

    // ─── Built-in handlers ────────────────────────────────────────────────────

    private void registerBuiltinHandlers() {

        // ── Sign-in / Login ────────────────────────────────────────────────
        Consumer<WebDriver> signinHandler = d -> {
            log.info("[handler] signin — waiting for login form");
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(SIGNIN_USERNAME));
            } catch (Exception e) {
                // Fallback to generic input detection
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(SIGNIN_FALLBACK));
                } catch (Exception ex) {
                    log.warn("[handler] signin — form not detected, falling back to DOM ready");
                    waitForDomReady();
                }
            }
            log.info("[handler] signin — ready");
        };
        handlers.put("security/signin", signinHandler);
        handlers.put("signin",          signinHandler);
        handlers.put("login",           signinHandler);

        // ── Home / Dashboard ───────────────────────────────────────────────
        Consumer<WebDriver> homeHandler = d -> {
            log.info("[handler] home — waiting for dashboard");
            // Wait for page title to contain "Home"
            try {
                wait.until(ExpectedConditions.titleContains("Home"));
            } catch (Exception ignored) { }
            // Also wait for the IPs button or app header as a fallback
            try {
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(DASHBOARD_IPS_BUTTON),
                        ExpectedConditions.visibilityOfElementLocated(APP_HEADER)
                ));
            } catch (Exception e) {
                log.warn("[handler] home — header element not found, using DOM ready");
                waitForDomReady();
            }
            waitForSpinnersToDisappear();
            log.info("[handler] home — ready");
        };
        handlers.put("home",      homeHandler);
        handlers.put("dashboard", homeHandler);

        // ── IP Catalog ─────────────────────────────────────────────────────
        handlers.put("ip/catalog", d -> {
            log.info("[handler] ip/catalog — waiting for IP catalog heading");
            waitForPageName("IP catalog");
            waitForAgGridSpinner();
            log.info("[handler] ip/catalog — ready");
        });

        // ── Library Catalog ────────────────────────────────────────────────
        handlers.put("library/catalog", d -> {
            log.info("[handler] library/catalog — waiting for Library catalog heading");
            waitForPageName("Library catalog");
            waitForAgGridSpinner();
            log.info("[handler] library/catalog — ready");
        });

        // ── Library Management ─────────────────────────────────────────────
        handlers.put("library/manage", d -> {
            log.info("[handler] library/manage");
            waitForPageNameContains("Library");
            waitForSpinnersToDisappear();
            log.info("[handler] library/manage — ready");
        });

        // ── Advanced Search ────────────────────────────────────────────────
        Consumer<WebDriver> searchHandler = d -> {
            log.info("[handler] advanced search");
            waitForPageName("Advanced search");
            log.info("[handler] advanced search — ready");
        };
        handlers.put("query/search", searchHandler);
        handlers.put("search",       searchHandler);

        // ── Labels Management ──────────────────────────────────────────────
        handlers.put("labels/manage", d -> {
            log.info("[handler] labels/manage");
            waitForPageNameContains("Labels");
            waitForSpinnersToDisappear();
            log.info("[handler] labels/manage — ready");
        });

        // ── Property Management ────────────────────────────────────────────
        handlers.put("property/manage", d -> {
            log.info("[handler] property/manage");
            waitForPageNameContains("Property");
            waitForSpinnersToDisappear();
            log.info("[handler] property/manage — ready");
        });

        // ── Property Sets Management ───────────────────────────────────────
        handlers.put("propertysets/manage", d -> {
            log.info("[handler] propertysets/manage");
            waitForPageNameContains("Property sets");
            waitForSpinnersToDisappear();
            log.info("[handler] propertysets/manage — ready");
        });

        // ── Create IP ──────────────────────────────────────────────────────
        handlers.put("ip/create", d -> {
            log.info("[handler] ip/create");
            waitForPageNameContains("Create IP");
            log.info("[handler] ip/create — ready");
        });
    }

    // ─── Dispatch ─────────────────────────────────────────────────────────────

    private void dispatch(String hashPath, String segment, String url) {
        lastAgGridLoadTime = 0; // reset for each navigation

        // 1. Try full hash path  (e.g. "ip/catalog")
        Consumer<WebDriver> handler = handlers.get(key(hashPath));

        // 2. Fall back to last segment  (e.g. "catalog")
        if (handler == null && !segment.equals(hashPath)) {
            handler = handlers.get(key(segment));
        }

        if (handler != null) {
            log.info("Executing handler for [{}]", hashPath.isEmpty() ? segment : hashPath);
            try {
                handler.accept(driver);
            } catch (Exception e) {
                log.warn("Handler threw an exception — falling back to DOM-ready wait: {}", e.getMessage());
                waitForDomReady();
            }
        } else {
            log.info("No handler for path='{}' segment='{}' — using DOM-ready wait", hashPath, segment);
            waitForDomReady();
        }

        log.info("Page confirmed ready → [{}]", url);
    }

    // ─── Wait helpers ─────────────────────────────────────────────────────────

    /** Waits for document.readyState === 'complete' then a Vue/JS settle pause. */
    private void waitForDomReady() {
        try {
            wait.until(d -> {
                try {
                    return "complete".equals(
                            ((JavascriptExecutor) d).executeScript("return document.readyState"));
                } catch (Exception e) {
                    return true;
                }
            });
        } catch (Exception ignored) { }

        // Let Vue / Angular finish any pending renders
        try {
            wait.until(d -> {
                try {
                    Object jqActive = ((JavascriptExecutor) d)
                            .executeScript("return (typeof jQuery !== 'undefined') ? jQuery.active : 0");
                    return jqActive != null && ((Number) jqActive).intValue() == 0;
                } catch (Exception e) {
                    return true;
                }
            });
        } catch (Exception ignored) { }
    }

    /** Waits for the Helix IPLM page-name heading with exact text. */
    private void waitForPageName(String name) {
        By locator = By.xpath(String.format(PAGE_NAME_XPATH, name));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Page-name '{}' not found — falling back to DOM ready", name);
            waitForDomReady();
        }
    }

    /** Waits for a page-name heading that *contains* the given text. */
    private void waitForPageNameContains(String partialName) {
        By locator = By.xpath(
                "//div[contains(@class,'page-name')][contains(normalize-space(),'" + partialName + "')]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Page-name containing '{}' not found — falling back to DOM ready", partialName);
            waitForDomReady();
        }
    }

    /**
     * Waits for the AG-Grid loading text to disappear and records the elapsed time
     * in {@link #lastAgGridLoadTime}.  Time is measured from when the spinner is
     * first detected (or when we start polling) until it is gone.
     */
    private void waitForAgGridSpinner() {
        lastAgGridLoadTime = 0;
        long start = System.currentTimeMillis();
        try {
            FluentWait<WebDriver> fluent = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(15))
                    .pollingEvery(Duration.ofMillis(100))
                    .ignoring(Exception.class);

            // Only measure if the spinner is actually present at this moment
            boolean spinnerPresent = !driver.findElements(AG_LOADING).isEmpty();
            if (spinnerPresent) {
                start = System.currentTimeMillis();
                fluent.until(ExpectedConditions.numberOfElementsToBe(AG_LOADING, 0));
                lastAgGridLoadTime = System.currentTimeMillis() - start;
                log.info("AG-Grid spinner was visible for {} ms", lastAgGridLoadTime);
            } else {
                // Spinner may appear shortly after — poll for up to 2 s before giving up
                long deadline = System.currentTimeMillis() + 2000;
                while (System.currentTimeMillis() < deadline) {
                    if (!driver.findElements(AG_LOADING).isEmpty()) {
                        start = System.currentTimeMillis();
                        fluent.until(ExpectedConditions.numberOfElementsToBe(AG_LOADING, 0));
                        lastAgGridLoadTime = System.currentTimeMillis() - start;
                        log.info("AG-Grid spinner was visible for {} ms (delayed appearance)", lastAgGridLoadTime);
                        return;
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                }
                log.debug("AG-Grid spinner not detected on this page (lastAgGridLoadTime=0)");
            }
        } catch (Exception ignored) {
            lastAgGridLoadTime = System.currentTimeMillis() - start;
        }
    }

    /** Waits for any SPA loading spinner to disappear. */
    private void waitForSpinnersToDisappear() {
        try {
            FluentWait<WebDriver> fluent = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(15))
                    .pollingEvery(Duration.ofMillis(300))
                    .ignoring(Exception.class);
            fluent.until(ExpectedConditions.numberOfElementsToBe(SPA_SPINNER, 0));
        } catch (Exception ignored) { }
    }

    /** Polls until URL stops changing (post-redirect stabilisation). */
    private String waitForUrlStable() {
        String prev = driver.getCurrentUrl();
        for (int i = 0; i < 30; i++) {          // up to 6 seconds
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            String current = driver.getCurrentUrl();
            if (current.equals(prev)) return current;
            prev = current;
        }
        return driver.getCurrentUrl();
    }

    // ─── URL parsing ──────────────────────────────────────────────────────────

    /**
     * Returns the path after the {@code #/} fragment, stripping leading slash.
     *
     * {@code http://host/#/ip/catalog?foo=bar  →  "ip/catalog"}
     * {@code http://host/#/security/signin      →  "security/signin"}
     * {@code http://host/#/home                 →  "home"}
     * {@code http://host/app/reports            →  "app/reports"}
     */
    public static String extractHashPath(String url) {
        if (url == null || url.trim().isEmpty()) return "";
        try {
            int hashIdx = url.indexOf('#');
            String path = hashIdx >= 0 ? url.substring(hashIdx + 1) : new java.net.URI(url).getPath();
            if (path == null) path = "";
            // strip leading slash, query string, trailing slash
            path = path.replaceAll("^/+", "").split("\\?")[0].replaceAll("/+$", "");
            return path.toLowerCase().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns only the last non-empty segment of the URL path/hash.
     *
     * {@code http://host/#/ip/catalog  →  "catalog"}
     * {@code http://host/#/home        →  "home"}
     */
    public static String extractSegment(String url) {
        String path = extractHashPath(url);
        if (path.isEmpty()) return "";
        String[] parts = path.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            String s = parts[i].trim();
            if (!s.isEmpty()) return s;
        }
        return "";
    }

    private static String key(String s) {
        return s == null ? "" : s.toLowerCase().trim();
    }
}