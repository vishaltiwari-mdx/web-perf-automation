/**
 * ################################################################################
 * # Copyright (c) 2010-2021 Methodics, Inc.
 * # All Rights Reserved.
 * #
 * # This source file is confidential and the proprietary information of
 * # Methodics, Inc. and its receipt or possession does not convey any rights to
 * # reproduce or disclose its contents, or to manufacture, use or sell anything
 * # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 * # specific written authorization is strictly forbidden.
 * ################################################################################
 */
package com.methodics.phi.pageobject.queries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.List;


public class AdvancedSearchPage extends BasePage {
    private WebDriver driver;

    public AdvancedSearchPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class,'page-name')][normalize-space()='Advanced search']")
    private WebElement advancedSearchSubHeader;

    @FindBy(xpath = "//textarea[@id='textarea-auto-height']")
    private WebElement queryInputField;

    @FindBy(xpath = "//a[@href='https://help.perforce.com/helix-iplm/public-latest/latest/Default.htm?cshid=Querying_IPs_in_the_System']")
    private WebElement queryHelpButton;

    @FindBy(xpath = "//label[@for='includeAllVersions']")
    private WebElement includeAllIpvsToggle;

    @FindBy(xpath = "//button[contains(@class, 'dashboard')]/i[contains(@class, 'fa-gauge-high')]")
    private WebElement attributesButton;

    @FindBy(xpath = "//i[contains(@class, 'table')]")
    private WebElement ipvsButton;

    @FindBy(xpath = "//div[contains(@class,'query-results') and contains(@style,'display: none')]")
    private List<WebElement> ipvsDataTable;
//	private String ipvsDataTable = "//div[contains(@class,'query-results')]";

    @FindBy(xpath = "//div[@class='align-self-center mx-auto' and contains(.,'Select a Query or create one.')]")
    private WebElement selectQueryMessage;

    private String xpathQueryHeader = "//*[contains(@class, 'app-subheader')]//*[@title='{title}']";
    private String previewButton = "//button[contains(., 'Preview')]";
    private String csvExportButton = "//button[contains(@class, 'csv-export-button')]";
    private String columnsButton = "//div[contains(@class,'column-switcher')]";
    private String propSetCheckbox = "//label[contains(., '{propSet}')]";
    private String xpathTooltip = "//div[@class='tooltip-inner' and text()='{tooltip}']";
    private String noIpsFoundMessage = "//div[@class='text-center text-body-secondary' and contains(.,'No IPs returned by selected Query.')]";
    private String rowIpv = "//div[@title='{fqn}']/span";
    private String rowGoToIP = rowIpv + "/following-sibling::i";
    private String rowIpvRow = "(//div[@col-id='fqn'][@role='gridcell']//div[@title]/span)";
    private String rowIpvByRowIndexGoToIP = rowIpvRow + "[{rowIndex}]/following-sibling::i";
    private String getThreeDotToggle = rowIpvRow + "[{row-index}]/ancestor::div[contains(@role,'gridcell')]//button[contains(@class,'dropdown-toggle')]/i";
    private String getThreeDotToggleByFqn = "(//*[@data-testid='{fqn}']/following::button[contains(@class,'dropdown-toggle')]/i)[1]";
    private String threeDotSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private String threeDotSubMenuItems = threeDotSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";
    private String threeDotSubMenuIcon = threeDotSubMenuItems + "//i[contains(@class,'icon-20x16' )]";

    @Step("Go to Advanced Search page...")
    public void goToAdvancedSearchPage(String url) {
        goTo(url);
    }

    @Step("Verify Advanced Search sub-header...")
    public boolean isAdvancedSearchSubHeaderPresent() {
        return isElementVisible(advancedSearchSubHeader);
    }

    @Step("Verify sub-header for selected query: {0}...")
    public boolean isSelectedQuerySubHeaderPresent(String query) {
        List<WebElement> queryHeader = driver.findElements(By.xpath(xpathQueryHeader.replace("{title}", query)));
        return getNumberOfVisibleElements(queryHeader) == 1;
    }

    @Step("Enter query: {0}, for the method: {method}...")
    public void enterSearchQuery(String query) {
        inputText(queryInputField, query);
    }

    @Step("Hit Enter key...")
    public void pressEnterKey() {
        pressEnterKey(queryInputField);
    }

    @Step("Remove query expression from the input field...")
    public void removeQueryExpressionFromInputField() {
        clearInputFieldWithBackspace(queryInputField);
    }

    @Step("Get query expression from the input field...")
    public String getQueryExpression() {
        return getAttribute(queryInputField);
    }

    @Step("Verify query input field is empty...")
    public boolean isQueryInputFieldEmpty() {
        return getQueryExpression().equals("");
    }

    @Step("Verify query expression field is enabled...")
    public boolean isQueryExpressionFieldEnabled() {
        return isElementEnabled(queryInputField);
    }

    @Step("Verify query expression field is disabled...")
    public boolean isQueryExpressionFieldDisabled() {
        return !isElementEnabled(queryInputField);
    }

    @Step("Click on Preview button...")
    public void clickOnPreviewButton() {
        click(findElementWithWait(By.xpath(previewButton)));
    }

    @Step("Verify Preview Button is enabled...")
    public boolean isPreviewButtonEnabled() {
        return isElementEnabled(findElementWithWait(By.xpath(previewButton)));
    }

    @Step("Verify Preview Button is disabled...")
    public boolean isPreviewButtonDisabled() {
        return !isElementEnabled(findElementWithWait(By.xpath(previewButton)));
    }

    @Step("Verify Preview Button is not displayed...")
    public int isPreviewButtonNotDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(previewButton), counter).size();
    }

    @Step("Click on Query Help Button...")
    public void clickOnQueryHelpButton() {
        click(queryHelpButton);
    }

    @Step("Enable ALL IPVs option...")
    public void сlickOnAllIpvsToggleButton() {
        // leave sleeps for stable regression
        DriverFactory.sleep(1000);
        waitForElementToBeClickable(includeAllIpvsToggle);
        click(includeAllIpvsToggle);
        DriverFactory.sleep(1000);
    }

    @Step("Click on Columns button...")
    public void clickOnColumnsButton() {
        click(findElementWithWait(By.xpath(columnsButton)));
    }

    @Step("Select property set {0}...")
    public void selectPropertySet(String propSet) {
        clickOnColumnsButton();
        WebElement checkbox = findElementWithWait(By.xpath(propSetCheckbox.replace("{propSet}", propSet)));
        checkbox.click();
        clickOnColumnsButton();
        DriverFactory.sleep(1000);
    }

    @Step("Verify Columns selector is not displayed...")
    public boolean isColumnsSelectorNotDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(columnsButton))) == 0;
    }

    @Step("Verify that property set is not displayed...")
    public boolean propertySetNotDisplayed(String propSetName, int counter) {
        return findElementsWithWait(By.xpath(propSetCheckbox.replace("{propSetName}", propSetName)), counter).size() == 0;
    }

    @Step("Export in csv format...")
    public void exportInCsvFormat() {
        click(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Hover over CSV Export button...")
    public void hoverOverExportButton() {
        hoverOverElement(driver.findElement(By.xpath(csvExportButton)));
    }

    @Step("Hover over CSV Export button...")
    public void hoverOverPreviewButton() {
        hoverOverElement(driver.findElement(By.xpath(previewButton)));
    }

    @Step("Verify CSV Export button is not displayed...")
    public int isCsvExportButtonNotDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(csvExportButton), counter).size();
    }

    @Step("Open Attributes tab...")
    public void openAttributesTab() {
        click(attributesButton);
    }

    @Step("Hover over Attributes button...")
    public void hoverOverAttributesButton() {
        // need to keep this sleep otherwise hovering happening too fast
        DriverFactory.sleep(1000);
        hoverOverElement(attributesButton);
    }

    @Step("Open IPVs tab...")
    public void openIpvsTab() {
        click(ipvsButton);
    }

    @Step("Hover over IPVs button...")
    public void hoverOverIpvsButton() {
        // need to keep this sleep otherwise hovering happening too fast
        DriverFactory.sleep(1000);
        hoverOverElement(ipvsButton);
    }

    @Step("Verify button tooltip...")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip.replace("{tooltip}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Verify IPVs data table is not displayed...")
    public boolean isIpvListNotDisplayed() {
        return getNumberOfVisibleElements(ipvsDataTable) == 0;
    }

    @Step("Verify 'No IPs returned by selected query.' message...")
    public boolean isNoIpsReturnedByQueryMssgPresent() {
       waitForSpinnersToDisappear(Duration.ofSeconds(3)); // Need to improve in future
        List<WebElement> noIpsMessage = driver.findElements(By.xpath(noIpsFoundMessage));
        return getNumberOfVisibleElements(noIpsMessage) == 1;
    }

    @Step("Verify 'Select a Query or Create one' text is present...")
    public boolean isSelectQueryMessageDisplayed() {
        return isElementVisible(selectQueryMessage);
    }

    @Step("Click on 'Go to IP' button...")
    public void clickOnGoToIP(String fqn) {
        hoverOverElement(findElementWithFluentWait(By.xpath(rowIpv.replace("{fqn}", fqn))));
        WebElement goToIpButton = findElementWithFluentWait(By.xpath(rowGoToIP.replace("{fqn}", fqn)));
        click(goToIpButton);
    }

    @Step("Verify 'Go to IP' icon is present for the row {i}...")
    public boolean isGoToIpIconPresent(String i) {
        hoverOverElement(findElementWithFluentWait(By.xpath(rowIpvRow + "[" + i + "]")));
        return isElementVisible(rowIpvByRowIndexGoToIP.replace("{rowIndex}", i));
    }

    @Step("get the row count of IPVs...")
    public int getIpvRowCount() {
        int rowCount = driver.findElements(By.xpath(rowIpvRow)).size();
        logger.info(rowCount + " rows found");
        return rowCount;
    }

    @Step("is three dot sub menu displayed for IP")
    public boolean isThreeDotForSubMenuDisplayedForIp(String rowIndex) {
        return isElementVisible(getThreeDotToggle.replace("{row-index}", rowIndex));
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenu(String rowIndex) {
        findElementWithFluentWait(By.xpath(getThreeDotToggle.replace("{row-index}", rowIndex))).click();
    }

    @Step("Click on three dot sub menu")
    public void clickOnThreeDotSubmenuByIPV(String fqn) {
        String locator = getThreeDotToggleByFqn.replace("{fqn}", fqn);
        logger.info(locator);
        findElementWithFluentWait(By.xpath(locator)).click();
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
        WebElement menuItem = findElementWithWait(By.xpath(threeDotSubMenuItems.replace("{menuOption}", menuItemName)));
        click(menuItem);
    }
}
