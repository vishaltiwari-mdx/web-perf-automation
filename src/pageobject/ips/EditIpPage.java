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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.awt.*;

public class EditIpPage extends BasePage {
    private WebDriver driver;

    public EditIpPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[normalize-space()='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//footer//button[normalize-space()='Save IP']")
    private WebElement confirmSaveButton;

    @FindBy(xpath = "//button[@disabled and normalize-space()='Save']")
    private WebElement disabledSaveButton;

    @FindBy(xpath = "//footer[@class='modal-footer message-box__footer']/button[normalize-space()='Create']")
    private WebElement createNewVersionButton;

    @FindBy(css = "[data-testid='ui-in-create-ip-name']")
    private WebElement ipNameInputField;

    @FindBy(css = "[data-testid='ui-in-create-ip-name'][disabled]")
    private WebElement ipNameFieldReadOnly;

    @FindBy(css = "[data-testid='dm-type-dropdown'] button")
    private WebElement dmTypeDropdown;

    @FindBy(css = "[data-testid='dm-type-dropdown'] button[disabled]")
    private WebElement dmTypeDropdownDisabled;

    @FindBy(css = "[data-testid='label-selector'] .input-group-text.px-0")
    private WebElement labelsDropdown;

    @FindBy(css = "[data-testid='label-selector'] .multiselect--disabled")
    private WebElement labelsDropdownDisabled;

    @FindBy(css = "[data-testid='ui-in-input-field-input-value'][placeholder='Search Labels']")
    private WebElement labelsInputField;

    @FindBy(css = "[data-testid='ui-in-tab-ip-details-host']")
    private WebElement hostField;

    @FindBy(css = "[data-testid='ui-in-tab-ip-details-host'][disabled]")
    private WebElement hostReadOnlyField;

    @FindBy(css = "[data-testid='tab-ip-details-ip-description']")
    private WebElement descriptionInputField;

    @FindBy(css = "[data-testid='tab-ip-details-ip-description'][disabled]")
    private WebElement descriptionReadOnlyField;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-pre-release']")
    private WebElement ipPreReleaseHookField;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-pre-release'][disabled]")
    private WebElement preReleaseHookDisabled;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-release']")
    private WebElement ipPostReleaseHookField;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-release'][disabled]")
    private WebElement postReleaseHookDisabled;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-update']")
    private WebElement ipHookPostUpdateField;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-update'][disabled]")
    private WebElement postUpdateHookDisabled;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-load']")
    private WebElement ipHookPostLoadField;

    @FindBy(css = "[data-testid='ui-in-expansion-hooks-hook-post-load'][disabled]")
    private WebElement postLoadHookDisabled;

    @FindBy(css = "[data-testid='details-tab']")
    private WebElement detailsTab;

    @FindBy(css = "[data-testid='ui-in-ipv-repo-path-input']")
    private WebElement repoPathInputFiled;

    @FindBy(css = "[data-testid='tab-ipv-details-version-message']")
    private WebElement versionMssgField;

    @FindBy(css = "[data-testid='tab-ip-details-line-description']")
    private WebElement lineDescriptionField;

    @FindBy(css = "[data-testid='project-properties-tab']")
    private WebElement projectPropertiesTab;

    @FindBy(css = "[data-testid='properties-tab']")
    private WebElement propertiesTab;

    @FindBy(xpath = "//a[@class='icon-collapse']")
    private WebElement closeIpDetailsAreaButton;

    @FindBy(xpath = "//div[contains(@class,'pannel--collapsed') and normalize-space()='IP details']")
    private WebElement ipDetailsPanelCollapsed;

    @FindBy(xpath = "//div[contains(@class,'fqn-bar__title')]")
    private WebElement ipvIdentifier;

    @FindBy(xpath = "//div[@role='alert' and normalize-space()='You have no permissions to change this IP.']")
    private WebElement permissionsAlert;

    @FindBy(css = "div[data-testid='message-box-content']")
    private WebElement createNewVersionMssg;

    @FindBy(xpath = "//div[contains(@class,'ip-manage-header')]//ancestor::div[normalize-space()='Edit IP']")
    private WebElement EditIpHeader;

    @FindBy(xpath = "//div[@class='tooltip-inner']")
    private WebElement tooltip;

    @FindBy(css = "[data-testid='tab-ip-details-ip-description'][disabled]")
    private WebElement ipDescriptionDisabled;

    private String xpathDropdownChoice = "//ul[@class='dropdown-menu show']//span[normalize-space()='{name}']";
    private String xpathLabelDropdownChoice = "//div[contains(@class, 'label-selector')]//li//span[normalize-space(text())='{name}']";
    private String xpathEditIpSecondTitle = "//div[contains(@class,'text-truncate') and @title='{name}']";
    private String xpathLibraryToolTip = "//*[@data-testid='ip-name']//span[@title='{name}']";
    private String xpathDeleteLabelButton = "//span[normalize-space(text())='{name}']//preceding-sibling::i//following-sibling::i";
    private String xpathColorLabelUnderDropdown = "//span[normalize-space(text())='{name}']//preceding-sibling::i[contains(@style,'color: rgb')]";
    private String xpathAddedResourceLink = "//span[contains(., '{ipvName}')]/a";
    private String xpathSelectedLabel = "//button[contains(@class,'input-dropdown-field__dropdown')]//span[normalize-space(text())='{labelName}']";
    private String xpathSelectedLabelOrder = "//ul[@class='dropdown-menu show']//div[contains(@class,'label-selector')]/li[{index}]//span[normalize-space()='{labelName}']";
    private String disabledField = "//input[@disabled and contains(@name,'{fiedlName}')]";
    private String disabledLabelCheckboxButton = "//span[normalize-space()='{label}']//preceding-sibling::span//div[@class='custom-control custom-checkbox']/input[@disabled='disabled']";
    private String loadingIcon = "//span[@class='spinner-border']";

    @Step("Click on Delete button...")
    public void clickOnDeleteButton() {
        click(deleteButton);
    }

    @Step("Click on Cancel button...")
    public IpDetailsPage clickOnCancelButton() {
        click(cancelButton);
        return new IpDetailsPage(driver);
    }

    @Step("Enter IP name: {0} ...")
    public void enterIpName(String ipName) {
        click(ipNameInputField);
        inputText(ipNameInputField, ipName);
    }

    @Step("Open Resource context menu...")
    public void openContextMenuForResource(String objectName) throws AWTException {
        WebElement xpathResourceLink = findElementWithWait(By.xpath(xpathAddedResourceLink.replace("{ipvName}", objectName)));
        performRightMouseClick(xpathResourceLink);
    }

    @Step("Remove IP name from the search field...")
    public void removeIpNameFromInputField() {
        waitTillVisibleWithFluentWait(ipNameInputField);
        hoverOverElement(ipNameInputField);
        ipNameInputField.clear();
    }

    @Step("Click on Save IP button...")
    public void clickOnSaveIpButton() {
        waitForPageLoaded();
        scrollUpPage();
        DriverFactory.sleep(2000);// for stable test execution, need to scope for improvement
        waitTillVisibleWithFluentWait(saveButton);
        waitTillClickableWithFluentWait(saveButton).click();
    }

    @Step("Save IP changes...")
    public VersionSelectorPanel saveIp() {
        clickOnSaveIpButton();
        logger.info("Click on Save IP button");
        waitTillVisibleWithFluentWait(confirmSaveButton);
        waitTillClickableWithFluentWait(confirmSaveButton).click();
        DriverFactory.sleep(1500);// performance bug PHI-13242
        getCurrentUrl().contains("details");
        return new VersionSelectorPanel(driver);
    }

    @Step("Save IP changes and creating new IP version...")
    public IpDetailsPage createNewIpVersion() {
        clickOnSaveIpButton();
        DriverFactory.sleep(1500);// for stable test execution, need to scope for improvement
        clickOnCreateButton();
        DriverFactory.sleep(2000);// for stable test execution, need to scope for improvement
        return new IpDetailsPage(driver);
    }

    @Step("Select a label from dropdown...")
    public void selectLabel(String labelName) {
        click(labelsDropdown);
        enterTextSlowly(labelsInputField, labelName);
        WebElement selectedLabel = findElementWithFluentWait(By.xpath(xpathLabelDropdownChoice.replace("{name}", labelName)));
        click(selectedLabel);
    }

    @Step("Verify removed label displayed dropdown menu...")
    public boolean isRemovedLabelInDropdownMenu(String labelName) {
        click(labelsDropdown);
        WebElement labelDropdownChoice = findElementWithFluentWait(By.xpath(xpathLabelDropdownChoice.replace("{name}", labelName)));
        return isElementVisible(labelDropdownChoice);
    }

    @Step("Get selected tooltip message...")
    public String getSelectedLabelTooltipMessage(String labelName) {
        WebElement selectedLabel = findElementWithWait(By.xpath(xpathSelectedLabel.replace("{labelName}", labelName)));
        hoverOverElement(selectedLabel);
        return getLinkTooltip();
    }

    @Step("Get tooltip text...")
    public String getLinkTooltip() {
        return getText(tooltip);
    }

    @Step("Select dm type from dropdown...")
    public void selectDmType(String ipType) {
        click(dmTypeDropdown);
        WebElement dmType = findElementWithWait(By.xpath(xpathDropdownChoice.replace("{name}", ipType)));
        click(dmType);
    }

    @Step("Verify CONT type is selected...")
    public boolean isContDmTypeSelected() {
        return isElementVisible(dmTypeDropdown);
    }

    @Step("Enter host name to the Perforce host field...")
    public void enterHostName(String hostName) {
        inputText(hostField, hostName);
    }

    @Step("Enter IP description: {0}, for the method: {method}...")
    public void enterIpDescription(String ipDescription) {
        inputText(descriptionInputField, ipDescription);
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

    @Step("Get host field value...")
    public String getHostFieldValue() {
        return getAttribute(hostField);
    }

    @Step("Get pre release hook field value...")
    public String getPreReleaseHook() {
        return getAttribute(ipPreReleaseHookField);
    }

    @Step("Get post release hook field value...")
    public String getPostReleaseHook() {
        return getAttribute(ipPostReleaseHookField);
    }

    @Step("Get post update hook field value...")
    public String getPostUpdateHook() {
        return getAttribute(ipHookPostUpdateField);
    }

    @Step("Get IP description field value...")
    public String getIpDescriptionFieldValue() {
        return getAttribute(descriptionInputField);
    }

    @Step("Click on Details tab...")
    public void goToDetailsSection() {
        click(detailsTab);
    }

    @Step("Verify on Details tab visible...")
    public boolean isDetailsTabVisible() {
        return isElementVisible(detailsTab);
    }

    @Step("Enter repository path...")
    public void enterRepoPath(String repoPath) {
        inputText(repoPathInputFiled, repoPath);
    }

    @Step("Enter IPV version mssg...")
    public void enterVersionMessage(String versionMssg) {
        inputText(versionMssgField, versionMssg);
    }

    @Step("Get version message field value...")
    public String getVersionMessageFieldValue() {
        return getAttribute(versionMssgField);
    }

    @Step("Get repo path field value...")
    public String getRepoPathFieldValue() {
        return getAttribute(repoPathInputFiled);
    }

    @Step("Navigate to Project properties section...")
    public ProjectPropertiesPage goToProjectPropertiesSection() {
        click(projectPropertiesTab);
        return new ProjectPropertiesPage(driver);
    }

    @Step("Navigate to Properties section...")
    public IpPropertiesSectionPage clickOnPropertiesSection() {
        waitTillVisibleWithFluentWait(propertiesTab);
        waitForElementToBeClickable(propertiesTab).click();
        return new IpPropertiesSectionPage(driver);
    }

    @Step("Remove repo path from the search field...")
    public void clearRepoPathField() {
        waitForElementToBeClickable(repoPathInputFiled).click();
        clearInputField(repoPathInputFiled);
    }

    @Step("Remove description from the search field...")
    public void clearIpDescription() {
        clearInputField(descriptionInputField);
    }

    @Step("Verify DM type dropdown is disabled...")
    public boolean isDmTypeDropdownDisabled() {
        return isElementVisible(dmTypeDropdownDisabled);
    }

    @Step("Remove version message from input field...")
    public void clearVersionMessageField() {
        clearInputField(versionMssgField);
    }

    @Step("Get create new version message...")
    public String verifyReleaseVersion() {
        return getText(createNewVersionMssg);
    }

    @Step("Get IPV identifier...")
    public String getIpvIdentifier() {
        WebElement ipvIdent = waitForElementToBeVisible(ipvIdentifier);
        String attribute = getAttributeValue(ipvIdent, "data-fqn");
        logger.info("IPV Identifier: " + attribute);
        return attribute;
    }

    @Step("Click on Create New Version button...")
    public void clickOnCreateButton() {
        waitForElementToBeClickable(createNewVersionButton).click();
    }

    @Step("Verify Library field tooltip...")
    public boolean isLibraryTooltipDisplayed(String lib) {
        WebElement library = findElementWithWait(By.xpath(xpathLibraryToolTip.replace("{name}", lib)));
        return isElementVisible(library);
    }

    @Step("Verify Label is selected...")
    public boolean isLabelSelected(String labelName) {
        WebElement label = findElementWithWait(By.xpath(xpathDeleteLabelButton.replace("{name}", labelName)));
        return isElementVisible(label);
    }

    @Step("Detach label from IP...")
    public void detachIpLabel(String labelName) {
        WebElement deleteLabelButton = findElementWithWait(By.xpath(xpathDeleteLabelButton.replace("{name}", labelName)));
        clickWithJS(deleteLabelButton);
    }

    @Step("Verify Edit IP sub-header is displayed...")
    public boolean isEditIpSubHeaderPresent() {
        return isElementVisible(EditIpHeader);
    }

    @Step("Verify Edit IP second sub-header is displayed...")
    public boolean isEditIpSecondSubHeaderPresent(String ipName) {
        waitForPageLoaded();
        WebElement editIpHeader = findElementWithFluentWait(By.xpath(xpathEditIpSecondTitle.replace("{name}", ipName)));
        return isElementVisible(editIpHeader);
    }

    @Step("Verify IP name field is disabled...")
    public boolean isIpNameFieldReadOnly() {
        return isElementVisible(ipNameFieldReadOnly);
    }

    @Step("Verify host field is disabled...")
    public boolean isIpHostFieldReadOnly() {
        return isElementVisible(hostReadOnlyField);
    }

    @Step("Verify labels select field is disabled...")
    public boolean isLabelsSelectorDisabled() {
        return isElementVisible(labelsDropdownDisabled);
    }

    @Step("Verify description field is disabled...")
    public boolean isDescriptionFieldReadOnly() {
        return isElementVisible(descriptionReadOnlyField);
    }

    @Step("Verify post-load hook field is disabled...")
    public boolean isPostLoadHookReadOnly() {
        return isElementVisible(postLoadHookDisabled);
    }

    @Step("Verify post-update hook field is disabled...")
    public boolean isPostUpdateHookReadOnly() {
        return isElementVisible(postUpdateHookDisabled);
    }

    @Step("Verify post-release hook field is disabled...")
    public boolean isPostReleaseHookReadOnly() {
        return isElementVisible(postReleaseHookDisabled);
    }

    @Step("Verify pre-release hook field is disabled...")
    public boolean isPreReleaseHookReadOnly() {
        return isElementVisible(preReleaseHookDisabled);
    }

    @Step("Verify permissions alert is displayed...")
    public boolean isRequiredPermissionsNoticeDisplayed() {
        return isElementVisible(permissionsAlert);
    }

    @Step("Navigate to the Edit IP Page...")
    public void goToEditIpPage(String fqn, String line) {
        waitForPageLoaded();
        goTo(DriverFactory.getFullUrl("/#/ip/" + fqn + "/edit?line=" + line));
    }

    @Step("Verify that  delete button is visible")
    public boolean isDeleteButtonVisible() {
        return isElementVisible(deleteButton);
    }

    public boolean isDeleteButtonVisibleForSelectedLabel(String label) {
        WebElement deleteLabelButton = findElementWithFluentWait(By.xpath(xpathDeleteLabelButton.replace("{name}", label)));
        return isElementVisible(deleteLabelButton);
    }

    @Step("Verify if the color label under dropdown is visible...")
    public boolean isColorLabelVisibleForSelectedLabel(String label) {
        WebElement colorLabel = findElementWithFluentWait(By.xpath(xpathColorLabelUnderDropdown.replace("{name}", label)));
        return isElementVisible(colorLabel);
    }

    @Step("Verify label order with index and label name ...")
    public boolean checkSelectedLabelOrderWithIndexAndName(int index, String labelName) {
        return isElementVisible(findElementWithFluentWait(By.xpath(xpathSelectedLabelOrder.replace("{index}", String.valueOf(index)).replace("{labelName}", labelName))));
    }


    @Step("Verify field is disabled...")
    public boolean isDisabledFieldVisible(String fieldName) {
        return isElementVisible(findElementWithFluentWait(By.xpath(disabledField.replace("{fiedlName}", fieldName))));
    }

    @Step("Verify IP Description field is disabled...")
    public boolean isDisabledIpDescriptionFieldVisible() {
        return isElementVisible(ipDescriptionDisabled);
    }

    @Step("Verify Label X Icon is not visible...")
    public boolean isLabelCrossIconNotVisible(String labelName) {
        return isElementNotVisible(xpathDeleteLabelButton.replace("{name}", labelName));
    }

    @Step("Verify Label checkbox is disabled in Label dropdown...")
    public boolean isLabelCheckboxDisabled(String labelName) {
        return isElementNotVisible(disabledLabelCheckboxButton.replace("{label}", labelName));
    }

    @Step("Type text into Line Description input field...")
    public void enterLineDescription(String lineDescription) {
        inputText(lineDescriptionField, lineDescription);
    }

    @Step("Verify disabled Save button is visible...")
    public boolean isDisabledSaveButtonVisible() {
        return isElementVisible(disabledSaveButton);
    }

    @Step("Verify loading spinner is displayed...")
    public void waitForDataToLoad() {
        waitForNumberOfElementsToBe(By.xpath(loadingIcon), 0);
    }

    @Step("Open label displayed dropdown menu...")
    public void clickOnLabelDropdown() {
        click(labelsDropdown);
    }
}
