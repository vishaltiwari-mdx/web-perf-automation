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
package com.methodics.phi.pageobject.queries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class QueriesPanelPage extends BasePage {
    private WebDriver driver;

    public QueriesPanelPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@data-testid='ui-list-group']/li")
    private List<WebElement> queries;

    @FindBy(xpath = "//input[@placeholder='Search Query']")
    private WebElement searchQueryInputField;

    @FindBy(xpath = "//i[@class='fa fa-times-circle-o']")
    private List<WebElement> clearSearchButton;

    @FindBy(xpath = "//div[contains(@class,'text-body-secondary') and contains(.,'No Queries found.')]")
    private WebElement noQueriesMessage;

    @FindBy(xpath = "//div[@class='text-center text-body-secondary' and contains(.,'No Queries in selected folder')]")
    private WebElement noQueriesInFolderMessage;

    @FindBy(xpath = "//div[@class='text-center text-body-secondary' and contains(.,'No Queries in history.')]")
    private WebElement noQueriesInHistoryMessage;

    @FindBy(xpath = "//button[contains(.,'New')]")
    private WebElement newButton;

    @FindBy(css = "i.fa-solid.fa-circle-exclamation.fa-fw.text-info")
    private WebElement queryInfoIcon;

    @FindBy(xpath = "//div[@class='tooltip-inner' and text()='This Query is not saved yet!']")
    private WebElement unsavedQueryTooltip;

    @FindBy(xpath = "//button[contains(.,'New')]/ancestor::div[contains(@class,'side-panel col side-panel')]")
    private List<WebElement> queriesPanel;

    private String xpathEditQueryButton = "//div[text()='{name}']//ancestor::li[@data-testid='ui-list-group-item']//div[@class='controls']//button";
    private String xpathQueryName = "//div[@class='col text-truncate name me-3'][text()='{name}']";
    private String xpathSelectedQueryName = "//li[contains(@class, 'list-group-item active')]";
    private String xpathHistoryIcon = "//div[normalize-space()='{name}']/parent::div[contains(@class, 'row')]//i[@class='fa-regular fa-clock fa-fw']";
    private String xpathQueryIcon = "//i[contains(@class,'fa-{icon}')]//parent::div[@data-testid='ui-col']//following-sibling::div[normalize-space()='{name}']";

    @Step("Entering query name...")
    public void enterQueryName(String name) {
        inputText(searchQueryInputField, name);
    }

    @Step("Remove query name from the search field by clicking on clear button...")
    public void removeQueryNameByClickingOnClearButton() {
        clickOnVisibleElement(clearSearchButton);
    }

    @Step("Remove query name from the search field...")
    public void removeQueryNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchQueryInputField);
    }

    @Step("Clicking on the query...")
    public void clickOnQuery(String queryName) {
        WebElement query = findElementWithWait(By.xpath(xpathQueryName.replace("{name}", queryName)));
        click(query);
    }

    @Step("Verifying query is displayed...")
    public boolean isQueryDisplayed(String queryName) {
        return isElementVisible(findElementWithWait(By.xpath(xpathQueryName.replace("{name}", queryName))));
    }

    @Step("Verifying query is not displayed...")
    public boolean isQueryNotDisplayed(String queryName) {
        return driver.findElements(By.xpath(xpathQueryName.replace("{name}", queryName))).size() == 0;
    }

    @Step("Clicking on Edit query button...")
    public void clickOnEditQueryButton(String queryName) {
        WebElement editButton = findElementWithWait(By.xpath(xpathEditQueryButton.replace("{name}", queryName)));
        click(editButton);
    }

    @Step("Verifying no queries are displayed...")
    public boolean isNoQueriesMessagePresent() {
        return isElementVisible(noQueriesMessage);
    }

    @Step("Getting the number of queries...")
    public int getNumberOfQueries() {
        return queries.size();
    }

    @Step("Verifying no queries are displayed in folder...")
    public boolean isNoQueriesInFolderMessagePresent() {
        return isElementVisible(noQueriesInFolderMessage);
    }

    @Step("Verifying History list is empty...")
    public boolean isHistoryListEmpty() {
        return isElementVisible(noQueriesInHistoryMessage);
    }

    @Step("Verifying query is selected...")
    public String getSelectedQuery() {
        return findElementWithWait(By.xpath(xpathSelectedQueryName)).getText();
    }

    @Step("Hovering query row...")
    public void hoverOverQuery(String queryName) {
        WebElement query = findElementWithWait(By.xpath(xpathQueryName.replace("{name}", queryName)));
        hoverOverElement(query);
    }

    @Step("Hovering unsaved query info icon...")
    public void hoverQueryInfoIcon() {
        hoverOverElement(queryInfoIcon);
    }

    @Step("Verifying query tooltip is displayed...")
    public boolean isUnsavedQueryTooltipDisplayed() {
        return isElementVisible(unsavedQueryTooltip);
    }

    @Step("Verifying query icon is displayed...")
    public boolean isQueryIconDisplayed(String queryName, String icon) {
        WebElement queryIcon = findElementWithWait(By.xpath(xpathQueryIcon.replace("{icon}", icon)
                .replace("{name}", queryName)));
        return isElementVisible(queryIcon);
    }

    @Step("Verifying history icon is displayed...")
    public boolean isHistoryIconDisplayed(String queryName) {
        WebElement queryIcon = findElementWithWait(By.xpath(xpathHistoryIcon.replace("{name}", queryName)));
        return isElementVisible(queryIcon);
    }

    @Step("Clicking on New button...")
    public void clickOnNewButton() {
        click(newButton);
    }

    @Step("Get query name by index...")
    public String getQueryNameByIndex(int index) {
        return getText(queries.get(index));
    }

    @Step("Verifying Queries Panel is hidden...")
    public boolean isQueriesPanelHidden() {
        return getNumberOfVisibleElements(queriesPanel) == 0;
    }

    @Step("Hovering info icon...")
    public void hoverQueryIcon() {
        hoverOverElementWithoutWait(queryInfoIcon);
    }
}
