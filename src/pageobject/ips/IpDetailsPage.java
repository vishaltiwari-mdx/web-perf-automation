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
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import com.methodics.phi.pageobject.libraries.LibraryDetailsPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.awt.*;
import java.util.List;
import org.openqa.selenium.support.ui.*;

public class IpDetailsPage extends BasePage {
    private WebDriver driver;

    public IpDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class,'breadcrumbs')]//a[text()='Home']")
    private WebElement homeBreadcrumb;

    @FindBy(xpath = "//div[contains(@class,'breadcrumbs')]//a[text()='IP catalog']")
    private WebElement ipCatalogBreadcrumb;

    @FindBy(xpath = "//div[contains(@class,'breadcrumbs')]//a[text()='Advanced search']")
    private WebElement advSearchBreadcrumb;

    @FindBy(xpath = "//div[contains(@class, 'breadcrumbs__button')]")
    private WebElement breadcrumbsButton;

    @FindBy(xpath = "//div[contains(@class,'left-sidebar')]//button[contains(@class, 'sidebar-collapse-btn_left')]")
    private WebElement collapseLeftPanel;

    @FindBy(xpath = "//div[contains(@class,'ip-page__left-sidebar')][@style='width: 68px;']")
    private WebElement collapsedLeftPanel;

    @FindBy(xpath = "//div[contains(@class,'left-sidebar')]//button[contains(@class, 'sidebar-collapse-btn_right')]")
    private WebElement expandLeftPanel;

    @FindBy(css = "div#navItemGroupContent_IP[class='collapse show']")
    private WebElement expandedLeftPanel;

    @FindBy(xpath = "//span[@class='fqn-bar__title-cursor']")
    private WebElement libraryName;

    @FindBy(xpath = "//i[contains(@class, 'clipboard')]")
    private WebElement copyToClipboardButton;

    @FindBy(xpath = "//a[normalize-space()='Edit latest IPV']")
    private WebElement editIpButton;

    @FindBy(css = "[data-testid='ui-btn-ip-details-copy-ipv']")
    private WebElement copyIpvButton;

    @FindBy(xpath = "//button[contains(@class, 'create-line')][normalize-space()='Create IP Line']")
    private WebElement createLineButton;

    @FindBy(css = "[data-testid='ip-details-info-tab']")
    private WebElement infoTab;

    @FindBy(css = "[data-testid='ip-details-info-tab'][aria-selected='true']")
    private WebElement infoTabActive;

    @FindBy(css = "[data-testid='ip-details-properties-tab'] span")
    private WebElement propertiesTab;

    @FindBy(css = "[data-testid='ip-details-properties-tab'][aria-selected='true']")
    private WebElement propertiesTabActive;

    @FindBy(css = "[data-testid='ip-details-project-properties-tab']")
    private WebElement projectPropertiesTab;

    @FindBy(css = "[data-testid='ip-details-project-properties-tab'][aria-selected='true']")
    private WebElement projectPropertiesTabActive;

    @FindBy(css = "[data-testid='ip-details-permissions-tab']")
    private WebElement permissionsTab;

    @FindBy(css = "[data-testid='ip-details-hooks-tab']")
    private WebElement hooksTab;

    @FindBy(css = "[data-testid='ip-details-hooks-tab'][aria-disabled='true']")
    private WebElement hooksTabDisabled;

    @FindBy(css = "[data-testid='contents-tab']")
    private WebElement contentsTab;

    @FindBy(xpath = "//a[normalize-space()='Contents'][@aria-selected='true']")
    private WebElement contentsTabActive;

    @FindBy(css = "[data-testid='hierarchy-tab']")
    private WebElement hierarchyTab;

    @FindBy(css = "[data-testid='ip-details-hierarchy-tab'][aria-selected='true']")
    private WebElement hierarchyTabActive;

    @FindBy(css = "[data-testid='ip-details-edit-ip']")
    private WebElement xpathEditButton;

    @FindBy(xpath = "//*[@id='ipvInfoCard']//div[normalize-space(.)='Repo path']/following-sibling::div")
    private WebElement repoPathValue;

    @FindBy(xpath = "//*[@data-testid='ipv_add-to-sc-btn']")
    private WebElement addToShoppingCartButton;

    @FindBy(xpath = "//h3[normalize-space()='Details']")
    private WebElement detailsHeader;

    @FindBy(xpath = "//a[@data-testid='dashboard-ip-tab']//span[contains(@class, 'badge-counter')]")
    private WebElement numberOfAttributesOnIpDashboardCard;

    @FindBy(xpath = "//a[@data-testid='dashboard-version-tab']//span[contains(@class, 'badge-counter')]")
    private WebElement numberOfAttributesOnIpvDashboardCard;

    @FindBy(xpath = "//a[@data-testid='dashboard-line-tab']//span[contains(@class, 'badge-counter')]")
    private WebElement numberOfAttributesOnLineDashboardCard;

    @FindBy(css = "span[data-testid='ui-tooltip-html-content']")
    private WebElement PlannedIpText;

    @FindBy(css = "div.position-fixed.overlay")
    private WebElement pageOverlay;

    private String breadcrumbDropdownMenu = "//ul[@class='dropdown-menu show']//span[normalize-space()='{text}']";
    private String ipFqn = "//div[contains(@class, 'fqn-bar')][@title={ipv}]";
    private String ipIcon = "//div[@title='{lib}.{ip}@{version}.{line}']/preceding-sibling::*[contains(@class,'{icon}')]";
    private String xpathTooltip = "//div[contains(@class,'tooltip-inner')][normalize-space()='{text}']";
    private String csvExportButton = "//button[@data-testid='ui-btn-properties-grid-csv-export']";
    private String xpathExpandHiddenItemsButton = "//div[normalize-space()='{fieldType}']/following-sibling::*//span[contains(@class,'expand-button')]";
    private String xpathCollapseHiddenItemsButton = "//div[normalize-space()='{fieldType}']/following-sibling::*//span[contains(@class,'collapse-button')]//i[contains(@class, 'fa-angle-up')]";
    private String xpathCollapseTextButton = "//div[@id='{fieldName}']//*[contains(@class, 'collapse')]/*[contains(@class, 'fa-regular fa-angle-up fa-fw')]";
    private String xpathExpandTextButton = "//div[@id='{fieldName}']//*[contains(@class, 'content-collapse-button')]/*[contains(@class, 'fa-regular fa-ellipsis fa-fw')]";
    private String xpathExpandedTextRow = "//div[@id='{fieldName}']//div[normalize-space()='{field}']/following-sibling::*//div[contains(@class,'expanded')]";
    private String xpathCollapsedTextRow = "//div[@id='{fieldName}']//div[normalize-space()='{field}']/following-sibling::*//div[contains(@class,'default')]";
    private String buttonIsDisabled = "//button[contains(@class,'disabled') and normalize-space()='{ButtonName}']";
    private String disabledEditIpButton = "//a[@data-testid='ip-details-edit-ip' and normalize-space()='Edit latest IPV' and contains(@class,'disabled')]";
    private String xpathTitleDashboardWidgets = "//div[contains(@class,'title-dashboard')]//div[@class='card widget-card px-3 pb-3 w-100 h-100 pt-3 widget-card_locked widget-{attributeType} widget__{name}' or @class='card widget-card px-3 pb-3 w-100 h-100 widget-card_locked widget-{attributeType} widget__{name}']";
    private String loadingSpinner = "//span[contains(@class, 'spinner-border')]";
    private String countTitleDashboardWidgets = "//div[contains(@class,'title-dashboard')]//div[@class='vue-grid-item']";
    private String xpathTooltipTestId = "//span[@data-testid='ui-tooltip-text-content'][text()='{text}']";
    private String xpathForTabLocationWithIndex = "//div[@data-testid='ip-details-tabs']//ul[@role='tablist']/li[{index}]//span";

    @Step("Verify that the page header is visible on the IP Details page")
    public boolean isPageHeaderDisplayed() {
        waitTillVisibleWithFluentWait(detailsHeader);
        return detailsHeader.isDisplayed();
    }

    @Step("Expand hidden items for field '{fieldName}' and type '{fieldType}' with button text '{buttonText}'")
    public void clickOnExpandHiddenItemsButton(String fieldName, String fieldType, String buttonText) {
        WebElement expandButton = findElementWithWait(By.xpath(xpathExpandHiddenItemsButton.replace("{fieldName}", fieldName).replace("{fieldType}", fieldType).replace("{numberOfHiddenItems}", buttonText)));
        waitTillClickableWithFluentWait(expandButton);
        expandButton.click();
    }

    @Step("Collapse hidden items for field '{fieldName}' and type '{fieldType}'")
    public void clickOnCollapseHiddenItemsButton(String fieldName, String fieldType) {
        WebElement collapseButton = findElementWithWait(By.xpath(xpathCollapseHiddenItemsButton.replace("{fieldName}", fieldName).replace("{fieldType}", fieldType)));
        click(collapseButton);
    }

    @Step("Click the IP Catalog breadcrumb link")
    public IpCatalogPage clickOnIpCatalogBreadcrumb() {
        click(ipCatalogBreadcrumb);
        return new IpCatalogPage(driver);
    }

    @Step("Expand text for field '{fieldName}'")
    public void clickOnExpandTextButton(String fieldName) {
        WebElement expandTextButton = findElementWithWait(By.xpath(xpathExpandTextButton.replace("{fieldName}", fieldName)));
        click(expandTextButton);
    }

    @Step("Collapse text for field '{fieldName}'")
    public void clickOnCollapseTextButton(String fieldName) {
        WebElement collapseTextButton = findElementWithWait(By.xpath(xpathCollapseTextButton.replace("{fieldName}", fieldName)));
        click(collapseTextButton);
    }

    @Step("Click the Properties tab")
    public IpPropertiesTable clickOnPropertiesTab() {
        click(propertiesTab);
        return new IpPropertiesTable(driver);
    }

    @Step("Click the Info tab")
    public void clickOnIpInfoTab() {
        click(infoTab);
    }

    @Step("Click the Permissions tab")
    public PermissionsTabPage clickOnPermissionsTab() {
        click(permissionsTab);
        return new PermissionsTabPage(driver);
    }

    @Step("Hover over the CSV Export button")
    public void hoverOverExportButton() {
        hoverOverElement(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Export the table in CSV format")
    public void exportInCsvFormat() {
        click(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Click the Advanced Search breadcrumb link")
    public void clickOnAdvancedSearchBreadcrumb() {
        click(advSearchBreadcrumb);
    }

    @Step("Hover over the breadcrumbs menu button")
    public void hoverBreadcrumbsMenu() {
        hoverOverElement(breadcrumbsButton);
    }

    @Step("Click the breadcrumbs menu button")
    public void clickBreadcrumbsButton() {
        click(breadcrumbsButton);
    }

    @Step("Check if the breadcrumbs dropdown menu contains '{text}'")
    public boolean isBreadcrumbsDropdownMenuChoiceVisible(String text) {
        WebElement breadcrumb = findElementWithWait(By.xpath(breadcrumbDropdownMenu.replace("{text}", text)));
        return isElementVisible(breadcrumb);
    }

    @Step("Check if the Edit Latest IP button is disabled")
    public boolean isEditLatestIpButtonDisable() {
        DriverFactory.sleep(2000);//Need for improvement
        return isElementVisible(driver.findElement(By.xpath(disabledEditIpButton)));
    }

    @Step("Click a breadcrumb menu item with text '{text}'")
    public void clickBreadcrumb(String text) {
        WebElement breadcrumb = findElementWithWait(By.xpath(breadcrumbDropdownMenu.replace("{text}", text)));
        click(breadcrumb);
    }

    @Step("Collapse the left sidebar panel")
    public void clickOnCollapseLeftPanelButton() {
        click(collapseLeftPanel);
    }

    @Step("Check if the left sidebar panel is collapsed")
    public boolean isLeftPanelCollapsed() {
        return isElementVisible(collapsedLeftPanel);
    }

    @Step("Expand the left sidebar panel")
    public void clickOnExpandLeftPanelButton() {
        click(expandLeftPanel);
    }

    @Step("Check if the left sidebar panel is expanded")
    public boolean isLeftPanelExpanded() {
        return isElementVisible(expandedLeftPanel);
    }

    @Step("Check if the IP FQN '{libName}.{ipName}@{version}.{line}' is present in the header")
    public boolean isIpFqnPresent(String libName, String ipName, String version, String line) {
        String fqn = "{lib}.{ip}@{version}.{line}".replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line).replace("{version}", version);
        String ipv = wrapObjNameForXPath(fqn);
        WebElement ipHeader = waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(ipFqn.replace("{ipv}", ipv))));
        return isElementVisible(ipHeader);
    }

    @Step("Get the repository path value from the IP Details page")
    public String getRepoPath() {
        waitForElementToBeVisible(repoPathValue);
        return repoPathValue.getText().trim();
    }

    @Step("Click the library name link in the IP Details header")
    public LibraryDetailsPage clickOnLibraryName() {
        click(libraryName);
        return new LibraryDetailsPage(driver);
    }

    @Step("Check if the Info tab is active")
    public boolean isInfoTabActive() {
        return isElementVisible(infoTabActive);
    }

    @Step("Check if the Properties tab is active")
    public boolean isPropertiesTabActive() {
        return isElementVisible(propertiesTabActive);
    }

    @Step("Check if the Project Properties tab is active")
    public boolean isProjectPropertiesTabActive() {
        return isElementVisible(projectPropertiesTabActive);
    }

    @Step("Check if the Contents tab is active")
    public boolean isContentsTabActive() {
        return isElementVisible(contentsTabActive);
    }

    @Step("Check if the Hierarchy tab is active")
    public boolean isHierarchyTabActive() {
        return isElementVisible(hierarchyTabActive);
    }

    @Step("Check if the text row for field '{fieldName}' and value '{field}' is expanded")
    public boolean isTextRowExpanded(String fieldName, String field) {
        WebElement expanded = findElementWithWait(By.xpath(xpathExpandedTextRow.replace("{fieldName}", fieldName).replace("{field}", field)));
        return isElementVisible(expanded);
    }

    @Step("Check if the text row for field '{fieldName}' and value '{field}' is collapsed")
    public boolean isTextRowCollapsed(String fieldName, String field) {
        WebElement collapsed = findElementWithWait(By.xpath(xpathCollapsedTextRow.replace("{fieldName}", fieldName).replace("{field}", field)));
        return isElementVisible(collapsed);
    }

    @Step("Click the copy-to-clipboard button")
    public void clickOnCopyToClipboard() {
        click(copyToClipboardButton);
    }

    @Step("Hover over the copy-to-clipboard icon")
    public void hoverCopyToClipboardIcon() {
        hoverOverElement(copyToClipboardButton);
    }

    @Step("Click the Edit IP button and open the Edit IP page")
    public EditIpPage clickOnEditIpButton() {
        logger.info("Clicking on Edit IP button...");
        waitForPageLoaded();
        waitTillVisibleWithFluentWait(xpathEditButton);
        hoverOverElement(xpathEditButton);
        initWait().until(ExpectedConditions.invisibilityOf(pageOverlay));
        waitTillClickableWithFluentWait(xpathEditButton).click();
        waitForPageLoaded();
        return new EditIpPage(driver);
    }

    @Step("Wait for all loading spinners to disappear from the page")
    public void waitForDataToLoad() {
        List<WebElement> spinners = driver.findElements(By.xpath(loadingSpinner));
        if (spinners.size() > 0) {
            waitForNumberOfElementsToBe(By.xpath(loadingSpinner), 0);
        } else {
            logger.info("No loading spinner found.");
        }
    }

    @Step("Check if the Edit IP button is displayed")
    public boolean isEditIpButtonDisplayed() {
        return isElementVisible(editIpButton);
    }

    @Step("Check if the Copy IPV button is displayed")
    public boolean isCopyIpvButtonDisplayed() {
        return isElementVisible(copyIpvButton);
    }

    @Step("Open the Edit IP page in a new browser tab")
    public EditIpPage openEditIpInANewTab() throws AWTException {
        openNewTabBrowserContextMenu(waitForElementToBeClickable(xpathEditButton));
        switchToOpenedTab();
        return new EditIpPage(driver);
    }

    @Step("Click the Copy IPV button and open the Copy IP page")
    public CopyIpPage clickOnCopyIpvButton() {
        waitForPageLoaded();
        hoverOverElement(xpathEditButton);
        copyIpvButton.click();
        return new CopyIpPage(driver);
    }

    @Step("Click the Create IP Line button and open the Create Line page")
    public CreateLinePage clickOnCreateIpLineButton() {
        hoverOverElement(createLineButton);
        click(createLineButton);
        return new CreateLinePage(driver);
    }

    @Step("Check if the Create IP Line button is displayed")
    public boolean isCreateIpLineButtonDisplayed() {
        return isElementVisible(createLineButton);
    }

    @Step("Check if the tooltip with text '{text}' is present")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithFluentWait(By.xpath(xpathTooltip.replace("{text}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Open the IP Details page for IP ID '{ipId}'")
    public void goToIpPage(String ipId) {
        goTo(DriverFactory.getFullUrl("/#/ip/" + ipId));
        DriverFactory.sleep(3000); // we can remove if waitForPageLoaded() working for all test
        waitForPageLoaded();
    }

    @Step("Open the IPV Details page for IPV ID '{ipvId}'")
    public void openIpvDetailsPage(String ipvId) {
        goTo(DriverFactory.getFullUrl("/#/ipv/" + ipvId));
    }

    @Step("Get the full URL for IP identifier '{ipIdentifier}' and tab '{tabUrl}'")
    public String verifyUrl(String ipIdentifier, String tabUrl) {
        waitForPageLoaded();
        return DriverFactory.getFullUrl("/#/ip/" + ipIdentifier + tabUrl).trim();
    }

    @Step("Click the Contents tab")
    public void clickOnContentsSubTab() {
        click(contentsTab);
    }

    @Step("Click the Hierarchy tab")
    public void clickOnHierarchySubTab() {
        click(hierarchyTab);
    }

    @Step("Check if the IP icon for '{icon}' and '{libName}.{ipName}@{version}.{line}' is displayed")
    public boolean isIpIconDisplayed(String icon, String libName, String ipName, String version, String line) {
        WebElement ipHeader = findElementWithFluentWait(By.xpath(ipIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line).replace("{version}", version)));
        return isElementVisible(ipHeader);
    }

    @Step("Hover over the '{buttonName}' button if it is disabled")
    public void hoverOverCopyIpCreateLineButton(String buttonName) {
        WebElement disableButton = findElementWithWait(By.xpath(buttonIsDisabled.replace("{ButtonName}", buttonName)));
        waitForElementToBeVisible(disableButton);
        hoverOverElement(disableButton);
    }

    @Step("Check if the '{buttonName}' button is not disabled")
    public boolean isCopyIPCreateLineButtonNotDisabled(String ButtonName) {
        return isElementNotVisible(buttonIsDisabled.replace("{ButtonName}", ButtonName));
    }

    @Step("Check if the Edit Latest IP button is not disabled")
    public boolean isEditLatestIpButtonNotDisable() {
        return isElementNotVisible(disabledEditIpButton);
    }

    @Step("Hover over the Edit Latest IP button if it is disabled")
    public void hoverOverEditLatestIpButton() {
        WebElement disableEditIpButton = findElementWithWait(By.xpath(disabledEditIpButton));
        waitForElementToBeVisible(disableEditIpButton);
        hoverOverElement(disableEditIpButton);
    }

    @Step("Check if the Add to Shopping Cart button is available and enabled")
    public boolean isAddToShoppingCartButtonAvailable() {
        waitTillVisibleWithFluentWait(addToShoppingCartButton);
        if (addToShoppingCartButton.isDisplayed() && addToShoppingCartButton.isEnabled()) {
            logger.info("Add to Shopping Cart button is available.");
            return true;
        } else {
            logger.error("Add to Shopping Cart button is not available.");
            return false;}
    }

    @Step("Click the Add to Shopping Cart button")
    public void clickAddToShoppingCartButton() {click(addToShoppingCartButton);}

    @Step("Hover over the Add to Shopping Cart button and check for tooltip '{tooltipText}'")
    public boolean getAddToShoppingCartButtonTooltip(String tooltipText) {
        waitTillVisibleWithFluentWait(addToShoppingCartButton);
        hoverOverElement(addToShoppingCartButton);
        return isTooltipPresent(tooltipText);
    }

    @Step("Click the Add to Shopping Cart button (alias method)")
    public void clickOnAddToShoppingCartButton() {
        click(addToShoppingCartButton);
    }

    @Step("Get the tooltip text when hovering over the Add to Shopping Cart button")
    public String getAddToShoppingCartButtonTooltip() {
        DriverFactory.sleep(2500);//Todo: Need to improve in future
        return toolTipOnHoverOverElement(addToShoppingCartButton);
    }

    @Step("Check if there are no attributes present near the IP Dashboard tab")
    public Boolean noAttributesPresentNearIpDashboardTab() {
        return isElementNotVisible(numberOfAttributesOnIpDashboardCard);
    }

    @Step("Get the number of attributes on the IP Dashboard card")
    public String getNumberOfAttributesOnIpDashboardCard() {
        return numberOfAttributesOnIpDashboardCard.getText();
    }

    @Step("Get the number of attributes on the IPV Dashboard card")
    public String getNumberOfAttributesOnIpvDashboardCard() {
        return numberOfAttributesOnIpvDashboardCard.getText();
    }

    @Step("Get the number of attributes on the Line Dashboard card")
    public String getNumberOfAttributesOnIplDashboardCard() {
        return numberOfAttributesOnLineDashboardCard.getText();
    }

    @Step("Count the number of title dashboard widgets displayed")
    public int getNumberOfTitleDashboardWidgets() {
        return findElements(By.xpath(countTitleDashboardWidgets)).size();
    }

    @Step("Check if the dashboard widget for '{attributeType}' and '{attributeName}' is visible")
    public boolean isWidgetVisibleInDashboard(String attributeType, String attributeName) {
        WebElement attrName = findElementWithWait(By.xpath(xpathTitleDashboardWidgets.replace("{attributeType}", attributeType).replace("{name}", attributeName)));
        return isElementVisible(attrName);
    }

    @Step("Hover over the dashboard widget for '{attributeType}' and '{attributeName}'")
    public void hoverOverTitleDashboardWidget(String attributeType, String attributeName) {
        WebElement attrName = findElementWithWait(By.xpath(xpathTitleDashboardWidgets.replace("{attributeType}", attributeType).replace("{name}", attributeName)));
        hoverOverElement(attrName);
    }

    @Step("Get the 'aria-posinset' attribute value for the active Project Properties tab")
    public String getProjectPropertiesTabsPosition() {
        return getAttributeValue(projectPropertiesTabActive,"aria-posinset");
    }

    @Step("Click the Project Properties tab")
    public void clickOnProjectPropertiesTab() {
        click(projectPropertiesTab);
    }

@Step("Get text of Planned IP text tip")
public String getTextPlannedIpToolText() {
    waitForElementToBeVisible(PlannedIpText);
    return PlannedIpText.getText().trim();
    }

    @Step("Check if the tooltip with text '{text}' with datatestid is present")
    public boolean isTooltipPresentWithDataTestId(String text) {
        WebElement tooltip = findElementWithFluentWait(By.xpath(xpathTooltipTestId.replace("{text}", text)));
        return isElementVisible(tooltip);
    }

    public String getTabTextByIndexValue(int index) {
        String xpath = xpathForTabLocationWithIndex.replace("{index}", String.valueOf(index));
        WebElement tabElement = findElementWithWait(By.xpath(xpath));
        return tabElement.getText().trim();
    }
}
