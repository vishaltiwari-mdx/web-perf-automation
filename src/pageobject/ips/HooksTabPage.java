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
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HooksTabPage extends BasePage {
    private final WebDriver driver;
    @FindBy(xpath = "//h6[normalize-space()='Pre-release']/following-sibling::div/span[normalize-space()='IP']/following-sibling::span")
    private WebElement preReleaseHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-release']/following-sibling::div/span[normalize-space()='IP']/following-sibling::span")
    private WebElement postReleaseHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-update']/following-sibling::div/span[normalize-space()='IP']/following-sibling::span")
    private WebElement postUpdateHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-load']/following-sibling::div/span[normalize-space()='IP']/following-sibling::span")
    private WebElement postLoadHook;

    @FindBy(xpath = "//h6[normalize-space()='Pre-release']/following-sibling::div/span[normalize-space()='Library']/following-sibling::span")
    private WebElement libraryPreReleaseHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-release']/following-sibling::div/span[normalize-space()='Library']/following-sibling::span")
    private WebElement libraryPostReleaseHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-update']/following-sibling::div/span[normalize-space()='Library']/following-sibling::span")
    private WebElement libraryPostUpdateHook;

    @FindBy(xpath = "//h6[normalize-space()='Post-load']/following-sibling::div/span[normalize-space()='Library']/following-sibling::span")
    private WebElement libraryPostLoadHook;

    @FindBy(css = "span[data-testid=ui-tooltip-text-content]")
    private WebElement tooltip;

    // copy button locator
    private final String ipHookCopyButton = "//h6[text()='%s']/following-sibling::div[2]//button[contains(@class, 'copy-to-clipboard-button')]//i";
    private final String ipHookHover = "//h6[normalize-space()='%s']/following-sibling::div/span[normalize-space()='IP']/following-sibling::span";
    private final String libraryHookCopyButton = "//h6[text()='%s']/following-sibling::div[1]//button[contains(@class, 'copy-to-clipboard-button')]";
    private final String libraryHookHover = "//h6[normalize-space()='%s']/following-sibling::div/span[normalize-space()='Library']/following-sibling::span";

    public HooksTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Verify pre release hook is displayed...")
    public String getPreReleaseHook() {
        return getText(waitForElementToBeVisible(preReleaseHook));
    }

    @Step("Verify post release hook is displayed...")
    public String getPostReleaseHook() {
        return getText(waitForElementToBeVisible(postReleaseHook));
    }

    @Step("Verify post update hook is displayed...")
    public String getPostUpdateHook() {
        return getText(waitForElementToBeVisible(postUpdateHook));
    }

    @Step("Verify post load hook is displayed...")
    public String getPostLoadHook() {
        return getText(waitForElementToBeVisible(postLoadHook));
    }


    @Step("Verify Library pre release hook is displayed...")
    public String getLibraryPreReleaseHook() {
        return getText(waitForElementToBeVisible(libraryPreReleaseHook));
    }

    @Step("Verify Library post release hook is displayed...")
    public String getLibraryPostReleaseHook() {
        return getText(waitForElementToBeVisible(libraryPostReleaseHook));
    }

    @Step("Verify Library post update hook is displayed...")
    public String getLibraryPostUpdateHook() {
        return getText(waitForElementToBeVisible(libraryPostUpdateHook));
    }

    @Step("Verify Library post load hook is displayed...")
    public String getLibraryPostLoadHook() {
        return getText(waitForElementToBeVisible(libraryPostLoadHook));
    }

    @Step("Verify Library post load hook is displayed...")
    public boolean isLibraryCopyButtonVisible(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(libraryHookHover, cardName))));
        try {
            return isElementVisible(findElementWithWait(By.xpath(String.format(libraryHookCopyButton, cardName))));
        } catch (TimeoutException t) {
            logger.warn("Copy button is not visible for library ");
            return false;
        }
    }

    @Step("Verify IP post load hook is displayed...")
    public boolean isIpCopyButtonVisible(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(ipHookHover, cardName))));
        try {
            return isElementVisible(findElementWithWait(By.xpath(String.format(ipHookCopyButton, cardName))));
        } catch (TimeoutException t) {
            logger.info("locator copy button not display: "+String.format(ipHookCopyButton, cardName));
            return false;
        }
    }

    @Step("Verify IP post load hook copy button tooltip is displayed...")
    public String getIpCopyButtonTooltip(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(ipHookHover, cardName))));
        DriverFactory.sleep(500);
        try {
            hoverOverElement(findElementWithWait(By.xpath(String.format(ipHookCopyButton, cardName))));
            return getObjectToolTip();
        } catch (TimeoutException t) {
            logger.warn("IP Hook Copy button/tooltip is not visible");
            return null;
        }
    }

    @Step("Verify Library post load hook copy button tooltip is displayed...")
    public String getLibraryCopyButtonTooltip(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(libraryHookHover, cardName))));
        DriverFactory.sleep(500);
        try {
            hoverOverElement(findElementWithWait(By.xpath(String.format(libraryHookCopyButton, cardName))));
            return getObjectToolTip();
        } catch (TimeoutException t) {
            logger.warn("Library Hook Copy button/tooltip is not visible");
            return null;
        }
    }

    @Step("Get tooltip text...")
    public String getObjectToolTip() {
        waitTillVisibleWithFluentWait(tooltip);
        return tooltip.getText().trim();
    }

    @Step("Verify Library post load hook is displayed...")
    public void clickCopyButtonIpFields(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(ipHookHover, cardName))));
        WebElement copyButton = findElementWithWait(By.xpath(String.format(ipHookCopyButton, cardName)));
        copyButton.click();
    }

    @Step("Verify Library post load hook is displayed...")
    public void clickCopyButtonLibraryFields(String cardName) {
        hoverOverElement(findElementWithWait(By.xpath(String.format(libraryHookHover, cardName))));
        WebElement copyButton = findElementWithWait(By.xpath(String.format(libraryHookCopyButton, cardName)));
        copyButton.click();
    }
}
