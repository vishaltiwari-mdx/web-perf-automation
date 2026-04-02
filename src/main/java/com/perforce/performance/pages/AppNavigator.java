package com.perforce.performance.pages;

import com.perforce.performance.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * AppNavigator — Click-based page navigation for Helix IPLM.
 *
 * Every navigation method mirrors what a real user does:
 *   it clicks the correct header/dashboard button and returns only
 *   when the click action itself has completed (not when the page is
 *   fully loaded — that is handled separately by NavigationRouter's
 *   readiness handlers).
 *
 * ── Header buttons (always visible regardless of current page) ────────────
 *   Catalogs ▸ IP catalog          →  navigateTo("ip/catalog")
 *   Catalogs ▸ Library catalog     →  navigateTo("library/catalog")
 *   Advanced search                →  navigateTo("query/search")
 *   Administration ▸ Library mgmt  →  navigateTo("library/manage")
 *   Administration ▸ Labels mgmt   →  navigateTo("labels/manage")
 *   Administration ▸ Property mgmt →  navigateTo("property/manage")
 *   Administration ▸ Prop-sets     →  navigateTo("propertysets/manage")
 *   Product logo (→ Home)          →  navigateTo("home") / "dashboard"
 *
 * ── Dashboard quick-links (only available on Home page) ───────────────────
 *   IPs button                     →  navigateTo("ip/catalog")   (fallback)
 *   Libraries button               →  navigateTo("library/catalog") (fallback)
 *
 * Adding a new screen:
 *   In registerActions() add:
 *     actions.put("my-screen", d -> {
 *         clickWhenVisible(CATALOGS_BUTTON);
 *         clickWhenVisible(By.xpath("//span[normalize-space()='My screen']"));
 *     });
 */
public class AppNavigator {

    private static final Logger log = LoggerFactory.getLogger(AppNavigator.class);

    // ── Header locators (Helix IPLM) ──────────────────────────────────────────
    /** "Catalogs" top-level dropdown button */
    private static final By CATALOGS_BUTTON =
            By.cssSelector("div[data-testid='ui-dropdown-menu-catalog'] > button");

    /** "IP catalog" inside the Catalogs dropdown */
    private static final By IP_CATALOG_ITEM =
            By.xpath("//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']");

    /** "Library catalog" inside the Catalogs dropdown */
    private static final By LIBRARY_CATALOG_ITEM =
            By.xpath("//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']");

    /** "Advanced search" always-visible link in the header */
    private static final By ADVANCED_SEARCH_BUTTON =
            By.xpath("//a[@data-testid='menu-item-link']//span[text()='Advanced search']");

    /** "Administration" top-level dropdown button */
    private static final By ADMIN_BUTTON =
            By.cssSelector("div[data-testid='ui-dropdown-menu-admin'] > button");

    /** Product logo — clicking it returns to Home/Dashboard */
    private static final By PRODUCT_LOGO =
            By.cssSelector(".app-header__navbar__brand a.navbar-brand");

    // ── Dashboard quick-link locators ─────────────────────────────────────────
    /** "IPs" quick-link on the Dashboard */
    private static final By DASHBOARD_IPS_BUTTON =
            By.xpath("//a[contains(@class,'dashboard-ips-button')]");

    /** "Libraries" quick-link on the Dashboard */
    private static final By DASHBOARD_LIBRARIES_BUTTON =
            By.xpath("//a[contains(@class,'libraries-button')]");

    // ── Generic admin dropdown item pattern ───────────────────────────────────
    /** XPath for any named item inside an open dropdown. */
    private static final String DROPDOWN_ITEM_XPATH =
            "//a[normalize-space(.)='%s']";

    // ─────────────────────────────────────────────────────────────────────────

    private final WebDriver driver;
    private final WebDriverWait wait;

    /** Hash-path  →  click sequence that navigates there. */
    private final Map<String, Consumer<WebDriver>> actions = new HashMap<>();

    public AppNavigator(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigReader.getInstance().getPageLoadTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        registerActions();
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    /**
     * Register a custom click-action for a URL hash path or last segment.
     *
     * @param pathOrSegment  e.g. {@code "ip/catalog"} or {@code "catalog"}
     * @param action         Consumer that performs the button clicks to reach the page
     */
    public void register(String pathOrSegment, Consumer<WebDriver> action) {
        actions.put(key(pathOrSegment), action);
        log.debug("Registered navigation action for: [{}]", pathOrSegment);
    }

    /**
     * Perform the click-based navigation to the page identified by {@code hashPath}.
     * Falls back to {@code segment} if no action is registered for the full path.
     *
     * @return {@code true} if a click action was found and executed;
     *         {@code false} if no action is registered (caller should fall back to URL).
     */
    public boolean navigateTo(String hashPath, String segment) {
        Consumer<WebDriver> action = actions.get(key(hashPath));
        if (action == null && !segment.equals(hashPath)) {
            action = actions.get(key(segment));
        }
        if (action == null) {
            return false;
        }
        log.info("Clicking navigation action for [{}]", hashPath.isEmpty() ? segment : hashPath);
        action.accept(driver);
        return true;
    }

    // ─── Registered click actions ─────────────────────────────────────────────

    private void registerActions() {

        // ── IP Catalog ─────────────────────────────────────────────────────
        // Header: Catalogs → IP catalog
        actions.put("ip/catalog", d -> {
            log.info("[action] ip/catalog — Catalogs ▸ IP catalog");
            openCatalogsMenu();
            clickWhenVisible(IP_CATALOG_ITEM);
        });

        // ── Library Catalog ────────────────────────────────────────────────
        // Header: Catalogs → Library catalog
        actions.put("library/catalog", d -> {
            log.info("[action] library/catalog — Catalogs ▸ Library catalog");
            openCatalogsMenu();
            clickWhenVisible(LIBRARY_CATALOG_ITEM);
        });

        // ── Advanced / Query Search ────────────────────────────────────────
        // Header: Advanced search link (always visible)
        Consumer<WebDriver> searchAction = d -> {
            log.info("[action] query/search — Advanced search button");
            clickWhenVisible(ADVANCED_SEARCH_BUTTON);
        };
        actions.put("query/search", searchAction);
        actions.put("search",       searchAction);

        // ── Home / Dashboard ───────────────────────────────────────────────
        // Click the product logo to return to Home
        Consumer<WebDriver> homeAction = d -> {
            log.info("[action] home — product logo click");
            clickWhenVisible(PRODUCT_LOGO);
        };
        actions.put("home",      homeAction);
        actions.put("dashboard", homeAction);

        // ── Library Management ─────────────────────────────────────────────
        // Header: Administration → Library management
        actions.put("library/manage", d -> {
            log.info("[action] library/manage — Administration ▸ Library management");
            openAdminMenu();
            clickDropdownItem("Library management");
        });

        // ── Labels Management ──────────────────────────────────────────────
        actions.put("labels/manage", d -> {
            log.info("[action] labels/manage — Administration ▸ Labels management");
            openAdminMenu();
            clickDropdownItem("Labels management");
        });

        // ── Property Management ────────────────────────────────────────────
        actions.put("property/manage", d -> {
            log.info("[action] property/manage — Administration ▸ Property management");
            openAdminMenu();
            clickDropdownItem("Property management");
        });

        // ── Property Sets Management ───────────────────────────────────────
        actions.put("propertysets/manage", d -> {
            log.info("[action] propertysets/manage — Administration ▸ Property sets management");
            openAdminMenu();
            clickDropdownItem("Property sets management");
        });

        // ── Query Folders ──────────────────────────────────────────────────
        actions.put("queryfolder/manage", d -> {
            log.info("[action] queryfolder/manage — Administration ▸ Query folders management");
            openAdminMenu();
            clickDropdownItem("Query folders management");
        });
    }

    // ─── Click helpers ────────────────────────────────────────────────────────

    /** Opens the "Catalogs" header dropdown menu. */
    private void openCatalogsMenu() {
        clickWhenVisible(CATALOGS_BUTTON);
        // Brief pause for the dropdown animation to finish
        pause(300);
    }

    /** Opens the "Administration" header dropdown menu. */
    private void openAdminMenu() {
        clickWhenVisible(ADMIN_BUTTON);
        pause(300);
    }

    /** Clicks a named item inside an already-open dropdown. */
    private void clickDropdownItem(String itemName) {
        By locator = By.xpath(String.format(DROPDOWN_ITEM_XPATH, itemName));
        clickWhenVisible(locator);
    }

    /**
     * Waits for an element to be clickable and then clicks it.
     * Uses the configured page-load timeout.
     */
    private void clickWhenVisible(By locator) {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
            log.debug("Clicked: {}", locator);
        } catch (Exception e) {
            log.warn("Could not click [{}]: {}", locator, e.getMessage());
            throw e;
        }
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static String key(String s) {
        return s == null ? "" : s.toLowerCase().trim();
    }
}