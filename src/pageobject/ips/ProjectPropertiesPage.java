/**
 * ################################################################################
 * # Copyright (c) 2010-2024 Methodics, Inc.
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

public class ProjectPropertiesPage extends BasePage {
    private WebDriver driver;

    public ProjectPropertiesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//label[contains(.,'Scope')]/following::div[@id='project-properties-form-scope']")
    private WebElement scopeDropdown;

    @FindBy(xpath = "//input[@placeholder='Select scope']")
    private WebElement scopeInputField;

    @FindBy(xpath = "//input[@data-testid='ui-in-tab-project-properties-path']")
    private WebElement pathInputField;

    @FindBy(xpath = "//textarea[@data-testid='tab-project-properties-resolve']")
    private WebElement resolveInputField;

    @FindBy(xpath = "//input[@data-testid='ui-in-tab-project-properties-unix-group']")
    private WebElement unixGroupInputField;

    @FindBy(xpath = "//label[contains(.,'Mode')]/following-sibling::div[@id='project-properties-form-mode']")
    private WebElement modeDropdown;

    @FindBy(xpath = "//span[normalize-space()='Resolve:']/following-sibling::p")
    private WebElement resolve;

    @FindBy(xpath = "//div[contains(@class, 'prop-card__mode')]")
    private WebElement mode;

    @FindBy(xpath = "//button[@data-testid='ui-btn-project-properties-add']")
    private WebElement addButton;

    @FindBy(xpath = "//button[@data-testid='ui-btn-project-properties-add' and @disabled]")
    private WebElement addButtonDisabled;

    @FindBy(css = ".btn.btn-danger.btn-sm.me-3[data-testid='ui-btn-project-properties-remove']")
    private WebElement removeButton;

    @FindBy(xpath = "//button[normalize-space()='Update']")
    private WebElement updateButton;

    @FindBy(xpath = "//div[@class='text-danger' and contains(.,\"Scope must be '*' when entering a resolution.\")]")
    private WebElement resolveValidationMessage;

    private String xpathScope = "//span[normalize-space()='Scope: {scope}']";
    private String xpathPath = "//span[normalize-space()='Path: {path}']";
    private String xpathUnixGroup = "//span[normalize-space()='Unix group: {group}']";
    private String xpathDropdownChoice = "//span[normalize-space()='{name}']";
    private String xpathProjectProperty = "//div[@class='card-body']/span[text()='{index}']";
    private String noPropertiesDataImage = "//div[contains(@class,'text-center') and contains(., 'No properties added yet.')]";
    private String projectProperties = "//div[@class='card-body']";
    private String xpathTooltip = "//div[@class='tooltip-inner']";
    private String scopeInMenu = "//span[@class='multiselect__option']/span[normalize-space()='{scope}']";


    @Step("Verify no properties data image is present...")
    public boolean isNoPropertiesDataImagePresent() {
        WebElement propData = waitForElementToBeVisible(driver.findElement(By.xpath(noPropertiesDataImage)));
        return isElementVisible(propData);
    }

    @Step("Select properties scope from dropdown...")
    public void selectScope(String scope) {
        WebElement button = waitTillClickableWithFluentWait(scopeDropdown);
        click(button);
        enterTextSlowly(scopeInputField, scope);
    }

    @Step("Enter project properties path...")
    public void enterPath(String path) {
        inputText(pathInputField, path);
    }

    @Step("Enter resolve data...")
    public void enterResolveData(String resource) {
        enterTextSlowly(resolveInputField, resource);
    }

    @Step("Enter resolve data...")
    public void inputResolveData(String resource) {
        inputText(resolveInputField, resource);
    }

    @Step("Enter unix group...")
    public void enterUnixGroup(String group) {
        enterTextSlowly(unixGroupInputField, group);
    }
    @Step("Enter unix group...")
    public void inputUnixGroup(String group) {
        inputText(unixGroupInputField, group);
    }

    @Step("Select project properties mode from dropdown...")
    public void selectMode(String mode) {
        scrollToElement(modeDropdown);
        click(modeDropdown);
        WebElement button = waitForElementToBeClickable(driver.findElement(By.xpath(xpathDropdownChoice.replace("{name}", mode))));
        click(button);
    }

    @Step("Get number of the project properties added...")
    public int getNumberOfProjectProps(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(projectProperties), counter).size();
    }

    @Step("Verify property scope is present...")
    public boolean isPropertyScopePresent(String scope) {
        WebElement propScope = findElementWithWait(By.xpath(xpathScope.replace("{scope}", scope)));
        return isElementVisible(propScope);
    }

    @Step("Verify property path is present...")
    public boolean isPropertyPathPresent(String path) {
        WebElement propPath = findElementWithWait(By.xpath(xpathPath.replace("{path}", path)));
        return isElementVisible(propPath);
    }

    @Step("Hover over properties Mode field...")
    public void hoverOverPropertiesModeField() {
        hoverOverElement(modeDropdown);
    }

    @Step("Hover over properties Scope field...")
    public void hoverOverPropertiesScopeField() {
        hoverOverElement(scopeDropdown);
    }

    @Step("Get tooltip message...")
    public String getTooltipMessage() {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip));
        return getText(tooltip);
    }

    @Step("Verify property resolve data is present...")
    public String getResolveValue() {
        return getText(resolve);
    }

    @Step("Verify unix group is present...")
    public boolean isUnixGroupPresent(String group) {
        WebElement propUnixGroup = findElementWithWait(By.xpath(xpathUnixGroup.replace("{group}", group)));
        return isElementVisible(propUnixGroup);
    }

    @Step("Get project properties mode value...")
    public String getPropertyMode() {
        return getText(mode);
    }

    @Step("Verify project properties mode is not set...")
    public boolean noModeSet() {
        return isElementVisible(mode);
    }

    @Step("Click on project property...")
    public void selectProjectProperty(String propNumber) {
        WebElement button = findElementWithWait(By.xpath(xpathProjectProperty.replace("{index}", propNumber)));
        click(button);
    }

    @Step("Clicking on Add button...")
    public void clickOnAddButton() {
        try {
            click(addButton);
        } catch (Exception e) {
            scrollDownPage();
            waitForElementToBeClickable(addButton);
            click(addButton);
        }
    }

    @Step("Verify Add button is disabled...")
    public boolean isAddButtonDisabled() {
        return isElementVisible(addButtonDisabled);
    }

    @Step("Click on Update button...")
    public void clickOnUpdateButton() {
        click(updateButton);
    }

    @Step("Click on Remove button...")
    public void removeProjectProperty() {
        click(removeButton);
    }

    @Step("Verify form validation message is present...")
    public boolean isResolveFieldValidationMessagePresent() {
        return isElementVisible(resolveValidationMessage);
    }

    @Step("Verify that scope is present in dropdpwn menu")
    public boolean isScopeVisibleInDropDown(String scope) {
        WebElement button = waitForElementToBeClickable(scopeDropdown);
        click(button);
        WebElement scopeInDropDown = findElementWithWait(By.xpath(scopeInMenu.replace("{scope}", scope)));
        return isElementVisible(scopeInDropDown);
    }
}
