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

public class LineGraphTabPage extends BasePage {
    private WebDriver driver;

    public LineGraphTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@class='lines-versions-page__main']//input[@placeholder='Search IP Lines']")
    private WebElement searchLineInput;

    @FindBy(xpath = "//i[contains(@class,'base-input-field__clear-icon')]")
    private WebElement clearSearchButton;

    private String line = "//div[contains(@class,'lines-cell')]//span[@title='{name}']";

    private String selectedLine = "//span[@title='{name}' and ancestor::div[@aria-selected='true']]";
    private String lineRows = "//span[@class='ag-group-value']";
    private String arrowButtonCollapse = "//span[contains(., '{name}')]//span[@data-ref='eExpanded']";
    private String arrowButtonExpand = "//span[contains(., '{name}')]//span[@data-ref='eContracted']";

    @Step("Enter line name to the search field...")
    public void enterLineName(String lineName) {
        inputText(searchLineInput, lineName);
    }

    @Step("Clear search field...")
    public void clearSearchField() {
        clearInputFieldWithBackspace(searchLineInput);
    }

    @Step("Click on clear search button...")
    public void clickOnClearButton() {
        click(clearSearchButton);
    }

    @Step("Click on IP line {0}...")
    public void clickOnLine(String lineName) {
        click(findElementWithWait(By.xpath(line.replace("{name}", lineName))));
    }

    @Step("Verify Line is displayed...")
    public int isLinePresent(String lineName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(line.replace("{name}", lineName)), counter).size();
    }

    @Step("Verify selected bold Line is displayed...")
    public int isSelectedBoldLinePresent(String lineName, int counter) {
        waitForPageLoaded();
        return waitForNumberOfElementsToBe(By.xpath(selectedLine.replace("{name}", lineName)), counter).size();
    }

    @Step("Get the number of the line rows...")
    public int getNumberOfLineRows(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(lineRows), counter).size();
    }

    @Step("Collapse IP Lines & Versions tree node...")
    public void collapseLineTreeNode(String name) {
        WebElement arrowButton = findElementWithWait(By.xpath(arrowButtonCollapse.replace("{name}", name)));
        click(arrowButton);
    }

    @Step("Expand IP Lines & Versions tree node...")
    public void expandLineTreeNode(String name) {
        WebElement arrowButton = findElementWithWait(By.xpath(arrowButtonExpand.replace("{name}", name)));
        click(arrowButton);
    }
}
