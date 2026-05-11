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

public class CopyIpPage extends BasePage {
    private WebDriver driver;

    public CopyIpPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//textarea[@name='version_message']")
    private WebElement versionMessageInputField;

    @FindBy(xpath = "//input[@placeholder='Enter repo path']")
    private WebElement repoPathInputField;

    @FindBy(xpath = "//a[normalize-space()='Copy repo path from IPV']")
    private WebElement copyRepoPathButton;

    @FindBy(xpath = "//button[@type='submit'][normalize-space()='Create copy']")
    private WebElement createCopyButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelCopyButton;

    private String xpathLibName = "//div[contains(@class, 'truncate')]/span[@class='library-selector__option-title'][@title='{title}']";
    private String librariesDropdown = "//span[normalize-space()='Select Library']";
    private String newIpNameInputField = "//input[@name='name']";
    private String selectedLibrary = "//div[contains(@class,'multiselect library-selector')][@title='{lib}']";
    private String newIpLibraryName = "//span[contains(@class, 'text-ellipsis')]";
    private String modalHeader = "//span[contains(@class, truncate)][@title='{ipv}']/parent::h4[contains(@class, 'modal-title')]";

    @Step("Selecting Library from dropdown in Copy IP modal...")
    public void selectLibrary(String libName) {
        waitForPageLoaded();
        WebElement libDropDown = waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(librariesDropdown)));
        click(libDropDown);
        WebElement selectedLibrary = findElementWithWait(By.xpath(xpathLibName.replace("{title}", libName)));
        click(selectedLibrary);
    }

    @Step("Entered IP name: {0}, for the method: {method}...")
    public void enterIpName(String ipName) {
        WebElement field = findElementWithWait(By.xpath(newIpNameInputField));
        click(field);
        inputText(field, ipName);
    }

    @Step("Entered Repo Path: {0}, for the method: {method}...")
    public void enterRepoPath(String repoPath) {
        inputText(repoPathInputField, repoPath);
    }

    @Step("Entered Version Message: {0}, for the method: {method}...")
    public void enterVersionMessage(String mssg) {
        inputText(versionMessageInputField, mssg);
    }

    @Step("Clicking on Copy Repo Path from IPv button...")
    public void clickOnCopyRepoPathButton() {
        click(copyRepoPathButton);
    }

    @Step("Clicking on Create Copy button...")
    public void clickOnCreateCopyButton() {
        click(createCopyButton);
    }

    @Step("Clicking on Cancel Copy button...")
    public void clickOnCancelCopyButton() {
        click(cancelCopyButton);
    }

    @Step("Creating IP copy...")
    public void copyIp(String libName, String ipName, String repoPath, String mssg) {
        waitForPageLoaded();
        DriverFactory.sleep(2000);
        selectLibrary(libName);
        enterIpName(ipName);
        enterRepoPath(repoPath);
        enterVersionMessage(mssg);
        clickOnCreateCopyButton();
    }

    @Step("Get modal title")
    public String getModalTitle(String ipName) {
        WebElement modalTitle = findElementWithWait(By.xpath(modalHeader.replace("{ipv}", ipName)));
        return getText(modalTitle);
    }

    @Step("Verify library is selected")
    public boolean isLibrarySelected(String lib) {
        WebElement selectedLib = findElementWithWait(By.xpath(selectedLibrary.replace("{lib}", lib)));
        return isElementVisible(selectedLib);
    }

    @Step("Verify library name is displayed for new IP")
    public String getLibNameOfNewIp() {
        WebElement library = findElementWithWait(By.xpath(newIpLibraryName));
        return getText(library).replace(".","");
    }
}
