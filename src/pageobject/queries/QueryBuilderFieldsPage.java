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
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class QueryBuilderFieldsPage extends BasePage {
    private WebDriver driver;

    public QueryBuilderFieldsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@class='remove-field']")
    private List<WebElement> selectedFields;

    @FindBy(xpath = "//input[@id='description_field']")
    private WebElement descriptionInputField;

    @FindBy(xpath = "//input[@id='fqn_field']")
    private WebElement fqnInputField;

    @FindBy(xpath = "//input[@id='library']/ancestor::div[@class='multiselect__tags']")
    private WebElement libsMultiSelectField;

    @FindBy(xpath = "//input[@id='labels']/ancestor::div[@class='multiselect__tags']")
    private WebElement labelsMultiSelectField;

    @FindBy(xpath = "//input[@id='lines']/ancestor::div[@class='multiselect__tags']")
    private WebElement linesMultiSelectField;

    @FindBy(xpath = "//input[@placeholder='Select IPV aliases']/ancestor::div[@class='multiselect__tags']")
    private WebElement aliasesMultiSelectField;

    @FindBy(xpath = "//input[@id='ip_author']/ancestor::div[@class='multiselect__tags']")
    private WebElement ipAuthorSingleSelectField;

    @FindBy(xpath = "//input[@id='ipv_author']/ancestor::div[@class='multiselect__tags']")
    private WebElement ipvAuthorSingleSelectField;

    @FindBy(xpath = "//div[contains(@class,'vue-daterange-picker')]")
    private WebElement datepicker;

    @FindBy(xpath = "//td[contains(@class,'today')]")
    private WebElement todayDate;

    @FindBy(xpath = "//input[@class='yearselect col']")
    private WebElement yearField;

    @FindBy(xpath = "//select[@class='monthselect col']")
    private WebElement monthSelect;

    @FindBy(xpath = "//select[@class='ampmselect']")
    private WebElement timeSelect;

    @FindBy(xpath = "//option[@value='AM']")
    private WebElement amTime;

    @FindBy(xpath = "//td[contains(@class,'yesterday')]")
    private WebElement calendarDayBefore;

    @FindBy(xpath = "//td[contains(@class,'tomorrow')]")
    private WebElement calendarDayAfter;

    @FindBy(xpath = "//button[text()='Apply']")
    private WebElement applyButton;

    @FindBy(xpath = "//input[@placeholder='Enter path']")
    private WebElement pathInputField;

    @FindBy(xpath = "//input[@placeholder='Enter resolution']")
    private WebElement resolveInputField;

    @FindBy(xpath = "//input[@placeholder='Enter Unix group']")
    private WebElement unixGroupInputField;

    @FindBy(xpath = "//input[@placeholder='Enter value']")
    private WebElement propertyInputField;

    @FindBy(xpath = "//div[contains(@class,'field__multiselect')]//i[contains(@class,'rotate')]")
    private WebElement operatorsSelector;

    @FindBy(xpath = "//span[contains(@class,'multiselect__single')]")
    private WebElement operatorsSelectorValue;

    @FindBy(xpath = "//input[@placeholder='Select options']/ancestor::div[@class='multiselect__tags']")
    private WebElement choiceMultiSelectField;

    @FindBy(xpath = "//div[contains(@class,'multiselect--active')]//input[@class='multiselect__input']")
    private WebElement selectorInputField;

    @FindBy(xpath = "//div[@class='multiselect' or contains(@class, 'multiselect property-field__choice')]")
    private WebElement selectField;

    @FindBy(xpath = "//div[@class='remove-field']//input[contains(@class,'form-control')]")
    private WebElement queryBuilderTextField;

    private String xpathDropdownItem = "//li[contains(@class, 'select') and normalize-space() = {name}]//span[@class='text-truncate'][@title={name}]";
    private String xpathOperator = "//span[@data-select][contains(@class, 'multiselect__option')]//span[text()='{text}']";
    private String xpathSelectedSearchField = "//label[normalize-space()='{fieldName}']/ancestor::div[@class='remove-field']";
    private String xpathDeleteSearchFieldButton = "//label[contains(.,'{fieldName}')]/ancestor::div[@class='remove-field']/i[contains(@class, 'remove-field__close-btn')]";
    private String xpathMultiSelectFieldValue = "//span[@class='multiselect__tag'][@title='{value}']/span[text()='{value}']";
    private String xpathDeleteTagButton = "//span[@class='multiselect__tag']/span[text()='{value}']/following-sibling::i";
    private String xpathSingleSelectFieldValue = "//div[@class='multiselect'][@title='{value}']//span[@class='multiselect__single'][normalize-space()='{value}']";
    private String xpathMonth = "//option[@value='{month}']";
    private String xpathDay = "//td[text()='{day}']";
    private String xpathRadioButton = "//input[@type='radio' and @value='{value}']/following-sibling::label";
    private String xpathSelectedProperty = "//label[contains(.,'{type}') and contains(.,'{prop}')][@title='{prop}'][contains(@class, 'truncate')]";

    @Step("Select dropdown value {0}...")
    public void selectDropdownValue(String value) {
        WebElement dropdownElement = findElementWithWait(By.xpath(xpathDropdownItem.replace("{name}", wrapObjNameForXPath(value))));
        click(dropdownElement);
    }

    @Step("Verify dropdown list does not contain item {0}...")
    public boolean itemNotPresentInDropdownList(String name) {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(xpathDropdownItem.replace("{name}", wrapObjNameForXPath(name))))) == 0;
    }

    @Step("Verify search field {0} is added to Query Builder form...")
    public boolean isSelectedFieldDisplayed(String field) {
        WebElement selectedField = findElementWithWait(By.xpath(xpathSelectedSearchField.replace("{fieldName}", field)));
        return isElementVisible(selectedField);
    }

    @Step("Get number of added query builder fields...")
    public int getNumberOfSelectedQueryBuilderFields() {
        return selectedFields.size();
    }

    @Step("Delete search field: {0}...")
    public void removeSearchField(String... fields) {
        for (String field : fields) {
            WebElement deleteButton = findElementWithWait(By.xpath(xpathDeleteSearchFieldButton.replace("{fieldName}", field)));
            DriverFactory.sleep(700);
            click(deleteButton);
        }
    }

    @Step("Enter IP description: {0}, for the method: {method}...")
    public void enterDescriptionIntoInputField(String description) {
        inputText(descriptionInputField, description);
    }

    @Step("Enter FQN: {0}, for the method: {method}...")
    public void enterFqnIntoInputField(String fqn) {
        inputText(fqnInputField, fqn);
    }

    @Step("Open Select Library dropdown...")
    public void openSelectLibrariesDropdown() {
        click(libsMultiSelectField);
    }

    @Step("Select library: {0}, for the method: {method}...")
    public void selectLibrary(String... libs) {
        for (String libName : libs) {
            openSelectLibrariesDropdown();
            selectDropdownValue(libName);
        }
    }

    @Step("Open Select Labels dropdown...")
    public void openSelectLabelsDropdown() {
        scrollToElement(labelsMultiSelectField);
        click(labelsMultiSelectField);
    }

    @Step("Select label: {0}, for the method: {method}...")
    public void selectLabel(String... labels) {
        for (String label : labels) {
            openSelectLabelsDropdown();
            selectDropdownValue(label);
        }
    }

    @Step("Open Select Lines dropdown...")
    public void openSelectLinesDropdown() {
        click(linesMultiSelectField);
    }

    @Step("Select IP line: {0}, for the method: {method}...")
    public void selectLine(String... lines) {
        for (String lineName : lines) {
            openSelectLinesDropdown();
            selectDropdownValue(lineName);
        }
    }

    @Step("Open Select Aliases dropdown...")
    public void openSelectAliasDropdown() {
        scrollToElement(aliasesMultiSelectField);
        click(aliasesMultiSelectField);
    }

    @Step("Select IP alias: {0}, for the method: {method}...")
    public void selectAlias(String... aliases) {
        for (String aliasName : aliases) {
            openSelectAliasDropdown();
            selectDropdownValue(aliasName);
        }
    }

    @Step("Select IP author: {0}, for the method: {method}...")
    public void selectIpAuthor(String user) {
        click(ipAuthorSingleSelectField);
        selectDropdownValue(user);
    }

    @Step("Select IPV author: {0}, for the method: {method}...")
    public void selectIpvAuthor(String userName) {
        click(ipvAuthorSingleSelectField);
        selectDropdownValue(userName);
    }

    @Step("Verify selected value {0} is displayed for multi select field...")
    public boolean isFieldValueSelected(String value) {
        List<WebElement> multiEditorTags = driver.findElements(By.xpath(xpathMultiSelectFieldValue.replace("{value}", value)));
        return getNumberOfVisibleElements(multiEditorTags) == 1;
    }

    @Step("Delete selected value...")
    public void deleteSelectedValue(String... tags) {
        for (String tag : tags) {
            WebElement deleteTagButton = findElementWithWait(By.xpath(xpathDeleteTagButton.replace("{value}", tag)));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", deleteTagButton);
        }
    }

    @Step("Verify selected value {0} is displayed for single select field...")
    public boolean isAuthorSelected(String user) {
        WebElement singleSelectTag = findElementWithWait(By.xpath(xpathSingleSelectFieldValue.replace("{value}", user)));
        return isElementVisible(singleSelectTag);
    }

    @Step("Select today's date...")
    public void selectTodaysDate() {
        click(datepicker);
        click(todayDate);
        click(todayDate);
        scrollToElementForLazyLoading(applyButton);
        click(applyButton);
    }

    @Step("Select date range...")
    public void selectDateRange(String year, String month, String startDate, String endDate) {
        click(datepicker);
        inputText(yearField, year);
        click(driver.findElement(By.xpath(xpathMonth.replace("{month}", month))));
        click(driver.findElement(By.xpath(xpathDay.replace("{day}", startDate))));
        click(driver.findElement(By.xpath(xpathDay.replace("{day}", endDate))));
        scrollToElementForLazyLoading(applyButton);
        click(applyButton);
    }

    @Step("Select date range...")
    public void selectDatetimePropValue() {
        click(datepicker);
        click(calendarDayBefore);
        click(timeSelect);
        click(amTime);
        click(calendarDayAfter);
        scrollToElementForLazyLoading(applyButton);
        click(applyButton);
    }

    @Step("Get selected date...")
    public String getSelectedDate() {
        return getText(datepicker);
    }

    @Step("Enter project property path: {0}...")
    public void enterPath(String path) {
        inputText(pathInputField, path);
    }

    @Step("Enter project property resolve IPV: {0}...")
    public void enterResolveIpv(String ipv) {
        inputText(resolveInputField, ipv);
    }

    @Step("Enter unix group name: {0}...")
    public void enterUnixGroup(String group) {
        inputText(unixGroupInputField, group);
    }

    @Step("Select Project Property mode: {0}, for the method: {method}...")
    public void checkRadioButton(String value) {
        WebElement radioButton = findElementWithWait(By.xpath(xpathRadioButton.replace("{value}", value)));
        click(radioButton);
    }

    @Step("Enter property value: {0}...")
    public void enterPropertyValue(String propValue) {
        inputText(propertyInputField, propValue);
    }

    @Step("Verify property {0} is added to Query Builder form...")
    public boolean isSelectedPropertyDisplayed(String type, String prop) {
        WebElement selectedProperty = findElementWithWait(By.xpath(xpathSelectedProperty
                .replace("{type}", type).replace("{prop}", prop)));
        return isElementVisible(selectedProperty);
    }

    @Step("Select operator: {0}, for the method: {method}...")
    public void selectOperator(String operator) {
        click(operatorsSelector);
        WebElement dropdownItem = findElementWithWait(By.xpath(xpathOperator.replace("{text}", operator)));
        click(dropdownItem);
    }

    @Step("Get selected operator...")
    public String getSelectedOperator() {
        return getText(operatorsSelectorValue);
    }

    @Step("Select choice options: {0}...")
    public void selectChoiceOptions(String... options) {
        for (String option : options) {
            click(choiceMultiSelectField);
            selectDropdownValue(option);
        }
    }

    @Step("Enter text {0} into the search field {1}...")
    public void enterTextIntoSearchField(String text) {
        inputText(selectorInputField, text);
    }

    @Step("Clear dropdown input value...")
    public void removeTextFromSearchField() {
        clearInputFieldWithBackspace(selectorInputField);
    }

    @Step("Open select field dropdown list...")
    public void openDropdownMenu() {
        waitForElementToBeClickable(selectField);
        click(selectField);
    }

    @Step("Enter text into the textbox...")
    public void enterValueIntoTextField(String text) {
        inputText(queryBuilderTextField, text);
    }

    @Step("Clear text field value...")
    public void clearTextboxValue() {
        DriverFactory.sleep(1000);
        clearInputFieldWithBackspace(queryBuilderTextField);
    }

    @Step("Get textbox value...")
    public String getTextboxValue() {
        return getAttribute(queryBuilderTextField);
    }
}
