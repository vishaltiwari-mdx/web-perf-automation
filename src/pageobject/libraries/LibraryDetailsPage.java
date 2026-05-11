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
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import static com.methodics.phi.util.CommonUrls.LIBRARY_DETAILS_PAGE;
import io.qameta.allure.Step;
import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibraryDetailsPage extends BasePage {
    private WebDriver driver;

    public LibraryDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//img[@class='library-details__info-panel__icon'][@alt='Lib']")
    private WebElement libraryIcon;

    @FindBy(xpath = "//div[@data-ips-count]")
    private WebElement numberOfIpsOnIpCard;

    @FindBy(xpath = "//p[contains(@class,'library-details__info-panel__name')][@data-name]")
    private WebElement libraryName;

    @FindBy(xpath = "//label[contains(@class,'library-details__info-panel__vendor')][@data-vendor-name]/following-sibling::span[contains(@class,'library-details__info-panel__vendor_value')]")
    private WebElement vendor;

    @FindBy(xpath = "//label[contains(@class,'library-details__info-panel__description')][@data-description]/following-sibling::span[contains(@class,'library-details__info-panel__description_value')]")
    private WebElement descriptionText;

    @FindBy(css = "button.btn-line-action-default-small[data-testid='ui-btn-copy-to-clipboard']")
    private WebElement copyToClipboardButton;

    @FindBy(css = "div[data-testid='ui-tooltip-trigger']")
    private WebElement copyLibraryNameTooltip;

    private String librarySubHeader = "//p[contains(@class,'library-details__info-panel__name')][@data-name='{name}' and @title='{name}']";

    @Step("Get library name...")
    public String getLibraryName() {
        waitTillVisibleWithFluentWait(libraryName);
        return getText(libraryName);
    }

    @Step("Get library vendor...")
    public String getVendorName() {
        waitTillVisibleWithFluentWait(vendor);
        return getText(vendor);
    }

    @Step("Get library description...")
    public String getLibDescription() {
        waitTillVisibleWithFluentWait(descriptionText);
        return getText(descriptionText);
    }

    @Step("Get library name tooltip...")
    public String getLibraryNameTooltip() {
        return getAttributeValue(libraryName, "title");
    }

    @Step("Get vendor tooltip...")
    public String getVendorTooltip() {
        return getAttributeValue(vendor, "title");
    }

    @Step("Get description tooltip...")
    public String getDescriptionTooltip() {
        return getAttributeValue(descriptionText, "title");
    }

    @Step("Getting number of IPs on the Card...")
    public String getNumberOfIpOnCard() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(numberOfIpsOnIpCard);
        return getText(numberOfIpsOnIpCard);
    }

    @Step("Clicking on the IPs Card...")
    public IpCatalogPage clickOnIPCard() {
        waitTillVisibleWithFluentWait(numberOfIpsOnIpCard);
        numberOfIpsOnIpCard.click();
        waitForPageLoaded();
        return new IpCatalogPage(driver);
    }

    @Step("Clicking on copy-to-clipboard button...")
    public void clickOnCopyToClipboard() {
        click(copyToClipboardButton);
    }

    @Step("Hovering copy-to-clipboard icon...")
    public void hoverCopyToClipboardIcon() {
        hoverOverElement(copyToClipboardButton);
    }

    @Step("Verifying Library Details sub-header is displayed...")
    public boolean isLibraryDetailsSubHeaderPresent(String libName) {
        WebElement libDetailsHeader = findElementWithWait(By.xpath(librarySubHeader.replace("{name}", libName)));
        return isElementVisible(libDetailsHeader);
    }

    @Step("Open Library Details page IP count link in a new tab...")
    public void openLibraryDetailsCountIpLinkInNewTab() throws AWTException {
        openNewTabBrowserContextMenu(numberOfIpsOnIpCard);
        waitForPageLoaded();
        switchToOpenedTab();
    }

    @Step("Opening Library Details page...")
    public void openLibraryDetailsPage(String libraryId) {
        goTo(DriverFactory.getFullUrl(LIBRARY_DETAILS_PAGE + libraryId));
    }
}
