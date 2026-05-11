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
package com.methodics.phi.pageobject.ips;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

import static com.methodics.phi.util.CommonUrls.GLOBAL_SHOPPING_CART_URL;
import static com.methodics.phi.util.Constants.GO_TO_LIBRARY;

public class ShoppingCartPage extends BasePage {

    private WebDriver driver;

    @FindBy(xpath = "//span[@role='heading' and normalize-space()='Shopping cart']")
    private WebElement shoppingCartHeader;

    @FindBy(xpath = "//div[contains(@class,'ag-body-viewport')]//div[@data-ref='eContainer']/div")
    private List<WebElement> itemsInShoppingCart;

    @FindBy(xpath = "//*[@data-testid='page-header_badge']")
    private WebElement ipvCountIndicator;

    @FindBy(xpath = "//div[contains(@class,'version-toggle')]")
    private WebElement aliasesOnlyToggle;

    @FindBy(xpath = "//div[contains(@data-testid,'dialog-content')]")
    private WebElement openModelContent;

    @FindBy(xpath = "//div[contains(@data-testid,'dialog-header')]")
    private WebElement openModelContentHeader;

    @FindBy(xpath = "//div[@data-testid='add-different-version-modal']")
    private WebElement addDifferentVersionModal;

    @FindBy(xpath = "//div[@data-testid='add-different-version-modal']//h5")
    private WebElement addDifferentVersionModalHeader;

    @FindBy(xpath = "//*[@data-testid='version-input']//input")
    private WebElement addDifferentVersionModalVersionDropdown;

    @FindBy(xpath = "//*[@data-testid='line-input-dropdown']//input[@placeholder='Select IP Line']")
    private WebElement addDifferentVersionModalLineDropdown;

    @FindBy(xpath = "//*[@data-testid='add-different-version-modal']//button[normalize-space()='Cancel']")
    private WebElement addDifferentVersionModalCancelButton;

    @FindBy(xpath = "//*[@data-testid='add-different-version-modal']//button[normalize-space()='Add IPV']")
    private WebElement addDifferentVersionModalAddIPVButton;

    @FindBy(xpath = "//button[contains(@data-testid,'dialog-close-button')]")
    private WebElement openModelContentCloseButton;

    @FindBy(xpath = "//span[normalize-space()='Tool panel']")
    private WebElement toolPanel;

    @FindBy(xpath = "//*[@data-testid='shopping-cart-tab']")
    private WebElement resourceShoppingCart;

    @FindBy(xpath = "//a[@target='_blank' and contains(text(),'Manage IPVs in Shopping cart')]")
    private WebElement manageIPVsInShoppingCartLink;

    @FindBy(css = "[data-testid='shopping-cart-container'] div[class*='center-cols-container'] > div")
    private List<WebElement> itemsInResourceShoppingCart;

    @FindBy(xpath = "//div[@row-index='0']//div[contains(@class,'ag-cell-value ag-cell ag-cell-not-inline-editing') and @aria-colindex='1']")
    private WebElement firstRowIPVName;

    @FindBy(xpath = "//div[@role='tooltip']//div[@class='tooltip-inner']")
    private WebElement tooltip;

    @FindBy(xpath = "//div[contains(@class,'line-dropdown')]//ul[@class='dropdown-menu show']//input")
    private WebElement lineDropdownSearch;

    @FindBy(xpath = "//*[@data-testid='version-search-input']//input")
    private WebElement versionDropdownSearch;

    @FindBy(xpath = "//*[@data-testid='tabpanel-aliases-dropdown-tab']//input")
    private WebElement aliasesVersionDropdownSearch;

    @FindBy(xpath = "//i[contains(@class,'clear')]")
    private WebElement clearSearchIcon;

    @FindBy(xpath = "//*[normalize-space(text())='No results found']")
    private WebElement noResultsFound;

    @FindBy(css = "button[data-testid='ui-btn-ui-modal-remove']")
    private WebElement removeButtonFromRemoveIPVConfirmationPopUp;

    String NoIPVAddedWrapper = "//*[@data-testid='empty-shopping-cart-wrapper']";
    String NoIPVAddedImage = NoIPVAddedWrapper + "//i[contains(@class,'icon-100x80')]";
    String NoIPVHeaderText = NoIPVAddedWrapper + "//h5";
    String NoIPVSubText = "//*[@data-testid='no-data-description']";
    private String getTableRow = "//*[@data-testid='shopping-cart-grid']//div[@row-id='{IPV_Name}']";
    private String getColumnValue = getTableRow + "//div[@col-id='{col_Id_name}']";
    private String truncateOpenModel = getColumnValue + "//span[contains(@data-testid,'dialog-open-modal-button')]";
    private String getThreeDotToggle = "//*[@row-index='{row-index}']/div[contains(@role,'gridcell')]//button[contains(@class,'dropdown-toggle')]/i";
    private String getThreeDotToggleByFqn = "//*[@data-testid='{fqn}' or @title='{fqn}' ]/following::div[@data-testid='ui-dropdown']//button[contains(@class,'dropdown-toggle')]/i";
    private String threeDotSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private String threeDotSubMenuItems = threeDotSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";
    private String threeDotSubMenuIcon = threeDotSubMenuItems + "//i[contains(@class,'icon-20x16' )]";
    private String DropdownOption = "(//ul[@class='dropdown-menu show']//span[normalize-space()='{lineVersion}'])[last()]";
    private String selectVersionTab = "//li[contains(@class, 'nav-item') and normalize-space()='{tabName}']";
    private String infoIconForLineInDropdownOption = DropdownOption + "/following-sibling::span/i[@data-testid='line-info-icon']";
    private String infoIconForVersionInDropdownOption = DropdownOption + "/following-sibling::span/i[@data-testid='version-info-icon']";

    private String getRowByIndex = " //*[@data-testid='shopping-cart-grid']//div[@row-index='{rowIndex}']";
    private String getColumnValueByIndex = getRowByIndex + "//span[contains(@class,'cell-text')]";
    private String getDropdownIconByIndex = getRowByIndex + "//*[contains(@class,'drag-n-drop-icon')]";
    private String versionColumnHeader = "//*[@role='columnheader' and @col-id='{colId}']";
    private String versionsButtonLocatorTemplate = "/..//*[@data-testid='ui-btn-alias-item-versions-button']";
    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Verify shopping cart Header display...")
    public boolean isShoppingCartHeaderDisplayed() {
        return shoppingCartHeader.isDisplayed();
    }

    @Step("Verify number of items in shopping cart and IPV/IPVs text in indicator display...")
    public boolean verifyShoppingCartIndicator() {
        waitForPageLoaded();
        int ipvCounts = itemsInShoppingCart.size();
        if (ipvCounts==0 && ipvCountIndicator.getText().equals("No IPVs")) {
            logger.info("No items in shopping cart and displaying correct text in indicator");
            return true;
        }
        int count = Integer.parseInt(ipvCountIndicator.getText().replaceAll("[^0-9]", "").trim());
        String textWithoutNumbers = ipvCountIndicator.getText().replaceAll("[0-9]", "").trim();

        if (ipvCounts!=count) {
            logger.error("Number of items in shopping cart: " + ipvCounts + " not equals to Indicator showing: " + count);
            return false;
        }
        if (count > 1 && "IPVs".equals(textWithoutNumbers)) {
            logger.info("Number of items in shopping cart: " + ipvCounts + " equals to Indicator showing: " + count);
            logger.info("IPVs text is displaying in indicator correct");
            return true;
        } else if (count==1 && "IPV".equals(textWithoutNumbers)) {
            logger.info("Number of items in shopping cart: " + ipvCounts + " equals to Indicator showing: " + count);
            logger.info("IPV text is displaying in indicator correct");
            return true;
        } else {
            logger.info("Text is displaying in Shopping cart indicator: " + textWithoutNumbers);
            return false;
        }
    }

    @Step("Verify No IPV added image should display for empty cart...")
    public boolean isNoIpAddImageVisible() {
        return isElementVisible(NoIPVAddedImage);
    }

    @Step("Verify No IPV added header text should display for empty cart...")
    public String getNoIPVHeaderText() {
        return getText(findElementWithFluentWait(By.xpath(NoIPVHeaderText)));
    }

    @Step("Verify No IPV added sub text should display for empty cart...")
    public String getNoIpAddSubTextVisible() {
        return getText(findElementWithFluentWait(By.xpath(NoIPVSubText))).trim();
    }

    @Step("Get cell value from table for IPV: {IPV_Name} and column: {colIdName}...")
    public String getCellValueFromTable(String colIdName, String IPV_Name, FiledRendererType filedType) {
        waitForPageLoaded();
        final ShoppingCartResourcesTable shoppingCartResourcesTable = new ShoppingCartResourcesTable(driver);
        shoppingCartResourcesTable.isColumnPresent(colIdName, GLOBAL_SHOPPING_CART_URL);
        final String elementSelector = (filedType==null) ? getColumnValue:getFiledTypeSelector(getColumnValue, filedType);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(elementSelector.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name)));
        logger.info("Actual IPV FQN text is displayed in Shopping cart: " + ipvElement.getText().trim());
        return waitTillVisibleWithFluentWait(ipvElement).getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("Get cell value from table for IPV Name by index: {rowIndex}")
    public String getCellValueFromTableByIndex(String rowIndex) {
        waitForPageLoaded();
        final String elementSelector = getColumnValueByIndex.replace("{rowIndex}", rowIndex);
        final WebElement ipvElement = findElementWithWait(By.xpath(elementSelector));
        logger.info("Actual IPV FQN text is displayed in Shopping cart: " + ipvElement.getText().trim());
        return waitTillVisibleWithFluentWait(ipvElement).getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("is IPV present in shopping cart: {IPV_Name}")
    public boolean isIpvPresentInShoppingCart(String colIdName, String IPV_Name, FiledRendererType filedType) {
        waitForPageLoaded();
        ShoppingCartResourcesTable shoppingCartResourcesTable = new ShoppingCartResourcesTable(driver);
        shoppingCartResourcesTable.isColumnPresent(colIdName, GLOBAL_SHOPPING_CART_URL);
        final String elementSelector = getFiledTypeSelector(getColumnValue, filedType);
        return isElementVisible(elementSelector.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name));
    }

    @Step("is Truncated icon displayed in column hover ...")
    public boolean isTruncatedIconDisplayedInColumnHover(String colIdName, String IPV_Name) {
        String colLocator = getColumnValue.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(colLocator));
        hoverOverElement(ipvElement);
        String modelLocator = truncateOpenModel.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name);
        return isElementVisible(modelLocator);
    }

    //click on the open model
    @Step("Click on the open model for IPV: {IPV_Name} and column: {colIdName}...")
    public void clickOnColumnTruncatedOpenModel(String colIdName, String IPV_Name) {
        String colLocator = getColumnValue.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name);
        final WebElement ipvElement = findElementWithFluentWait(By.xpath(colLocator));
        hoverOverElement(ipvElement);
        String modelLocator = truncateOpenModel.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name);
        waitForElement(By.xpath(modelLocator)).click();
    }

    @Step("Get Header open model ")
    public String getHeaderFromColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContentHeader);
        return openModelContentHeader.getText().trim();
    }

    @Step("Get the text from open model ")
    public String getTextFromColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContent);
        return openModelContent.getText().trim();
    }

    @Step("Close the open model ")
    public void closeColumnTruncatedOpenModel() {
        waitTillClickableWithFluentWait(openModelContentCloseButton);
        openModelContentCloseButton.click();
    }

    @Step(" IPV not available in Shopping cart: {IPV_Name}")
    public boolean ipvNotAvailableInShoppingCart(String colIdName, String IPV_Name, FiledRendererType filedType) {
        waitForPageLoaded();
        String elementSelector = getFiledTypeSelector(getColumnValue, filedType);
        return isElementNotVisible(elementSelector.replace("{col_Id_name}", colIdName).replace("{IPV_Name}", IPV_Name));
    }

    @Step("Get the number of IPV count from the IPV indicator")
    public int getIpvCountFromIndicator() {
        waitForPageLoaded();
        String countText = ipvCountIndicator.getText().replaceAll("[^0-9]", "").trim();
        return Integer.parseInt(countText);
    }

    @Step("Click on Tool panel on IP name...")
    public void clickOnToolPanelFromIpvName() {
        performRightMouseClick(firstRowIPVName);
        click(toolPanel);
    }

    @Step("Get the number of elements present in itemsInShoppingCart")
    public int getNumberOfItemsInShoppingCart(String url) {
        waitForPageLoaded();
        DriverFactory.sleep(1000);//need to improvement
        if (url.equals(GLOBAL_SHOPPING_CART_URL)) return itemsInShoppingCart.size();
        else return itemsInResourceShoppingCart.size();

    }

    @Step("Click on the resource shopping cart")
    public void clickOnResourceShoppingCart() {
        resourceShoppingCart.click();
    }

    @Step("is Manage IPVs in Shopping cart link enabled...")
    public boolean isManageIPVsInShoppingCartLinkEnabled() {
        waitForPageLoaded();
        return waitForElementToBeVisible(manageIPVsInShoppingCartLink).isEnabled();
    }

    @Step("Click on Manage IPVs in Shopping cart link...")
    public void clickOnManageIPVsInShoppingCartLink() {
        waitForPageLoaded();
        hoverOverElement(manageIPVsInShoppingCartLink);
        manageIPVsInShoppingCartLink.click();
    }

    @Step("Verify Manage IPVs in Shopping cart link is redirected to help page...")
    public boolean isManageIPVsInShoppingCartLinkRedirectedToHelpPage(String url) {
        waitForPageLoaded();
        final String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/#/shoppingcart")) {
            logger.info("Manage IPVs in Shopping cart link is redirected to help page");
            switchToOpenedTab();
            return waitForUrlToLoad(url, 40);
        } else {
            logger.error("Manage IPVs in Shopping cart link is not redirected to help page");
            return false;
        }
    }

    @Step("Click on the Go to Library button for IP: {fqn}")
    public void clickOnGoToLibraryButton(String fqn) {
        clickOnThreeDotSubmenuByIPV(fqn);
        clickOnThreeDotSubMenuItem(GO_TO_LIBRARY);
        switchToOpenedTab();
    }

    @Step("is three dot sub menu displayed for IP")
    public boolean isThreeDotForSubMenuDisplayedForIp(String rowIndex) {
        return isElementVisible(getThreeDotToggle.replace("{row-index}", rowIndex));
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenu(String rowIndex) {
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(getThreeDotToggle.replace("{row-index}", rowIndex)))).click();
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenuByIPV(String fqn) {
        final String locator = getThreeDotToggleByFqn.replace("{fqn}", fqn);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator))).click();
    }

    @Step("Close three dot sub menu option if open")
    public void closeThreeDotSubmenuIfOpen(String rowIndex) {
        if (isElementVisible(threeDotSubMenu)) {
            clickOnThreeDotSubmenu(rowIndex);
        }
    }

    @Step("is three dot sub menu displayed for IP")
    public boolean isSubmenuOpen() {
        return isElementVisible(threeDotSubMenu);
    }

    @Step("is three dot sub menu option displayed for IP")
    public boolean isSubmenuPresent(String menuItemName) {
        return isElementVisible(threeDotSubMenuItems.replace("{menuOption}", menuItemName));
    }

    @Step("is three dot sub menu option icon displayed for IP")
    public boolean isSubmenuIconPresent(String menuItemName) {
        return isElementVisible(threeDotSubMenuIcon.replace("{menuOption}", menuItemName));
    }

    @Step("Click on item from three dot sub menu")
    public void clickOnThreeDotSubMenuItem(String menuItemName) {
        final WebElement menuItem = findElementWithFluentWait(By.xpath(threeDotSubMenuItems.replace("{menuOption}", menuItemName)));
        click(menuItem);
    }

    @Step("Click on Remove button from Remove IPV confirmarion pop up")
    public void clickOnRemoveButtonFromRemoveIPVConfirmationPopUp() {
        waitForElementToBeClickable(removeButtonFromRemoveIPVConfirmationPopUp).click();
    }

    @Step("is Add Different Version Modal visible")
    public boolean isAddDifferentVersionModalVisible() {
        return isElementVisible(addDifferentVersionModal);
    }

    @Step("Get Add Different Version Modal Header")
    public String getAddDifferentVersionModalHeader() {
        return getText(addDifferentVersionModalHeader);
    }

    @Step("is Add Different Version Modal Version Dropdown visible")
    public boolean isAddDifferentVersionModalVersionDropdownVisible() {
        return isElementVisible(addDifferentVersionModalVersionDropdown);
    }

    @Step("is Add Different Version Modal Line Dropdown visible")
    public boolean isAddDifferentVersionModalLineDropdownVisible() {
        return isElementVisible(addDifferentVersionModalLineDropdown);
    }

    @Step("Get Selected Line in Add Different Version Modal")
    public String getSelectedLineInAddDifferentVersionModal() {
        waitTillVisibleWithFluentWait(addDifferentVersionModalLineDropdown);
        return addDifferentVersionModalLineDropdown.getAttribute("value").trim();
    }

    @Step("Click on Add Different Version Modal Line Dropdown")
    public void clickOnAddDifferentVersionModalLineDropdown() {
        waitForPageLoaded();
        waitForElementToBeClickable(addDifferentVersionModalLineDropdown).click();
        waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(DropdownOption.replace("{lineVersion}", "TRUNK"))));
    }

    @Step("Click on Add Different Version Modal Version Dropdown")
    public void clickOnAddDifferentVersionModalVersionDropdown() {
        waitForElementToBeClickable(addDifferentVersionModalVersionDropdown).click();
        waitTillVisibleWithFluentWait(versionDropdownSearch);
    }

    @Step("is Line Present in Dropdown in Add Different Version Modal")
    public boolean isLinePresentInDropdownInAddDifferentVersionModal(String line) {
        return isElementVisible(DropdownOption.replace("{lineVersion}", line));
    }

    @Step("is Latest Version Present with Line in Line Dropdown in Add Different Version Modal")
    public boolean isLatestVersionPresentWithLineInLineDropdown(String line, String version) {
        return isElementVisible((DropdownOption + " /following-sibling::span[normalize-space(.)='{version}']").replace("{lineVersion}", line).replace("{version}", version));
    }

    @Step("is info Icon Present With Line in Line Dropdown")
    public boolean isInfoIconPresentWithLineInLineDropdown(String line) {
        return isElementVisible(infoIconForLineInDropdownOption.replace("{lineVersion}", line));
    }

    @Step("Hover On Info Icon With Line in Line Dropdown")
    public void hoverOnInfoIconWithLineInLineDropdown(String expectedLine) {
        final String locator = infoIconForLineInDropdownOption.replace("{lineVersion}", expectedLine);
        hoverOverElement(waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator))));
        DriverFactory.sleep(500); //need to improve
    }

    @Step("Hover On Info Icon With version in version Dropdown")
    public void hoverOnInfoIconWithVersionInVersionDropdown(String expectedVersion) {
        final String locator = infoIconForVersionInDropdownOption.replace("{lineVersion}", expectedVersion);
        final WebElement element = scrollToElement(findElementWithFluentWait(By.xpath(locator)));
        DriverFactory.sleep(1000);
        hoverOverElement(waitTillVisibleWithFluentWait(element));
        DriverFactory.sleep(500);
    }

    @Step("Get Info Icon Details in Line Dropdown")
    public String getInfoIconDetailsInLineDropdown() {
        waitTillVisibleWithFluentWait(tooltip);
        return tooltip.getText().trim();
    }

    @Step("is Version Present in Dropdown in Add Different Version Modal")
    public boolean isVersionPresentInDropdownInAddDifferentVersionModal(String version) {
        return isElementVisible(DropdownOption.replace("{lineVersion}", version));
    }

    @Step("is No Result Found text present in Add Different Version Modal")
    public boolean isNoResultFoundTextPresentInAddDifferentVersionModal() {
        return isElementVisible(noResultsFound);
    }

    @Step("is Cancel Button Present in Add Different Version Modal")
    public boolean isCancelButtonPresentInAddDifferentVersionModal() {
        return isElementVisible(addDifferentVersionModalCancelButton);
    }

    @Step("is Add IPV Button Present in Add Different Version Modal")
    public boolean isAddIPVButtonPresentInAddDifferentVersionModal() {
        return isElementVisible(addDifferentVersionModalAddIPVButton);
    }

    @Step("Click on Add Different Version Modal Cancel Button")
    public void clickOnAddDifferentVersionModalCancelButton() {
        click(addDifferentVersionModalCancelButton);
    }

    @Step("Select Line and version from Add Different Version Modal ")
    public void selectLineAndVersionFromAddDifferentVersionModal(String line, String version) {
        clickOnAddDifferentVersionModalLineDropdown();
        final String lineLocator = DropdownOption.replace("{lineVersion}", line);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(lineLocator))).click();
        clickOnAddDifferentVersionModalVersionDropdown();
        final String versionLocator = DropdownOption.replace("{lineVersion}", version);
        WebElement element = findElementWithFluentWait(By.xpath(versionLocator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        DriverFactory.sleep(1000);
    }

    @Step("Select Line and aliases version from Add Different Version Modal ")
    public void selectLineAndAliasesVersionFromAddDifferentVersionModal(String line, String aliases) {
        clickOnAddDifferentVersionModalLineDropdown();
        final String lineLocator = DropdownOption.replace("{lineVersion}", line);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(lineLocator))).click();
        clickOnAddDifferentVersionModalVersionDropdown();
        clickOnTabForVersionSelect("Aliases");
        final String versionLocator = DropdownOption.replace("{lineVersion}", aliases);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(versionLocator))).click();
        DriverFactory.sleep(1000);
    }

    @Step("Click on Add Different Version Modal Add Button")
    public void clickOnAddDifferentVersionModalAddIPVButton() {
        click(addDifferentVersionModalAddIPVButton);
    }

    @Step("Close dropdown if open")
    public void closeDropdownIfOpen() {
        click(addDifferentVersionModalHeader);
    }

    @Step("is Add IPV Button Enabled")
    public boolean isAddIPVButtonEnabled() {
        return addDifferentVersionModalAddIPVButton.isEnabled();
    }

    @Step("Select Version in Add Different Version Modal")
    public void selectVersionInAddDifferentVersionModal(String version) {
        clickOnAddDifferentVersionModalVersionDropdown();
        final String versionLocator = DropdownOption.replace("{lineVersion}", version);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(versionLocator))).click();
    }

    @Step("Enable Aliases Only Toggle")
    public void enableAliasesOnlyToggle() {
        waitForElementToBeVisible(aliasesOnlyToggle);
        if (!aliasesOnlyToggle.isSelected()) {
            aliasesOnlyToggle.click();
            DriverFactory.sleep(1000);
        }
    }

    @Step("Disable Aliases Only Toggle")
    public void disableAliasesOnlyToggle() {
        waitTillClickableWithFluentWait(aliasesOnlyToggle);
        aliasesOnlyToggle.click();
        DriverFactory.sleep(1000);
    }

    @Step("Click on Tab for Version Select")
    public void clickOnTabForVersionSelect(String aliases) {
        final String tabLocator = selectVersionTab.replace("{tabName}", aliases);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(tabLocator))).click();
        DriverFactory.sleep(2000);
    }

    @Step("is Search Field Present in Line Dropdown")
    public boolean isSearchFieldPresentInLineDropdown() {
        return isElementVisible(lineDropdownSearch);
    }

    @Step("Type Line in Search Field in Line Dropdown")
    public void typeLineInSearchPresentInLineDropdown(String line) {
        waitTillClickableWithFluentWait(lineDropdownSearch);
        lineDropdownSearch.clear();
        enterTextSlowly(lineDropdownSearch, line);
        DriverFactory.sleep(1000);
    }

    @Step("is version Search Field Present in Line Dropdown")
    public boolean isVersionSearchFieldPresentInVersionDropdown() {
        return isElementVisible(versionDropdownSearch);
    }

    @Step("Get Selected Line from line Search Input in Add Different Version Modal")
    public String getSelectedLineFromInputAddDifferentVersionModal() {
        waitTillVisibleWithFluentWait(addDifferentVersionModalLineDropdown);
        return addDifferentVersionModalLineDropdown.getAttribute("value").trim();
    }

    @Step("Get Selected Version from version Search Input in Add Different Version Modal")
    public String getSelectedVersionFromInputAddDifferentVersionModal() {
        waitTillVisibleWithFluentWait(addDifferentVersionModalVersionDropdown);
        return addDifferentVersionModalVersionDropdown.getAttribute("value").trim();
    }

    @Step("Type Version in Search Field in Version Dropdown")
    public void typeVersionInSearchPresentInVersionDropdown(String version) {
        waitTillClickableWithFluentWait(waitForElementToBeVisible(versionDropdownSearch));
        versionDropdownSearch.clear();
        enterTextSlowly(versionDropdownSearch, version);
        DriverFactory.sleep(1000);
    }

    @Step("Type aliases in Search Field in Version Dropdown")
    public void typeAliasesInSearchPresentInVersionDropdown(String aliases) {
        waitTillClickableWithFluentWait(waitForElementToBeVisible(aliasesVersionDropdownSearch));
        aliasesVersionDropdownSearch.clear();
        enterTextSlowly(aliasesVersionDropdownSearch, aliases);
        DriverFactory.sleep(1000);
    }

    @Step("Click on Clear in Search Field in Version Dropdown")
    public void clickOnClearInSearchPresentInAliasesVersionDropdown() {
        aliasesVersionDropdownSearch.click();
        waitTillVisibleWithFluentWait(clearSearchIcon).click();
        waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(DropdownOption.replace("{lineVersion}", "HEAD"))));
    }

    @Step("Click on Clear in Search Field in Line Dropdown")
    public void clickOnClearInSearchPresentInLineDropdown() {
        lineDropdownSearch.click();
        waitTillVisibleWithFluentWait(clearSearchIcon).click();
        waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(DropdownOption.replace("{lineVersion}", "TRUNK"))));
    }

    @Step("is Version Disable In Dropdown In Add Different Version Modal")
    public boolean isVersionDisableInDropdownInAddDifferentVersionModal(String version) {
        final String versionLocator = DropdownOption.replace("{lineVersion}", version);
        return !findElementWithFluentWait(By.xpath(versionLocator + "/../../../button")).isEnabled();
    }

    public void clickAliasVersionsListIconInAddDifferentVersionModal(String aliasName) {
        final String versionLocator = DropdownOption.replace("{lineVersion}", aliasName);
        final String versionsButtonLocator = versionLocator + versionsButtonLocatorTemplate;
        WebElement versionsButton = findElementWithFluentWait(By.xpath(versionsButtonLocator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", versionsButton);
    }

    @Step("Drag and drop ipv by index ")
    public void dragAndDropIPVByIndex(String indexToMove, String indexPositionToMove) {
        final WebElement source = findElementWithFluentWait(By.xpath(getDropdownIconByIndex.replace("{rowIndex}", indexToMove)));
        final WebElement target = findElementWithFluentWait(By.xpath(getRowByIndex.replace("{rowIndex}", indexPositionToMove)));
        dragAndDropElement(source, target);
        DriverFactory.sleep(2000);
    }

    @Step("click on Column Header")
    public void clickOnColumnHeader(String ipVersionColId, String columnOrder) {
        final String locator = versionColumnHeader.replace("{colId}", ipVersionColId);
        WebElement element = waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(locator)));
        element.click();
        DriverFactory.sleep(2000);
        Assert.assertTrue(element.getAttribute("aria-sort").equals(columnOrder));
    }
}
