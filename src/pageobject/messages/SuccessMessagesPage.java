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

public class SuccessMessagesPage extends BasePage {
    private WebDriver driver;

    public SuccessMessagesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "[data-testid='ui-toast-item'].toast-success")
    private List<WebElement> successMessages;

    @FindBy(css = "[data-testid='ui-btn-toast-dismiss']")
    private WebElement closeButton;

    @FindBy(css = "[data-testid='toast-body']")
    private WebElement successMessageBody;

    @FindBy(css = "[data-testid='ui-toast-item'] i.fa-circle-check")
    private WebElement successMessageCheckMarkIcon;

    @FindBy(css = "[data-testid='ui-toast-item'] i.fa-circle-info")
    private WebElement successMessageCircleInfoMarkIcon;

    private String messageHeader = "//*[@data-testid='toast-title' and contains(.,'{title}')]";
    private String messageBody = "//*[@data-testid='toast-body' and contains(normalize-space(),'{message}')]";

    @Step("Get number of success messages...")
    public int getNumberOfSuccessMessages() {
        return successMessages.size();
    }

    @Step("Verify success message header is present...")
    public boolean isMessageHeaderPresent(String title) {
        waitForPageLoaded();
        final WebElement header = waitForElementToBePresentFluentWait(By.xpath(messageHeader.replace("{title}", title)));
        return isElementVisible(header);
    }

    @Step("Verify success message body is present...")
    public boolean isSuccessMessagePresent(String message) {
        final WebElement messageText = waitForElementToBePresentFluentWait(By.xpath(messageBody.replace("{message}", message)));
        return isElementVisible(messageText);
    }

    @Step("Verify success message header has check mark icon...")
    public boolean isSuccessMessageCheckMarkIconPresent() {
        return isElementVisible(successMessageCheckMarkIcon);
    }

    @Step("Verify success message header has info mark icon...")
    public boolean isSuccessMessageCircleInfoIconPresent() {
        return isElementVisible(successMessageCircleInfoMarkIcon);
    }

    @Step("get success message body is present...")
    public String getSuccessMessageBody() {
        return successMessageBody.getText();
    }

    @Step("Close success message...")
    public void closeSuccessMessage() {
        waitForPageLoaded();
        clickWithJS(closeButton);
        //We need to keep this 1 second wait, since looks like success message doesn't close right away, which makes tests to break
        DriverFactory.sleep(1000);
    }
}
