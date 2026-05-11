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

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import static com.methodics.phi.util.Constants.REMOVE;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AddResourcesTable extends BasePage {

    private final WebDriver driver;

    public AddResourcesTable(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@data-testid='modal-resources']")
    private WebElement resourcesModal;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//input[contains(@class,'base-input-field__input')]")
    private WebElement ipSearchField;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//h5[normalize-space()='Add resources']")
    private WebElement resourceHeader;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//div[contains(@class,'dropdown b-dropdown column-switcher')]")
    private WebElement columnSwitcher;

    @FindBy(xpath = "//button/i[@class='fa-solid fa-plus']")
    private WebElement plusButton;

    @FindBy(xpath = "//button/i[@class='fa-solid fa-minus']")
    private WebElement minusButton;

    @FindBy(xpath = "//button/i[@class='fa-solid fa-pen']")
    private WebElement editButton;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//button[normalize-space()='Add']")
    private WebElement addButton;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//button[normalize-space()='Confirm']")
    private WebElement confirmButton;

    @FindBy(xpath = "//*[@data-testid='ui-btn-resources-modal-cancel']")
    private WebElement resourceModelCancelButton;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//*[@data-testid='ui-btn-modal-header-close']")
    private WebElement closeIcon;

    @FindBy(xpath = "//div[contains(@class,'tab-resources__overlay__subtitle')][contains(., 'It looks like you have no resources added.')]")
    private WebElement noResourcesAddedMessage;

    @FindBy(xpath = "//span[@class='badge font-weight-normal badge-info']")
    private WebElement resourcesCountIcon;

    @FindBy(xpath = "//footer[contains(@id,'modal-resources')]//span[1]")
    private WebElement resourcesCountFooterLabel;

    @FindBy(xpath = "//a[@role='tab' and @aria-selected='true' and text()='IP Lines & Versions']")
    private WebElement linesAndVersionsTabSelected;

    @FindBy(xpath = "//a[@role='tab' and @aria-selected='false' and text()='IP Lines & Versions']")
    private WebElement linesAndVersionsTab;

    @FindBy(xpath = "//a[@role='tab' and text()='Unique aliases']")
    private WebElement uniqueAliasesTab;

    @FindBy(xpath = "//div[contains(@class,'text-center') and contains(., 'No unique aliases')]")
    private WebElement noUniqueAliasesImage;

    @FindBy(xpath = "//input[@placeholder='Search IPs']")
    private WebElement searchInputField;

    @FindBy(xpath = "//span[input[@placeholder='Search IPs']]/following-sibling::div//i")
    private WebElement searchInputFieldIcon;

    @FindBy(xpath = "/input[@type='checkbox']")
    private WebElement privateIpCheckbox;

    @FindBy(xpath = "//input[@disabled='disabled']/following-sibling::label[normalize-space()='Private resource']")
    private WebElement privateIpCheckboxDisabled;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//span[text()='Pin Column']")
    private WebElement pinColumnMenu;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//span[text()='Pin Right']")
    private WebElement pinRightButton;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//div[@class='ag-filter-body']//input")
    private WebElement filterInputField;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//div[contains(@class,'ag-horizontal-left-spacer')]")
    private WebElement scrollLeftCorner;

    @FindBy(xpath = "//*[contains(@class, 'no-rows-overlay__icon')]/../h5[contains(.,'No results found')]")
    private WebElement noIpFoundIcon;

    @FindBy(xpath = "//footer//span[1]")
    private WebElement footerIpSelectedCountLabel;

    @FindBy(css = "div[class='tooltip-inner']")
    private WebElement disableIpTooltip;

    @FindBy(xpath = "//div[@role='columnheader']")
    private WebElement columnName;

    @FindBy(css = "ul.dropdown-menu.show a[data-testid=aliases-dropdown-tab]")
    private WebElement aliasesTabInVersion;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll']//div[@class='ag-horizontal-right-spacer ag-scroller-corner']")
    private WebElement rightScrollbutton;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll']//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement scrollLine;

    private final String resourceIpRowId = "//div[@row-id='{fqn}']";
    private final String resoucesTab = "//*[@id='tabpanel-resource-browser-tab']";
    private final String disableIpCheckBoxRowId = resourceIpRowId + "//input[@type='checkbox' and @disabled]/..";
    private final String resourceIpRowIndex = resoucesTab + "//div[@row-index='{rowId}']";
    private final String addAsPrivateResourceIcon = resourceIpRowIndex + "//i[@aria-label='Add as private resource']";
    private final String privateResourceIcon = resourceIpRowIndex + "//i[@aria-label='Private resource']";
    private final String ipCheckBoxRowId = resourceIpRowIndex + "//input[@type='checkbox']/..";
    private final String ipCheckedBoxRowId = resoucesTab + "//div[@row-index='{rowId}' and contains(@class,'ag-row-selected')]//input[@type='checkbox']/..";
    private final String getCellValueByID = resourceIpRowIndex + "//div[@col-id='{cellId}']";
    private final String columnHeader = "//*[@data-testid='modal-resources']//div[@role='columnheader'][@col-id='{name}']";
    private final String columnHeaders = "//*[@data-testid='modal-resources']//div[@role='columnheader']";
    private final String pinnedRightColumn = "//*[@data-testid='modal-resources']//div[@class='ag-pinned-right-header']//div[@col-id='{name}']";
    private final String secondaryFilterIcon = "//*[@data-testid='modal-resources']//div[@col-id='{name}']//i[contains(@class, 'filter')]";
    private final String resizeColumnButton = "//*[@data-testid='modal-resources']//div[@col-id='{name}']/div[contains(@class, 'resize')]";
    private final String columnCheckboxUnchecked = "//*[@data-testid='modal-resources']//span[normalize-space()='{name}']/parent::div//div[contains(@class, 'checkbox-input')]";
    private final String xpathTooltip = "//*[@data-testid='modal-resources']//div[@class='tooltip-inner' and text()='{tooltip}']";
    private final String isIpSelectAsPrivateResource = "//span[normalize-space()='{name}']//ancestor::div[contains(@class,'ag-row ag-row')]//i[@aria-label='Private resource']";
    private final String selectRowByName = "//span[normalize-space()='{name}']//ancestor::div[contains(@class,'ag-row ag-row')]";
    private final String addAsPrivateResourceIconByName = selectRowByName + "//i[@aria-label='Add as private resource']";

    private final String getNameColumn = resourceIpRowId + "//div[@col-id='name']";
    private final String getCellValueByRowID = resourceIpRowId + "//div[@col-id='{cellId}']";
    private final String rowIpCheckbox = resourceIpRowId + "//input[@type='checkbox']/..";
    private final String rowIpCheckbox_disable = resourceIpRowId + "[contains(@class,'row-disabled')]//input";
    //LineAction
    private final String rowIpLine = resourceIpRowId + "//div[@col-id='selectLine']";
    private final String rowIpLineText = rowIpLine + "//span";
    private final String rowIpLineDropDown_icon = rowIpLine + "/div";
    private final String rowIpLineDropDown_searchBox = rowIpLine + "//input[@placeholder='Search IP Lines']";
    private final String rowIpLineDropDown_search_list = rowIpLine + "//ul[@class='dropdown-menu show']//li";
    private final String RowIpLineDropDown_search_list_item = rowIpLine + "//ul[@class='dropdown-menu show']//li[contains(.,'{lineName}')]";
    private final String RowIpLineDropDown_search_list_infoIcon = RowIpLineDropDown_search_list_item + "//i";
    private final String rowIpLineRemoveIcon_confirmation = resourceIpRowId + "//i[@title='Remove']";
    private final String getAnyCellValueByRowID = "(" + resourceIpRowId + "//div[@col-id='{col_Id_name}']//*[@title])[last()]";
    private final String rowIpVersionBase = resourceIpRowId + "//div[@col-id='versionCell']";
    private final String rowIpVersionDropDown_icon = rowIpVersionBase + "/div/i";
    private final String rowIpVersionDropDown_searchBox = "//*[@class='dropdown-menu show']//div[contains(@class,'active')]//input[@type='text']";
    private final String RowIpVersionDropDown_search_list_item = "//*[@class='dropdown-menu show']//div[contains(@class,'active')]//li[contains(.,'{version}')]";
    private final String resourceIpCheckbox = "//span[text()='{fqn}']/parent::div//parent::span//div[contains(@class, 'form-check')]";
    private String getThreeDotToggleByFqn = "//div[@row-id='{fqn}']//div[@data-testid='col-sub-menu-btn-container']//button[@data-testid='ui-dropdown-btn-toggle']/i[@data-testid='fav-record-action-btn']";
    private String threeDotSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private String threeDotSubMenuItems = threeDotSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";
    private String threeDotSubMenuIcon = threeDotSubMenuItems + "//i[contains(@class,'icon-20x16' )]";

    @Step("get cell value by RowID...")
    public String getCellValueByRowIndex(String rowId, String cellId) {
        return findElementWithWait(By.xpath(getCellValueByID.replace("{rowId}", rowId).replace("{cellId}", cellId))).getText();
    }

    @Step("get cell value by RowID...")
    public String getAnyCellValuesByRowID(String rowId, String cellId) {
        logger.info("Entering getCellValueByRowID method with rowId: {} and cellId: {} ", rowId, cellId);
        final WebElement element = waitForElementToBePresent(By.xpath(getAnyCellValueByRowID.replace("{fqn}", rowId).replace("{col_Id_name}", cellId)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        final String actualValue = element.getText().trim();
        logger.info("Retrieved cell value for rowId: {} and cellId: {} - Value: {}", rowId, cellId, actualValue);
        return actualValue;
    }

    @Step("get cell value by RowID...")
    public String getAnyCellValuesByRowID(String rowId, String cellId, FiledRendererType filedType) {
        logger.info("Entering getCellValueByRowID method with fqn: {} , cellId: {} and filedType: {}", rowId, cellId, filedType);
        final Actions action = new Actions(DriverFactory.getBrowserInstance());
        action.sendKeys(Keys.ARROW_RIGHT).perform();
        //  final String elementSelector = getFiledTypeSelector(getCellValueByRowID, filedType); // will remove this line after testing the new method
        final WebElement element = driver.findElement(By.xpath(getCellValueByRowID.replace("{fqn}", rowId).replace("{cellId}", cellId)));
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            waitForElementToBeVisible(element);
        } catch (Exception e) {
            logger.info("Element is not visible...");
            logger.info("Retrieved cell value for rowId: {} and cellId: {} - Value: {}", rowId, cellId);
            return "";
        }
        final String actualValue = element.getText().trim();
        logger.info("Retrieved cell value for rowId: {} and cellId: {} - Value: {}", rowId, cellId, actualValue);
        return actualValue;
    }

    @Step("Opening IP line dropdown for FQN: {fqn}")
    public void openIpLineDropDown(String fqn) {
        final WebElement element = findElementWithFluentWait(By.xpath(rowIpLineDropDown_icon.replace("{fqn}", fqn)));
        scrollToElement(element);
        DriverFactory.sleep(1000);
        final WebElement element2 = findElementWithFluentWait(By.xpath(rowIpLineDropDown_icon.replace("{fqn}", fqn)));
        scrollToElement(element2);
        waitTillClickableWithFluentWait(element2);
        hoverOverElement(element2);
        doubleClickOnElement(element2);
        logger.info("Opened IP line dropdown for FQN: " + fqn);
    }

    @Step("Opening IP Version dropdown for FQN: {fqn}")
    public void openVersionDropDown(String fqn) {
        logger.info(rowIpVersionDropDown_icon);
        final WebElement element = waitForElementToBePresent(By.xpath(rowIpVersionDropDown_icon.replace("{fqn}", fqn)));
        scrollToElement(element);
        waitTillClickableWithFluentWait(element);
        doubleClickOnElement(waitForElementToBeVisible(element));
        logger.info("Opened IP Version dropdown for FQN: " + fqn);
    }

    @Step("Opening IP line dropdown for FQN: {fqn}")
    public void updateLineToIp(String fqn, String lineName) {
        openIpLineDropDown(fqn);
        enterTextLineDropDownSearchBox(fqn, lineName);
        DriverFactory.sleep(1000);
        selectIpLineFromLineDropDownToIp(fqn, lineName);
    }

    @Step("Change IP Version dropdown for FQN: {fqn}")
    public void updateVersionToIp(String fqn, String version) {
        logger.info("Enter into updateVersionToIp FQN {} , {}", fqn, version);
        openVersionDropDown(fqn);
        enterVersionNumberSlowly(fqn, version);
        DriverFactory.sleep(1000);
        selectIpVersionFromLineDropDownToIp(fqn, version);
    }

    @Step("Change IP Version dropdown for FQN: {fqn}")
    public void updateAliasesVersionToIp(String fqn, String version) {
        logger.info("Enter into updateAliasesVersionToIp FQN {} , {}", fqn, version);
        openVersionDropDown(fqn);
        clickOnAliasesTabInVersion();
        enterVersionNumberSlowly(fqn, version);
        DriverFactory.sleep(1000);
        selectIpVersionFromLineDropDownToIp(fqn, version);
    }

    @Step("Click on Aliases tab in version dropdown...")
    public void clickOnAliasesTabInVersion() {
        waitTillClickableWithFluentWait(aliasesTabInVersion);
        aliasesTabInVersion.click();
    }

    @Step("Click to IP Checkbox...")
    public void clickOnIpCheckBoxByFqn(String... fqns) {
        for (final String fqn : fqns) {
            if (!isIpCheckBoxSelected(fqn)) {
                final WebElement element = findElementWithFluentWait(By.xpath(rowIpCheckbox.replace("{fqn}", fqn)));
                waitTillClickableWithFluentWait(element);
                element.click();
                logger.info("Clicked on IP CheckBox with FQN: {} ", fqn);
            }
        }
        DriverFactory.sleep(2000);
    }

    @Step("Check if IP check box is selected...")
    public boolean isIpCheckBoxSelected(String fqn) {
        try {
            final WebElement row = findElementWithFluentWait(By.xpath(resourceIpRowId.replace("{fqn}", fqn)));
            waitTillVisibleWithFluentWait(row);
            final boolean isSelected = row.getAttribute("class").contains("ag-row-selected");
            logger.info("Current checkbox state for FQN {}: {}", fqn, isSelected);
            return isSelected;
        } catch (NoSuchElementException e) {
            logger.info("Element is not visible...");
            return false;
        }
    }

    @Step("Check if IP check box is selected...")
    public void isIpCheckBoxByIpName(String ipName) {
        final WebElement rowCheckBox = findElementWithFluentWait(By.xpath(resourceIpCheckbox.replace("{fqn}", ipName)));
        waitTillVisibleWithFluentWait(rowCheckBox).click();
    }

    @Step("Check if row is visible...")
    public boolean isRowVisible(String fqn) {
        if (isElementVisible(getNameColumn.replace("{fqn}", fqn))) {
            logger.info("{} row is visible", fqn);
            return true;
        }
        logger.info("{} row is not visible", fqn);
        return false;
    }

    @Step("unchecked to IP Checkbox...")
    public void clickOnIpUnCheckedBoxByFqn(String... fqns) {
        for (final String fqn : fqns) {
            if (isIpCheckBoxSelected(fqn)) {
                final WebElement element = waitForElementToBePresent(By.xpath(rowIpCheckbox.replace("{fqn}", fqn)));
                element.click();
                logger.info("unchecked on IP CheckBox with FQN: {} ", fqn);
            }
        }
    }

    @Step("verify all Ip's unchecked ...")
    public boolean areAllIpsCheckboxesUnchecked(String... fqns) {
        boolean allUnchecked = true;
        for (final String fqn : fqns) {
            final WebElement row = waitForElementToBePresent(By.xpath(resourceIpRowId.replace("{fqn}", fqn)));
            final boolean isRowSelected = row.getAttribute("class").contains("ag-row-selected");
            logger.info("is checkbox state for FQN {}: {}", fqn, isRowSelected);
            if (isRowSelected) {
                allUnchecked = false;
            }
        }
        return allUnchecked;
    }

    @Step("verify all Ip's selected ...")
    public boolean areAllCheckboxesSelected(String... fqns) {
        boolean allUnchecked = true;
        for (final String fqn : fqns) {
            final WebElement row = waitForElementToBePresent(By.xpath(resourceIpRowId.replace("{fqn}", fqn)));
            final boolean isRowSelected = row.getAttribute("class").contains("ag-row-selected");
            logger.info("is checkbox state for FQN {}: {}", fqn, isRowSelected);
            if (!isRowSelected) {
                allUnchecked = false;
            }
        }
        return allUnchecked;
    }

    @Step("selecting line: {lineName} for opened IP line dropdown for FQN: {fqn} ")
    private void selectIpLineFromLineDropDownToIp(String fqn, String lineName) {
        logger.info("Before selecting line: {lineName} for opened IP line dropdown for FQN: {fqn}", lineName, fqn);
        final WebElement element = waitForElementToBePresentFluentWait(By.xpath(RowIpLineDropDown_search_list_item.replace("{fqn}", fqn).replace("{lineName}", lineName)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        click(element);
        logger.info("After selecting line: {lineName} for opened IP line dropdown for FQN: {fqn}", lineName, fqn);
    }

    @Step("Checking if IP line dropdown search box is present for FQN: {fqn}")
    public boolean isRowIpLineDropDownSearchBoxPresent(String fqn) {
        final WebElement element = waitForElementToBePresent(By.xpath(rowIpLineDropDown_searchBox.replace("{fqn}", fqn)));
        final boolean isPresent = isElementVisible(element);
        logger.info("IP line dropdown search box presence for FQN " + fqn + ": " + isPresent);
        return isPresent;
    }

    @Step("Entering text slowly into IP line dropdown search box for FQN: {fqn}")
    public void enterTextLineDropDownSearchBox(String fqn, String lineName) {
        final WebElement element = waitForElementToBePresent(By.xpath(rowIpLineDropDown_searchBox.replace("{fqn}", fqn)));
        element.clear();
        enterTextSlowly(waitForElementToBeVisible(element), lineName);
        logger.info("Entered text slowly into IP line dropdown search box for FQN: " + fqn);
    }

    @Step("Enter version into the search field slowly...")
    public void enterVersionNumberSlowly(String fqn, String version) {
        final WebElement element = findElementWithFluentWait(By.xpath(rowIpVersionDropDown_searchBox.replace("{fqn}", fqn)));
        enterTextSlowly(waitForElementToBeVisible(element), version);
        logger.info("Entered text slowly into IP version dropdown search box for FQN: " + fqn);
    }

    @Step("selecting version: {version} for opened IP line dropdown for FQN: {fqn} ")
    private void selectIpVersionFromLineDropDownToIp(String fqn, String version) {
        logger.info("Selecting version: {version} for opened IP version dropdown for FQN: {fqn}", version, fqn);
        final WebElement element = waitForElementToBePresent(By.xpath(RowIpVersionDropDown_search_list_item.replace("{fqn}", fqn).replace("{version}", version)));
        waitForElementToBeVisible(element).click();
    }

    @Step("Retrieving IP line dropdown search list for FQN: {fqn}")
    public List<String> getRowIpLineDropDownSearchList(String fqn) {
        final List<String> texts = getTextsForElements(By.xpath(rowIpLineDropDown_search_list.replace("{fqn}", fqn)));
        logger.info("Retrieved IP line dropdown search list for FQN: " + fqn);
        return texts;
    }

    @Step("Checking if IP line dropdown search info icon is present for FQN: {fqn} and lineName: {lineName}")
    public boolean isRowIpLineDropDownSearchInfoIconPresent(String fqn, String lineName) {
        final WebElement element = waitForElementToBePresent(By.xpath(RowIpLineDropDown_search_list_infoIcon.replace("{fqn}", fqn).replace("{lineName}", lineName)));
        final boolean isPresent = isElementVisible(element);
        logger.info("Info icon presence for FQN " + fqn + " and lineName " + lineName + ": " + isPresent);
        return isPresent;
    }

    @Step("Checking if IP is not visible on confirmation for FQNs: {fqns}")
    public boolean isIpNotVisibleOnConfirmationMode(String... fqns) {
        for (final String fqn : fqns) {
            if (isElementVisible(rowIpLine.replace("{fqn}", fqn))) {
                logger.info(String.format("Info icon is visible for FQN: %s", fqn));
                return false;
            }
        }
        logger.info("IP is not visible on confirmation.");
        return true;
    }

    @Step("is Add resources modal close...")
    public boolean isAddResourcesModalClose() {
        return isElementVisible(resourcesModal);
    }

    @Step("checking Ip Resource Disable For Select")
    public boolean isIpResourceDisableForSelect(String fqn) {
        try {
            final String element = rowIpCheckbox_disable.replace("{fqn}", fqn);
            waitForElementToBePresent(By.xpath(element));
            return true;
        } catch (WebDriverException e) {
            logger.info("Element  is not visible...");
            return false;
        }
    }

    @Step("Verify column is displayed...")
    public boolean isColumnHeaderPresent(String name) {
        try {
            logger.info("Checking if column is displayed: {}", name);
            final WebElement column = waitForElementToBePresent(By.xpath(columnHeader.replace("{name}", name)));
            scrollToElement(column);
            final Actions action = new Actions(DriverFactory.getBrowserInstance());
            action.sendKeys(Keys.ARROW_RIGHT).perform();
            return isElementVisible(column);
        } catch (WebDriverException e) {
            logger.info(name + " column is not visible...");
            return false;
        }
    }

    @Step("Verify column is not displayed...")
    public int columnNotDisplayed(String name, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(columnHeader.replace("{name}", name)), counter).size();
    }

    @Step("Click on column to sort...")
    public void clickOnColumnToSort(String columnName) {
        final WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", columnName)));
        click(column);
    }

    @Step("Get number of columns...")
    public int getNumberOfColumns(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(columnHeaders), counter).size();
    }

    @Step("Pin column to the right...")
    public void pinColumnToRight() {
        hoverOverElement(pinColumnMenu);
        click(pinRightButton);
    }

    @Step("Hover over column name...")
    public void hoverOverColumnName(String columnName) {
        final WebElement column = findElementWithWait(By.xpath(columnHeader.replace("{name}", columnName)));
        hoverOverElement(column);
    }

    @Step("Verify tooltip is present...")
    public boolean isTooltipPresent(String text) {
        final WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip.replace("{tooltip}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Verify column is pinned to the right...")
    public boolean isColumnPinnedRight(String columnName) {
        final WebElement pinnedColumn = findElementWithWait(By.xpath(pinnedRightColumn.replace("{name}", columnName)));
        return isElementVisible(pinnedColumn);
    }

    @Step("Verify no pinned columns are displayed...")
    public boolean noPinnedColumnsPresent(String columnName) {
        final List<WebElement> pinnedColumns = driver.findElements(By.xpath(pinnedRightColumn.replace("{name}", columnName)));
        return getNumberOfVisibleElements(pinnedColumns) == 0;
    }

    @Step("Click on column filter icon...")
    public void clickOnFilterIcon(String column) {
        hoverOverColumnName(column);
        final WebElement filterIcon = findElementWithWait(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        click(filterIcon);
    }

    @Step("Verify secondary filter icon is not present...")
    public int getNumberOfFilterIcons(String column) {
        hoverOverColumnName(column);
        final List<WebElement> filterIcons = driver.findElements(By.xpath(secondaryFilterIcon.replace("{name}", column)));
        return getNumberOfVisibleElements(filterIcons);
    }

    @Step("Type text into filter input field...")
    public void typeTextIntoFilterInputField(String text) {
        inputText(filterInputField, text);
    }

    @Step("Remove text from filter input field...")
    public void clearFilterInputField() {
        clearInputFieldWithBackspace(filterInputField);
    }

    @Step("Get text from the secondary input field...")
    public String getSecondaryFilterText() {
        return getAttribute(filterInputField);
    }

    @Step("Resize column...")
    public void resizeColumn(String columnName, String targetColumn) {
        final WebElement column = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", columnName)));
        final WebElement targetElement = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", targetColumn)));
        dragAndDropElement(column, targetElement);
        DriverFactory.sleep(2000);
    }

    @Step("Check column checkbox...")
    public void addColumn(String columnName) {
        final WebElement columnCheckbox = findElementWithWait(By.xpath(columnCheckboxUnchecked.replace("{name}", columnName)));
        click(columnCheckbox);
    }

    @Step("Scroll horizontally to the right...")
    public void scrollRight() {
        dragAndDropElement(horizontalScroll, scrollRightCorner);
    }

    @Step("Scroll horizontally to the left...")
    public void scrollLeft() {
        dragAndDropElement(horizontalScroll, scrollLeftCorner);
    }

    @Step("Is Private Resource Icon available on hover by rowID....")
    public boolean isPrivateResourceIconPresentHover(String rowId) {
        hoverOverElement(findElementWithWait(By.xpath(resourceIpRowIndex.replace("{rowId}", rowId))));
        return isElementVisible(findElementWithWait(By.xpath(addAsPrivateResourceIcon.replace("{rowId}", rowId))));
    }

    @Step("Is Check Box available by rowID....")
    public boolean isCheckboxPresentByRowID(String rowId) {
        waitForElementToBePresent(By.xpath(ipCheckBoxRowId.replace("{rowId}", rowId)));
        return isElementVisible(ipCheckBoxRowId.replace("{rowId}", rowId));
    }

    @Step("Is Private Resource Icon clickable by rowID....")
    public boolean isPrivateResourceIconClick(String rowId) {
        final WebElement resourceIp = findElementWithWait(By.xpath(resourceIpRowIndex.replace("{rowId}", rowId)));
        hoverOverElement(resourceIp);
        waitForElementToBeClickable(findElementWithWait(By.xpath(addAsPrivateResourceIcon.replace("{rowId}", rowId)))).click();
        return isElementVisible(findElementWithWait(By.xpath(privateResourceIcon.replace("{rowId}", rowId))));
    }

    @Step("Is check Box select by rowID....")
    public boolean isCheckboxCanBeClickByRowID(String rowId) {
        final WebElement resourceIp = waitForElementToBePresent(By.xpath(ipCheckBoxRowId.replace("{rowId}", rowId)));
        click(resourceIp);
        return true;
    }

    @Step("Is check Box unselect by rowID....")
    public boolean isCheckBoxUnselectedByClickingOnRowId(String rowId) {
        final WebElement resourceIp = waitForElementToBePresent(By.xpath(ipCheckedBoxRowId.replace("{rowId}", rowId)));
        click(resourceIp);
        return true;
    }

    public boolean isIpCheckBoxDisable(String fqn) {
        return isElementVisible(disableIpCheckBoxRowId.replace("{fqn}", fqn));
    }

    public String getTootipIpCheckBoxDisable(String fqn) {
        final WebElement element = findElementWithWait(By.xpath(disableIpCheckBoxRowId.replace("{fqn}", fqn)));
        hoverOverElement(element);
        return disableIpTooltip.getText().trim();
    }

    @Step("Is Private Resource Icon clickable by rowID....")
    public boolean isPrivateResourceIconSelected(String ipName) {
        return isElementVisible(findElementWithFluentWait(By.xpath(isIpSelectAsPrivateResource.replace("{name}", ipName))));
    }

    @Step("Is Private Resource Icon unselect by rowID....")
    public boolean isPrivateResourceIconUnselect(String rowId) {
        click(findElementWithWait(By.xpath(privateResourceIcon.replace("{rowId}", rowId))));
        return isElementVisible(findElementWithWait(By.xpath(addAsPrivateResourceIcon.replace("{rowId}", rowId))));
    }

    @Step("Is Private Resource Icon clickable by ipName....")
    public boolean isPrivateResourceIconUnSelected(String ipName) {
        return isElementNotVisible(isIpSelectAsPrivateResource.replace("{name}", ipName));
    }

    @Step("Is no Ip Found Icon display....")
    public boolean isNoIpFoundIconDisplay() {
        return isElementVisible(noIpFoundIcon);
    }

    @Step("click Private Resource Icon by ipName....")
    public void clickPrivateResourceIconByName(String ipName) {
        final WebElement selectRow = waitForElementToBePresent(By.xpath(selectRowByName.replace("{name}", ipName)));
        hoverOverElement(selectRow);
        final WebElement privateIcon = findElementWithFluentWait(By.xpath(addAsPrivateResourceIconByName.replace("{name}", ipName)));
        hoverOverElement(privateIcon);
        clickWithJS(privateIcon);
    }

    @Step("click Private Resource Icon by ipName....")
    public void unClickPrivateResourceIconByName(String ipName) {
        if (isPrivateResourceIconSelected(ipName)) {
            final WebElement privateIcon = findElementWithFluentWait(By.xpath(isIpSelectAsPrivateResource.replace("{name}", ipName)));
            hoverOverElement(privateIcon);
            waitTillClickableWithFluentWait(privateIcon).click();
        }
    }

    @Step("click on remove ip on confirmation modal....")
    public void clickOnRemoveIconOnConfirmationScreenFor(String... fqns) {
        for (final String fqn : fqns) {
            clickOnThreeDotSubmenuByIPV(fqn);
            Assert.assertTrue(isSubmenuOpen(), "Three dot sub menu is not open for FQN: " + fqn);
            clickOnThreeDotSubMenuItem(REMOVE);
            logger.info("Successfully clicked on remove icon for FQN: {}", fqn);
        }
    }

    @Step("is Remove button under three dot menu displayed for IP")
    public boolean isRemoveButtonUnderThreeDotMenuDisplayedForIP(String fqn) {
        clickOnThreeDotSubmenuByIPV(fqn);
        return isSubmenuPresent(REMOVE);
    }

    public Set<String> getAddResourceTableAllColumns() {
        return getAllColumnNameList();
    }

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

    @Step("click on scroll right....")
    public void clickOnRightScroll() {
                dragAndDropElement(scrollLine, rightScrollbutton);
    }

    @Step("Click on Cancel button")
    public void clickOnCancelButton() {
        waitTillClickableWithFluentWait(resourceModelCancelButton).click();
        logger.info("Clicked on the Cancel button.");
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenuByIPV(String fqn) {
        final String locator = getThreeDotToggleByFqn.replace("{fqn}", fqn);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator))).click();
    }

    //close submenu if open
    @Step("Close three dot sub menu")
    public void closeThreeDotSubMenu(String fqn) {
        if (isSubmenuOpen()) {
            clickOnThreeDotSubmenuByIPV(fqn);
            logger.info("Closed the three dot sub menu.");
        } else {
            logger.info("Three dot sub menu is not open, no action taken.");
        }
    }

    @Step("Click on item from three dot sub menu")
    public void clickOnThreeDotSubMenuItem(String menuItemName) {
        final WebElement menuItem = findElementWithFluentWait(By.xpath(threeDotSubMenuItems.replace("{menuOption}", menuItemName)));
        click(menuItem);
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

    @Step("is ip name has hyperlink ")
    public boolean isIpNameHyperlink(String fqn, String cellId) {
        final WebElement element = findElementWithFluentWait(By.xpath(getCellValueByRowID.replace("{fqn}", fqn).replace("{cellId}", cellId)));
        return element.getTagName().equals("a");
    }
}
