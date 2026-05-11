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
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class VersionSelectorPanel extends BasePage {
    private WebDriver driver;

    public VersionSelectorPanel(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@class='version-panel__row__name'][normalize-space()='Library']/following-sibling::div[contains(@class,'text-truncate')][@title]")
    private WebElement libName;

    @FindBy(xpath = "//div[@class='version-panel__row__name'][normalize-space()='IP']/following-sibling::div[contains(@class,'text-truncate')][@title]")
    private WebElement ipName;

    @FindBy(xpath = "//*[normalize-space()='Line']//following-sibling::div[@data-testid='line-input']//input")
    private WebElement selectedLine;

    @FindBy(xpath = "//*[normalize-space()='Line']//following-sibling::div[@data-testid='line-input']//i[contains(@class,'caret-icon')]")
    private WebElement lineCaret;

    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu show')]//li[@data-testid='ui-dropdown-item']")
    private List<WebElement> lines;

    @FindBy(xpath = "//input[@placeholder='Search IP Lines']")
    private WebElement lineSearchField;

    @FindBy(xpath = "//*[normalize-space()='Version']//following-sibling::div[@data-testid='version-input']//input")
    private WebElement selectedVersion;

    @FindBy(xpath = "//div[@data-testid='version-input']//i[contains(@class,'caret-icon')]")
    private WebElement versionCaret;

    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu show')]//span[@title='LATEST']")
    private WebElement latestIpVersion;

    @FindBy(xpath = "//input[@placeholder='Search IP Versions']")
    private WebElement versionSearchField;

    @FindBy(xpath = "//*[@class='dropdown-menu show']//*[contains(@class, 'highlight-selection')]//*[@class='flex-grow-1 text-truncate menu-item-text']")
    private WebElement dropdownSelectedItem;

    @FindBy(xpath = "//div[@data-testid='tabpanel-versions-tab']//span[contains(@class, 'badge-secondary')]")
    private WebElement showMoreAliasesBtn;

    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu show')]//*[normalize-space()='Aliases']")
    private WebElement aliasesTab;

    @FindBy(xpath = "//div[@data-testid='version-input-tabs']//*[@id='tabpanel-aliases-tab' and @aria-labelledby='aliases-tab' and @data-testid='tabpanel-aliases-tab']//following::li//button//button")
    private List<WebElement> aliasesInDropDown;

    @FindBy(xpath = "//*[normalize-space()='IP Versions with this alias']//following-sibling::li")
    private List<WebElement> ipVersionsWithAlias;

    @FindBy(xpath = "//input[@placeholder='Search aliases']")
    private WebElement aliasesSearchField;

    @FindBy(xpath = "//i[contains(@class, 'circle-xmark')]")
    private WebElement clearSearchButton;

    @FindBy(xpath = "//input[@data-testid='switch-to-aliased-only-input']")
    private WebElement aliasedOnlyToggle;

    private String dropdownItem = "//*[contains(@class,'show')]//*[contains(@class,'text-truncate')][normalize-space()='{text}'][@title]";
    private String lineIndex ="//ul[contains(@class,'dropdown-menu show')]//li[@data-testid='ui-dropdown-item'][{index}]";
    private String lineHintIcon = "//*[contains(@class, 'dropdown-menu show')]//span[normalize-space()='{line}']//ancestor::button//i[contains(@class, 'hint-icon')]";
    private String versions = "//div[@data-testid='versions-list']//child::li";
    private String versionHintIcon = "//div[contains(@class,'version-input-tabs-wrapper active')]//span[normalize-space()='{version}']/following-sibling::span/i";
    private String versionDropdownHintIcon = "//div[contains(@class,'versions')]//span[normalize-space()='@4.TRUNK']/following-sibling::span/i";
    private String aliasRelatedVersionsButton = "//span[normalize-space()='{alias}']/following-sibling::span[@class='menu-item__top-row__right-content']";
    private String versionWithSelectedAlias = "//*[contains(@class, 'versions')]//span[normalize-space()='{version}']";
    private String versionAlias = "//*[contains(@class, 'active')]//div[normalize-space()='{ver}']//following-sibling::div//*[contains(@class,'badge-alias-moving')][normalize-space()='{alias}']/*[@class='text-truncate']";
    private String versionUniqueAlias = "//*[contains(@class, 'active')]//div[normalize-space()='{ver}']//following-sibling::div//*[contains(@class,'badge-alias-unique')][normalize-space()='{alias}']/*[@title='{alias}']";
    private String collapsedVersionPanel = "(//*[@class='version-panel collapsed']//span[contains(@class,'badge') and normalize-space()='{selector}'])[1] ";
    private String noDataMssg = "//div[normalize-space()='{text}']";
    private String versionSelectorColumnHeader = "//div[@data-testid='version-input-tabs']//div[contains(@class,'version-input-tabs-wrapper')]//div[contains(@class, 'ag-header-cell') and @col-id='{colId}' and @aria-colindex='{orderNumber}']";
    private String versionSelectorColumnHeaderTitle = "//div[@data-testid='version-input-tabs']//div[contains(@class,'version-input-tabs-wrapper')]//div[@class='ag-header-viewport']//div[contains(@class, 'ag-header-cell') and @col-id='{colId}']//span[@title]";
    private String versionSelectorFirstColumnHeader = "//div[@data-testid='version-input-tabs']//div[contains(@class,'version-input-tabs-wrapper')]//div[@class='ag-pinned-left-header']//div[@col-id='{colId}' and @aria-colindex='{orderNumber}']";
    private String versionSelectorColumnsWithoutOrderNumber = "//div[@data-testid='version-input-tabs']//div[contains(@class,'version-input-tabs-wrapper')]//div[@class='ag-pinned-left-header']//div[@col-id='{colId}']";
    private String xpathVersionListAliasItems = "//div[@data-testid='tabpanel-versions-tab']//div[@data-testid='versions-list']//div[@data-testid='ui-row']//span[@title='{alias}']";

    @Step("Get library name...")
    public String getLibraryName() {
        return getText(libName);
    }

    @Step("Get IP name...")
    public String getIpName() {
        return getText(ipName);
    }

    @Step("Get selected line name...")
    public String getSelectedLine() {
        waitForPageLoaded();
        return getAttributeValue(selectedLine, "value");
    }

    @Step("Click on line dropdown caret...")
    public void openLineDropdown() {
        waitForElementToBeClickable(lineCaret).click();
    }

    @Step("Select IP Line...")
    public void selectLine(String lineName) {
        openLineDropdown();
        WebElement line = findElementWithFluentWait(By.xpath(dropdownItem.replace("{text}", lineName)));
        waitTillClickableWithFluentWait(line).click();
        waitForPageLoaded();
    }

    @Step("Verify Line is present...")
    public int isLineDisplayed(String lineName, int counter) {
        return findElementsWithWait(By.xpath(dropdownItem.replace("{text}", lineName)), counter).size();
    }

    @Step("Enter line name into line search field...")
    public void enterLineName(String line) {
        inputText(lineSearchField, line);
    }

    @Step("Get number of lines in dropdown...")
    public int getNumberOfLines() {
        return getNumberOfElements(lines);
    }

    @Step("Verify line order in the dropdown list...")
    public String verifyLinesOrder(String index) {
        WebElement lineName = findElementWithWait(By.xpath(lineIndex.replace("{index}", index)));
        return getText(lineName);
    }

    @Step("Hover over Line hint icon...")
    public void hoverOverHintIconInLineList(String line) {
        WebElement icon = findElementWithWait(By.xpath(lineHintIcon.replace("{line}", line)));
        hoverOverElement(icon);
    }

    @Step("Get selected version...")
    public String getSelectedVersion() {
        waitForPageLoaded();
        waitTillVisibleWithFluentWait(selectedVersion);
        return getAttributeValue(selectedVersion, "value");
    }

    @Step("Click on version dropdown caret...")
    public void openVersionDropdown() {
        click(versionCaret);
        DriverFactory.sleep(500);
    }

    @Step("Select IP Version...")
    public void selectVersion(String version) {
        openVersionDropdown();
        WebElement ver = findElementWithWait(By.xpath(dropdownItem.replace("{text}", version)));
        click(ver);
        DriverFactory.sleep(500);
    }

    @Step("Select the LATEST IP version...")
    public void goToLatestIpv() {
        openVersionDropdown();
        click(latestIpVersion);
    }

    @Step("Verify Line is selected in the dropdwon...")
    public String dropdownSelectedLineName() {
        openLineDropdown();
        String lineName = dropdownSelectedItem.getText();
        openLineDropdown();
        return lineName;
    }

    @Step("Enter version into the search field...")
    public void enterVersionNumber(String version) {
        inputText(versionSearchField, version);
    }

    @Step("Enter version into the search field slowly...")
    public void enterVersionNumberSlowly(String version) {
        enterTextSlowly(versionSearchField, version);
        // sleep needed for stable regression
        DriverFactory.sleep(1000);
    }

    @Step("Get number of versions in dropdown...")
    public int getNumberOfIpVersions(int counter) {
        DriverFactory.sleep(1000);//Need for Improve
        return waitForNumberOfElementsToBe(By.xpath(versions), counter).size();
    }

    @Step("Verify Version is selected in the dropdown...")
    public String dropdownSelectedVersion() {
        openVersionDropdown();
        String lineName = dropdownSelectedItem.getText();
        openVersionDropdown();
        return lineName;
    }

    @Step("Show more aliases...")
    public void showMoreAliases() {
        click(showMoreAliasesBtn);
    }

    @Step("Click on Aliases tab in versions dropdown...")
    public void selectAliasesTabInVersionsDropdown() {
        click(aliasesTab);
    }

    @Step("Get the number of aliases in dropdown...")
    public int getNumberOfAliasesInDropDown() {
        return getNumberOfElements(aliasesInDropDown);
    }

    @Step("Get the number of IP versions with alias...")
    public int getNumberOfVersionsWithAlias() {
        DriverFactory.sleep(1000);//Need for Improvement
        return getNumberOfElements(ipVersionsWithAlias);
    }

    @Step("Enter alias into the search field...")
    public void enterAlias(String alias) {
        waitForPageLoaded();
        inputText(aliasesSearchField, alias);
    }

    @Step("Click on alias related versions button...")
    public void clickOnAliasRelatedVersionsButton(String alias) {
        WebElement button = findElementWithWait(By.xpath(aliasRelatedVersionsButton.replace("{alias}", alias)));
        click(button);
    }

    @Step("Click on version with alias...")
    public void clickOnVersionWithSelectedAlias(String version) {
        WebElement ver = findElementWithWait(By.xpath(versionWithSelectedAlias.replace("{version}", version)));
        click(ver);
    }

    @Step("Hover over Version hint icon...")
    public void hoverOverHintIconInVersionList(String version) {
        WebElement ver;
        if(version.contains("@")){
           ver = findElementWithWait(By.xpath(versionDropdownHintIcon.replace("{version}", version)));}
        else{
             ver = findElementWithWait(By.xpath(versionHintIcon.replace("{version}", version)));
        }
        hoverOverElement(ver);
    }

    @Step("Verify alias: {1} is displayed for IP version: {0}... ")
    public boolean isAliasDisplayed(String version, String aliasName) {
        WebElement alias = findElementWithWait(By.xpath(versionAlias.replace("{ver}", version)
                .replace("{alias}", aliasName)));
        return isElementVisible(alias);
    }

    @Step("Verify unique alias: {1} is displayed for IP version: {0}... ")
    public boolean isUniqueAliasDisplayed(String version, String aliasName) {
        WebElement alias = findElementWithWait(By.xpath(versionUniqueAlias.replace("{ver}", version)
                .replace("{alias}", aliasName)));
        return isElementVisible(alias);
    }

    @Step("Verify alias: {1} is displayed for IP version: {0}... ")
    public boolean aliasNotDisplayed(String version, String aliasName, int counter) {
        waitForNumberOfElementsToBe(By.xpath(versionAlias.replace("{ver}", version)
                .replace("{alias}", aliasName)), counter);
        return true;
    }

    @Step("Verify unique alias: {1} is displayed for IP version: {0}... ")
    public boolean uniqueAliasNotDisplayed(String version, String aliasName, int counter) {
        waitForNumberOfElementsToBe(By.xpath(versionUniqueAlias.replace("{ver}", version)
                .replace("{alias}", aliasName)), counter);
        return true;
    }

    @Step("Clear selector input by clicking on clear button IP Version tab...")
    public void clickOnClearSearchButtonVersionTab() {
        hoverOverElement(versionSearchField);
        click(clearSearchButton);
    }

    @Step("Clear selector input by clicking on clear button Aliases tab...")
    public void clickOnClearSearchButtonAliasesTab() {
        hoverOverElement(aliasesSearchField);
        click(clearSearchButton);
    }

    @Step("Clear selector input by clicking on clear button Line tab...")
    public void clickOnClearSearchButtonLineTab() {
        hoverOverElement(lineSearchField);
        click(clearSearchButton);
    }

    @Step("Toggle the 'Aliased Only' switch...")
    public void showAliasedVersions() {
        DriverFactory.sleep(1000);
        aliasedOnlyToggle.click();
        DriverFactory.sleep(1000);//Need to Improvement
    }

    @Step("Hover over collpased Version panel...")
    public void hoverCollapsedVersionPanel(String selector) {
        WebElement verPanel = findElementWithWait(By.xpath(collapsedVersionPanel.replace("{selector}", selector)));
        hoverOverElement(verPanel);
    }

    @Step("Verify no data displayed")
    public boolean noDataMssgDisplayed(String text) {
        return isElementVisible(findElementWithWait(By.xpath(noDataMssg.replace("{text}", text))));
    }

    @Step("Verify column header is visible and in right order.")
    public boolean getOrderOfVersionSelectorColumns(String propName, String orderNumber) {
        WebElement columnName;
        if (propName.equals("version") || propName.equals("name")){
            columnName = findElementWithWait(By.xpath(versionSelectorFirstColumnHeader.replace("{colId}",
                propName).replace("{orderNumber}", orderNumber)));
        } else {
            columnName = findElementWithWait(By.xpath(versionSelectorColumnHeader.replace("{colId}",
                propName).replace("{orderNumber}", orderNumber)));
        }
        return isElementVisible(columnName);
    }

    @Step("Verify column header not visible. ")
    public boolean versionSelectorColumnHeaderNotDisplayed(String colId, int counter) {
        waitForNumberOfElementsToBe(By.xpath(versionSelectorColumnsWithoutOrderNumber.replace("{colId}", colId)), counter);
        return true;
    }

    @Step("Get Property name from the version selector table...")
    public String getColumnTitleName(String propName) {
        WebElement columnTitle = findElementWithWait(By.xpath(versionSelectorColumnHeaderTitle.replace("{colId}",
            propName)));
        return getText(columnTitle);
    }

    @Step("Wait for aliases with badge '{alias}' to be visible in the versions list")
    public void waitForAliasesWithBadgeToBeVisible(String alias) {
        waitForElementsToBeVisible(driver.findElements(By.xpath(xpathVersionListAliasItems.replace("{alias}", alias))));
    }
}
