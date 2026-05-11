/**
 * ################################################################################
 * # Copyright (c) 2010-2025 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject.ips;

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpDashboardPage extends BasePage {

    private WebDriver driver;

    public IpDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "[data-testid='dashboard-version-tab']")
    private WebElement ipvDashboardSubTab;

    @FindBy(css = "[data-testid='dashboard-version-tab'][aria-selected='true']")
    private WebElement ipvDashboardSubTabActive;

    @FindBy(css = "[data-testid='dashboard-ip-tab']")
    private WebElement ipDashboardSubTab;

    @FindBy(css = "[data-testid='dashboard-ip-tab'][aria-selected='true']")
    private WebElement ipDashboardSubTabActive;

    @FindBy(css = "[data-testid='dashboard-line-tab']")
    private WebElement lineDashboardSubTab;

    @FindBy(css = "[data-testid='dashboard-line-tab'][aria-selected='true']")
    private WebElement lineDashboardSubTabActive;

    @FindBy(css = ".nav-item .nav-link[aria-selected='true']")
    private WebElement selectedSubTab;

    private String tabCounter = "//*[@data-testid='dashboard-{tabName}-tab']//*[@data-testid='ui-badge']";

    @Step("Clicking on Dashboard Version subtab...")
    public void clickIpvDashboardSubTab() {
        isElementVisible(ipvDashboardSubTab);
        ipvDashboardSubTab.click();
    }

    @Step("Verify dashboard Version subtab is active...")
    public boolean isIpvDashboardSubTabActive() {
        return isElementVisible(ipvDashboardSubTabActive);
    }

    @Step("Clicking on Dashboard Line subtab...")
    public void clickLineDashboardSubTab() {
        isElementVisible(lineDashboardSubTab);
        lineDashboardSubTab.click();
    }

    @Step("Verify dashboard Line subtab is active...")
    public boolean isLineDashboardSubTabActive() {
        return isElementVisible(lineDashboardSubTabActive);
    }

    @Step("Clicking on Dashboard IP subtab...")
    public void clickIpDashboardSubTab() {
        isElementVisible(ipDashboardSubTab);
        ipDashboardSubTab.click();
    }

    @Step("Get active sub-tab name...")
    public String getActiveSubTab() {
        return selectedSubTab.getText();
    }

    @Step("Verify dashboard IP subtab is active...")
    public boolean isIpDashboardSubTabActive() {
        return isElementVisible(ipDashboardSubTabActive);
    }

    @Step("Get Dashboard subtab counter...")
    public String getIpDashboardSubTabCounter(String tabName) {
        WebElement xpathSubTabCounter = findElementWithWait(By.xpath(tabCounter.replace("{tabName}", tabName)));
        return xpathSubTabCounter.getText();
    }

    @Step("Verify Dashboard subtab counter not visible...")
    public boolean dashboardSubTabCounterNotDisplayed(String tabName, int counter) {
        waitForNumberOfElementsToBe(By.xpath(tabCounter.replace("{tabName}", tabName)), counter);
        return true;
    }
}
