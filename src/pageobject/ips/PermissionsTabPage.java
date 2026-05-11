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
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PermissionsTabPage extends BasePage {
    private WebDriver driver;

    public PermissionsTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[contains(@class,'base-input-field__input') and @placeholder='Search Groups']")
    private WebElement searchInputFieldGroups;

    @FindBy(xpath = "//input[contains(@class,'base-input-field__input') and @placeholder='Search Users']")
    private WebElement searchInputFieldUsers;

    @FindBy(xpath = "//a[normalize-space()='Groups']")
    private WebElement groupsButton;

    @FindBy(xpath = "//a[normalize-space()='Groups'][contains(@class, 'active')]")
    private WebElement groupsTabActive;

    @FindBy(xpath = "//a[normalize-space()='Users']")
    private WebElement usersButton;

    @FindBy(xpath = "//a[normalize-space()='Users'][contains(@class, 'active')]")
    private WebElement usersTabActive;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-header-label-icon ag-sort-ascending-icon']")
    private WebElement sortAscendingIcon;

    @FindBy(xpath = "//span[@class='ag-header-icon ag-header-label-icon ag-sort-descending-icon']")
    private WebElement sortDescendingIcon;

    @FindBy(xpath = "//span[contains(@class, 'ag-sort')]")
    private List<WebElement> sortingIcons;

    @FindBy(xpath = "//div[contains(@class,'permission-tab-users')]//div[@row-index='0']/div[@col-id='name']")
    private WebElement firstRowNameCellUser;

    @FindBy(xpath = "//div[contains(@class,'permission-tab-groups')]//div[@row-index='0']/div[@col-id='name']")
    private WebElement firstRowNameCellGroup;

    @FindBy(xpath = "//h5[contains(@class,'no-rows-overlay__text') and normalize-space()='No Users available']")
    private WebElement noUsersAvailable;

    @FindBy(xpath = "//h5[contains(@class,'no-rows-overlay__text') and normalize-space()='No results found']")
    private WebElement noResultsFound;

    @FindBy(xpath = "//div[@class='resources-permission']//button[contains(@class,'dropdown-toggle')]")
    private WebElement applyDropDown;

    @FindBy(xpath = "//div[@class='resources-permission']")
    private WebElement permTable;

    @FindBy(xpath = "//footer[contains(@class,'message-box__footer')]//button[normalize-space()='Apply to IP tree Lines']")
    private WebElement confirmApplyToTree;

    @FindBy(xpath = "//button[normalize-space()='Discard changes']")
    private WebElement discardChangesButton;

    @FindBy(xpath = "//button[normalize-space()='Discard changes' and contains(@class,'disabled')]")
    private WebElement discardChangesButtonDisabled;

    @FindBy(xpath = "//button[normalize-space()='Discard changes' and @data-testid='ui-btn-ui-modal-discard-changes']")
    private WebElement discardChangesDialogButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel' and  @data-testid='ui-btn-ui-modal-cancel']")
    private WebElement cancelDiscardChangesButton;

    @FindBy(xpath = "//div[@data-testid='ui-modal']//h5[normalize-space(text())='Discard unsaved changes']")
    private WebElement discardChangesDialog;

    private String userRows = "//div[contains(@class,'permission-tab-users')]//div[@col-id='name'][contains(@class, 'ag-cell-value')]";
    private String groupRows = "//div[contains(@class,'permission-tab-groups')]//div[@col-id='name'][contains(@class, 'ag-cell-value')]";
    private String userCell = "//div[@col-id='name'][normalize-space()='{name}']";
    private String disabledCheckBox = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='{colId}']//input[@disabled]/ancestor::div[@role='gridcell']";
    private String notCheckedCheckbox = "//span[normalize-space()='{name}']//ancestor::div/div[@col-id='{colId}']//*[contains(@class,'checkbox-normal_unchecked')]/ancestor::div[@role='gridcell']";
    private String setPermission = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='{colId}']//input[@checked]/ancestor::div[@role='gridcell']";
    private String notSetPermission = "//span[normalize-space()='{name}']//ancestor::div/div[@col-id='{colId}']//input[not(@checked)]/following-sibling::label";
    private String checkedCheckbox = "//span[normalize-space()='{name}']//ancestor::div/div[@col-id='{colId}']//div[contains(@class,'permission-checkbox')]/input";
    private String xpathSaveButton = "//button[normalize-space()='Save updates']";
    private String xpathApplyToTreeButton = "//button[normalize-space()='Apply to IP tree Lines']";
    private String xpathApplyToIpv = "//button[normalize-space()='Apply to IP Line']";

    @Step("Click on Save button...")
    public void clickOnSaveButton() {
        WebElement saveButton = findElementWithWait(By.xpath(xpathSaveButton));
        click(saveButton);
        // sleep added for the stable regression
        DriverFactory.sleep(400);
    }

    @Step("Enter text to the Users search input field...")
    public void enterNameToUsersSearchInputField(String name) {
        inputText(searchInputFieldUsers, name);
    }

    @Step("Enter text to the Groups search input field...")
    public void enterNameToGroupSearchInputField(String name) {
        waitTillVisibleWithFluentWait(searchInputFieldGroups);
        inputText(searchInputFieldGroups, name);
    }

    @Step("Remove text from the search input field...")
    public void removeNameFromSearchInputField() {
        clearInputField(searchInputFieldUsers);
    }

    @Step("Clear the search input field by using Backspace...")
    public void clearUserSearchFieldWithBackspace() {
        clearInputFieldWithBackspace(searchInputFieldUsers);
    }

    @Step("Clear the search input field by using Backspace...")
    public void clearGroupSearchFieldWithBackspace() {
        clearInputFieldWithBackspace(searchInputFieldGroups);
    }

    @Step("Select searched name with mouse click and drag...")
    public void selectSearchedNameWithMouse() {
        selectTextWithMouseClickAndHold(searchInputFieldGroups);
    }

    @Step("Get number of users...")
    public int getNumberOfUsers(int counter) {
        DriverFactory.sleep(1000);
        return waitForNumberOfElementsToBe(By.xpath(userRows), counter).size();
    }

    @Step("Get number of groups...")
    public int getNumberOfGroups(int counter) {
        DriverFactory.sleep(1000);
        return waitForNumberOfElementsToBe(By.xpath(groupRows), counter).size();
    }

    @Step("Click on Groups button...")
    public void clickOnGroupsButton() {
        click(groupsButton);
    }

    @Step("Verify Discard changes button is disabled...")
    public boolean isDiscardChangesButtonDisabled() {
        return isElementVisible(discardChangesButtonDisabled);
    }

    @Step("Verify Discard changes button is enabled...")
    public boolean isDiscardChangesButtonEnabled() {
        return isElementVisible(discardChangesButton);
    }

    @Step("Verify Discard changes dialog is visible...")
    public boolean isDiscardChangesModalVisible() {
        return isElementVisible(discardChangesDialog);
    }

    @Step("Verify Discard changes dialog is not visible...")
    public boolean isDiscardChangesModalHidden() {
        return isElementNotVisible(discardChangesDialog);
    }

    @Step("Click on Discard Changes button...")
    public void clickOnDiscardChangesButton() {
        click(discardChangesButton);
    }

    @Step("Click on Discard Changes dialog button...")
    public void clickOnDiscardChangesDialogButton() {
        waitForElementToBeClickable(discardChangesDialogButton);
        click(discardChangesDialogButton);
        DriverFactory.sleep(1500);
    }

    @Step("Click on Cancel Discard Changes button...")
    public void clickOnCancelDiscardChangesButton() {
        waitForElementToBeClickable(cancelDiscardChangesButton);
        click(cancelDiscardChangesButton);
        DriverFactory.sleep(1500);
    }

    @Step("Click on Users button...")
    public void clickOnUsersButton() {
        DriverFactory.sleep(700);
        click(usersButton);
    }

    @Step("Get search input field value...")
    public String getSearchFieldValue() {
        return getAttribute(searchInputFieldGroups);
    }

    @Step("Verify the user name is displayed...")
    public boolean isGroupDisplayed(String username) {
        WebElement user = findElementWithWait(By.xpath(userCell.replace("{name}", username)));
        return isElementVisible(user);
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

    @Step("Get username from the users first table row...")
    public String getNameFromFirstRowUsers() {
        return getText(firstRowNameCellUser);
    }

    @Step("Get username from the groups first table row...")
    public String getNameFromFirstRowGroups() {
        return getText(firstRowNameCellGroup);
    }

    @Step("Verify Save Permissions button is displayed...")
    public int isSavePermissionsButtonDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath((xpathSaveButton)), counter).size();
    }

    @Step("Verify no users found message is displayed...")
    public boolean noUsersFound() {
        return isElementVisible(noResultsFound);
    }

    @Step("Verify no users available message is displayed...")
    public boolean noUsersAvailable() {
        return isElementVisible(noUsersAvailable);
    }

    @Step("Verify no groups found message is displayed...")
    public boolean noGroupsFound() {
        return isElementVisible(noResultsFound);
    }

    @Step("Verify Users tab is preselected...")
    public boolean isUsersTabPreselected() {
        return isElementVisible(usersTabActive);
    }

    @Step("Verify Groups tab is preselected...")
    public boolean isGroupsTabPreselected() {
        return isElementVisible(groupsTabActive);
    }

    @Step("Verify Line view permissions checkbox is disabled...")
    public boolean isPermissionCheckBoxDisabled(String username, String colName) {
        WebElement permCheckbox = findElementWithWait(By.xpath(disabledCheckBox.replace("{name}", username)
                .replace("{colId}", colName)));
        return isElementVisible(permCheckbox);
    }

    @Step("Setting permission...")
    public void setPermission(String username, String columnId) {
        WebElement checkBox = driver.findElement(By.xpath(notSetPermission.replace("{name}", username).replace("{colId}", columnId)));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkBox);
    }

    @Step("Removing permission...")
    public void removePermission(String username, String columnId) {
        WebElement checkBox = driver.findElement(By.xpath(checkedCheckbox.replace("{name}", username).replace("{colId}", columnId)));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkBox);
    }

    @Step("Verify that permissions checkbox is not checked...")
    public boolean isPermissionNotSet(String username, String columnId) {
        WebElement ownerLinePermissionsSet = findElementWithWait(By.xpath(notCheckedCheckbox.replace("{name}", username)
                .replace("{colId}", columnId)));
        return isElementVisible(ownerLinePermissionsSet);
    }

    @Step("Verify that permissions checkbox is checked...")
    public boolean isPermissionSet(String username, String columnId) {
        WebElement ownerLinePermissionsSet = findElementWithWait(By.xpath(setPermission.replace("{name}", username)
                .replace("{colId}", columnId)));
        return isElementVisible(ownerLinePermissionsSet);
    }

    @Step("Click on apply button...")
    public void clickApplyButton() {
        WebElement applyToIpvButton = findElementWithWait(By.xpath(xpathApplyToIpv));
        waitForElementToBeClickable(applyToIpvButton);
        click(applyToIpvButton);
        DriverFactory.sleep(1500);
    }

    @Step("Set permission to a tree")
    public void setPermissionToTree() {
        WebElement applyToTreeButton = driver.findElement(By.xpath(xpathApplyToTreeButton));
        click(applyToTreeButton);
    }

    @Step("Open IP permissions page...")
    public void openIpPermissionsPage(String ipId) {
        goTo(DriverFactory.getFullUrl("/#/ip/" + ipId + "/permissions"));
    }

    @Step("Confirm apply to tree...")
    public void confirmApplyToTree() {
        waitForElementToBeClickable(confirmApplyToTree);
        click(confirmApplyToTree);
        DriverFactory.sleep(1000);
    }

    @Step("Verify Apply to tree button is displayed...")
    public int isApplyToTreeButtonDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath((xpathApplyToTreeButton)), counter).size();
    }

    @Step("Verify Apply to IPV button is displayed...")
    public int isApplyToIpvButtonDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath((xpathApplyToIpv)), counter).size();
    }

}
