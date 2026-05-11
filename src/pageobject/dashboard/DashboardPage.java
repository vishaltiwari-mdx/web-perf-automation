/**
 * ################################################################################
 * # Copyright (c) 2010-2025 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject.dashboard;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import com.methodics.phi.pageobject.libraries.LibraryCatalogPage;
import com.methodics.phi.pageobject.queries.AdvancedSearchPage;
import io.qameta.allure.Step;
import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {
    private WebDriver driver;
    private static final String DASHBOARD_IN_TITLE = "Home";

    public DashboardPage(WebDriver driver) {
        this.driver = driver;

        waitForTitle(DASHBOARD_IN_TITLE);
        if (!driver.getTitle().contains("Home")) {
            throw new IllegalStateException("This is not a Home Page, current page is: " + driver.getTitle());
        }
    }

    @FindBy(xpath = "//input[contains(@class, 'base-input-field__input')]")
    private WebElement ipSearchInCatalog;

    @FindBy(css = "button[data-testid='ui-btn-dashboard-tooltips-ip-catalog-search']")
    private WebElement ipSearchButton;

    @FindBy(xpath = "//button[@data-testid='ui-btn-dashboard-tooltips-ip-catalog-search'][@disabled]")
    private WebElement ipSearchBtnDisabled;

    @FindBy(xpath = "//a[contains(@class, 'adv-search-button')]")
    private WebElement advSearchButton;

    @FindBy(xpath = "//a[contains(@class, 'whats-new-button')]")
    private WebElement whatsNewButton;

    @FindBy(xpath = "//a[contains(@class, 'dashboard-ips-button')]")
    private WebElement ipsButton;

    @FindBy(xpath = "//a[contains(@class, 'libraries-button')]")
    private WebElement librariesButton;

    @FindBy(css = "[data-testid='main-logo-img'] ")
    private WebElement productLogo;

    @FindBy(xpath = "//b[text()='Helix IPLM']")
    private WebElement productName;

    @FindBy(xpath = "//button[@title='Previous tip']")
    private WebElement shevronPrevButton;

    @FindBy(xpath = "//button[@title='Next tip']")
    private WebElement shevronNextButton;

    @FindBy(xpath = "//i[contains(@class, 'content-fa-icon')]")
    private WebElement tipIcon;

    @FindBy(xpath = "//h5[contains(@class, 'tips__title-text')]")
    private WebElement tipHeader;

    @FindBy(xpath = "//div[@class='tips__text text-break']")
    private WebElement tipText;

    @FindBy(xpath = "//li/*[contains(text(),'Shopping cart')]")
    private WebElement shoppingCartMenuButton;

    @FindBy(xpath = "//li[contains(.,'Shopping cart')]")
    private WebElement shoppingCartMenuButtonTip;

    @FindBy(xpath = "//*[@class='tooltip-inner']")
    private WebElement toolTipHoverOverElement;

    private String tipsMainTitle = "//div[@class= 'tips__title']";
    private String openSearchModeDropdown = "//button[contains(@class,'dropdown-toggle')]//span[normalize-space()='Contains']";
    private String selectSearchModeName = "//span[@class='flex-grow-1 text-truncate menu-item-text'][normalize-space()='{searchModeName}']";

    @Step("Verify Tips title is present ...")
    public boolean isMainTipsTitlePresent() {
        WebElement tipsTitle = findElementWithWait(By.xpath(tipsMainTitle));
        return isElementVisible(tipsTitle);
    }

    @Step("Verify the tip icon is present ...")
    public boolean isTipIconPresent() {
        return isElementVisible(tipIcon);
    }

    @Step("Get the tip title text...")
    public String getTipTitle() {
        return tipHeader.getAttribute("title");
    }

    @Step("Get the tip body text...")
    public String getTipText() {
        return getText(tipText);
    }

    @Step("Click on next tip...")
    public void nextTip() {
        click(shevronNextButton);
    }

    @Step("Click on previous tip...")
    public void prevTip() {
        click(shevronPrevButton);
    }

    @Step("Click on logo...")
    public void openDashboardPage() {
        DriverFactory.sleep(1000);
        productLogo.click();
    }

    @Step("Click on logo...")
    public boolean isUrlContain(String url) {
        //until Favorites page will be refactored we can't avoid sleeps - If you click pages to fast - history simply won't appear.
        //tested this manually also - I spoke with Volodya and showed him console errors.
        DriverFactory.sleep(1000);
        waitForElementUrlContain(url);
        return true;
    }

    @Step("Verify search IP bar is present on the Dashboard page...")
    public boolean isSearchIPBarPresent() {
        return isElementVisible(ipSearchInCatalog);
    }

    @Step("Search IP in IP catalog...")
    public IpCatalogPage searchIpInCatalog(String ipSearch) {
        inputText(ipSearchInCatalog, ipSearch);
        click(ipSearchButton);
        waitForPageLoaded();
        return new IpCatalogPage(driver);
    }

    @Step("Click on Advanced Search button...")
    public AdvancedSearchPage clickOnAdvSearchButton() {
        click(advSearchButton);
        return new AdvancedSearchPage(driver);
    }

    @Step("Click on IPs button...")
    public IpCatalogPage clickOnIPsButton() {
        click(ipsButton);
        return new IpCatalogPage(driver);
    }

    @Step("Click on Libraries button...")
    public LibraryCatalogPage clickOnLibrariesButton() {
        click(librariesButton);
        return new LibraryCatalogPage(driver);
    }

    @Step("Open Dashboard Ip quick link in a new tab...")
    public void openDashboardIpQuickLinkInNewTab() throws AWTException {
        openNewTabBrowserContextMenu(ipsButton);
        switchToOpenedTab();
    }

    @Step("Open Dashboard Advance Search quick link in a new tab...")
    public void openDashboardAdvSearchQuickLinkInNewTab() throws AWTException {
        openNewTabBrowserContextMenu(advSearchButton);
        switchToOpenedTab();
    }

    @Step("Open Dashboard Library quick link in a new tab...")
    public void openDashboardLibraryQuickLinkInNewTab() throws AWTException {
        openNewTabBrowserContextMenu(librariesButton);
        switchToOpenedTab();
    }

    @Step("Verify product logo is present on the Dashboard page...")
    public boolean isProductLogoPresent() {
        return isElementVisible(productLogo);
    }

    @Step("Verify product name is displayed in footer...")
    public boolean isProductNamePresentInFooter() {
        return isElementVisible(productName);
    }

    @Step("Enter text in the search bar...")
    public void enterTextInSearchbar(String searchTerm) {
        inputText(ipSearchInCatalog, searchTerm);
    }

    @Step("Hit Enter key...")
    public void pressEnterKey() {
        pressEnterKey(ipSearchInCatalog);
    }

    @Step("Click on whats new button button...")
    public void clickOnWhatsNewButton() {
        click(whatsNewButton);
    }

    @Step("Verify IP search button is disabled ...")
    public boolean isIpSearchBtnDisabled() {
        return isElementVisible(ipSearchBtnDisabled);
    }

    @Step("Verify shopping cart button is present and disable ...")
    public boolean isShoppingCartButtonPresentAndDisabled() {
        logger.info("Verify shopping cart button is present and disable ...");
        waitForPageLoaded();
        waitForElementToBeVisible(shoppingCartMenuButton);
        return isElementVisible(shoppingCartMenuButton) && shoppingCartMenuButton.getAttribute("class").contains("disabled");
    }

    @Step(" Get the tool tip shopping cart menu on hover   ...")
    public String getShoppingCartToolTipOnHover() {
        logger.info("Get the tool tip shopping cart menu on hover ...");
        waitForPageLoaded();
        hoverOverElement(shoppingCartMenuButton);
        waitForElementToBeVisible(toolTipHoverOverElement);
        return toolTipHoverOverElement.getText();
    }

    @Step("Verify shopping cart button is present and enabled ...")
    public boolean isShoppingCartButtonPresentAndEnabled() {
        logger.info("Verify shopping cart button is present and enabled ...");
        waitForPageLoaded();
        return isElementVisible(shoppingCartMenuButton) && !shoppingCartMenuButton.getAttribute("class").contains("disabled");
    }

    @Step("Click search mode dropdown...")
    public void clickSearchModeDropdown(String searchMode) {
        WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        click(xpathSearchModeDropdown);
        WebElement xpathSelectSearchModeName = findElementWithWait(By.xpath(selectSearchModeName.replace("{searchModeName}", searchMode)));
        click(xpathSelectSearchModeName);
    }

    @Step("Get selected search mode text...")
    public String getSelectedSearchMode() {
        WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        return getText(xpathSearchModeDropdown);
    }

    public DashboardPage maximizeWindow() {
        driver.manage().window().maximize();
        return this;
    }
}
