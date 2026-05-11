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
package com.methodics.phi.pageobject.libraries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibraryManagementPage extends BasePage {
    private WebDriver driver;

    public LibraryManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[contains(@class,'edit-library-header')]")
    private WebElement formHeader;

    @FindBy(css = "[data-testid='ui-btn-manage-library-create-new-library']")
    private WebElement createNewLibraryButton;

    @FindBy(css = "[data-testid='ui-btn-manage-library-save-library']")
    private WebElement saveLibraryButton;

    @FindBy(css = "[data-testid='ui-btn-manage-library-save-library'][disabled]")
    private WebElement saveLibraryButtonDisabled;

    @FindBy(css = "[data-testid='ui-btn-manage-library-delete-library']")
    private WebElement deleteLibraryButton;

    @FindBy(css = "[data-testid='ui-btn-manage-library-delete-library'][disabled]")
    private WebElement deleteLibraryButtonDisabled;

    @FindBy(css = "[data-testid='ui-btn-manage-library-cancel-editing']")
    private WebElement cancelEditingButton;

    @FindBy(css = "[data-testid='ui-btn-manage-library-cancel-editing'][disabled]")
    private WebElement cancelEditingButtonDisabled;

    @FindBy(xpath = "//input[contains(@class, 'library-name')]")
    private WebElement nameField;

    @FindBy(xpath = "//input[contains(@class, 'library-vendor')]")
    private WebElement vendorField;

    @FindBy(xpath = "//textarea[contains(@class, 'library-description')]")
    private WebElement descriptionField;

    @FindBy(xpath = "//input[contains(@class, 'hook-pre-release')]")
    private WebElement preReleaseHookField;

    @FindBy(xpath = "//input[contains(@class, 'hook-post-release')]")
    private WebElement postReleaseHookField;

    @FindBy(xpath = "//input[contains(@class, 'hook-post-update')]")
    private WebElement postUpdateHookField;

    @FindBy(xpath = "//input[contains(@class, 'hook-post-load')]")
    private WebElement postLoadHookField;

    @FindBy(xpath = "//span[text()='No IPs found.']")
    private WebElement noIpsMessage;

    @Step("Click on Create New Library button...")
    public void clickOnCreateLibraryButton() {
        click(createNewLibraryButton);
    }

    @Step("Get form header...")
    public String getFormHeader() {
        return getText(formHeader);
    }

    @Step("Click on Save button...")
    public void clickOnSaveButton() {
        click(saveLibraryButton);
    }

    @Step("Click on Delete button...")
    public void clickOnDeleteButton() {
        click(deleteLibraryButton);
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        click(cancelEditingButton);
    }

    @Step("Verify Save button is disabled...")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(saveLibraryButtonDisabled);
    }

    @Step("Verify Delete button is disabled...")
    public boolean isDeleteButtonDisabled() {
        return isElementVisible(deleteLibraryButtonDisabled);
    }

    @Step("Verify Cancel button is disabled...")
    public boolean isCancelButtonDisabled() {
        return isElementVisible(cancelEditingButtonDisabled);
    }

    @Step("Enter library name...")
    public void enterLibraryName(String name) {
        inputText(nameField, name);
        // it looks like without this sleep we can't safe properly the library name
        DriverFactory.sleep(1000);
    }

    @Step("Get library name ...")
    public String getLibraryName() {
        return getAttribute(nameField);
    }

    @Step("Clear library name ...")
    public void clearLibraryName() {
        clearInputField(nameField);
    }

    @Step("Enter vendor name ...")
    public void enterVendorName(String name) {
        enterTextSlowly(vendorField, name);
    }

    @Step("Get library vendor ...")
    public String getVendorName() {
        return getAttribute(vendorField);
    }

    @Step("Clear vendor name...")
    public void clearVendorName() {
        clearInputField(vendorField);
    }

    @Step("Enter description...")
    public void enterDescription(String description) {
        enterTextSlowly(descriptionField, description);
    }

    @Step("Get library description ...")
    public String getDescriptionName() {
        return getAttribute(descriptionField);
    }

    @Step("Clear description...")
    public void clearDescription() {
        clearInputField(descriptionField);
    }

    @Step("Enter pre release hooks...")
    public void enterPreReleaseHooks(String hookName) {
        inputText(preReleaseHookField, hookName);
    }

    @Step("Get pre release hook...")
    public String getPreReleaseHook() {
        return getAttribute(preReleaseHookField);
    }

    @Step("Clear pre-release hooks...")
    public void clearPreReleaseHooks() {
        clearInputField(preReleaseHookField);
    }

    @Step("Enterpost release hooks...")
    public void enterPostReleaseHooks(String hookName) {
        inputText(postReleaseHookField, hookName);
    }

    @Step("Get post release hook..")
    public String getPostReleaseHook() {
        return getAttribute(postReleaseHookField);
    }

    @Step("Clearing post-release hooks...")
    public void clearPostReleaseHooks() {
        clearInputField(postReleaseHookField);
    }

    @Step("Enter post update hooks...")
    public void enterPostUpdateHooks(String hookName) {
        inputText(postUpdateHookField, hookName);
    }

    @Step("Get post update hook ...")
    public String getPostUpdateHook() {
        return getAttribute(postUpdateHookField);
    }

    @Step("Clear post-update hooks...")
    public void clearPostUpdateHooks() {
        clearInputField(postUpdateHookField);
    }

    @Step("Enter post load hooks...")
    public void enterPostLoadHooks(String hookName) {
        inputText(postLoadHookField, hookName);
    }

    @Step("Get post load hook ...")
    public String getPostLoadHooks() {
        return getAttribute(postLoadHookField);
    }

    @Step("Clear post load hooks...")
    public void clearPostLoadHooks() {
        clearInputField(postLoadHookField);
    }

    @Step("Creat a new library...")
    public void createNewLibrary(String name) {
        clickOnCreateLibraryButton();
        enterLibraryName(name);
        clickOnSaveButton();
    }

    @Step("Verify No IPs found ...")
    public boolean isNoIpsMessagePresent() {
        return isElementVisible(noIpsMessage);
    }

    @Step("Clicking on set vendor field")
    public void clickOnVendorField() {
        click(vendorField);
    }
}
