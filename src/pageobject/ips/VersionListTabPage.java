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
package com.methodics.phi.pageobject.ips;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.login.SignInPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.List;

import static com.methodics.phi.util.IpTableColumnConstants.IP_VERSION_COL_ID;


public class VersionListTabPage extends BasePage {
    private WebDriver driver;

    @FindBy(xpath = "//div[@data-testid='version-list-filter-bar']//div[@class='dropdown btn-group column-switcher']")
    private WebElement columnSwitcher;

    @FindBy(css = "div[class*='column-switcher'] button[data-testid='ui-dropdown-btn-toggle']")
    private WebElement closeDropdown;

    @FindBy(xpath = "//div[@class='tab-header mb-4']//h3")
    private WebElement tabHeader;

    @FindBy(xpath = "//div[contains(@data-testid,'dialog-header')]")
    private WebElement openModelContentHeader;

    @FindBy(xpath = "//div[contains(@class,'modal-body')]")
    private WebElement modalContent;

    @FindBy(xpath = "//div[contains(@data-testid,'dialog-content')]")
    private WebElement openModelContent;

    @FindBy(xpath = "//button[contains(@data-testid,'dialog-close-button')]")
    private WebElement openModelContentCloseButton;

    @FindBy(xpath = "//div[@class = 'tooltip-inner']")
    private WebElement tooltipText;

    @FindBy(xpath = "//*[@data-testid='version-list-data-grid-wrapper']//div[contains(@class,'ag-body-viewport')]//div[@class='ag-pinned-left-cols-container']/div")
    private List<WebElement> itemsInVersionList;

    @FindBy(xpath = "//input[@placeholder='Search IPVs'][@data-testid='ui-in-input-field']//parent::div//i")
    private WebElement searchInputFieldIcon;

    @FindBy(xpath = "//div[@data-testid='version-list-master-wrapper']//input[@placeholder='Search IPVs']")
    private WebElement searchInputField;

    @FindBy(xpath = "//div[@data-testid='version-list-master-wrapper']//div[contains(@class, 'invalid-feedback_info')]")
    private WebElement invalidFeedbackForSearchInput;

    @FindBy(xpath = "//div[@data-testid='version-list-master-wrapper']//div[contains(@class, 'ag-overlay')]//h5[contains(@class, 'no-rows-overlay__text')]")
    private WebElement noRowsOverlayText;

    @FindBy(xpath = "//div[contains(@class,'version-list-container')]//span[@class='text-start selected-item']//parent::button[@data-testid='ui-dropdown-btn-toggle']")
    private WebElement searchModeDropdownBtn;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-end show']//button[@data-testid='ui-btn-menu-item']")
    private List<WebElement> searchModeOptions;

    @FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-end show']//i[@id='lucene-search-info-icon']")
    private WebElement infoIcon;

    @FindBy(xpath = "//*[(@role='tooltip' or contains(@class,'tooltip')) and not(contains(@style,'display: none'))]")
    private WebElement tooltipGeneric;

    private static final String TABLE_ID = "//*[@data-testid='version-list-data-grid-wrapper']";
    private final String ipvLink = TABLE_ID + "//a[.='{fqn}']";
    private final String propSetCheckbox = "//div[@data-testid='version-list-filter-bar']//label[normalize-space()='{name}']";
    private final String columnName = "//div[@class='ag-header-cell-label']/span[normalize-space()='{name}']";
    private String xpathColumn = "//div[@role='columnheader']";
    private String loadingIcon = "//div[contains(@class, 'ag-loading')]";
    private final String tableRow = TABLE_ID + "//div[@row-id='{IPV_name}']";
    private final String columnValue = tableRow + "//div[@col-id='{col_Id_name}']";
    private final String openTruncateModal = columnValue + "//span[contains(@data-testid,'dialog-open-modal-button')]";
    private String ipIcon = "//span[contains(@class, '{icon}')]/following-sibling::a//span[@title='{lib}.{ip}@{version}.{line}']";
    private final String showMoreButton = "//span[@class='ellipsis-content']";
    private final String hoverOverCell = "//*[@data-testid='version-list-data-grid-wrapper']//div[@col-id='{col_id}']//span[@title='{cell_value}']";
    private String ipvSubMenuToggle = "//div[@role='rowgroup']//div[@row-index={row_index}]//div[@data-testid='col-sub-menu-btn-container']/ancestor::div[contains(@role,'gridcell')]//button[contains(@class,'dropdown-toggle')]/i";
    private String ipvSUbMenuToggleByFqn = "//*[@data-testid='{fqn}' or @title='{fqn}' ]/following::div[@data-testid='ui-dropdown'][1]//button[@data-testid='ui-dropdown-btn-toggle']/i";
    private String ipvRowCount = "//div[@role='rowgroup']//div[@row-index]//div[@col-id='ip_version']";
    private String ipvSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private String ipvSubMenuItem = ipvSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";
    private String ipvSubMenuItemIcon = ipvSubMenuItem + "//i[contains(@class,'icon-20x16' )]";
    private String columnValueHavingAnchorTag = "//span[@title='{title}']/ancestor::div[@role='row']//div[@col-id='{column}']//a[@data-testid='ipv-name']//span";
    private String columnValueBasedOnTitleForBuiltInFields = "//div[@title='{title}']/ancestor::div[@role='row']//div[@col-id='{column}']//div";
    private String columnValueBasedOnTitleForPropertyFields = "//span[@title='{title}']/ancestor::div[@role='row']//div[@col-id='{column}']//span";
    private String columnValueBasedOnIndex = "//div[@row-index='{index}']//div[@col-id='{column}']//div[contains(@class, 'expandable-cell')]";
    private String xpathFirstRowPropertyCell = "//div[@row-index='0']/div[@col-id='{propName}']";
    private final String loadingIdicator = "//span[contains(@class,'ag-loading-text') and normalize-space()='Loading']";
    private String resizeColumnButton = "//div[@col-id='{name}']/div[contains(@class, 'resize')]";
    private String xpathColumnHeader = "//div[@role='columnheader' and @col-id='{name}']";
    private String expandButton = "//div[@col-id='{column}']//span[contains(@class,'expand-button')]";
    private String expandedRowContentCount = "//div[@col-id='{col_id}']//div[@data-testid='ui-row']//div[contains(@class, 'expandable-cell')][count(./*) = '{count}']";
    private final String cellValueOfASpecificRowIndex = "//div[@data-testid='version-list-data-grid-wrapper']//div[@row-index='{index}']//div[@col-id='{cellId}']";

    public VersionListTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Open IPV on same tab for IPV: {fullFqn}...")
    public void openIpvOnSameTab(String fullFqn) {
        WebElement ipvHyperlink = driver.findElement(By.xpath(ipvLink.replace("{fqn}", fullFqn)));
        ipvHyperlink.click();
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForDataToLoad() {
        waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0);
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName) {
        waitTillVisibleWithFluentWait(columnSwitcher).click();
        WebElement checkbox = findElementWithWait(By.xpath(propSetCheckbox.replace("{name}", propSetName)));
        click(checkbox);
        click(tabHeader); // to remove focus from the dropdown
    }

    @Step("unSelect property set {0}...")
    public void unSelectPropertySet(String propSetName) {
        waitTillVisibleWithFluentWait(columnSwitcher).click();
        final WebElement checkbox = findElementWithFluentWait(By.xpath(propSetCheckbox.replace("{name}", propSetName)));
        clickWithJS(checkbox);
        waitTillClickableWithFluentWait(closeDropdown).click();
        click(tabHeader); // to remove focus from the dropdown
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameDisplayed(String nameColumn) {
        return isElementVisible(columnName.replace("{name}", nameColumn));
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameNotDisplayed(String nameColumn) {
        try {
            boolean flag = driver.findElement(By.xpath(columnName.replace("{name}", nameColumn))).isDisplayed();
            logger.info("Column name is displayed " + flag);
            return true;
        } catch (NoSuchElementException e) {
            logger.info("Column name is not displayed");
            return false;
        }
    }

    @Step("Get number of columns...")
    public int getNumberOfColumns(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathColumn), counter).size();
    }

    @Step("Verify that column name is visible...")
    public boolean isColumnNameDisplay(String nameColumn) {
        return isElementVisible(columnName.replace("{name}", nameColumn));
    }

    @Step("Login and open version list of IP: {fullFqn}...")
    public void loginAndGoToVersionList(String user, String IP_FQN) throws InterruptedException {
        final SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(user);
        IpDetailsPage ipDetailsPage = new IpDetailsPage(DriverFactory.getBrowserInstance());
        ipDetailsPage.goToIpPage(IP_FQN);
        ipDetailsPage.waitForPageLoaded();
        ipDetailsPage.waitForDataToLoad();
        TabNavigationPanel tabNavigationPanel = new TabNavigationPanel(DriverFactory.getBrowserInstance());
        tabNavigationPanel.openVersionListTab();
    }

    @Step("Get cell value from table for IPV: {IPV_Name} and column: {colIdName}...")
    public String getCellValueFromTable(String colIdName, String IPV_Name, FiledRendererType filedType) {
        final String elementSelector = (filedType == null) ? columnValue : getFiledTypeSelector(columnValue, filedType);
        final WebElement ipvElement = findElementWithWait(By.xpath(elementSelector.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name)));
        logger.info("Actual IPV FQN text is displayed in Shopping cart: " + ipvElement.getText().trim());
        return waitForElementToBeVisible(ipvElement).getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("verify if truncated icon displayed on column hover ...")
    public boolean isTruncatedIconDisplayedOnColumnHover(String colIdName, String IPV_Name) {
        String colLocator = columnValue.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(colLocator));
        hoverOverElement(ipvElement);
        String modelLocator = openTruncateModal.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name);
        return isElementVisible(modelLocator);
    }

    //click on the open model
    @Step("Click on the open model for IPV: {IPV_Name} and column: {colIdName}...")
    public void clickOnColumnTruncatedOpenModel(String colIdName, String IPV_Name) {
        String colLocator = columnValue.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(colLocator));
        hoverOverElement(ipvElement);
        String modelLocator = openTruncateModal.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name);
        waitForElement(By.xpath(modelLocator)).click();
    }

    @Step("Get the header from modal...")
    public String getHeaderFromColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContentHeader);
        return openModelContentHeader.getText().trim();
    }

    @Step("Get the text from modal...")
    public String getTextFromColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContent);
        return openModelContent.getText().trim();
    }

    @Step("Close the modal...")
    public void closeColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContentCloseButton);
        openModelContentCloseButton.click();
    }

    @Step("Verify IP icon is displayed...")
    public boolean isIpIconDisplayed(String icon, String libName, String ipName, String version, String line) {
        WebElement ipHeader = findElementWithWait(By.xpath(ipIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line).replace("{version}", version)));
        return isElementVisible(ipHeader);
    }

    @Step("Get tooltip text...")
    public String getTooltipText() {
        return getText(tooltipText);
    }

    @Step("Hover over show more...")
    public void hoverOverShowMore(String colIdName, String IPV_Name, String text) {
        String colLocator = columnValue.replace("{col_Id_name}", colIdName).replace("{IPV_name}", IPV_Name);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(colLocator));
        hoverOverElement(ipvElement);
        WebElement showMore = findElementWithWait(By.xpath(showMoreButton.replace("{text}", text)));
        hoverOverElement(showMore);
    }

    @Step("Check for scroll bar on truncated modal...")
    public String verifyIfScrollBarIsPresentInTruncatedModal() {
        return modalContent.getCssValue("overflow-y");
    }

    @Step("is sub menu displayed for IPV...")
    public boolean isThreeDotForSubMenuDisplayedForIp(String rowIndex) {
        return isElementVisible(ipvSubMenuToggle.replace("{row_index}", rowIndex));
    }

    @Step("Get the number of row in version list grid...")
    public int getIpvRowCount() {
        return driver.findElements(By.xpath(ipvRowCount)).size();
    }

    @Step("Click on IPV sub menu...")
    public void clickOnThreeDotSubmenu(String rowIndex) {
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(ipvSubMenuToggle.replace("{row_index}", rowIndex)))).click();
    }

    @Step("is sub menu displayed for IPV...")
    public boolean isSubmenuOpen() {
        return isElementVisible(ipvSubMenu);
    }

    @Step("Close IPV sub menu if open...")
    public void closeIPVSubmenuIfOpen(String rowIndex) {
        if (isElementVisible(ipvSubMenu)) {
            clickOnThreeDotSubmenu(rowIndex);
        }
    }

    @Step("is IPV sub menu option displayed for IPV: {0}")
    public boolean isSubmenuOptionPresent(String menuItemName) {
        return isElementVisible(ipvSubMenuItem.replace("{menuOption}", menuItemName));
    }

    @Step("is IPV sub menu option icon displayed for IPV...")
    public boolean isSubmenuIconPresent(String menuItemName) {
        return isElementVisible(ipvSubMenuItemIcon.replace("{menuOption}", menuItemName));
    }

    @Step("Click on ipv sub menu for ipv: {0}")
    public void clickOnIpvSubmenuByIPV(String fqn) {
        final String locator = ipvSUbMenuToggleByFqn.replace("{fqn}", fqn);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator))).click();
    }

    @Step("Click on item from sub menu... {0}")
    public void clickOnIpvSubMenuItem(String menuItemName) {
        final WebElement menuItem = findElementWithFluentWait(By.xpath(ipvSubMenuItem.replace("{menuOption}", menuItemName)));
        click(menuItem);
    }

    @Step("Verify column value is present...")
    public String getColumnValueTextForBuiltInFields(String title, String column) {
        String xPath;
        if (column.equals(IP_VERSION_COL_ID)) {
            xPath = columnValueHavingAnchorTag;
        } else {
            xPath = columnValueBasedOnTitleForBuiltInFields;
        }
        WebElement columnValue = findElementWithWait(By.xpath(xPath
                .replace("{title}", title)
                .replace("{column}", column)));
        return columnValue.getText();
    }

    @Step("Verify column value is present...")
    public String getColumnValueTextForPropertyFields(String title, String column) {
        WebElement columnValue = findElementWithWait(By.xpath(columnValueBasedOnTitleForPropertyFields
                .replace("{title}", title)
                .replace("{column}", column)));
        return columnValue.getText();
    }

    @Step("Verify column value is empty...")
    public String checkForEmptyValue(int index, String column) {
        WebElement propValue = findElementWithWait(By.xpath(columnValueBasedOnIndex
                .replace("{index}", String.valueOf(index))
                .replace("{column}", column)));
        return propValue.getText();
    }

    @Step("Get {0} property value from the first table row...")
    public String getPropertyValue(String property) {
        WebElement propertyValue = findElementWithWait(By.xpath(xpathFirstRowPropertyCell.replace("{propName}", property)));
        return getText(propertyValue);
    }

    @Step("Verify loading spinner is hidden...")
    public void waitForListDataToLoad() {
        waitForNumberOfElementsToBe(By.xpath(loadingIdicator), 0);
    }

    @Step("Verify loading spinner is hidden. Custom timeout...")
    public void waitForListDataToLoad(Duration duration) {
        waitForNumberOfElementsToBe(By.xpath(loadingIdicator), 0, duration);
    }

    @Step("Verify resize button is displayed...")
    public boolean isResizeOptionDisplayed(String name) {
        WebElement resizeButton = findElementWithWait(By.xpath(resizeColumnButton.replace("{name}", name)));
        return isElementVisible(resizeButton);
    }

    @Step("Get column width...")
    public int getColumnWidth(String name) {
        WebElement header = driver.findElement(By.xpath(xpathColumnHeader.replace("{name}", name)));
        return header.getSize().getWidth();
    }

    @Step("Click on expand button based on column")
    public void clickOnExpandButtonBasedOnColumn(String column) {
        WebElement expand = findElementWithWait(By.xpath(expandButton.replace("{column}", column)));
        click(expand);
    }

    @Step("Verify number of visible values in one row..")
    public boolean verifyNumberOfVisibleElements(String column, int count) {
        return isElementVisible(expandedRowContentCount
                .replace("{col_id}", column)
                .replace("{count}", String.valueOf(count)));
    }

    @Step("Get cell value of a specific row index...{0}")
    public String getValueOfACellOfASpecificIndex(int rowId, String cellID) {
        final String element = cellValueOfASpecificRowIndex.replace("{index}", String.valueOf(rowId)).replace("{cellId}", cellID);
        final WebElement fieldValue = findElementWithFluentWait(By.xpath(element));
        scrollToElement(fieldValue);
        waitTillVisibleWithFluentWait(fieldValue);
        pressTab(fieldValue);
        return waitForElementToBeVisible(fieldValue).getText();
    }

    @Step("Get the number of elements present in version list...")
    public int getNumberOfRowsInVersionList() {
        waitForPageLoaded();
        return itemsInVersionList.size();
    }

    @Step("Clear the search field...")
    public void clickOnClearSearchFieldIcon() {
        WebElement searchInput = waitTillVisibleWithFluentWait(searchInputField);
        hoverOverElement(searchInput);
        waitTillVisibleWithFluentWait(searchInputFieldIcon);
        click(searchInputFieldIcon);
    }

    @Step("Enter text into the search bar...")
    public void setSearchInputFieldData(String searchInput) {
        final WebElement inputBox = waitTillVisibleWithFluentWait(searchInputField);
        inputText(inputBox, searchInput);
    }

    @Step("Verify invalid search feedback indicator...")
    public boolean verifyInvalidFeedbackForSearchInputIndicator() {
        return isElementVisible(invalidFeedbackForSearchInput);
    }

    @Step("Get invalid search input text...")
    public String getInvalidSearchFeedbackText() {
        WebElement invalidTextFinder = waitTillVisibleWithFluentWait(invalidFeedbackForSearchInput);
        return invalidTextFinder.getText();
    }

    @Step("Verify no rows overlay text is visible...")
    public boolean verifyNoRowsOverlayTextIsVisible() {
        return isElementVisible(noRowsOverlayText);
    }

    @Step("Open the Search Mode dropdown")
    public void openSearchModeDropdown() {
        click(waitTillVisibleWithFluentWait(searchModeDropdownBtn));
    }

    @Step("Select search mode: {modeName}")
    public void selectSearchMode(String modeName) {
        openSearchModeDropdown();
        for (WebElement opt : searchModeOptions) {
            if (opt.getText().trim().equalsIgnoreCase(modeName)) {
                click(opt);
                return;
            }
        }
        throw new NoSuchElementException("Search mode not found: " + modeName);
    }
}
