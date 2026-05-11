/**
 * ################################################################################
 * # Copyright (c) 2010-2023 Methodics, Inc.
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

public class IpLineInfoTab extends BasePage {
    private WebDriver driver;

    public IpLineInfoTab(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[normalize-space()='Source IPV']/following-sibling::*[normalize-space()='No source IPV.']")
    private WebElement noSourceMessage;

    @FindBy(xpath = "//div[normalize-space()='Source IPV']/following::span[contains(@class, 'ipl-info-card__formatted-line-source')]")
    private WebElement lineSourceValue;

    @FindBy(xpath = "//i[contains(@class,'line-source-icon')]")
    private WebElement sourceLink;

    @FindBy(css = "div#iplInfoCard i[class*='message-with-icon__info']")
    private WebElement questionIcon;

    @FindBy(xpath = "//div[normalize-space()='Description']/following-sibling::*")
    private WebElement description;

    @FindBy(xpath = "//div[normalize-space()='Owners']/following-sibling::div//i[contains(@class, 'user ')]/parent::div")
    private WebElement userOwners;

    @FindBy(xpath = "//div[normalize-space()='Owners']/following-sibling::div//i[contains(@class, 'users ')]/parent::div")
    private WebElement groupOwners;

    private String xpathLineName = "//h5[@title='Line - {infoTabCardName}'][contains(@class, 'text-truncate')]";
    private String xpathHintIcon = "//div[contains(@class,'ip-details-info-tab-card')]//h5[@title='Line - {lineName}']//ancestor::div[@data-testid='ui-card-header']//div[@data-testid='ui-tooltip-trigger']//i[contains(@class, 'hint-icon-regular')]";

    @Step("Verify line name correct...")
    public boolean isLineNameVisible(String infoTabCardName) {
        WebElement lineName = findElementWithWait(By.xpath(xpathLineName.replace("{infoTabCardName}", infoTabCardName)));
        return isElementVisible(lineName);
    }

    @Step("Get Line name tooltip...")
    public String getLineNameTooltip(String infoTabCardName) {
        WebElement lineName = findElementWithWait(By.xpath(xpathLineName.replace("{infoTabCardName}", infoTabCardName)));
        return getAttributeValue(lineName, "title");
    }

    @Step("Hover over Version card hint icon...")
    public void hoverOverHintIcon(String lineName) {
        WebElement hintIcon = findElementWithWait(By.xpath(xpathHintIcon.replace("{lineName}", lineName)));
        hoverOverElement(hintIcon);
    }

    @Step("Get line source...")
    public String getLineSource() {
        return getText(lineSourceValue);
    }

    @Step("Get line source tooltip...")
    public String getLineSourceTooltip() {
        return getAttributeValue(lineSourceValue, "title");
    }

    @Step("Verify line source field is empty...")
    public boolean isLineSourceFieldEmpty() {
        return isElementVisible(noSourceMessage);
    }

    @Step("Verify line source field is displayed...")
    public boolean isLineSourceFieldVisible() {
        return isElementVisible(noSourceMessage);
    }

    @Step("Click on source link...")
    public void clickOnSourceLink() {
        click(sourceLink);
    }

    @Step("Hover over question icon...")
    public void hoverOverQuestionIcon() {
        click(questionIcon);
    }

    @Step("Get line description name...")
    public String getLineDescription() {
        return getText(description);
    }

    @Step("Get group owners...")
    public String getGroupOwners() {
        return getText(groupOwners);
    }

    @Step("Get user owners...")
    public String getUsersOwners() {
        return getText(userOwners);
    }
}
