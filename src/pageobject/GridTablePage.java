/**
 * ################################################################################
 * # Copyright (c) 2010-2024 Methodics, Inc.
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

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.libraries.LibraryDetailsPage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GridTablePage extends BasePage {
    private final WebDriver driver;

    public GridTablePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class, 'overlay-wrapper')]")
    private WebElement noResultsFound;

    @FindBy(xpath = "//div[text()='No Queries found.']")
    private WebElement noQueriesFound;

    @FindBy(xpath = "//*[text()='No results found']")
    private WebElement noPropertiesFoundMessage;

    @FindBy(xpath = "//span[text()='Copy']")
    private WebElement copyButton;

    @FindBy(xpath = "//span[text()='Copy with Headers']")
    private WebElement copyWithHeaders;

    @FindBy(xpath = "//span[contains(@class, 'ag-menu-option')][text()='Open link in new tab']")
    private WebElement openInNewTab;

    @FindBy(xpath = "//span[text()='Export']")
    private WebElement exportButton;

    @FindBy(xpath = "//span[text()='Export to CSV']")
    private WebElement csvExport;

    @FindBy(xpath = "//div[contains(@class,'ag-header-active')]//i[contains(@class,'ag-icon-filter')]")
    private WebElement filterIcon;

    @FindBy(xpath = "//div[contains(@class, 'header-active')]//i[contains(@class,'ag-icon-menu')]")
    private WebElement mainMenuHamburgerButton;

    @FindBy(xpath = "//div[contains(@class,'ag-popup-child')]//span[contains(@class,'ag-icon-menu')]")
    private WebElement hamburgerSubMenuButton;

    @FindBy(xpath = "//div[contains(@class,'ag-tabs')]//span[@class='ag-tab' and @aria-label='columns' and not(contains(@class, 'ag-tab-selected'))]")
    private WebElement columnSelectMenu;

    @FindBy(xpath = "//div[contains(@class,'ag-tabs')]//span[contains(@class,'ag-tab-selected')]")
    private WebElement columnSelectMenuOpen;

    @FindBy(xpath = "//span[text()='Pin Column']")
    private WebElement pinColumnMenu;

    @FindBy(xpath = "//span[text()='Pin Right']")
    private WebElement pinRightButton;

    @FindBy(xpath = "//span[text()='Pin Left']")
    private WebElement pinLeftButton;

    @FindBy(xpath = "//span[text()='Autosize This Column']")
    private WebElement autosizeThisColumn;

    @FindBy(xpath = "//span[text()='Autosize All Columns']")
    private WebElement autosizeAllColumns;

    @FindBy(xpath = "//span[text()='Reset Columns']")
    private WebElement resetColumnsButton;

    @FindBy(xpath = "//div[@class='ag-column-select-header']//input[@aria-label='Filter Columns Input']")
    private WebElement columnSearchField;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(xpath = "//span[contains(@class, 'ag-sort')]")
    private List<WebElement> sortingIcons;

    @FindBy(xpath = "//div[@class='ag-picker-field-display']/parent::div")
    private WebElement filterOptions;

    @FindBy(xpath = "//div[@class='ag-filter-body']//div[@role='presentation'][@aria-hidden = 'false']//input")
    private WebElement filterInputField;

    @FindBy(xpath = "//input[@aria-label='Search filter values']")
    private WebElement filteringSearchField;

    @FindBy(xpath = "//div[@class='ag-filter-no-matches']")
    private WebElement noMatchesMessage;

    @FindBy(xpath = "//span[normalize-space()='Tool panel']")
    private WebElement toolPanel;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCorner;

    @FindBy(xpath = "//input[@aria-label='Filter from value']")
    private WebElement stringFilterFrom;

    @FindBy(xpath = "//input[@aria-label='Filter to Value']")
    private WebElement stringFilterTo;

    @FindBy(xpath = "//div[@col-id='group']//span[@class='ag-group-contracted']//span[contains(@class,'ag-icon-tree-closed')]")
    private WebElement expandTreeButton;

    @FindBy(xpath = "//div[contains(@class,'ag-theme-mdx')]//div[@role='columnheader']")
    private List<WebElement> columnList;

    @FindBy(xpath = "(//input[@aria-label='Toggle All Columns Visibility'])[last()]")
    private WebElement toggleSelect;

    @FindBy(xpath = "//input[contains(@class,'base-input-field__input')]")
    private WebElement ipSearchField;

    @FindBy(xpath = "//div[contains(@class,'vue-daterange-picker')]")
    private WebElement datepicker;

    @FindBy(xpath = "//td[contains(@class,'today')]")
    private WebElement todayDate;

    @FindBy(xpath = "//td[contains(@class,'today')]/following::td")
    private WebElement calendarDayAfter;

    @FindBy(xpath = "//td[contains(@class,'today')]/preceding::td[1]")
    private WebElement yesterday;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-from')]")
    private WebElement filterFrom;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-to')]")
    private WebElement filterTo;

    @FindBy(xpath = "//div[@row-index='0']//*[contains(@class, 'ip-name-link-cell__link')]")
    private WebElement firstRowIpCell;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-ascending-icon']")
    private WebElement sortAscendingIcon;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-sort-descending-icon']")
    private WebElement sortDescendingIcon;

    @FindBy(xpath = "//span[@class='ag-icon ag-icon-filter']")
    private WebElement closeFilterIcon;

    @FindBy(xpath = "//div[@class='ag-filter']//input[@class='ag-input-field-input ag-text-field-input']")
    private WebElement booleanFilterInput;

    @FindBy(xpath = "//span[text()='Expand All Row Groups']")
    private WebElement expandAllRowGroupsLink;

    @FindBy(xpath = "//div[contains(@class,'ag-menu-header')]//span[contains(@class,'ag-icon-columns')]")
    private WebElement columnsTabInMenu;

    @FindBy(xpath = "//li[@class='column-switcher__dropdown-menu']")
    private WebElement propertySetScrollBar;

    @FindBy(xpath = "//div[@row-index='0']//div[contains(@class,'ag-cell-value ag-cell ag-cell-not-inline-editing') and @aria-colindex='1']")
    private WebElement firstCellOfGrid;

    @FindBy(xpath = "//*[contains(@class, 'spinner-border')] | //*[contains(@class, 'ag-loading')]")
    private WebElement loadingIndicator;

    @FindBy(css = ".show div.ag-theme-mdx:not([toolpanelsuppresssidebuttons]) div.ag-header-cell[role='columnheader']")
    private List<WebElement> getColumnHeader;

    @FindBy(xpath = "//div[contains(@class, 'overlay-wrapper')]//h5")
    private WebElement noRecordsFound;

    @FindBy(xpath = "//h5[contains(@class,'no-rows-overlay__text') and normalize-space()='No results found']")
    private WebElement noResultsFoundHeaderText;

    private final String tableCell = "//div[@role='gridcell']//*[@title='{value}' or @data-testid='{value}' or text()='{value}']";
    private final String tableCellWithoutLink = "//div[@role='gridcell']//*[@title='{value}']";
    private final String scrollForMoreNotice = "//div[normalize-space()='Scroll for more']";
    private final String xpathIpIcon = "//*[@title='{name}']//*[contains(@class,'{icon}')] | //*[contains(@class,'{icon}')]//following-sibling::*[@title='{name}']";
    private final String pinnedRightColumn = "//div[@class='ag-pinned-right-header']//div[@role='columnheader'][@col-id='group' or @col-id='{col}' or @col-id='ip_properties_{col}'or @col-id='fqn.{col}']";
    private final String pinnedLeftColumn = "//div[@class='ag-pinned-left-header']//div[@role='columnheader'][@col-id='{col}' or @col-id='ip_properties_{col}'or @col-id='fqn.{col}']";
    private final String columnCheckbox = "//*[@role='dialog']//*[normalize-space()='{name}'][@role='treeitem']//div[contains(@class,'ag-checkbox-input')]";
    private final String columnCheckboxSelected = "//span[contains(normalize-space(),'{name}')]/parent::div//div[contains(@class, 'checkbox-input')]";
    private final String columnHeaderByRole = "//div[contains(@class,'ag-theme-mdx')]//div[@role='columnheader']";
    private final String columnHeader = "(" + columnHeaderByRole + "[@col-id='{name}' or @col-id='ip_properties.{name}' or @col-id='ip_properties_{name}' or @col-id='fqn.{name}']/*[@data-ref='eHeaderCompWrapper'])[last()]";
    private final String columnHeaderWithTestDataID = columnHeaderByRole + "[@col-id='group' or @col-id='{name}' or @col-id='ip_properties.{name}' or @col-id='ip_properties_{name}' or @col-id='fqn.{name}']/*[@data-ref='eHeaderCompWrapper']";
    private final String firstColumnHeader = "//div[@role='columnheader'][@aria-colindex=1]";
    private final String resizeColumnButton = "//div[@col-id='{name}']//div[contains(@class, 'resize')]";
    private final String filterOption = "//div[@aria-label='Select Field']//span[normalize-space()='{option}']";
    private final String secondaryFilterIcon = "//div[@col-id='{name}'or @col-id='ip_properties.{name}' or @col-id='ip_properties_{name}']//i[contains(@class, 'filter')]";
    private final String columnsButton = "//div[contains(@class,'column-switcher')]//*[@data-testid='ps-selector-btn-icon']";
    private final String propSetCheckbox = "//label[normalize-space()='{name}']";
    private final String propSetCheckboxChecked = "//label[normalize-space()='{name}']/parent::div[contains(@class, 'checked')]";
    private final String propSetCheckboxTitle = "//span[normalize-space()='{name}'][@title='{name}']";
    private final String columnHeaders = "(//div[contains(@class,'ag-theme-mdx')])[last()]//div[contains(@class,'ag-header-cell')][@role='columnheader']";
    private final String tableCellLink = "//div[@role='gridcell']//*[@title='{value}' or @data-testid='{value}' or text()='{value}']/a";
    //update xPath after all pages are redesigned
    // Need to split for different grids or grid needs to be unified.
    private final String ipCellMain = "(//div[@row-index='{index}'])[last()]//div[@col-id='name' or @col-id='fqn' or @col-id='ag-Grid-AutoColumn' or @col-id='ipv' or @col-id='group']//*[@data-testid or @title]";
    private final String ipCellMainusagetab = "(//div[@row-index='{index}'])[last()]//div[@col-id='name' or @col-id='fqn' or @col-id='ag-Grid-AutoColumn' or @col-id='ipv' or @col-id='group']//div[@data-testid or @title]";
    private final String ipCell = ipCellMain + "[self::a or (self::div and contains(@class, 'asearch-link-cell__link')) or (self::span and contains(@class, 'bold-root-row')) or self::span and @title]";
    private final String appliedFilterIcon = "//div[@col-id='{name}'][contains(@class, 'ag-header-cell-filtered')]//i[contains(@class, 'filter')]";
    private final String cellId = "//div[contains(@class,'ag-theme-mdx')]//div[@row-index='{index}']//div[@col-id='{cellId}' or @col-id='ip_properties.{cellId}']/span";
    //update xPath after all pages are redesigned
    private final String rows = "//div[@col-id='name' or @col-id='fqn' or @col-id='ag-Grid-AutoColumn' or @col-id='fqn.ip' or @col-id='ipv'][contains(@class, 'ag-cell-value')]";
    private final String filterCheckBox = "//div[normalize-space()='{value}']//input[@type='checkbox']";
    private final String loadingIcon = "//*[contains(@class, 'spinner-border')] | //*[contains(@class, 'ag-loading')]";
    private final String cellTitle = "//div[@col-id='{column}'][contains(@class, 'ag-cell-value')]//*[@title='{value}']";
    private final String columnTitleXpath = "//span[contains(@class,'ag-header-cell-text') and normalize-space()='{columnTitle}']";
    private final String toolPanelColumnSelect = "//span[@class='ag-column-select-column-label' and normalize-space()= '{name}']";
    private final String rowByColId = "//div[@role='row']/div[@col-id='{column}'][contains(@class,'ag-cell')]";
    private final String horizontalScrollStr = "//div[@class='ag-body-horizontal-scroll-viewport']";
    private final String scrollRightCornerStr = "//div[contains(@class,'ag-horizontal-right-spacer')]";
    private final String scrollLeftCornerStr = "//div[contains(@class,'ag-horizontal-left-spacer')]";
    private final String columnName = "//div[@role='columnheader']";
    private final String libraryLinksCheck = "//span[contains(@class, 'ip-cell-renderer d-flex') and normalize-space()='{name}']/a";
    private final String cellIdVersionLine = "//div[@row-index='{index}']//div[@col-id='{cellId}']";
    private final String columnIndex = "(//div[@role='columnheader'][@aria-colindex='{index}'])[last()]";
    private final String allColumnCheckBox = "//div[@role='dialog' and contains(@class, 'ag-focus')]//*[@data-ref='eSelect']";
    private final String closeColumnSelect = ".ag-tab.ag-tab-selected";
    private final String ips = "//div[@row-index]";
    private final String firstRowProperty = "//div[@row-index='0']/div[@col-id='{propName}']";
    private final String filterOptionBooleanCheckBox = "//div[contains(@class,'ag-checkbox-label ag-label-ellipsis')][text()='{text}']";
    private final String shoppingCartTestDataId = "//*[@data-testid='shopping-cart-grid']" + firstRowProperty;
    // property Set common xpath
    private final String shieldIcon = "//*[@data-testid='ps-{ProtectedPropSetName}-shield-icon']";
    private final String shieldIconForPropertySet = shieldIcon + "[contains(@class,'icon-15x12')]";
    private final String shieldIcon20x16ForPropertySet = shieldIcon + "[contains(@class,'icon-20x16')]";
    private final String customIconForPropertySet = "//*[@data-testid='ps-{ProtectedPropSetName}-custom-icon'][@class='{iconName} fa-{iconName} fas fa-fw icon-20x16 me-1']";
    private final String proopertySetButton = "//div[contains(@class,'column-switcher')]//*[@data-testid='ps-selector-btn-icon']";
    private final String defaultIconForPropertySet = "//*[@data-testid='ps-{ProtectedPropSetName}-default-icon'][@class='icon-wrapper me-1']";
    private final String propertySetCheckBox = "//*[@data-testid='ps-{propertySetName}-checkbox-input']";
    private final String propertyShieldIconColumnHeader = "//*[contains(@data-testid,'column-header-{propertyName}')][@class='protected fa-regular fa-fw fa-shield-keyhole icon-15x12']";
    private final String propertyCustomIconColumnHeader = "//*[contains(@data-testid,'column-header-{propertyName}')][@class='fas fa-fw icon-15x12 me-2 {icon} fa-{icon}']";
    private final String protectedPropertyShieldIconOnPropertyColumn = "//*[@data-testId='column-header-{propertyName}-shield-icon'][contains(@class,'icon-15x12 fa-shield-keyhole')]";
    private final String propertySetNameCheck = "//span[normalize-space()='{propertySetName}' and @title='{propertySetName}']";
    private final String secondColumnHeader = "//div[@role='columnheader'][@aria-colindex=2]";
    private final String sortIconAfterHeaderClick = "//span[@class='ag-header-icon ag-sort-descending-icon']";
    private String columnHeaderPropertySetPopUpWithTestDataID = "//div[contains(@class, 'header-cell')]//div[contains(@class,'ag-header-cell-label')]//*[contains(text(),'{propName}')]";
    private final String columnHeaderPropertyModal = "//div[@role='columnheader'][@col-id='{name}' or @col-id='ip_properties.{name}' or @col-id='ip_properties_{name}' or @col-id='fqn.{name}']/*[@data-ref='eHeaderCompWrapper']";

    @Step("Get message...")
    public String getMessageText() {
        return waitForElementToBeVisible(noResultsFound).getText();
    }

    @Step("Verify no results found grid header message is displayed...")
    public String noResultsFoundHeaderText() {
        return waitForElementToBeVisible(noResultsFoundHeaderText).getText();
    }

    @Step("Verify no results found message is displayed...")
    public boolean noQueriesFound() {
        return isElementVisible(noQueriesFound);
    }

    @Step("Verify no results found message is displayed...")
    public boolean noPropertiesFound() {
        return isElementVisible(noPropertiesFoundMessage);
    }

    @Step("Filter IP name column...")
    public void filter(String column, String text) {
        hoverOverColumnName(column);
        click(filterIcon);
        inputText(filterInputField, text);
    }

    @Step("Get IP icon color...")
    public String getIpIconColor(String ipName, String iconClass) {
        final WebElement ipIcon = findElementWithWait(By.xpath(xpathIpIcon.replace("{name}", ipName).replace("{icon}", iconClass)));
        return ipIcon.getCssValue("color");
    }

    @Step("Enter text into filters input...")
    public void typeIntoFilterTextbox(String text) {
        enterTextSlowly(filterInputField, text);
    }

    @Step("Clear column filter input field...")
    public void clearFilters() {
        clearInputFieldWithBackspace(filterInputField);
    }

    @Step("Open context menu...")
    public void openContextMenu(String value) {
        final WebElement contextMenu = findElementWithFluentWait((By.xpath(tableCell.replace("{value}", value))));
        scrollToElement(contextMenu);
        waitTillClickableWithFluentWait(contextMenu);
        performRightMouseClick(contextMenu);
    }

    @Step("Open context menu for table with icon...")
    public void openContextMenuForTableWithoutLink(String value) {
        final WebElement contextMenu = findElementWithFluentWait((By.xpath(tableCellWithoutLink.replace("{value}", value))));
        scrollToElement(contextMenu);
        performRightMouseClick(contextMenu);
    }

    @Step("Click on Expand tree caret...")
    public void expandTreeNode() {
        click(expandTreeButton);
    }

    @Step("Context menu copy action...")
    public void copy() {
        click(copyButton);
    }

    @Step("Context menu copy with headers action...")
    public void copyWithHeaders() {
        click(copyWithHeaders);
    }

    @Step("Context menu open in new tab...")
    public void openInNewTab() {
        click(openInNewTab);
        switchToOpenedTab();
    }

    @Step("Context menu export action...")
    public void exportInCsvFormat() {
        click(csvExport);
    }

    @Step("Verify Export button is present in the context menu...")
    public boolean isExportButtonPresent() {
        return isElementVisible(exportButton);
    }

    @Step("Verify scroll for more notice is not displayed...")
    public int isScrollForMoreNoticeNotDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(scrollForMoreNotice), counter).size();
    }

    @Step("Verify IP icon is present...")
    public boolean isIpIconPresent(String ipName, String icon) {
        final WebElement ipIcon = findElementWithWait(By.xpath(xpathIpIcon.replace("{name}", ipName).replace("{icon}", icon)));
        return isElementVisible(ipIcon);
    }

    @Step("Pin column to the right...")
    public void pinColumnToRight() {
        hoverOverElement(pinColumnMenu);
        click(pinRightButton);
    }

    @Step("Autosize This Column...")
    public void autosizeColumn() {
        click(autosizeThisColumn);
    }

    @Step("Autosize This Columns...")
    public void autosizeAllColumns() {
        click(autosizeAllColumns);
    }

    @Step("Pin column to the left...")
    public void pinColumnLeft() {
        hoverOverElement(pinColumnMenu);
        click(pinLeftButton);
    }

    @Step("Get No Records found message...")
    public String getMessageOfNoRecordsFoundText() {
        return waitForElementToBeVisible(noRecordsFound).getText();
    }

    @Step("Verify column is pinned to the right...")
    public boolean isColumnPinnedRight(String columnName) {
        final WebElement pinnedColumn = findElementWithWait(By.xpath(pinnedRightColumn.replace("{col}", columnName)));
        return isElementVisible(pinnedColumn);
    }

    @Step("Verify column is pinned to the left...")
    public boolean isColumnPinnedLeft(String columnName) {
        final WebElement pinnedColumn = findElementWithWait(By.xpath(pinnedLeftColumn.replace("{col}", columnName)));
        return isElementVisible(pinnedColumn);
    }

    @Step("Verify no pinned columns are displayed...")
    public boolean noPinnedColumnsPresent(String columnName) {
        final List<WebElement> pinnedColumns = driver.findElements(By.xpath(pinnedRightColumn.replace("{name}", columnName)));
        return getNumberOfElements(pinnedColumns) == 0;
    }

    @Step("Check column checkbox...")
    public void addColumn(String... columns) {
        for (final String column : columns) {
            inputText(columnSearchField, "");
            enterTextSlowly(columnSearchField, column);
            DriverFactory.sleep(2000);
            final WebElement checkCheckbox = findElementWithFluentWait(By.xpath(columnCheckbox.replace("{name}", column)));
            waitTillClickableWithFluentWait(checkCheckbox);
            final boolean isChecked = checkCheckbox.getAttribute("class").contains("ag-checked");

            if (isChecked) {
                logger.info("Checkbox for column '{}' is already selected.", column);
            } else {
                final WebElement checkbox = waitForElementToBePresent(By.xpath(columnCheckbox.replace("{name}", column)));
                checkbox.click();
                logger.info("Checkbox for column '{}' is now selected.", column);
            }
        }
    }

    @Step("Check column checkbox and close Menu...")
    public void addColumnAndCloseMenu(String... columns) {
        addColumn(columns);
        click(findElementWithWait(By.cssSelector(closeColumnSelect)));
    }

    @Step("Uncheck column checkbox...")
    public void deselectColumn(String... columns) {
        logger.info("Deselecting columns: {}", (Object) columns);
        for (final String column : columns) {
            inputText(columnSearchField, "");
            enterTextSlowly(columnSearchField, column);
            final WebElement checkCheckbox = findElementWithFluentWait(By.xpath(columnCheckbox.replace("{name}", column)));
            final boolean isChecked = checkCheckbox.getAttribute("class").contains("ag-checked");

            if (!isChecked) {
                logger.info("Checkbox for column '{}' is already unselected.", column);
            } else {
                final WebElement checkbox = waitForElementToBePresent(By.xpath(columnCheckbox.replace("{name}", column)));
                checkbox.click();
                logger.info("Deselected checkbox for column '{}'", column);
            }
        }
    }

    @Step("Uncheck All columnS checkbox...")
    public void deselectAllColumns() {
        logger.info("Deselecting all columns except default columns");
        doubleClickOnElement(toggleSelect);
    }

    @Step("Verify column is displayed...")
    public boolean isColumnDisplayed(String name) {
        return isColumnDisplayed(name, PageModelName.none);
    }

    @Step("Verify column is displayed...")
    public boolean isColumnDisplayed(String name, PageModelName pageModelName) {
        try {
            final String locator = "(" + (getDataTestId(pageModelName) + columnHeaderWithTestDataID.replace("{name}", name)).trim() + ")[last()]";
            final WebElement column = waitForElementToBePresentFluentWait(By.xpath(locator));
            scrollToElement(column);
            logger.info("Column '{}' is displayed.", name);
            return isElementVisible(column);
        } catch (TimeoutException e) {
            logger.info("Column '{}' is not displayed.", name);
            return false;
        }
    }

    @Step("Verify column is not displayed...")
    public boolean columnNotDisplayed(String name) {
        return columnNotDisplayed(name, PageModelName.none);
    }

    @Step("Verify column is not displayed...")
    public boolean columnNotDisplayed(String name, PageModelName pageModelName) {
        waitForPageLoaded();
        DriverFactory.sleep(3000);
        final String column = "(" + (getDataTestId(pageModelName) + columnHeaderWithTestDataID).replace("{name}", name).trim() + ")[last()]";
        final boolean notDisplayed = getNumberOfVisibleElements(driver.findElements(By.xpath(column)))==0;
        if (notDisplayed) {
            logger.info("Column '{}' is not displayed.", name);
        } else {
            logger.info("Column '{}' is displayed.", name);
        }
        return notDisplayed;
    }

    @Step("Resize column...")
    public void resizeColumn(String columnName, String targetColumn) {
        DriverFactory.sleep(2000); // scope to improve
        final WebElement column = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", columnName)));
        final WebElement targetElement = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", targetColumn)));
        dragAndDropElement(column, targetElement);
        DriverFactory.sleep(3000); // scope to improve
    }

    //TODO: refactor this method not to take column index as a parameter from DOM, but as actual column index
    @Step("Get the name of the second visible column...")
    public String getSecondVisibleColumnName(String index) {
        DriverFactory.sleep(3000); // scope to refactor
        final WebElement column = findElementWithFluentWait(By.xpath(columnIndex.replace("{index}", index)));
        return getAttributeValue(column, "col-id");
    }

    @Step("Reorder columns...")
    public void reorderColumns(String columnName, String targetColumn) {
        waitForDataToLoad();
        final WebElement column = findElementWithFluentWait(By.xpath(columnHeader.replace("{name}", columnName)));
        final WebElement target = findElementWithFluentWait(By.xpath(columnHeader.replace("{name}", targetColumn)));
        dragAndDropElement(column, target);
        DriverFactory.sleep(4000); // scope to improve
    }

    @Step("Reorder columns...")
    public void reorderColumnsWithDataID(String columnName, String targetColumn, PageModelName pageModelName) {
        waitForDataToLoad();
        final String column1 = "(" + (getDataTestId(pageModelName) + columnHeaderWithTestDataID).replace("{name}", columnName).trim() + ")[last()]";
        final String column2 = "(" + (getDataTestId(pageModelName) + columnHeaderWithTestDataID).replace("{name}", targetColumn).trim() + ")[last()]";
        final WebElement column = findElementWithFluentWait(By.xpath(column1));
        final WebElement target = findElementWithFluentWait(By.xpath(column2));
        dragAndDropElement(column, target);
        waitForDataToLoad();
    }

    @Step("Click on column {0} to sort...")
    public void clickOnColumnToSort(String colHeader) {
        waitForDataToLoad();
        final WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", colHeader)));
        scrollToElement(column);
        waitTillClickableWithFluentWait(column).click();
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton() {
        click(findElementWithWait(By.xpath(columnsButton)));
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName) {
        clickOnColumnsButton();
        final WebElement checkbox = findElementWithWait(By.xpath(propSetCheckbox.replace("{name}", propSetName)));
        click(checkbox);
        if (isPropSetSelected(propSetName)) {
            logger.info("Checkbox for property Set '{}' is selected.", propSetName);
        } else {
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", checkbox);
            logger.info("Try to select Property Set checkbox again '{}'", propSetName);
        }
        clickOnColumnsButton();
    }

    @Step("Verify property set is selected...")
    public boolean isPropSetSelected(String propSetName) {
        final WebElement checkBoxStatus = waitForElementToBePresent(By.xpath(propSetCheckboxChecked.replace("{name}", propSetName)));
        return isElementVisible(checkBoxStatus);
    }

    @Step("Get number of columns when scrolling...")
    public int getNumberOfColumnsScrolling() {
        final List<WebElement> horizontalElements = driver.findElements(By.xpath(horizontalScrollStr));
        final List<WebElement> scrollRightCornerElements = driver.findElements(By.xpath(scrollRightCornerStr));
        final List<WebElement> scrollLeftCornerElements = driver.findElements(By.xpath(scrollLeftCornerStr));
        if (horizontalElements.size() == 2) {
            return getNumberOfColumnsHorizontalScroll(columnHeaders, horizontalElements.get(1), scrollRightCornerElements.get(1), scrollLeftCornerElements.get(1));
        } else {
            return getNumberOfColumnsHorizontalScroll(columnHeaders, horizontalScroll, scrollRightCorner, scrollLeftCorner);
        }
    }

    @Step("Get number of columns...")
    public int getNumberOfColumns() {
        return getNumberOfColumns(columnHeaders);
    }

    @Step("Get number of columns...")
    public int getNumberOfColumnsPlanningBom() {
        return getColumnHeader.size();
    }

    @Step("Get IP name from the first row...")
    public String getIpNameFromRow(String rowNumber) {
        return getText(findElementWithWait(By.xpath(ipCell.replace("{index}", rowNumber))));
    }

    @Step("Get IP name from the first row Usage tab...")
    public String getIpNameFromRowUsageTab(String rowNumber) {
        return getText(findElementWithWait(By.xpath(ipCellMainusagetab.replace("{index}", rowNumber))));
    }

    @Step("Get Specified column from the specified row...")
    public String getSpecifiedColumnFromRow(String cell, String rowNumber) {
        return getText(findElementWithWait(By.xpath(cellId.replace("{cellId}", cell).replace("{index}", rowNumber))));
    }

    @Step("Get number of sorting icons...")
    public int getNumberOfSortingIcons() {
        return getNumberOfVisibleElements(sortingIcons);
    }

    @Step("Click on Reset columns button...")
    public void resetColumnsState() {
        click(resetColumnsButton);
    }

    @Step("Select filter option and type text into the filter input field...")
    public void filterColumn(String column, String option, String text) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        click(filterOptions);
        final WebElement value = findElementWithWait(By.xpath(filterOption.replace("{option}", option)));
        waitForElementToBeClickable(value).click();
        clearInputFieldWithBackspace(filterInputField);
        enterTextSlowly(filterInputField, text);
    }

    @Step("Verify filter icon is not visible on hover...")
    public boolean filterIconNotVisible(String column) {
        hoverOverColumnName(column);
        return waitForNumberOfElementsToBe(By.xpath(secondaryFilterIcon.replace("{name}", column)), 0).isEmpty();
    }

    @Step("Verify sort icon not visible...")
    public boolean sortIconNotVisible(String colHeader) {
        final WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", colHeader)));
        scrollToElement(column);
        click(column);
        return waitForNumberOfElementsToBe(By.xpath(sortIconAfterHeaderClick), 0).isEmpty();
    }

    @Step("Select filter option and clear filter input field...")
    public void clearFilterColumn(String column) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        click(filterOptions);
        clearInputFieldWithBackspace(filterInputField);
    }

    // After use filterColumn function, filter is not closing, so you can not use filterColumn twice.
    // Great to use inputTextColumnFilter as second time filter for the same column
    @Step("Input text in open column filter")
    public void inputTextColumnFilter(String text) {
        waitForElementToBeClickable(filterInputField);
        inputText(filterInputField, text);
    }

    @Step("Click on filter icon...")
    public void clickOnFilterIcon(String column) {
        final WebElement filterIcon = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        waitForElementToBeClickable(filterIcon).click();
    }

    @Step("Click filter icon for column...")
    public void clickFilterIcon(String name) {
        final WebElement filterIcon = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", name)));
        waitForElementToBeClickable(filterIcon).click();
    }

    @Step("Check if filter icon is active for column...")
    public boolean isFilterIconActive(String name) {
        final WebElement filterIcon = findElementWithWait(By.xpath(appliedFilterIcon.replace("{name}", name)));
        return isElementVisible(filterIcon);
    }

    @Step("Check if number of rows is correct...")
    public boolean isNumberOfRowsCorrect(int counter) {
        final int actualRowCount = getNumberOfRowsForLazyLoadUntilFoundLastRow(rows);
        logger.info("Number of rows: " + actualRowCount);
        return actualRowCount==counter;
    }

    @Step("Verify all specified columns are present...")
    public void areColumnsPresent(String[] names) {
        for (final String name : names) {
            final boolean displayed = isColumnDisplayed(name);
            logger.info("Column '{}' present: {}", name, displayed);
            Assert.assertTrue(displayed, "Column " + name + " is not displayed");
        }
    }

    @Step("Check filtering checkbox for value...")
    public void checkFilteringBox(String value) {
        waitForVueRendering();
        final WebElement checkbox = waitForElementToBePresent(By.xpath(filterCheckBox.replace("{value}", value)));
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkbox);
    }

    @Step("Open filtering menu for column and check option...")
    public void clickOnFilteringMenuAndCheckTheOption(String column, String value) {
        waitForVueRendering();
        hoverOverColumnName(column);
        final WebElement filterIconMenu = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        click(filterIconMenu);
        checkFilteringBox(value);
    }

    @Step("Wait for loading spinner to appear...")
    public void waitForLoadingSpinner() {
        DriverFactory.sleep(500); // Short wait before checking
        try {
            waitForElementToBeInVisible(loadingIndicator);
        } catch (Exception e) {
            logger.warn("Loading indicator did not appear within the expected time.");
        }
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForDataToLoad(Duration duration) {
        waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0, duration);
    }

    @Step("Wait for loading spinner to disappear (data loaded)...")
    public void waitForDataToLoad() {
        DriverFactory.sleep(500); // Short wait before checking
        try {
            waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0);
        } catch (AssertionError | Exception e) {
            logger.warn("Loading indicator did not appear within the expected time.");
        }
    }

    @Step("Click on table cell with IP value...")
    public void clickOnIp(String value) {
        click(findElementWithWait(By.xpath(tableCellLink.replace("{value}", value))));
    }

    @Step("Check if property set menu tooltip is visible...")
    public boolean isPropertySetTooltipPresent(String propertySetName) {
        clickOnColumnsButton();
        final WebElement propertySetBox = findElementWithWait(By.xpath(propSetCheckboxTitle.replace("{name}", propertySetName).replace("{name}", propertySetName)));
        return isElementVisible(propertySetBox);
    }

    @Step("Verify grid cell title is present")
    public String getCellTooltipValue(String column, String cellValue) {
        final WebElement cellTooltip = findElementWithWait(By.xpath(cellTitle.replace("{column}", column)
                .replace("{value}", cellValue)));
        return getAttributeValue(cellTooltip, "title");
    }

    @Step("Verify that column name is truncated...")
    public String getColumnHeaderTooltip(String columnTitle) {
        final WebElement columnHeaderTooltip = findElementWithWait(By.xpath(columnTitleXpath.replace("{columnTitle}", columnTitle)));
        return getAttributeValue(columnHeaderTooltip, "title");
    }

    @Step("Open filter menu and enter text into the text field...")
    public void openFilterAndEnterTextIntoSearchField(String column, String searchValue) {
        hoverOverColumnName(column);
        final WebElement filterIconMenu = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        click(filterIconMenu);
        inputText(filteringSearchField, searchValue);
    }

    @Step("Check to see if no matches found message appears in filter search...")
    public boolean isNoMatchesFoundMessagePresent() {
        return isElementVisible(noMatchesMessage);
    }

    @Step("Open Tool panel...")
    public void openToolPanel(String columnId) {
        openMainHamburgerMenu(columnId);
        click(toolPanel);
    }

    @Step("Open Tool panel by selecting the option after right clicking any grid cell...")
    public void openToolPanelByRightClickingOnGridCell() {
        performRightMouseClick(firstCellOfGrid);
        click(toolPanel);
    }

    @Step("Verify column is displayed...")
    public boolean isColumnHeaderPresent(String name) {
        try {
            logger.info("Checking if column is displayed: {}", name);
            final WebElement column = waitForElementToBePresent(By.xpath(columnHeader.replace("{name}", name)));
            scrollToElement(column);
            return isElementVisible(column);
        } catch (WebDriverException e) {
            logger.info(name + " column is not visible...");
            return false;
        }
    }

    @Step("Select column from Tool panel...")
    public void selectColumnFromToolPanel(String columnName) {
        final WebElement column = findElementWithWait(By.xpath(toolPanelColumnSelect.replace("{name}", columnName)));
        click(column);
    }

    @Step("Select multiple columns from Tool panel...")
    public void selectMultipleColumnsFromToolPanel(String... columnNames) {
        for (final String columnName : columnNames) {
            selectColumnFromToolPanel(columnName);
        }
    }

    @Step("Remove text from filter input field...")
    public void hideFilterMenu() {
        click(filterIcon);
    }

    @Step("Remove text from filter input field...")
    public void clearFilterInputField() {
        clearInputFieldWithBackspace(filterInputField);
    }

    @Step("Scroll horizontally to the right...")
    public void scrollRight() {
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeft() {
        dragAndDropElement(horizontalScroll, scrollLeftCorner);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeft(String column) {
        final GridTablePage gridTablePage = new GridTablePage(driver);
        final int maxAttempts = 5; // Prevent infinite loop
        int attempts = 0;
        while (!gridTablePage.isColumnDisplayed(column) && attempts < maxAttempts) {
            dragAndDropElement(horizontalScroll, scrollLeftCorner);
            attempts++;
            DriverFactory.sleep(200);
        }
        if (attempts >= maxAttempts && !gridTablePage.isColumnDisplayed(column)) {
            throw new NoSuchElementException("Column '" + column + "' not found after " + maxAttempts + " scroll attempts");
        }
    }

    @Step("Selecting column and filtering option...")
    public void selectColumnAndFilteringOption(String column, String option) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        selectFilteringOption(option);
    }

    @Step("Selecting filter option...")
    public void selectFilteringOption(String option) {
        click(filterOptions);
        final WebElement value = findElementWithWait(By.xpath(filterOption.replace("{option}", option)));
        click(value);
    }

    @Step("Insert from and to value...")
    public void typeTextIntoFromAndToFields(String fromValue, String toValue) {
        inputText(stringFilterFrom, fromValue);
        inputText(stringFilterTo, toValue);
    }

    @Step("Hover over column name...")
    public void hoverOverColumnName(String columnName) {
        hoverOverColumnName(columnName, PageModelName.none);
    }

    @Step("Hover over column name...")
    public void hoverOverColumnName(String columnName, PageModelName pageModelName) {
        final String locator = (getDataTestId(pageModelName) + columnHeaderWithTestDataID.replace("{name}", columnName)).trim();
        final WebElement column = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column);
    }

    @Step("Open main hamburger menu...")
    public void openMainHamburgerMenu(String columnId) {
        hoverOverColumnName(columnId);
        waitTillClickableWithFluentWait(mainMenuHamburgerButton).click();
    }

    @Step("Open main hamburger menu...")
    public void openMainHamburgerMenu(String columnId, PageModelName pageModelName) {
        hoverOverColumnName(columnId, pageModelName);
        waitTillClickableWithFluentWait(mainMenuHamburgerButton).click();
    }

    @Step("Open main hamburger menu with first column...")
    public void openHamburgerMenuFirstColumn(PageModelName pageModelName) {
        final String locator = (getDataTestId(pageModelName) + firstColumnHeader).trim();
        final WebElement column = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column);
        waitTillClickableWithFluentWait(mainMenuHamburgerButton).click();
    }

    @Step("Open columns select menu")
    public void openColumnsSelectMenu() {
        try {
            waitTillClickableWithFluentWait(columnSelectMenu).click();
        } catch (Exception e) {
            logger.error("select menu already opened");
        }
    }

    @Step("Click on secondary hamburger menu...")
    public void openSecondaryHamburgerMenu() {
        click(hamburgerSubMenuButton);
    }

    @Step("Resize column...")
    public void resizeTableColumn(String columnName) {
        final WebElement column = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", columnName)));
        resizeGridColumn(column);
    }

    @Step("Verify number of rows by col-id...")
    public boolean getNumberOfRowsByColId(String colId, int counter) {
        waitForNumberOfElementsToBe(By.xpath(rowByColId.replace("{column}", colId)), counter).size();
        return true;
    }

    @Step("Verify on Ip List column do not having columns")
    public void isIpListColumnsNotVisible(String value) {
        waitForNumberOfElementsToBe(By.xpath(columnCheckbox.replace("{name}", value)), 0);
    }

    @Step("Verify on Column header do not having grid table")
    public void isColumnsNotVisibleGridTable(String value) {
        waitForNumberOfElementsToBe(By.xpath(columnTitleXpath.replace("{columnTitle}", value)), 0);
    }

    @Step("Verify number of Library links in grid")
    public void getNumberOfLibraryLinks(String libName, int counter) {
        waitForNumberOfElementsToBe(By.xpath(libraryLinksCheck.replace("{name}", libName)), counter).size();
    }

    @Step("Get Specified cell value from the specified Line & Version column")
    public String getColumnLineVersionValueFromRow(String cell, String rowNumber) {
        return getText(findElementWithWait(By.xpath(cellIdVersionLine.replace("{cellId}", cell).replace("{index}", rowNumber))));
    }

    @Step("Click on Library Button")
    public LibraryDetailsPage clickOnLibraryLink(String libName) {
        final WebElement libraryLink = findElementWithWait(By.xpath(libraryLinksCheck.replace("{name}", libName)));
        click(libraryLink);
        return new LibraryDetailsPage(driver);
    }

    @Step("Hover over Library link ")
    public void hoverOverLibraryLink(String name) {
        final WebElement libraryLink = findElementWithWait(By.xpath(libraryLinksCheck.replace("{name}", name)));
        hoverOverElement(libraryLink);
    }

    @Step("Getting a list of columns")
    public List<String> getListOfColumns() {
        final List<String> columns = new ArrayList();
        final int i = getNumberOfElements(columnList);
        for (int j = 0; j < i; j++) {
            columns.add(columnList.get(j).getAttribute("col-id"));
        }
        return columns;
    }

    @Step("Close column filter menu...")
    public void closeColumnFilter() {
        click(closeFilterIcon);
    }

    @Step("Select all columns option...")
    public void selectAllColumns() {
        final WebElement allColumns = waitForElementToBePresent(By.xpath(allColumnCheckBox));
        click(allColumns);
        click(findElementWithWait(By.cssSelector(closeColumnSelect)));
    }

    @Step("Get number of IPs...")
    public int getNumberOfIps(int counter) {
        return getNumberOfIps(counter, PageModelName.none);
    }

    @Step("Get number of IPs...")
    public int getNumberOfIps(int counter, PageModelName pageModelName) {
        final String element = (getDataTestId(pageModelName) + ips).trim();
        return waitForNumberOfElementsToBe(By.xpath(element), counter).size();
    }

    @Step("Click on the calendar icon...")
    public void clickOnCalendarIcon() {
        click(datepicker);
    }

    @Step("Select today's date...")
    public void selectToday() {
        click(todayDate);
    }

    @Step("Select the next day...")
    public void selectNextDay() {
        click(calendarDayAfter);
    }

    @Step("Select the previous day...")
    public void selectYesterday() {
        click(yesterday);
    }

    @Step("Click on the calendar icon from...")
    public void clickOnCalendarIconFrom() {
        click(filterFrom);
    }

    @Step("Click on the calendar icon to...")
    public void clickOnCalendarIconTo() {
        click(filterTo);
    }

    @Step("Get IP name from the first row...")
    public String getIpFromFirstTableRow() {
        return getText(firstRowIpCell);
    }

    @Step("Get property value from the first row of the table...")
    public String getPropValueFromFirstTableRow(String propName) {
        final WebElement propValue = findElementWithWait(By.xpath(firstRowProperty.replace("{propName}", propName)));
        final String value = getText(propValue);
        logger.info("Actual value from the first row of the table: {}", value);
        return value;
    }

    @Step("Get property value from the first row of the table...")
    public String getPropValueFromFirstTableRowFromModel(String propName, String testName) {
        final WebElement propValue = findElementWithWait(By.xpath(shoppingCartTestDataId.replace("{propName}", propName)));
        final String value = getText(propValue);
        logger.info("Actual value from the first row of the table: {}", value);
        return value;
    }

    @Step("Verify sort ascending icon is present...")
    public boolean isSortAscendingIconPresent() {
        return isElementVisible(sortAscendingIcon);
    }

    @Step("Verify sort desc icon is present...")
    public boolean isSortDescendingIconPresent() {
        return isElementVisible(sortDescendingIcon);
    }

    @Step("Adding multiple columns...")
    public void addMultipleColumns(String... columns) {
        for (final String column : columns) {
            addColumn(column);
        }
    }

    @Step("Select boolean filter option and type text into the filter input field...")
    public void filterBooleanColumn(String column, String option) {
        hoverOverColumnName(column);
        clickOnFilterIcon(column);
        final WebElement value = findElementWithWait(By.xpath(filterOptionBooleanCheckBox.replace("{text}", option)));
        waitForElementToBeClickable(value).click();
    }

    @Step("Select boolean filter option from the opened selector...")
    public void selectBooleanValue(String option) {
        final WebElement value = findElementWithWait(By.xpath(filterOptionBooleanCheckBox.replace("{text}", option)));
        waitForElementToBeClickable(value).click();
    }

    @Step("Closing columns menu...")
    public void closeColumnsSelectMenu() {
        waitTillClickableWithFluentWait(columnSelectMenuOpen).click();
    }

    @Step("Expand all row groups...")
    public void expandAllRowGroups(String columnId) {
        openMainHamburgerMenu(columnId);
        waitTillVisibleWithFluentWait(expandAllRowGroupsLink).click();
    }

    @Step("Clicking on columns tab in menu")
    public void clickOnColumnsTabInMenu() {
        waitTillVisibleWithFluentWait(columnsTabInMenu).click();
    }

    @Step("Click on Columns button...")
    public void clickOnPropertySetButton() {
        click(findElementWithWait(By.xpath(proopertySetButton)));
    }

    @Step("Click on Specific property set checkbox have dropdown with Scroll bar...")
    public void clickOnPropertySetCheckBox(String propertySetName) {
        final String checkBox = propertySetCheckBox.replace("{propertySetName}", propertySetName);
        final Actions actions = new Actions(driver);
        logger.info("Checkbox of Property set xpath: {}", checkBox);
        int attempts = 0;
        while (attempts < 13) {
            try {
                final WebElement checkBoxElement = driver.findElement(By.xpath(checkBox));
                if (checkBoxElement.isDisplayed() && checkBoxElement.isEnabled()) {
                    click(checkBoxElement);
                    logger.info("Checkbox '{}' clicked successfully.", propertySetName);
                    break;
                }
            } catch (TimeoutException e) {
                logger.warn("Attempt {}: Checkbox '{}' is not clickable yet.", attempts + 1, propertySetName);
                actions.sendKeys(Keys.TAB).perform(); // Simulate pressing Tab
                attempts++;
            }
        }
        if (attempts == 13) {
            logger.error("Checkbox '{}' could not be clicked after 13 attempts.", propertySetName);
            throw new TimeoutException("Failed to click checkbox after multiple attempts.");
        }
    }

    @Step("Verify that shield icon for property set is visible...")
    public boolean isShieldIconForProtectedPropertySetVisible(String propertySetName) {
        final WebElement shieldIcon = findElementWithWait(By.xpath(shieldIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)));
       if(shieldIcon.isDisplayed()){
           logger.info("Shield icon for property set '{}' is displayed.", propertySetName);}
        return shieldIcon.isDisplayed();
    }

    @Step("Verify that shield icon for unprotected property set is not visible...")
    public boolean isShieldIconNotDisplayForUnProtectedPropertySetVisible(String propertySetName) {
        final String xpath = shieldIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName);
        final List<WebElement> shieldIcons = driver.findElements(By.xpath(xpath));
        if(shieldIcons.isEmpty()){
            logger.info("Shield icon for property set '{}' is not displayed.", propertySetName);}
        return shieldIcons.isEmpty();
    }

    @Step("Verify that shield icon 20x16 for property set is visible...")
    public boolean isShieldIcon20x16Visible(String propertySetName) {
        final WebElement shieldIcon = findElementWithWait(By.xpath(shieldIcon20x16ForPropertySet.replace("{ProtectedPropSetName}", propertySetName)));
        if (shieldIcon.isDisplayed()) {
            logger.info("Shield icon 20x16 for property set '{}' is displayed.", propertySetName);
        }
        return shieldIcon.isDisplayed();
    }

    @Step("Verify that shield icon 20x16 for unprotected property set is not visible...")
    public boolean isShieldIcon20x16NotVisible(String propertySetName) {
        final String xpath = shieldIcon20x16ForPropertySet.replace("{ProtectedPropSetName}", propertySetName);
        final List<WebElement> shieldIcons = driver.findElements(By.xpath(xpath));
        if (shieldIcons.isEmpty()) {
            logger.info("Shield icon 20x16 for property set '{}' is not displayed.", propertySetName);
        }
        return shieldIcons.isEmpty();
    }

    @Step("Verify that custom icon for property set is visible...")
    public boolean isCustomIconForPropertySetVisible(String propertySetName, String iconName) {
        final WebElement customIcon = findElementWithWait(By.xpath(customIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)
                .replace("{iconName}", iconName)));
        if (customIcon.isDisplayed()) {
            logger.info("Custom icon '{}' for property set '{}' is displayed.", iconName, propertySetName);
        }
        return customIcon.isDisplayed();
    }

    @Step("Verify that custom icon for property set is visible...")
    public boolean isDefaultIconForPropertySetVisible(String propertySetName) {
        final WebElement customIcon = findElementWithWait(By.xpath(defaultIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)));
        if (customIcon.isDisplayed()) {
            logger.info("default icon for property set '{}' is displayed.", propertySetName);
        }
        return customIcon.isDisplayed();
    }

    @Step("Verify that property custom icon in column header is displayed...")
    public boolean isPropertyShieldIconColumnInHeaderDisplayed(String propertyName) {
        final String xpath = propertyShieldIconColumnHeader.replace("{propertyName}", propertyName);
        final WebElement customIconColumnHeader = findElementWithWait(By.xpath(xpath));
        if (customIconColumnHeader.isDisplayed()) {
            logger.info("Property Shield icon column header '{}' is displayed.", propertyName);
        } else {
            logger.warn("Property Shield icon column header '{}' is not displayed.", propertyName);
        }
        return customIconColumnHeader.isDisplayed();
    }

    @Step("Verify that property shield icon column header is displayed...")
    public boolean isPropertyCustomIconColumnHeaderDisplayed(String propertyName) {
        final String iconName = propertyName.substring(propertyName.lastIndexOf('_') + 1);
        final String xpath = propertyCustomIconColumnHeader
                .replace("{propertyName}", propertyName)
                .replace("{icon}", iconName);
        final WebElement shieldIconColumnHeader = findElementWithWait(By.xpath(xpath));
        if (shieldIconColumnHeader.isDisplayed()) {
            logger.info("Custom icon for column header '{}' with icon '{}' is displayed.", propertyName, iconName);
        } else {
            logger.warn("Custom icon for column header '{}' with icon '{}' is not displayed.", propertyName, iconName);
        }
        return shieldIconColumnHeader.isDisplayed();
    }

    @Step("Verify that property custom icon column header is not present...")
    public boolean isPropertyCustomIconColumnHeaderNotPresent(String propertyName) {
        final String iconName = propertyName.substring(propertyName.lastIndexOf('_') + 1);
        final String xpath = propertyCustomIconColumnHeader
                .replace("{propertyName}", propertyName)
                .replace("{icon}", iconName);
        final List<WebElement> customIconColumnHeaders = driver.findElements(By.xpath(xpath));
        if (customIconColumnHeaders.isEmpty()) {
            logger.info("Property custom icon column header '{}' is not present.", propertyName);
            return true;
        } else {
            logger.warn("Property custom icon column header '{}' is present.", propertyName);
            return false;
        }
    }

    @Step("Verify that property shield icon column header is not present...")
    public boolean isPropertyShieldIconColumnHeaderNotPresent(String propertyName) {
        final String xpath = propertyShieldIconColumnHeader.replace("{propertyName}", propertyName);
        final List<WebElement> shieldIconColumnHeaders = driver.findElements(By.xpath(xpath));
        if (shieldIconColumnHeaders.isEmpty()) {
            logger.info("Property shield icon column header '{}' is not present.", propertyName);
            return true;
        } else {
            logger.warn("Property shield icon column header '{}' is present.", propertyName);
            return false;
        }
    }

    @Step("Verify that property set name '{propertySetName}' is not displayed...")
    public boolean isPropertySetNameNotDisplayed(String propertySetName, PageModelName pageModelName) {
        final String xpath = getDataTestId(pageModelName) + propertySetNameCheck.replace("{propertySetName}", propertySetName);
        final List<WebElement> elements = driver.findElements(By.xpath(xpath));
        if(elements.isEmpty() || !elements.get(0).isDisplayed()) {
            logger.info("Property set name '{}' is not displayed.", propertySetName);
            return true;
        }
        else {
            logger.warn("Property set name '{}' is displayed.", propertySetName);
            return false;
        }
    }

    @Step("Verify Property column is not present in column dropdown...")
    public boolean propertyColumnNotPresentInColumnDropdown(String column) {
            inputText(columnSearchField, "");
            enterTextSlowly(columnSearchField, column);
        final List<WebElement> checkCheckboxes = driver.findElements(By.xpath(columnCheckbox.replace("{name}", column)));
            if (checkCheckboxes.isEmpty()) {
                logger.info("Property column is not present in column dropdown "+ column);
                return true;
            } else {
                return false;
            }
        }

    @Step("Open main hamburger menu with second column...")
    public void openHamburgerMenuSecondColumn(PageModelName pageModelName) {
        final String locator = (getDataTestId(pageModelName) + secondColumnHeader).trim();
        final WebElement column = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column);
        waitTillClickableWithFluentWait(mainMenuHamburgerButton).click();
    }

    @Step("Resize column...")
    public void resizeAndIncreaseColumnWidth(String columnName) {
        final Actions actions = new Actions(DriverFactory.getBrowserInstance());
        // Resize by dragging 300 pixels to the right
        actions.clickAndHold(findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", columnName))))
                .moveByOffset(300, 0)
                .release()
                .perform();
    }

    @Step("Get the list of available columns...")
    public Set<String> getAllColumnNameList() {
        logger.info("Scrolling horizontally to get all column names...");
        final Set<String> uniqueColumns = new TreeSet<>();
        boolean lastColumnVisible = true;
        do {
            final List<String> initialScroll = getTextsForElements(By.xpath(columnHeaders));
            if (isLastAgGridRowColumnDisplay(agGridLastColumnCssPath)) {
                lastColumnVisible = false;
            } else {
                dragAndDropElement(horizontalScroll, scrollRightCorner);
                DriverFactory.sleep(1000);
            }
            uniqueColumns.addAll(initialScroll);
        } while (lastColumnVisible);
        scrollToElement(DriverFactory.getBrowserInstance().findElement(By.cssSelector(agGridLastColumnCssPath)));
        uniqueColumns.addAll(getTextsForElements(By.xpath(columnHeaders)));
        uniqueColumns.remove("");
        return uniqueColumns;
    }

    @Step("close sub Menu...")
    public void closeSubMenu() {
        click(findElementWithWait(By.cssSelector(closeColumnSelect)));
    }

    public void filterColumnOfPlanningBom(String column, String option, String text) {
        final String locator =  columnHeader.replace("{name}", column).trim();
        final WebElement column1 = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column1);
        clickOnFilterIcon(column);
        click(filterOptions);
        final WebElement value = findElementWithWait(By.xpath(filterOption.replace("{option}", option)));
        waitForElementToBeClickable(value).click();
        clearInputFieldWithBackspace(filterInputField);
        enterTextSlowly(filterInputField, text);
    }

    @Step("Hover over Property set pop-up column name...")
    public void hoverOverPropertySetColumnName(String columnName, PageModelName pageModelName) {
        final String locator = (getDataTestId(pageModelName) + columnHeaderPropertySetPopUpWithTestDataID.replace("{propName}", columnName)).trim();
        final WebElement column = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column);
    }

    @Step("Open main hamburger menu for Property set...")
    public void openMainHamburgerMenuForPropertySet(String columnId, PageModelName pageModelName) {
        hoverOverPropertySetColumnName(columnId, pageModelName);
        waitTillClickableWithFluentWait(mainMenuHamburgerButton).click();
    }

    @Step("Click on filter icon includes test data ID...")
    public void clickOnFilterIconWithPageModalId(String column, PageModelName pageModelName) {
        final WebElement filterIcon = findElementWithWait(By.xpath(getDataTestId(pageModelName) + secondaryFilterIcon.replace("{name}", column)));
        logger.info("Clicking on filter icon for column '{}' with page model '{}' locator {}", column, pageModelName, getDataTestId(pageModelName) + secondaryFilterIcon.replace("{name}", column));
        waitForElementToBeClickable(filterIcon).click();
    }

    @Step("Select filter option with Page Modal Id and type text into the filter input field...")
    public void filterColumnWithPageModalId(String column, String option, String text, PageModelName pageModelName) {
        DriverFactory.sleep(1000); // ToDO: Need to Improve in future
        hoverOverPropertyModalColumnName(column, pageModelName);
        clickOnFilterIconWithPageModalId(column, pageModelName);
        click(filterOptions);
        final WebElement value = findElementWithWait(By.xpath(filterOption.replace("{option}", option)));
        logger.info("Clicking on filter option '{}' for column '{}' with page model '{}' locator {}", option, column, pageModelName, filterOption.replace("{option}", option));
        waitForElementToBeClickable(value).click();
        logger.info("Filter option '{}' clicked for column '{}' with page model '{}' filterInputField Locator", option, column, pageModelName, filterInputField.toString());
        clearInputFieldWithBackspace(filterInputField);
        enterTextSlowly(filterInputField, text);
    }

    @Step("Hover over column Property Modal name...")
    public void hoverOverPropertyModalColumnName(String columnName, PageModelName pageModelName) {
        final String locator = (getDataTestId(pageModelName) + columnHeaderPropertyModal.replace("{name}", columnName)).trim();
        logger.info("Hovering over column with locator: {}", locator);
        final WebElement column = findElementWithFluentWait(By.xpath(locator));
        hoverOverElement(column);
    }
}