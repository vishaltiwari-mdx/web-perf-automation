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

public class CreateLinePage extends BasePage {
    private WebDriver driver;

    public CreateLinePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@placeholder='Enter IP Line name']")
    private WebElement lineNameInputField;

    @FindBy(xpath = "//input[@placeholder='Enter repo path']")
    private WebElement repoPathInputField;

    @FindBy(xpath = "//textarea[@placeholder='Enter IP Version message']")
    private WebElement versionMessageInputField;

    @FindBy(xpath = "//a[contains(.,'Copy repo path from IPV')]")
    private WebElement copyRepoPathButton;

    @FindBy(xpath = "//div[@class='modal-content shadow']//button[@data-testid='ui-btn-create-line-create']")
    private WebElement createNewLineButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    private String modalHeader = "//span[contains(@class, truncate)][@title='{ipv}']/parent::h4[contains(@class, 'modal-title')]";

    @Step("Entered new Line name: {0}, for the method: {method}...")
    public void enterLineName(String ipName) {
        waitForPageLoaded();
        inputText(lineNameInputField, ipName);
    }

    @Step("Entered Repo Path: {0}, for the method: {method}...")
    public void enterRepoPath(String repoPath) {
        waitForPageLoaded();
        inputText(repoPathInputField, repoPath);
    }

    @Step("Entered Version Message: {0}, for the method: {method}...")
    public void enterVersionMessage(String mssg) {
        waitForPageLoaded();
        inputText(versionMessageInputField, mssg);
    }

    @Step("Clicking on Copy Repo Path from IPv button...")
    public void clickOnCopyRepoPathButton() {
        click(copyRepoPathButton);
    }

    @Step("Clicking on Create New Line button...")
    public void clickOnCreateButton() {
        click(createNewLineButton);
        DriverFactory.sleep(2500);
    }

    @Step("Clicking on Cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Creating New Line...")
    public void createNewLine(String lineName, String repoPath, String mssg) {
        enterLineName(lineName);
        enterRepoPath(repoPath);
        enterVersionMessage(mssg);
        clickOnCreateButton();
    }

    @Step("Getting repoPath prefilled value...")
    public String getRepoPathPrefilledValue() {
        return getAttribute(repoPathInputField);
    }

    @Step("Verifying Copy Repo Path from IPV button is not present...")
    public int isCopyRepoPathOptionNotAvailable(int counter) {
        String element = "//a[contains(.,'Copy Repo Path from IPV')]";
        return waitForNumberOfElementsToBe(By.xpath(element), counter).size();
    }

    @Step("Verifying Repo Path field is not present...")
    public int isRepoPathFieldNotPresent(int counter) {
        String element = "//input[@placeholder='Repository Path']";
        return waitForNumberOfElementsToBe(By.xpath(element), counter).size();
    }

    @Step("Get modal title")
    public String getModalTitle(String ipName) {
        WebElement modalTitle = findElementWithWait(By.xpath(modalHeader.replace("{ipv}", ipName)));
        return getText(modalTitle);
    }
}
