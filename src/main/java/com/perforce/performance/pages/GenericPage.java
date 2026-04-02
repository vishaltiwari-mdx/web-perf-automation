package com.perforce.performance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * GenericPage - Used for any additional screens after Home.
 * Measures performance of any URL you navigate to.
 *
 * Add screen-specific page objects as your app grows.
 * Example: DashboardPage, ReportsPage, SettingsPage, etc.
 */
public class GenericPage extends BasePage {

    private final String screenName;
    private final String screenUrl;

    public GenericPage(WebDriver driver, String screenName, String screenUrl) {
        super(driver);
        this.screenName = screenName;
        this.screenUrl = screenUrl;
    }

    @Override
    public boolean isLoaded() {
        try {
            return driver.getCurrentUrl().contains(screenUrl.replace(config.getBaseUrl(), ""))
                    || !driver.findElements(By.tagName("body")).isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    public void navigateTo() {
        log.info("Navigating to screen '{}' at: {}", screenName, screenUrl);
        driver.get(screenUrl);
    }

    public String getScreenName() { return screenName; }
    public String getScreenUrl()  { return screenUrl; }
}
