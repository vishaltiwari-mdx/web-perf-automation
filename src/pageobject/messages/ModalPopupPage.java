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
package com.methodics.phi.pageobject.messages;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ModalPopupPage extends BasePage {
    private WebDriver driver;

    public ModalPopupPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = ".modal-header .modal-title")
    private WebElement modalHeader;

    @FindBy(css = "div.modal-body.message-box__body div[data-testid='message-box-content']")
    private WebElement modalBody;

    @FindBy(css = "button[data-testid*='ui-btn-ui-modal-delete']")
    private WebElement confirmButton;

    @FindBy(css = "button[data-testid = 'ui-btn-ui-modal-delete-ip']")
    private WebElement confirmButtonForDeleteIp;

    @FindBy(css = "button[data-testid = 'ui-btn-ui-modal-delete-query-folder']")
    private WebElement confirmButtonForQueryFolder;

    @FindBy(css = "button[data-testid = 'ui-btn-ui-modal-delete-label']")
    private WebElement confirmButtonForLabel;

    @FindBy(css = "button[data-testid = 'ui-btn-ui-modal-delete-library']")
    private WebElement confirmButtonForLibrary;

    @FindBy(css = "button[data-testid = 'ui-btn-ui-modal-delete-query']")
    private WebElement confirmButtonForQueryModalFolderInAdvanceSearch;

    @FindBy(css = "footer.message-box__footer .btn-outline-secondary[data-testid='ui-btn-ui-modal-cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "(//div[contains(@class, 'modal-header')]//button[@data-testid='ui-btn-modal-header-close'])[last()]")
    private WebElement closeModalButton;

    @FindBy(xpath = "//*[contains(@class, 'modal-header')]//following::a[@href='https://help.perforce.com/helix-iplm/public-latest/latest/Default.htm?cshid=Planning_BOM']//i")
    private WebElement planningBomCartHelpButton;

    @FindBy(css = "button[data-testid='ui-btn-create-version-modal-create']")
    private WebElement modalConfirmCreateVersion;

    @FindBy(css = "button[data-testid='ui-btn-make-latest-version-modal-create-version-copy']")
    private WebElement createVersionCopyButton;

    @FindBy(css = "div[class*='modal-header'] h5")
    private WebElement planningBomModalHeader;

    @FindBy(css = "button[data-testid='ui-btn-session-timeout-modal-session-expire']")
    private WebElement logOutButton;

    @FindBy(css = "button[data-testid='ui-btn-session-timeout-modal-session-extend']")
    private WebElement extendButton;

    @FindBy(xpath = "//div[@class='modal-body']/p")
    private WebElement expirationModal;

    @FindBy(css = "button[data-testid='ui-btn-propertyset-modal-cancel']")
    private WebElement propertySetCloseModalButton;

    @FindBy(css = "button[data-testid='ui-btn-ui-modal-discard-changes']")
    private WebElement discardChangesButton;

    @FindBy(css = "button[data-testid='ui-btn-ui-modal-remove']")
    private WebElement removeButton;

    private String modal = "//div[@class='modal-content']";
    private final String getPropValue = "//div[contains(@class, 'modal-body')]//following::div[@col-id='{propName}']//span[@title='{propValue}']";
    private final String confirmButtonWithText="//button[contains(@data-testid,'ui-btn-') and contains(normalize-space(.),'{name}')]";
    private String propertyRowsModal = "//div[contains(@class, 'modal-body')]//div[@role='rowgroup' and @class='ag-pinned-left-cols-container']//*[contains(@class, 'ag-row-no-focus')]";

    @FindBy(xpath = "//div[contains(@class,'modal-body')]//following::div[contains(@class,'invalid-feedback')]")
    private WebElement minLengthMessage;

    @Step("Get modal header...")
    public String getModalHeader() {
        return getText(modalHeader);
    }

    @Step("Get modal message...")
    public String getModalMessage() {
        return getText(modalBody);
    }

    @Step("Click on confirm action button...")
    public void clickOnConfirmButton() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButton);
        clickWithJS(confirmButton);
    }

    @Step("Click on confirm action button...")
    public void clickOnConfirmIpDeleteButton() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButtonForDeleteIp);
        clickWithJS(confirmButtonForDeleteIp);
    }

    @Step("Click on confirm action button...")
    public void clickOnConfirmQueryFolderButton() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButtonForQueryFolder);
        clickWithJS(confirmButtonForQueryFolder);
    }

    @Step("Click on Library confirm action button...")
    public void clickOnConfirmButtonForLibrary() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButtonForLibrary);
        clickWithJS(confirmButtonForLibrary);
    }

    @Step("Click on discard changes button...")
    public void clickOnDiscardChangesButton() {
        waitForPageLoaded();
        click(discardChangesButton);
    }

    @Step("Click on confirm action button...")
    public void clickOnConfirmQueryModalFolderInAdvanceSearch() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButtonForQueryModalFolderInAdvanceSearch);
        clickWithJS(confirmButtonForQueryModalFolderInAdvanceSearch);
    }

    @Step("Click on planning bom cart help button...")
    public void clickOnPlanningBomCartHelpButton() {
        click(planningBomCartHelpButton);
    }

    @Step("Hover over planning bom cart help button...")
    public void hoverOverPlanningBomCartHelpButton() {
        hoverOverElement(planningBomCartHelpButton);
    }

    @Step("Clicking on Modal Create Version button...")
    public void clickOnModalCreateVersion() {
        click(modalConfirmCreateVersion);
    }

    @Step("Clicking on Modal Create Version Copy button...")
    public void clickOnCreateVersionCopyButton() {
        click(createVersionCopyButton);
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        DriverFactory.sleep(500);
        click(cancelButton);
    }

    @Step("Get number of modals...")
    public boolean isModalClosed() {
        waitForNumberOfElementsToBe(By.xpath(modal), 0);
        return true;
    }

    @Step("Click on close modal button...")
    public void closeModal() {
        waitForPageLoaded();
        hoverOverElement(closeModalButton);
        waitForElementToBeClickable(closeModalButton).click();
    }

    @Step("Getting planning bom modal header...")
    public String getPlanningBomModalHeader() {
        return getText(planningBomModalHeader);
    }

    @Step("Verify that extend button is present...")
    public boolean isExtendButtonPresent() {
        return extendButton.isDisplayed();
    }

    @Step("Verify that log out button is present...")
    public boolean isLogOutButtonPresent() {
        return logOutButton.isDisplayed();
    }

    @Step("Getting expiration dialog text...")
    public String getExpirationModalBodyText() {
        return getText(expirationModal);
    }

    @Step("Clicking on log out button...")
    public void clickOnLogOutButton() {
        click(logOutButton);
    }

    @Step("Clicking on extend button...")
    public void clickOnExtendButton() {
        click(extendButton);
    }

    @Step("Get minimum query length info message...")
    public String getResourceSearchLengthInfoMsg() {
        return getText(minLengthMessage);
    }

    @Step("Verify property value...")
    public boolean isPropertyValueVisible(String propName, String propValue) {
        final WebElement xpathPropValue = findElementWithWait(By.xpath(getPropValue.replace("{propName}", propName).replace("{propValue}", propValue)));
        return isElementVisible(xpathPropValue);
    }

    @Step("Click on confirm button with text: {name} ...")
    public void clickOnConfirmButtonWithText(String name) {
        String xpath = confirmButtonWithText.replace("{name}", name);
        WebElement button = findElementWithWait(By.xpath(xpath));
        waitForElementToBeClickable(button).click();
    }

    @Step("Click on close modal Of Property Setbutton...")
    public void propertySetCloseModal() {
        waitForPageLoaded();
        hoverOverElement(propertySetCloseModalButton);
        waitForElementToBeClickable(propertySetCloseModalButton).click();
    }

    @Step("Click on Remove button...")
    public void clickOnRemoveButton() {
        waitForPageLoaded();
        waitForElementToBeClickable(removeButton).click();
    }

    @Step("Click on confirm button for label...")
    public void clickOnConfirmButtonForLabel() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(confirmButtonForLabel);
        clickWithJS(confirmButtonForLabel);
    }

    @Step("Get number of properties Add Properties modal...")
    public int getNumberOfPropertiesModal(int counter) {
        waitForPageLoaded();
        waitForUiModule();
        return waitForNumberOfElementsToBe(By.xpath(propertyRowsModal), counter).size();
    }

    public boolean isRemoveModalDisplayed() {
        try {
            waitForPageLoaded();
            waitForElementToBeClickable(removeButton);
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCancelButtonDisplayed() {
        try {
            waitForPageLoaded();
            waitForElementToBeClickable(cancelButton);
            return cancelButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRemoveButtonDisplayed() {
        try {
            waitForPageLoaded();
            waitForElementToBeClickable(removeButton);
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
