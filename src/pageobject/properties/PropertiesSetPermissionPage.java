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
package com.methodics.phi.pageobject.properties;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.*;

import static com.methodics.phi.util.CommonUrls.PROPERTY_SETS_MANAGEMENT_PAGE;

public class PropertiesSetPermissionPage extends BasePage {
    private WebDriver driver;
    private static final String PROPERTY_SETS_MANAGEMENT_TITLE = "Property Sets";

    public PropertiesSetPermissionPage(WebDriver driver) {
        this.driver = driver;

        if (driver!=null) {
            driver.get(DriverFactory.getFullUrl(PROPERTY_SETS_MANAGEMENT_PAGE));
        }

        waitForTitle(PROPERTY_SETS_MANAGEMENT_TITLE);
        if (!driver.getTitle().contains("Property Sets")) {
            throw new IllegalStateException(
                    "This is not Property Sets Page, current page is: " + driver.getTitle());
        }
    }

    public PropertiesSetPermissionPage() {
        driver = DriverFactory.getBrowserInstance();
    }

    //Permission Page locators
    @FindBy(css = "[data-testid='users']")
    private WebElement usersTabInPermissionPage;

    @FindBy(css = "[data-testid='groups']")
    private WebElement groupsTabInPermissionPage;

    @FindBy(css = "[data-testid='tabpanel-groups']")
    private WebElement groupTabPaneInPermissionPage;

    @FindBy(css = "[data-testid='tabpanel-users']")
    private WebElement userTabPaneInPermissionPage;

    //Add User/Group modal locators
    @FindBy(css = "button[data-testid='ui-btn-add-users-groups-button']")
    private WebElement addUserGroupButton;

    @FindBy(css = "[data-testid='modal-users']")
    private WebElement userTabInAddUserGroupModal;

    @FindBy(css = "[data-testid='modal-groups']")
    private WebElement groupTabInAddUserGroupModal;

    @FindBy(css = "[data-testid='tabpanel-modal-groups']")
    private WebElement groupTabPaneInAddUserGroupModal;

    @FindBy(css = "[data-testid='tabpanel-modal-users']")
    private WebElement userTabPaneInAddUserGroupModal;

    @FindBy(css = addUserGroupModelTestdataID + " .modal-footer span:nth-child(1)")
    private WebElement addUserGroupModalFooter;

    @FindBy(css = addUserGroupModelTestdataID)
    private WebElement addUserGroupModal;

    @FindBy(css = addUserGroupModelTestdataID + " .tabs__nav-link.active")
    private WebElement selectedTabInAddUserGroupModal;

    @FindBy(css = addUserGroupModelTestdataID + " .app-modal-header__title")
    private WebElement addUserGroupModalTitle;

    @FindBy(css = addUserGroupModelTestdataID + " [data-testid='modal-users']")
    private WebElement usersTabInModal;

    @FindBy(css = addUserGroupModelTestdataID + " [data-testid='modal-groups']")
    private WebElement groupsTabInModal;

    @FindBy(css = addUserGroupModelTestdataID + " button[data-testid='ui-btn-cancel-button']")
    private WebElement addUserGroupModalCancelButton;

    @FindBy(css = addUserGroupModelTestdataID + " button[data-testid='ui-btn-add-to-list-button']")
    private WebElement addUserGroupModalAddToListButton;

    @FindBy(css = addUserGroupModelTestdataID + " button[data-testid='group-permissions-grid-master-wrapper-actions-dropdown-toggle']")
    private WebElement groupShowAllDropdownToggle;

    @FindBy(css = addUserGroupModelTestdataID + " button[data-testid='user-permissions-grid-master-wrapper-actions-dropdown-toggle']")
    private WebElement userShowAllDropdownToggle;

    @FindBy(xpath = group_tab_dataTestId + "//button[@data-testid='ui-btn-select-all-action']")
    private WebElement groupShowAllOption;

    @FindBy(xpath = group_tab_dataTestId + "//button[@data-testid='ui-btn-show-selected-action']")
    private WebElement groupShowSelectedOption;

    @FindBy(xpath = user_tab_dataTestId + "//button[@data-testid='ui-btn-select-all-action']")
    private WebElement userShowAllOption;

    @FindBy(xpath = user_tab_dataTestId + "//button[@data-testid='ui-btn-show-selected-action']")
    private WebElement userShowSelectedOption;

    @FindBy(xpath = "//button[@data-testid='ui-btn-sticky-actions-toolbar-remove-btn']")
    private WebElement removeButton;

    @FindBy(xpath = "//button[@data-testid='ui-btn-ui-modal-remove']")
    private WebElement confirmRemoveButton;

    @FindBy(css = "div[data-testid='review-update-permissions-modal'] h5")
    private WebElement reviewUpdatePermissionsModalTitle;

    @FindBy(css = "div[data-testid='review-update-permissions-modal'] button[data-testid='ui-btn-modal-header-close']")
    private WebElement reviewUpdatePermissionsModalCancelButton;

    @FindBy(css = "div[data-testid='review-update-permissions-modal'] div[class*='fqn-bar__title']")
    private WebElement propertySetNameOnreviewUpdatePermissionsModal;

    @FindBy(xpath = "//div[@data-testid='toast-body']")
    private WebElement successToastMessage;

    @FindBy(css = "button[data-testid='ui-btn-add-users-groups-button'][disabled]")
    private WebElement disabledAddUserGroupButton;

    private static final String addUserGroupModelTestdataID = "div.add-users-and-groups-modal";
    private static final String group_tab_dataTestId = "//*[@data-testid='tabpanel-modal-groups']";
    private static final String user_tab_dataTestId = "//*[@data-testid='tabpanel-modal-users']";
    private static final String selectAgGridRowInAddUserGroupModal = "//div[@role='row'][.//span[@title='{groupUserName}']]//div[contains(@class,'ag-checkbox-input')]";
    private static final String findAgGridRowInAddUserGroupModal = "//div[@role='row'][.//span[@title='{groupUserName}']]";
    private static final String columnHeaderInAddUserGroupModal = ".//*[normalize-space(text())='{columnName}']//ancestor::*[@role='columnheader']";
    private static final String readCheckboxDataTestId = "[data-testid='cell-renderer-perm-checkbox_read-input']";
    private static final String writeCheckboxDataTestId = "[data-testid='cell-renderer-perm-checkbox_write-input']";
    private static final String ownerCheckboxDataTestId = "[data-testid='cell-renderer-perm-checkbox_owner-input']";
    private static final String permissionCellXpathTemplate = ".//div[@row-index='{rowIndex}']/child::div[@col-id='{permission}']";
    private static final String groupRowXpathTemplate = ".//div[@role='row'][.//span[@title='{groupName}']]";
    private static final String userRowXpathTemplate = ".//div[@role='row'][.//span[@title='{userName}']]";
    private static final String addGroupUsersPlaceholder = "//div[@data-testid='tabpanel-modal-{userOrGroup}s']//following::div[@data-testid='{userOrGroup}-permissions-grid-master-wrapper']//input[@data-testid='ui-in-input-field-input-value']";
    private static final String noResultFoundMessage = "//div[@data-testid='tabpanel-modal-{userOrGroup}s']//following::div[@data-testid='{userOrGroup}-permissions-grid-master-wrapper']//h5[contains(@class,'no-rows-overlay__text')]";
    private static final String visibleRows = "//div[@data-testid='tabpanel-{tabName}']//div[contains(@class,'ag-pinned-left-cols-container')]//div[@role='row' and @row-index]";
    private static final String selectedRows = "//div[@data-testid='tabpanel-{tabName}']//div[contains(@class,'ag-pinned-left-cols-container')]//div[@role='row'][contains(@class, 'ag-row-selected')]";
    private static final String bulkActionRemoveButton = "//span[contains(normalize-space(.), '{tabName} selected') or contains(normalize-space(.), '{tabName}s selected') ]//following-sibling::button[@data-testid='ui-btn-sticky-actions-toolbar-remove-btn']";
    private static final String bulkActionCloseButton = "//span[contains(normalize-space(.), '{tabName} selected') or contains(normalize-space(.), '{tabName}s selected') ]//following::button[@data-testid='ui-btn-sticky-actions-toolbar-close'][1]";
    private static final String permissionCheckboxBulkAction = "//div[@name='{permissionType}']//child::input";
    private static final String permissionTypeColumnXpath = "//div[@row-index='{rowIndex}']/child::div[@col-id='{permission}']//input[@disabled]";

    // No rows overlay locators
    private static final By NO_ROWS_OVERLAY_MESSAGE = By.cssSelector(".no-rows-overlay__text");
    private static final By NO_ROWS_OVERLAY_DESCRIPTION = By.cssSelector(".no-rows-overlay__description");
    private static final By NO_ROWS_OVERLAY_ICON = By.cssSelector(".no-rows-overlay__icon");
    private static final By NO_ROWS_OVERLAY_LINK = By.cssSelector(".no-rows-overlay__link");
    private static final By checkboxBy = By.cssSelector(".ag-selection-checkbox .ag-checkbox-input-wrapper, .ag-checkbox-input-wrapper, input[type='checkbox']");

    // Locator template for left cell
    private static final String LEFT_CELL_SELECTOR_TEMPLATE = "[role='row'][row-index='{rowId}'] [role='gridcell'][col-id*='name'] .cell-text";

    // Column filter locators
    private static final By FILTER_ICON = By.cssSelector(".ag-header-active .ag-icon-filter");
    private static final By OPERATOR_DISPLAY = By.cssSelector(".ag-focus-managed .ag-picker-field-display");
    private static final By FILTER_INPUT = By.cssSelector(".ag-focus-managed input[placeholder='Filter...']");
    private static final By SET_FILTER_ITEM_CHECKBOX = By.cssSelector(".ag-set-filter-item-checkbox");
    private static final By SET_FILTER_CHECKBOX_LABEL = By.cssSelector(".ag-checkbox-label");
    private static final By SET_FILTER_CHECKBOX_INPUT = By.cssSelector(".ag-checkbox-input-wrapper");
    private static final String CONTAINS_OPTION = "//div[@role='option']/*[normalize-space(text())='Contains']";
    private static final String OPERATOR_OPTION_TEMPLATE = "//div[@role='option']/*[normalize-space(text())='{operator}']";

    // Shared locators
    private static final By CENTER_CONTAINER = By.cssSelector(".ag-center-cols-container");
    private static final By LEFT_CONTAINER = By.cssSelector(".ag-pinned-left-cols-container");
    private static final By CENTER_ROWS = By.cssSelector("[role='row'][row-id]");
    private static final By VERTICAL_VIEWPORT = By.cssSelector(".ag-body-vertical-scroll-viewport, .ag-body-viewport");

    // Group-specific locators
    private static final By GROUP_GRID_WRAPPER = By.cssSelector("[data-testid='group-permissions-grid-data-grid-wrapper']");
    private static final By GROUP_DESC_CELL = By.cssSelector("[role='gridcell'][col-id='group_description'] .cell-text");
    private static final By GROUP_SOURCE_BADGE = By.cssSelector("[role='gridcell'][col-id='source'] [data-testid='badge-cell-renderer-text']");
    private static final By GROUP_SOURCE_CELL_TX = By.cssSelector("[role='gridcell'][col-id='source'] .cell-text");
    private static final By GROUP_SOURCE_CELL = By.cssSelector("[role='gridcell'][col-id='source']");

    // User-specific locators
    private static final By USER_GRID_WRAPPER = By.cssSelector("[data-testid='user-permissions-grid-data-grid-wrapper']");
    private static final By USER_FULL_NAME_CELL = By.cssSelector("[role='gridcell'][col-id='full_name'] .cell-text");
    private static final By USER_EMAIL_CELL = By.cssSelector("[role='gridcell'][col-id='email'] .cell-text");
    private static final By USER_DESC_CELL = By.cssSelector("[role='gridcell'][col-id='user_description'] .cell-text");
    private static final By USER_SOURCE_BADGE = By.cssSelector("[role='gridcell'][col-id='source'] [data-testid='badge-cell-renderer-text']");
    private static final By USER_SOURCE_CELL_TX = By.cssSelector("[role='gridcell'][col-id='source'] .cell-text");
    private static final By USER_SOURCE_CELL = By.cssSelector("[role='gridcell'][col-id='source']");

    private final String columnHeadingName = "//div[@data-testid='review-update-permissions-modal']//span[normalize-space(.)='{ColumnName}']";
    private final String categoryName = "//div[@data-testid='review-update-permissions-modal'] //div[@role='row' and .//span[normalize-space()='{GroupOrUserName}']] /div[@role='gridcell'][last()]//span";
    private final String permissionCheckBoxOnUserTab = "//div[contains(@data-testid,'user-permissions')]//input[@data-testid='cell-renderer-perm-checkbox_{permissionType}-input']";
    private final String beforePermissionOnPermissionReviewModal = "//div[@data-testid='review-update-permissions-modal'] //div[@role='row' and .//span[normalize-space()='{groupUserName}']]//div[@role='gridcell' and @aria-colindex='2']//span[@data-testid='permissions-cell-renderer-{permissionType}']";
    private final String afterPermissionOnPermissionReviewModal = "//div[@data-testid='review-update-permissions-modal'] //div[@role='row' and .//span[normalize-space()='{groupUserName}']]//div[@role='gridcell' and @aria-colindex='3']//span[@data-testid='permissions-cell-renderer-{permissionType}']";
    private final String noPermissionOnReviewModal = "//div[@data-testid='review-update-permissions-modal'] //div[@role='row' and .//span[normalize-space()='{groupUserName}']]//span[@data-testid='permissions-cell-renderer-no-permissions']";
    private final String permissionCheckBoxOnGroupTab = "//div[contains(@data-testid,'group-permissions')]//input[@data-testid='cell-renderer-perm-checkbox_{permissionType}-input']";

    private interface RecordBuilder {
        Map<String, String> build(String name, String[] centerValues, String source);
    }

    @Step("Click on tab {groupsUsersTab} on Permission Page...")
    public void clickOnTabOnPermissionPage(String groupsUsersTab) {
        switch (groupsUsersTab.toLowerCase()) {
            case "groups":
                waitTillClickableWithFluentWait(groupsTabInPermissionPage).click();
                break;
            case "users":
                waitTillClickableWithFluentWait(usersTabInPermissionPage).click();
                break;
            default:
                throw new IllegalArgumentException("Unknown tab name: " + groupsUsersTab);
        }
    }

    @Step("Click on Add User/Group button...")
    public void clickOnAddUserGroupButton() {
        logger.info("Click on Add User/Group button");
        waitTillClickableWithFluentWait(addUserGroupButton).click();
    }

    @Step("Verify if Add User/Group button is visible...")
    public boolean isAddUserGroupButtonVisible() {
        logger.info("Checking if Add User/Group button is visible");
        return isElementVisible(addUserGroupButton);
    }

    @Step("Verify if Add User/Group button is disabled...")
    public boolean isAddUserGroupButtonDisabled() {
        logger.info("Check if Add User/Group button is disabled");
        try {
            final WebElement addBtn = waitTillVisibleWithFluentWait(disabledAddUserGroupButton);
            return !addBtn.isEnabled();
        } catch (Exception e) {
            logger.warn("Add User/Group button is not available", e);
            return false;
        }
    }

    @Step("Verify Add User/Group interface is displayed...")
    public boolean verifyAddUserGroupInterfaceIsDisplayed() {
        try {
            waitTillVisibleWithFluentWait(addUserGroupModal);
            waitTillVisibleWithFluentWait(addUserGroupModalTitle);
            return true;
        } catch (Exception e) {
            logger.warn("Add Users & Groups modal was not displayed", e);
            return false;
        }
    }

    @Step("Get Add User/Group modal title...")
    public String getAddUserGroupModalTitle() {
        return getText(addUserGroupModalTitle);
    }

    @Step("Get selected tab in Add User/Group modal...")
    public String getSelectedTabInAddUserGroupModal() {
        try {
            final WebElement activeTab = waitForElementToBeVisible(selectedTabInAddUserGroupModal);
            final String text = activeTab.getText().trim();
            return text.replaceAll("\\s+", " ");
        } catch (Exception e) {
            logger.warn("Unable to determine selected tab in Add Users & Groups modal", e);
            return "";
        }
    }

    @Step("Verify cancel button is enabled in Add User/Group modal...")
    public boolean isCancelButtonEnabledInAddUserGroupModal() {
        try {
            final WebElement cancelBtn = waitTillVisibleWithFluentWait(addUserGroupModalCancelButton);
            return cancelBtn.isDisplayed() && cancelBtn.isEnabled();
        } catch (Exception e) {
            logger.warn("Cancel button is not available in Add Users & Groups modal", e);
            return false;
        }
    }

    @Step("Verify Add to list button is disabled in Add User/Group modal...")
    public boolean isAddToListButtonDisabledInAddUserGroupModal() {
        try {
            final WebElement addBtn = waitTillVisibleWithFluentWait(addUserGroupModalAddToListButton);
            return !addBtn.isEnabled();
        } catch (Exception e) {
            logger.warn("Add to list button is not available in Add Users & Groups modal", e);
            return false;
        }
    }

    @Step("Click Add to list button on Add User/Group modal...")
    public void clickAddToListButtonInAddUserGroupModal() {
        logger.info("Click Add to List button on Add User/Group modal");
        waitTillClickableWithFluentWait(addUserGroupModalAddToListButton).click();
    }

    @Step("Click Cancel on Add User/Group modal...")
    public void clickCancelOnAddUserGroupModal() {
        logger.info("Click Cancel  button on Add User/Group modal");
        waitTillClickableWithFluentWait(addUserGroupModalCancelButton).click();
    }

    @Step("Verify Add User/Group modal is not visible...")
    public boolean isAddUserGroupModalNotVisible() {
        return isElementNotVisible(addUserGroupModal);
    }

    @Step("Select {itemName} from Add User/Group modal {tabName} tab...")
    public void selectItemInAddUserGroupModal(String tabName, String... itemName) {
        final String paneSelector = getTabDataTestIDSelectorAddUserGroupModel(tabName);
        logger.info("Select {} from Add User/Group modal {} tab", itemName, tabName);
        for (String name : itemName) {
            final String element = paneSelector + selectAgGridRowInAddUserGroupModal.replace("{groupUserName}", name);
            final WebElement row = waitForElementToBePresent(By.xpath(element));
            waitTillClickableWithFluentWait(row).click();
        }
    }

    @Step("Unselect {itemName} from Add User/Group modal {tabName} tab...")
    public void unselectItemInAddUserGroupModal(String tabName, String... itemName) {
        final String paneSelector = getTabDataTestIDSelectorAddUserGroupModel(tabName);
        logger.info("Unselect {} from Add User/Group modal {} tab", itemName, tabName);
        for (String name : itemName) {
            final String element = paneSelector + selectAgGridRowInAddUserGroupModal.replace("{groupUserName}", name);

            try {
                final WebElement checkbox = waitForElementToBePresent(By.xpath(element));
                if (checkbox.getAttribute("class").contains("ag-checked")) {
                    waitTillClickableWithFluentWait(checkbox).click();
                } else {
                    logger.info("{} is already unselected in Add User/Group modal {} tab", itemName, tabName);
                }
            } catch (Exception e) {
                logger.warn("Unable to find checkbox for {} in Add User/Group modal {} tab; cannot unselect", itemName, tabName, e);
            }
        }
    }

    @Step("Click on tab {Tab} on Add User/Group modal...")
    public void clickOnTabOnAddUserGroupModal(String tabName) {
        switch (tabName.toLowerCase()) {
            case "groups":
                groupTabInAddUserGroupModal.click();
                break;
            case "users":
                userTabInAddUserGroupModal.click();
                break;
            default:
                throw new IllegalArgumentException("Unknown tab name: " + tabName);
        }
        waitForPageLoaded();
    }

    @Step("Verify if record with name {s} is disabled in {usersTab} tab of Add User/Group modal...")
    public boolean isRecordDisableInAddUserGroupModal(String usersTab, String s) {
        final String paneSelector = getTabDataTestIDSelectorAddUserGroupModel(usersTab);
        logger.info("Check if record with name {} is disabled in {} tab of Add User/Group modal", s, usersTab);
        final String element = paneSelector + findAgGridRowInAddUserGroupModal.replace("{groupUserName}", s);
        try {
            final WebElement row = waitForElementToBePresent(By.xpath(element));
            return row.getAttribute("class").contains("row-disabled");
        } catch (Exception e) {
            logger.warn("Record with name {} was not found in {} tab of Add User/Group modal", s, usersTab, e);
            return false;
        }
    }

    @Step("Get footer message in Add User/Group modal...")
    public String getFooterMessageInAddUserGroupModal() {
        waitTillVisibleWithFluentWait(addUserGroupModalFooter);
        return addUserGroupModalFooter.getText();
    }

    @Step("Set sorting order to {orderType} for {columnName} in {tabName} tab of Add User/Group modal...")
    public void setSoringOderToForAddGroupModel(String tabName, String columnName, String orderType) {
        final String paneSelector = getTabDataTestIDSelectorAddUserGroupModel(tabName);
        final String columnHeaderSelector = paneSelector + columnHeaderInAddUserGroupModal.replace("{columnName}", columnName).substring(1);
        try {
            final WebElement columnHeader = waitForElementToBePresent(By.xpath(columnHeaderSelector));
            String currentSort = columnHeader.getAttribute("aria-sort");
            int attempts = 0;
            while (attempts++ < 3 && !orderType.equalsIgnoreCase(currentSort)) {
                logger.info("Current sort for column {} is {}, clicking to change to {} (attempt {}/{})", columnName, currentSort, orderType, attempts, 3);
                waitTillClickableWithFluentWait(columnHeader).click();
                DriverFactory.sleep(1000); // small sleep to allow sorting to take effect and aria-sort to update
                currentSort = columnHeader.getAttribute("aria-sort");
                logger.info("After click, current sort for column {} is {}", columnName, currentSort);
            }
            if (!orderType.equalsIgnoreCase(currentSort)) {
                logger.warn("Unable to set sorting order to {} for column {} in {} tab of Add User/Group modal after {} attempts; current sort is {}", orderType, columnName, tabName, attempts - 1, currentSort);
            }
        } catch (Exception e) {
            logger.warn("Unable to find column header {} in {} tab of Add User/Group modal to set sorting order", columnName, tabName, e);
        }
        DriverFactory.sleep(2000); // Wait for sorting to take effect
    }

    @Step("Set sorting order to {orderType} for {columnName} in {groupsTab} tab of Permission Page...")
    public void setSoringOderToForPermissionScreen(String groupsTab, String columnName, String orderType) {
        WebElement tabPane;
        switch (groupsTab.toLowerCase()) {
            case "groups":
                tabPane = groupTabPaneInPermissionPage;
                break;
            case "users":
                tabPane = userTabPaneInPermissionPage;
                break;
            default:
                throw new IllegalArgumentException("Unknown tab name: " + groupsTab);
        }
        try {
            final String columnHeaderLocator = columnHeaderInAddUserGroupModal.replace("{columnName}", columnName);
            final WebElement columnHeader = tabPane.findElement(By.xpath(columnHeaderLocator));
            String currentSort = columnHeader.getAttribute("aria-sort");
            int attempts = 0;
            while (attempts++ < 3 && !orderType.equalsIgnoreCase(currentSort)) {
                logger.info("Current sort for column {} is {}, clicking to change to {} (attempt {}/{})", columnName, currentSort, orderType, attempts, 3);
                waitTillClickableWithFluentWait(columnHeader).click();
                DriverFactory.sleep(1000); // small sleep to allow sorting to take effect and aria-sort to update
                currentSort = columnHeader.getAttribute("aria-sort");
                logger.info("After click, current sort for column {} is {}", columnName, currentSort);
            }
            if (!orderType.equalsIgnoreCase(currentSort)) {
                logger.warn("Unable to set sorting order to {} for column {} in {} tab of Permission Page after {} attempts; current sort is {}", orderType, columnName, groupsTab, attempts - 1, currentSort);
            }
        } catch (Exception e) {
            logger.warn("Unable to find column header {} in {} tab of Permission Page to set sorting order", columnName, groupsTab, e);
        }
        DriverFactory.sleep(2000); // Wait for sorting to take effect

    }

    @Step("Get all records in Groups tab in Permission Page...")
    public List<Map<String, String>> getTotalRecordsFromGroupsTabInPermissionPage() {
        final List<By> centerCells = Arrays.asList(GROUP_DESC_CELL, GROUP_SOURCE_BADGE, GROUP_SOURCE_CELL_TX, GROUP_SOURCE_CELL);
        final RecordBuilder builder = (name, vals, src) -> buildGroupRecord(name, vals[0], src);
        return getRecordsFromGridtable(groupTabPaneInPermissionPage, GROUP_GRID_WRAPPER, centerCells, 1, builder, 140, "Collected {} unique group records from Add User/Group modal Groups tab");
    }

    @Step("Get all user records from Users tab in Add User/Group modal...")
    public List<Map<String, String>> getRecordsFromUserFromTabAddUserGroupModal() {
        final List<By> centerCells = Arrays.asList(USER_FULL_NAME_CELL, USER_EMAIL_CELL, USER_DESC_CELL, USER_SOURCE_BADGE, USER_SOURCE_CELL_TX, USER_SOURCE_CELL);
        final RecordBuilder builder = (name, vals, src) -> buildUserRecord(name, vals[0], vals[1], vals[2], src);
        return getRecordsFromGridtable(userTabPaneInAddUserGroupModal, USER_GRID_WRAPPER, centerCells, 3, builder, 120, "Collected {} unique user records from Add User/Group modal Users tab");
    }

    @Step("Get all records in Users tab in Permission Page...")
    public List<Map<String, String>> getTotalRecordsFromUsersTabInPermissionPage() {
        final List<By> centerCells = Arrays.asList(USER_FULL_NAME_CELL, USER_EMAIL_CELL, USER_DESC_CELL, USER_SOURCE_BADGE, USER_SOURCE_CELL_TX, USER_SOURCE_CELL);
        final RecordBuilder builder = (name, vals, src) -> buildUserRecord(name, vals[0], vals[1], vals[2], src);
        return getRecordsFromGridtable(userTabPaneInPermissionPage, USER_GRID_WRAPPER, centerCells, 3, builder, 120, "Collected {} unique user records from Add User/Group modal Users tab");
    }

    @Step("Get all group records from Groups tab in Add User/Group modal...")
    public List<Map<String, String>> getRecordsFromGroupsFromTabAddUserGroupModal() {
        final List<By> centerCells = Arrays.asList(GROUP_DESC_CELL, GROUP_SOURCE_BADGE, GROUP_SOURCE_CELL_TX, GROUP_SOURCE_CELL);
        final RecordBuilder builder = (name, vals, src) -> buildGroupRecord(name, vals[0], src);
        return getRecordsFromGridtable(groupTabPaneInAddUserGroupModal, GROUP_GRID_WRAPPER, centerCells, 1, builder, 140, "Collected {} unique group records from Add User/Group modal Groups tab");
    }

    @Step("Get no permissions message...")
    public String getNoPermissionsMessage(String tabName) {
        final WebElement page = getTabDataTestIDSelectorPermissionPage(tabName);
        return waitTillVisibleWithFluentWait(page.findElement(NO_ROWS_OVERLAY_MESSAGE)).getText();
    }

    @Step("Get no permissions description...")
    public String getNoPermissionsDescription(String tabName) {
        final WebElement page = getTabDataTestIDSelectorPermissionPage(tabName);
        return waitTillVisibleWithFluentWait(page.findElement(NO_ROWS_OVERLAY_DESCRIPTION)).getText().replace("\n", " ");
    }

    @Step("Get no permissions icon class...")
    public String getNoPermissionsIconClass(String tabName) {
        final WebElement page = getTabDataTestIDSelectorPermissionPage(tabName);
        return page.findElement(NO_ROWS_OVERLAY_ICON).getAttribute("class");
    }

    @Step("Click on learn more link in no permissions message...")
    public void clickOnLearnMoreLinkInNoPermissionsMessage(String tabName) {
        final WebElement page = getTabDataTestIDSelectorPermissionPage(tabName);
        waitTillClickableWithFluentWait(page.findElement(NO_ROWS_OVERLAY_LINK)).click();
    }

    @Step("Verify if learn more link in no permissions message redirects to help page...")
    public boolean isLearnMoreLinkRedirectedToHelpPage(String tabName, String permissionManagementHelpLink) {
        try {
            DriverFactory.sleep(2000); // wait for potential redirection
            switchToOpenedTab();
            final String currentUrl = driver.getCurrentUrl();
            logger.info("Current URL after clicking learn more link: {}", currentUrl);
            assert currentUrl!=null;
            return currentUrl.equals(permissionManagementHelpLink);
        } catch (Exception e) {
            logger.warn("Error while clicking learn more link in no permissions message", e);
            return false;
        }
    }

    @Step("Hover over column header {columnName} in {tabName} tab of Add User/Group modal...")
    public void hoverOverColumnHeaderInAddUserGroupModal(String tabName, String columnName) {
        final WebElement tabPane = waitForElementToBePresent(By.xpath(getTabDataTestIDSelectorAddUserGroupModel(tabName)));
        logger.info("Hover over column header {} in {} tab of Add User/Group modal", columnName, tabName);
        final String columnHeaderLocator = columnHeaderInAddUserGroupModal.replace("{columnName}", columnName);
        final WebElement columnHeader = tabPane.findElement(By.xpath(columnHeaderLocator));
        hoverOverElement(columnHeader);
    }

    @Step("Hover over column header {columnName} in {groupsTab} tab of Permission Page...")
    public void hoverOverColumnHeaderInPermissionPage(String tabName, String columnName) {
        final WebElement tabPane = waitForElementToBeClickable(getTabDataTestIDSelectorPermissionPage(tabName));
        logger.info("Hover over column header {} in {} tab of permission tab", columnName, tabName);
        final String columnHeaderLocator = columnHeaderInAddUserGroupModal.replace("{columnName}", columnName);
        waitForElementToBeVisible(tabPane).click();
        final WebElement columnHeader = waitTillClickableWithFluentWait(tabPane.findElement(By.xpath(columnHeaderLocator)));
        hoverOverElement(columnHeader);
    }

    @Step("Set filter with operator {operator} and value {filterValue} for column ")
    public void setFilterForColumn(String operator, String filterValue) {
        logger.info("Set filter with operator {} and value {} for column ", operator, filterValue);
        waitForElementToBeClickable(findElementWithFluentWait(FILTER_ICON)).click();
        // Wait for filter popup
        DriverFactory.sleep(500);
        // Select operator
        final WebElement operatorDisplay = waitForElementToBePresent(OPERATOR_DISPLAY);
        waitTillClickableWithFluentWait(operatorDisplay).click();
        final WebElement operatorOption = waitForElementToBePresent(By.xpath(OPERATOR_OPTION_TEMPLATE.replace("{operator}", operator)));
        waitTillClickableWithFluentWait(operatorOption).click();
        // Enter filter value
        final WebElement filterInput = waitForElementToBePresent(FILTER_INPUT);
        filterInput.clear();
        filterInput.sendKeys(filterValue);
        // Wait for filter to apply
        DriverFactory.sleep(1000);
    }

    @Step("Set custom filter with {filterValue}")
    public void setCustomFilter(String filterValue) {
        logger.info("Set custom filter with {}", filterValue);
        waitForElementToBeClickable(findElementWithFluentWait(FILTER_ICON)).click();
        // Wait for filter popup
        DriverFactory.sleep(500);
        // Find the checkbox for the given filterValue
        List<WebElement> items = driver.findElements(SET_FILTER_ITEM_CHECKBOX);
        for (WebElement item : items) {
            WebElement label = item.findElement(SET_FILTER_CHECKBOX_LABEL);
            if (!label.getText().equals(filterValue)) {
                logger.info("Found checkbox for filter value {} in filter list; clicking to apply filter", filterValue);
                final WebElement checkbox = item.findElement(SET_FILTER_CHECKBOX_INPUT);
                // if checkbox is checked, uncheck it
                if (checkbox.getAttribute("class").contains("ag-checked"))
                    label.click();
                break;
            }
        }
        // Wait for filter to apply
        DriverFactory.sleep(1000);
    }

    @Step("Clear filter for column")
    public void clearFilterForColumn() {
        final WebElement filterIcon = waitForElementToBePresent(FILTER_ICON);
        DriverFactory.sleep(500);
        waitTillClickableWithFluentWait(filterIcon).click();
        // Click clear or something; assuming there's a clear button or entering empty
        final WebElement filterInput = waitForElementToBePresent(FILTER_INPUT);
        clearTextByBackspace(filterInput);
        // Select Contains or default
        final WebElement operatorDisplay = waitForElementToBePresent(OPERATOR_DISPLAY);
        if (!operatorDisplay.getText().equals("Contains")) {
            waitTillClickableWithFluentWait(operatorDisplay).click();
            final WebElement containsOption = waitForElementToBePresent(By.xpath(CONTAINS_OPTION));
            waitTillClickableWithFluentWait(containsOption).click();
        }
        // Wait for filter to clear
        DriverFactory.sleep(2000);
    }

    @Step("clear custom filter ..")
    public void clearCustomFilter(String filterValue) {
        waitForElementToBeClickable(findElementWithFluentWait(FILTER_ICON)).click();
        DriverFactory.sleep(500);
        // Find the checkbox for the given filterValue
        final List<WebElement> items = driver.findElements(SET_FILTER_ITEM_CHECKBOX);
        for (final WebElement item : items) {
            final WebElement label = item.findElement(SET_FILTER_CHECKBOX_LABEL);
            final WebElement checkbox = item.findElement(SET_FILTER_CHECKBOX_INPUT);
            if (!Objects.requireNonNull(checkbox.getAttribute("class")).contains("ag-checked"))
                    label.click();
            }
        // Wait for filter to clear
        DriverFactory.sleep(2000);
    }

    @Step("Verify if filter is applied for column {columnName} in {tabName} tab of Add User/Group modal...")
    public boolean isFilterAppliedForColumnInAddUserGroupModal(String tabName, String columnName) {
        final WebElement tabPane = waitForElementToBePresent(By.xpath(getTabDataTestIDSelectorAddUserGroupModel(tabName)));
        logger.info("Verify if filter is applied for column {} in {} tab of Add User/Group modal", columnName, tabName);
        final String columnHeaderLocator = columnHeaderInAddUserGroupModal.replace("{columnName}", columnName);
        final WebElement columnHeader = tabPane.findElement(By.xpath(columnHeaderLocator));
        return !columnHeader.findElements(FILTER_ICON).isEmpty();
    }

    @Step("Click on Show All drop down button in Add User/Group modal for {tabName} tab...")
    public void clickOnShowAllDropDownButtonInAddUserGroupModal(String tabName) {
        WebElement toggle = tabName.equalsIgnoreCase("groups") ? groupShowAllDropdownToggle:userShowAllDropdownToggle;
        if (toggle.getAttribute("aria-expanded").equals("false"))
        waitTillClickableWithFluentWait(toggle).click();
    }

    @Step("Check if Show Selected option is disabled in Add User/Group modal for {tabName} tab...")
    public boolean isShowSelectedOptionDisabledInAddUserGroupModal(String tabName) {
        WebElement element = tabName.equalsIgnoreCase("groups") ? groupShowSelectedOption:userShowSelectedOption;
        element = waitTillVisibleWithFluentWait(element);
        return element.getAttribute("class").contains("disabled");
    }

    @Step("Check if Show All option is disabled in Add User/Group modal for {tabName} tab...")
    public boolean isShowAllOptionDisabledInAddUserGroupModal(String tabName) {
        WebElement element = tabName.equalsIgnoreCase("groups") ? groupShowAllOption:userShowAllOption;
        element = waitTillVisibleWithFluentWait(element);
        return element.getAttribute("class").contains("disabled");
    }

    @Step("Click on Show Selected option in Add User/Group modal for {tabName} tab...")
    public void clickOnShowSelectedOptionInAddUserGroupModal(String tabName) {
        clickOnShowAllDropDownButtonInAddUserGroupModal(tabName);
        final WebElement element = tabName.equalsIgnoreCase("groups") ? groupShowSelectedOption:userShowSelectedOption;
        waitTillClickableWithFluentWait(element).click();
        DriverFactory.sleep(2000); // Wait for show selected take effect
    }

    @Step("Click on Show All option in Add User/Group modal for {tabName} tab...")
    public void clickOnShowAllOptionInAddUserGroupModal(String tabName) {
        clickOnShowAllDropDownButtonInAddUserGroupModal(tabName);
        final WebElement element = tabName.equalsIgnoreCase("groups") ? groupShowAllOption:userShowAllOption;
        waitTillClickableWithFluentWait(element).click();
    }

    @Step("Update permission to group {group1} with read {b}, write {b1}, owner {b2} in Permission Page...")
    public void updatePermissionToGroup(String groupName, boolean readPermission, boolean writePermission, boolean ownerPermission) {
        logger.info("Update permission to group {} with read {}, write {}, owner {} in Permission Page", groupName, readPermission, writePermission, ownerPermission);

        // Find the row for the specific group by name in the left pinned column
        final WebElement groupRow = groupTabPaneInPermissionPage.findElement(By.xpath(groupRowXpathTemplate.replace("{groupName}", groupName)));
        final String rowIndex = groupRow.getAttribute("row-index");

        // Permissions to update
        final String[] permissions = {"read", "write", "owner"};
        final boolean[] permissionValues = {readPermission, writePermission, ownerPermission};
        final String[] dataTestIds = {readCheckboxDataTestId, writeCheckboxDataTestId, ownerCheckboxDataTestId};

        for (int i = 0; i < permissions.length; i++) {
            final String cellXpath = permissionCellXpathTemplate.replace("{rowIndex}", rowIndex).replace("{permission}", permissions[i]);
            final WebElement cell = groupTabPaneInPermissionPage.findElement(By.xpath(cellXpath));
            final WebElement checkbox = cell.findElement(By.cssSelector(dataTestIds[i]));
            if (checkbox.isSelected()!=permissionValues[i]) {
                waitTillClickableWithFluentWait(checkbox).click();
            }
        }
    }

    @Step("Update permission to user {userName} with read {b}, write {b1}, owner {b2} in Permission Page...")
    public void updatePermissionToUser(String userName, boolean readPermission, boolean writePermission, boolean ownerPermission) {
        logger.info("Update permission to user {} with read {}, write {}, owner {} in Permission Page", userName, readPermission, writePermission, ownerPermission);
        // Ensure users tab is active
        clickOnTabOnPermissionPage("users");

        // Find the row for the specific user by name in the left pinned column
        final WebElement userRow = userTabPaneInPermissionPage.findElement(By.xpath(userRowXpathTemplate.replace("{userName}", userName)));
        final String rowIndex = userRow.getAttribute("row-index");

        // Permissions to update
        final String[] permissions = {"read", "write", "owner"};
        final boolean[] permissionValues = {readPermission, writePermission, ownerPermission};
        final String[] dataTestIds = {readCheckboxDataTestId, writeCheckboxDataTestId, ownerCheckboxDataTestId};

        for (int i = 0; i < permissions.length; i++) {
            final String cellXpath = permissionCellXpathTemplate.replace("{rowIndex}", rowIndex).replace("{permission}", permissions[i]);
            final WebElement cell = userTabPaneInPermissionPage.findElement(By.xpath(cellXpath));
            final WebElement checkbox = cell.findElement(By.cssSelector(dataTestIds[i]));
            if (checkbox.isSelected()!=permissionValues[i]) {
                waitTillClickableWithFluentWait(checkbox).click();
            }
        }
    }

    @Step("Get permissions for user {userName} in Permission Page")
    public Map<String, Boolean> getPermissionsForUser(String userName) {
        logger.info("Get permissions for user {} in Permission Page", userName);
        // Ensure users tab is active
        clickOnTabOnPermissionPage("users");

        // Find the row for the specific user by name in the left pinned column
        final WebElement userRow = userTabPaneInPermissionPage.findElement(By.xpath(userRowXpathTemplate.replace("{userName}", userName)));
        final String rowIndex = userRow.getAttribute("row-index");

        // Permissions to check
        final String[] permissions = {"read", "write", "owner"};
        final String[] dataTestIds = {readCheckboxDataTestId, writeCheckboxDataTestId, ownerCheckboxDataTestId};

        final Map<String, Boolean> permissionMap = new HashMap<>();
        for (int i = 0; i < permissions.length; i++) {
            final String cellXpath = permissionCellXpathTemplate.replace("{rowIndex}", rowIndex).replace("{permission}", permissions[i]);
            final WebElement cell = userTabPaneInPermissionPage.findElement(By.xpath(cellXpath));
            final WebElement checkbox = cell.findElement(By.cssSelector(dataTestIds[i]));
            permissionMap.put(permissions[i], checkbox.isSelected());
        }
        return permissionMap;
    }

    @Step("Get permissions for group {groupName} in Permission Page")
    public Map<String, Boolean> getPermissionsForGroup(String groupName) {
        logger.info("Get permissions for group {} in Permission Page", groupName);
        // Ensure groups tab is active
        clickOnTabOnPermissionPage("groups");

        // Find the row for the specific group by name in the left pinned column
        final WebElement groupRow = groupTabPaneInPermissionPage.findElement(By.xpath(groupRowXpathTemplate.replace("{groupName}", groupName)));
        final String rowIndex = groupRow.getAttribute("row-index");

        // Permissions to check
        final String[] permissions = {"read", "write", "owner"};
        final String[] dataTestIds = {readCheckboxDataTestId, writeCheckboxDataTestId, ownerCheckboxDataTestId};

        final Map<String, Boolean> permissionMap = new HashMap<>();
        for (int i = 0; i < permissions.length; i++) {
            final String cellXpath = permissionCellXpathTemplate.replace("{rowIndex}", rowIndex).replace("{permission}", permissions[i]);
            final WebElement cell = groupTabPaneInPermissionPage.findElement(By.xpath(cellXpath));
            final WebElement checkbox = cell.findElement(By.cssSelector(dataTestIds[i]));
            permissionMap.put(permissions[i], checkbox.isSelected());
        }
        return permissionMap;
    }

    /*
     * @description Core helper method to extract records from a grid table with virtual scrolling; handles both center and left columns, badge vs text values, and ensures uniqueness and correct order of records
     * tabLocator: the WebElement representing the tab pane containing the grid
     * gridWrapper: the locator for the grid wrapper element within the tab
     * centerCells: list of locators for the desired center cell values to extract (in order)
     * preSourceCount: number of center cells before the source-related cells (used to determine where source info is located among center cells)
     * builder: a functional interface to build the final record map from extracted values
     * sleepMs: milliseconds to sleep between scrolls to allow content to load
     * logMessage: message template for logging the number of records collected, should include a placeholder for the count (e.g. "Collected {} unique records")
     * @return a list of maps representing the extracted records, with keys and values determined by the provided builder; records are unique and ordered as they appear in the grid
     */
    private List<Map<String, String>> getRecordsFromGridtable(
            WebElement tabLocator,
            By gridWrapper,
            List<By> centerCells,
            int preSourceCount,
            RecordBuilder builder,
            int sleepMs,
            String logMessage) {

        final WebElement grid = tabLocator.findElement(gridWrapper);
        final WebElement center = grid.findElement(CENTER_CONTAINER);
        final WebElement left = grid.findElement(LEFT_CONTAINER);
        final WebElement viewport = grid.findElement(VERTICAL_VIEWPORT);

        final List<Map<String, String>> records = new ArrayList<>();
        final Set<String> processedRowIds = new HashSet<>();

        final List<WebElement> allRows = scrollGridAndCollectRows(center, viewport, sleepMs);

        // iterate over all collected rows to extract data; maintain the actual order from the grid, skipping duplicates
        for (final WebElement row : allRows) {
            final String rowId = waitForElementToBeVisible(row).getAttribute("row-index");
            if (rowId==null || rowId.trim().isEmpty() || !processedRowIds.add(rowId)) continue;

            // read center cells
            final String[] centerValues = new String[centerCells.size()];
            for (int i = 0; i < centerCells.size(); i++) {
                centerValues[i] = readValueFromGridCell(row, centerCells.get(i));
            }

            // determine source value by checking badge first, then falling back to cell text; use preSourceCount to know where source-related cells start
            String source = centerValues[preSourceCount];
            if (source==null || source.trim().isEmpty()) source = centerValues[preSourceCount + 1];
            if (source==null || source.trim().isEmpty()) source = centerValues[preSourceCount + 2];

            // read left cell for name; use row-index to find corresponding left cell; if not found, skip this record since name is essential for mapping and we cannot correlate without it
            String name = "";
            try {
                final String selector = LEFT_CELL_SELECTOR_TEMPLATE.replace("{rowId}", rowId.replace("'", "\\'"));
                final WebElement leftCell = left.findElement(By.cssSelector(selector));
                name = preferTitleThenText(leftCell);
            } catch (NoSuchElementException ignored) {
                logger.info("Unable to find name cell for row-index {}, skipping record", rowId);
            }

            if (name!=null && !name.trim().isEmpty()) {
                Map<String, String> record = builder.build(name, centerValues, source);
                record.put("rowIndex", rowId);
                records.add(record);
            }
        }

        // sort records by row-index to ensure correct order
        records.sort(Comparator.comparingInt(r -> Integer.parseInt(r.get("rowIndex"))));

        logger.info(logMessage, records.size());
        logger.info("Records collected: {}", records);
        return records;
    }

// ---------- helpers (shared) ----------

    /*
     * @description Helper to scroll through a grid with virtual scrolling and collect all row elements; handles scrolling logic to ensure all rows are loaded and collected, with safeguards against infinite loops
     * center: the WebElement representing the center container of the grid where rows are located
     * viewport: the WebElement representing the scrollable viewport of the grid
     * sleepMs: milliseconds to sleep between scrolls to allow content to load
     * @return a list of WebElements representing all rows collected from the grid during scrolling; may contain duplicates if rows are reloaded during scrolling, so caller should handle uniqueness if needed
     */
    private List<WebElement> scrollGridAndCollectRows(WebElement center, WebElement viewport, int sleepMs) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;

        long lastTop = -1;
        int guard = 0;
        final int MAX_SCROLLS = 500;

        final List<WebElement> allRows = new ArrayList<>();

        while (guard++ < MAX_SCROLLS) {

            // collect current visible rows
            final List<WebElement> rows = center.findElements(CENTER_ROWS);
            allRows.addAll(rows);

            final long top = ((Number) js.executeScript("return arguments[0].scrollTop;", viewport)).longValue();
            final long clientH = ((Number) js.executeScript("return arguments[0].clientHeight;", viewport)).longValue();
            final long scrollH = ((Number) js.executeScript("return arguments[0].scrollHeight;", viewport)).longValue();

            final boolean atBottom = (top + clientH) >= (scrollH - 1);

            // if at bottom, do a small scroll up and down to trigger potential loading of more rows, then check if scroll position changed; if not, we are done
            if (atBottom) {
                js.executeScript("arguments[0].scrollTop = Math.max(0, arguments[0].scrollTop - 20);", viewport);
                js.executeScript("arguments[0].scrollTop = arguments[0].scrollTop + 20;", viewport);
                DriverFactory.sleep(sleepMs);

                final long after = ((Number) js.executeScript("return arguments[0].scrollTop;", viewport)).longValue();
                if (after==top || after==lastTop) break;
            } else {
                js.executeScript("arguments[0].scrollTop = arguments[0].scrollTop + arguments[0].clientHeight;", viewport);
                DriverFactory.sleep(sleepMs);
            }

            final long newTop = ((Number) js.executeScript("return arguments[0].scrollTop;", viewport)).longValue();
            if (newTop==lastTop) break;
            lastTop = newTop;
        }
        return allRows;
    }

    /*
     * @Description Helper to build a group record map with only non-empty values; used for both center and left columns
     * return Map with keys: groupName (from left), description (from center), source (from center badge or text)
     */
    private static Map<String, String> buildGroupRecord(String groupName, String description, String source) {
        final Map<String, String> rec = new LinkedHashMap<>();
        rec.put("groupName", groupName);
        if (description!=null && !description.trim().isEmpty()) rec.put("description", description);
        if (source!=null && !source.trim().isEmpty()) rec.put("source", source);
        return rec;
    }

    /*
     * @description Helper to build a user record map with only non-empty values; used for both center and left columns
     * return Map with keys: userName (from left), fullName, email, description, source (all from center)
     */
    private static Map<String, String> buildUserRecord(String userName, String fullName, String email,
                                                       String description, String source) {
        final Map<String, String> rec = new LinkedHashMap<>();
        rec.put("userName", userName);
        if (fullName!=null && !fullName.trim().isEmpty()) rec.put("fullName", fullName);
        if (email!=null && !email.trim().isEmpty()) rec.put("email", email);
        if (description!=null && !description.trim().isEmpty()) rec.put("description", description);
        if (source!=null && !source.trim().isEmpty()) rec.put("source", source);
        return rec;
    }

    /*
     * @description Helper to read a value from a cell, preferring badge text if present, then falling back to cell text; returns empty string if neither is available
     * scope: the row element to search within
     * locator: the specific locator for the desired value (e.g. badge or cell text)
     */
    private static String readValueFromGridCell(WebElement scope, By locator) {
        try {
            final WebElement el = scope.findElement(locator);
            final String title = el.getAttribute("title");
            if (title!=null && !title.trim().isEmpty()) return title.trim();
            final String txt = el.getText();
            return txt==null || txt.trim().isEmpty() ? "":txt.trim();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    /*
     * @description Helper to read a value from a cell, preferring the 'title' attribute if present (to capture truncated text), then falling back to visible text; returns empty string if neither is available
     * scope: the cell element to read from
     * @return the preferred text value for the cell, with whitespace trimmed; empty string if no meaningful value is found
     */
    private static String preferTitleThenText(WebElement el) {
        final String t = el.getAttribute("title");
        if (t!=null && !t.trim().isEmpty()) return t.trim();
        final String txt = el.getText();
        return txt==null || txt.trim().isEmpty() ? "":txt.trim();
    }

    /*
     * @description Helper to get the appropriate pane selector based on the tab name; also ensures the correct tab is active by clicking it if necessary
     * tabName: the name of the tab ("groups" or "users") for which to get the pane selector
     * @return the dataTestid selector string for the corresponding tab pane in the Add User/Group modal
     * @throws IllegalArgumentException if an unknown tab name is provided
     */
    private String getTabDataTestIDSelectorAddUserGroupModel(String tabName) {
        switch (tabName.toLowerCase()) {
            case "groups":
                clickOnTabOnAddUserGroupModal("Groups");
                return group_tab_dataTestId;
            case "users":
                clickOnTabOnAddUserGroupModal("Users");
                return user_tab_dataTestId;
            default:
                throw new IllegalArgumentException("Unknown tab: " + tabName);
        }
    }

    private WebElement getTabDataTestIDSelectorPermissionPage(String tabName) {
        return tabName.equalsIgnoreCase("groups") ? groupTabPaneInPermissionPage:userTabPaneInPermissionPage;
    }

    @Step("Get search placeholder in Add User/Group modal...")
    public String getSearchPlaceholderInAddUserGroupModal(String userOrGroup) {
        try {
            final String searchInput = addGroupUsersPlaceholder.replace("{userOrGroup}", userOrGroup);
            final WebElement searchInputElement = waitForElementToBePresent(By.xpath(searchInput));
            final String placeholder = searchInputElement.getAttribute("placeholder");
            logger.info("Search placeholder in Add User/Group modal: {}", placeholder);
            return placeholder != null ? placeholder.trim() : "";
        } catch (Exception e) {
            logger.warn("Unable to get search placeholder from Add User/Group modal", e);
            return "";
        }
    }

    @Step("Search for {searchTerm} in Add User/Group modal...")
    public void searchInAddUserGroupModal(String searchTerm, String userOrGroup) {
        try {
            final String searchInput = addGroupUsersPlaceholder.replace("{userOrGroup}", userOrGroup);
            final WebElement searchInputElement = waitForElementToBePresent(By.xpath(searchInput));
            inputText(searchInputElement, searchTerm);
            DriverFactory.sleep(400);
            logger.info("Searched for '{}' in Add User/Group modal", searchTerm);
        } catch (Exception e) {
            logger.warn("Unable to search for '{}' in Add User/Group modal", searchTerm, e);
        }
    }

    @Step("Get search term in Add User/Group modal...")
    public String getSearchTermInAddUserGroupModal(String userOrGroup) {
        try {
            final String searchInput = addGroupUsersPlaceholder.replace("{userOrGroup}", userOrGroup);
            final WebElement searchInputElement = waitForElementToBePresent(By.xpath(searchInput));
            final String searchTerm = searchInputElement.getAttribute("value");
            logger.info("Search term in Add User/Group modal: {}", searchTerm);
            return searchTerm != null ? searchTerm.trim() : "";
        } catch (Exception e) {
            logger.warn("Unable to get search term from Add User/Group modal", e);
            return "";
        }
    }

    @Step("Clear search in Add User/Group modal for {userOrGroup}...")
    public void clearSearchInAddUserGroupModal(String userOrGroup) {
        final String searchInput = addGroupUsersPlaceholder.replace("{userOrGroup}", userOrGroup);
        final WebElement searchInputElement = waitForElementToBePresent(By.xpath(searchInput));
        clearInputFieldWithBackspace(searchInputElement);
    }

    @Step("Get no results message in Add User/Group modal for {userOrGroup}...")
    public String getNoResultsMessageInAddUserGroupModal(String userOrGroup) {
        try {
            final String noResultMessage = noResultFoundMessage.replace("{userOrGroup}", userOrGroup);
            final WebElement noResultFoundMessageElement = waitForElementToBePresent(By.xpath(noResultMessage));
            final String text = noResultFoundMessageElement.getText();
            logger.info("No results message in Add User/Group modal for {}: {}", userOrGroup, text);
            return text != null ? text.trim() : "";
        } catch (Exception e) {
            logger.warn("Unable to get no results message in Add User/Group modal for {}", userOrGroup, e);
            return "";
        }
    }

    @Step("Verify if {itemName} is present in {tabName} tab of Permission Page...")
    public boolean isUserOrGroupPresentInTable(String itemName, String tabName) {
        try {
            logger.info("Verify if {} is present in {} tab of Permission Page", itemName, tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final String xpath = tabName.equalsIgnoreCase("users") ?
                    userRowXpathTemplate.replace("{userName}", itemName) :
                    groupRowXpathTemplate.replace("{groupName}", itemName);

            final WebElement row = waitForElementToBePresent(By.xpath(xpath));
            return row != null && row.isDisplayed();
        } catch (NoSuchElementException e) {
            logger.info("{} not found in {} tab of Permission Page", itemName, tabName);
            return false;
        } catch (Exception e) {
            logger.warn("Error while checking if {} is present in {} tab of Permission Page", itemName, tabName, e);
            return false;
        }
    }

    @Step("Verify if row for {itemName} is highlighted in {tabName} tab of Permission Page...")
    public boolean isRowHighlighted(String itemName, String tabName) {
        try {
            logger.info("Verify if row for {} is highlighted in {} tab of Permission Page", itemName, tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final String xpath = tabName.equalsIgnoreCase("users") ?
                    userRowXpathTemplate.replace("{userName}", itemName) :
                    groupRowXpathTemplate.replace("{groupName}", itemName);

            final WebElement row = waitForElementToBePresent(By.xpath(xpath));
            final String highlightClass = row.getAttribute("class");
            final boolean isHighlighted = highlightClass != null && highlightClass.contains("highlight") || highlightClass.contains("new-row");

            logger.info("Row for {} in {} tab is highlighted: {}", itemName, tabName, isHighlighted);
            return isHighlighted;
        } catch (NoSuchElementException e) {
            logger.warn("{} row not found in {} tab of Permission Page", itemName, tabName);
            return false;
        } catch (Exception e) {
            logger.warn("Error while checking if row for {} is highlighted in {} tab of Permission Page", itemName, tabName, e);
            return false;
        }
    }

    @Step("Remove {itemNames} from {tabName} tab of Permission Page...")
    public void removeUsersOrGroupsFromTable(List<String> itemNames, String tabName) {
        logger.info("Remove {} from {} tab of Permission Page", itemNames, tabName);
        clickOnTabOnPermissionPage(tabName);
        final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);

        for (String itemName : itemNames) {
            final String rowXpath = tabName.equalsIgnoreCase("users")
                    ? userRowXpathTemplate.replace("{userName}", itemName)
                    : groupRowXpathTemplate.replace("{groupName}", itemName);
            try {
                final WebElement row = waitForElementToBePresent(By.xpath(rowXpath));
                if (row == null || !row.isDisplayed()) continue;
                // 1 Select checkbox in front of user/group
                final WebElement checkbox = waitTillClickableWithFluentWait(row.findElement(checkboxBy));
                checkbox.click();
            } catch (Exception e) {
                logger.warn("Unable to select {} from {} tab of Permission Page", itemName, tabName, e);
            }
        }

        try {
            // Click on remove button
            clickOnRemoveUserGroupButton();
            // Click on remove button on confirmation popup
            clickOnConfirmRemoveUserGroupButton();
            waitForPageLoaded();
        } catch (Exception e) {
            logger.warn("Unable to remove items from {} tab of Permission Page", tabName, e);
        }
    }

    @Step("Click on Remove User/Group button...")
    public void clickOnRemoveUserGroupButton() {
        logger.info("Click on Remove User/Group button");
        waitTillClickableWithFluentWait(removeButton).click();
    }

    @Step("Click on remove button on confirmation popup...")
    public void clickOnConfirmRemoveUserGroupButton() {
        logger.info("Click on Remove Confirmation User/Group button");
        waitTillClickableWithFluentWait(confirmRemoveButton).click();
    }

    @Step("Verify review update permissions modal title is: {0} ...")
    public String verifyReviewUpdatePermissionsModalTitle() {
        try {
            waitForElementToBeVisible(reviewUpdatePermissionsModalTitle);
            String title = reviewUpdatePermissionsModalTitle.getText().trim();
            logger.info("Review update permissions modal title: {}", title);
            return title;
        } catch (Exception e) {
            logger.warn("Failed to get review update permissions modal title: {}", e.getMessage());
            return "";
        }
    }

    @Step("Click review update permissions modal close button...")
    public void clickReviewUpdatePermissionsModalCloseButton() {
        try {
            waitTillClickableWithFluentWait(reviewUpdatePermissionsModalCancelButton).click();
            logger.info("Clicked review update permissions modal close button.");
        } catch (Exception e) {
            logger.warn("Failed to click review update permissions modal close button: {}", e.getMessage());
        }
    }

    @Step("Validate property set name on review update permissions modal title is: {0} ...")
    public String isPropertySetNameOnReviewUpdatePermissionsModalCorrect() {
      try {
        waitForElementToBeVisible(propertySetNameOnreviewUpdatePermissionsModal);
        String title = propertySetNameOnreviewUpdatePermissionsModal.getText().trim();
        logger.info("Review update permissions modal title: {}", title);
        return title;
    } catch (Exception e) {
        logger.warn("Failed to get review update permissions modal title: {}", e.getMessage());
        return "";
    }
}

    @Step("Verify column heading '{0}' is displayed on review update permissions modal...")
    public boolean isColumnHeadingDisplayedOnReviewUpdatePermissionsModal(String columnName) {
        String xpath = columnHeadingName.replace("{ColumnName}", columnName);
        try {
            WebElement heading = waitForElementToBePresent(By.xpath(xpath));
            boolean displayed = heading.isDisplayed();
            logger.info("Column heading '{}' displayed: {}", columnName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Column heading '{}' not found or not displayed: {}", columnName, e.getMessage());
            return false;
        }
    }

    @Step("Verify category for {groupOrUserName} is '{expectedCategory}' in review update permissions modal...")
    public boolean isCategoryCorrectOnReviewUpdatePermissionsModal(String groupOrUserName, String expectedCategory) {
        String xpath = categoryName.replace("{GroupOrUserName}", groupOrUserName);
        try {
            WebElement categoryElement = waitForElementToBePresent(By.xpath(xpath));
            String actualCategory = categoryElement.getText().trim();
            boolean result = actualCategory.equals(expectedCategory);
            logger.info("Category for '{}' is '{}', expected '{}': {}", groupOrUserName, actualCategory, expectedCategory, result);
            return result;
        } catch (Exception e) {
            logger.warn("Failed to verify category for '{}': {}", groupOrUserName, e.getMessage());
            return false;
        }
    }

    @Step("Verify success toast message starts with expected prefix")
    public boolean isPropertySetUpdatedSuccessToastDisplayed() {
        try {
            waitForElementToBeVisible(successToastMessage);
            String actualMessage = successToastMessage.getText().trim();
            String expectedPrefix = "Saved Permissions updated for Property Set ";
            boolean result = actualMessage.startsWith(expectedPrefix);
            logger.info("Success toast message: '{}', starts with '{}': {}", actualMessage, expectedPrefix, result);
            return result;
        } catch (Exception e) {
            logger.warn("Success toast message not displayed or incorrect: {}", e.getMessage());
            return false;
        }
    }

    @Step("Uncheck '{permissionType}' checkbox on user tab if checked...")
    public void uncheckPermissionCheckboxOnUserTabIfChecked(String permissionType) {
        DriverFactory.sleep(500);  //Need to improve
        String xpath = permissionCheckBoxOnUserTab.replace("{permissionType}", permissionType);
        WebElement checkbox = waitForElementToBePresent(By.xpath(xpath));
        if (checkbox.isSelected()) {
            waitTillClickableWithFluentWait(checkbox);
            clickWithJS(checkbox);
            logger.info("Unchecked '{}' checkbox on user tab.", permissionType);
        } else {
            logger.info("'{}' checkbox on user tab is already unchecked.", permissionType);
        }
    }

    @Step("Verify permission '{permissionType}' is displayed for group user '{groupUserName}' in review update permissions modal...")
    public boolean isBeforePermissionDisplayedForGroupUserOnReviewUpdatePermissionsModal(String groupUserName, String permissionType) {
        String xpath = beforePermissionOnPermissionReviewModal
                .replace("{groupUserName}", groupUserName)
                .replace("{permissionType}", permissionType);
        try {
            WebElement permissionElement = waitForElementToBePresent(By.xpath(xpath));
            boolean displayed = permissionElement.isDisplayed();
            logger.info("Permission '{}' for group user '{}' is displayed: {}", permissionType, groupUserName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Permission '{}' for group user '{}' not found: {}", permissionType, groupUserName, e.getMessage());
            return false;
        }
    }

    @Step("Verify permission '{permissionType}' is displayed for group user '{groupUserName}' in review update permissions modal...")
    public boolean isAfterPermissionDisplayedForGroupUserOnReviewUpdatePermissionsModal(String groupUserName, String permissionType) {
        String xpath = afterPermissionOnPermissionReviewModal
                .replace("{groupUserName}", groupUserName)
                .replace("{permissionType}", permissionType);
        try {
            WebElement permissionElement = waitForElementToBePresent(By.xpath(xpath));
            boolean displayed = permissionElement.isDisplayed();
            logger.info("Permission '{}' for group user '{}' is displayed: {}", permissionType, groupUserName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Permission '{}' for group user '{}' not found: {}", permissionType, groupUserName, e.getMessage());
            return false;
        }
    }

    @Step("Verify 'no permissions' is displayed for group user '{groupUserName}' in review update permissions modal...")
    public boolean isNoPermissionDisplayedForGroupUserOnReviewUpdatePermissionsModal(String groupUserName) {
        String xpath = noPermissionOnReviewModal.replace("{groupUserName}", groupUserName);
        try {
            WebElement noPermElement = waitForElementToBePresent(By.xpath(xpath));
            boolean displayed = noPermElement.isDisplayed();
            logger.info("'No permissions' for group user '{}' is displayed: {}", groupUserName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("'No permissions' for group user '{}' not found: {}", groupUserName, e.getMessage());
            return false;
        }
    }

    @Step("Uncheck '{permissionType}' checkbox on user tab if checked...")
    public void checkPermissionCheckboxOnUserTabIfUnChecked(String permissionType) {
        String xpath = permissionCheckBoxOnGroupTab.replace("{permissionType}", permissionType);
        WebElement checkbox = waitForElementToBePresent(By.xpath(xpath));
            waitTillClickableWithFluentWait(checkbox).click();
            logger.info("Unchecked '{}' checkbox on user tab.", permissionType);
    }

    @Step("Verify selection checkboxes are visible in {tabName} tab...")
    public boolean areSelectionCheckboxesVisible(String tabName) {
        try {
            logger.info("Verify selection checkboxes are visible in {} tab", tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final List<WebElement> checkboxes = tabPane.findElements(checkboxBy);
            return !checkboxes.isEmpty() && checkboxes.get(0).isDisplayed();
        } catch (Exception e) {
            logger.warn("Error while verifying selection checkboxes visibility in {} tab", tabName, e);
            return false;
        }
    }

    @Step("Select row(s) in {tabName} tab of Permission Page...")
    public void selectRowsInPermissionTable(List<String> itemNames, String tabName) {
        try {
            logger.info("Select rows in {} tab of Permission Page: {}", tabName, itemNames);
            clickOnTabOnPermissionPage(tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);

            for (String itemName : itemNames) {
                final String rowXpath = tabName.equalsIgnoreCase("users")
                        ? userRowXpathTemplate.replace("{userName}", itemName)
                        : groupRowXpathTemplate.replace("{groupName}", itemName);

                final WebElement row = waitForElementToBePresent(By.xpath(rowXpath));
                if (row != null && row.isDisplayed()) {
                    final WebElement checkbox = waitTillClickableWithFluentWait(row.findElement(checkboxBy));
                    checkbox.click();
                }
            }
        } catch (Exception e) {
            logger.warn("Error while selecting rows in {} tab of Permission Page", tabName, e);
        }
    }

    @Step("Select single row in {tabName} tab of Permission Page...")
    public void selectSingleRowInPermissionTable(String itemName, String tabName) {
        selectRowsInPermissionTable(List.of(itemName), tabName);
    }

    @Step("Verify if Bulk Actions bar is visible...")
    public boolean isBulkActionsBarVisible(String tabName) {
        try {
            logger.info("Verify if Bulk Actions bar is visible");
            final List<WebElement> elements = driver.findElements(By.xpath(bulkActionRemoveButton.replace("{tabName}", tabName)));
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            logger.info("Bulk Actions bar is not visible");
            return false;
        }
    }

    @Step("Verify if Bulk Actions bar is hidden...")
    public boolean isBulkActionsBarHidden(String tabName) {
        try {
            logger.info("Verify if Bulk Actions bar is hidden");
            final List<WebElement> elements = driver.findElements(By.xpath(bulkActionRemoveButton.replace("{tabName}", tabName)));
            return elements.isEmpty() || !elements.get(0).isDisplayed();
        } catch (Exception e) {
            logger.info("Bulk Actions bar is not found");
            return true;
        }
    }

    @Step("Verify if row is highlighted in {tabName} tab of Permission Page...")
    public boolean isRowHighlightedAfterSelection(String itemName, String tabName) {
        try {
            logger.info("Verify if row for {} is highlighted in {} tab after selection", itemName, tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final String xpath = tabName.equalsIgnoreCase("users")
                    ? userRowXpathTemplate.replace("{userName}", itemName)
                    : groupRowXpathTemplate.replace("{groupName}", itemName);

            final WebElement row = waitForElementToBePresent(By.xpath(xpath));
            final String rowClass = row.getAttribute("class");
            return rowClass != null && (rowClass.contains("ag-row-selected") || rowClass.contains("selected"));
        } catch (Exception e) {
            logger.warn("Error while checking if row for {} is highlighted in {} tab", itemName, tabName, e);
            return false;
        }
    }

    @Step("Clear all row selections using X button on Bulk Actions bar...")
    public void clearAllSelectionsUsingBulkActionsBar(String tabName) {
        try {
            logger.info("Clear all row selections using X button on Bulk Actions bar");
            final WebElement closeButton = driver.findElement(By.xpath(bulkActionCloseButton.replace("{tabName}", tabName)));
            waitTillClickableWithFluentWait(closeButton).click();
            logger.info("Cleared all selections using Bulk Actions bar X button");
        } catch (Exception e) {
            logger.warn("Unable to clear selections using Bulk Actions bar X button", e);
        }
    }

    @Step("Verify all rows are deselected in {tabName} tab...")
    public boolean areAllRowsDeselected(String tabName) {
        try {
            logger.info("Verify all rows are deselected in {} tab", tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final List<WebElement> selectedRowsList = tabPane.findElements(By.xpath(selectedRows.replace("{tabName}", tabName.toLowerCase())));
            return selectedRowsList.isEmpty();
        } catch (Exception e) {
            logger.warn("Error while verifying if all rows are deselected in {} tab", tabName, e);
            return false;
        }
    }

    @Step("Get count of selected rows in {tabName} tab...")
    public int getCountOfSelectedRows(String tabName) {
        try {
            logger.info("Get count of selected rows in {} tab", tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final List<WebElement> selectedRowsList = tabPane.findElements(By.xpath(selectedRows.replace("{tabName}", tabName.toLowerCase())));
            logger.info("Count of selected rows in {} tab: {}", tabName, selectedRowsList.size());
            return selectedRowsList.size();
        } catch (Exception e) {
            logger.warn("Error while getting count of selected rows in {} tab", tabName, e);
            return 0;
        }
    }

    @Step("Get count of visible rows in {tabName} tab...")
    public int getCountOfVisibleRows(String tabName) {
        try {
            logger.info("Get count of visible rows in {} tab", tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final List<WebElement> visibleRowList = tabPane.findElements(By.xpath(visibleRows.replace("{tabName}", tabName.toLowerCase())));
            logger.info("Count of visible rows in {} tab: {}", tabName, visibleRowList.size());
            return visibleRowList.size();
        } catch (Exception e) {
            logger.warn("Error while getting count of visible rows in {} tab", tabName, e);
            return 0;
        }
    }

    @Step("Get Read permission state in {tabName} tab...")
    public String getBulkActionPermissionState(String tabName, String permissionsType) {
        try {
            logger.info("Get Read permission state in {} tab", tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final WebElement permissionCheckbox = waitTillVisibleWithFluentWait(tabPane.findElement(By.xpath(permissionCheckboxBulkAction.replace("{permissionType}", permissionsType.toLowerCase()))));
            boolean isChecked = permissionCheckbox.isSelected();
            // If selected, return "selected"
            if (isChecked) {
                return "selected";
            }
            // Check indeterminate via JS property (works with :indeterminate styles)
            boolean isIndeterminate = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].indeterminate === true;", permissionCheckbox);
            if (isIndeterminate) {
                return "indeterminate";
            }
            // Otherwise, it's unset
            return "unset";

        } catch (Exception e) {
            logger.warn("Error while getting {} permission state in {} tab", permissionsType, tabName, e);
            return "";
        }
    }

    @Step("Click {permissionType} in Bulk Actions bar for {tabName} tab...")
    public void clickBulkActionPermission(String tabName, String permissionType) {
        try {
            logger.info("Click {} in Bulk Actions bar for {} tab", permissionType, tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);
            final WebElement permissionCheckbox = waitTillClickableWithFluentWait(
                    tabPane.findElement(By.xpath(permissionCheckboxBulkAction.replace("{permissionType}", permissionType.toLowerCase())))
            );
            permissionCheckbox.click();
        } catch (Exception e) {
            logger.warn("Unable to click {} in Bulk Actions bar for {} tab", permissionType, tabName, e);
        }
    }

    @Step("Unselect row(s) in {tabName} tab of Permission Page...")
    public void deselectRowsInPermissionTable(List<String> itemNames, String tabName) {
        try {
            logger.info("Unselect rows in {} tab of Permission Page: {}", tabName, itemNames);
            clickOnTabOnPermissionPage(tabName);
            final WebElement tabPane = getTabDataTestIDSelectorPermissionPage(tabName);

            for (String itemName : itemNames) {
                final String rowXpath = tabName.equalsIgnoreCase("users")
                        ? userRowXpathTemplate.replace("{userName}", itemName)
                        : groupRowXpathTemplate.replace("{groupName}", itemName);

                final WebElement row = waitForElementToBePresent(By.xpath(rowXpath));
                if (row != null && row.isDisplayed()) {
                    final String rowClass = row.getAttribute("class");
                    if (rowClass != null && (rowClass.contains("ag-row-selected") || rowClass.contains("selected"))) {
                        final WebElement checkbox = waitTillClickableWithFluentWait(row.findElement(checkboxBy));
                        checkbox.click();
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Error while unselecting rows in {} tab of Permission Page", tabName, e);
        }
    }

    @Step("Verify permission type column elements are present and disabled for rowIndex: {rowIndex} and permission: {permission}...")
    public boolean isPermissionTypeColumnPresent(String rowIndex, String permission) {
        final String xpath = permissionTypeColumnXpath
                .replace("{rowIndex}", rowIndex)
                .replace("{permission}", permission);
        logger.info("Checking presence of permissionTypeColumn elements with locator: {}", xpath);
        final List<WebElement> elements = driver.findElements(By.xpath(xpath));
        logger.info("Found {} element(s) for rowIndex: {} and permission: {}", elements.size(), rowIndex, permission);
        return !elements.isEmpty();
    }

    @Step("Verify permission type column elements are present and enabled for rowIndex: {rowIndex} and permission: {permission}...")
    public boolean isPermissionTypeColumnPresentAndEnabled(String rowIndex, String permission) {
        final String xpath = permissionCellXpathTemplate
                .replace("{rowIndex}", rowIndex)
                .replace("{permission}", permission)
                + "//input";
        logger.info("Checking presence and enabled state of permissionCell input with locator: {}", xpath);
        final List<WebElement> elements = driver.findElements(By.xpath(xpath));
        logger.info("Found {} input element(s) for rowIndex: {} and permission: {}", elements.size(), rowIndex, permission);
        if (elements.isEmpty()) {
            logger.warn("No input element found for rowIndex: {} and permission: {}", rowIndex, permission);
            return false;
        }
        final WebElement input = elements.get(0);
        final boolean isEnabled = input.isEnabled() && !"true".equals(input.getAttribute("disabled"));
        logger.info("Input element for rowIndex: {} and permission: {} is enabled: {}", rowIndex, permission, isEnabled);
        return isEnabled;
    }
}
