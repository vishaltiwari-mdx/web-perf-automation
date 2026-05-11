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
package com.methodics.phi.pageobject.queries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class QueryManagementButtonsPage extends BasePage {
    private WebDriver driver;

    public QueryManagementButtonsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//footer[contains(@class,'modal-footer')]//button[normalize-space()='Create Query']")
    private WebElement createQuery;

    @FindBy(xpath = "//footer[@class='modal-footer message-box__footer']//button[normalize-space()='Save Query']")
    private WebElement saveQueryModalButton;

    @FindBy(xpath = "//h2[@id='swal2-title' and text() = 'Save Query']")
    private List<WebElement> saveQueryModalDialog;

    private String saveQueryButton = "//button[normalize-space()='Save'][not(contains(@class,'disabled'))]";
    private String saveQueryButtonDisabled = "//button[normalize-space()='Save' and @disabled and @data-testid='ui-btn-query-actions-save']";
    private String saveCopyButton = "//button[normalize-space()='Create copy']";
    private String deleteButton = "//button[normalize-space()='Delete']";
    private String cancelButton = "//button[normalize-space()='Cancel']";

    @Step("Verifying Save button is enabled...")
    public boolean isSaveButtonEnabled() {
        return isElementEnabled(findElementWithWait(By.xpath(saveQueryButton)));
    }

    @Step("Verifying Save button is disabled...")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(findElementWithWait(By.xpath(saveQueryButtonDisabled)));
    }

    @Step("Verifying Save button is not displayed...")
    public boolean isSaveButtonNotDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(saveQueryButton))) == 0;
    }

    @Step("Clicking on Save query button...")
    public void clickOnSaveQueryButton() {
        click(findElementWithWait(By.xpath(saveQueryButton)));
    }

    @Step("Saving a new query...")
    public void saveQuery() {
        DriverFactory.sleep(500);
        clickOnSaveQueryButton();
        click(waitForElementToBeClickable(saveQueryModalButton));
    }

    @Step("Creating a copy of query...")
    public void createQueryCopy() {
        clickOnSaveCopyButton();
        click(waitForElementToBeClickable(createQuery));
    }

    @Step("Clicking on Save Copy button...")
    public void clickOnSaveCopyButton() {
        click(findElementWithWait(By.xpath(saveCopyButton)));
    }

    @Step("Verifying Save Copy button is not displayed...")
    public boolean isSaveCopyButtonNotDisplayed() {
        return driver.findElements(By.xpath(saveCopyButton)).size() == 0;
    }

    @Step("Verifying Save Copy button is displayed...")
    public boolean isSaveCopyButtonDisplayed() {
        return isElementVisible(findElementWithWait(By.xpath(saveCopyButton)));
    }

    @Step("Clicking on Delete query button...")
    public void clickOnDeleteButton() {
        click(findElementWithWait(By.xpath(deleteButton)));
    }

    @Step("Verifying Delete button is not displayed...")
    public boolean isDeleteButtonNotDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(deleteButton))) == 0;
    }

    @Step("Verifying Cancel button is disabled...")
    public boolean isCancelButtonDisabled() {
        return !isElementEnabled(findElementWithWait(By.xpath(cancelButton)));
    }

    @Step("Verifying Cancel button is enabled...")
    public boolean isCancelButtonEnabled() {
        return isElementEnabled(findElementWithWait(By.xpath(cancelButton)));
    }

    @Step("Verifying Cancel button is not displayed...")
    public boolean isCancelButtonNotDisplayed() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(cancelButton))) == 0;
    }

    @Step("Clicking on Cancel button...")
    public void clickOnCancelButton() {
        click(findElementWithWait(By.xpath(cancelButton)));
    }

    @Step("Verifying Save Query modal dialog is not present...")
    public boolean isSaveQueryModalNotDisplayed() {
        return getNumberOfVisibleElements(saveQueryModalDialog) == 0;
    }
}