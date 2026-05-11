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

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class IpPropertiesTable extends BasePage {
    private WebDriver driver;

    public IpPropertiesTable(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@class='card project-property']")
    private List<WebElement> projectProperties;

    @FindBy(xpath = "//div[@class= 'card-body']//span[contains(., 'Mode')]")
    private WebElement propMode;

    @FindBy(xpath = "//input[@data-input='properties']")
    private WebElement propertySearchField;

    @FindBy(xpath = "//i[contains(@class, 'fa-circle-xmark')]")
    private WebElement propertySearchCancelButton;

    @FindBy(xpath = "//div[@data-testid='tabpanel-ip-details-properties-tab']//div[@class='ag-overlay-panel' and normalize-space()]")
    private WebElement noDataFoundMessage;

    @FindBy(xpath = "//div[@class='modal-body']//p")
    private WebElement modalBody;

    @FindBy(xpath = "//h5[@class='modal-title']")
    private WebElement modalHeader;

    @FindBy(css = "input[data-testid='show-empty-props-input']")
    private WebElement showEmptyPropsToggle;

    private String xpathMode = "//span[normalize-space()='Mode: {mode}']";
    private String xpathScope = "//span[normalize-space()='Scope: {scope}']";
    private String xpathPath = "//span[normalize-space()='Path: {path}']";
    private String xpathResolve = "//span[normalize-space()='Resolve:']/following-sibling::p[normalize-space()='{resolve}']";
    private String xpathUnixGroup = "//span[normalize-space()='Unix group: {group}']";
    private String protectedIcon = "//*[contains(@data-testid,'-shield-icon')]";
    private String columnValueBasedOnTitle = "//span[@title='{title}']/ancestor::div[@role='row']//div[@col-id='{column}' or @col-id='ip_properties.{column}' or @col-id='{column}']";
    private String properties = "//div[@col-id='name'][@role='gridcell'][contains(@class, 'ag-cell-value')]";
    private String propertiesByType = "//span[contains(@class, 'target-type__ip')]/span[normalize-space()='{typeName}']//ancestor::div[@role='row']";
    private String expandButton = "//div[@col-id='{column}']//span[contains(@class,'expand-button')]";
    private String columnValueList = "//div[@col-id='name'][normalize-space()='{prop}']//parent::div[@role='row']//div[@col-id='{column}']//span[@title][contains(@class, 'truncate')]";
    private String propertySet = "//span[contains(@class, 'property-set-badge')]/span[@class='text-truncate'][@title='{propSet}']";
    private String xpathOpenModalIcon = "(//div[@col-id='{field}']//i[contains(@class, 'fa-square-poll-horizontal')])[1]";
    private String mdxProtectedPropBadge = "//div[@col-id='target_type']//span/span[normalize-space()='{badgeName}']";
    private String customIconValidation = "//*[normalize-space()='{propName}']//ancestor::*[@col-id='{idName}']//*[@data-testid='{propNameIcon}-custom-icon']";
    private String customIconPropSetValidation = "//*[normalize-space()='{propSetName}']//ancestor::*[@col-id='{idName}']//*[@data-testid='ps-{propSetName}-custom-icon']";
    private String defaultIconValidation = "//*[normalize-space()='{propName}']//ancestor::*[@col-id='{idName}']//*[@data-testid='{propName}-default-icon']";
    private String defaultIconDependentPropertyValidation = "//*[normalize-space()='{propName}']//ancestor::*[@col-id='{idName}']//*[@data-testid='dependent-{propName}-default-icon']";
    private String defaultIconPropSetValidation = "//*[normalize-space()='{propSetName}']//ancestor::*[@col-id='{idName}']//*[@data-testid='ps-{propSetNameIcon}-default-icon']";
    private String shieldIconProperty = "//*[@col-id='{idName}']//*[normalize-space()='{propertyName}']//*[@data-testid='{propertyName}-shield-icon']";
    private String shieldIconpropertySet = "//*[@col-id='{idName}']//*[normalize-space()='{propertySet}']//parent::span//*[@data-testid='ps-{propertySet}-shield-icon']";
    private String shieldIconDependentProperties = " //*[@col-id='{idName}']//*[normalize-space(text())='{propertyName}']//parent::span//*[@data-testid='dependent-{propertyName}-shield-icon']";
    private String getColumnValueTextByRowNumber = "//span[@title='{title}']/ancestor::div[@row-index='{rowNumber}']//*[@col-id='{column}' or @col-id='{column}']";

    //   PLEASE UPDATE AFTER THE PROJECT PROPERTIES DESIGN TASK IS DONE
/*        @Step("Get the number of Project Properties...")
    public int getNumberOfProjectProps() {
        return getNumberOfVisibleElements(projectProperties);
    }

        @Step("Verify project property scope is displayed...")
    public boolean isModeDisplayed(String mode) {
        WebElement propMode = findElementWithWait(By.xpath(xpathMode.replace("{mode}", mode)));
        return isElementVisible(propMode);
    }

    @Step("Verify project property scope is displayed...")
    public boolean isScopeDisplayed(String scope) {
        WebElement propScope = findElementWithWait(By.xpath(xpathScope.replace("{scope}", scope)));
        return isElementVisible(propScope);
    }

    @Step("Verify project property path is displayed...")
    public boolean isPathDisplayed(String path) {
        WebElement propPath = findElementWithWait(By.xpath(xpathPath.replace("{path}", path)));
        return isElementVisible(propPath);
    }

    @Step("Verify project property resolve data displayed...")
    public boolean isResolveDataDisplayed(String resource) {
        WebElement resolve = findElementWithWait(By.xpath(xpathResolve.replace("{resolve}", resource)));
        return isElementVisible(resolve);
    }

    @Step("Verify unix group is displayed...")
    public boolean isUnixGroupDisplayed(String group) {
        WebElement unixGroup = findElementWithWait(By.xpath(xpathUnixGroup.replace("{group}", group)));
        return isElementVisible(unixGroup);
    }
*/
    @Step("Verify column value is present...")
    public String getColumnValueText(String title, String column) {
        WebElement propValue = findElementWithWait(By.xpath(columnValueBasedOnTitle
                .replace("{title}", title)
                .replace("{column}", column)));
        return propValue.getText();
    }

    @Step("Getting number of protected icons...")
    public int getNumberOfProtectedIcons(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(protectedIcon), counter).size();
    }

    @Step("Getting number of protected prop badge icons...")
    public int getNumberOfProtectedBadgeIcons(String badgeName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(mdxProtectedPropBadge.replace("{badgeName}", badgeName)), counter).size();
    }

    @Step("Verifying that values exist in the table...")
    public void areColumnValuesPresent(String[] title, String column, String[] propertyValue) {
        Assert.assertEquals(title.length, propertyValue.length, "Properties and values do not match!");
        for (int i = 0; i < title.length; i++) {
            Assert.assertEquals(getColumnValueText(title[i], column), propertyValue[i],
                    "Value of property " + title[i] + " is incorrect");
        }
    }

    @Step("Get number of IPs...")
    public int getNumberOfProperties(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(properties), counter).size();
    }

    @Step("Get number of IPs or IPVs...")
    public int getNumberOfPropertiesTypes(String typeName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(propertiesByType.replace("{typeName}", typeName)), counter).size();
    }

    @Step("Entering value into the search field")
    public void enterValueIntoTheSearchField(String value) {
        waitForElementToBeVisible(propertySearchField);
        enterTextSlowly(propertySearchField, value);
    }

    @Step("Clicking on cancel button in search...")
    public void clickOnCancelButton() {
        click(propertySearchCancelButton);
    }

    @Step("Getting no data found message..")
    public String getNoDataMessage() {
        return getText(noDataFoundMessage);
    }

    @Step("Click on modal icon")
    public void clickOnOpenModalIcon(String field) {
        WebElement openModalIcon = findElementWithWait(By.xpath(xpathOpenModalIcon.replace("{field}", field)));
        click(openModalIcon);
    }

    @Step("Hover over modal icon")
    public void hoverOverModalIcon(String field) {
        WebElement openModalIcon = findElementWithWait(By.xpath(xpathOpenModalIcon.replace("{field}", field)));
        hoverOverElement(openModalIcon);
    }

    @Step("Get modal message...")
    public String getModalMessage() {
        return getText(modalBody);
    }

    @Step("Get modal header...")
    public String getModalHeader() {
        return getText(modalHeader);
    }

    @Step("Click on expand button based on column")
    public void clickOnExpandButtonBasedOnColumn(String column) {
        WebElement expand = findElementWithWait(By.xpath(expandButton.replace("{column}", column)));
        click(expand);
    }

    @Step("Getting number of values in one row..")
    public int getNumberOfValuesInRowByColumn(String prop, String column, int count) {
        List<WebElement> items = findElementsWithWait(By.xpath(columnValueList
                .replace("{prop}", prop)
                .replace("{column}", column)), count);
        return getNumberOfVisibleElements(items);
    }

    @Step("Get dependent property tooltip..")
    public String getDependentPropertyTooltip(String prop, String column) {
        WebElement depProp = findElementWithWait(By.xpath(columnValueList
                .replace("{prop}", prop)
                .replace("{column}", column)));
        return getAttributeValue(depProp, "title");
    }

    @Step("Verify property set tooltip is present...")
    public boolean isPropertySetTooltipPresent(String title) {
        WebElement columnValue = findElementWithWait(By.xpath(propertySet.replace("{propSet}", title)));
        return isElementVisible(columnValue);
    }

    @Step("Click on Show Empty Properties toggle...")
    public void toggleShowEmptyProps() {
        showEmptyPropsToggle.click();
    }

    @Step("Verify if custom icon validation is displayed...")
    public boolean isCustomIconValidationDisplayed(String idName, String propNameIcon, String propertyName) {
        WebElement customIcon = findElementWithWait(By.xpath(customIconValidation.replace("{idName}", idName).replace("{propNameIcon}", propNameIcon).replace("{propName}", propertyName)));
        return isElementVisible(customIcon);
    }

    @Step("Verify if custom icon validation is displayed...")
    public boolean isCustomIconPropSetDisplayed(String idName1,  String propertySetName, int count) {
        List<WebElement> customIcons = driver.findElements(By.xpath(customIconPropSetValidation
                .replace("{idName}", idName1)
                .replace("{propSetName}", propertySetName)));
        if (customIcons.size() == count) {
            logger.info("custom icons is visible for " + propertySetName);
            return true;
        } else {
            logger.info("custom icon is not visible for " + propertySetName + " as count is displaying " + customIcons.size());
            logger.info("xpath is " +customIconPropSetValidation.replace("{idName}", idName1).replace("{propSetName}", propertySetName));
            return false;
        }
    }

    @Step("Verify if default icon validation for dependent property is displayed...")
    public boolean isDefaultIconValidationDisplayed(String idName, String propertyName) {
        WebElement defaultIcon = findElementWithWait(By.xpath(defaultIconValidation
                .replace("{idName}", idName)
                .replace("{propName}", propertyName)));
        if (isElementVisible(defaultIcon)) {
            logger.info("default icons is visible for " + propertyName);
            return true;
        } else {
            logger.info("default icon is not visible for " + propertyName);
            return false;
        }
    }

    @Step("Verify if default icon validation for dependent property is displayed...")
    public boolean isDefaultIconValidationDependentPropertyDisplayed(String idName, String propertyName) {
        WebElement defaultIcon = findElementWithWait(By.xpath(defaultIconDependentPropertyValidation
                .replace("{idName}", idName)
                .replace("{propName}", propertyName)));
        if (isElementVisible(defaultIcon)) {
            logger.info("default icons is visible for dependent property " + propertyName);
            return true;
        } else {
            logger.info("default icon is not visible for dependent property " + propertyName);
            return false;
        }
    }

    @Step("Verify if default icon prop set validation is displayed...")
    public boolean isDefaultIconPropSetValidationDisplayed(String idName, String propSetNameIcon, String propertySetName, int count) {
        List<WebElement> defaultIcons = driver.findElements(By.xpath(defaultIconPropSetValidation
                .replace("{idName}", idName)
                .replace("{propSetName}", propSetNameIcon)
                .replace("{propSetNameIcon}", propertySetName)));
        if (defaultIcons.size() == count) {
            logger.info("default icons is visible for " + propertySetName);
            return true;
        } else {
            logger.info("default icon is not visible for " + propertySetName);
            return false;
        }
    }

    @Step("Verify if shield icon property is displayed...")
    public boolean isShieldIconPropertyDisplayed(String idName, String propertyName) {
        WebElement shieldIcon = findElementWithWait(By.xpath(shieldIconProperty
                .replace("{idName}", idName)
                .replace("{propertyName}", propertyName)));
        if (isElementVisible(shieldIcon)) {
            logger.info("Shield icon is visible for " + propertyName);
            return true;
        } else {
            logger.info("Shield icon is not visible for " + propertyName);
            return false;
        }
    }

    @Step("Verify if shield icon property set is displayed...")
    public boolean isShieldIconPropertySetDisplayed(String idName, String propertySetName, int count) {
        List<WebElement> shieldIcons = driver.findElements(By.xpath(shieldIconpropertySet
                .replace("{idName}", idName)
                .replace("{propertySet}", propertySetName)));
        if (shieldIcons.size() == count) {
            logger.info("Shield icons are visible for " + propertySetName);
            return true;
        } else {
            logger.info("Shield icons are not visible for " + propertySetName);
            return false;
        }
    }
    @Step("Verify if shield icon dependent properties are displayed...")
    public boolean isShieldIconDependentPropertiesDisplayed(String idName, String propertyName) {
        WebElement shieldIconDependent = findElementWithWait(By.xpath(shieldIconDependentProperties
                .replace("{idName}", idName)
                .replace("{propertyName}", propertyName)));
        if (isElementVisible(shieldIconDependent)) {
            logger.info("Shield icon is visible for dependent property " + propertyName);
            return true;
        } else {
            logger.info("Shield icon is not visible for dependent property " + propertyName);
            return false;
        }
    }

    @Step("Verify column value is present...")
    public String getColumnValueText(String title, String column, String row) {
        WebElement propValue = findElementWithWait(By.xpath(getColumnValueTextByRowNumber
                .replace("{title}", title)
                .replace("{rowNumber}", row)
                .replace("{column}", column)));
        return propValue.getText();
    }

    @Step("Verify if column value based on title is present...")
    public boolean isColumnValueBasedOnTitlePresent(String title, String column) {
        try {
            WebElement propValue = findElementWithWait(By.xpath(columnValueBasedOnTitle
                    .replace("{title}", title)
                    .replace("{column}", column)));
            return isElementVisible(propValue);
        } catch (Exception e) {
            logger.info("Column value based on title is not present for title: " + title + " and column: " + column);
            return false;
        }
    }
}
