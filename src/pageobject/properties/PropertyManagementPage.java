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
import static com.methodics.phi.util.CommonUrls.PROPERTY_MANAGEMENT_PAGE;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PropertyManagementPage extends BasePage {
    private WebDriver driver;
    private static final String PROPERTY_MANAGEMENT_TITLE = "Properties";

    public PropertyManagementPage(WebDriver driver) {
        this.driver = driver;

        if (driver != null) {
            driver.get(DriverFactory.getFullUrl(PROPERTY_MANAGEMENT_PAGE));
        }

        waitForTitle(PROPERTY_MANAGEMENT_TITLE);
        if (!driver.getTitle().contains("Properties")) {
            throw new IllegalStateException(
                    "This is not Properties Page, current page is: " + driver.getTitle());
        }
    }

    @FindBy(xpath = "//button[normalize-space()='Create new Property']")
    private WebElement createNewPropertyButton;

    @FindBy(xpath = "//input[@id='target_type']/ancestor::div[@class='multiselect__tags']")
    private WebElement targetTypeDropdown;

    @FindBy(css = ".multiselect--disabled.multiselect:has(input#target_type)")
    private WebElement targetTypeSelectorDisabled;

    @FindBy(xpath = "//input[@id='value_type']/ancestor::div[@class='multiselect__tags']")
    private WebElement propTypeDropdown;

    @FindBy(css = ".multiselect--disabled.multiselect:has(input#value_type)")
    private WebElement valueTypeSelectorDisabled;

    @FindBy(css = "input[data-testid='ui-in-property_name_field']")
    private WebElement propNameInputField;

    @FindBy(xpath = "//textarea[@placeholder='Enter description']")
    private WebElement propDescriptionInputField;

    @FindBy(xpath = "//div[contains(@class,'vue-daterange-picker')]")
    private WebElement datepicker;

    @FindBy(xpath = "//td[contains(@class,'today')]")
    private WebElement todayDate;

    @FindBy(xpath = "//td[contains(@class,'today')]/following::td")
    private WebElement calendarDayAfter;

    @FindBy(xpath = "//button[normalize-space()='Apply']")
    private WebElement applyButton;

    @FindBy(xpath = "//div[@class='date_input']//i[contains(@class,'fa fa-times-circle')]")
    private WebElement clearDateButton;

    @FindBy(xpath = "//button[@data-testid='ui-btn-property-editor-save']")
    private WebElement saveButton;

    @FindBy(css = "button[data-testid='ui-btn-property-editor-save'][disabled]")
    private WebElement saveButtonDisabled;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(css = "button[data-testid='ui-btn-property-editor-delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//input[@id='default_value']")
    private WebElement defaultValueField;

    @FindBy(xpath = "//input[@type='checkbox']/following-sibling::label[normalize-space()='Default value']")
    private WebElement defaultValueCheckBox;

    @FindBy(xpath = "//input[not(@aria-checked)]/following-sibling::label[normalize-space()='Default value']")
    private WebElement booleanValueCheckBoxNotChecked;

    @FindBy(xpath = "//input[@type='radio' and @value='true']/following-sibling::label")
    private WebElement trueRadioButton;

    @FindBy(css = "input[data-testid='ui-radio-true-input'][value='true'][checked]")
    private WebElement trueRadioButtonChecked;

    @FindBy(xpath = "//input[@type='radio' and @value='false']/following-sibling::label")
    private WebElement falseRadioButton;

    @FindBy(css = "input[data-testid='ui-radio-false-input'][value='false'][checked]")
    private WebElement falseRadioButtonChecked;

    @FindBy(xpath = "//input[@placeholder='Search Properties']")
    private WebElement searchPropertiesInputField;

    @FindBy(xpath = "//input[@type='checkbox']/following-sibling::label[normalize-space()='Multi-choice enabled']")
    private WebElement multiChoiceCheckbox;

    @FindBy(xpath = " //input[@type='checkbox' and @disabled]/following-sibling::label[normalize-space()='Multi-choice enabled']")
    private WebElement multiChoiceCheckboxDisabled;

    @FindBy(css = "input[data-testid='property-editor-multi-choice-input'][checked]")
    private WebElement multiCheckboxSelected;

    @FindBy(xpath = "//input[not(@aria-checked)]/following-sibling::label")
    private WebElement multiCheckboxNotChecked;

    @FindBy(xpath = "//input[@placeholder='Add options']")
    private WebElement optionsInputField;

    @FindBy(xpath = "//i[@class='fa fa-plus']")
    private WebElement addOptionButton;

    @FindBy(xpath = "//input[@id='prop_suffix']")
    private WebElement suffixInputField;

    @FindBy(css = "li[data-testid='ui-list-group-item']")
    private List<WebElement> options;

    @FindBy(xpath = "//div[contains(@class,'props-manage__info-description')]")
    private WebElement descriptionInfo;

    @FindBy(xpath = "//span[normalize-space()='Create a Property or select a Property to edit.']")
    private WebElement createPropertyHeader;

    @FindBy(xpath = "//i[contains(@class,'fa-circle-xmark')]")
    private WebElement cancelSearchButton;

    @FindBy(xpath = "//span[contains(@class, 'panel-grid-results__quantity')]")
    private WebElement numberOfSetsIcon;

    @FindBy(xpath = "//div[contains(@class,'text-center') and contains(., 'No Property Sets have added this Property yet.')]")
    private WebElement noPropertySetAddedMssg;

    @FindBy(css = "div[data-testid='property-sets-table-grid'] div[class*='ag-cell-value']")
    private List<WebElement> propertySets;

    @FindBy(css = "div[aria-checked='true']:has(> input[data-testid='property-editor-propagate-input'])")
    private WebElement propagateToggleEnabled;

    @FindBy(css = "div[class*='tooltip-inner'")
    private WebElement propagateToggleTooltip;

    @FindBy(xpath = "//div[contains(@class,'page-name')][normalize-space()='Properties']")
    private WebElement propertySubHeader;

    @FindBy(xpath = "//div[contains(@class,'info-description')]//i[contains(@class,'object-ungroup')]")
    private WebElement pageIcon;

    @FindBy(xpath = "//button[@data-testid='ui-btn-ui-list-group-item']//*[contains(@class,'icon')]")
    private WebElement propertyIcon;

    @FindBy(xpath = "//div[contains(.,'No Properties found.')][contains(@class, 'text-center')]")
    private WebElement noPropertiesFound;

    @FindBy(xpath = "//*[text()='Created']//ancestor::div[contains(@class,'toast-header')]//button[@data-testid='ui-btn-toast-dismiss']")
    private WebElement createdPopUpCloseButton;

    private String xpathCheckboxChecked = "//*[@id='choice_check_{name}'][contains(@class, 'success')]";
    private String xpathCheckboxUnchecked = "//button[@data-testid='ui-btn-choice-selector-set-default']//*[@id='choice_check_{name}']";
    private String xpathDeleteChoiceOption = "//li[normalize-space()='{name}' and @data-testid='ui-list-group-item']//i[contains(@class, 'text-danger')]";
    private String xpathDropdownChoice = "//li[@class='multiselect__element']/span[normalize-space()='{type}']";
    private String xpathPropertyType = "//div[@title='{name}']/following-sibling::div[contains(@class, 'type')][normalize-space()='{propType}']";
    private String xpathProperty = "//div[@title=\"{name}\"][contains(@class,'text-truncate')]";
    private String xpathSuffix = "//a[@class='text-info text-decoration-none' and text()='{text}']";
    private String xpathPropSetName = "//div[@role='gridcell']//span[@title='{name}'][normalize-space()='{name}']";
    private String cssPropertyHeader = "div.col.d-flex.align-items-center.text-truncate.page-name[data-testid='ui-col'] span[title='{name}']";
    private String propertySetsCountBadge = "//div[@title='{name}']/following-sibling::*//span[@data-testid='ui-badge']";
    private String propagateToggleButton = "//input[@id='propagate_value' and not(@aria-checked)]/parent::div";
    private String xpathProperties = "//div[contains(@class,'d-flex')]//div[contains(@class, 'prop-name')]";
    private String gridTitle = "//div[contains(@class,'text-truncate')]//span[@title='Property Sets that include the {name} Property.']";
    private String propertyWithCustomIcon = "//button[@data-testid='ui-btn-ui-list-group-item']//following::div//i[contains(@data-testid, '{customIconProperty}-custom-icon')]";
    private String propertyWithDefaultIcon = "//button[@data-testid='ui-btn-ui-list-group-item']//following::div[contains(@data-testid, '{defaultIconProperty}-default-icon')]";
    private String propertySetWithCustomIcon = "//a[contains(text(), '{propertySetName}')]//child::i[contains(@data-testid, 'custom-icon') and contains(@class, '{iconName}')]";
    private String propertySetWithDefaultIcon = "//a[contains(text(), '{propertySetName}')]//child::div[contains(@data-testid, 'default-icon') and contains(@class, '{iconName}')]";
    private String protectedIconForPropertySet = "//a[contains(text(), '{propertySetName}')]//child::i[contains(@data-testid, 'protected-icon')]";

    @Step("Click on Create New Property button...")
    public void clickOnCreateNewPropertyButton() {
        waitTillClickableWithFluentWait(createNewPropertyButton).click();
    }

    @Step("Select property target type...")
    public void selectTargetType(String targetType) {
        click(targetTypeDropdown);
        WebElement propTargetType = findElementWithWait(By.xpath(xpathDropdownChoice.replace("{type}", targetType)));
        click(propTargetType);
    }

    @Step("Enter property name: {0}, for the method: {method}...")
    public String enterPropertyName(String propName) {
        waitTillVisibleWithFluentWait(propNameInputField).click();
        clearTextByBackspace(propNameInputField);
        inputText(propNameInputField, propName);
        String fullPropertyName = propNameInputField.getAttribute("value");
        return fullPropertyName;
    }

    @Step("Select property type...")
    public void selectPropertyType(String propType) {
        waitTillVisibleWithFluentWait(propTypeDropdown).click();
        WebElement propertyType = findElementWithFluentWait(By.xpath(xpathDropdownChoice.replace("{type}", propType)));
        click(propertyType);
    }

    @Step("Specify default datetime property value...")
    public void selectDefaultDateValue() {
        clickOnCalendarIcon();
        selectDay();
        clickOnApplyButton();
    }

    @Step("Specify default property value...")
    public void enterDefaultValue(String value) {
        clearInputFieldWithBackspace(defaultValueField);
        inputText(defaultValueField, value);
    }

    @Step("Enter property description: {0}, for the method: {method}...")
    public void enterPropertyDescription(String propDescription) {
        inputText(propDescriptionInputField, propDescription);
    }

    @Step("Click on Save button...")
    public void clickOnSaveButton() {
        saveButton.click();
        DriverFactory.sleep(1500);//Need for Improvement
    }

    @Step("Click on Create New Property and enter general info...")
    public void clickCreateNewPropertyAndAddGeneralInfo(String targetType, String propName, String propType) {
        clickOnCreateNewPropertyButton();
        selectTargetType(targetType);
        enterPropertyName(propName);
        selectPropertyType(propType);
    }

    @Step("Clear property name...")
    public void clearPropertyName() {
        clearInputFieldWithBackspace(propNameInputField);
    }

    @Step("Verify Save button is disabled...")
    public boolean isSavePropertyButtonDisabled() {
        return isElementVisible(saveButtonDisabled);
    }

    @Step("Get the name of the properties with name: {0}...")
    public int getNumberOfPropertiesByName(String propName) {
        DriverFactory.sleep(500);
        return driver.findElements(By.xpath(xpathProperty.replace("{name}", propName))).size();
    }

    @Step("Get number of properties...")
    public int getNumberOfProperties(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathProperties), counter).size();
    }

    @Step("Select property from the properties list...")
    public void clickOnProperty(String propName) {
        waitForPageLoaded();
        WebElement property = findElementWithWait(By.xpath(xpathProperty.replace("{name}", propName)));
        scrollToElement(property);
        click(property);
    }

    @Step("Click on true radio button...")
    public void selectTrueValue() {
        click(trueRadioButton);
    }

    @Step("Click on false radio button...")
    public void selectFalseValue() {
        click(falseRadioButton);
    }

    @Step("Check Default Value check box")
    public void checkDefaultValueCheckBox() {
        click(defaultValueCheckBox);
    }

    @Step("Verify true radio button is checked...")
    public boolean isTrueRadioButtonChecked() {
        return isElementVisible(trueRadioButtonChecked);
    }

    @Step("Verify false radio button is checked...")
    public boolean isFalseRadioButtonChecked() {
        return isElementVisible(falseRadioButtonChecked);
    }

    @Step("Click on the calendar icon...")
    public void clickOnCalendarIcon() {
        click(datepicker);
    }

    @Step("Select today's date...")
    public void selectDay() {
        click(todayDate);
    }

    @Step("Select the next day...")
    public void selectNextDay() {
        click(calendarDayAfter);
    }

    @Step("Click On Apply button...")
    public void clickOnApplyButton() {
        click(applyButton);
    }

    @Step("Clear datetime property value...")
    public void clearDateField() {
        click(clearDateButton);
    }

    @Step("Get property name ...")
    public String getPropertyName() {
        return getAttribute(propNameInputField);
    }

    @Step("Get property target type...")
    public String getTargetType() {
        return getText(targetTypeDropdown);
    }

    @Step("Get property value type...")
    public String getValueType() {
        return getText(propTypeDropdown);
    }

    @Step("Get property description field value...")
    public String getPropertyDescription() {
        return getAttribute(propDescriptionInputField);
    }

    @Step("Get default property value...")
    public String getPropDefaultValue() {
        return getAttribute(defaultValueField);
    }

    @Step("Get datetime property default value...")
    public String getDateDefaultValue() {
        return getText(datepicker);
    }

    @Step("Clear property default value field...")
    public void clearDefaultValueField() {
        clearInputField(defaultValueField);
    }

    @Step("Update property default value field...")
    public void updatePropValue(String propName) {
        clickOnProperty(propName);
        clickOnCalendarIcon();
        selectNextDay();
        clickOnApplyButton();
        clickOnSaveButton();
    }

    @Step("Enter a property name to the search input field...")
    public void enterPropertyNameToSearchField(String propName) {
        inputText(searchPropertiesInputField, propName);
    }

    @Step("Check multiple choice checkbox...")
    public void enableMultiChoiceOption() {
        click(multiChoiceCheckbox);
    }

    @Step("Add new choice option...")
    public void addChoiceOptions(String... args) {
        for (String option : args) {
            inputText(optionsInputField, option);
            click(addOptionButton);
        }
    }

    @Step("Verify multi choice checkbox is checked...")
    public boolean isMutliChoiceSelected() {
        return isElementVisible(multiCheckboxSelected);
    }

    @Step("Verify multi choice checkbox is not checked...")
    public boolean isMutliChoiceUnselected() {
        return isElementVisible(multiCheckboxNotChecked);
    }

    @Step("Verify multi choice is disabled...")
    public boolean isMutliChoiceCheckboxDisabled() {
        return isElementVisible(multiChoiceCheckboxDisabled);
    }

    @Step("Verify choice is set as a default value...")
    public boolean isChoiceSetAsDefault(String choice) {
        WebElement choice_option = findElementWithWait(By.xpath(xpathCheckboxChecked.replace("{name}", choice)));
        return isElementVisible(choice_option);
    }

    @Step("Verify choice is not set to default...")
    public boolean isChoiceOptionNotSelected(String choice) {
        WebElement choice_option = findElementWithWait(By.xpath(xpathCheckboxUnchecked.replace("{name}", choice)));
        return isElementVisible(choice_option);
    }

    @Step("Mark choice options as default values...")
    public void selectChoicesAsDefault(String... options) {
        for (String option : options) {
            WebElement checkbox = findElementWithWait(By.xpath(xpathCheckboxUnchecked.replace("{name}", option)));
            click(checkbox);
        }
    }

    @Step("Click on choice option checkbox to unselect default value...")
    public void unselectDefaultChoiceOption(String choice) {
        WebElement checkbox = findElementWithWait(By.xpath(xpathCheckboxChecked.replace("{name}", choice)));
        click(checkbox);
    }

    @Step("Delete choice property option...")
    public void removeChoiceOption(String option) {
        WebElement deleteButton = findElementWithWait(By.xpath(xpathDeleteChoiceOption.replace("{name}", option)));
        waitForElementToBeClickable(deleteButton).click();
        DriverFactory.sleep(1000);//Need to Improvement
    }

    @Step("Verify delete choice button is disabled...")
    public int isDeleteOptionButtonDisabled(String option, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathDeleteChoiceOption.replace("{name}", option)), counter).size();
    }

    @Step("Get number of the choice options...")
    public int getNumberOfChoiceOptions() {
        DriverFactory.sleep(500);
        return getNumberOfElements(options);
    }

    @Step("Enter property suffix: {0}, for the method: {method}...")
    public void enterPropertySuffix(String suffix) {
        inputText(suffixInputField, suffix);
    }

    @Step("Click on property suffix: {0}, for the method: {method}...")
    public void selectPropertySuffix(String suffix) {
        WebElement propSuffix = driver.findElement(By.xpath(xpathSuffix.replace("{text}", suffix)));
        click(propSuffix);
    }

    @Step("Get property suffix field value...")
    public String getPropertySuffix() {
        return getAttribute(suffixInputField);
    }

    @Step("Get page description...")
    public String getPageDescription() {
        return getText(descriptionInfo);
    }

    @Step("Verify page description is present...")
    public boolean isPageDescriptionPresent() {
        return isElementVisible(descriptionInfo);
    }

    @Step("Click on Cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Verify create new property info message is displayed...")
    public boolean isCreateNewPropertyInfoMessageDisplayed() {
        return isElementVisible(createPropertyHeader);
    }

    @Step("Verify property target type selector is disabled...")
    public boolean isPropertyTargetTypeDropdownDisabled() {
        return isElementVisible(targetTypeSelectorDisabled);
    }

    @Step("Verify property value type selector is disabled...")
    public boolean isPropertyValueTypeDropdownDisabled() {
        return isElementVisible(valueTypeSelectorDisabled);
    }

    @Step("Verify target type is displayed next to property: {0}...")
    public boolean isCorrectTypeDisplayedNextToProperty(String propName, String propType) {
        WebElement property = driver.findElement(By.xpath(xpathPropertyType.replace("{name}", propName).replace("{propType}", propType)));
        return isElementVisible(property);
    }

    @Step("Remove property name from the search field...")
    public void removePropertyNameFromSearchField() {
        clearTextByBackspace(searchPropertiesInputField);
    }

    @Step("Remove property name from the search field by clicking on cancel button...")
    public void removePropertyNameFromSearchFieldByClickingCancelButton() {
        waitForElementToBeVisible(cancelSearchButton);
        click(cancelSearchButton);
    }

    @Step("Click on property set: {0}...")
    public void clickOnPropertySet(String propSetName) {
        WebElement propertySet = driver.findElement(By.xpath(xpathPropSetName.replace("{name}", propSetName)));
        click(propertySet);
    }

    @Step("Get number of property sets from the icon...")
    public String getNumberOfPropertySetsFormIcon() {
        return getText(numberOfSetsIcon);
    }

    @Step("Get number of property sets on badge...")
    public String getPropertySetsBadgeCount(String propertyName) {
        WebElement badge = driver.findElement(By.xpath(propertySetsCountBadge.replace("{name}", propertyName)));
        return getText(badge);
    }

    @Step("Verify property set badge is not displayed")
    public boolean noPropertySetBadgeDisplayed(String propName) {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(propertySetsCountBadge.replace("{name}", propName)))) == 0;
    }

    @Step("Verify 'No properties added yet' is displayed")
    public boolean isNoPropertySetsMessageDisplayed() {
        return isElementVisible(noPropertySetAddedMssg);
    }

    @Step("Verify 'No properties found' is displayed")
    public boolean isNoPropertiesFoundMessageDisplayed() {
        return isElementVisible(noPropertiesFound);
    }

    @Step("Get number of property sets...")
    public int getNumberOfPropertySets() {
        return getNumberOfVisibleElements(propertySets);
    }

    @Step("Verify property set is not displayed...")
    public boolean isPropertySetDisplayed(String propSet) {
        WebElement propertySet = driver.findElement(By.xpath(xpathPropSetName.replace("{name}", propSet)));
        return isElementVisible(propertySet);
    }

    @Step("Switch on propagate toggle button...")
    public void enablePropagateOption() {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getBrowserInstance();
        js.executeScript("document.querySelector('label.form-check-label','::before').click();");
    }

    @Step("Verify propagate toggle button is not displayed...")
    public boolean noPropagateToggleButtonDisplayed() {
        List<WebElement> propagateToggle = driver.findElements(By.xpath(propagateToggleButton));
        return getNumberOfVisibleElements(propagateToggle) == 0;
    }

    @Step("Hover propagate toggle button...")
    public void hoveOverPropagationToggle() {
        hoverOverElement(driver.findElement(By.xpath(propagateToggleButton)));
    }

    @Step("Verify propagate toggle button tooltip is displayed...")
    public String isPropagateButtonTooltipDisplayed() {
        return getText(propagateToggleTooltip);
    }

    @Step("Verify propagate toggle button is switched on...")
    public boolean isPropagateOptionEnabled() {
        return isElementVisible(propagateToggleEnabled);
    }

    @Step("Click on Delete button...")
    public void clickOnDeleteButton() {
        click(deleteButton);
    }

    @Step("Verify that default value checkbox is not checked...")
    public boolean isDefaultValueNotSelected() {
        return isElementVisible(booleanValueCheckBoxNotChecked);
    }

    @Step("Verify Property sub-header is displayed...")
    public boolean isPropertySubHeaderPresent() {
        return isElementVisible(propertySubHeader);
    }

    @Step("Verify sub-header for selected property is displayed...")
    public boolean isSelectedPropertySubHeaderPresent(String property) {
        WebElement propertyHeader = waitForElement(By.cssSelector(cssPropertyHeader.replace("{name}", property)));
        logger.info("property header text:" +propertyHeader.getText());
        return isElementVisible(propertyHeader);
    }

    @Step("Go to Property Management page...")
    public void goToPropertyManagementPage(String url) {
        goTo(url);
    }

    @Step("Verify page icon is present...")
    public boolean isPageIconDisplayed() {
        return isElementVisible(pageIcon);
    }

    @Step("Verify property icon is present...")
    public boolean isPropertyIconDisplayed() {
        return isElementVisible(propertyIcon);
    }

    @Step("Verify message above the grid is present...")
    public boolean isGridMessagePresent(String libName) {
        WebElement gridMessage = findElementWithWait(By.xpath(gridTitle.replace("{name}", libName)));
        return isElementVisible(gridMessage);
    }

    @Step("Verify property Created pop-up appear and close it")
    public void clickOnCloseCreatedPopUpButton() {
        click(waitForElementToBeClickable(createdPopUpCloseButton));
    }

    @Step("Clear property name...")
    public void clearPropertyNameWithKeyBoardAndInputText(String value) {
        clearTextWithKeyboard(propNameInputField);
        inputText(propNameInputField, value);
    }

    @Step("Verify custom or default icon is visible for property")
    public boolean isCustomOrDefaultIconVisibleForProperty(String propertyName, String iconType) {
        try {
            String propertyIconXpath = iconType.equalsIgnoreCase("custom")
                    ? propertyWithCustomIcon.replace("{customIconProperty}", propertyName)
                    : propertyWithDefaultIcon.replace("{defaultIconProperty}", propertyName);

            WebElement propertyIcon = findElementWithWait(By.xpath(propertyIconXpath));
            return isElementVisible(propertyIcon);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify custom or default icon is visible for property set")
    public boolean isCustomOrDefaultIconForPropertySetVisible(String propertySetName, String iconName, String iconType) {
        try {
            String propertySetIconXpath = iconType.equalsIgnoreCase("custom")
                    ? propertySetWithCustomIcon.replace("{propertySetName}", propertySetName).replace("{iconName}", iconName)
                    : propertySetWithDefaultIcon.replace("{propertySetName}", propertySetName).replace("{iconName}", iconName);

            WebElement propertySetIcon = findElementWithWait(By.xpath(propertySetIconXpath));
            return isElementVisible(propertySetIcon);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify that protected icon for property set is visible...")
    public boolean isProtectedIconForProtectedPropertySetVisible(String propertySetName) {
        try{
            final WebElement protectedIcon = findElementWithWait(By.xpath(protectedIconForPropertySet.replace("{propertySetName}", propertySetName)));
            logger.info("Protected icon for property set '{}' is displayed.", propertySetName);
            return isElementVisible(protectedIcon);
        } catch (Exception e) {
            logger.info("Protected icon for property set '{}' is not displayed.", propertySetName);
            return false;
        }
    }
}
