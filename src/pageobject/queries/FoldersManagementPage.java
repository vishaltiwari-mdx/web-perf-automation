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
package com.methodics.phi.pageobject.queries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FoldersManagementPage extends BasePage {
    private WebDriver driver;
    private static final String QUERY_FOLDER_MANAGEMENT_TITLE = "Query Folders";

    public FoldersManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[contains(@class, 'create-button')]")
    private WebElement createNewFolderButton;

    @FindBy(xpath = "//button[contains(@class, 'save-button_enabled')]")
    private WebElement saveButtonEnabled;

    @FindBy(xpath = "//button[contains(@class, 'save-button') and @disabled]")
    private WebElement saveButtonDisabled;

    @FindBy(xpath = "//button[contains(@class, 'edit__cancel-button')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[contains(@class, 'edit__remove-button')]")
    private WebElement deleteButton;

    @FindBy(xpath = "//input[@name='name']")
    private WebElement folderNameField;

    @FindBy(xpath = "//textarea[@name='description']")
    private WebElement folderDescriptionField;

    @FindBy(xpath = "//div[contains(@class, 'panel-search')]//input")
    private WebElement searchFoldersInputField;

    @FindBy(xpath = "//i[contains(@class, 'panel-search__search-icon fa-circle-xmark')]")
    private WebElement cancelSearchButton;

    @FindBy(xpath = "//div[contains(@class,'page-name') and normalize-space()='Query Folders']")
    private WebElement queryFolderSubHeader;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]")
    private WebElement defaultStateIcon;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]//i[contains(@class, 'fa-folder-magnifying-glass')]")
    private WebElement queryFolderPageIcon;

    @FindBy(xpath = "//button[contains(@class, 'query-folders-list')]//i[contains(@class, 'fa-folder-magnifying-glass')]")
    private WebElement queryFolderIcon;

    @FindBy(xpath = "//span[text()='Create a folder or select a folder to edit.']")
    private WebElement createFolderInfoText;

    private String folders = "//div[contains(@class,'d-flex')]/div[contains(@class, 'folders-list__item-name')]";
    private String xpathFolderName = "//div[@title='{name}'][contains(@class,'text-truncate')]";
    private String xpathQueryFolderHeader = "//span[normalize-space()='Query Folders']/following-sibling::span[@title='{name}'][@class='text-truncate']";

    @Step("Go to Query Folders Management page...")
    public void goToQueryFoldersManagementPage(String url) {
        goTo(url);
    }

    @Step("Click on Create New Folder button...")
    public void clickOnCreateNewFolderButton() {
        click(createNewFolderButton);
    }

    @Step("Enter folder name...")
    public void enterFolderName(String name) {
        inputText(folderNameField, name);
    }

    @Step("Enter folder description...")
    public void enterFolderDescription(String name) {
        inputText(folderDescriptionField, name);
    }

    @Step("Click on Save button...")
    public void clickSaveButton() {
        click(saveButtonEnabled);
    }

    @Step("Type folder name...")
    public void enterFolderNameToSearchField(String folderName) {
        inputText(searchFoldersInputField, folderName);
    }

    @Step("Click on folder...")
    public FoldersManagementQuerySectionPage clickOnFolder(String folderName) {
        DriverFactory.sleep(1000);
        WebElement folder = findElementWithWait(By.xpath(xpathFolderName.replace("{name}", folderName)));
        folder.click();
        DriverFactory.sleep(1000);
        return new FoldersManagementQuerySectionPage(driver);
    }

    @Step("Get number of folders...")
    public int getNumberOfFolders() {
        return driver.findElements(By.xpath(folders)).size();
    }

    @Step("Get folder name...")
    public String getFolderName() {
        return getAttribute(folderNameField);
    }

    @Step("Get folder description...")
    public String getFolderDescription() {
        return getAttribute(folderDescriptionField);
    }

    @Step("Clear folder name field...")
    public void clearFolderName() {
        clearInputField(folderNameField);
    }

    @Step("Verify Save button is disabled...")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(saveButtonDisabled);
    }

    @Step("Creat a new folder...")
    public void createNewFolder(String name) {
        clickOnCreateNewFolderButton();
        inputText(folderNameField, name);
        clickSaveButton();
    }

    @Step("Get page info description...")
    public String getPageInfoDescription() {
        return getText(defaultStateIcon);
    }

    @Step("Click on Cancel button...")
    public void clickCancelButton() {
        click(cancelButton);
    }

    @Step("Verify Create folder title is displayed...")
    public boolean isCreateFolderTitlePresent() {
        return isElementVisible(createFolderInfoText);
    }

    @Step("Click on Delete button...")
    public void clickOnDeleteButton() {
        click(deleteButton);
    }

    @Step("Delete folder...")
    public void deleteFolder(String name) {
        clickOnFolder(name);
        clickOnDeleteButton();
    }

    @Step("Clear Search Folders field...")
    public void removeFolderNameFromSearchInputFieldByClickingCancelButton() {
        waitForElementToBeClickable(cancelSearchButton);
        click(cancelSearchButton);
    }

    @Step("Remove folder name from the search field...")
    public void removeFolderNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchFoldersInputField);
    }

    @Step("Verify Query folder sub-header is displayed...")
    public boolean isQueryFolderSubHeaderPresent() {
        return isElementVisible(queryFolderSubHeader);
    }

    @Step("Verify sub-header for selected query folder is displayed...")
    public boolean isSelectedQueryFolderSubHeaderPresent(String folder) {
        WebElement queryFolderHeader = findElementWithWait(By.xpath(xpathQueryFolderHeader.replace("{name}", folder)));
        return isElementVisible(queryFolderHeader);
    }

    @Step("Verify Query Folders page icon displayed...")
    public boolean isQueryFolderPageIconDisplayed() {
        return isElementVisible(queryFolderPageIcon);
    }

    @Step("Verify Query Folder icon displayed...")
    public boolean isQueryIconDisplayed() {
        return isElementVisible(queryFolderIcon);
    }

    @Step("Click to create New folder and enter details of folder..")
    public void clickToCreateButtonAndEnterFolderName(String name) {
        clickOnCreateNewFolderButton();
        inputText(folderNameField, name);
    }
}
