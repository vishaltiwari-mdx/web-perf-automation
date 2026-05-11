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
package com.methodics.phi.pageobject.ip_catalog;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpCatalogTablePage extends BasePage {
    private WebDriver driver;

    public IpCatalogTablePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@row-index='0']//*[contains(@class, 'ip-catalog__ip-name__link')]")
    private WebElement firstRowIpCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='library']")
    private WebElement firstRowLibCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='dm_type']")
    private WebElement firstRowDmTypeCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='description']")
    private WebElement firstRowDescriptionCell;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-ascending-icon']")
    private WebElement sortAscendingIcon;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-descending-icon']")
    private WebElement sortDescendingIcon;

    @FindBy(xpath = "//span[contains(@class, 'icon ag-sort')]")
    private List<WebElement> sortingIcons;

    @FindBy(xpath = "//div[@class='ag-filter-body']//input[@aria-label='Filter Value']")
    private WebElement filterInputField;

    @FindBy(xpath = "//i[contains(@class,'icon-menu')]")
    private WebElement hamburgerMenu;

    @FindBy(xpath = "//div[contains(@class,'menu-header')]//span[contains(@class,'icon-menu')]")
    private WebElement hamburgerMenuButton;

    @FindBy(xpath = "//span[@aria-label='columns']")
    private WebElement columnsMenu;

    @FindBy(xpath = "//span[text()='Pin Column']")
    private WebElement pinColumnMenu;

    @FindBy(xpath = "//span[text()='Pin Right']")
    private WebElement pinRightButton;

    @FindBy(xpath = "//span[text()='Reset Columns']")
    private WebElement resetColumnsButton;

    @FindBy(xpath = "//span[text()='Tool panel']")
    private WebElement toolPanel;

    @FindBy(xpath = "//div[@role='columnheader'][@aria-colindex='1']")
    private WebElement firstTableColumn;

    @FindBy(xpath = "//div[@class='ag-picker-field-display']")
    private WebElement filterOptions;

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

    @FindBy(xpath = "//div[contains(@class, 'modal-body')]//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScrollBom;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[contains(@class, 'modal-body')]//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCornerBom;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(xpath = "//div[contains(@class, 'modal-body')]//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCornerBom;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCorner;

    @FindBy(xpath = "//span[@aria-label = 'filter']")
    private WebElement filterIcon;

    @FindBy(xpath = "//div[@class='ag-column-select-header']//input[@aria-label='Filter Columns Input']")
    private WebElement columnSearchField;

    private String columnHeader = "//div[@role='columnheader'][@col-id='{name}' or @col-id='ip_properties.{name}']//span";
    private String columnHeaders = "//div[@role='columnheader']";
    private String bomColumnHeaders = "//div[contains(@class, 'modal-body')]//div[@class='ag-header-row ag-header-row-column']/div[@role='columnheader']";
    private String firstRowProperty = "//div[@row-index='0']/div[@col-id='{propName}']";
    private String pinnedRightColumn = "//div[@class='ag-pinned-right-header']//div[@col-id='{name}']";
    private String secondaryFilterIcon = "//div[@col-id='{name}']//i[contains(@class, 'filter')]";
    private String resizeColumnButton = "//div[@col-id='{name}']/div[contains(@class, 'resize')]";
    private String columnCheckboxUnchecked = "//span[normalize-space()='{name}']/parent::div//div[contains(@class, 'checkbox-input')]";
    private String filterOption = "//span[normalize-space()='{option}']";
    private String filterCheckBox = "//div[normalize-space()='{value}']//input[@type='checkbox']";
    private String xpathTooltip = "//*[@data-testid='ui-tooltip-text-content' and normalize-space(text())='{tooltip}']";
    private String firstRowGeos = "//div[@row-index='0']/div[@col-id='{geosType}']";
    private String gpsIconColor = "//div[@col-id='{geoType}']//i[@class='fa-solid fa-location-dot fa-fw ip-catalog__grid-geo_{geoName}']";
    private String geosExpandButton = "//div[@col-id='{geoType}']//span[contains(@class, 'expand-button')][normalize-space()='+{numberOfHiddenGeos}']";
    private String getGeosCell = "//span[@title='{ipName}']/ancestor::div/div[contains(@col-id, '{geoType}')]";
    private String numberOfVisibleGeos = "//div[@col-id='{geoType}']//span[contains(@class, 'badge')]/span[@class='text-truncate']";
    private String geosCollapseButton = "//div[@col-id='{geoType}']//span[contains(@class,'collapse-button')]//i[contains(@class, 'up')]";
    private String visibleGeos = "//span[@title='{ipName}']/ancestor::div/div[contains(@col-id, '{geoType}')]//div[@title='{geoName}']";

    @Step("Get the number of the geos visible...")
    public int getNumberOfVisibleGeos(String geoType) {
        waitForPageLoaded();
        waitForSpinnersToDisappear(Duration.ofSeconds(5));
        List<WebElement> allGeos = DriverFactory.getBrowserInstance()
                .findElements(By.xpath(numberOfVisibleGeos.replace("{geoType}", geoType)));
        long visibleCount = allGeos.stream().filter(WebElement::isDisplayed).count();

        logger.info("Number of visible geos: " + visibleCount);
        return (int) visibleCount;
    }

    @Step("Click on Geos expand button...")
    public void clickOnGeosExpandButton(String geoType, String numberOfHiddenGeos) {
        DriverFactory.sleep(1000);
        WebElement expandButton = findElementWithWait(By.xpath(geosExpandButton.replace("{geoType}", geoType).replace("{numberOfHiddenGeos}", numberOfHiddenGeos)));
        click(expandButton);
    }

    @Step("Click on Geos collapse button...")
    public void clickOnGeosCollapseButton(String geoType) {
        DriverFactory.sleep(1000);
        WebElement collapseButton = findElementWithWait(By.xpath(geosCollapseButton.replace("{geoType}", geoType)));
        click(collapseButton);
    }

    @Step("Verify geos are displayed...")
    public int isGeoPresent(String ipName, String geoType, String geoName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(visibleGeos.replace("{ipName}", ipName).replace("{geoType}", geoType).replace("{geoName}", geoName)), counter).size();
    }

    @Step("Verify column is displayed...")
    public boolean isColumnHeaderPresent(String name) {
        WebElement column = waitForElementToBePresent(By.xpath(columnHeader.replace("{name}", name)));
        scrollToElement(column);
        return isElementVisible(column);
    }

    @Step("Verify column is not displayed...")
    public int columnNotDisplayed(String name, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(columnHeader.replace("{name}", name)), counter).size();
    }

    @Step("Click on column to sort...")
    public void clickOnColumnToSort(String columnName) {
        WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", columnName)));
        click(column);
    }

    @Step("Reorder columns...")
    public void reorderColumns(String columnName, String targetColumn) {
        WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", columnName)));
        WebElement target = findElementWithWait(By.xpath(columnHeader.replace("{name}", targetColumn)));
        dragAndDropElement(column, target);
    }

    @Step("Get number of columns...")
    public int getNumberOfColumns(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(columnHeaders), counter).size();
    }

    @Step("Get number of BOM columns...")
    public int getNumberOfBomColumns(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(bomColumnHeaders), counter).size();
    }

    @Step("Get geos cell...")
    public WebElement getGeosCell(String ipName, String geoType) {
        return findElementWithWait(By.xpath(getGeosCell.replace("{ipName}", ipName).replace("{geoType}", geoType)));
    }

    @Step("Get IP name from the first row...")
    public String getIpFromFirstTableRow() {
        return getText(firstRowIpCell);
    }

    @Step("Verify geo icon color...")
    public String getGpsIconColorDisplayed(String geoType, String geoName) {
        WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsIconColor.replace("{geoType}", geoType).replace("{geoName}", geoName)));
        return gpsColor.getAttribute("data-color");
    }

    @Step("Get Library from the first row...")
    public String getLibraryFromFirstTableRow() {
        return getText(firstRowLibCell);
    }

    @Step("Get Dm Type from the first row...")
    public String getDmTypeFromFirstTableRow() {
        return waitForElementToBeVisible(firstRowDmTypeCell).getText();
    }

    @Step("Get description from the first row of the table...")
    public String getDescriptionFromFirstTableRow() {
        return getText(firstRowDescriptionCell);
    }

    @Step("Get geos from the first row of the table...")
    public String getGeosFromFirstTableRow(String geoType) {
        WebElement geoFilters = findElementWithFluentWait(By.xpath(firstRowGeos.replace("{geosType}", geoType)));
        return waitTillVisibleWithFluentWait(geoFilters).getText();
    }

    @Step("Get the name of the first column...")
    public String getFirstColumnName() {
        return getText(firstTableColumn);
    }

    @Step("Get property value from the first row of the table...")
    public String getPropValueFromFirstTableRow(String propName) {
        WebElement propValue = findElementWithWait(By.xpath(firstRowProperty.replace("{propName}", propName)));
        return getText(propValue);
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

    @Step("Click on columns menu button...")
    public void openColumnsMenu() {
        click(columnsMenu);
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

    @Step("Verify column is pinned to the right...")
    public boolean isColumnPinnedRight(String columnName) {
        WebElement pinnedColumn = findElementWithWait(By.xpath(pinnedRightColumn.replace("{name}", columnName)));
        return isElementVisible(pinnedColumn);
    }

    @Step("Verify no pinned columns are displayed...")
    public boolean noPinnedColumnsPresent(String columnName) {
        List<WebElement> pinnedColumns = driver.findElements(By.xpath(pinnedRightColumn.replace("{name}", columnName)));
        return getNumberOfVisibleElements(pinnedColumns) == 0;
    }

//	@Step("Click on Reset columns button...")
//	public void resetColumnsState() {
//		openHamburgerMenu();
//		click(resetColumnsButton);
//	}

    @Step("Click on column filter icon...")
    public void clickOnFilterIcon(String column) {
        hoverOverColumnName(column);
        WebElement filterIcon = findElementWithFluentWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        clickWithJS(filterIcon);
    }

    @Step("Verify secondary filter icon is not present...")
    public int getNumberOfFilterIcons(String column) {
        hoverOverColumnName(column);
        List<WebElement> filterIcons = driver.findElements(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        return getNumberOfVisibleElements(filterIcons);
    }

    @Step("Type text into filter input field...")
    public void typeTextIntoFilterInputField(String text) {
        inputText(filterInputField, text);
    }

    @Step("Remove text from filter input field...")
    public void clearFilterInputField() {
        clearInputField(filterInputField);
    }

    @Step("Get text from the secondary input field...")
    public String getSecondaryFilterText() {
        return getAttribute(filterInputField);
    }

    @Step("Resize column...")
    public void resizeColumn(String columnName, String targetColumn) {
        WebElement column = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", columnName)));
        WebElement targetElement = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", targetColumn)));
        dragAndDropElement(column, targetElement);
        DriverFactory.sleep(2000);
    }

    @Step("Verify resize button is displayed...")
    public boolean isResizeButtonDisplayed(String name) {
        // we need to keep this sleep - the reason is - we don't hide the element when we resize the column,
        // because of this - only findElements method will return false since element is out of the boundaries
        DriverFactory.sleep(1000);
        WebElement resizeButton = driver.findElement(By.xpath(resizeColumnButton.replace("{name}", name)));
        return isElementVisible(resizeButton);
    }

    @Step("Check column checkbox...")
    public void addColumn(String columnName) {
        WebElement columnCheckbox = findElementWithWait(By.xpath(columnCheckboxUnchecked.replace("{name}", columnName)));
        click(columnCheckbox);
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

    @Step("Checking the filtering box...")
    public void checkFilteringBox(String value) {
        WebElement checkbox = waitForElementToBePresent(By.xpath(filterCheckBox.replace("{value}", value)));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkbox);
    }

    @Step("Opening filter menu and checking the box...")
    public void openFilterMenuAndCheckTheBox(String column, String value) {
        clickOnFilterIcon(column);
        checkFilteringBox(value);
    }

    @Step("Scroll horizontally to the right...")
    public void scrollRightBom() {
        dragAndDropElement(horizontalScrollBom, scrollRightCornerBom);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeftBom() {
        DriverFactory.sleep(1000);
        dragAndDropElement(horizontalScrollBom, scrollLeftCornerBom);
    }

    @Step("Scroll horizontally to the right...")
    public void scrollRight() {
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeft() {
        dragAndDropElement(horizontalScroll, scrollLeftCorner);
    }
}
