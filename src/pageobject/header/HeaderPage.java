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
package com.methodics.phi.pageobject.header;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.CustomPage;
import com.methodics.phi.pageobject.GridTablePage;
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import com.methodics.phi.pageobject.libraries.LibraryCatalogPage;
import com.methodics.phi.pageobject.queries.AdvancedSearchPage;
import static com.methodics.phi.util.CommonUrls.ADVANCED_SEARCH_PAGE;
import static com.methodics.phi.util.CommonUrls.IP_CATALOG_PAGE;
import static com.methodics.phi.util.CommonUrls.LIBRARY_CATALOG_PAGE;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@FindBy(css = ".navbar")
public class HeaderPage extends BasePage {
    private final WebDriver driver;

    public HeaderPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[@translate='dashboard.heading']")
    private WebElement dashboardHeading;

    @FindBy(css = ".app-header__navbar__brand a.navbar-brand")
    private WebElement productLogo;

    @FindBy(css = "div[data-testid='ui-dropdown-menu-admin'] > button")
    private WebElement administrationButton;

    @FindBy(css = "div[data-testid='ui-dropdown-menu-catalog'] > button")
    private WebElement catalogsButton;

    @FindBy(css = "li[data-testid='shopping-cart'] > a")
    private WebElement shoppingCartIcon;

    @FindBy(xpath = "//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']")
    private WebElement libraryCatalogButton;

    @FindBy(xpath = "//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']")
    private WebElement ipCatalogButton;

    @FindBy(xpath = "//a[@data-testid='menu-item-link']//span[text()='Advanced search']")
    private WebElement advancedSearchButton;

    @FindBy(css = "div[data-testid='ui-dropdown-menu-user']")
    private WebElement userDropdownMenu;

    @FindBy(css = ".menu-user__whats-new a")
    private WebElement whatsNewAdmin;

    @FindBy(css = "button.menu-user__logout")
    private WebElement logOutButton;

    @FindBy(css = "div.menu-user__app-versions")
    private WebElement appVersions;

    @FindBy(xpath = "//span[contains(@class, 'app-header__navbar__brand')]")
    private WebElement customHeader;

    @FindBy(xpath = "//div[contains(@class, 'page-title__heading')]")
    private WebElement pageHeaderNavigation;

    @FindBy(xpath = "//div[contains(@class,'page-name')]")
    private WebElement pageNameHeader;


    private final String loggedUserName = "//*[contains(@class, 'menu-user__username')]";
    private final String xpathCreateButton = "//span[normalize-space()='Create']";
    private final String dropDownItem = "//a[normalize-space(.)='{itemName}']";
    private final String dropDownItemIcon = "//*[@class='dropdown-item menu-item__inner-element']//*[normalize-space(text())='{itemName}']/..//i";
    private final String subMenuElements = "//span[normalize-space()='{text}']/parent::a/following-sibling::ul/li";
    private final String userMenu = "//span[contains(text(),'admin')]/ancestor::ul/li";
    private final String customMenuButton = "//a[contains(@class, 'link') and text()='{pageName}']";

    @Step("Getting the logged in user's name...")
    public String getLoggedUserName() {
        final WebElement username = findElementWithWait(By.xpath(loggedUserName));
        return getText(username);
    }

    @Step("Clicking on Administration Button...")
    public void clickOnAdministrationButton() {
        click(administrationButton);
    }

    @Step("Clicking on Catalogs Button...")
    public void clickOnCatalogsButton() {
        click(catalogsButton);
        DriverFactory.sleep(1000);
    }

    @Step("Clicking on Create Button...")
    public void clickOnCreateButton() {
        final WebElement createButton = findElementWithFluentWait(By.xpath(xpathCreateButton));
        click(createButton);
    }

    @Step("Verifying Create Button is present...")
    public int getCreateButtonNumber(int counter) {
        return findElementsWithWait(By.xpath(xpathCreateButton), counter).size();
    }

    @Step("Clicking on Library Catalog Button...")
    public LibraryCatalogPage clickOnLibraryCatalogButton() {
        click(libraryCatalogButton);
        waitForPageToLoad(LIBRARY_CATALOG_PAGE);
        return new LibraryCatalogPage(driver);
    }

    @Step("Clicking on Library Catalog Button...")
    public void onlyClickOnLibraryCatalogButton() {
        click(libraryCatalogButton);
    }

    @Step("Clicking on Ip Catalog Button...")
    public IpCatalogPage clickOnIpCatalogButton() {
        click(ipCatalogButton);
        waitForPageToLoad(IP_CATALOG_PAGE);
        return new IpCatalogPage(driver);
    }

    @Step("Clicking on Advanced Search Button...")
    public AdvancedSearchPage clickOnAdvancedSearchButton() {
        click(advancedSearchButton);
        waitForPageToLoad(ADVANCED_SEARCH_PAGE);
        return new AdvancedSearchPage(driver);
    }

    @Step("Getting number of links in the sub Menu...")
    public int getNumberOfLinksInSubMenu(String parent, int counter) {
        final int numberOfLinks = findElements(By.xpath(subMenuElements.replace("{text}", parent))).size();
        return numberOfLinks==0 ? counter:numberOfLinks;
    }

    @Step("Getting number of links in the user's sub Menu...")
    public int getNumberOfLinksInUserSubMenu(String user, int counter) {
        waitForPageLoaded();
        final int numberOfLinks = findElements(By.xpath(userMenu.replace("{text}", user))).size();
        return numberOfLinks==0 ? counter:numberOfLinks;
    }

    @Step("Clicking on logged in user's menu...")
    public void clickOnLoggedInUserMenu() {
        final WebElement username = findElementWithFluentWait(By.xpath(loggedUserName));
        click(username);
    }

    @Step("Verifying product logo is present...")
    public boolean isProductIconPresent() {
        return isElementVisible(productLogo);
    }

    @Step("Verifying IP Catalog Button is present...")
    public boolean isIpCatalogButtonPresent() {
        return isElementVisible(ipCatalogButton);
    }

    @Step("Verifying Catalogs Dropdown Button is present...")
    public boolean isCatalogsDropDownButtonPresent() {
        return isElementVisible(catalogsButton);
    }

    @Step("Verifying Administrator Button is present...")
    public boolean isAdministratorDropDownButtonPresent() {
        return isElementVisible(administrationButton);
    }

    @Step("Verifying Advanced Search Button is present...")
    public boolean isAdvancedSearchButtonPresent() {
        return isElementVisible(advancedSearchButton);
    }

    @Step("Verifying Custom header is present...")
    public String getCustomHeader() {
        return getText(customHeader);
    }

    @Step("Verifying Create Button is present...")
    public boolean isCreateButtonPresent() {
        final WebElement createButton = driver.findElement(By.xpath(xpathCreateButton));
        return isElementVisible(createButton);
    }

    @Step("Verifying menu item is present...")
    public boolean isMenuItemPresent(String menuItem) {
        final WebElement menuItemElement = findElementWithWait(By.xpath(dropDownItem.replace("{itemName}", menuItem)));
        return isElementVisible(menuItemElement);
    }

    @Step("click on menu item...")
    public void clickOnMenuItem(String menuItem) {
        final WebElement menuItemElement = findElementWithWait(By.xpath(dropDownItem.replace("{itemName}", menuItem)));
        click(menuItemElement);
    }

    @Step("Click on logo...")
    public boolean isUrlContain(String url) {
        //until Favorites page will be refactored we can't avoid sleeps - If you click pages to fast - history simply won't appear.
        //tested this manually also - I spoke with Volodya and showed him console errors.
        DriverFactory.sleep(1000);
        waitForElementUrlContain(url);
        return true;
    }

    @Step("Click on custom mane button...")
    public CustomPage clickOnMenuButton(String pageName) {
        final WebElement menuButton = findElementWithWait(By.xpath(customMenuButton.replace("{pageName}", pageName)));
        click(menuButton);
        return new CustomPage(driver);
    }

    @Step("Click on whats new in admin menu")
    public void clickOnWhatsNewInAdmin() {
        click(whatsNewAdmin);
    }

    @Step("Getting Ui and Server version in footer... ")
    public String getUiAndServerVersion() {
        clickOnLoggedInUserMenu();
        return getText(appVersions);
    }

    @Step("click on shopping cart Icon...")
    public void clickOnShoppingCartIcon() {
        click(shoppingCartIcon);
        final GridTablePage gridTablePage = new GridTablePage(DriverFactory.getBrowserInstance());
        gridTablePage.waitForDataToLoad();
    }

    @Step("Verifying menu item icon is present...")
    public boolean isMenuItemIconPresent(String menuItem, String menuItemIcon) {
        final WebElement menuItemElement = findElementWithFluentWait(By.xpath(dropDownItemIcon.replace("{itemName}", menuItem)));
        final String classValue = menuItemElement.getAttribute("class");
        return classValue.contains(menuItemIcon);
    }

    public String getPageHeaderNavigation() {
        waitTillVisibleWithFluentWait(pageHeaderNavigation);
        return pageHeaderNavigation.getText().replace("\n", " ").replace("\r", " ").trim();
    }

    public String getPageheader() {
        waitTillVisibleWithFluentWait(pageNameHeader);
        return pageNameHeader.getText().replace("\n\r", " ").trim();
    }
}
