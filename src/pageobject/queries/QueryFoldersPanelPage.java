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

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class QueryFoldersPanelPage extends BasePage {
    private WebDriver driver;
    private static final String ADVANCED_SEARCH_TITLE = "Advanced Search";

    public QueryFoldersPanelPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[@class='minimized-text']//b")
    private WebElement collapsedFoldersPanel;

    @FindBy(xpath = "//div[contains(@class,'side-panel')]//*[contains(@class,'fa-arrow-right-from-line')]")
    private WebElement openButton;

    @FindBy(xpath = "//div[contains(@class,'side-panel')]//*[contains(@class,'fa-arrow-left-from-line')]")
    private WebElement collapseButtonClose;

    @FindBy(xpath = "//button[normalize-space()='History']")
    private WebElement historyButton;

    @FindBy(xpath = "//button[@class='btn btn-secondary btn-md list-group-item list-group-item-action active'][@data-testid='ui-btn-ui-list-group-item']")
    private WebElement historySelected;

    @FindBy(xpath = "//button[contains(@class, 'list-group-item list-group-item-action')]//i")
    private List<WebElement> folders;

    @FindBy(xpath = "//input[@placeholder='Search Query Folders']")
    private WebElement foldersSearchInputField;

    @FindBy(xpath = "//form[contains(@class,'query-search-field_form')]//i[@class='fa fa-times-circle-o']")
    private WebElement clearSearchButton;

    @FindBy(xpath = "//div[contains(@class,'col align-self-center') and contains(.,'No folders found.')]")
    private WebElement noFoldersMessage;

    private String xpathFolderName = "//div[@title='{name}'][@class='name text-truncate']";
    private String xpathSelectedFolder = "//button[contains(@class,'active') and @data-testid='ui-btn-ui-list-group-item']//div[@title='{name}']";
    private String xpathFolderTooltip = "//div[@class='tooltip-inner' and text()='{name}']";
    private String xpathFolderIcon = "//div[normalize-space()='{name}']//preceding-sibling::i[contains(@class, 'fa-folder-magnifying-glass')]";

    @Step("Opening folders panel...")
    public void openFoldersPanel() {
        click(openButton);
    }

    @Step("Collapsing folders panel...")
    public void collapseFoldersPanel() {
        click(collapseButtonClose);
    }

    @Step("Getting collapsed panel header...")
    public String getCollapsedPanelHeader() {
        return getText(collapsedFoldersPanel);
    }

    @Step("Getting number of folders...")
    public int getNumberOfFolders() {
        return getNumberOfElements(folders);
    }

    @Step("Verifying folder is not present...")
    public boolean folderNotDisplayed(String folderName) {
        return driver.findElements(By.xpath(xpathFolderName.replace("{name}", folderName))).size() == 0;
    }

    @Step("Clicking on folder...")
    public void clickOnFolder(String folderName) {
        WebElement folder = findElementWithWait(By.xpath(xpathFolderName.replace("{name}", folderName)));
        click(folder);
    }

    @Step("Clicking on History item...")
    public void clickOnHistory() {
        click(historyButton);
    }

    @Step("Verifying folder is displayed...")
    public boolean isFolderDisplayed(String folderName) {
        return isElementVisible(findElementWithWait(By.xpath(xpathFolderName.replace("{name}", folderName))));
    }

    @Step("Verifying folder is selected...")
    public boolean isFolderSelected(String folderName) {
        return isElementVisible(findElementWithWait(By.xpath(xpathSelectedFolder.replace("{name}", folderName))));
    }

    @Step("Verifying History is selected...")
    public boolean isHistorySelected() {
        return isElementVisible(historySelected);
    }

    @Step("Verifying History is displayed...")
    public boolean isHistoryDisplayed() {
        return isElementVisible(historyButton);
    }

    @Step("Entering folder name...")
    public void enterFolderName(String name) {
        inputText(foldersSearchInputField, name);
    }

    @Step("Remove folder name from the search field by clicking on clear button...")
    public void removeFolderNameByClickingOnClearButton() {
        click(clearSearchButton);
    }

    @Step("Remove folder name from the search field...")
    public void removeFolderNameFromSearchInputField() {
        clearInputFieldWithBackspace(foldersSearchInputField);
    }

    @Step("Verifying no folders are displayed...")
    public boolean isNoFoldersMessagePresent() {
        return isElementVisible(noFoldersMessage);
    }

    @Step("Verifying folder icon is present...")
    public boolean isFolderIconDisplayed(String queryName) {
        WebElement folderIcon = findElementWithWait(By.xpath(xpathFolderIcon.replace("{name}", queryName)));
        return isElementVisible(folderIcon);
    }
}
