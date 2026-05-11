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
package com.methodics.phi.pageobject.login;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.dashboard.DashboardPage;
import com.methodics.phi.pageobject.messages.ErrorMessagesPage;
import com.methodics.phi.util.CommonUrls;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignInPage extends BasePage {
    private WebDriver driver;

    public SignInPage(WebDriver driver) throws InterruptedException {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[contains(@class, 'signin-page__card__username-input')]")
    private WebElement userNameField;

    @FindBy(xpath = "//input[contains(@class, 'signin-page__card__password-input')]")
    private WebElement userPasswordField;

    @FindBy(xpath = "//button[contains(@class, 'signin-page__card__submit-button')]")
    private WebElement loginButton;

    @FindBy(xpath = "//img[contains(@src,'/media/img/logo') and contains(@alt,'Helix IPLM')]")
    private WebElement productLogo;

	@FindBy(xpath = "//div[normalize-space()='2026 Perforce Software, Inc. All rights reserved.']")
	private WebElement copyright;

    @FindBy(xpath = "//i[contains(@class,'copyright')]")
    private WebElement copyrightIcon;

    @FindBy(xpath = "//a[contains(@class,'doc-link')][normalize-space()='Product documentation']")
    private WebElement docsLink;

    @FindBy(xpath = "//div[@class='signin-page__card__version-info mb-2']")
    private WebElement versionInfo;

    @FindBy(xpath = "//input[contains(@class, 'username-input')]/ancestor::div[@class='field-validator']/div[contains(@class,'invalid-feedback_error')]")
    private WebElement usernameValidationError;

    @FindBy(xpath = "//input[contains(@class, 'password-input')]//ancestor::div[@class='field-validator']/div[contains(@class,'invalid-feedback_error')]")
    private WebElement passwordValidationError;

    private String capsLockIcon = "//i[@title='Caps Lock is on']";

    @Step("Enter user name: {0}, for the method: {method}...")
    public void enterUserName(String userName) {
        inputText(userNameField, userName);
    }

    @Step("Enter password: {0}, for the method: {method}...")
    public void enterUserPassword(String password) {
        inputText(userPasswordField, password);
    }

    @Step("Enter password: {0}, for the method: {method}...")
    public void enterUserPasswordSlowly(String password) {
        enterTextSlowly(userPasswordField, password);
    }

    @Step("Click on Login button...")
    public void clickLoginButton() {
        waitForElementToBeVisible(loginButton);
        click(loginButton);
    }

    @Step("Login step with user name: {0}, password: {1}, for the method: {method}...")
    public DashboardPage login(String userName, String password) {
        int maxRetries = 3;
        int retryCount = 0;
        boolean pageLoaded = false;

        while (retryCount < maxRetries && !pageLoaded) {
            goTo(DriverFactory.getFullUrl(CommonUrls.SIGN_IN_PAGE));
            enterUserName(userName);
            enterUserPassword(password);
            clickLoginButton();

            pageLoaded = waitForPageLoaded();

            if (!pageLoaded) {
                retryCount++;
                logger.warn("Page did not load correctly. Retrying... (" + retryCount + "/" + maxRetries + ")");
            }
        }
        if (!pageLoaded) {
            throw new RuntimeException("Login failed after " + maxRetries + " attempts due to page load timeout.");
        }
        return new DashboardPage(driver);
    }

    @Step("Log in a new tab...")
    public void loginNewTab(String userName, String password) {
        enterUserName(userName);
        enterUserPassword(password);
        clickLoginButton();
    }

    @Step("Login step with user name: {0}, for the method: {method}...")
    public DashboardPage login(String userName) {
        return login(userName, userName);
    }

    @Step("Login step with user name: {0}, password: {1}, for the method: {method}...")
    public void logIn(String userName) {
        enterUserName(userName);
        enterUserPassword(userName);
        clickLoginButton();
        getCurrentUrl().equals(DriverFactory.getFullUrl(CommonUrls.HOME_URL));
    }

    @Step("Invalid login step with user name: {0}, password: {1}, for the method: {method}...")
    public ErrorMessagesPage incorrectLogin(String userName, String password) {
        enterUserName(userName);
        enterUserPassword(password);
        clickLoginButton();
        return new ErrorMessagesPage(driver);
    }

    @Step("Go to Sign in page...")
    public void goToSignInPage(String url) {
        goTo(url);
    }

    @Step("Verify Log In button is present...")
    public boolean isLogInButtonPresent() {
        return isElementVisible(loginButton);
    }

    @Step("Verify Username input is present...")
    public boolean isUsernameInputPresent() {
        return isElementVisible(waitForElementToBeVisible(userNameField));
    }

    @Step("Verify product logo is displayed...")
    public boolean isProductLogoPresent() {
        return isElementVisible(productLogo);
    }

    @Step("Verify copyright is displayed...")
    public boolean isProductCopyrightPresent() {
        return isElementVisible(copyright);
    }

    @Step("Verify copyright icon is displayed...")
    public boolean isCopyrightIconDisplayed() {
        try {
            waitForElementToBeVisible(copyrightIcon);
            return copyrightIcon.isDisplayed();
        } catch (Exception e) {
            logger.warn("Copyright icon not found or not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get validation message for username...")
    public String getUsernameValidationMessageText() {
        return getText(usernameValidationError);
    }

    @Step("Get validation message for password...")
    public String getPasswordValidationMessageText() {
        return getText(passwordValidationError);
    }

    @Step("Verify that username field is disabled...")
    public boolean isUserNameFieldEnabled() {
        return isElementEnabled(userNameField);
    }

    @Step("Verify that password field is disabled...")
    public boolean isPasswordFieldEnabled() {
        return isElementEnabled(userPasswordField);
    }

    @Step("Verify that Log In button is disabled...")
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButton);
    }

    @Step("Click on docs link...")
    public void clickOnDocsLink() {
        waitTillClickableWithFluentWait(docsLink);
        click(docsLink);
    }

    @Step("Get versions info...")
    public String getVersionsInfo() {
        return getText(versionInfo);
    }

    @Step("Verify Caps Lock icon is not present...")
    public boolean capsLockIconNotPresent() {
        return driver.findElements(By.xpath(capsLockIcon)).size() == 0;
    }

    @Step("Hover over Caps Lock icon...")
    public void hoverOverCapsLockIcon() {
        hoverOverElement(driver.findElement(By.xpath(capsLockIcon)));
    }
}
