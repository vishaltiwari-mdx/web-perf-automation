/**
 * ################################################################################
 * # Copyright (c) 2010-2021 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject;

import com.methodics.phi.actions.WebElementActions;
import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.util.BrowserUtils;
import com.methodics.phi.util.SessionManager;
import com.paulhammant.ngwebdriver.NgWebDriver;
import io.qameta.allure.Step;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

public class BasePage implements WebElementActions {

    public static final Logger logger = LogManager.getLogger(BasePage.class);
    //ngWebDriver is a library of WebDriver locators for Java to work with AngularJS1 and AngularJS2 to find
    private static final NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) DriverFactory.getBrowserInstance());
    private static final String SPINNER_SELECTORS =
            ".spinner, .loading-spinner, .ngx-spinner, .spinner-border, .fa-spinner, " +
                    "[data-test='loading-spinner'], [class*='spinner'], [class*='loading']";

    public BasePage() {
        PageFactory.initElements(DriverFactory.getBrowserInstance(), this);
    }

    public static boolean waitForPageLoaded() {
        try {
            // 1. Wait for document.readyState to be 'complete'
            new FluentWait<>(DriverFactory.getBrowserInstance())
                    .withTimeout(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT))
                    .pollingEvery(Duration.ofMillis(DEFAULT_FLUENT_WAIT_POLLING_INTERVAL))
                    .ignoring(Exception.class)
                    .until(driver -> {
                        final JavascriptExecutor js = (JavascriptExecutor) driver;
                        final String readyState = (String) js.executeScript("return document.readyState");
                        return "complete".equals(readyState);
                    });

            // 2. Wait for any visible spinner elements to disappear
            // waitForSpinnersToDisappear(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT));

            // 3. Wait for jQuery to be idle (if present)
            new FluentWait<>(DriverFactory.getBrowserInstance())
                    .withTimeout(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT))
                    .pollingEvery(Duration.ofMillis(DEFAULT_FLUENT_WAIT_POLLING_INTERVAL))
                    .ignoring(Exception.class)
                    .until(driver -> {
                        final JavascriptExecutor js = (JavascriptExecutor) driver;
                        final Object defined = js.executeScript("return typeof jQuery !== 'undefined'");
                        if (Boolean.TRUE.equals(defined)) {
                            final Object active = js.executeScript("return jQuery.active");
                            return active instanceof Long && ((Long) active)==0L;
                        }
                        return true;
                    });

            // 4. Wait for Vue rendering (if applicable)
            try {
                waitForVueRendering();
            } catch (Exception e) {
                logger.warn("----------Found Vue rendering failed for page load validation ");
                //logger.warn("Vue check failed: {}", e.getMessage());  // uncomment for debugging
            }

            // Final check: ensure document.readyState is 'complete'
            final JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getBrowserInstance();
            final String readyState = (String) js.executeScript("return document.readyState");
            if (!"complete".equals(readyState)) {
                logger.error("Final readyState check failed: {}", readyState);
                return false;
            }
            return true;
        } catch (TimeoutException e) {
            logger.error("Page load timeout: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during page load wait: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Waits for all UI modules to be loaded and initialized.
     * Checks for module presence, initialization state, and readiness.
     */
    public static void waitForUiModule() {
        try {
            new FluentWait<>(DriverFactory.getBrowserInstance())
                    .withTimeout(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT))
                    .pollingEvery(Duration.ofMillis(DEFAULT_FLUENT_WAIT_POLLING_INTERVAL))
                    .ignoring(Exception.class)
                    .until(driver -> {
                        final JavascriptExecutor js = (JavascriptExecutor) driver;

                        // Check if UI module is defined
                        final Object uiModuleCheck = js.executeScript(
                                "return typeof uiModule !== 'undefined' && uiModule !== null");
                        if (!Boolean.TRUE.equals(uiModuleCheck)) {
                            return false;
                        }

                        // Check if UI module is initialized (has required properties/methods)
                        final Object moduleInitialized = js.executeScript(
                                "if (typeof uiModule === 'undefined' || uiModule === null) return false;" +
                                        "if (typeof uiModule.initialized !== 'undefined') return uiModule.initialized === true;" +
                                        "if (typeof uiModule.ready !== 'undefined') return uiModule.ready === true;" +
                                        "if (typeof uiModule.loaded !== 'undefined') return uiModule.loaded === true;" +
                                        "return true;");

                        if (!Boolean.TRUE.equals(moduleInitialized)) {
                            return false;
                        }

                        // Check for Angular/Vue app initialization if present
                        final Object appReady = js.executeScript(
                                "if (typeof angular !== 'undefined' && angular.element) {" +
                                        "  try {" +
                                        "    var injector = angular.element(document.body).injector();" +
                                        "    return injector !== null && injector !== undefined;" +
                                        "  } catch(e) { return true; }" +
                                        "}" +
                                        "if (typeof Vue !== 'undefined' && Vue.version) {" +
                                        "  return document.querySelector('[data-v-app]') !== null || " +
                                        "         document.querySelector('#app') !== null || " +
                                        "         document.querySelectorAll('[data-v-]').length > 0;" +
                                        "}" +
                                        "return true;");

                        return Boolean.TRUE.equals(appReady);
                    });

            logger.info("All UI modules loaded successfully");
        } catch (TimeoutException e) {
            logger.warn("UI module load timeout after 15 seconds: {}", e.getMessage());
        } catch (Exception e) {
            logger.warn("UI module check failed: {}", e.getMessage());
        }
    }

    public static void waitForSpinnersToDisappear(Duration timeout) {
        final AtomicBoolean spinnerWasVisible = new AtomicBoolean(false);
        try {
            new FluentWait<>(DriverFactory.getBrowserInstance())
                    .withTimeout(timeout)
                    .pollingEvery(Duration.ofMillis(DEFAULT_FLUENT_WAIT_POLLING_INTERVAL))
                    .ignoring(Exception.class)
                    .until(driver -> {
                        final JavascriptExecutor js = (JavascriptExecutor) driver;
                        final Long visible = (Long) js.executeScript(
                                "const sels = arguments[0].split(',').map(s=>s.trim());" +
                                        "const els = sels.flatMap(sel => Array.from(document.querySelectorAll(sel))); " +
                                        "return els.filter(e => {" +
                                        "  if (!e) return false;" +
                                        "  const style = window.getComputedStyle(e);" +
                                        "  const rect = e.getBoundingClientRect();" +
                                        "  const visible = style.visibility !== 'hidden' && style.display !== 'none' && rect.width>0 && rect.height>0;" +
                                        "  return visible;" +
                                        "}).length;", SPINNER_SELECTORS);

                        if (visible!=null && visible > 0) {
                            spinnerWasVisible.set(true);
                            return false; // still showing
                        }
                        // If spinner was seen at least once and now gone -> success
                        return spinnerWasVisible.get() || visible==0;
                    });
        } catch (TimeoutException e) {
            logger.error("Spinner check timeout: {}", e.getMessage());
        }
    }

    public static void waitForVueRendering() {
        try {
            new FluentWait<>(DriverFactory.getBrowserInstance())
                    .withTimeout(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT))
                    .pollingEvery(Duration.ofMillis(DEFAULT_FLUENT_WAIT_POLLING_INTERVAL))
                    .ignoring(Exception.class)
                    .until(driver -> {
                        final JavascriptExecutor js = (JavascriptExecutor) driver;
                        final Boolean vuePresent = (Boolean) js.executeScript("return typeof Vue !== 'undefined'");
                        if (Boolean.TRUE.equals(vuePresent)) {
                            return (Boolean) js.executeScript(
                                    "return !window.__VUE_DEVTOOLS_GLOBAL_HOOK__ || " +
                                            "!window.__VUE_DEVTOOLS_GLOBAL_HOOK__.Vue || " +
                                            "!window.__VUE_DEVTOOLS_GLOBAL_HOOK__.Vue.nextTick || " +
                                            "!window.__VUE_DEVTOOLS_GLOBAL_HOOK__.Vue.nextTick.pending");
                        }
                        return true;
                    });
        } catch (Exception e) {
            System.out.println("Vue rendering check failed: " + e.getMessage());
        }
    }

    /**
     * Stores the login session for a user.
     *
     * @param userName The username for authentication
     * @return JSON string containing access token and user ID
     * @throws IOException If there's an error managing the session
     */
    @Step("get the login response for the user name: {0}, for the method: {method}...")
    public static String storeLoginSession(String userName) throws IOException {
        // Delegate to SessionManager
        return SessionManager.storeLoginSession(userName);
    }

    /**
     * Main method to set authentication cookies with retry logic.
     *
     * @param driver The WebDriver instance
     * @param userName The username for which to set cookies
     * @throws IOException If there is an issue setting the cookies
     */
    @Step("Set authentication cookies")
    public static void setCookiesToUi(WebDriver driver, String userName) throws IOException {
        final int maxRetries = 3;
        int retryCount = 0;
        Exception lastException = null;

        while (retryCount < maxRetries) {
            try {
                // Step 1: Ensure browser is initialized and responsive
                WebDriver activeDriver = BrowserUtils.ensureBrowserInitialized();
                activeDriver = BrowserUtils.ensureDriverResponsive(activeDriver);

                // Step 2: Get user session details
                final JSONObject userDetails = BrowserUtils.getUserSessionDetails(userName);
                final String accessToken = userDetails.getString(SessionManager.ACCESS_TOKEN);

                // Step 3: Set up cookie parameters
                final String baseUrl = DriverFactory.getBaseUrl();
                final String domain = new URL(baseUrl).getHost();
                final Cookie authCookie = BrowserUtils.createAuthCookie(accessToken, domain);

                // Step 4: Apply cookie and verify login
                BrowserUtils.applyAuthCookieAndVerifyLogin(activeDriver, authCookie, baseUrl);

                logger.info("Successfully set auth cookie for user {} on domain: {}", userName, domain);
                return; // Success, exit the method

            } catch (Exception e) {
                lastException = e;
                retryCount++;
                logger.warn("Attempt {} failed to set auth cookie for user: {}. Error: {}",
                        retryCount, userName, e.getMessage());

                try {
                    // Wait before retrying
                    Thread.sleep(2000L * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        // If we get here, all retries failed
        final String errorMsg = "Failed to set auth cookie for user: " + userName +
                " after " + maxRetries + " attempts. Last error: " +
                (lastException!=null ? lastException.getMessage():"Unknown error");

        logger.error(errorMsg, lastException);
        throw new IOException(errorMsg, lastException);
    }
}