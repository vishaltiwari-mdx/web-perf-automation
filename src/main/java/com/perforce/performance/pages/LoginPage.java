package com.perforce.performance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage - Page Object for the Login screen.
 *
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  CONFIGURE: Update the By locators below to match your app  ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Common locator strategies:
 *   By.id("username")
 *   By.name("email")
 *   By.cssSelector("input[type='email']")
 *   By.xpath("//input[@placeholder='Enter email']")
 */
public class LoginPage extends BasePage {

    // ─── TODO: Update these locators to match your application ───────────────
    private static final By USERNAME_FIELD  = By.cssSelector("input[type='text'], input[type='email'], input[name='username'], #username, #email");
    private static final By PASSWORD_FIELD  = By.cssSelector("input[type='password'], input[name='password'], #password");
    private static final By LOGIN_BUTTON    = By.cssSelector("button[type='submit'], input[type='submit'], .login-btn, #login-button, button.btn-primary");
    private static final By ERROR_MESSAGE   = By.cssSelector(".error, .alert-danger, .login-error, [class*='error']");
    // ─────────────────────────────────────────────────────────────────────────

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isLoaded() {
        return isElementPresent(USERNAME_FIELD) && isElementPresent(PASSWORD_FIELD);
    }

    public void enterUsername(String username) {
        log.info("Entering username");
        type(USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        log.info("Entering password");
        type(PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        log.info("Clicking login button");
        click(LOGIN_BUTTON);
    }

    /**
     * Full login action: enters credentials and clicks login.
     * Returns after login button click (does not wait for redirect).
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean hasError() {
        return isElementPresent(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        if (hasError()) return getText(ERROR_MESSAGE);
        return "";
    }
}
