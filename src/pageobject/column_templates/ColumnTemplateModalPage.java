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
package com.methodics.phi.pageobject.column_templates;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ColumnTemplateModalPage extends BasePage {
    private WebDriver driver;

    public ColumnTemplateModalPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[data-testid='ui-in-edit-modal-template-name']")
    private WebElement templateNameInput;

    @FindBy(css = "input[data-testid='edit-modal-public-input']")
    private WebElement publicCheckbox;

    @FindBy(css = "input[data-testid='edit-modal-default-input']")
    private WebElement defaultCheckbox;

    @FindBy(css = "input[data-testid='edit-modal-default-input'][disabled]")
    private WebElement defaultCheckboxDisabled;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-submit']")
    private WebElement createButton;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-submit']:disabled")
    private WebElement disabledCreateButton;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-cancel'], button[data-testid='ui-btn-unpublish-modal-cancel']")
    private WebElement cancelButton;

    @FindBy(css = "button[data-testid='ui-btn-update-modal-tabular-templates-form-update']")
    private WebElement updateButton;

    @FindBy(css = "button[data-testid='ui-btn-update-modal-tabular-templates-form-update']:disabled")
    private WebElement disabledUpdateButton;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-submit']")
    private WebElement saveButton;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-submit']:disabled")
    private WebElement disabledSaveButton;

    @FindBy(css = "button[data-testid='ui-btn-edit-modal-tabular-templates-form-delete'], button[data-testid='ui-btn-delete-modal-tabular-templates-delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[normalize-space()='Unpublish template']")
    private WebElement unpublishButton;

    @FindBy(xpath = "(//h5[contains(@class,'app-modal-header__title') or contains(@class,'modal-title')])[last()]")
    private WebElement modalTitle;

    @FindBy(css = "[data-testid*='tabular-templates'] .modal-body")
    private WebElement modalBody;

    @FindBy(css = "button[data-testid='ui-btn-revert-button']:not([disabled])")
    private WebElement revertButton;

    @FindBy(css = "button[data-testid='ui-btn-revert-button'][disabled]")
    private WebElement revertButtonDisabled;

    @FindBy(css = "[data-testid='ui-btn-modal-header-close']")
    private WebElement closeButtonTemplateModalDialog;

    private String modalDialog = "//div[contains(@class,'tabular-templates__edit-modal') and contains(@class,'modal-dialog')]";

    @Step("Type template name...")
    public void enterTemplateName(String name) {
        DriverFactory.sleep(500);
        templateNameInput.clear();
        enterTextSlowly(templateNameInput, name);
    }

    @Step("Get template name...")
    public String getNameFieldValue() {
        return getAttribute(templateNameInput);
    }

    @Step("Clear name input field...")
    public void clearNameField() {
        clearInputField(templateNameInput);
    }

    @Step("Check public checkbox...")
    public void checkPublicCheckbox() {
        DriverFactory.sleep(50); // slight delay required to avoid click issues
        waitTillClickableWithFluentWait(publicCheckbox).click();
    }

    @Step("Check default checkbox...")
    public void checkDefaultCheckbox() {
        click(defaultCheckbox);
    }

    @Step("Verify default checkbox is disabled...")
    public boolean isDefaultCheckboxDisabled() {
        return isElementVisible(defaultCheckboxDisabled);
    }

    @Step("Click on Create button...")
    public void clickOnCreateButton() {
        click(waitTillClickableWithFluentWait(createButton));
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        click(waitForElementToBeClickable(cancelButton));
    }

    @Step("Click on Update button...")
    public void clickOnUpdateButton() {
        click(waitForElementToBeClickable(updateButton));
    }

    @Step("Click on Save button...")
    public void clickOnSaveButton() {
        waitForElementToBeClickable(saveButton).click();
        DriverFactory.sleep(1000);
    }

    @Step("Click on Delete button...")
    public void clickOnDeleteButton() {
        click(waitForElementToBeClickable(deleteButton));
        DriverFactory.sleep(1000);
    }

    @Step("Click on Unpublish button...")
    public void clickOnUnpublishButton() {
        click(waitForElementToBeClickable(unpublishButton));
        DriverFactory.sleep(1000);
    }

    @Step("Get modal title...")
    public String getModalTitle() {
        DriverFactory.sleep(500);
        return getText(modalTitle);
    }

    public static String normalizeMessage(String message) {
        if (message==null) return null;
        // Trim each line, then replace multiple spaces with a single space
        String trimmedLines = java.util.Arrays.stream(message.split("\\R"))
                .map(String::trim)
                .collect(java.util.stream.Collectors.joining(" "));
        return trimmedLines.replaceAll("\\s{2,}", " ").trim();
    }

    @Step("Get modal title...")
    public String getModalTextMessage() {
        waitTillVisibleWithFluentWait(modalBody);
        return normalizeMessage(modalBody.getText());
    }

    @Step("Check if Create button is disabled...")
    public boolean isCreateButtonDisabled() {
        return isElementVisible(disabledCreateButton);
    }

    @Step("Check if Save button is disabled...")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(disabledSaveButton);
    }

    @Step("Check if Template Modal dialog is closed...")
    public boolean isTemplateModalClosed() {
        waitForNumberOfElementsToBe(By.xpath(modalDialog), 0);
        return true;
    }

    @Step(" close Template modal dialog")
    public void closeTemplateModalDialog() {
        clickWithJS(closeButtonTemplateModalDialog);
    }

    @Step("Hover over Revert changes button...")
    public void hoverOverRevertButton() {
        DriverFactory.sleep(500);
        hoverOverElement(revertButton);
    }

    @Step("Check if Revert button is disabled...")
    public boolean isRevertButtonDisabled() {
        return isElementVisible(revertButtonDisabled);
    }

    @Step("Click on Revert changes button...")
    public void clickOnRevertChangesButton() {
        click(waitForElementToBeClickable(revertButton));
    }

    @Step("Check if Revert button is active...")
    public boolean isRevertButtonActive() {
        return isElementVisible(revertButton);
    }


}
