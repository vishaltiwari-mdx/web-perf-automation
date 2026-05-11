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
import com.methodics.phi.pageobject.dashboard.DashboardPage;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateIpPage extends BasePage {
    private final WebDriver driver;

    public CreateIpPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "[data-testid='ui-in-input-dropdown-input-value'][placeholder='Select a Library']")
    private WebElement libraryDropdown;

    @FindBy(css = "[data-testid='ip-name'] input[data-testid='ui-in-create-ip-name']")
    private WebElement ipNameInputField;

    @FindBy(css = "[data-testid='dm-type-dropdown'] input[data-testid='ui-in-input-dropdown-input-value']")
    private WebElement dmTypeDropdown;

    @FindBy(css = "[data-testid='ip-host'] input[data-testid='ui-in-tab-ip-details-host'][disabled]")
    private WebElement hostFieldDisabled;

    @FindBy(css = "[data-testid='ip-host'] input[data-testid='ui-in-tab-ip-details-host']:not([disabled])")
    private WebElement hostFieldActive;

    @FindBy(css = "[data-testid='ip-host'] input[data-testid='ui-in-tab-ip-details-host']")
    private WebElement hostField;

    @FindBy(css = "[data-testid='ip-description'] textarea[data-testid='tab-ip-details-ip-description']")
    private WebElement descriptionInputField;

    @FindBy(css = "[data-testid='line-description'] textarea[data-testid='tab-ip-details-line-description']")
    private WebElement lineDescriptionInputField;

    @FindBy(css = "[data-testid='label-selector'] i[class*='rotate fa-angle-up fa-fw caret-icon']")
    private WebElement labelsDropdown;

    @FindBy(css = "[data-testid='label-selector'] input[placeholder='Search Labels']")
    private WebElement labelsInputField;

    @FindBy(css = "[data-testid='expansion-hooks'] input[data-testid='ui-in-expansion-hooks-hook-pre-release']")
    private WebElement ipPreReleaseHookField;

    @FindBy(css = "[data-testid='expansion-hooks'] input[data-testid='ui-in-expansion-hooks-hook-post-release']")
    private WebElement ipPostReleaseHookField;

    @FindBy(css = "[data-testid='expansion-hooks'] input[data-testid='ui-in-expansion-hooks-hook-post-update']")
    private WebElement ipHookPostUpdateField;

    @FindBy(css = "[data-testid='expansion-hooks'] input[data-testid='ui-in-expansion-hooks-hook-post-load']")
    private WebElement ipHookPostLoadField;

    @FindBy(css = "[data-testid='expansion-hooks'] .plus-minus")
    private WebElement clientSideHooksButton;

    @FindBy(css = "[data-testid='ipv-repo-path'] input[data-testid='ui-in-ipv-repo-path-input']")
    private WebElement repoPathInputFiled;

    @FindBy(css = "[data-testid='ipv-repo-path'] input[data-testid='ui-in-ipv-repo-path-input'][disabled]")
    private WebElement repoPathFiledDisabled;

    @FindBy(css = "[data-testid='ipv-version-message'] textarea[data-testid='tab-ipv-details-version-message']")
    private WebElement versionMssgField;

    @FindBy(css = "[data-testid='project-properties-tab']")
    private WebElement projectPropertiesTab;

    @FindBy(css = "[data-testid='properties-tab']")
    private WebElement propertiesTab;

    @FindBy(css = "[data-testid='ui-btn-ui-modal-save-ip']")
    private WebElement saveIpOnConfirmation;

    @FindBy(css = "[data-testid='ui-btn-editor-page-header-save']")
    private WebElement createButton;

    @FindBy(css = "[data-testid='ui-btn-editor-page-header-cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[contains(@class, 'page-title__heading') and normalize-space()='Create IP']")
    private WebElement createIpSubHeader;

    @FindBy(css = ".tooltip-inner")
    private WebElement tooltip;

    @FindBy(css = "[data-testid='ui-in-tab-ipv-details-file-path']")
    private WebElement filePath;

    @FindBy(css = "div[class*='label-selector'] i[class*='rotate fa-angle-up fa-fw caret-icon']")
    private WebElement disabledLabelArrow;

    @FindBy(css = "[data-testid='label-selector'] .label-selector-input-dropdown input[placeholder='Select Labels'] + span[data-testid='ui-input-group'] i")
    private WebElement collapseLabelArrow;

    private final String xpathDmTypeDropdownChoice = "//button[@data-testid='ui-btn-dm-type-menu-items']//span[contains(@class,'trailing-text')][normalize-space()='{name}']";
    private final String xpathDropdownChoice = "//div[contains(@class, 'libraries-selector')]//li[@data-testid='ui-dropdown-item']//span[contains(@class,'menu-item-text')][normalize-space(text())={name}]";
    private final String xpathLabelDropdownChoice = "//div[contains(@class, 'label-selector')]//li[@data-testid='ui-dropdown-item']//span[contains(@class,'menu-item-text')][normalize-space(text())={name}]";
    private final String libraries = "//*[@data-testid='library-selector']//div[@class='menu-items-wrapper']/li[@data-testid='ui-dropdown-item']";
    private final String xpathLabelCheckbox = "//span[normalize-space()='{label}']//preceding-sibling::span//*[@class='form-check-input']";
    private final String xpathLabelColorCheck = "//span[normalize-space()='{label}']//preceding-sibling::span//i[contains(@style,'color: rgb')]";
    private final String tabNames = "//*[@data-testid='ip-tabs']/ul/li[{cellId}]/a";
    private final String toolTipHooks = "//span[normalize-space()='{name}']//i[contains(@class,'hint-icon icon-15x12')]";
    private final String xpathSelectedLabel = "//span[@data-testid='ui-badge']//span[normalize-space()='{label}']";

    // Library selector root element
    private static final String LIBRARY_SELECTOR_XPATH = "//*[@data-testid='library-selector']";
    private final String librarySelector = LIBRARY_SELECTOR_XPATH;
    private final String librariesDropdown = librarySelector + "//input[@placeholder='Select a Library']";
    private final String libraryDropdownChoice = librarySelector + "//li//span[normalize-space(text())={name}]";
    private final String librariesDropdownShow = librarySelector + "//ul[contains(@class,'dropdown-menu') and contains(@class,'show')]";
    private final String libraryDropdownList = librariesDropdownShow + "//div/li[contains(@class,'menu-item')]//span[@title]";
    private final String libraryDropdownItemIcon = libraryDropdownChoice + "/preceding-sibling::span/*[contains(@class,'badge-20x16')]";
    private final String libraryDropdownItemHintIcon = libraryDropdownChoice + "/following-sibling::span/i[contains(@class,'hint-icon fa-fw icon-15x12')]";
    private final String libraryInputField = librariesDropdownShow + "//input[@placeholder='Search Libraries']";


    @Step("Navigate to Create IP page...")
    public CreateIpPage goToCreateIpPage(String url) {
        goTo(url);
        return new CreateIpPage(driver);
    }

    @Step("Verify the library dropdown is displayed...")
    public boolean isLibraryDropdownPresent() {
        return isElementVisible(librariesDropdown);
    }

    @Step("input the library name")
    public void inputLibraryNameInSearch(String libName) {
        DriverFactory.sleep(500);
        final WebElement element = waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(libraryInputField)));
        element.clear();
        element.click();
        enterTextSlowly(element, libName);
        DriverFactory.sleep(1500);
    }

    @Step("Select the library from the dropdown list...")
    public void selectLibrary(String libName) {
        openLibrariesDropdown();
        inputLibraryNameInSearch(libName);
        libName = wrapObjNameForXPath(libName);
        final WebElement library = findElementWithFluentWait(By.xpath(libraryDropdownChoice.replace("{name}", libName)));
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", library);
    }

    @Step("Click on Library dropdown...")
    public void openLibrariesDropdown() {
        waitForPageLoaded();
        final WebElement dropdown = findElementWithFluentWait(By.xpath(librariesDropdown));
        waitForElementToBeClickable(dropdown).click();
    }

    @Step("Click on Library dropdown...")
    public void collapseLibrariesDropdown() {
        waitForPageLoaded();
        if (isElementVisible(librariesDropdownShow)) {
            final WebElement dropdown = findElementWithFluentWait(By.xpath(librariesDropdown));
            waitForElementToBeClickable(dropdown).click();
        }
    }

    @Step("Click on Labels dropdown...")
    public void collapseLabelsDropdown() {
        waitForPageLoaded();
        if (isElementVisible(collapseLabelArrow)) {
            waitForElementToBeClickable(collapseLabelArrow).click();
        }
    }

    @Step("get the list of library item")
    public List<String> getLibraryItemList() {
        waitForPageLoaded();
        final List<WebElement> libraryElements = findElementsWithFluentWait(By.xpath(libraryDropdownList));
        final List<String> uniqueLibraryItems = libraryElements.stream().map(element -> element.getText().trim()).collect(Collectors.toList());
        logger.info("Library items: " + uniqueLibraryItems);
        return new ArrayList<>(uniqueLibraryItems);
    }

    @Step("Get number of libraries in dropdown...")
    public int getNumberOfLibraries(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(libraries), counter).size();
    }

    public boolean isLibrarySearchFieldPresent() {
        return isElementVisible(libraryInputField);
    }

    @Step("Verify the library item icon is displayed...")
    public boolean isLibraryIconPresent(String library) {
        library = wrapObjNameForXPath(library);
        return isElementVisible(libraryDropdownItemIcon.replace("{name}", library));
    }

    @Step("Verify the library item hint icon is displayed...")
    public boolean isLibraryTooltipIconPresent(String library) {
        library = wrapObjNameForXPath(library);
        return isElementVisible(libraryDropdownItemHintIcon.replace("{name}", library));
    }

    @Step("Enter IP name: {0}, for the method: {method}...")
    public void enterIpName(String ipName) {
        click(ipNameInputField);
        inputText(ipNameInputField, ipName);
    }

    @Step("Clear IP name field...")
    public void clearEnterIpName() {
        clearInputField(ipNameInputField);
    }

    @Step("Select dm type from dropdown...")
    public void selectDmType(String ipType) {
        openDmTypeDropdown();
        final WebElement dmType = findElementWithWait(By.xpath(xpathDmTypeDropdownChoice.replace("{name}", ipType)));
        scrollToElement(dmType);
        click(dmType);
    }

    @Step("Open DM type dropdown...")
    public void openDmTypeDropdown() {
        click(dmTypeDropdown);
    }

    @Step("Enter host name to the Perforce host field...")
    public void enterHostName(String hostName) {
        inputText(hostField, hostName);
    }

    @Step("Verify host field is present...")
    public boolean isHostFieldPresent() {
        return isElementVisible(hostField);
    }

    @Step("Select label from dropdown list...")
    public void selectLabel(String labelName) {
        click(labelsDropdown);
        inputText(labelsInputField, labelName);
        DriverFactory.sleep(5000);
        final String name = wrapObjNameForXPath(labelName);
        final WebElement label = findElementWithWait(By.xpath(xpathLabelDropdownChoice.replace("{name}", name)));
        click(label);
    }

    @Step("open label dropdown...")
    public void openLabelDropdown() {
        waitForPageLoaded();
        click(labelsDropdown);
    }

    @Step("Select label from dropdown list...")
    public void selectLabelByClickCheckBox(String labelName) {
        inputText(labelsInputField, labelName);
        DriverFactory.sleep(2000);
        final WebElement label = findElementWithWait(By.xpath(xpathLabelCheckbox.replace("{label}", labelName)));
        click(label);
    }

    @Step("Verify if the selected label is visible...")
    public boolean isSelectedLabelVisible(String label) {
        final WebElement selectedLabelName = findElementWithFluentWait(By.xpath(xpathSelectedLabel.replace("{label}", label)));
        return isElementVisible(selectedLabelName);
    }

    @Step("Verify if the selected label is not visible...")
    public boolean isRemovedLabelInDropdownMenu(String label) {
        return isElementNotVisible(xpathSelectedLabel.replace("{label}", label));
    }

    @Step("Verify label name is visible in label dropdown...")
    public boolean isLabelDropdownChoiceVisible(String label) {
        final String name = wrapObjNameForXPath(label);
        final WebElement labelDropdownChoice = findElementWithFluentWait(By.xpath(xpathLabelDropdownChoice.replace("{name}", name)));
        return isElementVisible(labelDropdownChoice);
    }

    @Step("Verify if the label checkbox is visible...")
    public boolean isLabelCheckboxVisible(String label) {
        final WebElement labelCheckbox = findElementWithFluentWait(By.xpath(xpathLabelCheckbox.replace("{label}", label)));
        return isElementVisible(labelCheckbox);
    }

    @Step("Verify if the label checkbox is visible...")
    public boolean isLabelColouredVisible(String label) {
        final WebElement labelCheckbox = findElementWithFluentWait(By.xpath(xpathLabelColorCheck.replace("{label}", label)));
        return isElementVisible(labelCheckbox);
    }

    @Step("Get number of labels in dropdown...")
    public int getNumberOfLabels(String labelName, int counter) {
        click(labelsDropdown);
        inputText(labelsInputField, labelName);
        final String name = wrapObjNameForXPath(labelName);
        return waitForNumberOfElementsToBe(By.xpath(xpathDropdownChoice.replace("{name}", name)), counter).size();
    }

    @Step("Enter IP description: {0}, for the method: {method}...")
    public void enterIpDescription(String ipDescription) {
        inputText(descriptionInputField, ipDescription);
    }

    @Step("Click on IP line description field...")
    public void clickOnIpLineDescriptionField() {
        descriptionInputField.click();
        waitForPageLoaded();
    }

    @Step("Enter pre release hooks...")
    public void enterPreReleaseHooks(String hookName) {
        inputText(ipPreReleaseHookField, hookName);
    }

    @Step("Enter post release hooks...")
    public void enterPostReleaseHooks(String hookName) {
        inputText(ipPostReleaseHookField, hookName);
    }

    @Step("Enter post update hooks...")
    public void enterPostUpdateHooks(String hookName) {
        inputText(ipHookPostUpdateField, hookName);
    }

    @Step("Enter post load hooks...")
    public void enterPostLoadHooks(String hookName) {
        inputText(ipHookPostLoadField, hookName);
    }

    @Step("Enter repository path...")
    public void enterRepoPath(String repoPath) {
        inputText(repoPathInputFiled, repoPath);
    }

    @Step("click repository path...")
    public void clickOnRepoPathField() {
        repoPathInputFiled.click();
    }

    public void clickOnFilePathField() {
        filePath.click();
        waitForPageLoaded();
    }

    @Step("click host field...")
    public void clickOnHostField() {
        hostField.click();
        waitForPageLoaded();
    }

    @Step("Enter file path...")
    public void enterFilePath(String path) {
        inputText(filePath, path);
    }

    @Step("Get repo path field value...")
    public String getRepoPathFieldValue() {
        return getAttribute(repoPathInputFiled);
    }

    @Step("Remove repo path ...")
    public void clearRepoPathField() {
        waitForElementToBeClickable(repoPathInputFiled).click();
        clearInputField(repoPathInputFiled);
    }

    @Step("Remove file path ...")
    public void clearFilePathField() {
        waitForElementToBeClickable(filePath).click();
        clearInputField(filePath);
    }

    @Step("Enter IPV version mssg...")
    public void enterVersionMssg(String versionMssg) {
        versionMssgField.click();
        inputText(versionMssgField, versionMssg);
    }

    @Step("Enter ip line description...")
    public void enterIpLineDescription(String ipLineDescription) {
        inputText(lineDescriptionInputField, ipLineDescription);
    }

    @Step("Navigate to Project properties section...")
    public ProjectPropertiesPage goToProjectPropertiesSection() {
        scrollToElement(projectPropertiesTab);
        waitTillClickableWithFluentWait(projectPropertiesTab);
        DriverFactory.sleep(2000);
        projectPropertiesTab.click();
        return new ProjectPropertiesPage(driver);
    }

    @Step("Navigate to Properties section...")
    public IpPropertiesSectionPage clickOnPropertiesSection() {
        click(propertiesTab);
        return new IpPropertiesSectionPage(driver);
    }

    @Step("Click on Save button...")
    public IpDetailsPage saveIp() {
        scrollUpPage();
        scrollToElement(createButton);
        waitForPageLoaded();
        final WebElement element = waitTillClickableWithFluentWait(createButton);
        hoverOverElement(element);
        clickWithJS(element);
        waitForPageLoaded();
        return new IpDetailsPage(driver);
    }

    @Step("Click on Save button...")
    public IpDetailsPage clickOnSaveIpOnConfirmation() {
        waitTillClickableWithFluentWait(saveIpOnConfirmation).click();
        return new IpDetailsPage(driver);
    }

    @Step("Get Create Ip button enabled status...")
    public boolean createIpButtonStatus() {
        scrollUpPage();
        waitTillVisibleWithFluentWait(createButton);
        return createButton.isEnabled();
    }

    @Step("Create a new IP: {0}...")
    public IpDetailsPage createNewIp(String libraryName, String ipName) {
        selectLibrary(libraryName);
        enterIpName(ipName);
        saveIp();
        return new IpDetailsPage(driver);
    }

    @Step("Click on Cancel button...")
    public DashboardPage clickCancelButton() {
        click(cancelButton);
        return new DashboardPage(driver);
    }

    @Step("Click on Close IP Details Area button...")
    public void clickPlusMinusClientSideButton() {
        scrollToEndOfPage();
        click(clientSideHooksButton);
    }

    @Step("Verify Create IP sub-header is displayed...")
    public boolean isCreateIpSubHeaderPresent() {
        return isElementVisible(createIpSubHeader);
    }

    @Step("Verify selected Library dropdown menu long name is truncate...")
    public boolean isSelectedLibraryDropdownMenuTruncated() {
        final WebElement dropdown = findElementWithFluentWait(By.xpath(librariesDropdown));
        logger.info("Locator path: "+dropdown.toString());
        waitTillVisibleWithFluentWait(dropdown);
        final String text_overflow = dropdown.getCssValue("text-overflow").trim();
        final String white_space = dropdown.getCssValue("white-space").trim();
        final String overflow = dropdown.getCssValue("overflow").trim();
        logger.info("text-overflow: " + text_overflow + ", white-space: " + white_space + ", overflow: " + overflow);
        return text_overflow.equals("ellipsis") || white_space.equals("nowrap")  ||  overflow.equals("clip");
    }

    @Step("Get tab name...")
    public String getTabNameText(String id) {
        return getText(findElementWithWait(By.xpath(tabNames.replace("{cellId}", id))));
    }

    @Step("Get value from pre release hooks...")
    public String getPreReleaseHooksText() {
        return getAttribute(ipPreReleaseHookField);
    }

    @Step("Get value from post release hooks...")
    public String getPostReleaseHooksText() {
        return getAttribute(ipPostReleaseHookField);
    }

    @Step("Get value from post update hooks...")
    public String getPostUpdateHooksText() {
        return getAttribute(ipHookPostUpdateField);
    }

    @Step("Get value from post load hooks...")
    public String getPostLoadHooksText() {
        return getAttribute(ipHookPostLoadField);
    }

    @Step("Get tooltip text for hooks...")
    public String getTooltipTextForHooks(String name) {
        final WebElement toolIcon = findElementWithWait(By.xpath(toolTipHooks.replace("{name}", name)));
        waitForElementToBeVisible(toolIcon);
        hoverOverElement(toolIcon);
        return getObjectToolTip();
    }

    @Step("Get selected DM type value...")
    public String getSelectedValueForDMType() {
        waitTillVisibleWithFluentWait(dmTypeDropdown);
        DriverFactory.sleep(1000);
        return getAttribute(dmTypeDropdown);
    }

    @Step("Verify Repo path is displaying disable...")
    public boolean isRepoPathDisable() {
        return isElementVisible(repoPathFiledDisabled);
    }

    @Step("Verify Host is displaying disable...")
    public boolean isHostFieldDisable() {
        scrollToElement(hostFieldDisabled);
        return isElementVisible(hostFieldDisabled);
    }

    @Step("Scroll to the bottom of the page...")
    public void scrollDownBottomPage() {
        scrollDownPage();
    }

    public String getRepoPathDisableToolTip() {
        scrollToElement(repoPathInputFiled);
        hoverOverElement(repoPathInputFiled);
        return getObjectToolTip();
    }

    public String getHostDisableToolTip() {
        hoverOverElement(hostFieldDisabled);
        DriverFactory.sleep(100);
        return getObjectToolTip();
    }

    @Step("Get tooltip text...")
    public String getObjectToolTip() {
        waitTillVisibleWithFluentWait(tooltip);
        return tooltip.getText().trim();
    }

    @Step("open disabled label dropdown...")
    public void openDisabledLabelDropdown() {
        waitForPageLoaded();
        click(disabledLabelArrow);
    }

    @Step("open disabled label dropdown...")
    public void hoverToLabelDropdownSearchField() {
        hoverOverElement(labelsInputField);
    }
}
