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

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.methodics.phi.util.Constants.OPEN_PERMISSIONS;

public class HierarchyTabPage extends BasePage {
    private WebDriver driver;

    public HierarchyTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//i[contains(@class, 'clear-icon')]")
    private WebElement clearSearchButton;

    @FindBy(xpath = "//input[contains(@class, 'base-input-field__input')][@placeholder='Search IPs']")
    private WebElement searchResourcesInputField;

    @FindBy(xpath = "//button/i[contains(@class,'fa-arrow-down-left-and-arrow-up-right-to-center')]")
    private WebElement collapseTreeButton;

    @FindBy(xpath = "//button/i[contains(@class,'fa-arrow-up-right-and-arrow-down-left-from-center')]")
    private WebElement expandTreeButton;

    @FindBy(xpath = "//div[contains(@class,'column-switcher')]//*[@data-testid='ps-selector-btn-icon']")
    private WebElement columnsButton;

    @FindBy(xpath = "//div[@role='columnheader']")
    private List<WebElement> columns;

    @FindBy(xpath = "//div[@aria-label='Filtering operator']")
    private WebElement filterOptions;

    @FindBy(xpath = "//div[@class='ag-filter-body']//input")
    private WebElement filterInputField;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[@class='ag-body-vertical-scroll-viewport']")
    private WebElement verticalScroll;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCorner;

    @FindBy(xpath = "//i[contains(@class, 'ag-icon-menu')]")
    private WebElement hamburgerMenuButton;

    @FindBy(xpath = "//span[@aria-label='columns']")
    private WebElement manageColumnsMenu;

    @FindBy(xpath = "//input[@aria-label='Filter Columns Input']")
    private WebElement columnSearchField;

    @FindBy(xpath = "//input[@aria-label='Filter from value']")
    private WebElement stringFilterFrom;

    @FindBy(xpath = "//input[@aria-label='Filter to Value']")
    private WebElement stringFilterTo;

    @FindBy(xpath = "//div[contains(@class,'vue-daterange-picker')]")
    private WebElement datepicker;

    @FindBy(xpath = "//td[contains(@class,'today')]")
    private WebElement todayDate;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-from')]")
    private WebElement filterFrom;

    @FindBy(xpath = "//div[contains(@class,'ag-filter-date-to')]")
    private WebElement filterTo;

    @FindBy(xpath = "//span[@aria-label='filter']//span[contains(@class,'icon-filter')]")
    private WebElement filterIcon;

    @FindBy(xpath = "//div[contains(@class,'view-selector')]")
    private WebElement viewSelector;

    @FindBy(xpath = "//div[contains(@class, 'menu-item')]/span[normalize-space()='Tree view']")
    private WebElement treeView;

    @FindBy(xpath = "//div[contains(@class, 'menu-item')]/span[normalize-space()='Flat view']")
    private WebElement flatView;

    @FindBy(xpath = "//*[contains(@class,'view-selector')]//*[contains(@class, 'selected-item')]")
    private WebElement selectedView;

    @FindBy(xpath = "//ul[@class='dropdown-menu show']//li[@class='menu-item']/button[contains(@class, 'menu-item__inner-element_highlight-selection')]")
    private WebElement selectedViewInDropdown;

    @FindBy(xpath = "//div[contains(@class,'ag-header-icon')]/i[contains(@class,'fa-gear icon')]")
    private WebElement fqnConfigButton;

    @FindBy(css = "[data-testid='displayed-level-selector-dropdown-toggle']")
    private WebElement levelsSelector;

    @FindBy(css = "div[data-testid='displayed-level-selector-dropdown'] span[class*='displayed-level-selector']")
    private WebElement levelsSelectorText;

    @FindBy(xpath = "//div[contains(@class, 'displayed-level-selector')]//form[contains(@class, 'dropdown-item menu-item')]")
    private WebElement manualLevelsSelector;

    @FindBy(xpath = "//input[@type='number']")
    private WebElement levelsInput;

    @FindBy(xpath = "//form[@data-testid='menu-item-form' and contains(@class,'highlight-selection')]")
    private WebElement customLevelSelectedInDropdown;

    @FindBy(xpath = "//div[contains(@class, 'align-items-center ')]//span[@class='mdx-ag-paging-panel-number']")
    private WebElement resourcesTotalNumber;

    @FindBy(css = "button[data-testid='ui-btn-displayed-level-selector-alllevels']")
    private WebElement allLevels;

    @FindBy(xpath = "//button[@data-testid='ui-btn-displayed-level-selector-immediatelevels']")
    private WebElement immediateLevels;

    @FindBy(xpath = "//div[contains(@class,'ag-body-viewport')]")
    private WebElement viewport;

    @FindBy(css = "div[data-testid='tree-type-selector-dropdown']")
    private WebElement treeTypeSelector;

    @FindBy(css = "button[data-testid='ui-btn-tree-type-selector-standard-release-tree']")
    private WebElement standardReleaseTreeOption;

    @FindBy(css = "button[data-testid='ui-btn-tree-type-selector-conflict-resolved-tree']")
    private WebElement conflictResolvedTreeOption;

    @FindBy(css = "button[data-testid='tree-type-selector-conflict-resolved-tree'] span.menu-item-text")
    private WebElement conflictResolvedTreeText;

    @FindBy(css = "button[data-testid='tree-type-selector-standard-release-tree'] span.menu-item-text")
    private WebElement standardReleaseTreeText;

    @FindBy(css = "div[data-testid='tree-type-selector-dropdown'] span.tree-type-selector__button-text")
    private WebElement selectedTreeType;

    @FindBy(css ="div[role='rowgroup'] div[row-index]")
    private List<WebElement> resourceCount;

    private final String ipsFlat = "//div[contains(@class, 'flat-grid__row')]";
    private final String rowById = "//div[@class='ag-center-cols-container']//div[@row-index=%s]";
    private String flatViewGeneral = "//span[normalize-space()='{view}']";
    private String arrowButtonExpanded = "//span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eExpanded']";
    private String arrowButtonContracted = "//span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eContracted']";
    private String xpathColumnHeader = "//div[@role='columnheader']//span[normalize-space()='{name}']";
    private String xpathTableIpRow = "//div[@role='row'][contains(., '{ipName}')][contains(., '{propValue}')]";
    // change after PHI-9977
    private String firstColumn = "//*[contains(@class,'ag-column-first')]";
    private String xpathTooltip = "//span[contains(@class,'ag-header-cell')][@title='{tooltip}']";
    private String xpathResourceRow = firstColumn + "//*[normalize-space(.)='{fqn}']";
    private String resources = "//div[@role='rowgroup']//div[@row-index]";
    private String propSetcheckbox = "//label[normalize-space()='{propSetName}']";
    private String secondaryFilterIcon = "//div[normalize-space()='{name}']//i[contains(@class, 'filter')]";
    private String filterOption = "//span[normalize-space()='{option}']";
    private String xpathColumnCheckbox = "//span[normalize-space()='{name}']/preceding-sibling::div";
    private String filterCheckBox = "//div[@data-ref='eCheckbox' and normalize-space()='{value}']";
    private String findAddedGeos = "//div[@col-id='{geoType}']//span[normalize-space()='{geoShortName}'][@class='text-truncate']";
    private String gpsIconColor = "//div[@col-id='{geoType}']//*[@data-color='{geoColorHex}']/following-sibling::span[normalize-space()='{geoName}']";
    private String loadingIcon = "//span[@class='ag-overlay-loading-center']";
    private String xpathResourceRightMenu = "//div[@class='list-group']//div[contains(@class, 'text-truncate')][@title='{name}']";
    private String contextMenuButton = "//a[@title='{ipv}']/following-sibling::div//button[contains(@class,'dropdown-toggle')]";
    private String goToResourceIpvButton = "//span[@title='{fqn}']/ancestor::div[@col-id='ag-Grid-AutoColumn'or @col-id='ipv']//i[contains(@class,'fa-arrow-up-right-from-square')]";
    private String goToResourceHyperlink = xpathResourceRow;
    private String instancesCount = "//a[normalize-space()='{resource}']/../../span[normalize-space()='{count} instances']";
    private String resourceIcon = "//span[contains(@class, '{icon}')]/following-sibling::a[@data-testid='{lib}.{ip}@{version}.{line}']";
    private String resourceIpIcon = "//span[contains(@class, '{icon}')]/following-sibling::*[@title='{lib}.{ip}']";
    private String resourceIplIcon = "//span[contains(@class, '{icon}')]/following-sibling::span[@title='{lib}.{ip}@.{line}']";
    private String xpathConflictRedFlag = "//a[@data-testid='{title}']/preceding-sibling::i[contains(@class, 'fa-flag')]";
    private String collapseIconPresent = "//div[@aria-expanded='true']/span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eExpanded']";
    private String expandIconPresent = "//div[@aria-expanded='false']/span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eExpanded']";
    private String countOfTheIPVs = "//div[@role='row'][contains(., '{name}')]//span[contains(@class,'instances-count-label') and contains(., '{text}')]";
    private String ipNameOptions = "//div[@class='card card-dropdown']//label[normalize-space()='{name}']";
    private String xpathIpName = "//*[contains(@class,'text-truncate ms') and normalize-space()='{name}']";
    private String xpathLevels = "//div[contains(@class, 'menu-item')]/span[normalize-space()='{levels}']";
    private String ipListHeaderRightJustifiedText = "//div[@col-id='group' and contains(@class, 'header')]//span[contains(@class, 'position-static fw-normal')]";
    private String calendarDayAfter = "//div[@class='calendar-table']//following::td[contains(@class,'today') or contains(@class,'weekend')]/following::td[normalize-space()='{dayAfter}']";
    private String calendarYesterday = "//div[@class='calendar-table']//following::td[contains(@class,'today') or contains(@class,'weekend')]/preceding::td[normalize-space()='{yesterday}']";
    private String csvExportButton = "//button[@data-testid='ui-btn-resources-csv-export']";
    private String ipvRowCount = resources + "//span[contains(@class,'badge-ipv')]";
    private String getThreeDotToggle = "(" + ipvRowCount + ")[{row-index}]/ancestor::div[contains(@role,'gridcell')]//button[contains(@class,'dropdown-toggle')]/i";
    private String getThreeDotToggleByFqn = "//*[@data-testid='{fqn}' or @title='{fqn}' ]//ancestor::div[contains(@class,'ip-cell-renderer')]//div[@data-testid='col-sub-menu-btn-container']//i[contains(@class,'ellipsis-vertical')]";
    private String threeDotSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private String threeDotSubMenuItems = threeDotSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";
    private String threeDotSubMenuIcon = threeDotSubMenuItems + "//i[contains(@class,'icon-20x16' )]";

    @Step("Get the number of IPV resources...")
    public int getNumberOfResources(int counter) {
        waitForPageLoaded();
        return waitForNumberOfElementsToBe(By.xpath(resources), counter).size();
    }

    @Step("Get the number of row in hierarchy table ...")
    public int getIpvRowCount() {
        return driver.findElements(By.xpath(ipvRowCount)).size();
    }

    @Step("Verify added geos is displayed...")
    public boolean isAddedGeosPresent(String geoType, String geoShortName) {
        WebElement geos = findElementWithWait(By.xpath(findAddedGeos.replace("{geoType}", geoType).replace("{geoShortName}", geoShortName)));
        return isElementVisible(geos);
    }

    @Step("Verify resources page right menu texts are truncate...")
    public boolean isResourceRightMenuTextsTruncate(String resourceName) {
        WebElement name = findElementWithWait(By.xpath(xpathResourceRightMenu.replace("{name}", resourceName)));
        return isElementVisible(name);
    }

    @Step("Get the number of geos...")
    public int getNumberOfGeos(String geoType, String geoShortName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(findAddedGeos.replace("{geoShortName}", geoShortName)), counter).size();
    }

    @Step("Verify resource: {0} is displayed...")
    public boolean isResourcePresent(String resourceName) {
        waitForPageLoaded();
        WebElement resourceRow = findElementWithWait(By.xpath(xpathResourceRow.replace("{fqn}", resourceName)));
        return isElementVisible(resourceRow);
    }

    @Step("Export in csv format...")
    public void exportInCsvFormat() {
        click(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Hover over CSV Export button...")
    public void hoverOverExportButton() {
        hoverOverElement(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Click on Go To IPV button...")
    public IpDetailsPage goToResourceIpvPage(String fqn) {
        WebElement ipvButton = findElementWithWait(By.xpath(goToResourceIpvButton.replace("{fqn}", fqn)));
        click(ipvButton);
        return new IpDetailsPage(driver);
    }

    @Step("Enter resource name to the search field...")
    public void enterResourceNameToSearchField(String resource) {
        enterTextSlowly(searchResourcesInputField, resource);
    }

    @Step("Remove resource name from the search field...")
    public void removeResourceNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchResourcesInputField);
    }

    @Step("Click on Collapse Resources Tree button...")
    public void clickOnCollapseTree() {
        click(collapseTreeButton);
    }

    @Step("Click on Expand Resources Tree...")
    public void clickOnExpandTree() {
        click(expandTreeButton);
    }

    @Step("Hover over Collapse Resources Tree button...")
    public void hoverOverCollapseTreeButton() {
        hoverOverElement(collapseTreeButton);
    }

    @Step("Hover over Expand Resources Tree button...")
    public void hoverOverExpandTreeButton() {
        hoverOverElement(expandTreeButton);
        DriverFactory.sleep(500);
    }

    @Step("Hover over column name...")
    public void hoverOverColumnName(String columnName) {
        WebElement column = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", columnName)));
        hoverOverElement(column);
    }

    @Step("Verify column {0} is not displayed...")
    public boolean columnNotDisplayed(String column) {
        List<WebElement> columnHeaders = driver.findElements(By.xpath(xpathColumnHeader.replace("{name}", column)));
        return columnHeaders.size() == 0;
    }

    @Step("Verify tooltip is present...")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip.replace("{tooltip}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Verify resource conflict red flag is not visible...")
    public int isResourceConflictRedFlagVisible(String resourceName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathConflictRedFlag.replace("{title}", resourceName)), counter).size();
    }

    @Step("Click on Arrow button expanded...")
    public void clickOnArrowButtonExpanded(String name) {
        WebElement arrowButton = findElementWithWait(By.xpath(arrowButtonExpanded.replace("{name}", name)));
        click(arrowButton);
    }

    @Step("Clicking on Arrow button contracted...")
    public void clickOnArrowButtonContracted(String name) {
        WebElement arrowButton = findElementWithWait(By.xpath(arrowButtonContracted.replace("{name}", name)));
        click(arrowButton);
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName) {
        clickOnColumnsButton();
        WebElement checkbox = findElementWithWait(By.xpath(propSetcheckbox.replace("{propSetName}", propSetName)));
        click(checkbox);
        clickOnColumnsButton();
    }

    @Step("deselect property set {0}...")
    public void deselectPropertySet(String propSetName) {
        logger.info("Deselecting property set: " + propSetName);
        clickOnColumnsButton();
        WebElement checkbox = waitForElementToBePresentFluentWait(By.xpath((propSetcheckbox + "/../input").replace("{propSetName}", propSetName)));
        if (checkbox.isSelected()) {
            logger.info("Checkbox is selected, clicking to deselect it.");
            WebElement checkbox1 = findElementWithWait(By.xpath(propSetcheckbox.replace("{propSetName}", propSetName)));
            click(checkbox1);
        }
        clickOnColumnsButton();
    }


    @Step("Get number of prop sets from the dropdown list...")
    public int getNumberOfPropertySets(String propSetName) {
        return driver.findElements(By.xpath(propSetcheckbox.replace("{propSetName}", propSetName))).size();
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton() {
        click(columnsButton);
    }

    @Step("Get the number of columns...")
    public int getNumberOfColumns() {
        return getNumberOfVisibleElements(columns);
    }

    @Step("Click on column to sort...")
    public void clickOnColumnToSort(String columnName) {
        WebElement column = findElementWithWait(By.xpath(xpathColumnHeader.replace("{name}", columnName)));
        click(column);
    }

    @Step("Verify property column header is displayed...")
    public boolean isColumnHeaderPresent(String name) {
        WebElement columnHeader = waitForElementToBePresent(By.xpath(xpathColumnHeader.replace("{name}", name)));
        return isElementVisible(columnHeader);
    }

    @Step("Verify property column header is hide")
    public boolean isColumnHeaderHidden(String name) {
        List<WebElement> columnHeader = driver.findElements(By.xpath(xpathColumnHeader.replace("{name}", name)));
        return columnHeader.size() == 0;
    }

    @Step("Verify IP property value is displayed in Resources tab...")
    public boolean isPropValueDisplayedInTable(String ipName, String propValue) {
        WebElement ipPropValue = findElementWithWait(By.xpath(xpathTableIpRow
                .replace("{ipName}", ipName).replace("{propValue}", propValue)));
        return isElementVisible(ipPropValue);
    }

    @Step("Verify geo icon color...")
    public boolean isGpsIconColorDisplayed(String geoType, String geoName, String geoIconColorHex) {
        WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsIconColor.replace("{geoType}", geoType).replace("{geoName}", geoName).replace("{geoColorHex}", geoIconColorHex)));
        return isElementVisible(gpsColor);
    }

    @Step("Click on column filter icon...")
    public void clickOnFilterIcon(String column) {
        WebElement filterIcon = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
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

    @Step("Check column checkbox...")
    public void addColumn(String column) {
        hoverOverColumnName(column);
        click(hamburgerMenuButton);
        click(manageColumnsMenu);
        inputText(columnSearchField, column);
        WebElement columnCheckbox = findElementWithWait(By.xpath(xpathColumnCheckbox.replace("{name}", column)));
        click(columnCheckbox);
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Insert from and to value...")
    public void typeFromAndToValues(String fromValue, String toValue) {
        inputText(stringFilterFrom, fromValue);
        inputText(stringFilterTo, toValue);
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
        LocalDate today = LocalDate.now();
        String tomorrow = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
        String[] dateParts = tomorrow.split("-");
        logger.info(dateParts[2]);
        String previousDay = ((dateParts[2].startsWith("0")) ? dateParts[2].substring(1) : dateParts[2]);
        WebElement nextDay = findElementWithWait(By.xpath(calendarDayAfter.replace("{dayAfter}", previousDay)));
        click(nextDay);
    }

    @Step("Select yesterday date...")
    public void selectDayBefore() {
        LocalDate today = LocalDate.now();
        String yesterday = (today.minusDays(1)).format(DateTimeFormatter.ISO_DATE);
        String[] dateParts = yesterday.split("-");
        logger.info(dateParts[2]);
        String previousDay = ((dateParts[2].startsWith("0")) ? dateParts[2].substring(1) : dateParts[2]);
        WebElement dayBefore = findElementWithWait(By.xpath(calendarYesterday.replace("{yesterday}", previousDay)));
        click(dayBefore);
    }

    @Step("Click on the calendar icon from...")
    public void clickOnCalendarIconFrom() {
        click(filterFrom);
    }

    @Step("Click on the calendar icon to...")
    public void clickOnCalendarIconTo() {
        click(filterTo);
    }

    @Step("Open resource IPV in a new tab...")
    public IpDetailsPage openIpvInANewTab(String fqn) throws AWTException {
        WebElement gotoIpvButton = findElementWithWait(By.xpath(goToResourceIpvButton.replace("{fqn}", fqn)));
        gotoIpvButton.click();
        return new IpDetailsPage(driver);
    }

    @Step("Open resource IP in a Same tab by Hyperlink...")
    public IpDetailsPage openIpHyperlinkInSameTab(String fqn) {
        findElementWithWait(By.xpath(goToResourceHyperlink.replace("{fqn}", fqn))).click();
        return new IpDetailsPage(driver);
    }

    @Step("Scroll to the right...")
    public void scrollRight() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth", horizontalScroll);
    }

    @Step("Scroll to the left...")
    public void scrollLeft() {
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForLoadingSpinner() {
        driver.findElements(By.xpath(loadingIcon));
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForDataToLoad() {
        waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0);
    }

    @Step("Hover over ip and click on context menu")
    public void hoverOverIp(String ipv) {
        DriverFactory.sleep(3000);
        WebElement parentIp = findElementWithWait(By.xpath(xpathResourceRow.replace("{fqn}", ipv)));
        hoverOverElement(parentIp);
    }

    @Step("Click on Resources view selector...")
    public void clickOnViewSelector() {
        click(viewSelector);
    }

    @Step("Hover over selected resources view...")
    public void hoverViewSelector() {
        hoverOverElement(selectedView);
    }

    @Step("Select Flat view...")
    public void selectFlatView(String view) {
        clickOnViewSelector();
        hoverOverElement(flatView);
        WebElement flatViewSelect = findElementWithWait(By.xpath(flatViewGeneral.replace("{view}", view)));
        click(flatViewSelect);
    }

    @Step("Select Tree view...")
    public void selectTreeView() {
        click(treeView);
    }

    @Step("Get selected resources view")
    public String getSelectedView() {
        return getText(selectedView);
    }

    @Step("Get selected resources view in the dropdown")
    public String getSelectedViewInDropdown() {
        DriverFactory.sleep(500);
        return getText(selectedViewInDropdown);
    }

    @Step("Verify number of resource instances")
    public boolean isNumberOfInstancesCorrect(String ipv, String count) {
        WebElement instances = findElementWithWait(By.xpath(instancesCount.replace("{resource}", ipv).replace("{count}", count)));
        return isElementVisible(instances);
    }

    @Step("Verify collapse tree node button is displayed...")
    public boolean isCollapseTreeNodeBtnDisplayed(String name) {
        List<WebElement> collapseBtn = driver.findElements(By.xpath(arrowButtonExpanded.replace("{name}", name)));
        return collapseBtn.size() != 0;
    }

    @Step("Verify IPV icon is displayed...")
    public boolean isResourceIconDisplayed(String icon, String libName, String ipName, String version, String line) {
        WebElement ipvIcon = findElementWithWait(By.xpath(resourceIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line).replace("{version}", version)));
        return isElementVisible(ipvIcon);
    }

    @Step("Verify IP icon is displayed...")
    public boolean isResourceIpIconDisplayed(String icon, String libName, String ipName) {
        WebElement ipvIcon = findElementWithWait(By.xpath(resourceIpIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName)));
        return isElementVisible(ipvIcon);
    }

    @Step("Verify IPL icon is displayed...")
    public boolean isResourceIplIconDisplayed(String icon, String libName, String ipName, String line) {
        WebElement ipvIcon = findElementWithWait(By.xpath(resourceIplIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line)));
        return isElementVisible(ipvIcon);
    }

    @Step("Clicking on clear search button")
    public void clickOnClearSearchButton() {
        hoverOverElement(searchResourcesInputField);
        click(clearSearchButton);
    }

    @Step("Verify expand icon is displayed...")
    public boolean isExpandIconDisplayed(String name) {
        List<WebElement> expandIcon = driver.findElements(By.xpath(expandIconPresent.replace("{name}", name)));
        return expandIcon.size() != 0;
    }

    @Step("Verify collapse icon is displayed...")
    public boolean isCollapseIconDisplayed(String name) {
        List<WebElement> collapseIcon = driver.findElements(By.xpath(collapseIconPresent.replace("{name}", name)));
        return collapseIcon.size() != 0;
    }

    @Step("Verify expected IPV count Text is displayed...")
    public boolean isExpectedTextCountOfIPVsPresent(String ipName, String expectedText) {
        WebElement ipvCount = findElementWithWait(By.xpath(countOfTheIPVs.replace("{name}", ipName).replace("{text}", expectedText)));
        return isElementVisible(ipvCount);
    }

    @Step("Click on display setting button")
    public void clickOnDisplaySettingsButton(String value) {
        hoverOverColumnName(value);
        waitForElementToBeClickable(fqnConfigButton).click();
    }

    @Step("Verify on permission setting button visible")
    public boolean ipTreeOptionsAvailable(String value) {
        WebElement ipTreeOptionsElements = findElementWithWait(By.xpath(ipNameOptions.replace("{name}", value)));
        return isElementVisible(ipTreeOptionsElements);
    }

    @Step("Click on button for uncheck")
    public void ipTreeOptionsButtonClick(String value) {
        WebElement ipTreeButton = findElementWithWait(By.xpath(ipNameOptions.replace("{name}", value)));
        click(ipTreeButton);
    }

    @Step("Verify IP name format is correct")
    public boolean isFqnFormatCorrect(String value) {
        WebElement ipTreeButton = findElementWithWait(By.xpath(xpathIpName.replace("{name}", value)));
        return isElementVisible(ipTreeButton);
    }

    @Step("Hover over levels selector...")
    public void hoverLevelsSelector() {
        hoverOverElement(levelsSelector);
    }

    @Step("Open levels dropdown")
    public void openLevelsDropdown() {
        click(levelsSelector);
    }

    @Step("Get selected level...")
    public String getSelectedLevels() {
        return getText(levelsSelectorText).trim();
    }

    @Step("Select All levels...")
    public void selectAllLevels() {
        openLevelsDropdown();
        click(allLevels);
    }

    @Step("Select Immediate levels...")
    public void selectImmediateLevels() {
        openLevelsDropdown();
        click(immediateLevels);
    }

    @Step("Select manual levels picker...")
    public void selectManualLevelsPicker() {
        clickWithJS(manualLevelsSelector);
    }

    @Step("Hover over levels manual selector...")
    public void hoverLevelsManualSelector() {
        hoverOverElement(manualLevelsSelector);
    }

    @Step("Type number of resources levels...")
    public void enterNumberOfLevels(String levels) {
        DriverFactory.sleep(500);
        clearInputFieldWithBackspace(levelsInput);
        enterTextSlowly(levelsInput, levels);
    }

    @Step("Set number of resources levels...")
    public void setLevel(String levels) {
        openLevelsDropdown();
        enterNumberOfLevels(levels);
        DriverFactory.sleep(500);
        selectManualLevelsPicker();
    }

    @Step("Select level by hitting Enter...")
    public void levelsInputPressEnter() {
        click(levelsInput);
        pressEnterKey(levelsInput);
    }

    @Step("Get number of resources levels...")
    public String getLevelNumber() {
        return getAttribute(levelsInput);
    }

    @Step("Verify manual level selecor is selected in dropdown")
    public boolean isCustomLevelSelectedInDropdwon() {
        return isElementVisible(customLevelSelectedInDropdown);
    }

    @Step("Get total number of resources on all pages...")
    public String getNumberOfResourcesAllPages() {
        return getText(resourcesTotalNumber);
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForLazyLoadFlatView() {
        return getNumberOfRowsForLazyLoad(ipsFlat, rowById, 10);
    }

    @Step("Get total number of resources with scrolling...")
    public int getNumberOfAllResources() {
        waitForPageLoaded();
        Set<String> uniqueResources = new HashSet<>();
        int previousCount = 0;
        int currentCount = 0;

        // Scroll to top first
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop = 0;", viewport);

        do {
            previousCount = currentCount;
            DriverFactory.sleep(1000);// Need to Improve
            List<WebElement> visibleResources = waitForElementsToBeVisible(resourceCount);
            for (WebElement resource : visibleResources) {
                String resourceId = resource.getAttribute("row-index");
                if (resourceId != null && !resourceId.isEmpty()) {
                    uniqueResources.add(resourceId);
                }
            }

            currentCount = uniqueResources.size();
            // Scroll by one viewport
            js.executeScript("arguments[0].scrollTop = arguments[0].scrollTop + arguments[0].clientHeight;", viewport);

            // Small pause so the grid can render the next batch
            DriverFactory.sleep(500);

        } while (currentCount > previousCount);
        scrollToTop();
        return uniqueResources.size();

    }


    @Step("Get IP List header right-justified text")
    public String getIpListHeaderRightJustifiedText() {
        WebElement ipListHeaderRightText = findElementWithFluentWait(By.xpath(ipListHeaderRightJustifiedText));
        return getText(ipListHeaderRightText);
    }

    @Step("Verify IP List header right-justified text present...")
    public boolean isIpListHeaderRightJustifiedTextPresent() {
        List<WebElement> ipListHeaderRightText = findElementsWithFluentWait(By.xpath(ipListHeaderRightJustifiedText));
        return !ipListHeaderRightText.isEmpty();
    }

    @Step("Verify IP List header right-justified text disappear after hovering ...")
    public boolean ipListHeaderRightJustifiedTextDisappearAfterHover() {
        WebElement ipListHeaderRightText = findElementWithFluentWait(By.xpath(ipListHeaderRightJustifiedText));
        hoverOverElement(ipListHeaderRightText);
        return !isIpListHeaderRightJustifiedTextNotPresent();
    }

    @Step("Verify property column header is displayed...")
    public boolean isColumnHeaderPresentWithScrolls(String name) {
        if (!isColumnHeaderPresent(name)) {
            scrollRight();
        } else if (!isColumnHeaderPresent(name)) {
            scrollLeft();
        }
        return isColumnHeaderPresent(name);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    @Step("is three dot sub menu displayed for IP")
    public boolean isThreeDotForSubMenuDisplayedForIp(String rowIndex) {
        return isElementVisible(getThreeDotToggle.replace("{row-index}", rowIndex));
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenu(String rowIndex) {
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(getThreeDotToggle.replace("{row-index}", rowIndex)))).click();
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenuByIPV(String fqn) {
        final String locator = getThreeDotToggleByFqn.replace("{fqn}", fqn);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator))).click();
    }

    @Step("Close three dot sub menu option if open")
    public void closeThreeDotSubmenuIfOpen(String rowIndex) {
        if (isElementVisible(threeDotSubMenu)) {
            clickOnThreeDotSubmenu(rowIndex);
        }
    }

    @Step("is three dot sub menu displayed for IP")
    public boolean isSubmenuOpen() {
        return isElementVisible(threeDotSubMenu);
    }

    @Step("is three dot sub menu option displayed for IP")
    public boolean isSubmenuPresent(String menuItemName) {
        return isElementVisible(threeDotSubMenuItems.replace("{menuOption}", menuItemName));
    }

    @Step("is three dot sub menu option icon displayed for IP")
    public boolean isSubmenuIconPresent(String menuItemName) {
        return isElementVisible(threeDotSubMenuIcon.replace("{menuOption}", menuItemName));
    }

    @Step("Click on Ipv hyperlink")
    public void clickIpvHasHyperlink(String fqn) {
        final WebElement ipvHyperlink = findElementWithWait(By.xpath(goToResourceHyperlink.replace("{fqn}", fqn)));
        scrollToElement(ipvHyperlink);
        click(ipvHyperlink);
    }

    @Step("Click on item from three dot sub menu")
    public void clickOnThreeDotSubMenuItem(String menuItemName) {
        final WebElement menuItem = findElementWithFluentWait(By.xpath(threeDotSubMenuItems.replace("{menuOption}", menuItemName)));
        click(menuItem);
    }

    @Step("Click on Resource Permissions")
    public void openResourcePermissions(String fqnName) {
        clickOnThreeDotSubmenuByIPV(fqnName);
        clickOnThreeDotSubMenuItem(OPEN_PERMISSIONS);
    }

    @Step("Scroll to the top of the hierarchy table...")
    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop = 0;", viewport);
    }

    @Step("Scrolling down using grid body element...")
    public void scrollDownInGrid() {
        if (verticalScroll.isDisplayed()) {
            dragAndDropElement(verticalScroll, scrollRightCorner);
        } else {
            logger.info("Scrolling not needed.");
        }

    }

    @Step("Verify that tree type selector is displayed...")
    public boolean isTreeTypeSelectorDisplayed() {
        return isElementVisible(treeTypeSelector);
    }

    @Step("Clicking on tree type selector...")
    public void clickOnTreeTypeSelector() {
        click(treeTypeSelector);
    }

    @Step("Get standard release tree text...")
    public String getStandardReleaseTreeText() {
        return getText(standardReleaseTreeText);
    }

    @Step("Get conflict resolved tree text...")
    public String getConflictResolvedTreeText() {
        return getText(conflictResolvedTreeText);
    }

    @Step("Get selected tree type text")
    public String getSelectedTreeTypeText() {
        return getText(selectedTreeType);
    }

    @Step("Select standard release tree option...")
    public void selectStandardReleaseTreeOption() {
        click(standardReleaseTreeOption);
    }

    @Step("Select conflict resolved tree option...")
    public void selectConflictResolvedTreeOption() {
        click(conflictResolvedTreeOption);
    }

    @Step("Verify IP List header right-justified text present...")
    public boolean isIpListHeaderRightJustifiedTextNotPresent() {
        return isElementVisible(ipListHeaderRightJustifiedText);

    }
}