/**
 * ################################################################################
 * # Copyright (c) 2010-2022 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject.labels;

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LabelsManagementIpSectionPage extends BasePage {
    private WebDriver driver;

    public LabelsManagementIpSectionPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@id='ipsSearchInput']")
    private WebElement searchIpField;

    @FindBy(css = "[role='button-clear-ips']")
    private WebElement clearButton;

    @FindBy(xpath = "//span[text()='No IPs found.']")
    private WebElement noIpsMessage;

    @FindBy(xpath = "//div[contains(@class, 'panel-header')]/span")
    private WebElement ipsNumberBadge;

    private String tableTitle = "//div[contains(@class,'page-name')]/span[@title='{name}']";
    private String ips = "(//span[@title][contains(@class,'labels__ip_name')])";
    public static final String GRID_SCROLL_CONTAINER_SELECTOR = "[class*='ag-body-vertical-scroll-viewport']";

    @Step("Verify no IPs message is present...")
    public boolean isNoIpsFoundMessagePresent() {
        return isElementVisible(noIpsMessage);
    }

    @Step("Verify descriptive table title is present...")
    public boolean isTableTitlePresent(String label) {
        WebElement title = driver.findElement(By.xpath(tableTitle.replace("{name}", label)));
        return isElementVisible(title);
    }

    @Step("Enter IP name to the search field...")
    public void enterIpNameToSearchField(String ipName) {
        enterTextSlowly(searchIpField, ipName);
    }

    @Step("Remove IP name from the search field by clicking on clear button...")
    public void removeIpFromSearchFieldByClickingOnClearButton() {
        click(clearButton);
    }

    @Step("Remove IP name from the search field...")
    public void removeIpNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchIpField);
    }

    @Step("Verify IPs count badge is displayed...")
    public boolean isIPsNumberBadgePresent() {
        return isElementVisible(ipsNumberBadge);
    }

    @Step("Get number of IPs in the Ips count badge...")
    public String getNumberOfIpsInIpsNumberBadge() {
        return getText(ipsNumberBadge);
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForLazyLoad() {
        return getNumberOfRowsForLazyLoadUntilFoundLastRow(ips);
    }

    @Step("Verify IP is present in the list...")
    public int getNumberOfIpsOnLabelPageForLazyLoad(){
        return getNumberOfRowsForLazyLoadOnLabelPageUntilFoundLastRow(ips);
    }
}
