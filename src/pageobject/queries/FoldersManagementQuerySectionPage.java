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
package com.methodics.phi.pageobject.queries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class FoldersManagementQuerySectionPage extends BasePage {
    private WebDriver driver;

    public FoldersManagementQuerySectionPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class,'modal-body')]//input[@name='search']")
    private WebElement searchQueryField;

    @FindBy(xpath = "//span[text()='No Queries added yet.']")
    private WebElement noQueriesAddedMessage;

    @FindBy(xpath = "//button[contains(@class, 'add-button')]")
    private WebElement plusButton;

    @FindBy(xpath = "//button[contains(@class,'results__remove-button')]")
    private WebElement removeButton;

    @FindBy(xpath = "//button[contains(@class,'modal__done-button')]")
    private WebElement addButton;

    @FindBy(xpath = "//button[contains(@class, 'queries-modal__cancel')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//span[contains(@class,'panel-grid-results')][@data-testid='ui-badge']/span")
    private WebElement numberOfQueriesIcon;

    private String queryNameCell = "//div[@col-id='name'][contains(@class, 'ag-cell-value')]//span[@title]";
    private String xpathQuery = "//div[@col-id='name']//span[@title='{name}']";
    private String modalXpathQuery = "//div[@data-testid='query-folder-modal']//div[@col-id='name' and normalize-space()='{name}']";
    private String numberOfQueriesBadge = "//button[@data-testid='ui-btn-ui-list-group-item']//*[@data-testid='ui-badge']";
    private String gridTitle = "//div[contains(@class,'text-truncate')]//span[@title='Queries that are in the {name} folder.']";

    @Step("Verify No Queries message is present...")
    public boolean isNoQueriesAddedMessagePresent() {
        return isElementVisible(noQueriesAddedMessage);
    }

    @Step("Type query name...")
    public void enterQueryNameToSearchField(String queryName) {
        inputText(searchQueryField, queryName);
    }

    @Step("Remove query name from the search field...")
    public void removeQueryNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchQueryField);
    }

    @Step("Get number of queries in folder...")
    public int getNumberOfQueries(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(queryNameCell), counter).size();
    }

    @Step("Click on add button...")
    public void clickOnPlusButton() {
        click(plusButton);
    }

    @Step("Select query: {0}, for the method: {method}...")
    public void selectQueryToAdd(String queryName) {
        WebElement query = findElementWithWait(By.xpath(modalXpathQuery.replace("{name}", queryName)));
        click(query);
    }

    @Step("Click on Add button...")
    public void clickOnAddButton() {
        click(addButton);
    }

    @Step("Add query to folder...")
    public void addQuery(String query) {
        clickOnPlusButton();
        selectQueryToAdd(query);
        clickOnAddButton();
    }

    @Step("Remove query from folder...")
    public void removeQuery(String query) {
        //sleep added to ensure stable regression
        DriverFactory.sleep(700);
        click(driver.findElement(By.xpath(xpathQuery.replace("{name}", query))));
        waitForElementToBeClickable(removeButton);
        click(removeButton);
        DriverFactory.sleep(1000);
    }

    @Step("Get number of queries in folder from the icon...")
    public String getNumberOfQueriesFromIcon() {
        return getText(driver.findElement(By.xpath(numberOfQueriesBadge)));
    }

    @Step("Verify queries number badge is not present...")
    public boolean noQueriesNumberBadgePresent() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(numberOfQueriesBadge))) == 0;
    }

    @Step("Get number of added queries from icon...")
    public String getNumberOfAddedQueriesFromIcon() {
        return getText(numberOfQueriesIcon);
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Select multiple queries and click remove...")
    public void selectMultipleQueriesAndClickRemove(String... queries) {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.COMMAND).build().perform();
        for (String query : queries) {
            click(driver.findElement(By.xpath(xpathQuery.replace("{name}", query))));
        }
        click(removeButton);
    }

    @Step("Verify message above the grid is present...")
    public boolean isGridMessagePresent(String libName) {
        WebElement gridMessage = findElementWithWait(By.xpath(gridTitle.replace("{name}", libName)));
        return isElementVisible(gridMessage);
    }
}
