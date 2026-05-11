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
import com.methodics.phi.pageobject.GridTablePage;
import com.methodics.phi.pageobject.login.SignInPage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static com.methodics.phi.actions.WebElementActions.PageModelName.none;
import static com.methodics.phi.util.CommonUrls.CREATE_IP_PAGE;
import static com.methodics.phi.util.Constants.ADMIN;
import static com.methodics.phi.util.Constants.NON_ADMIN_MDXUSER;

public class AddResourcesPage extends BasePage {

    private final WebDriver driver;

    public AddResourcesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@data-testid='modal-resources']//h5[normalize-space()='Add resources']")
    private WebElement resourceHeader;

    @FindBy(xpath = "//*[contains(@class,'tab-pane active')]//*[@data-testid='ps-selector-btn-icon']")
    private WebElement columnSwitcher;

    @FindBy(xpath = "//div[contains(@class,'app-modal-header modal-header')]//a")
    private WebElement infoIconButton;

    @FindBy(css = "[data-testid='resources-tab']")
    private WebElement resourceTab;

    @FindBy(xpath = "//h5[contains(@class,'tab-resources__overlay')][normalize-space()='Add your first resource']")
    private WebElement firstResourceMessage;

    @FindBy(css = "[data-testid='ui-btn-tab-resources-add-resource'], [data-testid='ui-btn-add-resources']")
    private WebElement addResourceButton;

    @FindBy(css = "[data-testid='shopping-cart-tab']")
    private WebElement shoppingCartTab;

    @FindBy(css = "[data-testid='resource-browser-tab']")
    private WebElement resourceBrowserTab;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-next']")
    private WebElement nextButton;

    @FindBy(xpath = "//button[normalize-space()='Back']")
    private WebElement backButton;

    @FindBy(xpath = "//h6[normalize-space()='Are you sure you want to add the below resources?']")
    private WebElement confirmationHeader;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-next'][disabled]")
    private WebElement disableNextButton;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-confirm']")
    private WebElement confirmButtonCheck;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-confirm'][disabled]")
    private WebElement disableConfirmButton;

    @FindBy(xpath = "//a[@data-testid='aliases-dropdown-tab']")
    private WebElement aliasesTab;

    @FindBy(xpath = "//div[@role='menuitemcheckbox']/span[normalize-space()='Tool panel']")
    private WebElement toolPanel;

    @FindBy(xpath = "//ul[@class='dropdown-menu show']//div[contains(@class,'active')]//input[@placeholder='Search IP Versions']")
    private WebElement searchVersion;

    @FindBy(xpath = "//i[contains(@class,'fa-solid fa-circle-xmark')]")
    private WebElement searchVersionTextCancel;

    @FindBy(xpath = "//input[@data-input='ips-add-resources']/following-sibling::i[contains(@class, 'base-input-field__clear-icon')]")
    private WebElement searchInputFieldIcon;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//button[contains(@class,'close')]/i")
    private WebElement closeIcon;

    @FindBy(xpath = "//*[contains(@class,'modal-footer')]//span[1]")
    private WebElement resourcesCountFooterLabel;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-confirm']")
    private WebElement confirmButton;

    @FindBy(xpath = "//div[@role='tabpanel' and contains(@class,'active')]//div[@data-testid='ui-col']")
    private WebElement noResourcesAddedMessage;

    @FindBy(css = "[data-testid='ui-btn-resources-modal-cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//input[@placeholder='Search IP Versions']")
    private WebElement searchIpVersionInput;

    @FindBy(xpath = "//label[normalize-space()='Aliased only']/parent::div[contains(@class,'custom-control custom-switch')]")
    private WebElement aliasControlButton;

    @FindBy(xpath = "//ul[@class='dropdown-menu show']//div[contains(@class,'active')]//li")
    private List<WebElement> searchVersionValueList;

    @FindBy(xpath = "//*[contains(@class,'no-rows-overlay')]//h5[normalize-space()='No results found']")
    private WebElement ipsNotFound;

    @FindBy(xpath = "//div[@col-id='description']")
    private WebElement description;

    @FindBy(xpath = "//button[contains(@class, 'csv-export')]")
    private WebElement csvExportButton;

    private final String ips = "(//*[@data-testid='modal-resources']//div[@col-id='name' or @col-id='fqn'][contains(@class, 'ag-cell-value')]//div[contains(@class,'ip-name-link-cell')])";
    private final String propSetCheckbox = "//*[@data-testid='modal-resources']//label[normalize-space()='{name}']";
    private final String rowById = "//div[@class='ag-pinned-left-cols-container']//div[@row-index=%s]";
    private final String ipCheckBoxCheck = "//span[normalize-space()='{name}']/ancestor::div[@role='row']//input[@type='checkbox']/..";
    private final String xpathIpIcon = "//div[@title='{name}']/ancestor::span[contains(@class, 'ip-name-link-cell')]/span[contains(@class,'{icon}')]";
    private final String columnName = "//div[@class='ag-header-cell-label']/span[normalize-space()='{name}']";
    private final String ipNameCheck = "//div[contains(@class,'ip-name-link-cell__link')]/span[normalize-space()='{name}']";
    private final String cellValue = "//div[@row-index='{index}']//div[@col-id='{cellId}' or @col-id='ip_properties.{cellId}']";
    private final String lineVersionSelection = "//ul//div[@role='tabpanel']//span[normalize-space(.)='{versionName}']";
    private final String aliasesSelection = "//div[@id='tabpanel-aliases-dropdown-tab']//span[normalize-space()='{name}']";
    private final String deleteIcon = "//span[text()='{ipName}']//ancestor::div[@col-id='name']//i[@title='Remove']";
    private final String ipSearchField = "//input[@placeholder='Search IPs' or @placeholder='Search IPVs' ]";
    private String ipIcon = "//span[contains(@class, '{icon}')]/following-sibling::div[@title='{lib}.{ip}@{version}.{line}']";

    private final String ips_rowID = "//*[@row-id='{name}']";
    private final String versionSelectedValue = ips_rowID + "//div[@col-id='versionCell']//span[normalize-space()='{version}']";
    private final String versionDropdownCheck = ips_rowID + "//ul[@class='dropdown-menu show']//a[normalize-space()='IP Versions']";
    private final String goToIpv = "//*[@title='{ipName}']/..//i[contains(@class,'redirect-icon')]";
    private final String searchVersionValue = "//li//span[contains(.,'{name}')]";
    private final String lineSelectedValue = "//ul[@data-testid='ui-dropdown-menu']//span[contains(@class,'flex-grow-1') and normalize-space()='{name}']";
    private final String lineSelectorDropdown = "//div[@row-id='{fqn}']//div[@col-id='selectLine']";
    private final String versionSelectorDropdown = "//div[@row-id='{name}']//div[@col-id='versionCell']";
    private final String stepNumber = "//div[contains(@class,'steps-container') and  normalize-space()='{name}']";
    private final String Ipdescription = ips_rowID + "//div[@col-id='description']";
    private final String showMoreButton = "//span[@class='ellipsis-content']";
    private final String descriptionTextPopUp = "//div[@class='formatted-text cell-text' and normalize-space()='{text}']";
    private final String descriptionIp = "//div[@col-id='description']//div[@role='label-name' and normalize-space()='{text}']";
    private final String xpathResources = "//*[@data-testid='modal-resources']//div[@class='ag-pinned-left-cols-container']//div[@role='row']";
    private final String aliasValidation = "//span[normalize-space()='{versionName}']//ancestor::button//span[@title='{alias}' and normalize-space()='{alias}']";
    private final String infoIcon = "//span[normalize-space()='{versionLine}']//parent::div[contains(@class,'row menu-item__top-row')]//i";
    private final String VersionValueInDropdown = "//div[contains(@class,'row menu-item__top-row')]/span[normalize-space()='{versionLine}']";
    private final String versionTooltippresent = "//header[normalize-space()='IP Versions with this alias']/parent::li/following-sibling::li[@class='menu-item']//span[normalize-space()='{versionLine}']//parent::div[1]//i[contains(@aria-describedby,'tooltip')]";
    private final String aliasLatestVersion = "//span[@class='flex-grow-1 text-truncate' and normalize-space()='{alias}']//following-sibling::span[normalize-space()='{version}']";
    private final String aliasIconClick = "//span[@class='flex-grow-1 text-truncate menu-item-text' and normalize-space()='{alias}']//ancestor::button[contains(@class,'dropdown-item menu')]//button[contains(@class,'btn-line-action-default-regular')]";
    private final String versionCountForAlias = "//span[contains(@class,'flex-grow-1') and normalize-space()='{alias}']//parent::div[contains(@class,'row menu-item__top-row')]//ul[@class='dropdown-menu show']/li[@class='menu-item']";
    private final String versionCheckForAlias = "//ul[@class='dropdown-menu show']/li[@class='menu-item']//span[normalize-space()='{versionLine}']";
    private final String versionCheckIcon = "//header[normalize-space()='IP Versions with this alias']//ancestor::ul[1]//span[normalize-space()='{versionLine}']//parent::div[contains(@class,'row menu-item__top-row')]//i";
    private final String IpCellValue = "//div[@row-id='{index}']//div[@col-id='{cellId}' or @col-id='ip_properties.{cellId}']";
    private final String ipNameByTitle = "//*[@title='{ipName}']";
    private final String ipNameValidation = ipNameByTitle + "/span[normalize-space()='{ipName}']";
    private final String GoToIpvName= "//*[@title='{ipName}']/i[contains(@class,'redirect-icon')]";

    @Step("Get number of IPs on Resources modal...")
    public int getNumberOfIps(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathResources), counter).size();
    }

    @Step("Enter text into the search IPs field...")
    public void enterIpNameToInputField(String ipName) {
        WebElement inputField = findElementWithWait(By.xpath(ipSearchField));
        clearInputField(inputField);
        enterTextSlowly(inputField, ipName);
        DriverFactory.sleep(3000);
    }

    @Step("Get IP icon color...")
    public String getIpIconColor(String ipName, String iconClass) {
        WebElement ipIcon = findElementWithWait(By.xpath(xpathIpIcon.replace("{name}", ipName).replace("{icon}", iconClass)));
        return ipIcon.getCssValue("color");
    }

    @Step("Verify No Resources Added Message is displayed...")
    public String isNoResourcesMsgPresent() {
        return waitForElementToBeVisible(noResourcesAddedMessage).getText();
    }

    @Step("Click on Cancel Button...")
    public void clickCancelOnModal() {
        click(cancelButton);
    }

    @Step("verify user navigate to Add resource screen ...")
    public void navigateToResourcesPage() throws InterruptedException {
        SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(ADMIN);
        CreateIpPage createIpPage = new CreateIpPage(DriverFactory.getBrowserInstance());
        createIpPage.goToCreateIpPage(DriverFactory.getFullUrl(CREATE_IP_PAGE));
        clickOnResourceTab();
        clickOnAddResourceButton();
    }

    @Step("verify Non Admin user navigate to Add resource screen ...")
    public void navigateToResourcesPageNonAdminUser() throws InterruptedException {
        SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(NON_ADMIN_MDXUSER);
        IpDetailsPage ipDetailsPage = new IpDetailsPage(DriverFactory.getBrowserInstance());
        ipDetailsPage.goToIpPage("tutorial.analog_top/edit?line=TRUNK");
        clickOnResourceTab();
    }

    @Step("verify Admin user navigate to Add resource screen using url ...")
    public void navigationToAddResourceUsingEditIp(String url) throws InterruptedException {
        SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(ADMIN);
        signInPage.goTo(DriverFactory.getFullUrl(url));
        clickOnResourceTab();
        clickOnAddResourceButton();
        DriverFactory.sleep(2000); //TODO: scope to improve
    }

    @Step("verify Admin user navigate to Add resource tab screen using url ...")
    public void navigationToResourceTabUsingEditIp(String url) throws InterruptedException {
        final SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(ADMIN);
        signInPage.goTo(DriverFactory.getFullUrl(url));
        clickOnResourceTab();
        DriverFactory.sleep(2000); //TODO: scope to improve
    }

    @Step("verify  user navigate to Add resource tab screen using url ...")
    public void navigationToResourceTabUsingEditIp(String user, String url) throws InterruptedException {
        final SignInPage signInPage = new SignInPage(DriverFactory.getBrowserInstance());
        signInPage.login(user);
        signInPage.goTo(DriverFactory.getFullUrl(url));
        waitForPageLoaded();
        clickOnResourceTab();
        DriverFactory.sleep(2000); //TODO: scope to improve
    }

    @Step("verify  user navigate to Add resource screen using url ...")
    public void navigationToAddResourceUsingEditIp(String user, String url) throws InterruptedException {
        navigationToResourceTabUsingEditIp(user, url);
        clickOnAddResourceButton();
        DriverFactory.sleep(2000); //TODO: scope to improve
    }

    @Step("verify  user navigate to Shopping Cart tab screen using url ...")
    public void navigationToShoppingCartTabUsingEditIp(String user, String url) throws InterruptedException {
        navigationToResourceTabUsingEditIp(user, url);
        clickOnAddResourceButton();
        clickOnShoppingCartTab();
        DriverFactory.sleep(2000); //TODO: scope to improve
    }

    @Step("Click on Shopping Cart Tab...")
    public void clickOnShoppingCartTab() {
        logger.info("Clicking on Shopping Cart Tab");
        DriverFactory.sleep(2000); //TODO: scope to improve
        waitTillVisibleWithFluentWait(shoppingCartTab);
        waitTillClickableWithFluentWait(shoppingCartTab);
        clickWithJS(shoppingCartTab);
        final GridTablePage gridTablePage = new GridTablePage(DriverFactory.getBrowserInstance());
        gridTablePage.waitForDataToLoad();
        waitForPageLoaded();
    }

    @Step("Is shopping cart tab enable.")
    public boolean isShoppingCartTabEnable() {
        return shoppingCartTab.getAttribute("class").contains("active");
    }

    @Step("is Resource tab enable.")
    public boolean isResourceBrowserTabEnable() {
        return resourceBrowserTab.getAttribute("class").contains("active");
    }

    @Step("click on Resources Browser...")
    public void clickOnResourceBrowserTab() {
        logger.info("Clicking on Resource Browser Tab");
        waitTillClickableWithFluentWait(resourceBrowserTab);
        hoverOverElement(resourceBrowserTab);
        clickWithJS(resourceBrowserTab);
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForLazyLoad() {
        return getNumberOfRowsForLazyLoad(ips, rowById, 10);
    }

    @Step("Remove IP name from the search field...")
    public void clickOnClearSearchFieldIcon() {
        WebElement searchField = findElementWithWait(By.xpath(ipSearchField));
        hoverOverElement(searchField);
        waitTillVisibleWithFluentWait(searchInputFieldIcon);
        click(searchInputFieldIcon);
    }

    @Step("Click to Info Icon button...")
    public void clickInfoIconButton() {
        click(infoIconButton);
    }

    @Step("Click on Resource Tab...")
    public void clickOnResourceTab() {
        logger.info("Clicking on Resource Tab");
        waitTillClickableWithFluentWait(resourceTab);
        hoverOverElement(resourceTab);
        waitTillClickableWithFluentWait(resourceTab);
        click(resourceTab);
        waitForPageLoaded();
    }

    @Step("Verify First Resources Added Message is displayed...")
    public boolean isFirstResourcesMsgPresent() {
        return isElementVisible(firstResourceMessage);
    }

    @Step("Click on Add Resource Button to add the resource...")
    public void clickOnAddResourceButton() {
        logger.info("Clicking on Add Resource Button");
        waitForPageLoaded();
        waitTillVisibleWithFluentWait(addResourceButton);
        waitTillClickableWithFluentWait(addResourceButton).click();
    }

    @Step("Check if next button is enable...")
    public boolean isNextButtonEnabled() {
        return isElementEnabled(nextButton);
    }

    @Step("Click on next button...")
    public void clickOnNextButton() {
        waitTillClickableWithFluentWait(nextButton);
        nextButton.click();
        DriverFactory.sleep(2000); // scope to improve
    }

    @Step("Click on Back button...")
    public void clickOnBackButton() {
        click(backButton);
    }

    @Step("Check if next button is disabled...")
    public boolean isConfirmationModeHeaderVisible() {
        return isElementVisible(confirmationHeader);
    }

    @Step("Click to IP Checkbox...")
    public void clickOnIpCheckBox(String ipName) {
        click(findElementWithWait(By.xpath(ipCheckBoxCheck.replace("{name}", ipName))));
    }

    @Step("Check if next button is enable...")
    public boolean isNextButtonDisable() {
        return isElementVisible(disableNextButton);
    }

    @Step("Verify Resource icon is present...")
    public boolean isIpIconPresent(String ipName, String icon) {
        WebElement ipIcon = findElementWithWait(By.xpath(xpathIpIcon.replace("{name}", ipName).replace("{icon}", icon)));
        return isElementVisible(ipIcon);
    }

    @Step("Verify IPs not found message is present...")
    public boolean ipsNotFoundMsgPresent() {
        return isElementVisible(ipsNotFound);
    }

    public boolean isAddResourceHeaderPresent() {
        return isElementVisible(resourceHeader);
    }

    @Step("Enter IP name into the search bar...")
    public void enterIpName(String ipName) {
        enterIpName(ipName, none);
    }

    @Step("Enter IP name into the search bar...")
    public void enterIpName(String ipName, PageModelName pageModelName) {
        final String element = (getDataTestId(pageModelName) + ipSearchField).trim();
        final WebElement e1 = findElementWithFluentWait(By.xpath(element));
        inputText(e1, ipName);
    }

    @Step("Get text from the search IPs input field...")
    public String getTextFromSearchIpsField() {
        return getTextFromSearchIpsField(none);
    }

    @Step("Get text from the search IPs input field...")
    public String getTextFromSearchIpsField(PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + ipSearchField;
        final WebElement e1 = findElementWithFluentWait(By.xpath(element));
        return getAttribute(e1);
    }

    @Step("Remove IP name from the search bar...")
    public void clearSearchBar(PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + ipSearchField;
        final WebElement e1 = findElementWithFluentWait(By.xpath(element));
        clearInputFieldWithBackspace(e1);
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSetName) {
        clickOnColumnsButton();
        clickOnSetCheckbox(propSetName);
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton() {
        click(columnSwitcher);
    }

    @Step("Select property set from the list...")
    public void clickOnSetCheckbox(String setName) {
        WebElement checkbox = findElementWithWait(By.xpath(propSetCheckbox.replace("{name}", setName)));
        click(checkbox);
    }

    @Step("Export IPs in CSV format...")
    public void exportInCsvFormat() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", csvExportButton);
    }

    @Step("Get number of IP checkbox...")
    public int getNumberOfIpCheckBox() {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".custom-checkbox"));
        return getNumberOfVisibleElements(checkboxes);
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameDisplay(String nameColumn) {
        return isElementVisible(columnName.replace("{name}", nameColumn));
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameNotDisplay(String nameColumn) {
        try {
            boolean flag = driver.findElement(By.xpath(columnName.replace("{name}", nameColumn))).isDisplayed();
            logger.info("Column name is displayed " + flag);
            return true;
        } catch (NoSuchElementException e) {
            logger.info("Column name is not displayed");
            return false;
        }
    }

    @Step("Verify IP name displaying correct...")
    public boolean isIpNameDisplayCorrect(String name) {
        WebElement ipNameCheckElement = findElementWithWait(By.xpath(ipNameCheck.replace("{name}", name)));
        return isElementVisible(ipNameCheckElement);
    }

    @Step("Verify column value displaying correct...")
    public String isValueDisplayCorrect(String rowId, String cellID) {
        return isValueDisplayCorrect(rowId, cellID, none);
    }

    @Step("Verify column value displaying correct...")
    public String isValueDisplayCorrect(String rowId, String cellID, PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + cellValue.replace("{index}", rowId).replace("{cellId}", cellID);
        final WebElement fieldValue = findElementWithFluentWait(By.xpath(element));
        scrollToElement(fieldValue);
        waitTillVisibleWithFluentWait(fieldValue);
        pressTab(fieldValue);
        return waitForElementToBeVisible(fieldValue).getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("Verify Description value displaying correct...")
    public boolean isDescriptionValueDisplayCorrect() {
        return isElementVisible(description);
    }

    @Step("Click on close icon...")
    public void clickOnCloseResource() {
        click(closeIcon);
    }

    @Step("Select version from dropdown...")
    public void selectVersionFromDropdown(String ipName, String versionName) {
        DriverFactory.sleep(1000);
        clickOnVersionDropdown(ipName);
        WebElement versionSelect = findElementWithWait(By.xpath(lineVersionSelection.replace("{versionName}", versionName)));
        click(versionSelect);
    }

    @Step("Click on version dropdown button...")
    public void clickOnVersionDropdown(String ipName) {
        WebElement versionDropdown = findElementWithWait(By.xpath(versionSelectorDropdown.replace("{name}", ipName)));
        doubleClickOnElement(versionDropdown);
        DriverFactory.sleep(1000);
    }

    @Step("Check if Confirm button is enable...")
    public boolean isConfirmEnabled() {
        return isElementEnabled(confirmButtonCheck);
    }

    @Step("Check if Confirm button is disable...")
    public boolean isConfirmButtonDisable() {
        return isElementVisible(disableConfirmButton);
    }

    @Step("Select Alias in Aliases tab under version selector dropdown...")
    public void selectAliasInAliasesTab(String ipName, String name) {
        DriverFactory.sleep(1000);
        clickOnVersionDropdown(ipName);
        waitForElementToBeClickable(aliasesTab).click();
        WebElement aliasSelect = findElementWithWait(By.xpath(aliasesSelection.replace("{name}", name)));
        click(aliasSelect);
    }

    @Step("Click to Confirm button...")
    public void clickConfirmButton() {
        waitForElementToBeClickable(confirmButtonCheck);
        click(confirmButtonCheck);
    }

    @Step("Click to Back button...")
    public void clickBackButton() {
        waitForElementToBeClickable(backButton);
        click(backButton);
    }

    @Step("Verify IP name is displayed...")
    public boolean isIpNameDisplayed(String name) {
        return isIpNameDisplayed(name, none);
    }

    @Step("Verify IP name is displayed...")
    public boolean isIpNameDisplayed(String name, PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + ipNameCheck.replace("{name}", name);
        final WebElement ipName = findElementWithFluentWait(By.xpath(element));
        return isElementVisible(ipName);
    }

    @Step("Verify that column name display...")
    public boolean isIpvNameNotDisplay(String name) {
        return isElementNotVisible(ipNameCheck.replace("{name}", name));
    }

    @Step("Click to Confirm button...")
    public void clickGoToIpvButton(String name) {
        hoverOverElement(findElementWithFluentWait(By.xpath(ipNameValidation.replace("{ipName}", name))));
        click(findElementWithWait(By.xpath(GoToIpvName.replace("{ipName}", name))));
    }

    @Step("Click to Confirm button...")
    public void clickGoToIpvHyperLink(String name) {
        hoverOverElement(findElementWithFluentWait(By.xpath(ipNameByTitle.replace("{ipName}", name))));
        click(findElementWithWait(By.xpath(goToIpv.replace("{ipName}", name))));
    }

    @Step("Select line from dropdown...")
    public void selectLineFromDropdown(String fqn, String name) {
        DriverFactory.sleep(1000);
        clickOnLineDropdown(fqn);
        WebElement lineSelect = findElementWithWait(By.xpath(lineSelectedValue.replace("{name}", name)));
        click(lineSelect);
    }

    @Step("Click on line dropdown button...")
    public void clickOnLineDropdown(String fqn) {
        WebElement lineDropdown = findElementWithFluentWait(By.xpath(lineSelectorDropdown.replace("{fqn}", fqn)));
        doubleClickOnElement(lineDropdown);
    }

    @Step("Click on Tool panel on IP name...")
    public void clickOnToolPanelFromIpName(String name) {
        WebElement ipName = findElementWithWait(By.xpath(ipNameCheck.replace("{name}", name)));
        performRightMouseClick(ipName);
        click(toolPanel);
    }

    @Step("Verify Version Value selected display is correct...")
    public boolean isVersionValueDisplayCorrect(String ipName, String value) {
        return isElementVisible(findElementWithWait(By.xpath(versionSelectedValue.replace("{name}", ipName).replace("{version}", value))));
    }

    @Step("Enter text into the search IPs field...")
    public void enterVersionValueToInputField(String version) {
        waitForElementToBeVisible(searchVersion);
        waitForElementToBeClickable(searchVersion);
        searchVersion.click();
        searchVersion.clear();
        searchVersion.sendKeys(version);
    }

    @Step("Verify Version Value selected display is correct...")
    public List<String> getListOfSearchVersion() {
        List<String> listVersion = new ArrayList<>();
        for (WebElement we : searchVersionValueList) {
            listVersion.add(we.getText().replace("\n", " ").replace("\r", " "));
        }
        logger.info("List of version found " + listVersion);
        return listVersion;
    }

    @Step("Click on Search Version value Remove button...")
    public void clickOnSearchVersionValueCancelButton() {
        click(searchVersionTextCancel);
    }

    @Step("Verify Version Value selected display is correct...")
    public void clickToSearchVersionValue(String name) {
        click(findElementWithWait(By.xpath(searchVersionValue.replace("{name}", name))));
    }

    @Step("Verify Versions value available for specific line...")
    public boolean isSearchVersionValueDisplaysForSpecificLine(String name) {
        return isElementVisible(findElementWithWait(By.xpath(lineVersionSelection.replace("{versionName}", name))));
    }

    @Step("Verify step number displaying correct...")
    public boolean isStepNumberDisplayCorrect(String name) {
        return isElementVisible(findElementWithWait(By.xpath(stepNumber.replace("{name}", name))));
    }

    @Step("Click on Show more button")
    public void clickOnShowMoreButton(String text) {
        hoverOverElement(findElementWithWait(By.xpath(descriptionIp.replace("{text}", text))));
        WebElement showMore = findElementWithWait(By.xpath(showMoreButton.replace("{text}", text)));
        click(showMore);
    }

    @Step("Verify Description value displaying correct in window pop...")
    public String isDescriptionValueDisplayCorrectInPopUP(String value) {
        WebElement fieldValue = findElementWithWait(By.xpath(descriptionTextPopUp.replace("{text}", value)));
        return getText(fieldValue).trim();
    }

    @Step("Hover over IP name display...")
    public void hoverOverShowMore(String fqn, String text) {
        hoverOverElement(findElementWithFluentWait(By.xpath(Ipdescription.replace("{name}", fqn))));
        WebElement showMore = findElementWithWait(By.xpath(showMoreButton.replace("{text}", text)));
        hoverOverElement(showMore);
    }

    @Step("get the number of resources from Footer label...")
    public String getNumberOfResourcesFromFooterLabel() {
        return getText(resourcesCountFooterLabel).trim();
    }

    @Step("Get number of resource ip present...")
    public int getResourceIpPresent() {
        return getNumberOfRowsForLazyLoad(ips, rowById, 10);
    }

    @Step("Click on Confirm button...")
    public void clickOnConfirmButton() {
        click(confirmButton);
        DriverFactory.sleep(1000);
    }

    @Step("Select Alias tab under version selector dropdown...")
    public void clickOnAliases(String id) {
        DriverFactory.sleep(1000);
        clickOnVersionDropdown(id);
        click(aliasesTab);
    }

    @Step("Verify Alias Value display is correct...")
    public boolean isAliasValueDisplayCorrect(String name) {
        return isElementVisible(findElementWithWait(By.xpath(aliasesSelection.replace("{name}", name))));
    }

    @Step("Verify alias displaying for specific version under version tab in version dropdown...")
    public boolean isAliasDisplayingCorrectForSpecificVersionUnderVersionDropdown(String name, String alias) {
        return isElementVisible(findElementWithWait(By.xpath(aliasValidation.replace("{versionName}", name).replace("{alias}", alias))));
    }

    @Step("Hover over IP name display...")
    public void hoverOverInfoIcon(String text) {
        WebElement infoIconButton = findElementWithWait(By.xpath(infoIcon.replace("{versionLine}", text)));
        hoverOverElement(infoIconButton);
    }

    @Step("Enter text into the search IPs field...")
    public void enterTextInputFieldInSearchAliasVersionMessage(String text) {
        clearInputField(searchIpVersionInput);
        inputText(searchIpVersionInput, text);
        DriverFactory.sleep(3000);
    }

    @Step("Verify alias not displaying for specific version under version tab in version dropdown...")
    public boolean isAliasNotDisplayingCorrectForSpecificVersionUnderVersionDropdown(String name, String alias) {
        return isElementNotVisible(aliasValidation.replace("{versionName}", name).replace("{alias}", alias));
    }

    @Step("Verify version value displaying for specific version message under version tab in version dropdown...")
    public boolean isVersionDisplayingCorrectForSpecificVersionUnderVersionDropdown(String name) {
        return isElementVisible(findElementWithWait(By.xpath(VersionValueInDropdown.replace("{versionLine}", name))));
    }

    @Step("Verify version value not displaying for specific version under version tab in version dropdown...")
    public boolean isVersionNotDisplayingCorrectForSpecificVersionUnderVersionDropdown(String name) {
        return isElementNotVisible(VersionValueInDropdown.replace("{versionLine}", name));
    }

    @Step("Click on Add Resource Button to add the resource...")
    public void clickOnAliasControlButton() {
        DriverFactory.sleep(1000);
        click(aliasControlButton);
    }

    @Step("Verify Version Value selected display is correct...")
    public boolean isAliasLatestVersionDisplayCorrect(String alias, String version) {
        return isElementVisible(findElementWithWait(By.xpath(aliasLatestVersion.replace("{alias}", alias).replace("{version}", version))));
    }

    @Step("Click on a alias Icon Click...")
    public void clickOnAliasIconClick(String alias) {
        WebElement aliasClickIcon = findElementWithWait(By.xpath(aliasIconClick.replace("{alias}", alias)));
        click(aliasClickIcon);
    }

    @Step("Get number of Versions for Alias...")
    public int getNumberOfIpVersionForAlias(String alias) {
        List<WebElement> VersionCount = findElements(By.xpath(versionCountForAlias.replace("{alias}", alias)));
        return getNumberOfVisibleElements(VersionCount);
    }

    @Step("Verify version line value displays Correct for specific Alias...")
    public boolean isVersionValueCheckForAlias(String versionLine) {
        WebElement versionIcon = findElementWithWait(By.xpath(versionCheckForAlias.replace("{versionLine}", versionLine)));
        return isElementVisible(versionIcon);
    }

    @Step("Verify version value not displaying for specific version under version tab in version dropdown...")
    public boolean isVersionNotDisplayForSpecificAlias(String versionLine) {
        return isElementNotVisible(versionCheckForAlias.replace("{versionLine}", versionLine));
    }

    @Step("Hover over IP name display...")
    public void hoverOverVersionInfoIcon(String text) {
        WebElement versionIconButton = findElementWithWait(By.xpath(versionCheckIcon.replace("{versionLine}", text)));
        hoverOverElement(versionIconButton);
    }

    @Step("is version tooltip present...")
    public boolean isVersionToolTipPresent(String text) {
        WebElement infoIconButton = findElementWithWait(By.xpath(versionTooltippresent.replace("{versionLine}", text)));
        return isElementVisible(infoIconButton);
    }

    @Step("Verify version dropdown is not display...")
    public boolean isVersionDropdownNotDisplay(String ipNameLine) {
        return isElementNotVisible(versionDropdownCheck.replace("{name}", ipNameLine));
    }

    @Step("Verify version value is not display...")
    public boolean isVersionDropdownValueNotDisplay(String ipNameLine, String versionValue) {
        return isElementNotVisible(versionSelectedValue.replace("{name}", ipNameLine).replace("{version}", versionValue));
    }

    @Step("Verify column value displaying correct...")
    public String isIpValueDisplayCorrect(String fqn, String cellID) {
        return isIpValueDisplayCorrect(fqn, cellID, none);
    }

    @Step("Verify column value displaying correct...")
    public String isIpValueDisplayCorrect(String fqn, String cellID, PageModelName pageModelName) {
        final String element = getDataTestId(pageModelName) + IpCellValue.replace("{index}", fqn).replace("{cellId}", cellID);
        final WebElement fieldValue = findElementWithWait(By.xpath(element));
        return getText(fieldValue);
    }

    @Step("Check if the IP icon for '{icon}' and '{libName}.{ipName}@{version}.{line}' is displayed")
    public boolean isIpIconDisplayed(String icon, String libName, String ipName, String version, String line) {
        WebElement ipHeader = findElementWithFluentWait(By.xpath(ipIcon.replace("{icon}", icon).replace("{lib}", libName)
                .replace("{ip}", ipName).replace("{line}", line).replace("{version}", version)));
        return isElementVisible(ipHeader);
    }
}
