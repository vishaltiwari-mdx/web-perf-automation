/**
 * ################################################################################
 * # Copyright (c) 2010-2021 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class CustomPage extends BasePage {
    private WebDriver driver;

    public CustomPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class, 'app__content')]//h1")
    private WebElement customPageTitle;

    @FindBy(xpath = "//div[contains(@class, 'app__content')]//p")
    private WebElement customPageContent;

    @FindBy(xpath = "//div[@class='card-body']")
    private List<WebElement> textCards;

    @FindBy(xpath = "//div[contains(@class, 'page-name')]")
    private WebElement pageHeader;

    private String menuButton = "//li[@class='list-group-item'][text()='{name}']";
    private String tab = "//div[text()='{tabName}'][contains(@class, 'tab')]";
    private String expandButton = "//button[@title='{text}']";

    @Step("Get custom page header...")
    public String getCustomPageTitle() {
        return getText(customPageTitle);
    }

    @Step("Get custom page content...")
    public String getCustomPageContent() {
        return getText(customPageContent);
    }

    @Step("Get number of text cards...")
    public int getNumberOfTextCards() {
        return getNumberOfElements(textCards);
    }

    @Step("Verify button is displayed...")
    public boolean isButtonPresent(String name) {
        return isElementVisible(driver.findElement(By.xpath(menuButton.replace("{name}", name))));
    }

    @Step("Get page header...")
    public String getPageHeader() {
        return getText(pageHeader);
    }

    @Step("Verify tab is displayed...")
    public boolean isTabPresent(String tabName) {
        return isElementVisible(findElementWithWait(By.xpath(tab.replace("{tabName}", tabName))));
    }

    @Step("Verify Expand/Collapse button is displayed...")
    public boolean isTreeButtonPresent(String title) {
        return isElementVisible(findElementWithWait(By.xpath(expandButton.replace("{text}", title))));
    }
}
