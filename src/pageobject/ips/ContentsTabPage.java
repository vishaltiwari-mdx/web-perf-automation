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

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ContentsTabPage extends BasePage {
    private WebDriver driver;

    public ContentsTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@col-id='name'][contains(@class,'ag-cell')]")
    private List<WebElement> files;

    @FindBy(xpath = "//button/i[@class='fa fa-minus']")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[text()='Delete']")
    private WebElement deleteButtonModal;

    @FindBy(xpath = "//button[@type='button' and text()='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[@class='multiselect__select']")
    private WebElement selectFolderDropdown;

    @FindBy(xpath = "//span[@class='multiselect__single' and text()='--no_folder_selected--']")
    private WebElement noFolderSelected;

    @FindBy(xpath = "//i[@class='fa fa-folder-open-o']")
    private WebElement createFolderButton;

    @FindBy(xpath = "//input[@placeholder='Folder name']")
    private WebElement folderInputField;

    @FindBy(xpath = "//div[contains(., 'Create New Folder')]//button[@type='button' and text()='Cancel']")
    private WebElement cancelButtonOnModal;

    @FindBy(xpath = "//button/i[@class='fa fa-plus']")
    private WebElement uploadFileButton;

    @FindBy(xpath = "//button[normalize-space()='Save Files']")
    private WebElement saveFilesButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    private WebElement cancelFileUploadButton;

    @FindBy(xpath = "//b[normalize-space()='Selected file to upload:']/following-sibling::span")
    private WebElement fileToUpload;

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveFolderButton;

    @FindBy(xpath = "//button[text()='OK']")
    private WebElement okModalButton;

    @FindBy(xpath = "//p[@class='card-text']")
    private WebElement cardText;

    @FindBy(xpath = "//button[@data-testid='ui-btn-grid-pagination-go-to-first-page']//i[contains(@class, 'fa-angles-left')]")
    private WebElement goToFirstPage;

    @FindBy(xpath = "//button[@data-testid='ui-btn-grid-pagination-go-to-previous-page']//i[contains(@class, 'fa-angle-left')]")
    private WebElement goToPreviousPage;

    @FindBy(xpath = "//button[@data-testid='ui-btn-grid-pagination-go-to-next-page']//i[contains(@class, 'fa-angle-right')]")
    private WebElement goToNextPage;

    @FindBy(xpath = "//button[@data-testid='ui-btn-grid-pagination-go-to-last-page']//i[contains(@class, 'fa-angles-right')]")
    private WebElement goToLastPage;

    @FindBy(xpath = "//input[@placeholder='Search file']")
    private WebElement searchField;

    @FindBy(xpath = "//div[contains(@class, 'alert--piforce-not-available')]")
    private WebElement piForceNotAvailableMessage;

    private String xpathFile = "//span[@role='button' and normalize-space()='{name}']";
    private String xpathExpandFolderButton =
            "//span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eContracted']";
    private String xpathCollapseFolderButton =
            "//span[contains(@class,'ag-row-group-indent') and contains(., '{name}')]//span[@data-ref='eExpanded']";
    private String xpathNumberOfFilesInFolder = "//span[contains(., '{name}')]//span[@class='ag-group-child-count']";
    private String xpathFileRow = "//span[normalize-space()='{name}']//ancestor::span/ancestor::div[@role='row']";
    private String xpathFolderChoiceDropdown = "//li[@class='multiselect__element' and normalize-space()='{name}']";
    private String xpathFolder = "//span[@class='fa fa-folder-o']/following-sibling::span[text()='{name}']";
    private String xpathVersion = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='revision']/span";
    private String folders = "//span[@class='fa fa-folder-o']";
    private String noDataAvailable = "//div[@class='ag-overlay']//h5";
    private String xpathChangelistRevision = "//div[@class='col text-end']";
    private String xpathGoToPage = "//*[@class='page-link' and text()='{pageNumber}']";
    private String xpathSelectedPageNumber = "//span[@class='mdx-paging-description mx-3']//span[@class='mdx-ag-paging-panel-number me-1']";
    private String xpathSelectedLastPageNumber = "//span[@class='mdx-paging-description mx-3']//span[@class='mdx-ag-paging-panel-number']";
    private String xpathNumberOfFilesOnPage = "//span[contains(normalize-space(), '{name}')]/ancestor::div/div[@col-id='name']";
    private String xpathDownloadIconDisabled = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='name']//button[@class='btn btn-line-action-default-regular btn-md disabled']/i[contains(@class,'fa-arrow-down')]";
    private String xpathPreviewIconDisabled = "//span[normalize-space()='{filePath}']/ancestor::div/div[@col-id='name']//button[@class='btn btn-line-action-default-regular btn-md disabled me-3']/i[contains(@class,'fa-glasses-round')]";
    private String xpathPreviewIconEnabled = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='name']//button[@class='btn btn-line-action-default-regular btn-md me-3']/i[contains(@class,'fa-glasses-round')]";
    private String xpathDownloadIconEnabled = "//span[normalize-space()='{name}']/ancestor::div/div[@col-id='name']//button[@class='btn btn-line-action-default-regular btn-md']/i[contains(@class,'fa-arrow-down-to-bracket')]";
    private String xpathPreviewDataNewTab = "//body";
    private String xpathNonFusaFile = "//span[normalize-space()='{text}']/ancestor::div/div[@col-id='name']";
    private String xpathActionButtons = "//td[contains(@class,'tab-contents__action-buttons')]";
    private String xpathGoToLastPageDisabled = "//button[@class='btn btn-line-action-default-small btn-md disabled'][@data-testid='ui-btn-grid-pagination-go-to-last-page']";

    @Step("Getting filelist after search ...")
    public String getFilePathAfterSearch(String filePath) {
        WebElement filePathAfterSearch = findElementWithWait(By.xpath(xpathNonFusaFile.replace("{text}", filePath)));
        return getText(filePathAfterSearch);
    }

    @Step("Clicking on file to download...")
    public void clickOnFileToDownload(String fileName) {
        WebElement file = findElementWithWait(By.xpath(xpathFile.replace("{name}", fileName)));
        click(file);
    }

    @Step("Clicking on folder...")
    public void clickOnFolder(String folderName) {
        WebElement expandFolderButton = findElementWithWait(By.xpath(xpathExpandFolderButton.replace("{name}", folderName)));
        click(expandFolderButton);
    }

    @Step("Getting number of folders...")
    public int getNumberOfFolders(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(folders), counter).size();
    }

    @Step("Verify the file path is present in Contents tab...")
    public boolean isFilePathPresent(String filePath) {
        WebElement file = findElementWithWait(By.xpath(xpathNonFusaFile.replace("{text}", filePath)));
        return isElementVisible(file);
    }

    @Step("Verify the file path is present in Contents tab...")
    public int isFilePathNotPresent(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathNonFusaFile), counter).size();
    }

    @Step("Getting number of files in folder...")
    public String getNumberOfFilesInFolderFromIcon(String folderName) {
        WebElement numberOfFilesInFolder = findElementWithWait(By.xpath(xpathNumberOfFilesInFolder.replace("{name}", folderName)));
        return getText(numberOfFilesInFolder);
    }

    @Step("Getting number of files on page...")
    public int getNumberOfFilesOnPage(String filePath, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathNumberOfFilesOnPage.replace("{name}", filePath)), counter).size();
    }

    @Step("Getting number of files on page...")
    public int getPreviousPageNumber() {
        WebElement pagingPanelNumber = findElementWithWait(By.xpath(xpathSelectedLastPageNumber));
        String lastPageNumber = getText(pagingPanelNumber);
        return Integer.parseInt(lastPageNumber) - 1;
    }

    @Step("Clicking on delete file button...")
    public void clickOnDeleteFileButton() {
        click(deleteButton);
    }

    @Step("Clicking on file row...")
    public void clickOnFileRow(String fileName) {
        WebElement fileRow = findElementWithWait(By.xpath(xpathFileRow.replace("{name}", fileName)));
        click(fileRow);
    }

    @Step("Getting number of files in folder...")
    public int getNumberOfFiles(String fileName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathFile.replace("{name}", fileName)), counter).size();
    }

    @Step("Clicking on delete button in modal dialog...")
    public void clickOnDeleteButton() {
        click(deleteButtonModal);
    }

    @Step("Clicking on cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Deleting a file...")
    public void deleteFile(String fileName) {
        clickOnFileRow(fileName);
        clickOnDeleteFileButton();
        clickOnDeleteButton();
    }

    @Step("Clicking on First Page button...")
    public void clickOnFirstPageButton() {
        click(goToFirstPage);
    }

    @Step("Clicking on Previous Page button...")
    public void clickOnPreviousPageButton() {
        click(goToPreviousPage);
    }

    @Step("Clicking on Page button...")
    public void clickOnPage(String pageNumber) {
        WebElement expandFolderButton = findElementWithWait(By.xpath(xpathGoToPage.replace("{pageNumber}", pageNumber)));
        click(expandFolderButton);
    }

    @Step("Clicking on Next Page button...")
    public void clickOnNextPageButton() {
        click(goToNextPage);
    }

    @Step("Clicking on Last Page button...")
    public void clickOnLastPageButton() {
        click(goToLastPage);
    }

    @Step("Verify the last page button is disabled in Contents tab...")
    public boolean isLastPageButtonDisabled() {
        WebElement goLastPageDisabled = findElementWithWait(By.xpath(xpathGoToLastPageDisabled));
        return isElementVisible(goLastPageDisabled);
    }

    @Step("Clicking on Download icon...")
    public void clickOnDownloadIcon(String filePath) {
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathDownloadIconEnabled.replace("{name}", filePath)));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", downloadIcon);
    }

    @Step("Clicking on Preview icon...")
    public void clickOnPreviewIcon(String filePath) {
        WebElement previewIcon = findElementWithWait(By.xpath(xpathPreviewIconEnabled.replace("{name}", filePath)));
        click(previewIcon);
    }

    @Step("Hover over Download icon...")
    public void hoverOverDownloadIcon(String filePath) {
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathDownloadIconEnabled.replace("{name}", filePath)));
        hoverOverElement(downloadIcon);
    }

    @Step("Hover over Preview icon...")
    public void hoverOverPreviewIcon(String filePath) {
        WebElement previewIcon = findElementWithWait(By.xpath(xpathPreviewIconEnabled.replace("{name}", filePath)));
        hoverOverElement(previewIcon);
        DriverFactory.sleep(1500);
    }

    @Step("Getting text of preview tab...")
    public String getTextFromPreviewTab() {
        DriverFactory.sleep(1000);
        switchToOpenedTab();
        WebElement previewText = findElementWithWait(By.xpath(xpathPreviewDataNewTab));
        return getText(previewText);
    }

    @Step("Opening folders dropdown...")
    public void openDropdownMenu() {
        click(selectFolderDropdown);
    }

    @Step("Verifying folder is present in dropdown...")
    public int isFolderPresentInDropdown(String folderName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathFolderChoiceDropdown.replace("{name}", folderName)), counter).size();
    }

    @Step("Verifying correct page is selected...")
    public String isPageSelected() {
        WebElement pageNumber = findElementWithWait(By.xpath(xpathSelectedPageNumber));
        return getText(pageNumber);
    }

    @Step("Verifying download icon is disabled...")
    public boolean isDownloadIconDisabled(String filePath) {
        WebElement fileName = findElementWithWait(By.xpath(xpathNonFusaFile.replace("{text}", filePath)));
        hoverOverElement(fileName);
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathDownloadIconDisabled.replace("{name}", filePath)));
        return isElementVisible(downloadIcon);
    }

    @Step("Verifying download icon is enabled...")
    public boolean isDownloadIconEnabled(String filePath) {
        WebElement fileName = findElementWithWait(By.xpath(xpathNonFusaFile.replace("{text}", filePath)));
        hoverOverElement(fileName);
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathDownloadIconEnabled.replace("{name}", filePath)));
        return isElementVisible(downloadIcon);
    }

    @Step("Verifying preview icon is disabled...")
    public boolean isPreviewIconDisabled(String filePath) {
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathPreviewIconDisabled.replace("{filePath}", filePath)));
        return isElementVisible(downloadIcon);
    }

    @Step("Verifying preview icon is enabled...")
    public boolean isPreviewIconEnabled(String filePath) {
        WebElement downloadIcon = findElementWithWait(By.xpath(xpathPreviewIconEnabled.replace("{name}", filePath)));
        return isElementVisible(downloadIcon);
    }

    @Step("Verify Preview and Download icons are not displayed...")
    public int buttonsAreNotDisplayed(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathActionButtons), counter).size();
    }

    @Step("Verifying folder is not selected in Select Folder menu...")
    public boolean isNoFolderSelected() {
        return isElementVisible(noFolderSelected);
    }

    @Step("Verify changelist revision message not displayed...")
    public String isChangelistRevisionMessageNotDisplayed() {
        WebElement changelist = driver.findElement(By.xpath(xpathChangelistRevision));
        return changelist.getText();
    }

    @Step("Getting changelist revision message...")
    public String getChangelistRevisionMessage() {
        WebElement revision = findElementWithWait(By.xpath(xpathChangelistRevision));
        return getText(revision);
    }

    @Step("Getting no data available message...")
    public String getNoDataAvailableMessage() {
        WebElement noDataMessage = findElementWithWait(By.xpath(noDataAvailable));
        return getText(noDataMessage);
    }

    @Step("Clicking on Create New Folder button...")
    public void clickOnCreateNewFolderButton() {
        click(createFolderButton);
    }

    @Step("Entered Folder name: {0}, for the method: {method}...")
    public void enterFolderName(String folderName) {
        inputText(folderInputField, folderName);
    }

    @Step("Clicking Cancel button on Create New Folder modal...")
    public void clickCancelOnCreateFolderModal() {
        click(cancelButtonOnModal);
    }

    @Step("Verifying folder name input field is empty...")
    public boolean isFolderInputFieldEmpty() {
        return getText(folderInputField).equals("");
    }

    @Step("Clicking on upload file button...")
    public void clickOnUploadFileButton() {
        click(uploadFileButton);
    }

    @Step("Clicking on Save files button...")
    public void clickOnSaveFileButton() {
        click(saveFilesButton);
    }

    @Step("Clicking on Cancel File Upload button...")
    public void cancelFileUpload() {
        click(cancelFileUploadButton);
    }

    @Step("Verifying folder is not present...")
    public int isFolderNotDisplayed(String folderName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathFolder.replace("{name}", folderName)), counter).size();
    }

    @Step("Clicking on collapse folder button...")
    public void hideFolderFiles(String folderName) {
        WebElement collapseFolderButton = findElementWithWait(By.xpath(xpathCollapseFolderButton.replace("{name}", folderName)));
        click(collapseFolderButton);
    }

    @Step("Getting name of file that is being uploaded...")
    public String getNameOfSelectedFileToUpload() {
        return getText(fileToUpload);
    }

    @Step("Selecting folder from dropdown...")
    public void selectFolderFromDropdown(String folderName) {
        WebElement selectedFolder = findElementWithWait(By.xpath(xpathFolderChoiceDropdown.replace("{name}", folderName)));
        click(selectedFolder);
    }

    @Step("Clicking on Save Folder button...")
    public void clickOnSaveFolderButton() {
        click(saveFolderButton);
    }

    @Step("Getting version number of file...")
    public String getFileVersion(String fileName) {
        WebElement version = findElementWithWait(By.xpath(xpathVersion.replace("{name}", fileName)));
        return getText(version);
    }

    @Step("Getting contents card text ..")
    public String getContentsCard() {
        return getText(cardText);
    }

    @Step("Clicking on OK button on modal...")
    public void clickOnOkButton() {
        click(okModalButton);
    }

    @Step("Enter file name in search field...")
    public void enterFileNameInSearchField(String searchText) {
        inputText(searchField, searchText);
    }

    @Step("Verify file path displayed as text...")
    public boolean isFilePathClickable(String filePath) {
        WebElement filePathDisplayedAsText = findElementWithWait(By.xpath(xpathNonFusaFile.replace("{text}", filePath)));
        return isElementClickable(filePathDisplayedAsText);
    }

    @Step("Get the number of files...")
    public int getNumberOfFiles() {
        DriverFactory.sleep(1000);
        return getNumberOfElements(files);
    }

    @Step("Get warning message...")
    public String getPiForceNotAvailableWarningMsg() {
        return getText(piForceNotAvailableMessage);
    }

}
