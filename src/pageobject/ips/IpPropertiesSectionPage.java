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
package com.methodics.phi.pageobject.ips;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class IpPropertiesSectionPage extends BasePage {
    private WebDriver driver;

    public IpPropertiesSectionPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@placeholder='Select Property Sets']/ancestor::div[@class='multiselect__tags']")
    private WebElement propSetInputField;

    @FindBy(xpath = "//td[contains(@class,'today')]/following::td")
    private WebElement calendarDayAfter;

    @FindBy(xpath = "//button[contains(@class,'applyBtn')]")
    private WebElement applyButton;

    @FindBy(xpath = "//div[contains(@class, 'multiselect--disabled')]")
    private WebElement disabledPropSets;

    @FindBy(xpath = "//div[@class='alert__component__body'][normalize-space()='You have no permissions to change Property Sets.']")
    private WebElement noPermissionsMessage;

    private String xpathPropSet = "//div[@class='text-truncate'][@title='{name}'][normalize-space()='{name}']/parent::span";
    private String xpathPropSetDeleteButton = "//a[contains(.,'{name}')]/i";
    private String xpathAttachedPropSet = "//a[@title='{name}']/span[normalize-space()='{text}']";
    private String xpathPropInputField = "//input[@name='{propName}']";
    private String booleanValueFalse = "//input[@name='{propName}' and @aria-checked='false']";
    private String booleanValueTrue = "//input[@name='{propName}' and @aria-checked='true']";
    private String xpathPropertyLabel = "//label[contains(@class, 'd-flex')][normalize-space()='{type}{name}']//span[@class='text-truncate']";
    private String xpathPropSuffix = "//input[@name='{name}']/following-sibling::span[normalize-space()='{suffix}']";
    private String xpathOpenChoiceDropdown = "//div[contains(@class,'prop-value-component')][.//text()[normalize-space()='{propName}']]//div[@class='multiselect__tags']";
    private String xpathChoiceDropdown = "//span[contains(@class,'multiselect__option')]/span[text()='{text}'][@title='{text}']";
    private String xpathChoicePropValues = "//div[contains(@class, 'prop-value') and contains(.,'{name}')]//span[@class='multiselect__tag']";
    private String xpathAddedChoiceValue = "//div[contains(.,'{name}')]//span[@class='multiselect__tag']/span[text()='{text}']";
    private String xpathSingleChoiceValue = "//div[contains(@class, 'prop-value') and contains(.,'{name}')]//span[@class='multiselect__single']";
    private String xpathDeleteChoiceButton = "//div[contains(@class, 'prop-value') and contains(.,'{name}')]//span[@class='multiselect__tag']//i";
    private String xpathDateField = "//span[@class='text-truncate' and text()='{propName}']/ancestor::div[contains(@class,'prop-value-component')]//div[@class='vue-daterange-picker']//div[contains(@class,'justify-content-between')]";
    private String xpathPropertyFieldDisabled = "//div[contains(.,'{propName}')]/div/div[contains(@class, 'read-only-value') and normalize-space()='{value}']";
    private String attachedPropertySets = "//a[@role='button' and @class='multiselect__tag']";
    private String deleteButtonDisabled = "//a[contains(.,'global')]/i[@class='multiselect__tag-icon no-close']";
    private String xpathTooltip = "//div[@class='tooltip-inner' and text()='{tooltip}']";
    private String protectedIcons = "//div[contains(@class, 'prop-value-component')]//i[contains(@class, 'protected-icon')]";
    private String readOnlyValue = "//div[contains(@class, 'read-only-value')]";
    private String xpathPropertyDisplayed = "//div[contains(@class, 'prop-value') and contains(.,'{name}')]";

    @Step("Open Property set dropdown...")
    public void clickOnPropertySetInputField() {
        click(propSetInputField);
    }

    @Step("Click on Property set...")
    public void clickOnPropertySet(String propSetName) {
        WebElement propSet = findElementWithWait(By.xpath(xpathPropSet.replace("{name}", propSetName)));
        scrollToElement(propSet);
        click(propSet);
    }

    @Step("Select property set from the dropdown list...")
    public void addNewPropertySet(String propSetName) {
        DriverFactory.sleep(1000);
        clickOnPropertySetInputField();
        clickOnPropertySet(propSetName);
    }

    @Step("Verify property set is not present in dropdown...")
    public int isPropertySetNotDisplayedInDropdown(String propSetName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathPropSet.replace("{name}", propSetName)), counter).size();
    }

    @Step("Get number of attached property sets...")
    public int getNumberOfAttachedPropertySets(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(attachedPropertySets), counter).size();
    }

    @Step("Verify attached property set is displayed...")
    public boolean isAttachedPropertySetPresent(String propSetName, String objectType) {
        WebElement attachedProperty = findElementWithWait(By.xpath(xpathAttachedPropSet.replace("{text}", objectType)
                .replace("{name}", propSetName)));
        return isElementVisible(attachedProperty);
    }

    @Step("Detach property set from IP...")
    public void detachPropertySetFromIp(String propSetName) {
        WebElement deleteSetButton = findElementWithWait(By.xpath(xpathPropSetDeleteButton.replace("{name}", propSetName)));
        clickWithJS(deleteSetButton);
    }

    @Step("Verify property set delete button is not displayed...")
    public boolean isDeleteButtonDisabled() {
        return driver.findElements(By.xpath(deleteButtonDisabled)).size() == 1;
    }

    @Step("Enter property value...")
    public void enterPropertyValue(String propName, String value) {
        WebElement propInputField = findElementWithWait(By.xpath(xpathPropInputField.replace("{propName}", propName)));
        inputText(propInputField, value);
    }

    @Step("Get property value...")
    public String getPropertyValue(String propName) {
        WebElement property = findElementWithWait(By.xpath(xpathPropInputField.replace("{propName}", propName)));
        return getAttribute(property);
    }

    @Step("Verify toggle button is set to false...")
    public boolean isBoolPropValueSetToFalse(String propName) {
        return isElementVisible(findElementWithWait(By.xpath(booleanValueFalse.replace("{propName}", propName))));
    }

    @Step("Verify toggle button is set to true...")
    public boolean isBoolPropValueSetToTrue(String propName) {
        return isElementVisible(findElementWithWait(By.xpath(booleanValueTrue.replace("{propName}", propName))));
    }

    @Step("Verify property: {1} of type {0} is displayed...")
    public boolean isPropertyLabelPresent(String type, String name) {
        WebElement propertyLabel = findElementWithWait(By.xpath(xpathPropertyLabel
                .replace("{type}", type).replace("{name}", name)));
        return isElementVisible(propertyLabel);
    }

    @Step("Verify property: {1} is not displayed...")
    public int isPropertyDeletedFromIp(String type, String name, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathPropertyLabel
                .replace("{type}", type).replace("{name}", name)), counter).size();
    }

    @Step("Hover over property name...")
    public void hoverOverPropertyName(String propertyType, String propertyName) {
        WebElement property = findElementWithWait(By.xpath(xpathPropertyLabel.replace("{type}", propertyType).replace("{name}", propertyName)));
        hoverOverElement(property);
    }

    @Step("Verify tooltip is present...")
    public boolean isTooltipPresent(String text) {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip.replace("{tooltip}", text)));
        return isElementVisible(tooltip);
    }

    @Step("Verify property suffix is displayed...")
    public boolean isPropertySuffixDisplayed(String name, String suffix) {
        WebElement propSuffix = findElementWithWait(By.xpath(xpathPropSuffix
                .replace("{name}", name).replace("{suffix}", suffix)));
        return isElementVisible(propSuffix);
    }

    @Step("Select multi choice default value from dropdown...")
    public void addChoiceValue(String propName, String... options) {
        WebElement choiceInputField = findElementWithWait(By.xpath(xpathOpenChoiceDropdown.replace("{propName}", propName)));
        for (String choice : options) {
            scrollToElement(choiceInputField);
            click(choiceInputField);
            DriverFactory.sleep(2000); //scope for improvement
            clickOnVisibleElement(driver.findElements(By.xpath(xpathChoiceDropdown.replace("{text}", choice))));
        }
    }

    @Step("Get number of choice default values...")
    public int getNumberOfChoiceDefaultValues(String propName) {
        List<WebElement> choiceValues = driver.findElements(By.xpath(xpathChoicePropValues.replace("{name}", propName)));
        return choiceValues.size();
    }

    @Step("Verify set choice value is displayed...")
    public boolean isChoiceValuePresent(String choice, String value) {
        WebElement choiceValue = findElementWithWait(By.xpath(xpathAddedChoiceValue.replace("{name}", choice)
                .replace("{text}", value)));
        return isElementVisible(choiceValue);
    }

    @Step("Get set choice value...")
    public String getSingleChoiceValue(String propName) {
        WebElement choiceValue = findElementWithWait(By.xpath(xpathSingleChoiceValue.replace("{name}", propName)));
        return getText(choiceValue);
    }

    @Step("Delete multi choice default value...")
    public void deleteChoiceValue(String name) {
        WebElement deleteValueButton = findElementWithWait(By.xpath(xpathDeleteChoiceButton.replace("{name}", name)));
        click(deleteValueButton);
    }

    @Step("Get property value...")
    public String getDatePropertyValue(String propName) {
        WebElement property = findElementWithWait(By.xpath(xpathDateField.replace("{propName}", propName)));
        return getText(property);
    }

    @Step("Update datetime property value...")
    public void updateDatetimePropValue(String propName) {
        WebElement datepicker = findElementWithFluentWait(By.xpath(xpathDateField.replace("{propName}", propName)));
        click(datepicker);
        scrollToElement(calendarDayAfter);
        click(calendarDayAfter);
        scrollToElement(applyButton);
        click(applyButton);
    }

    @Step("Verify property value field is disabled...")
    public Boolean isPropertyValueFieldDisabled(String propName, String value) {
        WebElement property = findElementWithWait(By.xpath(xpathPropertyFieldDisabled
                .replace("{propName}", propName).replace("{value}", value)));
        return isElementVisible(property);
    }

    @Step("Verify that property set field is disabled...")
    public Boolean isPropertySetFieldDisabled() {
        return isElementVisible(disabledPropSets);
    }

    @Step("Verify that permissions message is displayed")
    public boolean isPermissionsMessageDisplayed() {
        return isElementVisible(noPermissionsMessage);
    }

    @Step("Verify added property is visible...")
    public boolean isAddedPropertyVisible(String propName) {
        WebElement choiceValue = findElementWithWait(By.xpath(xpathPropertyDisplayed.replace("{name}", propName)));
        return isElementVisible(choiceValue);
    }

    @Step("Get choice value text")
    public String getChoicePropertyText(String propName, String choice) {
        WebElement choiceInputField = findElementWithWait(By.xpath(xpathOpenChoiceDropdown.replace("{propName}", propName)));
        scrollToElement(choiceInputField);
        click(choiceInputField);
        WebElement propertyChoice = findElementWithWait(By.xpath(xpathChoiceDropdown.replace("{text}", choice)));
        scrollToElement(propertyChoice);
        return getText(propertyChoice);
    }

    @Step("Get the number of Protected Properties...")
    public int getNumberOfProtectedProps(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(protectedIcons), counter).size();
    }

    @Step("Get the number of Read only Properties...")
    public int getNumberOfReadOnlyProperties(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(readOnlyValue), counter).size();
    }
}
