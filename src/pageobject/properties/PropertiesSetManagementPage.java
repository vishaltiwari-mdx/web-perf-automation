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
package com.methodics.phi.pageobject.properties;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.messages.ModalPopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static com.methodics.phi.util.CommonUrls.PROPERTY_SETS_MANAGEMENT_PAGE;

public class PropertiesSetManagementPage extends BasePage {
    private WebDriver driver;
    private static final String PROPERTY_SETS_MANAGEMENT_TITLE = "Property Sets";

    public PropertiesSetManagementPage(WebDriver driver) {
        this.driver = driver;

        if (driver != null) {
            driver.get(DriverFactory.getFullUrl(PROPERTY_SETS_MANAGEMENT_PAGE));
        }

        waitForTitle(PROPERTY_SETS_MANAGEMENT_TITLE);
        if (!driver.getTitle().contains("Property Sets")) {
            throw new IllegalStateException(
                    "This is not Property Sets Page, current page is: " + driver.getTitle());
        }
    }

    @FindBy(xpath = "//input[@placeholder='Search Property Sets']")
    private WebElement propertySetSearchField;

    @FindBy(css = "span[data-testid='ui-input-group']")
    private WebElement cancelSearchButton;

    @FindBy(css = "button[data-testid='ui-btn-propertyset-create-new']")
    private WebElement createNewSetButton;

    @FindBy(css = "input[data-testid='ui-in-input-field']")
    private WebElement setNameInputField;

    @FindBy(css = "input[placeholder='Enter name'][disabled]")
    private WebElement setNameInputFieldDisabled;

    @FindBy(xpath = "//textarea[@placeholder='Enter description']")
    private WebElement descriptionInputField;

    @FindBy(css = "button[data-testid='ui-btn-properties-add']")
    private WebElement plusButton;

    @FindBy(css = "input[data-testid='ui-in-input-field-input-value'][placeholder='Search Property Sets']")
    private WebElement propNameInputField;

    @FindBy(css = "input[data-testid='ui-in-input-field-input-value'][placeholder='Filter Properties']")
    private WebElement propNameInputPopUpModalField;

    @FindBy(css = "button[data-testid='ui-btn-propertyset-modal-add-properties']")
    private WebElement addButton;

    @FindBy(css = "button[data-testid='ui-btn-propertyset-modal-add-properties'][disabled]")
    private WebElement addButtonDisabled;

    @FindBy(css = "button[data-testid='ui-btn-property-set-create']")
    private WebElement saveButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(css = "button[data-testid='ui-btn-propertyset-modal-cancel']")
    private WebElement cancelButtonPropertyModal;

    @FindBy(css = "button[data-testid='ui-btn-property-set-create'][disabled]")
    private WebElement createButtonDisabled;

    @FindBy(xpath = "//*[text()='Add Properties to the Property Set']")
    private WebElement noPropertiesAddedMessage;

    @FindBy(xpath = "//strong[normalize-space()='Properties added to this Property Set']//following-sibling::span[@data-testid='ui-badge']")
    private WebElement propertiesAddedBadge;

    @FindBy(xpath = "//div[@name='left']//div[@row-index and @aria-selected='false']")
    private List<WebElement> numberOfUnCheckBoxCount;

    @FindBy(xpath = "//*[contains(@data-testid,'property-set-list-item') and contains(@class,'selected')]//*[contains(@data-testid,'list-item-badge')]")
    private WebElement numberOfPropertiesBadge;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]")
    private WebElement pageDescription;

    @FindBy(xpath = "//div[text()=' This Property Set has been set as the global default across Helix IPLM. Properties added to this Property Set will be applied to all IPs.']"
    )
    private WebElement gloablPropertySetMessage;

    @FindBy(xpath = "//i[@class='fa fa-minus']")
    private WebElement minusButton;

    @FindBy(css = "button[data-testid='ui-btn-properties-remove']")
    private WebElement removeButton;

    @FindBy(xpath = "//span[text()='Create a set or select a set to edit.']")
    private WebElement createPropertySetHeader;

    @FindBy(xpath = "//*[@data-testid='ui-col']//div[normalize-space()='Property Sets']")
    private WebElement propertySetSubHeader;

    @FindBy(css = "div[data-testid='ui-list-group'] > li[data-testid]")
    private List<WebElement> propSetsList;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]/i[contains(@class, 'object-group')]")
    private WebElement propertySetPageIcon;

    @FindBy(xpath = "//button//i[contains(@class, 'object-group')]")
    private WebElement propertySetIcon;

    @FindBy(css = "div[data-testid='ui-alert'] div.align-items-center")
    private WebElement globalPropertySetAlert;

    @FindBy(xpath = "//*[normalize-space()='Security']//ancestor::div[@data-testid='ui-col']//*[@data-testid='ui-input-group']//*[contains(@class,'checkbox-switch_normal')]")
    private WebElement securityToggleSwitch;

    @FindBy(xpath = "//div[@class='prop-sets-list']//*[normalize-space()='No Property Sets found.']")
    private WebElement NoPropertySetFound;

    @FindBy(css = "button[data-testid='ui-btn-property-set-update']")
    private WebElement updateDetailsButton;

    @FindBy(css = "a[data-testid='permissions']")
    private WebElement permissionsButton;

    @FindBy(css = "a[data-testid='details']")
    private WebElement detailsTab;

    @FindBy(css = "div[data-testid='tabpanel-permissions'] input[data-testid='ui-in-input-field-input-value'][placeholder='Search Groups']")
    private  WebElement searchGroupsButton;

    @FindBy(css = "div[data-testid='tabpanel-permissions'] input[data-testid='ui-in-input-field-input-value'][placeholder='Search Users']")
    private  WebElement userSearchBox;

    @FindBy(xpath = "//a[@data-testid='permissions']//following::a[@data-testid='users']")
    private WebElement usersTab;

    @FindBy(xpath = "//a[@data-testid='permissions']//following::a[@data-testid='groups']")
    private WebElement groupsTab;

    @FindBy(css = "button[data-testid='ui-btn-property-set-update'][disabled]")
    private WebElement updateDetailsDisabledButton;

    @FindBy(css = "textarea[data-testid='property-set-description'][disabled]")
    private WebElement detailsDisabledField;

    @FindBy(css = "button[data-testid = 'ui-btn-property-set-discard-changes'][disabled]")
    private WebElement discardChangesButtonDisabled;

    @FindBy(css = "button[data-testid='ui-btn-properties-remove'][disabled]")
    private WebElement removeButtonDisabled;

    @FindBy(css = "button[data-testid='ui-btn-properties-add']")
    private WebElement addButtonDetailsPagePropSet;

    @FindBy(css = "div[data-testid='message-box-content']")
    private WebElement discardConfirmationMessage;

    @FindBy(xpath = "//h5[normalize-space(.)='Discard changes']")
    private WebElement discardPopupTitle;

    @FindBy(css = "button[data-testid='ui-btn-property-set-discard-changes']")
    private WebElement discardChangesButton;

    @FindBy(css = "button[data-testid='ui-btn-ui-modal-cancel']")
    private WebElement discardConfirmationPopUpCancelButton;

    @FindBy(css = "button[data-testid='ui-btn-ui-modal-discard-changes']")
    private WebElement discardConfirmationPopUpDiscardChangeButton;

    @FindBy(css = "span[class='ag-tab'] span[class='ag-icon ag-icon-columns']")
    private WebElement menuIconOnGrid;

    @FindBy(xpath = "//*[@data-testid='groups']//*[@data-testid='ui-badge']")
    private WebElement groupsTabBadge;

    @FindBy(xpath = "//*[@data-testid='users']//*[@data-testid='ui-badge']")
    private WebElement usersTabBadge;

    @FindBy(css = "div:has(> input[aria-label$='all rows selection (checked)'])")
    private WebElement allRowsSelection;

    @FindBy(xpath = "//div[contains(@class,'cols-container')]//div[contains(@class,'input-wrapper ag-checked')]")
    private List<WebElement> checkedInputWrappers;

    @FindBy(xpath = "//a[@data-testid='details' and contains(@class, 'active')]")
    private WebElement activeDetailsTab;

    @FindBy(xpath = "//a[@data-testid='permissions' and contains(@class, 'active')]")
    private WebElement activePermissionTab;

    @FindBy(css = "button[data-testid='ui-btn-properties-modal-close'][class*= 'btn-close-dark']")
    private WebElement xButtonOnPropertiesPopUp;

    @FindBy(xpath = "//h5[normalize-space()='No results found']")
    private WebElement noResultsFoundMessage;

    @FindBy(css = "div[data-testid='truncate-modal-cell-renderer-content']")
    private WebElement largeTextPopUpText;

    @FindBy(css = "button[data-testid='ui-btn-truncate-modal-cell-renderer-close-button']")
    private WebElement largeTextPopUpCloseButton;

    @FindBy(css = "[data-testid='property-icon'][class='icon-wrapper no-rows-overlay__icon']")
    private WebElement noPropertyOverlayIcon;

    @FindBy(xpath = "//div[@data-testid='ui-col']//strong[normalize-space()='Properties added to this Property Set']")
    private WebElement propertyLabel;

    @FindBy(xpath = "//*[@data-testid='ui-col']/h5[contains(@class,'overlay') and normalize-space()='Add Properties to the Property Set']")
    private WebElement addPropertiesToPropertySetOverlayTitle;

    @FindBy(xpath = "//*[@data-testid='ui-in-input-field-input-value' and @placeholder='Search Properties' and @disabled]")
    private WebElement searchPropertiesInputFieldDisabled;

    @FindBy(xpath = "//div[@class='ag-overlay']//span[contains(@class,'no-rows-overlay__description') and contains(text(),'Click the Add button to add Properties to this Property Set. Learn more about ')]")
    private WebElement noRowsOverlayDescription;

    @FindBy(css = "a[data-testid='link-properties-help-doc']")
    private WebElement propertiesHelpDocLink;

    @FindBy(xpath = " //div[contains(@data-testid,'user-permissions')]//input[@data-testid='cell-renderer-perm-checkbox_owner-input']")
    private WebElement userPermissionCheckboxOwnerInput;

    @FindBy(xpath = " //div[contains(@data-testid,'group-permissions')]//input[@data-testid='cell-renderer-perm-checkbox_owner-input']")
    private WebElement groupPermissionCheckboxOwnerInput;

    @FindBy(xpath = "//div[@data-testid='review-update-permissions-modal']//div[@class='alert__component__body']")
    private WebElement reviewUpdatePermissionsWarningMessage;

    @FindBy(css = "i[data-testid='name-cell-renderer-right-icon']")
    private WebElement permissionAlertIcon;

    @FindBy(css = "button[data-testid='ui-btn-confirm-changes-button']")
    private WebElement confirmChangesButton;

    @FindBy(css = "button[data-testid='ui-btn-continue-editing-button']")
    private WebElement continueediting;

    @FindBy(css = "[data-testid='properties-modal-filter'] input[data-testid='ui-in-input-field-input-value']")
    private WebElement filterPropertiesInputFieldPopUP;

    @FindBy(xpath = "//input[@data-testid='property-set-is-protected-switch-input']//following::label[@data-testid='property-set-is-protected-switch-label']")
    private WebElement propertySetIsProtectedSwitchLabel;

    @FindBy(xpath = " //input[@data-testid='property-set-is-protected-switch-input']/preceding::span[normalize-space(.)='Security']")
    private WebElement propertySetSecurityLabel;

    @FindBy(xpath = "//input[@data-testid='property-set-is-protected-switch-input']//following::i[@data-testid='shield-icon']")
    private WebElement propertySetProtectedIcon;

    @FindBy(css = "i[data-testid='shield-icon'] + i.hint-icon")
    private WebElement infoIconForProtected;

    @FindBy(css = ".tooltip-inner")
    private WebElement tooltipInner;

    @FindBy(xpath = "//label[@data-testid='property-set-allow_write_on_target_read-checkbox-label']")
    private WebElement allowWriteOnTargetReadCheckboxLabel;

    @FindBy(css = "div.form-check:has(> input[data-testid='property-set-allow_write_on_target_read-checkbox-input']) + i.hint-icon")
    private WebElement allowWriteOnTargetReadInfoIcon;

    @FindBy(xpath = "//a[@data-testid='permissions' and @aria-disabled='true']")
    private WebElement permissionsTabDisabled;

    @FindBy(xpath = "//input[@data-testid='property-set-allow_write_on_target_read-checkbox-input']")
    private WebElement permissionsTabAllowWriteOnTargetReadCheckboxInput;

    @FindBy(xpath = " //*[normalize-space()='Security']//ancestor::div[@data-testid='ui-col']//*[@data-testid='ui-input-group']//*[contains(@class,'checkbox-switch_normal')]//input[@data-testid='property-set-is-protected-switch-input' and @disabled] ")
    private WebElement protectedSwitchDisabled;

    @FindBy(xpath = "//h5[normalize-space()='Remove protection']")
    private WebElement removeProtectionConfirmationPopupTitle;

    @FindBy(css = "button[data-testid='ui-btn-modal-header-close']")
    private WebElement closePopupRemoveProtectionConfirmationPopup;

    @FindBy(css = "div[data-testid='message-box-content']")
    private WebElement messageBoxContentRemoveProtectionConfirmationPopup;

    @FindBy(xpath = "//div[contains(@class, 'overlay-wrapper')]//h5[text()='No results found']")
    private WebElement noResultsFound;

    // Add these as String XPaths for hidden/visible divs
    private final String allowWriteOnTargetReadHiddenDivXpath = "//label[@data-testid='property-set-allow_write_on_target_read-checkbox-label']//parent::div[@style='display: none;']";
    private final String removeProtectionPopupOptions = "button[data-testid='ui-btn-ui-modal-{popupOptionName}']";

    private String xpathPropertySetHeader = "//*[@data-testid='ui-list-group']//li[@data-testid='property-set-list-item-{name}' and contains(@class,'selected')]";
    private String xpathPropSetName = "//*[@data-testid='property-set-list-item-{name}']//div[normalize-space()='{name}'][contains(@class,'text-truncate')]";
    private String propSets = "//*[contains(@data-testid,'property-set-list-item')]";
    private String xpathPropSetSelected = "//*[contains(@data-testid,'property-set-list-item-{name}') and contains(@class,'selected')]";
    private String xpathPropertyRow = "//div[normalize-space()='target_size']//div[contains(@class,'ag-checkbox-input')]";
    private String propertyRows = "//div[@role='rowgroup' and @class='ag-pinned-left-cols-container']//*[contains(@class, 'ag-row-no-focus')]";
    private String xpathSelectPropertiesModal = "//h5[text()='Select Properties']";
    private String gridTitle = "//div[contains(@class,'text-truncate')]//span[@title='Properties that are included in the {name} Property Set.']";
    private String customIconPropertySet = "//li[@data-testid='property-set-list-item-{propSetName}']//*[@data-testid= 'property-set-list-custom-icon-{name}']";
    private String defaultIconPropertySet = "//li[@data-testid='property-set-list-item-{propSetName}']//*[@data-testid= 'property-set-icon']";
    private String propertySetName = "//li[@data-testid='property-set-list-item-{propSetName}']";
    private String deleteButton = "//button[@data-testid = 'ui-btn-property-set-delete-btn-{propSet}']";
    private String groupList = "div[class*='cols-container'] div[col-id='name_group']";
    private String userlist = "div[class*='cols-container'] div[col-id='name']";
    private String propertyCheckBox = "//*[@data-testid='{propName}']//ancestor::div[@role='row']//input[@type='checkbox']//parent::div";
    private String shildIconXpath = "//li[@data-testid='property-set-list-item-{propSetName}']//*[contains(@class, 'fa-shield')]";
    private String checkBoxesOnPopup = "//div[@aria-label='{name}']//input[@type='checkbox']";
    private String disabledColumnHeaderCheckbox  = "div[aria-label='{name}'] div[class*='ag-disabled']";
    private String hoverOnColumnHeaderOnPermissionTab  = ".ag-header-cell:has(.ag-header-cell-text[title='{name}']) .ag-icon.ag-icon-menu";
    private String columnHeaderOnGroupUserTab = "div[id='{tabpanel}'] span[title='{name}']";
    private String columnnameToHideDisplay = "div[aria-label='{name}']";
    private String columnnameOnGroupTab = "div[id='tabpanel-groups'] div[class*='ag-header-row'] > div[col-id='{name}']";
    private String columnnameOnUserTab  = "div[id='tabpanel-users'] div[class*='ag-header-row'] > div[col-id='{name}']";
    private String cellValuePropertyTableXpath = "//*[@col-id='{colId}']//*[@data-testid='{name}']";
    private String goToPropertyIconXpath = "//*[normalize-space()='{propName}']//*[contains(@class,'cursor--pointer redirect-icon')]";
    private String dropdownOptionForGroupUserTab = "//div[contains(@data-testid,'{tabname}')]//button[contains(@data-testid,'dropdown-toggle')]";
    private String dropDownOption = "//div[contains(@data-testid,'{tabname}')]//button[@data-testid='ui-btn-menu-item']//span[normalize-space()='{optionname}']";
    private String dropDownOptionDisable = "//div[contains(@data-testid,'{tabname}')]//button[@data-testid='ui-btn-menu-item' and @disabled]//span[normalize-space()='{optionname}']";
    private String GroupUserList = "//div[contains(@class,'cols-container')]//div[@col-id='{name}']";
    private String  columnHeader = "//*[text()='{colName}' and contains(@class,'ag-header-cell')]";
    private String columeHeaderCountsPropSet = "//div[@role]//span[@title and contains(@class,'ag-header')]";
    private final String descriptionProperty = "//div[@col-id='{name}']//div[@role='label-name']";
    private final String showMoreButton = "//span[@class='ellipsis-content']";
    private final String numberOfCheckBoxStatusCount = "//div[@class='ag-pinned-left-cols-container']//div[@col-id='0']//div[contains(@class,'{checkboxState}')]";
    private final String selectAllPropertiesCheckBox = "//*[@class ='property-sets']//*[@col-id='name']//span[text()='Property name']//ancestor::div[@role='row']/div[@col-id='{colId}']//div[@data-ref='eWrapper' and @role]";
    private String columnHeaderName = "//div[@col-id='{colId}' and @role='columnheader']";
    private String columnHeadersPropertyNameCellsValue = "//div[@col-id='{colId}' and @role='gridcell']/parent::div[@row-index='0']";
    private String columnHeadersPropertyCellsValue = "//div[@col-id='{colId}' and @role='gridcell']/parent::div[@row-index='0']//div[@col-id='{colId}']";
    private String columnHeadersPropertyCellsValues = "//div[@col-id='{colId}' and @role='gridcell']/parent::div[@row-index]//div[@col-id='{colId}']";
    private String xpathCustomIconInPropertySet = "//li[@data-testid='property-set-list-item-{propSet}']//*[@data-testid='property-set-list-custom-icon-{iconName}']";
    private String xpathDefaultIconInPropertySet = "//li[@data-testid='property-set-list-item-{propSet}']//*[@data-testid='property-set-icon'][contains(@class,'icon-wrapper')]";
    private String xpathShieldIconInPropertySet = "//*[@data-testid='ps-{propSet}-shield-icon' and contains(@class,'shield-keyhole icon-20x16')]";

    @Step("Go to Property Sets Management page...")
    public void goToPropertySetsManagementPage(String url) {
        goTo(url);
    }

    @Step("Click on Create New Set button...")
    public void clickOnCreateNewPropertySetButton() {
        DriverFactory.sleep(2000);//Need for Improvement
        waitTillClickableWithFluentWait(createNewSetButton).click();
        DriverFactory.sleep(1000);//Need for Improvement
    }

    @Step("Enter property set name: {0}, for the method: {method}...")
    public void enterPropertySetName(String setName) {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(setNameInputField).click();
        enterTextSlowly(setNameInputField, setName);
    }

    @Step("Clear property set name...")
    public void clearPropertySetName() {
        clearInputFieldWithBackspace(setNameInputField);
    }

    @Step("Get the property set name input field value...")
    public String getPropertySetName() {
        return getAttribute(setNameInputField);
    }

    @Step("Click on Save button...")
    public void clickOnSaveButton() {
        click(saveButton);
    }

    @Step("Create a new property set...")
    public void createNewPropertySet(String setName) {
        waitForPageLoaded();
        clickOnCreateNewPropertySetButton();
        enterPropertySetName(setName);
        clickOnSaveButton();
        DriverFactory.sleep(1500);// Need for Improvement
    }

    @Step("Create a new protected property set...")
    public void createNewProtectedPropertySet(String setName) {
        clickOnCreateNewPropertySetButton();
        enterPropertySetName(setName);
        clickOnSecurityToggleSwitch();
        clickOnSaveButton();
    }

    @Step("Enter property set description: {0}, for the method: {method}...")
    public void enterPropertySetDescription(String setName) {
        enterTextSlowly(descriptionInputField, setName);
    }

    @Step("Get the property set description input field value...")
    public String getPropertySetDescription() {
        return getAttribute(descriptionInputField);
    }

    @Step("Verify Save button is disabled...")
    public boolean isCreatePropertySetButtonDisabled() {
        return isElementVisible(createButtonDisabled);
    }

    @Step("Verify Add button is disabled...")
    public boolean isAddButtonDisabled() {
        return isElementVisible(addButtonDisabled);
    }

    @Step("Click on property set: {0}...")
    public void clickOnPropertySet(String propSetName) {
        WebElement propertySet = findElementWithFluentWait(By.xpath(xpathPropSetName.replace("{name}", propSetName)));
        click(propertySet);
        DriverFactory.sleep(1000);//Need for Improvement
    }

    @Step("Click on Plus button...")
    public void clickOnPlusButton() {
        click(plusButton);
    }

    @Step("Enter property name: {0}, for the method: {method}...")
    public void enterPropNameIntoSearchField(String propName) {
        click(propNameInputField);
        enterTextSlowly(propNameInputField, propName);
    }

    @Step("Enter property name in pop-up Modal: {0}, for the method: {method}...")
    public void enterPropNameIntoSearchPopUpModalField(String propName) {
        click(propNameInputPopUpModalField);
        enterTextSlowly(propNameInputPopUpModalField, propName);
    }

    @Step("Clear and enter value in Filter Properties input field: {value}...")
    public void clearAndEnterFilterPropertiesInputField(String value) {
        final WebElement input = waitTillVisibleWithFluentWait(filterPropertiesInputFieldPopUP);
        scrollToElement(input);
        // Use JS click to bypass any overlay blocking normal interaction
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        clearInputFieldWithBackspace(input);
        logger.info("Cleared Filter Properties input field, entering value: '{}'", value);
        enterTextSlowly(input, value);
        logger.info("Entered value '{}' into Filter Properties input field", value);
    }

    @Step("Select property from the list: {0}, for the method: {method}...")
    public void selectPropertyToAdd(String propName) {
        // need to keep this sleep for stable regression
        DriverFactory.sleep(1000);
        clickPropertyCheckBox(propName);
    }

    @Step("Click on Done button...")
    public void clickOnAddButton() {
        click(addButton);
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Click on Remove button...")
    public void clickOnRemoveButton() {
        waitForElementToBeClickable(removeButton).click();
    }

    @Step("Add property to property set...")
    public void addPropertyToPropertySet(String propSetName, String propName) {
        clickOnPropertySet(propSetName);
        clickOnPlusButton();
        enterPropNameIntoSearchPopUpModalField(propName);
        selectPropertyToAdd(propName);
        clickOnAddButton();
        DriverFactory.sleep(1000);
        clickOnUpdateDetailsButton();
        DriverFactory.sleep(1000); //after Update details it take some time to reflect the changes on UI, need to keep sleep for stable regression
    }

    @Step("Get the name of the property sets by name: {0}...")
    public boolean isPropertySetPresentOnPropSetsPanel(String propSetName) {
        WebElement elem = findElementWithWait(By.xpath(xpathPropSetName.replace("{name}", propSetName)));
        return isElementVisible(elem);
    }

    @Step("Get number of property sets...")
    public int getNumberOfPropertySets(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(propSets), counter).size();
    }

    @Step("Click on Delete button...")
    public void clickDeleteButton(String propertySetName) {
        hoverOverPropertySetName(propertySetName);
        WebElement xpathDeleteButton =waitForElement(By.xpath(deleteButton.replace("{propSet}", propertySetName)));
        click(xpathDeleteButton);
    }

    @Step("Click on Cancel button on Select Properties modal...")
    public void clickCancelOnPropertiesModal() {
        click(cancelButtonPropertyModal);
    }

    @Step("Delete a property set...")
    public void deletePropertySet(String propName) {
        hoverOverPropertySetName(propName);
        clickDeleteButton(propName);
    }

    @Step("Verify no properties are added yet...")
    public boolean isNoPropertiesAddedMessagePresent() {
        return isElementVisible(noPropertiesAddedMessage);
    }

    @Step("Get number of the added properties from the icon...")
    public String getNumberOfAddedPropertiesFromIcon() {
        DriverFactory.sleep(500);//Need for improvement
        return getText(propertiesAddedBadge);
    }

    @Step("Get number of properties in property set from the icon...")
    public String getNumberOfPropertiesFromIcon() {
        return getText(numberOfPropertiesBadge);
    }

    @Step("Get number of properties...")
    public int getNumberOfProperties(int counter) {
        waitForPageLoaded();
        waitForUiModule();
        return waitForNumberOfElementsToBe(By.xpath(propertyRows), counter).size();
    }

    @Step("Enter property set name: {0}, for the method: {method}...")
    public void enterPropertySetNameIntoSearchField(String setName) {
        //sleep added to ensure stable regression
        DriverFactory.sleep(600);
        propertySetSearchField.clear();
        waitTillClickableWithFluentWait(propertySetSearchField);
        enterTextSlowly(propertySetSearchField, setName);
        DriverFactory.sleep(1000);
    }

    @Step("Get page description...")
    public String getPageDescription() {
        return getText(pageDescription);
    }

    @Step("Verify page description is present...")
    public boolean isPageDescriptionPresent() {
        return isElementVisible(pageDescription);
    }

    @Step("Verify property: {0} is displayed in Property Set...")
    public int isPropertyAddedToPropertySet(String propName, int counter) {
        return waitForElements(By.xpath(xpathPropertyRow.replace("{propName}", propName)), counter).size();
    }

    @Step("Delete the property from the property set...")
    public void deletePropertyFromPropertySet(String propSetName, String propName) {
        clickPropertyCheckBox(propSetName);
        clickOnRemoveButton();
        clickOnSaveButton();
        //sleep added to ensure stable regression
        DriverFactory.sleep(1000);
    }

    @Step("Verify Select Properties modal dialog is not displayed...")
    public boolean selectPropertiesHeadingNotDisplayed() {
        return waitForNumberOfElementsToBe(By.xpath(xpathSelectPropertiesModal), 0).size() == 0;
    }

    @Step("Remove property name from the search field...")
    public void removePropertyNameFromSearchField() {
        clearTextByBackspace(propNameInputPopUpModalField);
    }

    @Step("Remove property set name from the search field by clicking on cancel button...")
    public void removeSetNameFromSearchFieldByClickingCancelButton() {
        waitForElementToBeClickable(cancelSearchButton);
        click(cancelSearchButton);
    }

    @Step("Remove property set name from the search field...")
    public void removeSetNameFromSearchField() {
        clearInputFieldWithBackspace(propertySetSearchField);
    }

    @Step("Verify global property set info message is displayed...")
    public boolean isGlobalPropertySetInfoMessageDisplayed() {
        return isElementVisible(gloablPropertySetMessage);
    }

    @Step("Verify create new set info message is displayed...")
    public boolean isCreateNewPropertySetInfoMessageDisplayed() {
        return isElementVisible(createPropertySetHeader);
    }

    @Step("Verify property set is selected...")
    public boolean isPropertySetSelected(String propSetName) {
        WebElement propertySetSelected = findElementWithWait(By.xpath(xpathPropSetSelected.replace("{name}", propSetName)));
        return isElementVisible(propertySetSelected);
    }

    @Step("Verify Property set sub-header is displayed...")
    public boolean isPropertySetSubHeaderPresent() {
        return isElementVisible(propertySetSubHeader);
    }

    @Step("Verify sub-header for selected property set is displayed...")
    public boolean isSelectedPropertySetSubHeaderPresent(String property) {
        WebElement propertySetHeader = findElementWithWait(By.xpath(xpathPropertySetHeader.replace("{name}", property)));
        return isElementVisible(propertySetHeader);
    }

    @Step("Get initial number of property sets...")
    public int getInitialNumberOfPropertySets() {
        List<WebElement> elements = waitForElementsToBeVisible(propSetsList);
        return elements.size();
    }

    @Step("Verify Property Set page icon is displayed...")
    public boolean isPropertySetPageIconPresent() {
        return isElementVisible(propertySetPageIcon);
    }

    @Step("Verify Property Set icon is displayed...")
    public boolean isPropertySetIconPresent() {
        return isElementVisible(propertySetIcon);
    }

    @Step("Verify message above the grid is present...")
    public boolean isGridMessagePresent(String libName) {
        WebElement gridMessage = findElementWithWait(By.xpath(gridTitle.replace("{name}", libName)));
        return isElementVisible(gridMessage);
    }

    @Step("Verify that the global property set has  correct alert message")
    public boolean isGlobalPropertySetAlertTextMatch(String expectedText) {
        String actualText = getText(globalPropertySetAlert).trim();
        return expectedText.equals(actualText);
    }

    @Step("Verify that the global property set security toggle switch is not visible for Global property set...")
    public boolean isGlobalPropertySetSecurityToggleSwitchNotVisible() {
        return !isElementVisible(securityToggleSwitch);
    }

    @Step("Verify that the  property set do not have Badge for no property associated")
    public boolean isNumberOfPropertiesBadgeNotVisible() {
        try {
            return !numberOfPropertiesBadge.isDisplayed();
        } catch (Exception e) {
            logger.warn("Number of Property Badge is not displaying for "+numberOfPropertiesBadge.toString()+" element");
            return true;
        }
    }

    @Step("Verify that the custom icon is visible for the property set: {0}...")
    public boolean isCustomIconPropertySetVisible(String iconName, String propertySetName) {
        WebElement customIconElement = waitForElementToBePresent(By.xpath(customIconPropertySet.replace("{name}", iconName).replace("{propSetName}", propertySetName)));
        return customIconElement.isDisplayed();
    }

    @Step("Verify that the default icon is visible for the property set: {0}...")
    public boolean isDefaultIconPropertySetVisible(String propertySetName) {
        WebElement defaultIconElement = waitForElementToBePresent(By.xpath(defaultIconPropertySet.replace("{propSetName}", propertySetName)));
        return defaultIconElement.isDisplayed();
    }

    @Step("Verify set name input field is disabled...")
    public boolean isSetNameInputFieldDisabled() {
        return setNameInputFieldDisabled != null && setNameInputFieldDisabled.isDisplayed();
    }

    @Step("Hover over property set name: {propSetName}...")
    public void hoverOverPropertySetName(String propSetName) {
        WebElement propertySet = findElementWithFluentWait(By.xpath(propertySetName.replace("{propSetName}", propSetName)));
        hoverOverElement(propertySet);
    }

    @Step("Delete button is not displaying for Global property")
    public boolean deleteButtonNotDisplayingFor(String propertySet) {
        hoverOverPropertySetName(propertySet);
        return isElementNotVisible(deleteButton);
    }

    @Step("Add property to property set without Save...")
    public void addPropertyToPropertySetWithoutSave(String propName) {
        clickOnPlusButton();
        enterPropNameIntoSearchPopUpModalField(propName);
        selectPropertyToAdd(propName);
        clickOnAddButton();
        DriverFactory.sleep(1000);
    }

    @Step("Delete the property from the property set without Save...")
    public void deletePropertyFromPropertySetWithoutSave(String propName) {
        clickPropertyCheckBox(propName);
        clickOnRemoveButton();
        //sleep added to ensure stable regression
        DriverFactory.sleep(1000);
    }

    @Step("Delete the Multiple properties from the property set without Save...")
    public void deletePropertyFromPropertySetWithoutSave(String... propName) {
        for (int j = 0; j < propName.length; j++) {
            clickPropertyCheckBox(propName[j]);
        }
        clickOnRemoveButton();
        DriverFactory.sleep(1000);
    }

    @Step("No property set found message displaying")
    public boolean isNoPropertySetFound()
    {
        return isElementVisible(NoPropertySetFound);
    }

    @Step("Click on Update Details button")
    public void clickOnUpdateDetailsButton() {
        waitTillClickableWithFluentWait(updateDetailsButton).click();
    }

    @Step("Click on permission tab button...")
    public void clickOnPermissionsButton() {
        DriverFactory.sleep(1000); //need to keep sleep for stable regression
        waitForElementToBeVisible(permissionsButton).click();
    }

    @Step("Click on Details tab button...")
    public void clickOnDetailsTab() {
        waitForElementToBeVisible(detailsTab).click();
    }

    @Step("enter text in search box: {0}...")
    public void enterTextInSearchBox(String text) {
        waitForElementToBeVisible(searchGroupsButton);
        clearInputFieldWithBackspace(searchGroupsButton);
        searchGroupsButton.sendKeys(text);
    }

    @Step("Get number of groups present on permission tab...")
    public int getNumberOfGroupsPresent(){
        List<WebElement> groups = findElementsWithFluentWait(By.cssSelector(groupList));
        return groups.size();
    }

    @Step("Click on usertab button...")
    public void clickOnUserTab() {
        waitTillClickableWithFluentWait(usersTab);
        click(usersTab);
    }

    @Step("Click on Grouptab button...")
    public void clickOnGroupTab() {
        waitTillClickableWithFluentWait(groupsTab);
        click(groupsTab);
    }

    @Step("Get number of users present on permission tab...")
    public int getNumberOfUserPresent() {
        try {
            List<WebElement> users = findElementsWithFluentWait(By.cssSelector(userlist));
            return users.size();
        } catch (TimeoutException e) {
            logger.debug("No users present on permission tab", e);
            return 0;
        }
    }

    @Step("Get badge count for groups tab...")
    public String getGroupsTabBadgeCount() {
        return groupsTabBadge.getText();
    }

    @Step("Get badge count for users tab...")
    public String getUsersTabBadgeCount() {
        return usersTabBadge.getText();
    }

    @Step("enter text in user search box: {0}...")
    public void enterTextInUserSearchBox(String text) {
        waitForElementToBeVisible(userSearchBox);
        clearInputFieldWithBackspace(userSearchBox);
        userSearchBox.sendKeys(text);
    }

    @Step("Verify Update Details Disabled field displaying")
    public boolean isUpdateDetailsFieldDisabled() {
        return isElementVisible(detailsDisabledField);
    }

    @Step("Verify disabled set name input field is visible")
    public boolean isSetNameInputFieldDisabledVisible() {
        return setNameInputFieldDisabled.isDisplayed();
    }

    @Step("Verify disabled set description input field is visible")
    public boolean isSetNameDescriptionFieldDisabledVisible() {
        return setNameInputFieldDisabled.isDisplayed();
    }

    @Step("Verify disabled update details button is visible")
    public boolean isUpdateDetailsButtonDisabledVisible() {
        return isElementVisible(updateDetailsDisabledButton);
    }

    @Step("Verify disabled discard changes button is not visible")
    public boolean isDiscardChangesDisabledButtonNotVisible() {
        try {
            if (discardChangesButtonDisabled.isDisplayed())
            {
                logger.warn("Discard Changes Button is displaying for {} element", discardChangesButtonDisabled);
                return false;
            }
        } catch (Exception e) {
            logger.info("Discard Changes Button is not displaying for element", discardChangesButtonDisabled);
        }
        return true;
    }

    @Step("Verify disabled remove button is not visible")
    public boolean isRemoveButtonDisabledNotVisible() {
        try {
            if (removeButtonDisabled.isDisplayed())
            {
                logger.warn("Remove Button disabled is displaying for {} element", removeButtonDisabled);
                return false;}
        } catch (Exception e) {
            logger.info("Remove Button is not disabled for {} element", removeButtonDisabled);
        }
        return true;
    }

    @Step("Verify Add Property button is not visible")
    public boolean isAddPropertyButtonNotVisible() {
        try {
            if (addButtonDetailsPagePropSet.isDisplayed())
            {
                logger.warn("Add Property Button is displaying for {} element", addButtonDetailsPagePropSet);
                return false;}
        } catch (Exception e) {
            logger.info("Add Property Button is not displaying for {} element", addButtonDetailsPagePropSet);
        }
        return true;
    }

    @Step("Click on Security Toggle Switch to mark property set as protected...")
    public void clickOnSecurityToggleSwitch() {
        waitTillClickableWithFluentWait(securityToggleSwitch);
        click(securityToggleSwitch);
    }

    @Step("Click on Security Toggle Switch to make it unprotected...")
    public void clickOnSecurityToggleSwitchToMakeItUnprotected() {
        waitTillClickableWithFluentWait(securityToggleSwitch).click();
        final ModalPopupPage modalPopupPage = new ModalPopupPage(driver);
        modalPopupPage.clickOnRemoveButton();
        waitForPageLoaded();
    }

    @Step("Verify that the shield icon is visible for the property set: {0}...")
    public boolean isShieldIconPropertySetVisible(String propertySetName) {
        try {
            final WebElement propertySetItem = waitForElementToBePresent(By.xpath(
                    shildIconXpath.replace("{propSetName}", propertySetName
                    )));
            return propertySetItem.isDisplayed();
        } catch (Exception e) {
            logger.warn("Shield icon is not visible for property set: " + propertySetName);
            return false;
        }
    }

    @Step("Verify Update Details button is enabled")
    public boolean isUpdateDetailsButtonEnabled() {
        try {
            return updateDetailsButton.isEnabled() && updateDetailsButton.isDisplayed();
        } catch (Exception e) {
            logger.warn("Update Details button is not enabled or not visible");
            return false;
        }
    }

    @Step("Verify user can hover on column header and open menu icon")
    public void hoverMouseOncolumnAndOpenMenuIcon(String Columnname,String tabpanelname) {
        try {
            String Hoverlocator = hoverOnColumnHeaderOnPermissionTab.replace("{name}", Columnname);
            WebElement checkboxHover = driver.findElement(By.cssSelector(Hoverlocator));

            String columnHeaderLocator = columnHeaderOnGroupUserTab.replace("{name}", Columnname).replace("{tabpanel}", tabpanelname);
            WebElement checkboxColumnHeader = driver.findElement(By.cssSelector(columnHeaderLocator));

            waitTillClickableWithFluentWait(checkboxColumnHeader);
            click(checkboxColumnHeader);
            waitTillClickableWithFluentWait(checkboxHover);
            click(checkboxHover);
        } catch (Exception e) {
            logger.warn("Hover on Group column header is not working");
        }
    }

    @Step("Verify user can open menu icon on grid to hide/display column ")
    public void clickOnMenuIconOnGrid() {
        try {
            if (isElementVisible(menuIconOnGrid)) {
                click(menuIconOnGrid);
            } else {
                logger.warn("Menu icon is not visible for grid to hide on grid element");
            }
        } catch (Exception e) {
            logger.warn("Click on menu icon on grid is not working");
        }
    }

    @Step("Click on column header to hide or display column")
    public void clickOnColumnHeaderToHideOrDisplay(String ColumnName) {
        String columnHeaderLocator = columnnameToHideDisplay.replace("{name}", ColumnName);
        WebElement clickonColumnHeader = driver.findElement(By.cssSelector(columnHeaderLocator));
        waitTillClickableWithFluentWait(clickonColumnHeader);
        click(clickonColumnHeader);
    }

    @Step("Verify column header is visible on group tab or Not")
    public boolean isColumnDisplayOnGroupTableGrid(String ColumnName) {
        try {
            String columnHeaderLocator = columnnameOnGroupTab.replace("{name}",ColumnName);
            WebElement columnnameOnTable = driver.findElement(By.cssSelector(columnHeaderLocator));
            columnnameOnTable.isDisplayed();
        } catch (Exception e) {
            logger.warn("Description row heading is not visible");
            return false;
        }
        return true;
    }

    @Step("Verify {Columnname} column checkbox is selected on hide/display column popup")
    public boolean isCheckBoxSelected(String Columnname){
        String finalXpath = checkBoxesOnPopup.replace("{name}", Columnname);
        return driver.findElement(By.xpath(finalXpath)).isSelected();
    }

    @Step("Verify Column checkbox is disabled")
    public boolean isDisabledColumnHeaderCheckboxVisible(String Columnname) {
        try {
            String finalXpath = disabledColumnHeaderCheckbox.replace("{name}", Columnname);
            WebElement checkbox = driver.findElement(By.cssSelector(finalXpath));
            checkbox.isDisplayed();
        } catch (Exception e) {
            logger.warn("Disabled Read column header checkbox is not visible");
            return false;
        }
        return true;
    }

    @Step("Verify Column header is visible on user tab or Not")
    public boolean isColumnDisplayOnUserTableGrid(String ColumnName) {
        try {
            String columnHeaderLocator = columnnameOnUserTab.replace("{name}",ColumnName);
            WebElement columnnameOnTable = driver.findElement(By.cssSelector(columnHeaderLocator));
            columnnameOnTable.isDisplayed();
        } catch (Exception e) {
            logger.warn("source row heading is not visible");
            return false;
        }
        return true;
    }

    @Step("Click on cell value property table element...")
    public void hoverOverCellOfPropertyTable(String colId, String propName) {
        WebElement cellValue = waitForElementToBePresent(By.xpath(
                cellValuePropertyTableXpath.replace("{colId}", colId)
                        .replace("{name}",propName)));
        hoverOverElement(cellValue);
    }

    @Step("Check if GoToProperty icon is displayed for property: {propName}...")
    public boolean goToPropertyIconDisplayForPropertySet(String colId, String propName) {
        WebElement cellValue = waitForElementToBePresent(By.xpath(
                cellValuePropertyTableXpath.replace("{colId}", colId).replace("{name}", propName)));
        hoverOverElement(cellValue);
        return isElementVisible(goToPropertyIconXpath.replace("{propName}", propName));
    }

    @Step("Wait for GoToProperty icon to be displayed and click it for property: {propName}...")
    public void goToPropertyAndClick(String colId, String propName) {
        // Hover over the cell in the property table to reveal the GoToProperty icon
        WebElement cellValue = waitForElementToBePresent(By.xpath(
            cellValuePropertyTableXpath.replace("{colId}", colId).replace("{name}", propName)));
        hoverOverElement(cellValue);
        // Wait for the GoToProperty icon to be present and clickable, then click it
        WebElement goToIcon = waitForElementToBePresent(By.xpath(goToPropertyIconXpath.replace("{propName}", propName)));
        waitTillClickableWithFluentWait(goToIcon).click();
        switchToOpenedTab();
        }

    @Step("Click on property checkbox: {0}...")
    public void clickPropertyCheckBox(String propName) {
        String checkBoxXpath = propertyCheckBox.replace("{propName}", propName);
        WebElement checkBox = waitForElementToBePresent(By.xpath(checkBoxXpath));
        waitTillClickableWithFluentWait(checkBox).click();
    }

    @Step("Verify property checkbox is not visible for property: {propName}...")
    public boolean isPropertyCheckBoxNotVisible(String propName) {
        final String checkBoxXpath = propertyCheckBox.replace("{propName}", propName);
        final boolean noCheckBoxForPropName = driver.findElements(By.xpath(checkBoxXpath)).isEmpty();
        logger.info("Property checkbox for '{}' is not visible: {}", propName, noCheckBoxForPropName);
        return noCheckBoxForPropName;
    }

    @Step("Get tab badge count for groups/users: {groups}...")
    public int getTabBadgeCount(String tabName) {
        final WebElement badgeLocator;
        switch (tabName.toLowerCase()) {
            case "groups":
                badgeLocator = groupsTabBadge;
                break;
            case "users":
                badgeLocator = usersTabBadge;
                break;
            default:
                throw new IllegalArgumentException("Unknown tab name: " + tabName);
        }
        try {
            final WebElement badge = waitTillVisibleWithFluentWait(badgeLocator);
            final String badgeText = getText(badge).replaceAll("\\D+", "");
            if (badgeText.isEmpty()) {
                return 0;
            }
            logger.info("Badge count for tab {}: {}", tabName, badgeText);
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            logger.warn("Unable to read badge count for tab {}", tabName, e);
            return 0;
        }
    }

    @Step("Get discard confirmation message text...")
    public String getDiscardConfirmationMessageText() {
        try {
            return discardConfirmationMessage.getText().trim();
        } catch (Exception e) {
            logger.warn("Discard confirmation message element not found: " + e.getMessage() + " Element: " + discardConfirmationMessage.toString());
            return null;
        }
    }

    @Step("Verify discard confirmation pop-up is displayed...")
    public boolean isDiscardConfirmationPopUpTitle() {
        return isElementVisible(discardPopupTitle);
    }

    @Step("Click on Discard Changes button...")
    public void clickDiscardChangesButton() {
        waitTillClickableWithFluentWait(discardChangesButton).click();
    }

    @Step("Click on Confirm Changes button...")
    public void clickConfirmChangesButton() {
        waitTillClickableWithFluentWait(confirmChangesButton).click();
    }

    @Step("Click on Discard Confirmation Pop-Up Cancel Button...")
    public void clickDiscardConfirmationPopUpCancelButton() {
        waitTillClickableWithFluentWait(discardConfirmationPopUpCancelButton).click();
    }

    @Step("Click on Discard Confirmation Pop-Up Discard Changes Button...")
    public void clickDiscardConfirmationPopUpDiscardChangesButton() {
        waitTillClickableWithFluentWait(discardConfirmationPopUpDiscardChangeButton).click();
    }

    @Step("Click dropdown option for tab: {tabName}...")
    public void clickOnDropDownButtonForGroupUserTab(String tabName) {
        String dropdown = dropdownOptionForGroupUserTab.replace("{tabname}", tabName);
        WebElement dropdownButton = findElementWithFluentWait(By.xpath(dropdown));
        click(dropdownButton);
    }

    @Step("Is DropDown Option '{optionname}' disabled for tab: {tabName}?")
    public boolean isDropDownOptionDisable(String tabName, String optionname) {
        String dropdown = dropDownOptionDisable.replace("{tabname}", tabName).replace("{optionname}", optionname);
            WebElement dropdownButton = findElementWithFluentWait(By.xpath(dropdown));
            return dropdownButton.isDisplayed();
    }

    @Step("Select dropdown value '{optionname}' for tab: {tabName}...")
    public void selectDropDownValue(String tabName, String optionname) {
        String dropdown = dropDownOption.replace("{tabname}", tabName).replace("{optionname}", optionname);
        WebElement dropdownButton = findElementWithFluentWait(By.xpath(dropdown));
        click(dropdownButton);
    }

    @Step("Check if 'All Rows Selection' checkbox is checked...")
    public boolean isAllRowsSelectionChecked() {
        try {
            waitForElementToBeVisible(allRowsSelection);
            return allRowsSelection.isDisplayed();
        } catch (Exception e) {
            logger.warn("element not found or not interactable: " + e.getMessage() + " Element: " + allRowsSelection.toString());
            return false;
        }
    }

    @Step("Check if 'Select All Properties' checkbox is visible...")
    public boolean isSelectAllPropertiesCheckBoxVisible(String colId) {
        try {
            WebElement checkBox = waitForElementToBePresentFluentWait(By.xpath(selectAllPropertiesCheckBox.replace("{colId}", colId)));
            waitForElementToBeVisible(checkBox);
            return checkBox.isDisplayed();
        } catch (Exception e) {
            logger.warn("Select All Properties CheckBox is not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify Permissions tab is active...")
    public boolean isPermissionsTabActive() {
        try {
            return isElementVisible(activePermissionTab);
        } catch (Exception e) {
            logger.warn("Permissions tab is not active");
            return false;
        }
    }

    @Step("Verify Details tab is active...")
    public boolean isDetailsTabActive() {
        try {
            return isElementVisible(activeDetailsTab);
        } catch (Exception e) {
            logger.warn("Details tab is not active");
            return false;
        }
    }


    @Step("Verify column is displayed...")
    public boolean isColumnHeaderPresent(String name, PageModelName pageModelName) {
        try {
            logger.info("Checking if column is displayed: {}", name);
            String columnHeaderLocator = getDataTestId(pageModelName) + columnHeader.replace("{colName}", name);
            logger.info("Looking for column header with locator: {}", columnHeaderLocator);
            final WebElement column = waitForElementToBePresent(By.xpath(columnHeaderLocator));
            scrollToElement(column);
            final Actions action = new Actions(DriverFactory.getBrowserInstance());
            action.sendKeys(Keys.ARROW_RIGHT).perform();
            return isElementVisible(column);
        } catch (WebDriverException e) {
            logger.info(name + " column is not visible...");
            return false;
        }
    }

    @Step("Click on column header with colId: {colId} in pageModel: {pageModelName}...")
    public void clickOnColumnHeader(PageModelName pageModelName, String colId) {
        final String columnHeaderLocator = getDataTestId(pageModelName) + columnHeaderName.replace("{colId}", colId);
        logger.info("Clicking on column header with locator: {}", columnHeaderLocator);
        final WebElement columnHeaderElement = waitForElementToBePresent(By.xpath(columnHeaderLocator));
        waitTillClickableWithFluentWait(columnHeaderElement).click();
        logger.info("Clicked on column header with colId: {}", colId);
    }

    @Step("Get column header name cell elements in fresh-DOM order for colId: {colId} in pageModel: {pageModelName}...")
    public WebElement getColumnHeadersNameCellsValue(PageModelName pageModelName, String colId) {

        switch (colId)
        {
            case "name": {
                final String freshLocator = getDataTestId(pageModelName) + columnHeadersPropertyNameCellsValue.replace("{colId}", colId);
                final WebElement element = driver.findElement(By.xpath(freshLocator));
                logger.info("Getting column header name cell value with locator: {}", freshLocator);
                return element;
            }
            case "value_type", "targetTitle", "description", "suffix", "propagate_value", "default_value":
            {
                final String freshLocator = getDataTestId(pageModelName) + columnHeadersPropertyCellsValue.replace("{colId}", colId);
                final WebElement element = driver.findElement(By.xpath(freshLocator));
                logger.info("Getting column header name cell value with locator: {}", freshLocator);
                return element;
            }
            default:
                throw new IllegalArgumentException("Unknown colId: " + colId);
        }
    }

    @Step("Get column header name cell text values in visual order for colId: {colId} in pageModel: {pageModelName}...")
    public String getColumnHeadersNameCellsValueText(PageModelName pageModelName, String colId) {
        final WebElement cellElements = getColumnHeadersNameCellsValue(pageModelName, colId);
            final String text = cellElements.getText().trim();
        return text;
    }

    @Step("Get all cell values for colId: {colId} in pageModel: {pageModelName}...")
    public List<String> getColumnHeadersPropertyCellsValues(PageModelName pageModelName, String colId) {
        final String freshLocator = getDataTestId(pageModelName) + columnHeadersPropertyCellsValues.replace("{colId}", colId);
        logger.info("Getting all cell values for colId: {} with locator: {}", colId, freshLocator);
        final List<WebElement> cellElements = driver.findElements(By.xpath(freshLocator));
        final List<String> cellValues = new ArrayList<>();
        for (WebElement cell : cellElements) {
            final String text = cell.getText().trim();
            if (!text.isEmpty()) {
                cellValues.add(text);
            }
        }
        logger.info("Cell values for colId: {} are: {}", colId, cellValues);
        return cellValues;
    }

    @Step("Get number of checked input wrapper results...")
    public int getNumberOfCheckedInputWrapperResults() {
        return checkedInputWrappers != null ? checkedInputWrappers.size() : 0;
    }

    @Step("Get number of {name} present in the list...")
    public int getNumberOfGroupOrUserPresent(String name) {
        String xpath = GroupUserList.replace("{name}", name);
        List<WebElement> elements = findElementsWithFluentWait(By.xpath(xpath));
        return elements.size();
    }

    public boolean isAddPropertyButtonPresentAndEnabled() {
        return isElementEnabled(addButton);
    }

    public boolean isCancelButtonPresent() {
        return isElementVisible(cancelButton);
    }

    public boolean isCloseButtonPresent() {
        return isElementVisible(xButtonOnPropertiesPopUp);
    }

    @Step("Click on X button on Properties Pop-Up...")
    public void clickXButtonOnPropertiesPopUp() {
        waitTillClickableWithFluentWait(xButtonOnPropertiesPopUp).click();
    }

    public boolean isPropertyAdded(String propName) {
        try {
            String propertyXpath = xpathPropertyRow.replace("{propName}", propName);
            WebElement property = waitForElementToBePresent(By.xpath(propertyXpath));
            return isElementVisible(property);
        } catch (Exception e) {
            logger.warn("Property: " + propName + " is not added to the property set. Exception: " + e.getMessage());
            return false;
        }
    }

    @Step("Get column headers in order for Property Set modal...")
    public List<String> getColumnHeadersInOrder(PageModelName pageModelName, String columnName) {
        final List<String> columnHeaders = new ArrayList<>();
        final String columnHeadersLocator = getDataTestId(pageModelName) + columeHeaderCountsPropSet;
        final Actions action = new Actions(DriverFactory.getBrowserInstance());

        boolean targetColumnFound = false;
        int maxScrollAttempts = 30; // Prevent infinite loop
        int scrollCount = 0;

        // Scroll horizontally and collect unique headers until we find the last column name
        while (!targetColumnFound && scrollCount < maxScrollAttempts) {
            List<WebElement> headers = driver.findElements(By.xpath(columnHeadersLocator));
            for (WebElement header : headers) {
                String headerText = header.getText().trim();
                if (!headerText.isEmpty() && !columnHeaders.contains(headerText)) {
                    columnHeaders.add(headerText);
                    if (headerText.equals(columnName)) {
                        targetColumnFound = true;
                        break;
                    }
                }
            }
            // scroll right till last column name is visible
            if (!targetColumnFound) {
                action.sendKeys(Keys.ARROW_RIGHT).perform();
                scrollCount++;
            }
        }
        return columnHeaders;
    }

    @Step("Verify Add Property button is enabled")
    public boolean isAddPropertyButtonEnabled() {
        try {
            if (isElementEnabled(addButtonDetailsPagePropSet)) {
                logger.warn("Add Property Button is displaying for {} element", addButtonDetailsPagePropSet);
                return true;
            }
        } catch (Exception e) {
            logger.info("Add Property Button is not displaying for {} element", addButtonDetailsPagePropSet);
        }
        return false;
    }

    @Step("Check if 'No results found' message is displayed...")
    public boolean isNoResultsFoundMessageDisplayed() {
        try {
            return isElementVisible(noResultsFoundMessage);
        } catch (Exception e) {
            logger.warn("No results found message element not found or not visible: " + e.getMessage());
            return false;
        }
    }

    @Step("Click on Show more button")
    public void clickOnShowMoreButton(PageModelName pageModelName, String columnName) {
        hoverOverElement(findElementWithWait(By.xpath(getDataTestId(pageModelName) + descriptionProperty.replace("{name}", columnName))));
        WebElement showMore = findElementWithWait(By.xpath(getDataTestId(pageModelName) + showMoreButton));
        click(showMore);
    }

    @Step("Get description value displayed in window popup...")
    public String getDescriptionValueFromPopUp() {
        String popUpText = getText(largeTextPopUpText).trim();
        logger.info("Description value in pop-up: {}", popUpText);
        return popUpText;
    }

    @Step("Click on Large Text PopUp Close Button...")
    public void clickOnLargeTextPopUpCloseButton() {
        click(largeTextPopUpCloseButton);
        logger.info("Clicked on Large Text PopUp Close Button");
    }

    @Step("Click on Select All Properties CheckBox...")
    public void clickOnSelectAllPropertiesCheckBox(String colId) {
        WebElement checkBoxForSelectAllProperties = findElementWithWait(By.xpath(selectAllPropertiesCheckBox.replace("{colId}", colId)));
        waitTillClickableWithFluentWait(checkBoxForSelectAllProperties).click();
        logger.info("Clicked on Select All Properties CheckBox");
    }

    @Step("Count number CheckBox with state: {checkboxState}...")
    public int countNumberOfCheckBoxWithState(String checkboxState) {
        DriverFactory.sleep(1000);//Need for improvement
        List<WebElement> checkBox = findElementsWithFluentWait(By.xpath(numberOfCheckBoxStatusCount.replace("{checkboxState}", checkboxState)));
        int count = checkBox.size();
        logger.info("Number of CheckBox with state {}: {}", checkboxState, count);
        return count;
    }

    @Step("Get properties added badge count...")
    public int getPropertiesAddedBadgeCount() {
        String badgeText = getText(propertiesAddedBadge).trim();
        int count = Integer.parseInt(badgeText);
        logger.info("Properties added badge count: {}", count);
        return count;
    }

    @Step("Count number Un-CheckBox with state: {checkboxState}...")
    public int countNumberOfUnCheckBoxWithState(String checkboxState) {
        DriverFactory.sleep(1000);//Need for improvement
        List<WebElement> checkBox;
        if (checkboxState.equals("unchecked")) {
            checkBox = findElementsWithFluentWait(By.xpath(numberOfCheckBoxStatusCount.replace("{checkboxState}", checkboxState)));
        } else {
            checkBox = waitForElementsToBeVisible(numberOfUnCheckBoxCount);
        }
        int count = checkBox.size();
        logger.info("Number of CheckBox with state {}: {}", checkboxState, count);
        return count;
    }

    @Step("Verify that the no property overlay icon is visible...")
    public boolean isNoPropertyOverlayIconVisible() {
        try {
            if (noPropertyOverlayIcon.isDisplayed()) {
                logger.info("No property overlay icon is visible for element: {}", noPropertyOverlayIcon.toString());
                return true;
            }
        } catch (Exception e) {
            logger.warn("No property overlay icon is not visible: {}", e.getMessage());
        }
        return false;
    }

    @Step("Verify that the property label is visible...")
    public boolean isPropertyLabelVisible() {
        try {
            boolean visible = isElementVisible(propertyLabel);
            logger.info("Property label visible: {}", propertyLabel.toString());
            return visible;
        } catch (Exception e) {
            logger.warn("Property label is not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify that the 'Add Properties to the Property Set' overlay title is visible...")
    public boolean isAddPropertiesToPropertySetOverlayTitleVisible() {
        try {
            boolean visible = isElementVisible(addPropertiesToPropertySetOverlayTitle);
            logger.info("Add Properties overlay title visible: {}", visible);
            return visible;
        } catch (Exception e) {
            logger.warn("Add Properties overlay title is not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify that the Search Properties input field is disabled and visible...")
    public boolean isSearchPropertiesInputFieldDisabledVisible() {
        try {
            boolean visible = isElementVisible(searchPropertiesInputFieldDisabled);
            logger.info("Search Properties input field disabled visible: {}", visible);
            return visible;
        } catch (Exception e) {
            logger.warn("Search Properties input field disabled is not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify that the no rows overlay description is visible...")
    public boolean isNoRowsOverlayDescriptionVisible() {
        try {
            boolean visible = isElementVisible(noRowsOverlayDescription);
            logger.info("No rows overlay description visible: {}", visible);
            return visible;
        } catch (Exception e) {
            logger.warn("No rows overlay description is not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get text of properties help doc link...")
    public String getPropertiesHelpDocLinkText() {
        String rawText = getText(propertiesHelpDocLink);
        String cleanText = rawText.replaceAll("\\s+", " ").trim();
        logger.info("Properties help doc link text: {}", cleanText);
        return cleanText;
    }

    @Step("Click on properties help doc link and switch to new tab, return the URL...")
    public String clickHelpDocLinkAndSwitchToNewTab() {
        openLinkInNewTab(propertiesHelpDocLink);
        switchToOpenedTab();
        String url = getCurrentUrl();
        logger.info("Properties help doc link opened in new tab with URL: {}", url);
        return url;
    }

    @Step("Uncheck owner checkbox if it is already checked...")
    public void uncheckOwnerCheckboxIfChecked() {
        waitTillClickableWithFluentWait(userPermissionCheckboxOwnerInput);
        if (userPermissionCheckboxOwnerInput.isSelected()) {
            click(userPermissionCheckboxOwnerInput);
        }
    }

    @Step("Verify review update permissions warning message is displayed...")
    public boolean isReviewUpdatePermissionsWarningMessageDisplayed() {
        try {
            waitForElementToBeVisible(reviewUpdatePermissionsWarningMessage);
            return reviewUpdatePermissionsWarningMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get review update permissions warning message text...")
    public String getReviewUpdatePermissionsWarningMessageText() {
        try {
            waitForElementToBeVisible(reviewUpdatePermissionsWarningMessage);
            return reviewUpdatePermissionsWarningMessage.getText().trim();
        } catch (Exception e) {
           return "";
        }
    }

    @Step("Verify review update permissions permission Alert Icon is displayed...")
    public boolean ispermissionAlertIconDisplayed() {
        try {
            waitForElementToBeVisible(permissionAlertIcon);
            return permissionAlertIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Uncheck owner checkbox if it is already checked...")
    public void clickOnConfirmChangesButton() {
        waitTillClickableWithFluentWait(confirmChangesButton);
            clickWithJS(confirmChangesButton);
        DriverFactory.sleep(1000);  // sleep added to ensure stable
    }

    @Step("Uncheck owner checkbox if it is already checked...")
    public void clickOnContinueEditingButton() {
        waitTillClickableWithFluentWait(continueediting);
        clickWithJS(continueediting);
        DriverFactory.sleep(1000);  // sleep added to ensure stable
    }

    @Step("Uncheck owner checkbox if it is already checked...")
    public void checkOwnerCheckboxIfUnheckedOnGroup() {
        waitTillClickableWithFluentWait(groupPermissionCheckboxOwnerInput);
        if (!groupPermissionCheckboxOwnerInput.isSelected()) {
            click(groupPermissionCheckboxOwnerInput);
        }
    }

    @Step("Uncheck owner checkbox if it is already checked...")
    public void unCheckOwnerCheckboxIfCheckedOnGroup() {
        waitTillClickableWithFluentWait(groupPermissionCheckboxOwnerInput);
        if (groupPermissionCheckboxOwnerInput.isSelected()) {
            click(groupPermissionCheckboxOwnerInput);
        }
    }

    @Step("Get number of properties with pagemodal...")
    public int getNumberOfPropertiesWithPageModal(PageModelName pageModelName, int counter) {
        waitForPageLoaded();
        waitForUiModule();
        return waitForNumberOfElementsToBe(By.xpath( getDataTestId(pageModelName) + propertyRows), counter).size();
    }

    @Step("Verify owner checkbox for group is unchecked...")
    public boolean isOwnerCheckboxUncheckedOnGroup() {
        waitTillClickableWithFluentWait(groupPermissionCheckboxOwnerInput);
        return !groupPermissionCheckboxOwnerInput.isSelected();
    }

    @Step("Verify Property Set Is Protected Switch label is displayed...")
    public boolean isPropertySetIsProtectedSwitchLabelDisplayed() {
        return isElementVisible(propertySetIsProtectedSwitchLabel);
    }

    @Step("Verify Property Set Security label is displayed...")
    public boolean isPropertySetSecurityLabelDisplayed() {
        return isElementVisible(propertySetSecurityLabel);
    }

    @Step("Verify Property Set Protected icon is displayed...")
    public boolean isPropertySetProtectedIconDisplayed() {
        return isElementVisible(propertySetProtectedIcon);
    }

    @Step("Verify tooltip message for shield icon hint is displayed and correct...")
    public boolean isShieldIconHintTooltipDisplayedWithMessage(String expectedMessage) {
        try {
            hoverOverElement(infoIconForProtected);
            waitForElementToBeVisible(tooltipInner);
            String actualTooltip = tooltipInner.getText().trim();
            logger.info("Tooltip message displayed: {}", actualTooltip);
            return actualTooltip.equals(expectedMessage);
        } catch (Exception e) {
            logger.warn("Tooltip for shield icon hint not displayed or message mismatch: {}", e.getMessage());
            return false;
        }
    }

    @Step("Check if 'Allow Write on Target Read' checkbox is hidden...")
    public boolean isAllowWriteOnTargetReadCheckboxHidden() {
        try {
            // Check for hidden div (display: none)
            List<WebElement> hiddenDivs = driver.findElements(By.xpath(allowWriteOnTargetReadHiddenDivXpath));
            if (!hiddenDivs.isEmpty()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.info("'Allow Write on Target Read' checkbox hidden div not found or not hidden: {}", e.getMessage());
            return false;
        }
    }

    @Step("Check if 'Allow Write on Target Read' checkbox label is displayed...")
    public boolean isAllowWriteOnTargetReadCheckboxLabelDisplayed() {
        try {
            waitForElementToBeVisible(allowWriteOnTargetReadCheckboxLabel);
            return allowWriteOnTargetReadCheckboxLabel.isDisplayed();
        } catch (Exception e) {
            logger.warn("'Allow Write on Target Read' checkbox label not found or not visible: " + e.getMessage());
            return false;
        }
    }

    @Step("Verify tooltip message for shield icon hint is displayed and correct...")
    public boolean isInfoIconTooltipDisplayedWithMessageForAllowWrite(String expectedMessage) {
        try {
            hoverOverElement(allowWriteOnTargetReadInfoIcon);
            waitForElementToBeVisible(tooltipInner);
            String actualTooltip = tooltipInner.getText().trim();
            logger.info("Tooltip message displayed: {}", actualTooltip);
            return actualTooltip.equals(expectedMessage);
        } catch (Exception e) {
            logger.warn("Tooltip for shield icon hint not displayed or message mismatch: {}", e.getMessage());
            return false;
        }
    }

    @Step("Check if Permissions tab is disabled or displayed...")
    public boolean isPermissionsTabDisabledOrDisplayed() {
        try {
            waitForElementToBeVisible(permissionsTabDisabled);
            return permissionsTabDisabled.isDisplayed();
        } catch (Exception e) {
            logger.info("Permissions tab (disabled) is not displayed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Select 'Allow Write on Target Read' checkbox if not already selected...")
    public void selectAllowWriteOnTargetReadCheckbox() {
        waitTillClickableWithFluentWait(permissionsTabAllowWriteOnTargetReadCheckboxInput);
        if (!permissionsTabAllowWriteOnTargetReadCheckboxInput.isSelected()) {
            click(permissionsTabAllowWriteOnTargetReadCheckboxInput);
        }
    }

    @Step("Check if 'Protected Switch' is disabled...")
    public boolean isProtectedSwitchDisabled() {
        try {
            waitForElementToBeVisible(securityToggleSwitch);
            // Check both displayed and disabled state
            boolean isDisplayed = securityToggleSwitch.isDisplayed();
            boolean isDisabled = !protectedSwitchDisabled.isEnabled();
            logger.info("Protected switch displayed: {}, disabled: {}", isDisplayed, isDisabled);
            return isDisplayed && isDisabled;
        } catch (Exception e) {
            logger.info("Protected switch (disabled) is not displayed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Check if 'Remove Protection' confirmation modal is displayed...")
    public boolean isRemoveProtectionConfirmationPopupDisplayed() {
        try {
            waitForElementToBeVisible(removeProtectionConfirmationPopupTitle);
            return removeProtectionConfirmationPopupTitle.isDisplayed();
        } catch (Exception e) {
            logger.info("Remove Protection confirmation modal not displayed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Click on close button of Remove Protection confirmation popup...")
    public void clickClosePopupRemoveProtectionConfirmationPopup() {
        try {
            waitTillClickableWithFluentWait(closePopupRemoveProtectionConfirmationPopup);
            clickWithJS(closePopupRemoveProtectionConfirmationPopup);
            logger.info("Clicked on close button of Remove Protection confirmation popup.");
        } catch (Exception e) {
            logger.warn("Could not click close button on Remove Protection confirmation popup: {}", e.getMessage());
        }
    }

    @Step("Click on Remove Protection popup option: {0} ...")
    public void clickRemoveProtectionPopupOption(String popupOptionName) {
        String selector = removeProtectionPopupOptions.replace("{popupOptionName}", popupOptionName);
        WebElement button = driver.findElement(By.cssSelector(selector));
        waitTillClickableWithFluentWait(button);
        button.click();
    }

    @Step("Get text from Remove Protection confirmation popup message box...")
    public String getRemoveProtectionConfirmationPopupMessageText() {
        try {
            waitForElementToBeVisible(messageBoxContentRemoveProtectionConfirmationPopup);
            return messageBoxContentRemoveProtectionConfirmationPopup.getText().trim();
        } catch (Exception e) {
            logger.info("Could not get message text from Remove Protection confirmation popup: {}", e.getMessage());
            return "";
        }
    }

    @Step("clear Text search box For Group")
    public void ClearSearchTextBoxForGroup() {
        waitForElementToBeVisible(searchGroupsButton);
        clearInputFieldWithBackspace(searchGroupsButton);
    }

    @Step("Get message...")
    public String getMessageText() {
        return waitForElementToBeVisible(noResultsFound).getText();
    }

    @Step("Verify that custom Icon displayed in property set...")
    public boolean isCustomIconDisplayedInPropertySet(String propertySetName, String iconName) {
        String customIconXpath = xpathCustomIconInPropertySet.replace("{iconName}", iconName).replace("{propSet}", propertySetName);
        try {
            WebElement customIcon = waitForElementToBePresent(By.xpath(customIconXpath));
            logger.info("Custom icon '{}' is visible in property set '{}'.", iconName, propertySetName);
            return customIcon.isDisplayed();
        } catch (Exception e) {
            logger.warn("Custom icon '{}' is not visible in property set '{}': {}", iconName, propertySetName, e.getMessage());
            return false;
        }
    }

    @Step("Verify that default Icon displayed in property set...")
    public boolean isDefaultIconDisplayedInPropertySet(String propertySetName) {
        String defaultIconXpath = xpathDefaultIconInPropertySet.replace("{propSet}", propertySetName);
        try {
            WebElement defaultIcon = waitForElementToBePresent(By.xpath(defaultIconXpath));
            logger.info("Default icon is visible in property set '{}'.", propertySetName);
            return defaultIcon.isDisplayed();
        } catch (Exception e) {
            logger.warn("default icon is not visible in property set '{}'. {}", propertySetName, e.getMessage());
            return false;
        }
    }

    @Step("Verify that shield Icon displayed in property set...")
    public boolean isShieldIconDisplayedInPropertySet(String propertySetName) {
        String shieldIconXpath = xpathShieldIconInPropertySet.replace("{propSet}", propertySetName);
        try {
            WebElement shieldIcon = waitForElementToBePresent(By.xpath(shieldIconXpath));
            logger.info("Shield icon is visible in property set '{}'.", propertySetName);
            return shieldIcon.isDisplayed();
        } catch (Exception e) {
            logger.warn("Shield icon is not visible in property set '{}': {}", propertySetName, e.getMessage());
            return false;
        }
    }
}
