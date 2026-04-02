package com.perforce.performance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Page Object for the Home / Dashboard screen after login.
 *
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CONFIGURE: Update the By locators below to match your app  ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class HomePage extends BasePage {

    // ─── TODO: Update these locators to match your application ───────────────
    private static final By HOME_INDICATOR     = By.cssSelector("nav, .navbar, .sidebar, .dashboard, main, #main-content, [class*='home'], [class*='dashboard']");
    private static final By USER_PROFILE       = By.cssSelector(".user-profile, .avatar, .username, [class*='user'], #user-menu");
    private static final By NAVIGATION_MENU    = By.cssSelector("nav ul li a, .nav-link, .menu-item, .sidebar-link");
    private static final By LOGOUT_LINK        = By.cssSelector("a[href*='logout'], button[class*='logout'], .logout, #logout");
    // ─────────────────────────────────────────────────────────────────────────

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return isElementPresent(HOME_INDICATOR);
    }

    public boolean isUserLoggedIn() {
        return isElementPresent(USER_PROFILE) || isElementPresent(LOGOUT_LINK);
    }

    public void clickNavItem(String linkText) {
        log.info("Clicking navigation item: {}", linkText);
        click(By.linkText(linkText));
    }

    public void clickNavItemPartial(String partialText) {
        log.info("Clicking nav item containing: {}", partialText);
        click(By.partialLinkText(partialText));
    }

    public void navigateToScreen(String cssSelector) {
        log.info("Navigating to screen via: {}", cssSelector);
        click(By.cssSelector(cssSelector));
    }
}
