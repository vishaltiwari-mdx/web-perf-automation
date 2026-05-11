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

public class AdvancedSearchTablePage extends BasePage {
    private WebDriver driver;

    public AdvancedSearchTablePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@role='columnheader' and @aria-colindex='1']")
    private WebElement firstColumn;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-ascending-icon']")
    private WebElement sortAscendingIcon;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-descending-icon']")
    private WebElement sortDescendingIcon;

    @FindBy(xpath = "//span[contains(@class, 'ag-sort')]")
    private List<WebElement> sortingIcons;

    @FindBy(xpath = "//span[@data-ref='eName' and text()='Reset Columns']")
    private WebElement resetColumnsButton;

    @FindBy(xpath = "//span[@data-ref='eName' and text()='Pin Column']")
    private WebElement pinColumnMenu;

    @FindBy(xpath = "//span[@aria-label='columns']")
    private WebElement manageColumnsMenu;

    @FindBy(xpath = "//span[@data-ref='eName' and text()='Pin Right']")
    private WebElement pinRightButton;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='fqn']")
    private WebElement firstRowIpvCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='dm_type']")
    private WebElement firstRowDmTypeCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='creator']")
    private WebElement firstRowCreatorCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='creation_timestamp']")
    private WebElement firstRowDateCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='version_message']")
    private WebElement firstRowVersionCell;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCorner;

    @FindBy(xpath = "//div[@class='ag-picker-field-display']")
    private WebElement filterOptions;

    @FindBy(xpath = "//div[@class='ag-filter-body']//input")
    private WebElement filterInputField;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-from')]")
    private WebElement filterFrom;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-to')]")
    private WebElement filterTo;

    @FindBy(xpath = "//div[contains(@class,'vue-daterange-picker')]")
    private WebElement datepicker;

    @FindBy(xpath = "//td[contains(@class,'today')]")
    private WebElement todayDate;

    @FindBy(xpath = "//td[contains(@class,'today')]/following::td")
    private WebElement calendarDayAfter;

    @FindBy(xpath = "//input[@aria-label='Filter from value']")
    private WebElement stringFilterFrom;

    @FindBy(xpath = "//input[@aria-label='Filter to Value']")
    private WebElement stringFilterTo;

    private String xpathColumn = "//div[@role='columnheader']";
    private String xpathColumnHeader = "//div[@role='columnheader']//span[normalize-space()='{name}']";
    private String xpathColumnChecked = "//span[normalize-space()='{name}']/preceding-sibling::div/div[contains(@class, 'ag-checked')]";
    private String xpathColumnUnchecked = "//span[text()='{name}']/preceding-sibling::div";
    private String xpathPinnedRightColumn = "//div[@class='ag-pinned-right-header']//span[normalize-space()='{name}']";
    private String xpathFirstRowPropertyCell = "//div[@row-index='0']/div[@col-id='{propName}']";
    private String xpathResizeButton = "//div[contains(@class,'ag-header-cell') and contains(.,'{name}')]//div[@class='ag-header-cell-resize']";
    private String xpathIpv = "//div[@title='{ipv}']//span[@class='text-truncate']";
    private String ipRows = "//div[@col-id='fqn'][contains(@class, 'ag-cell-value')]//div[@title]//span[@class='text-truncate']";
    private String hamburgerMenuButton = "//div[@col-id='{name}']//i[contains(@class, 'ag-icon-menu')]";
    private String secondaryFilterIcon = "//div[@col-id='{name}']//i[contains(@class, 'filter')]";
    private String filterOption = "//span[normalize-space()='{option}']";
    private String filterCheckBox = "//div[@data-ref='eCheckbox' and normalize-space()='{value}']";
    private String xpathTooltip = "//div[@class='tooltip-inner' and text()='{tooltip}']";
    private String columnHeader = "//div[@role='columnheader'][@col-id='{name}']";
    private String loadingIcon = "//span[@class='spinner-border']";

    @Step("Verify column header {0} is displayed...")
    public boolean isColumnDisplayed(String column) {
        WebElement columnHeader = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", column)));
        scrollToElement(columnHeader);
        return isElementVisible(columnHeader);
    }

    @Step("Verify column {0} is not displayed...")
    public boolean columnNotDisplayed(String column) {
        List<WebElement> columnHeaders = driver.findElements(By.xpath(columnHeader.replace("{name}", column)));
        return columnHeaders.size() == 0;
    }

    @Step("Click on column {0} to sort...")
    public void clickOnColumnToSort(String columnHeader) {
        WebElement column = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", columnHeader)));
        scrollToElement(column);
        click(column);
    }

    @Step("Verify sort ascending icon is present...")
    public boolean isSortAscendingIconPresent() {
        return isElementVisible(sortAscendingIcon);
    }

    @Step("Verify sort desc icon is present...")
    public boolean isSortDescendingIconPresent() {
        return isElementVisible(sortDescendingIcon);
    }

    @Step("Get number of sorting icons...")
    public int getNumberOfSortingIcons() {
        return getNumberOfVisibleElements(sortingIcons);
    }

    @Step("Get IPV name from the first table row...")
    public String getIpvFromFirstRow() {
        return waitForElementToBeVisible(firstRowIpvCell).getText();
    }

    @Step("Get Dm Type from the first table row...")
    public String getDmTypeFromFirstTableRow() {
        return getText(firstRowDmTypeCell);
    }

    @Step("Get username from the first row of the table...")
    public String getUsernameFromFirstRow() {
        return getText(firstRowCreatorCell);
    }

    @Step("Get creation date from the first table row...")
    public String getCreationDateFromFirstRow() {
        return waitForElementToBeVisible(firstRowDateCell).getText();
    }

    @Step("Get version message from the first table row...")
    public String getVersionMssgFromFirstRow() {
        return waitForElementToBeVisible(firstRowVersionCell).getText();
    }

    @Step("Get {0} property value from the first table row...")
    public String getPropertyValue(String property) {
        WebElement propertyValue = findElementWithWait(By.xpath(xpathFirstRowPropertyCell.replace("{propName}", property)));
        return getText(propertyValue);
    }

    @Step("Click on hamburger menu button...")
    public void openHamburgerMenu(String columnName) {
        hoverOverColumnName(columnName);
        WebElement menuButton = findElementWithWait(By.xpath(hamburgerMenuButton.replace("{name}", columnName)));
        click(menuButton);
    }

    @Step("Click on Reset button...")
    public void resetColumnsState(String column) {
        openHamburgerMenu(column);
        click(resetColumnsButton);
    }

    @Step("Pin column to the right...")
    public void pinColumnToRight() {
        hoverOverElement(pinColumnMenu);
        click(pinRightButton);
    }

    @Step("Hover over column name...")
    public void hoverOverColumnName(String columnName) {
        WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", columnName)));
        hoverOverElement(column);
    }

    @Step("Verify tooltip is present...")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip.replace("{tooltip}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Uncheck column checkbox...")
    public boolean isColumnPinnedRight(String column) {
        WebElement pinnedRightColumn = findElementWithWait(By.xpath(xpathPinnedRightColumn.replace("{name}", column)));
        return isElementVisible(pinnedRightColumn);
    }

    @Step("Verify there are no pinned columns...")
    public boolean noPinnedColumnsPresent(String column) {
        List<WebElement> pinnedRightColumns = driver.findElements(By.xpath(xpathPinnedRightColumn.replace("{name}", column)));
        return getNumberOfVisibleElements(pinnedRightColumns) == 0;
    }

    @Step("Click on manage columns menu button...")
    public void openManageColumnsMenu() {
        click(manageColumnsMenu);
    }

    @Step("Uncheck column checkbox...")
    public void deselectColumn(String column) {
        WebElement columnCheckbox = findElementWithWait(By.xpath(xpathColumnChecked.replace("{name}", column)));
        click(columnCheckbox);
    }

    @Step("Check column checkbox...")
    public void addColumn(String column) {
        WebElement columnCheckbox = findElementWithWait(By.xpath(xpathColumnUnchecked.replace("{name}", column)));
        click(columnCheckbox);
    }

    @Step("Reorder columns...")
    public void swapColumns(String columnName, String targetColumnName) {
        WebElement column = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", columnName)));
        WebElement targetColumn = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", targetColumnName)));
        dragAndDropElement(column, targetColumn);
    }

    @Step("Get the name of the first column...")
    public String getFirstColumnName() {
        return getText(firstColumn);
    }

    @Step("Resize column width...")
    public void resizeColumn(String columnName, String targetColumn) {
        WebElement column = findElementWithWait(By.xpath(xpathResizeButton.replace("{name}", columnName)));
        WebElement targetElement = findElementWithWait(By.xpath(xpathResizeButton.replace("{name}", targetColumn)));
        dragAndDropElement(column, targetElement);
    }

    @Step("Get number of the searched Ipvs...")
    public int getNumberOfSearchedIps(int counter) {
        return findElementsWithWait(By.xpath(ipRows), counter).size();
    }

    @Step("Get number of Ipvs when do lazy loading...")
    public int getNumberOfIpsForLazyLoad() {
        String xpath = "//div[@role='row' and @row-id]";
        String xpathForFirstScroll = "(//div[@col-id='name' or @col-id='fqn'][contains(@class, 'ag-cell-value')])[8]";
        return getNumberOfRowsForLazyLoad(xpath, xpathForFirstScroll, 6);
    }

    @Step("Scroll horizontally to the right...")
    public void scrollRight() {
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeft() {
        dragAndDropElement(horizontalScroll, scrollLeftCorner);
        DriverFactory.sleep(1000);
    }

    @Step("Get number of columns when scrolling...")
    public int getNumberOfColumnsScrolling() {
        return getNumberOfColumnsHorizontalScroll(xpathColumn, horizontalScroll, scrollRightCorner, scrollLeftCorner);
    }

    @Step("Get number of columns...")
    public int getNumberOfColumns(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathColumn), counter).size();
    }

    @Step("Verifying that table does not contain record...")
    public boolean isIpvNotPresentInTable(String ipvName) {
        List<WebElement> ipvs = driver.findElements(By.xpath(xpathIpv.replace("{ipv}", ipvName)));
        return getNumberOfVisibleElements(ipvs) == 0;
    }

    @Step("Type text into filter input field...")
    public void typeTextIntoFilterInputField(String text) {
        inputText(filterInputField, text);
    }

    @Step("Click on column filter icon...")
    public void clickOnFilterIcon(String column) {
        WebElement filterIcon = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        scrollToElement(filterIcon);
        click(filterIcon);
    }

    @Step("Selecting filter option...")
    public void selectFilteringOption(String option) {
        click(filterOptions);
        WebElement value = findElementWithWait(By.xpath(filterOption.replace("{option}", option)));
        click(value);
    }

    @Step("Selecting column and filtering option...")
    public void selectColumnAndFilteringOption(String column, String option) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        selectFilteringOption(option);
    }

    @Step("Typing a text into the filter input field with selected option...")
    public void typeTextIntoSelectedOptionTextField(String column, String option, String text) {
        selectColumnAndFilteringOption(column, option);
        typeTextIntoFilterInputField(text);
        DriverFactory.sleep(1000);
    }

    @Step("Insert from and to value...")
    public void typeTextIntoFromAndToFields(String fromValue, String toValue) {
        inputText(stringFilterFrom, fromValue);
        inputText(stringFilterTo, toValue);
    }

    @Step("Select today's date...")
    public void selectToday() {
        click(todayDate);
    }

    @Step("Select the next day...")
    public void selectNextDay() {
        click(calendarDayAfter);
    }

    @Step("Click on the calendar icon...")
    public void clickOnCalendarIcon() {
        click(datepicker);
    }

    @Step("Click on the calendar icon from...")
    public void clickOnCalendarIconFrom() {
        click(filterFrom);
    }

    @Step("Click on the calendar icon to...")
    public void clickOnCalendarIconTo() {
        click(filterTo);
    }

    @Step("Remove text from filter input field...")
    public void clearFilterInputField() {
        clearInputFieldWithBackspace(filterInputField);
    }

    @Step("Checking the filtering box...")
    public void checkFilteringBox(String value) {
        WebElement checkbox = findElementWithWait(By.xpath(filterCheckBox.replace("{value}", value)));
        click(checkbox);
    }

    @Step("Opening filter menu and checking the box...")
    public void openFilterMenuAndCheckTheBox(String column, String value) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        checkFilteringBox(value);
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForLoadingSpinner() {
        findElementWithWait(By.xpath(loadingIcon));
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForDataToLoad() {
        waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0);
    }
}
