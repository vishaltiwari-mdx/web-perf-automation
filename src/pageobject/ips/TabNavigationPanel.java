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

import com.methodics.phi.actions.WebElementActions;
import static com.methodics.phi.actions.WebElementActions.PageModelName.*;
import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.attributes.AttributesTabPage;
import com.methodics.phi.pageobject.login.SignInPage;
import static com.methodics.phi.util.CommonUrls.CREATE_IP_PAGE;
import static com.methodics.phi.util.CommonUrls.GLOBAL_SHOPPING_CART_URL;
import static com.methodics.phi.util.CommonUrls.IP_FQN_URL;
import static com.methodics.phi.util.ConstantTabs.*;
import static com.methodics.phi.util.Constants.NON_ADMIN_MDXUSER;
import static com.methodics.phi.util.Constants.TUTORIAL_IP_FQN;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TabNavigationPanel extends BasePage {
    private WebDriver driver;

    public static final String GLOBAL_SHOPPING_CART = "Global Shopping Cart";
    public static final String RESOURCE_TAB = "Resource Tab";
    public static final String ADD_RESOURCES = "Add Resources";
    public static final String SHOPPING_CART_TAB = "Shopping Cart Tab";
    public static final String VERSION_LIST_TAB = "Version list tab";

    public TabNavigationPanel(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[normalize-space()='Details']")
    private WebElement detailsTab;

    @FindBy(xpath = "//span[normalize-space()='Hierarchy and contents']")
    private WebElement ipvHierarchyAndContentsTab;

    @FindBy(xpath = "//span[normalize-space()='Usage']")
    private WebElement ipvUsageTab;

    @FindBy(xpath = "//span[normalize-space()='Dashboard']")
    private WebElement dashboardTab;

    @FindBy(xpath = "//span[normalize-space()='Planning BOM']")
    private WebElement planningBomTab;

    @FindBy(xpath = "//span[normalize-space()='Version list']")
    private WebElement versionListTab;

    @FindBy(xpath = "//span[normalize-space()='Line graph']")
    private WebElement lineGraphTab;

    @FindBy(xpath = "//*[contains(@data-testid,'tab-list__nav-item')]")
    private List<WebElement> tabs;

    @FindBy(xpath = "//div[@class='tab-header mb-4']//h3")
    private WebElement tabHeader;

    @FindBy(xpath = "//h5[text()='Test tab']")
    private WebElement testTabHeader;

    @FindBy(xpath = "//h4[text()='IP Errata']")
    private WebElement ipvErrataTabHeader;

    @FindBy(xpath = "//b[text()='Number of IP units:  and corresponding ASP:$']")
    private WebElement ipvLicensingTabHeader;

    @FindBy(xpath = "//span[text()='SharePoint']")
    private WebElement ipvSharePointTabHeader;

    @FindBy(xpath = "//div[text()='Test Ipv Tab']")
    private WebElement ipvTestIpvTabHeader;

    @FindBy(xpath = "//h5[@class='no-rows-overlay__text my-0']")
    private WebElement noDataMessage;

    @FindBy(xpath = "//div[contains(@class,'neon-tab')]//h1")
    private WebElement customTabHeader;

    @FindBy(css = "div.position-fixed.overlay")
    private WebElement pageOverlay;

    private String verticalTab = "//span[normalize-space()='{tabName}']";
    private String parentsTab = "//span[normalize-space()='Parents']";
    private String hooksTab = "//ul[@role='tablist']/li[normalize-space()='Hooks']";
    private String customTab = "//span[normalize-space()='Test']";
    private String selectedTab = "//li[contains(@class,'list-group-item')]//div[contains(@class,'selected')]";
    private String selectedSubTab = "//ul[@role='tablist']//a[contains(@class,'active')]";
    private String tabIcon = "//span[normalize-space()='{tab}']/ancestor::li[contains(@data-testid, 'tab-list__nav-item-')]";
    private String xpathIntelDashboard = "//h4[@class='card-title' and contains(text(),'{name}')]";
    private String widgetsCountBadge = "//span[contains(@class, 'mdx-nav-item__div__badge')]";
    private String tabsGroupCaretIcon = "//li[@data-testid='tab-list__{group}-related-group']//child::div[contains(@class, 'caret-icon')]";
    private String tabGroupExpanded = "//div[@id='navItemGroupContent_{group}'][@class='collapse show']";
    private String tabGroupCollapsed = "//li[@type='{group}']//div[@id='navItemGroupContent_{group}' and contains(@class,'collapse show')]";
    private String nonHorizontalTab = "//li[@class='tabs__nav-item nav-item']//a//child::span[normalize-space()='{tabName}']";
    private String horizontalTab = "//div//span[normalize-space()='{tabName}']";

    @Step("Open Details tab...")
    public void openDetailsTab() {
        click(detailsTab);
    }

    @Step("Open Hierarchy and Contents tab...")
    public HierarchyTabPage openHierarchyAndContentsTab() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(ipvHierarchyAndContentsTab);
        waitTillClickableWithFluentWait(ipvHierarchyAndContentsTab).click();
        waitForPageLoaded();
        return new HierarchyTabPage(driver);
    }

    @Step("Open dashboard tab...")
    public AttributesTabPage openDashboardTab() {
        DriverFactory.sleep(2000);
        click(dashboardTab);
        return new AttributesTabPage(driver);
    }

    @Step("Open the Usage tab...")
    public UsageTabPage openUsageTab() {
        initWait().until(ExpectedConditions.invisibilityOf(pageOverlay));
        waitTillClickableWithFluentWait(ipvUsageTab).click();
        return new UsageTabPage(driver);
    }

    @Step("Open IP dashboard tab...")
    public AttributesTabPage openIpDashboardTab() {
        click(dashboardTab);
        return new AttributesTabPage(driver);
    }

    @Step("Call the Open Permissions tab method from IpDetailsPage POM...")
    public PermissionsTabPage openPermissionsTab() {
        IpDetailsPage ipDetailsPage = new IpDetailsPage(driver);
        return ipDetailsPage.clickOnPermissionsTab();
    }

    @Step("Open Line Graph tab...")
    public LineGraphTabPage openLineGraphTab() {
        click(lineGraphTab);
        return new LineGraphTabPage(driver);
    }

    @Step("Open Parents tab...")
    public ParentsTabPage openParentsTab() {
        click(findElementWithWait(By.xpath(parentsTab)));
        return new ParentsTabPage(driver);
    }

    @Step("Open Hooks tab...")
    public HooksTabPage openHooksTab() {
        click(findElementWithWait(By.xpath(hooksTab)));
        return new HooksTabPage(driver);
    }

    @Step("Open version list tab...")
    public VersionListTabPage openVersionListTab() {
        click(versionListTab);
        return new VersionListTabPage(driver);
    }

    @Step("Open Planning BOM tab...")
    public PlanningBOMPage openPlanningBomTab() {
        click(planningBomTab);
        return new PlanningBOMPage(driver);
    }

    @Step("Get number of tabs...")
    public int getNumberOfTabs() {
        return getNumberOfVisibleElements(tabs);
    }

    @Step("Verify the  tab  is active...")
    public boolean isTabActive(String targetTab) {
        WebElement activeTab = findElementWithWait(By.xpath(selectedTab));
        return targetTab.equals(activeTab.getText());
    }

    @Step("Verify if a tab is visible...")
    public boolean isTabVisible(String name) {
        try {
            logger.info("Checking if tab is displayed: {}", name);
            WebElement column = waitForElementToBePresent(By.xpath(verticalTab.replace("{tabName}", name)));
            return isElementVisible(column);
        } catch (WebDriverException e) {
            logger.info(name + " tab is not visible...");
            return false;
        }
    }

    @Step("Verify the sub-tab is active...")
    public boolean isSubTabActive(String targetSubTab) {
        WebElement activeSubTab = findElementWithWait(By.xpath(selectedSubTab));
        return targetSubTab.equals(activeSubTab.getText());
    }

    @Step("Click on tab with name: {tabName}")
    public void clickOnTab(String tabName) {
        final Set<String> nonHorizontalTabs = Set.of(INFO_TAB, PROPERTIES_TAB, PROJECT_PROPERTIES_TAB, PERMISSIONS_TAB, HOOKS_TAB);
        if (nonHorizontalTabs.contains(tabName)) {
            findElementWithFluentWait(By.xpath(nonHorizontalTab.replace("{tabName}", tabName))).click();
        } else {
            clickOnSideMenu(tabName);
        }
        waitForPageLoaded();
    }

    @Step("Click on side menu with name: {menuName}")
    public void clickOnSideMenu(String menuName) {
        findElementWithFluentWait(By.xpath(horizontalTab.replace("{tabName}", menuName))).click();
    }

    @Step("Hover over tab...")
    public void hoverOverTab(String tabName) {
        WebElement tab = findElementWithWait(By.xpath(tabIcon.replace("{tab}", tabName)));
        hoverOverElement(tab);
    }

    @Step("Get tab header...")
    public String getTabHeader() {
        return getText(tabHeader);
    }

    @Step("Verify custom Test tab header is displayed...")
    public boolean isMyTestTabHeaderDisplayed() {
        return isElementVisible(testTabHeader);
    }

    @Step("Verify Errata tab header is displayed...")
    public boolean isErrataTabHeaderDisplayed() {
        return isElementVisible(ipvErrataTabHeader);
    }

    @Step("Verify SharePoint tab header is displayed...")
    public boolean isSharePointTabHeaderDisplayed() {
        scrollToElement(ipvSharePointTabHeader);
        return isElementVisible(ipvSharePointTabHeader);
    }

    @Step("Verify Licensing tab header is displayed...")
    public boolean isLicensingTabHeaderDisplayed() {
        return isElementVisible(ipvLicensingTabHeader);
    }

    @Step("Verify TestIpv tab header is displayed...")
    public boolean isTestIpvTabHeaderDisplayed() {
        return isElementVisible(ipvTestIpvTabHeader);
    }

    @Step("Verifying Intel Dashboard tab body: {0} is displayed...")
    public boolean intelDashboardTab(String boxName) {
        WebElement intelDashboard = findElementWithWait(By.xpath(xpathIntelDashboard.replace("{name}", boxName)));
        return boxName.equals(intelDashboard.getText());
    }

    @Step("Get number of widgets on badge...")
    public int getNumberOfWidgetsOnBadge(String tabGroup) {
        WebElement badge = findElementWithWait(By.xpath(widgetsCountBadge.replace("{group}", tabGroup)));
        return Integer.parseInt(badge.getText());
    }

    @Step("Hover over widgets count badge...")
    public void hoverWidgetsCountBadge(String tabGroup) {
        hoverOverElement(findElementWithWait(By.xpath(widgetsCountBadge.replace("{group}", tabGroup))));
    }

    @Step("Hover over tabs group caret icon...")
    public void hoverOverTabsGroupCaret(String group) {
        WebElement ipRelatedGroupCaretIcon = findElementWithWait(By.xpath(tabsGroupCaretIcon.replace("{group}", group.toLowerCase())));
        hoverOverElement(ipRelatedGroupCaretIcon);
    }

    @Step("Click on tabs group caret icon...")
    public void toggleTabsGroupVisibility(String group) {
        WebElement ipRelatedGroupCaretIcon = findElementWithWait(By.xpath(tabsGroupCaretIcon.replace("{group}", group.toLowerCase())));
        click(ipRelatedGroupCaretIcon);
        DriverFactory.sleep(1000);
    }

    @Step("Verify tabs group is expanded...")
    public boolean isTabGroupExpanded(String group) {
        return isElementVisible(tabGroupExpanded.replace("{group}", group));
    }

    @Step("Verify tabs group is collapsed...")
    public boolean isTabGroupCollapsed(String group) {
        return !isElementVisible(tabGroupCollapsed.replace("{group}", group));
    }

    @Step("Verify 'No Data Displayed' message...")
    public boolean verifyTabEmptyState(String text) {
        return text.equals(getText(noDataMessage));
    }

    @Step("Get custom tab header...")
    public String getCustomTabHeader() {
        scrollToElement(customTabHeader);
        return getText(customTabHeader);
    }

    @Step("Navigate to tab/page...")
    public static WebElementActions.PageModelName navigation(String user, String pageName) throws InterruptedException {
        String url = CREATE_IP_PAGE;
        return navigation(user, url, pageName);
    }

    @Step("Navigate to tab/page...")
    public static WebElementActions.PageModelName navigation(String user, String url, String pageName) throws InterruptedException {
        final AddResourcesPage resourcesPage = new AddResourcesPage(DriverFactory.getBrowserInstance());
        final SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        WebElementActions.PageModelName pageModelName = null;

        if (user.equals(NON_ADMIN_MDXUSER)) {
            url = IP_FQN_URL;
        }
        switch (pageName) {
            case RESOURCE_TAB:
                resourcesPage.navigationToResourceTabUsingEditIp(user, IP_FQN_URL);
                pageModelName = AddResourceTabsID;
                break;
            case ADD_RESOURCES:
                resourcesPage.navigationToAddResourceUsingEditIp(user, url);
                pageModelName = AddResourcesID;
                break;
            case SHOPPING_CART_TAB:
                resourcesPage.navigationToShoppingCartTabUsingEditIp(user, url);
                pageModelName = ShoppingCartStandaloneID;
                break;
            case GLOBAL_SHOPPING_CART:
                signInPage.login(user);
                signInPage.goTo(DriverFactory.getFullUrl(GLOBAL_SHOPPING_CART_URL));
                pageModelName = ShoppingCartGlobalID;
                break;
            case VERSION_LIST_TAB:
                signInPage.login(user);
                IpDetailsPage ipDetailsPage = new IpDetailsPage(DriverFactory.getBrowserInstance());
                ipDetailsPage.goToIpPage(TUTORIAL_IP_FQN);
                TabNavigationPanel tabNavigationPanel = new TabNavigationPanel(DriverFactory.getBrowserInstance());
                tabNavigationPanel.openVersionListTab();
                pageModelName = VersionListID;
                break;
            default:
                break;
        }
        return pageModelName;
    }
}
