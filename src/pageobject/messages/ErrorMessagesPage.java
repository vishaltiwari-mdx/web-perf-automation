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
package com.methodics.phi.pageobject.messages;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ErrorMessagesPage extends BasePage {
    private WebDriver driver;

    public ErrorMessagesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "[data-testid='ui-toast-item'].toast-danger")
    private List<WebElement> toastMessages;

    @FindBy(css = "[data-testid='ui-btn-toast-dismiss']")
    private WebElement toastDismissButton;

    @FindBy(css = "[data-testid='toast-title']")
    private WebElement toastHeader;

    @FindBy(css = "[data-testid='toast-body']")
    private WebElement toastBody;

    @FindBy(css = "[data-testid='toast-title']")
    private WebElement toastHeaderText;

    @FindBy(css = "[data-testid='ui-toast-item'] i.fa-triangle-exclamation")
    private WebElement toastWarningExclamationIcon;

    @FindBy(css = "[data-testid='ui-toast-item'] i.fa-circle-xmark")
    private WebElement toastXmarkIcon;

    @FindBy(xpath = "//div[contains(@class,'invalid-feedback_error')]")
    private WebElement inlineErrorMessage;

    private String toastBodyMessageXpath = "//div[contains(@class,'toast-body') and normalize-space()='{message}']";
    private String nameFieldError = "//div[contains(@class,'invalid-feedback') and normalize-space()='{message}']";

    @Step("Verify number of error messages...")
    public int getNumberOfErrorMessages() {
        return toastMessages.size();
    }

    @Step("Close error toaster message...")
    public void closeErrorMessage() {
        click(toastDismissButton);
        //We need to keep this 1 second wait, since looks like success message doesn't close right away, which makes tests to break
        DriverFactory.sleep(1000);
    }

    @Step("Verify error header is present...")
    public boolean isErrorHeaderPresent() {
        return isElementVisible(toastHeader);
    }

    @Step("Verify error message body is correct...")
    public boolean isErrorMessagePresent(String error) {
        WebElement message = waitForElementToBeVisible(driver.findElement(By.xpath(toastBodyMessageXpath.replace("{message}", error))));
        return isElementVisible(message);
    }

    @Step("Verify warning header message has warning exclamation icon")
    public boolean isWarningHeaderExclamationIconPresent() {
        return isElementVisible(toastWarningExclamationIcon);
    }

    @Step("Verify error message header has circle xmark icon...")
    public boolean isErrorHeaderXmarkIconPresent() {
        return isElementVisible(toastXmarkIcon);
    }

    @Step("Verify error message for name input field...")
    public boolean isErrorMessageForNameFieldDisplayed(String messageBody) {
        WebElement message = waitForElementToBeVisible(driver.findElement(By.xpath(nameFieldError.replace("{message}", messageBody))));
        return isElementVisible(message);
    }

    @Step("Get error message text...")
    public String getErrorMessage() {
        return getText(toastBody);
    }

    @Step("Get inline error message text...")
    public String getInlineErrorMessage() {
        return getText(inlineErrorMessage).trim();
    }

    @Step("Verify login error header is correct...")
    public String getErrorHeaderText() {
        return getText(toastHeaderText);
    }

    @Step("Get number of field validation errors...")
    public int getNumberOfFormValidationErrorMessages(String messageBody) {
        List<WebElement> messages = driver.findElements(By.xpath(nameFieldError.replace("{message}", messageBody)));
        return messages.size();
    }
}
