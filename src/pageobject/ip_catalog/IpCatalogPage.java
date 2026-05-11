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
package com.methodics.phi.pageobject.ip_catalog;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.GridTablePage;
import com.methodics.phi.pageobject.ips.IpDetailsPage;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpCatalogPage extends BasePage {
    private final WebDriver driver;

    public IpCatalogPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@placeholder='Search IPs' and @data-input='ips-catalog']")
    private WebElement ipSearchField;

    @FindBy(css = "[data-testid='ui-input-group'] input[data-testid='ui-in-input-field']")
    private WebElement ipSearchFieldModal;

    @FindBy(xpath = "//i[contains(@class, 'fa-circle-xmark')]")
    private WebElement cancelSearchButton;

    @FindBy(xpath = "//button[contains(@class, 'csv-export')]")
    private WebElement csvExportButton;

    @FindBy(xpath = "//span[contains(@class, 'spinner')]")
    private WebElement downloadSpinner;

    @FindBy(xpath = "//div[@class='no-rows-overlay']/h5[normalize-space()='No IPs found']")
    private WebElement ipsNotFound;

    @FindBy(xpath = "//div[@class='no-rows-overlay']/div[contains(.,'Need help with search syntax? Take a look at the documentation')]")
    private WebElement luceneDocMessage;

    @FindBy(xpath = "//div[@class='no-rows-overlay']//a[@href='https://help.perforce.com/helix-iplm/public-latest/latest/Default.htm?cshid=Syntax']")
    private WebElement luceneDocLink;

    @FindBy(xpath = "//div[@data-testid='popover-body']/a")
    private WebElement learnMore;

    @FindBy(xpath = "//div[@data-testid='popover-target-wrapper']//i")
    private WebElement luceneDocIcon;

    @FindBy(xpath = "//div[@data-testid='popover-body']//p")
    private List<WebElement> luceneHelpHoverMenu;

    @FindBy(xpath = "//a[contains(@class, 'global-search-input__dropdown-alert')]//span[@class='alert__primary-message flex-grow-1']")
    private WebElement removeWildCardMessage;

    @FindBy(xpath = "//div[contains(@class,'invalid-feedback')]")
    private WebElement minLengthMessage;

    @FindBy(css = "[data-testid='ui-dropdown'] button[aria-expanded='true'] i")
    private WebElement closeDropdown;

    private String columnSwitcher = "//*[contains(@class,'column-switcher')][@data-testid='ui-dropdown']";
    private final String ipCatalogHeader = "//div[contains(@class, 'page-name')][normalize-space()='IP catalog']";
    private final String libHeader = "//div[contains(@class,'page-name')][normalize-space()='IP catalog \u00a0-\u00a0 {name}']";
    private final String csvTooltip = "//div[@class='tooltip-inner']";
    private final String propSetCheckbox = "//*[@data-testid='ps-{name}-checkbox-input']";
    private final String checkedPropSetCheckBox = "//*[@data-testid='ps-{name}-checkbox-input']//parent::div[contains(@class,'checkbox-normal_checked')]";
    private final String ip = "//div[@col-id='name']//*[@title={ipName}]";
    private final String ips = "(//div[@col-id='name' or @col-id='fqn'][contains(@class, 'ag-cell-value')])";
    private final String xpathIpIcon = "//*[@title='{name}']/ancestor::span[contains(@class, 'ip-catalog__ip-name')]/span[contains(@class,'{icon}')]";
    private final String ipPropValue = "//div[contains(.,'{ipName}')]/div[@col-id='{property}']";
    private final String propSuffix = "//span[contains(@class,'header') and normalize-space()='{name} ({suffix})']";
    private final String xpathTooltip = "//div[@class='tooltip-inner']";
    private final String ipCatalogResultsCount = "//span[contains(@class,'catalog__results-col__results-count')][normalize-space()='{ipsCount}']";
    private final String openSearchModeDropdown = "//button[contains(@class,'global-search-input__dropdown-toggle')]//span";
    private final String selectSearchModeName = "//span[@class='flex-grow-1 text-truncate menu-item-text'][normalize-space()='{searchModeName}']";
    private final String searchModeButtonTitleDisabled = "//button[contains(@class, 'global-search-input__dropdown-toggle_disable') and .//span[text()='{searchModeName}']]";
    private final String searchModeDropdownNamesDisabled = "//ul[@data-testid='ui-dropdown-menu']//li[@data-testid='ui-dropdown-item']//button[@data-testid='ui-btn-menu-item' and @disabled]//span[normalize-space()='{searchModeName}']";
    private final String tableRowIPName = "//*[contains(text(),'{ipName}')]//ancestor::div[contains(@class,'ip-catalog-ip-name-wrapper')]";
    private final String ipThreeDots = tableRowIPName + "//*[@data-testid='col-sub-menu-btn-container']";
    private final String addIpToShoppingCartButton = tableRowIPName + "//*[@data-testid='ui-btn-add-to-sc-btn']";
    private final String goToLibraryButton = tableRowIPName + "//span[normalize-space()='Go to Library']";
    private final String ipCatalogIpCount = "//div[@col-id='name' or @col-id='fqn' or @col-id='ag-Grid-AutoColumn' or @col-id='fqn.ip' or @col-id='ipv'][contains(@class, 'ag-cell-value')]//span[contains(@class,'ip-catalog')]//*[not(self::i)][@title]";
    private final String loadingIdicator = "//span[contains(@class,'ag-loading-text') and normalize-space()='Loading']";
    private final String loadingTextAboveGrid = "//span[contains(@class,'ip-catalog__results-col__results') and normalize-space()='{text}']";

    @Step("Navigate to the IP Catalog Page...")
    public void goToIpCatalogPage(String url) {
        goTo(url);
    }

    @Step("Verify IP Catalog sub-header is displayed...")
    public boolean isIpCatalogHeaderPresent() {
        waitForPageLoaded();
        final List<WebElement> test = findElements(By.xpath(ipCatalogHeader));
        return (test.size() == 1);
    }

    @Step("Get text from tooltip...")
    public String getTooltiptext() {
        final WebElement tooltipText = findElementWithWait(By.xpath(xpathTooltip));
        return getText(tooltipText);
    }

    @Step("Hover over search tooltip icon...")
    public void hoverOverSearchTooltipIcon() {
        hoverOverElement(luceneDocIcon);
    }

    @Step("Verify selected library is displayed in page header...")
    public boolean isSelectedLibraryHeaderPresent(String libName) {
        final WebElement header = findElementWithWait(By.xpath(libHeader.replace("{name}", libName)));
        return isElementVisible(header);
    }

    @Step("Enter IP name into the search bar...")
    public void enterIpName(String ipName) {
        waitTillVisibleWithFluentWait(ipSearchField);
        waitTillClickableWithFluentWait(ipSearchField);
        inputText(ipSearchField, ipName);
    }

    @Step("Click into the IP search bar...")
    public void clickIpSearchField() {
        click(ipSearchField);
    }

    @Step("Enter IP name into the search bar on modal...")
    public void enterIpNameOnModal(String ipName) {
        waitForElementToBeVisible(ipSearchFieldModal);
        inputText(ipSearchFieldModal, ipName);
    }

    @Step("Get text from the search IPs input field...")
    public String getTextFromSearchIpsField() {
        return getAttribute(ipSearchField);
    }

    @Step("Remove IP name from the search bar...")
    public void clearSearchBar() {
        clearInputFieldWithBackspace(ipSearchField);
    }

    @Step("Remove IP name from the search bar by clicking on cancel button...")
    public void clickOnClearSearchButton() {
        hoverOverElement(ipSearchField);
        click(cancelSearchButton);
    }

    @Step("Click on IP...")
    public IpDetailsPage selectIP(String ipName) {
        waitForPageLoaded();
        final String name = wrapObjNameForXPath(ipName);
        waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(ip.replace("{ipName}", name)))).click();
        waitForPageLoaded();
        return new IpDetailsPage(driver);
    }

    @Step("Get number of IPs...")
    public int getNumberOfIps(int counter) {
        waitForPageLoaded();
        return waitForNumberOfElementsToBe(By.xpath(ips), counter).size();
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForLazyLoad() {
        final GridTablePage gridTablePage = new GridTablePage(DriverFactory.getBrowserInstance());
        gridTablePage.waitForDataToLoad();
        return getNumberOfRowsForLazyLoad(ips, ips + "[11]");
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForSecondLazyLoad() {
        return getNumberOfRowsForLazyLoad(ips, ips + "[16]", ips + "[27]");
    }

    @Step("Verify IP icon is present...")
    public boolean isIpIconPresent(String ipName, String icon) {
        final WebElement ipIcon = findElementWithWait(By.xpath(xpathIpIcon.replace("{name}", ipName)
                .replace("{icon}", icon)));
        return isElementVisible(ipIcon);
    }

    @Step("Verify IPs not found message is present...")
    public boolean ipsNotFoundMssgPresent() {
        return isElementVisible(ipsNotFound);
    }

    @Step("Verify Lucene use documentation message is present...")
    public boolean luceneDocMssgPresent() {
        return isElementVisible(luceneDocMessage);
    }

    @Step("Verify Lucene use documentation link is correct...")
    public boolean luceneDocLinkIsCorrect() {
        return isElementVisible(luceneDocLink);
    }

    @Step("Verify Lucene use documentation link is correct...")
    public String clickLuceneDocLink() {
        click(luceneDocLink);
        switchToOpenedTab();
        return driver.getCurrentUrl();
    }

    @Step("Verify Lucene Learn more link is correct...")
    public String clickLearnMoreLink() {
        click(learnMore);
        switchToOpenedTab();
        return driver.getCurrentUrl();
    }

    @Step("Verify Lucene Learn more link is visible...")
    public boolean isLearnMoreLinkVisible() {
        return isElementVisible(learnMore);
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton() {
        clickOnColumnsButton(PageModelName.none);
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton(PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + columnSwitcher;
        findElementWithFluentWait(By.xpath(element)).click();
    }

    @Step("Select property set from the list...")
    public void clickOnSetCheckbox(String setName) {
        clickOnSetCheckbox(setName, PageModelName.none);
    }

    @Step("Select property set from the list...")
    public void clickOnSetCheckbox(String setName, PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + propSetCheckbox;
        logger.info("Selecting property set: " + element);
        final WebElement checkbox = findElementWithFluentWait(By.xpath(element.replace("{name}", setName)));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    @Step("unselect property set from the list...")
    public void clickOnUnCheckPropSet(String setName, PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + checkedPropSetCheckBox;
        logger.info("unSelecting property set: " + element);
        final WebElement checkbox = findElementWithFluentWait(By.xpath(element.replace("{name}", setName)));
        if (checkbox.isSelected() || checkbox.isDisplayed()) {
            checkbox.click();
        }
    }

    @Step("Get number of prop sets from the dropdown list...")
    public int getNumberOfPropertySets(String propSetName) {
        return driver.findElements(By.xpath(propSetCheckbox.replace("{name}", propSetName))).size();
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName) {
        selectPropertySet(propSetName, PageModelName.none);
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName, PageModelName pageModelName) {
        logger.info("Selecting property set: " + propSetName);
        clickOnColumnsButton(pageModelName);
        clickOnSetCheckbox(propSetName, pageModelName);
        waitForPageLoaded();
        DriverFactory.sleep(1000); // need to improve
        waitTillClickableWithFluentWait(closeDropdown).click();
    }

    @Step("unSelect property set {0}...")
    public void unSelectPropertySet(String propSetName, PageModelName pageModelName) {
        clickOnColumnsButton(pageModelName);
        clickOnUnCheckPropSet(propSetName, pageModelName);
        waitTillClickableWithFluentWait(closeDropdown).click();
        DriverFactory.sleep(3000); //wait required
    }

    @Step("Export IPs in CSV format...")
    public void exportInCsvFormat() {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", csvExportButton);
    }

    @Step("Verify download spinner is present...")
    public boolean isDownloadSpinnerPresent() {
        return isElementVisible(downloadSpinner);
    }

    @Step("Hover over CSV Export button...")
    public void hoverOverExportButton() {
        hoverOverElement(csvExportButton);
    }

    @Step("Get CSV export button tooltip...")
    public String getCsvExportTooltip() {
        return getText(findElementWithWait(By.xpath(csvTooltip)));
    }

    @Step("Get IP property value...")
    public String getPropertyValue(String ip, String propName) {
        final WebElement ipProp = waitForElement(By.xpath(ipPropValue.replace("{ipName}", ip)
                .replace("{property}", propName)));
        return getText(ipProp);
    }

    @Step("Verify property suffix is displayed in column header...")
    public boolean isPropertySuffixDisplayed(String name, String suffix) {
        final WebElement propertyColumnHeader = findElementWithWait(By.xpath(propSuffix.replace("{name}", name)
                .replace("{suffix}", suffix)));
        return isElementVisible(propertyColumnHeader);
    }

    @Step("Get minimum query length info message...")
    public String getQueryLengthInfoMsg() {
        return getText(minLengthMessage);
    }

    @Step("Get IP Catalog results count...")
    public boolean getIpCatalogResultsCount(String ipsCount) {
        final WebElement resultsCount = findElementWithWait(By.xpath(ipCatalogResultsCount.replace("{ipsCount}", ipsCount)));
        return isElementVisible(resultsCount);
    }

    @Step("Open search mode dropdown...")
    public void openSearchModeDropdown() {
        final WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        click(xpathSearchModeDropdown);
    }

    @Step("Hover over global search mode dropdown...")
    public void hoverOverSearchModesDropdownButton() {
        final WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        hoverOverElement(xpathSearchModeDropdown);
    }

    @Step("Click search mode dropdown...")
    public void clickSearchModeDropdown(String searchMode) {
        final WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        click(xpathSearchModeDropdown);
        final WebElement xpathSelectSearchModeName = findElementWithWait(By.xpath(selectSearchModeName
                .replace("{searchModeName}", searchMode)));
        click(xpathSelectSearchModeName);
    }

    @Step("Get selected search mode text...")
    public String getSelectedSearchMode() {
        final WebElement xpathSearchModeDropdown = findElementWithWait(By.xpath(openSearchModeDropdown));
        return getText(xpathSearchModeDropdown);
    }

    @Step("Getting search module help menu text")
    public String getSearchModeHelpMenuText(String searchModeName) {
        final List<WebElement> elements = waitForElementsToBeVisible(luceneHelpHoverMenu);
        for (final WebElement element : elements) {
            final String text = element.getText();
            if (text.startsWith(searchModeName + ":")) {
                return text;
            }
        }
        return "";
    }

    @Step("Getting Remove wildcards message")
    public String getSearchModeHelpMenuText() {
        return removeWildCardMessage.getText();
    }

    @Step("Verify search mode tile is disabled")
    public boolean isSearchModeDropdownButtonTitleDisabled(String searchMode) {
        final WebElement xpathSearchModeButtonTitleDisabled = findElementWithWait(By.xpath(searchModeButtonTitleDisabled
                .replace("{searchModeName}", searchMode)));
        return isElementVisible(xpathSearchModeButtonTitleDisabled);
    }

    @Step("Verify search mode dropdown list names are disabled")
    public boolean isSearchModeDropdownButtonListNamesDisabled(String searchMode) {
        final WebElement xpathSearchModeButtonTitleDisabled = findElementWithWait(By.xpath(searchModeDropdownNamesDisabled
                .replace("{searchModeName}", searchMode)));
        return isElementVisible(xpathSearchModeButtonTitleDisabled);
    }

    @Step("Verify search mode dropdown visible...")
    public boolean isSearchModeDropdownVisible(String searchMode) {
        final WebElement xpathSelectSearchModeName = findElementWithWait(By.xpath(selectSearchModeName
                .replace("{searchModeName}", searchMode)));
        return isElementVisible(xpathSelectSearchModeName);
    }

    @Step("Click on the three dots menu for IP: {ipName}")
    public void clickOnIpThreeDots(String ipName) {
        final String xpath = ipThreeDots.replace("{ipName}", ipName);
        final WebElement threeDotsElement = findElementWithFluentWait(By.xpath(xpath));
        click(threeDotsElement);
    }

    public void clickOnAddToShoppingCartButton(String ipName) {
        enterIpName(ipName);
        waitForPageLoaded();
        DriverFactory.sleep(1000);//scope to improvement - wait for the search display the IP in the list
        waitForIpCatalogDataToLoad();
        clickOnIpThreeDots(ipName);
        DriverFactory.sleep(500);//scope to improvement
        final WebElement addShoppingCartButton = findElementWithFluentWait(By.xpath(addIpToShoppingCartButton.replace("{ipName}", ipName)));
        click(addShoppingCartButton);
    }

    @Step("Click on the Go to Library button for IP: {ipName}")
    public void clickOnGoToLibraryButton(String ipName) {
        click(findElementWithFluentWait(By.xpath(goToLibraryButton.replace("{ipName}", ipName))));
        switchToOpenedTab();
    }

    @Step("Validate Go to Library button for IP dropdown option")
    public String validateGoToLibraryButtonValue(String ipName) {
        final WebElement goToLibraryButtonElement = findElementWithFluentWait(By.xpath(goToLibraryButton.replace("{ipName}", ipName)));
        return goToLibraryButtonElement.getText().trim();
    }

    @Step("Validate Add to Shopping Cart button value for IP: {ipName}")
    public String validateAddToShoppingCartButtonValue(String ipName) {
        final WebElement addToShoppingCartButtonElement = findElementWithFluentWait(By.xpath(addIpToShoppingCartButton.replace("{ipName}", ipName)));
        return addToShoppingCartButtonElement.getText().trim();
    }
    @Step("Get number of rows...")
    public int isNumberOfIpRowsCorrect(int counter) {
        DriverFactory.sleep(3000);// scope to improvement - wait for the search display the IP list taking long time in pipeline
        waitForIpCatalogDataToLoad();
        return waitForNumberOfElementsToBe(By.xpath(ipCatalogIpCount), counter).size();
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForIpCatalogDataToLoad() {
        try {
            waitForNumberOfElementsToBe(By.xpath(loadingIdicator), 0);
        } catch (Exception e) {
            logger.info("Loading spinner not found or already disappeared: ");
        }
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForIpCatalogDataToLoad(Duration timeout) {
        waitForNumberOfElementsToBe(By.xpath(loadingIdicator), 0, timeout);
    }

    @Step("Verify loading text displayed above grid...")
    public boolean isLoadingTextDisplayedAboveGrid(String text) {
        final WebElement loadingText = findElementWithFluentWait(By.xpath(loadingTextAboveGrid.replace("{text}", text)));
        return isElementVisible(loadingText);
    }

    @Step("Verify loading text displayed above grid...")
    public int isLoadingTextNotDisplayedAboveGrid(String text, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(loadingTextAboveGrid.replace("{text}", text)), counter).size();
    }

    public void waitForLoadingTextAboveGridToDisappear(String text, int timeout) {
        By locator = By.xpath(loadingTextAboveGrid.replace("{text}", text));
        WebElement locator1 = findElementWithFluentWait(By.xpath(loadingTextAboveGrid.replace("{text}", text)));
        try {waitForElementToBeVisible(locator1, timeout);
        } catch (Exception ignored) {
            logger.info("Element never appeared, proceed");
        }
        waitForNumberOfElementsToBe(locator, 0, Duration.ofSeconds(timeout));
    }
}
