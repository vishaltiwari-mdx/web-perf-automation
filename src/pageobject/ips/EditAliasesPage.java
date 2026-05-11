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
import org.testng.Assert;

public class EditAliasesPage extends BasePage {

    private WebDriver driver;

    public EditAliasesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@id='aliases']")
    private WebElement aliasInputField;

    @FindBy(xpath = "//div[@class='multiselect__tags']")
    private WebElement aliasesSelectArrow;

    @FindBy(xpath = "//div[contains(@class,'scrollable h-max')]")
    private WebElement aliasesContainerScroll;

    @FindBy(xpath = "//h4[contains(.,'Mark aliases as Unique')]")
    private WebElement uniqueAliasesModalTitle;

    @FindBy(css = "button[data-testid='ui-btn-edit-aliases-modal-close']")
    private WebElement closeAliasModal;

    private String aliasDropdownElement = "//li[@class='multiselect__element' and contains(.,'{alias}')]";
    private String xpathDeleteAliasButton = "//span[text()='{alias}']/following-sibling::i[@class='multiselect__tag-icon']";
    private String xpathAddedAlias = "//span[@title='{alias}']//span[@class='multiselect__tag-option']";
    private String cancelButton = "//button[@data-testid='ui-btn-edit-aliases-cancel']";
    private String checkboxUniqueAlias = "//input[contains(@class,'cbx')]";
    private String doneButton = "//button[@data-testid='ui-btn-edit-aliases-save']";
    private String doneButtonDisabled = "//button[@data-testid='ui-btn-edit-aliases-save' and @disabled]";
    private String aliasesModalDialog = "//header[@class='modal-header']//h5[text()='Edit Aliases']";
    private String uniqueOptionSelected = "//div[contains(@class, 'unique-aliases')]//input[@type='checkbox']/following::span[normalize-space()='+{name}'][@class='text-truncate'][@title='+{name}']";
    private String suggestedAliases = "//span[contains(@class, 'multiselect__option')]//div[normalize-space()='{option}'][@title='{option}']";

    @Step("Enter alias name: {0}, for the method: {method}...")
    public void enterAliasName(String aliasName) {
        openAliasesDropdown();
        enterTextSlowly(aliasInputField, aliasName);
        pressEnter(aliasInputField);
    }

    @Step("Click on Confirm Button...")
    public void clickOnSaveButton() {
        WebElement element = findElementWithWait(By.xpath(doneButton));
        clickWithJS(element);
    }

    @Step("Open aliases dropdown...")
    public void openAliasesDropdown() {
        click(aliasesSelectArrow);
    }

    @Step("Add standard alias: {0}, for the method: {method}...")
    public void addAlias(String aliasName) {
        openAliasesDropdown();
        enterTextSlowly(aliasInputField, aliasName);
        WebElement targetAlias = findElementWithWait(By.xpath(aliasDropdownElement.replace("{alias}", aliasName)));
        click(targetAlias);
        clickOnSaveButton();
        // sleep needed for stable regression
        DriverFactory.sleep(1000);
    }

    @Step("Add built in alias and verify that the save button is disabled...")
    public boolean isSaveButtonDisabledWhenAddingBuiltInAlias(String aliasName) {
        Assert.assertEquals(isAliasSuggested(aliasName, 0), 0, "Suggested element is present");
        enterTextSlowly(aliasInputField, aliasName);
        WebElement disabledSaveButton = findElementWithWait(By.xpath(doneButtonDisabled));
        return isElementVisible(disabledSaveButton);
    }

    @Step("Click on Cancel Button...")
    public void clickOnCancelButton() {
        WebElement element = findElementWithWait(By.xpath(cancelButton));
        clickWithJS(element);
    }

    @Step("Check Unique Alias checkbox...")
    public void markAliasAsUnique() {
        WebElement element = findElementWithWait(By.xpath(checkboxUniqueAlias));
        clickWithJS(element);
    }

    @Step("Uncheck Unique Alias checkbox...")
    public void deselectUniqueAliasOption() {
        WebElement element = findElementWithWait(By.xpath(checkboxUniqueAlias));
        clickWithJS(element);
    }

    @Step("Add unique alias: {0}, for the method: {method}...")
    public void addUniqueAlias(String aliasName) {
        enterAliasName(aliasName);
        markAliasAsUnique();
        clickOnSaveButton();
        // sleep needed for stable regression
        DriverFactory.sleep(1000);
    }

    @Step("Delete unique alias...")
    public void deleteAlias(String alias) {
        WebElement deleteAliasButton = findElementWithWait(By.xpath(xpathDeleteAliasButton.replace("{alias}", alias)));
        clickWithJS(deleteAliasButton);
        clickOnSaveButton();
    }

    @Step("Verify that aliases modal is closed...")
    public boolean isAliasesModalClosed() {
        return waitForNumberOfElementsToBe(By.xpath(aliasesModalDialog), 0).size() == 0;
    }

    @Step("Verify delete alias button is not present ...")
    public int isDeleteAliasButtonNotDisplayed(String alias, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathDeleteAliasButton.replace("{alias}", alias)), counter).size();
    }

    @Step("Verify unique alias option is unavailable ...")
    public boolean isUniqueAliasOptionUnavaliable() {
        return !isElementVisible(uniqueAliasesModalTitle);
    }

    @Step("Verify aliases list container is scrollable...")
    public boolean isAliasesListScrollable() {
        return isElementVisible(aliasesContainerScroll);
    }

    @Step("Verifying unique aslias checkbox is checked...")
    public boolean isUniqueAliasOptionSelected(String alias) {
        WebElement checkboxSelected = findElementWithWait(By.xpath(uniqueOptionSelected.replace("{name}", alias)));
        return isElementVisible(checkboxSelected);
    }

    @Step("Get number of added aliases...")
    public int getNumberOfAddedAliases(String alias, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathAddedAlias.replace("{alias}", alias)), counter).size();
    }

    @Step("Verify that alias is not displayed in suggested list...")
    public int isAliasSuggested(String aliasName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(suggestedAliases.replace("{option}", aliasName)), counter).size();
    }
    @Step("Click on close modal button on Aliases popup...")
    public void closeAliasModal() {
        waitForPageLoaded();
        hoverOverElement(closeAliasModal);
        waitForElementToBeClickable(closeAliasModal).click();
    }
}
