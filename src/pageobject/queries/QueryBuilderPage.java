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

import java.util.List;

public class QueryBuilderPage extends BasePage {
    private WebDriver driver;

    public QueryBuilderPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@id='query_name']")
    private WebElement queryNameInputField;

    @FindBy(xpath = "//span[text()='Saved Query name ']/*[contains(@class, 'message-with-icon__info')]")
    private WebElement nameFieldHelpIcon;

    @FindBy(xpath = "//*[@class='field-validator' and normalize-space()='The Query name is required.']")
    private WebElement emptyNameFieldError;

    @FindBy(xpath = "//div[contains(@class,'invalid-feedback')and normalize-space()='A Query name must not contain spaces.']")
    private WebElement nameValidationError;

    @FindBy(xpath = "//div[@role='alert' and contains(.,'Query Builder is not available for this expression.')]")
    private WebElement queryBuilderWarning;

    @FindBy(xpath = "//div[@class='multiselect more-attrs__multiselect']")
    private WebElement queryBuilderDropdown;

    @FindBy(css = "div.multiselect--active.multiselect.more-attrs__multiselect")
    private List<WebElement> selectedFields;

    @FindBy(xpath = "//i[@class='fa fa-plus']")
    private WebElement plusButton;

    @FindBy(xpath = "//input[@placeholder='Select item']")
    private WebElement dropdownSearchField;

    @FindBy(xpath = "//li[normalize-space()='No items found.']")
    private WebElement noResultsMessage;

    @FindBy(xpath = "//button[contains(@class, 'dropdown-toggle')]")
    private WebElement termsDropDown;

    private String xpathQueryBuilderPanel = "//div[contains(@class,'column query-builder')]";
    private String xpathDropdownItem = "//span[@data-more-attrs-option-name and @title='{name}'][normalize-space()='{name}']";
    private String disabledDropDownItem = "//li/span[contains(@class, 'multiselect__option--disabled') and normalize-space() = '{name}']";
    private String dropdownListItems = "//span[@data-more-attrs-option-name][@class='text-truncate']";
    private String searchCriteriaValues = "//ul//li[@class='multiselect__element']";
    private String xpathFieldActive = "//span[contains(@class, 'option--selected')]/span[normalize-space() = '{name}']";
    private String xpathFieldDisabled = "//span[contains(@class, 'option--disabled')]/span[normalize-space() = '{name}']";
    private String xpathDmTypeCheckbox = "//input[@type='checkbox' and @value='{type}']/following-sibling::label";
    private String xpathDmTypeCheckboxChecked = "//input[@type='checkbox' and @value='{type}' and @data-testid='ui-checkbox-input' and @checked]";
    private String searchTerm = "//li[@data-testid='ui-dropdown-item']//span[normalize-space()='{term}']";
    private String selectedSearchTerm = "//span[@title='{term}']";
    private String toolTip = "//div[@class='tooltip-inner']";
    private String selectedSearchTermTool = "//span[@title='{term}']/parent::div";

    @Step("Verifying Query Builder panel is displayed...")
    public boolean isQueryBuilderPanelDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(xpathQueryBuilderPanel))) == 1;
    }

    @Step("Verifying Query Builder panel is not displayed...")
    public boolean isQueryBuilderPanelNotDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(xpathQueryBuilderPanel))) == 0;
    }

    @Step("Enter query name {0} into the input field...")
    public void enterQueryName(String queryName) {
        inputText(queryNameInputField, queryName);
    }

    @Step("Clear query name input field...")
    public void clearQueryNameInputField() {
        clearInputFieldWithBackspace(queryNameInputField);
    }

    @Step("Update query name...")
    public void updateQueryName(String queryName) {
        clearQueryNameInputField();
        enterQueryName(queryName);
    }

    @Step("Get query name from the input field...")
    public String getQueryName() {
        return getAttribute(queryNameInputField);
    }

    @Step("Verify required field error...")
    public boolean isRequiredFieldErrorPresent() {
        return isElementVisible(emptyNameFieldError);
    }

    @Step("Verify name validation error...")
    public boolean isNameValidationErrorPresent() {
        return isElementVisible(nameValidationError);
    }

    @Step("Verify Query Builder warning message...")
    public boolean isQueryBuilderNotAvaliable() {
        return isElementVisible(queryBuilderWarning);
    }

    @Step("Check if Query Builder dropdown is displayed...")
    public boolean isQueryBuilderDropdownDisplayed() {
        return isElementVisible(queryBuilderDropdown);
    }

    @Step("Open search fields dropdown...")
    public void openSearchFieldsDropdown() {
        click(queryBuilderDropdown);
    }

    @Step("Select dropdown value {0}...")
    public void selectDropdownValue(String value) {
        WebElement dropdownElement = findElementWithWait(By.xpath(xpathDropdownItem.replace("{name}", value)));
        click(dropdownElement);
    }

    @Step("Click on Plus button...")
    public void clickOnAddButton() {
        click(plusButton);
    }

    @Step("Add search field: {0}...")
    public void addSearchField(String... fields) {
        for (String field : fields) {
            openSearchFieldsDropdown();
            enterFieldNameIntoSearchField(field);
            selectDropdownValue(field);
            clickOnAddButton();
        }
    }

    @Step("Verify dropdown item is disabled...")
    public boolean itemDisabledInDropdownList(String name) {
        WebElement item = findElementWithWait(By.xpath(disabledDropDownItem.replace("{name}", name)));
        return isElementVisible(item);
    }

    @Step("Verify dropdown list item {0} is active...")
    public boolean isDropdownItemSelected(String name) {
        WebElement activeField = findElementWithWait(By.xpath(xpathFieldActive.replace("{name}", name)));
        return isElementVisible(activeField);
    }

    @Step("Get number of selected fields...")
    public int getNumberOfSelectedFields() {
        return getNumberOfVisibleElements(selectedFields);
    }

    @Step("Verify dropdown list item {0} is unselectable...")
    public boolean isDropdownItemDisabled(String name) {
        WebElement disabledField = findElementWithWait(By.xpath(xpathFieldDisabled.replace("{name}", name)));
        return isElementVisible(disabledField);
    }

    @Step("Get selected field name from dropdown input field...")
    public String getSelectedFieldName() {
        DriverFactory.sleep(1000);
        return getText(queryBuilderDropdown);
    }

    @Step("Select IP DM type: {0}, for the method: {method}...")
    public void selectDmType(String... options) {
        for (String dmType : options) {
            WebElement dmTypeCheckbox = findElementWithWait(By.xpath(xpathDmTypeCheckbox.replace("{type}", dmType)));
            click(dmTypeCheckbox);
        }
    }

    @Step("Deselect IP DM type: {0}, for the method: {method}...")
    public void deselectDmType(String dmType) {
        WebElement dmTypeCheckbox = findElementWithWait(By.xpath(xpathDmTypeCheckbox.replace("{type}", dmType)));
        click(dmTypeCheckbox);
    }

    @Step("Verify DM type checkbox...")
    public boolean isDmTypeCheckboxChecked(String dmType) {
        return driver.findElements(By.xpath(xpathDmTypeCheckboxChecked.replace("{type}", dmType))).size() == 1;
    }

    @Step("Enter field name {0} into the search field...")
    public void enterFieldNameIntoSearchField(String fieldName) {
        inputText(dropdownSearchField, fieldName);
    }

    @Step("Clear search input field value...")
    public void clearSearchInputField() {
        clearInputFieldWithBackspace(dropdownSearchField);
    }

    @Step("Get number of search fields...")
    public int getNumberOfSearchFields(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(dropdownListItems), counter).size();
    }

    @Step("Get number of items in dropdown list...")
    public int getNumberOfDropdownListItems(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(searchCriteriaValues), counter).size();
    }

    @Step("Verify no results are found...")
    public boolean noResultsFound() {
        return isElementVisible(noResultsMessage);
    }

    @Step("Opening terms drop down menu")
    public void clickOnTermsDropDown() {
        termsDropDown.click();
    }

    @Step("Choosing a search term....")
    public void selectTerm(String term) {
        findElementWithWait(By.xpath(searchTerm.replace("{term}", term))).click();
    }

    @Step("Verifying that the term is selected")
    public boolean isTermSelected(String term) {
        WebElement selectedTerm = findElementWithWait(By.xpath(selectedSearchTerm.replace("{term}", term)));
        return isElementVisible(selectedTerm);
    }

    @Step("Verify that column name is truncated...")
    public String getSearchTermTooltip(String term) {
        WebElement columnHeaderTooltip = findElementWithWait(By.xpath(selectedSearchTerm.replace("{term}", term)));
        return getAttributeValue(columnHeaderTooltip, "title");
    }

    @Step("Verify tooltip is present...")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithWait(By.xpath(toolTip.replace("{text}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Adding Library and search term...")
    public void addLibraryAndSearchTerm(String searchValue, String term, String... fields) {
        addSearchField(fields);
        QueryBuilderFieldsPage queryBuilderFieldsPage = new QueryBuilderFieldsPage(DriverFactory.getBrowserInstance());
        queryBuilderFieldsPage.openDropdownMenu();
        queryBuilderFieldsPage.selectDropdownValue(searchValue);
        clickOnTermsDropDown();
        selectTerm(term);
    }

    @Step("Adding Search value and search term...")
    public void addSearchValueAndTerm(String searchValue, String searchTerm) {
        addSearchField(searchValue);
        clickOnTermsDropDown();
        selectTerm(searchTerm);
    }

    @Step("Hover over name field help icon...")
    public void hoverOverHelpIcon() {
        hoverOverElement(nameFieldHelpIcon);
    }
}
