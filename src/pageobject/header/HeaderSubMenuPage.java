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
package com.methodics.phi.pageobject.header;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.ApiDocsPage;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.geofencing.GeofencingManagementPage;
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import com.methodics.phi.pageobject.ips.CreateIpPage;
import com.methodics.phi.pageobject.labels.LabelsManagementPage;
import com.methodics.phi.pageobject.libraries.LibrariesPage;
import com.methodics.phi.pageobject.libraries.LibraryCatalogPage;
import com.methodics.phi.pageobject.libraries.LibraryManagementPage;
import com.methodics.phi.pageobject.properties.PropertiesSetManagementPage;
import com.methodics.phi.pageobject.properties.PropertyManagementPage;
import com.methodics.phi.pageobject.queries.AdvancedSearchPage;
import com.methodics.phi.pageobject.queries.FoldersManagementPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("unused")
@FindBy(css = ".navbar")
public class HeaderSubMenuPage extends BasePage {
    private final WebDriver driver;

    /**
     * Constructor initializes PageFactory to inject @FindBy elements.
     */
    public HeaderSubMenuPage(WebDriver driver) {
        this.driver = driver;
        org.openqa.selenium.support.PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".menu-admin__library-manage a[data-testid='menu-item-link']")
    private WebElement librariesLink;

    @FindBy(css = ".menu-admin__labels-manage a[data-testid='menu-item-link']")
    private WebElement labelsLink;

    @FindBy(css = ".menu-admin__property-manage a[data-testid='menu-item-link']")
    private WebElement propertiesLink;

    @FindBy(css = ".menu-admin__propertysets a[data-testid='menu-item-link']")
    private WebElement propertySetLink;

    @FindBy(css = ".menu-admin__queryfolder-manage a[data-testid='menu-item-link']")
    private WebElement queryFolderLink;

    @FindBy(css = ".menu-admin__geofencing-manage a[data-testid='menu-item-link']")
    private WebElement geofencingLink;

    @FindBy(css = ".menu-admin__library-manage [data-testid='library-badge']")
    private WebElement libraryIcon;

    @FindBy(css = ".menu-admin__labels-manage i.fa-tags")
    private WebElement labelIcon;

    @FindBy(css = ".menu-admin__property-manage [data-testid='property-icon']")
    private WebElement propertyIcon;

    @FindBy(css = ".menu-admin__propertysets [data-testid='property-set-icon']")
    private WebElement propertySetIcon;

    @FindBy(css = ".menu-admin__queryfolder-manage i.fa-folder-magnifying-glass")
    private WebElement queryFolderIcon;

    @FindBy(css = ".menu-admin__geofencing-manage i.fa-location-dot")
    private WebElement geofencingIcon;

    @FindBy(css = ".menu-catalog__library-catalog")
    private WebElement libraryCatalogLink;

    @FindBy(css = ".menu-catalog__ip-catalog")
    private WebElement ipCatalogLink;

    @FindBy(css = ".menu-catalog__query-catalog")
    private WebElement advancedSearchLink;

    @FindBy(css = ".menu-new__create-ip")
    private WebElement createNewIpLink;

    @FindBy(xpath = "//a[contains(@class,'menu-item') and contains(.,'API docs')]")
    private WebElement apiDocs;

    @FindBy(css = ".menu-user__logout")
    private WebElement logoutButton;

    private static final String XPATH_CREATE_LIBRARY_BUTTON = "//a[@data-testid='menu-item-link' and contains(.,'New Library')]";
    private static final String XPATH_CREATE_LABEL_BUTTON = "//a[@data-testid='menu-item-link' and contains(.,'New Label')]";

    @Step("Clicking on Libraries link...")
    public LibrariesPage clickOnLibrariesLink() {
        click(librariesLink);
        return new LibrariesPage(driver);
    }

    @Step("Clicking on Labels link...")
    public LabelsManagementPage clickOnLabelsLink() {
        click(labelsLink);
        return new LabelsManagementPage(driver);
    }

    @Step("Clicking on Properties link...")
    public PropertyManagementPage clickOnPropertiesLink() {
        click(propertiesLink);
        return new PropertyManagementPage(driver);
    }

    @Step("Clicking on Property sets link...")
    public PropertiesSetManagementPage clickOnPropertySetsLink() {
        click(propertySetLink);
        return new PropertiesSetManagementPage(driver);
    }

    @Step("Clicking on Query Folder link...")
    public FoldersManagementPage clickOnQueryFolderLink() {
        click(queryFolderLink);
        return new FoldersManagementPage(driver);
    }

    @Step("Clicking on Library Catalog link...")
    public LibraryCatalogPage clickOnLibraryCatalogLink() {
        click(libraryCatalogLink);
        DriverFactory.sleep(1000);
        return new LibraryCatalogPage(driver);
    }

    @Step("Clicking on IP Catalog link...")
    public IpCatalogPage clickOnIpCatalogLink() {
        click(ipCatalogLink);
        return new IpCatalogPage(driver);
    }

    @Step("Clicking on Advanced Search link...")
    public AdvancedSearchPage clickOnAdvancedSearchLink() {
        click(advancedSearchLink);
        return new AdvancedSearchPage(driver);
    }

    @Step("Clicking on Geofencing link...")
    public GeofencingManagementPage clickOnGeoFencingLink() {
        click(geofencingLink);
        return new GeofencingManagementPage(driver);
    }

    @Step("Clicking on Create new IP link...")
    public CreateIpPage clickOnCreateNewIpLink() {
        click(createNewIpLink);
        return new CreateIpPage(driver);
    }

    @Step("Clicking on Create new Library link...")
    public LibraryManagementPage clickOnCreateNewLibraryLink() {
        final WebElement createNewLibraryButton = findElementWithWait(By.xpath(XPATH_CREATE_LIBRARY_BUTTON));
        click(createNewLibraryButton);
        return new LibraryManagementPage(driver);
    }

    @Step("Clicking on Create new Label link...")
    public LabelsManagementPage clickOnCreateNewLabelLink() {
        final WebElement crateNewLabelButton = findElementWithWait(By.xpath(XPATH_CREATE_LABEL_BUTTON));
        click(crateNewLabelButton);
        return new LabelsManagementPage(driver);
    }

    @Step("Clicking on API Docs link...")
    public ApiDocsPage clickOnApiDocsLink() {
        click(apiDocs);
        switchToOpenedTab();
        return new ApiDocsPage(driver);
    }

    @Step("Clicking on Logout button...")
    public void clickOnLogoutButton() {
        click(logoutButton);
        DriverFactory.sleep(1000);
    }

    @Step("Verifying Create Library Button is present...")
    public int getNumberOfCreateLibraryButtons(int counter) {
        return findElementsWithWait(By.xpath(XPATH_CREATE_LIBRARY_BUTTON), counter).size();
    }

    @Step("Verifying Create Label Button is present...")
    public int getNumberOfCreateLabelButtons(int counter) {
        return findElementsWithWait(By.xpath(XPATH_CREATE_LABEL_BUTTON), counter).size();
    }

    @Step("Verifying Libraries button icon is present...")
    public boolean isLibrariesButtonIconPresent() {
        return isElementVisible(libraryIcon);
    }

    @Step("Verifying Labels button icon is present...")
    public boolean isLabelsButtonIconPresent() {
        return isElementVisible(labelIcon);
    }

    @Step("Verifying Properties button icon is present...")
    public boolean isPropertiesButtonIconPresent() {
        return isElementVisible(propertyIcon);
    }

    @Step("Verifying Property Sets button icon is present...")
    public boolean isPropertySetsButtonIconPresent() {
        return isElementVisible(propertySetIcon);
    }

    @Step("Verifying Query Folders button icon is present...")
    public boolean isQueryFoldersButtonIconPresent() {
        return isElementVisible(queryFolderIcon);
    }

    @Step("Verifying Create New IP button is present...")
    public boolean isCreateNewIpButtonIconPresent() {
        return isElementVisible(createNewIpLink);
    }

    @Step("Log out...")
    public void logOut() {
        final HeaderPage headerPage = new HeaderPage(DriverFactory.getBrowserInstance());
        headerPage.clickOnLoggedInUserMenu();
        clickOnLogoutButton();
    }

    @Step("Verifying Geofencing button is present...")
    public boolean isGeofencingButtonIconPresent() {
        return isElementVisible(geofencingIcon);
    }

    @Step("Verifying Property Set is visible in Administration dropdown...")
    public boolean isPropertySetLinkVisible() {
        return isElementVisible(propertySetLink);
    }
}
