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
package com.methodics.phi.pageobject.widgets;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.File;
import java.util.List;

public class WidgetEditorPage extends BasePage {
    private WebDriver driver;

    public WidgetEditorPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "button[data-testid='ui-btn-ip-details-dashboard-add-widget']")
    private WebElement dashboardScreenAddWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-dashboard-add-widget-button']:not(disabled)")
    private WebElement mainAddWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-dashboard-add-widget-button'][disabled]")
    private WebElement disabledMainAddWidgetButton;

    @FindBy(css = "button[data-testId='ui-btn-dashboard-lock-button'][disabled]")
    private WebElement disabledLockDashboardButton;

    @FindBy(css = "h5.tab-dashboard__main__title")
    private WebElement addYourFirstWidgetMessage;

    @FindBy(css = "button[data-testId='ui-btn-dashboard-lock-button']")
    private WebElement unlockedDashboardButton;

    @FindBy(css = "button[data-testId='ui-btn-dashboard-unlock-button']")
    private WebElement lockedDashboardButton;

    @FindBy(css = "i[class*='fa-lock']")
    private WebElement tabLockedDashboardButton;

    @FindBy(css = "button[data-testid='ui-btn-dashboard-add-widget-button']:not(disabled)")
    private WebElement tabMainAddWidgetButton;

    @FindBy(css = "a[data-testid='code-editor-tab']")
    private WebElement codeEditorTab;

    @FindBy(css = ".ProseMirror > p")
    private WebElement visualEditorInput;

    @FindBy(css = ".ProseMirror-focused")
    private WebElement visualEditorInputFocused;

    @FindBy(css = "textarea[data-testid='textarea']")
    private WebElement codeEditorInput;

    @FindBy(css = "input[data-testid='ui-in-widget-header-attribute-name-input']")
    private WebElement attributeNameInput;

    @FindBy(css = "input[data-testid='ui-in-widget-header-widget-name-input']")
    private WebElement widgetHeaderInput;

    @FindBy(css = "button[data-testid='ui-btn-widget-editor-cancel-button']")
    private WebElement cancelButton;

    @FindBy(css = "button[data-testid='ui-btn-widget-editor-save-button']")
    private WebElement saveButton;

    @FindBy(css = "button[data-testid='ui-btn-widget-editor-save-button'][disabled]")
    private WebElement disabledCheckAndSaveButton;

    @FindBy(css = "button[data-testid='ui-btn-dashboard-add-widget-button'][disabled]")
    private WebElement tabDisabledAddWidgetButton;

    @FindBy(css = "div[class*='invalid-feedback_error']")
    private WebElement errorValidationMessage;

    @FindBy(css = "button[data-testid='ui-btn-widget-richtext-table-button']>i")
    private WebElement tableButton;

    @FindBy(css = "button[data-testid='ui-btn-widget-richtext-iframe-button']>i")
    private WebElement iframeButton;

    @FindBy(css = "button[data-testid='ui-btn-widget-richtext-add-button']")
    private WebElement buttonWidget;

    @FindBy(css = "button[data-testid='ui-btn-table-start-from-scratch-button']")
    private WebElement createFromScratchButton;

    @FindBy(css = "button[data-testid='ui-btn-table-upload-csv-button']")
    private WebElement uploadCsvButton;

    @FindBy(css = "h5[class*='app-modal-header__title']")
    private WebElement createTableHeader;

    @FindBy(css = ".init-screen__content > p")
    private WebElement createTableMessage;

    @FindBy(css = ".init-screen__content > h4")
    private WebElement createTableSubHeader;

    @FindBy(css = "input[data-testid='table-file-upload-csv-input']")
    private WebElement fileUpload;

    @FindBy(css = "input[data-testid='ui-in-iframe-url-input']")
    private WebElement iframeUrl;

    @FindBy(css = "input[data-testid='ui-in-insert-edit-iframe-modal-field-width']")
    private WebElement iframeWidth;

    @FindBy(css = "input[data-testid='ui-in-insert-edit-iframe-modal-field-height']")
    private WebElement iframeHeight;

    @FindBy(css = "input[data-testid='ui-in-create-edit-button-modal-add-button-label']")
    private WebElement buttonLabel;

    @FindBy(css = "input[data-testid='ui-in-create-edit-button-modal-button-width-label']")
    private WebElement buttonWidth;

    @FindBy(css = "input[data-testid='ui-in-create-edit-button-modal-button-url-label']")
    private WebElement buttonUrl;

    @FindBy(css = "button[data-testid='ui-btn-create-edit-button-modal-submit']:not([disabled])")
    private WebElement activeCreateButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-create-tab-create'][disabled]")
    private WebElement disabledCreateButton;

    @FindBy(css = "button[data-testid='ui-btn-create-edit-button-modal-submit'][disabled]")
    private WebElement disabledCreateButtonButton;

    @FindBy(css = "h5[class*='app-modal-header__title']")
    private WebElement modalTitle;

    @FindBy(css = "input[data-testid='ui-in-modal-create-tab-name']")
    private WebElement tabNameInput;

    @FindBy(css = "input[data-testid='ui-in-modal-create-tab-rank']")
    private WebElement tabRankInput;

    @FindBy(css = "button[data-testid='ui-btn-widgets-editor-create-new-tab']")
    private WebElement createNewTabButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-create-tab-cancel']")
    private WebElement createNewTabCancelButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-create-tab-create']:not(disabled)")
    private WebElement createNewTabInModalButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-create-tab-create'][disabled]")
    private WebElement createNewTabInModalButtonDisabled;

    @FindBy(xpath = "//div[contains(@class, 'widget-tab-selector')]//span[@class='multiselect__single']")
    private WebElement selectedTab;

    @FindBy(xpath = "//div[contains(@class,'tabbed-card__header-tabs')]//a[not(contains(@data-testid,'tabbed-card-header-tabs-off-screen'))]")
    private List<WebElement> tabs;

    @FindBy(xpath = "//div[contains(@class,'widget-tab-selector')]//div[contains(@class,'caret-icon__wrapper')]")
    private WebElement widgetTabSelector;

    @FindBy(xpath = "//input[@class='table-header-cell__input']")
    private WebElement tableHeaterCellInput;

    @FindBy(xpath = "//div[contains(@class,'add-col_last')]//button[@class='add-col__button']")
    private WebElement addColumnButton;

    @FindBy(xpath = "//div[contains(@class,'add-row__last')]//button[@class='add-row__button']")
    private WebElement addLastRowButton;

    @FindBy(xpath = "//textarea[@class='table-cell__input']")
    private WebElement tableCellInput;

    @FindBy(xpath = "//button[normalize-space()= 'Create']")
    private WebElement createTableInModal;

    @FindBy(xpath = "//div[@class='tbody__content']//div[@class='tr']")
    private List<WebElement> numberOfRows;

    @FindBy(xpath = "//div[@class='thead']//div[@class='th']")
    private List<WebElement> numberOfColumns;

    @FindBy(xpath = "//div[@class='area-menu']")
    private WebElement areaMenu;

    @FindBy(xpath = "//i[contains(@class,'fa-trash-xmark')]")
    private WebElement deleteIcon;

    @FindBy(xpath = "//div[contains(@class,'card widget-editor')]//div//i[contains(@class,'fa-pencil')]")
    private WebElement editButton;

    @FindBy(css = "[data-testid='ui-btn-widget-button-text']")
    private WebElement widgetEditorWidget;

    @FindBy(css = "button[data-testid='ui-btn-create-edit-button-modal-submit']")
    private WebElement saveButtonInModal;

    @FindBy(xpath = "//div[@class='position-relative']//iframe[@src]")
    private WebElement iframeUrlText;

    @FindBy(xpath = "//div[contains(@class,'position-relative')]//img[@src]")
    private WebElement imageUrlText;

    @FindBy(css = "button[data-testid='ui-btn-widget-richtext-image-button']")
    private WebElement imageButton;

    @FindBy(css = "input[data-testid='ui-in-image-url-input']")
    private WebElement imageUrl;

    @FindBy(css = "button[data-testid='ui-btn-image-save-button']")
    private WebElement insertButton;

    @FindBy(xpath = "//button[normalize-space()='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//div[@class='tab-dashboard__main__body'][contains(text(), 'Use widgets to summarize your data in graphs, tables, and images.')]")
    private WebElement useWidgetMessage;

    @FindBy(xpath = "//div[contains(@class, 'tab-dashboard__main__body')][contains(text(), 'The dashboard can show all your widgets in one place to visualize your data.')]")
    private WebElement theDashboardCanShowMessage;

    @FindBy(css = "button[data-testid='ui-btn-create-chart-modal-submit']:not(disabled)")
    private WebElement submitChartWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-table-submit-button']:not(disabled)")
    private WebElement submitTableWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-create-edit-button-modal-submit']:not(disabled)")
    private WebElement submitButtonWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-iframe-insert-button']:not(disabled)")
    private WebElement submitIframeWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-image-save-button']:not(disabled)")
    private WebElement submitImageWidgetButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-insert-link-create']:not(disabled)")
    private WebElement submitLinkWidgetButton;

    @FindBy(xpath = "//a[@title='Other' and contains(@class,'disabled')]")
    private WebElement disabledOtherTab;

    @FindBy(css = "button[data-testid='ui-btn-iframe-insert-button'][disabled]")
    private WebElement disabledCreateIframeButton;

    private String widgetEditButton = "//div[contains(@class,'{name}')]/parent::div//i[contains(@class,'fa-pencil')]";
    private String widgetDeleteButton = "//div[contains(@class,'{name}')]/parent::div//i[contains(@class,'fa-trash-xmark')]";
    private String widgetsTab = "//a[@title='{name}']";
    private String selectiveTabs = "//li[@class='multiselect__element']/span[normalize-space()='{name}']";
    private String columnHeader = "//div[@data-col-index='{position}']//p[@class='table-header-cell__label']//span | //div[@data-col-index='{position}']//p[@class='table-header-cell__label']";
    private String widgetColumnHeader = "//div[contains(@class,'ag-theme-mdx')][not(@toolpanelsuppresssidebuttons)]//span[@title='{name}']";
    private String columnHeaderMenu = "//div[@data-col-index='{position}']//div[@class='table-header-cell__menu']";
    private String tableCell = "//div[@class='tr' and @data-row-index='{rowPosition}']//div[@data-col-index='{columnPosition}']";
    private String cellMenu = "//div[@class='tr' and @data-row-index='{rowPosition}']//div[@data-col-index='{columnPosition}']//div[@class='table-cell__menu']";
    private String menuItem = "//button[@class='menu__item' and normalize-space()='{menuItem}']";
    private String widgetHint = "//span[normalize-space()='{fieldName}']/i";
    private String xpathTooltip = "//div[contains(@style,'visibility: visible')]//*[@class='tooltip-inner']";
    private String expandAddButton = "button[data-testid='ui-dropdown-btn-toggle'] > i[class*='fa-plus']";
    private String widgetButtonFromAddDropDown = "//span[normalize-space()='{button}']";
    private String widgetButton = "button[data-testid='ui-btn-widget-richtext-{button}-button']";
    private String widgetTab = "//a[@title='{tabName}']";

    @Step("Check to see if dashboard page contains add you first widget message...")
    public String addYourFirstWidgetMessage() {
        return addYourFirstWidgetMessage.getText();
    }

    @Step("Check to see if the dashboard page contains messages on different lines")
    public boolean isDashboardPageMessagesOnDifferentLines() {
        Point firstPosition = useWidgetMessage.getLocation();
        Point secondPosition = theDashboardCanShowMessage.getLocation();
        return firstPosition.getY() != secondPosition.getY();
    }

    @Step("Check to see if dashboard page contains dashboard unlock button...")
    public boolean isUnlockDashboardButtonPresent() {
        return isElementVisible(lockedDashboardButton);
    }

    @Step("Check to see if tabbed dashboard page contains dashboard unlock button...")
    public boolean isUnlockDashboardButtonPresentOnTab() {
        return isElementVisible(tabLockedDashboardButton);
    }

    @Step("Check to see if dashboard empty page contains add widget button...")
    public boolean isAddWidgetButtonPresent() {
        return isElementVisible(dashboardScreenAddWidgetButton);
    }

    @Step("Check to see if dashboard page contains add widget button...")
    public boolean isMainAddWidgetButtonPresent() {
        return isElementVisible(mainAddWidgetButton);
    }

    @Step("Enter Attribute name name: {0} ...")
    public void enterAttributeName(String attributeName) {
        clickOnAttributeInput();
        inputText(attributeNameInput, attributeName);
    }

    @Step("Enter Attribute name name: {0} ...")
    public void clickOnAttributeInput() {
        click(attributeNameInput);
    }

    @Step("Enter Widget Header: {0} ...")
    public void enterWidgetHeader(String header) {
        clickOnWidgetHeaderInput();
        inputText(widgetHeaderInput, header);
    }

    @Step("Enter Attribute name name: {0} ...")
    public void clickOnWidgetHeaderInput() {
        click(widgetHeaderInput);
    }

    @Step("Enter Text in visual editor: {0} ...")
    public void enterTextInVisualEditor(String text) {
        click(visualEditorInput);
        enterTextSlowly(visualEditorInputFocused, text);
    }

    @Step("Enter text in code editor: {0} ...")
    public void enterTextInCodeEditor(String text) {
        click(codeEditorInput);
        clearInputField(codeEditorInput);
        inputText(codeEditorInput, text);
    }

    @Step("Enter text in code editor: {0} ...")
    public void enterText(String text) {
        inputText(codeEditorInput, text);
    }

    @Step("Clicking on unlock dashboard button ...")
    public void clickOnUnlockButtonInTabs() {
        click(tabLockedDashboardButton);
    }

    @Step("Clicking on unlock dashboard button ...")
    public void clickOnUnlockButton() {
        waitForElementToBeVisible(lockedDashboardButton);
        click(lockedDashboardButton);
    }

    @Step("Clicking on lock dashboard button ...")
    public void clickOnLockButton() {
        Assert.assertTrue(isElementVisible(unlockedDashboardButton));
        click(unlockedDashboardButton);
    }

    @Step("Clicking on add widget button in tab ...")
    public void clickOnMainAddWidgetButtonInTab() {
        click(tabMainAddWidgetButton);
    }

    @Step("Check to see if add widget button is enabled...")
    public boolean isSelectedTabAddWidgetButtonPresent() {
        return isElementVisible(tabMainAddWidgetButton);
    }

    @Step("Clicking on Tab Title button ...")
    public void clickOnTabTitle(String tabName) {
        WebElement tabTitle = findElementWithWait(By.xpath(widgetTab.replace("{tabName}", tabName)));
        click(tabTitle);
    }

    @Step("Clicking on edit widget button ...")
    public void clickOnWidgetEditButton(String widgetName) {
        WebElement editButton = findElementWithWait(By.xpath(widgetEditButton.replace("{name}", widgetName)));
        click(editButton);
    }

    @Step("Clicking on delete widget button ...")
    public void clickOnWidgetDeleteButton(String widgetName) {
        WebElement deleteButton = findElementWithWait(By.xpath(widgetDeleteButton.replace("{name}", widgetName)));
        click(deleteButton);
    }

    @Step("Check to see if add widget button is disabled...")
    public boolean isTabAddWidgetButtonDisabled() {
        return isElementVisible(tabDisabledAddWidgetButton);
    }

    @Step("Check to see if Lock Dashboard button is disabled...")
    public boolean isLockDashboardButtonDisabled() {
        return isElementVisible(disabledLockDashboardButton);
    }

    @Step("Check to see if add widget button is disabled...")
    public boolean isAddWidgetButtonDisabled() {
        return isElementVisible(disabledMainAddWidgetButton);
    }

    @Step("Clicking on widgets tab")
    public void clickOnWidgetTab(String tabName) {
        WebElement tab = findElementWithWait(By.xpath(widgetsTab.replace("{name}", tabName)));
        click(tab);
    }

    @Step("Clicking on Add widget button on dashboard...")
    public void clickOnDashboardAddWidgetButton() {
        click(dashboardScreenAddWidgetButton);
        DriverFactory.sleep(1000);
    }

    @Step("Getting validation message text...")
    public String getAttributeFieldValidationMessage() {
        return getText(errorValidationMessage);
    }

    @Step("Verifying that check and save button is disabled...")
    public boolean isCheckAndSaveDisabled() {
        return isElementVisible(disabledCheckAndSaveButton);
    }

    @Step("Clicking on cancel button...")
    public void clickOnCancelButton() {
        click(cancelButton);
    }

    @Step("Clicking on Check and save button...")
    public void clickOnSaveButton() {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getBrowserInstance();
        js.executeScript("arguments[0].click();", waitForElementToBeVisible(saveButton));
    }

    @Step("Selecting code editor tab...")
    public void selectCodeEditorTab() {
        click(codeEditorTab);
    }

    @Step("Clicking on create table button...")
    public void clickOnCreateTableButton() {
        click(tableButton);
    }

    @Step("Clicking on Create from scratch button...")
    public void clickOnCreateFromScratchButton() {
        click(createFromScratchButton);
    }

    @Step("Clicking on Create from scratch button...")
    public void clickOnUploadCsvButton() {
        click(uploadCsvButton);
    }

    @Step("Check to see if dashboard empty page contains add widget button...")
    public boolean isCreateFromScratchPresent() {
        return isElementVisible(createFromScratchButton);
    }

    @Step("Check to see if dashboard empty page contains add widget button...")
    public boolean isUploadCsvButtonPresent() {
        return isElementVisible(uploadCsvButton);
    }

    @Step("Getting create table modal header text...")
    public String getCreateTableModalHeader() {
        return createTableHeader.getText();
    }

    @Step("Getting create table modal sub header text...")
    public String getCreateTableModalSubHeader() {
        return createTableSubHeader.getText();
    }

    @Step("Getting create table modal message text...")
    public String getCreateTableModalMessage() {
        return createTableMessage.getText();
    }

    @Step("Uploading csv file...")
    public void uploadCsvFile(String path) {
        File file = new File(path);
        fileUpload.sendKeys(file.getAbsolutePath());
    }

    @Step("Clicking on Iframe button...")
    public void clickOnIframeButton() {
        click(iframeButton);
    }

    @Step("Clicking on Iframe Url input")
    public void clickOnIframeUrlInput() {
        click(iframeUrl);
    }

    @Step("Entering iframe url...")
    public void enterIframeUrl(String url) {
        clickOnIframeUrlInput();
        inputText(iframeUrl, url);
    }

    @Step("Clearing iframe url input field ")
    public void clearIframeUrlInputField() {
        clearInputField(iframeUrl);
    }

    @Step("Entering iframe height...")
    public void enterIframeHeight(String height) {
        click(iframeHeight);
        inputText(iframeHeight, height);
    }

    @Step("Entering iframe width...")
    public void enterIframeWidth(String width) {
        clickOnIframeWidth();
        inputText(iframeWidth, width);
    }

    @Step("Clicking on iframe width...")
    public void clickOnIframeWidth() {
        click(iframeWidth);
    }

    @Step("Clicking on button label input field...")
    public void clickOnButtonLabelInput() {
        click(buttonLabel);
    }

    @Step("Clicking on button url input field...")
    public void clickOnButtonUrlInput() {
        click(buttonUrl);
    }

    @Step("Clicking on create button widget...")
    public void clickOnCreateButtonWidget() {
        waitForElementToBeVisible(buttonWidget).click();
    }

    @Step("Clicking on specified editor panel button...")
    public void clickOnEditorPanelButton(String buttonName) {
        List<WebElement> size = driver.findElements(By.cssSelector(expandAddButton));
        if (size.size() != 0) {
            WebElement item = findElementWithWait(By.cssSelector(expandAddButton));
            click(item);
            WebElement listElementXpath = findElementWithWait(By.xpath(widgetButtonFromAddDropDown.replace("{button}", buttonName)));
            click(listElementXpath);
        } else {
            WebElement buttonXpath = findElementWithWait(By.cssSelector(widgetButton.replace("{button}", buttonName)));
            click(buttonXpath);
        }
    }

    @Step("Clicking on create Chart button widget...")
    public void clickOnCreateChartWidget() {
        clickOnEditorPanelButton("chart");
    }

    @Step("Check if create button is disabled...")
    public boolean isCreateButtonDisabled() {
        return isElementVisible(disabledCreateButton);
    }

    @Step("Check if create iframe button is disabled...")
    public boolean isCreateIframeButtonDisabled() {
        return isElementVisible(disabledCreateIframeButton);
    }

    @Step("Check if create button is disabled...")
    public boolean isCreateButtonEnabled() {
        return isElementVisible(activeCreateButton);
    }

    @Step("Get modal title..")
    public String getModalTitle() {
        return getText(modalTitle);
    }

    @Step("Entering label...")
    public void enterLabel(String label) {
        clickOnButtonLabelInput();
        inputText(buttonLabel, label);
    }

    @Step("Entering url...")
    public void enterUrl(String url) {
        clickOnButtonUrlInput();
        inputText(buttonUrl, url);
    }

    @Step("Clicking on active create button for {0} widget")
    public void clickOnActiveCreateButton(String widgetType) {
        WebElement buttonToClick;
        switch (widgetType.toLowerCase()) {
            case "chart":
                buttonToClick = submitChartWidgetButton;
                break;
            case "table":
                buttonToClick = submitTableWidgetButton;
                break;
            case "button":
                buttonToClick = submitButtonWidgetButton;
                break;
            case "iframe":
                buttonToClick = submitIframeWidgetButton;
                break;
            case "image":
                buttonToClick = submitImageWidgetButton;
                break;
            case "link":
                buttonToClick = submitLinkWidgetButton;
                break;
            default:
                buttonToClick = activeCreateButton;
                break;
        }
        waitForElementToBeClickable(buttonToClick);
        click(buttonToClick);
    }

    @Step("Clicking on create new tab button...")
    public void clickOnCreateNewTabButton() {
        click(createNewTabButton);
    }

    @Step("Clicking on create new tab cancel button...")
    public void clickOnCreateNewTabCancelButton() {
        click(createNewTabCancelButton);
    }

    @Step("Clicking on tab name input...")
    public void clickOnTabNameInput() {
        click(tabNameInput);
    }

    @Step("Clicking on tab rank input...")
    public void clickOnTabRankInput() {
        click(tabRankInput);
    }

    @Step("Entering tab name...")
    public void enterTabName(String name) {
        clickOnTabNameInput();
        inputText(tabNameInput, name);
    }

    @Step("Entering tab rank...")
    public void enterTabRank(String rank) {
        clickOnTabRankInput();
        inputText(tabRankInput, rank);
    }

    @Step("Getting selected tab text...")
    public String getSelectedTabText() {
        return getText(selectedTab);
    }

    @Step("Getting number of tabs on a widget")
    public int getNumberOfTabs() {
        DriverFactory.sleep(500);
        DriverFactory.sleep(1000);
        return getNumberOfVisibleElements(tabs);
    }

    @Step("Selecting tab from dropdown in widget editor...")
    public void selectWidgetTabFromDropDown(String tabName) {
        click(widgetTabSelector);
        WebElement tab = findElementWithWait(By.xpath(selectiveTabs.replace("{name}", tabName)));
        click(tab);
    }

    @Step("Double click on table header...")
    public void doubleClickOnTableHeader(String position) {
        WebElement header = findElementWithWait(By.xpath(columnHeader.replace("{position}", position)));
        doubleClickOnElement(header);
    }

    @Step("Entering text in table header..")
    public void enterTextIntoColumnHeader(String position, String text) {
        doubleClickOnTableHeader(position);
        inputText(tableHeaterCellInput, text);
    }

    @Step("Double click on table cell...")
    public void doubleClickOnTableCell(String columnPosition, String rowPosition) {
        WebElement cell = findElementWithWait(By.xpath(tableCell
                .replace("{columnPosition}", columnPosition)
                .replace("{rowPosition}", rowPosition)));
        doubleClickOnElement(cell);
    }

    @Step("Entering text in table cell..")
    public void enterTextIntoTableCell(String columnPosition, String rowPosition, String text) {
        doubleClickOnTableCell(columnPosition, rowPosition);
        inputText(tableCellInput, text);
    }

    @Step("Clicking on add column button...")
    public void clickOnAddColumnButton() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", addColumnButton);
    }

    @Step("Insert column names...")
    public void insertMultipleColumnNames(String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            enterTextIntoColumnHeader(String.valueOf(i), columnNames[i]);
        }
    }

    @Step("Filling the row...")
    public void insertRow(String rowNum, String[] cell) {
        for (int i = 0; i < cell.length; i++) {
            enterTextIntoTableCell(String.valueOf(i), rowNum, cell[i]);
        }
    }

    @Step("Click on on create table in widget editor modal...")
    public void clickOnCreateTableButtonInModal() {
        click(createTableInModal);
    }

    @Step("Hovering over table cell...")
    public void hoverOverTableCell(String rowPosition, String columnPosition) {
        WebElement cell = findElementWithWait(By.xpath(tableCell
                .replace("{columnPosition}", columnPosition)
                .replace("{rowPosition}", rowPosition)));
        hoverOverElement(cell);
    }

    @Step("Clicking on table cell menu...")
    public void clickOnTableCellMenu(String row, String column) {
        hoverOverTableCell(row, column);
        WebElement cellContext = findElementWithWait(By.xpath(cellMenu
                .replace("{rowPosition}", row)
                .replace("{columnPosition}", column)));
        click(cellContext);
    }

    @Step("Clicking on context menu item...")
    public void clickOnMenuItem(String selection) {
        WebElement item = findElementWithWait(By.xpath(menuItem.replace("{menuItem}", selection)));
        click(item);
    }

    @Step("Getting number of columns...")
    public int getNumberOfColumns() {
        return getNumberOfElements(numberOfColumns);
    }

    @Step("Getting number of rows...")
    public int getNumberOfRows() {
        return getNumberOfElements(numberOfRows);
    }

    @Step("Selecting table cell...")
    public void clickOnTableCell(String columnPosition, String rowPosition) {
        WebElement cell = findElementWithWait(By.xpath(tableCell
                .replace("{columnPosition}", columnPosition)
                .replace("{rowPosition}", rowPosition)));
        click(cell);
    }

    @Step("Selecting multiple cells...")
    public void selectMultipleTableCells(String initialColumn, String initialRow, String targetColumn, String targetRow) {
        Actions act = new Actions(driver);
        clickOnTableCell(initialColumn, initialRow);
        act.keyDown(Keys.LEFT_SHIFT).build().perform();
        clickOnTableCell(targetColumn, targetRow);
    }

    @Step("Clicking on selected area menu")
    public void clickOnAreaMenu() {
        click(areaMenu);
    }

    @Step("Clicking on edit button...")
    public void clickOnEditIcon() {
        DriverFactory.sleep(2000);
        click(editButton);
    }

    @Step("Clicking on delete icon...")
    public void clickOnDeleteIcon() {
        click(deleteIcon);
    }

    @Step("Clicking on delete button...")
    public void clickOnDeleteButton() {
        click(deleteButton);
    }

    @Step("Getting widget name from widget editor interface...")
    public String getWidgetEditorWidgetName() {
        return widgetEditorWidget.getText();
    }

    @Step("Clicking on save button in modal...")
    public void clickOnSaveButtonInModal() {
        click(saveButtonInModal);
    }

    @Step("Getting iframe url...")
    public String getIframeUrl() {
        return iframeUrlText.getAttribute("src");
    }

    @Step("Getting iframe url...")
    public String getImageUrl() {
        return imageUrlText.getAttribute("src");
    }

    @Step("Hovering over table header...")
    public void hoverOverTableHeader(String columnPosition) {
        WebElement cell = findElementWithWait(By.xpath(columnHeader
                .replace("{position}", columnPosition)));
        hoverOverElement(cell);
    }

    @Step("Clicking on table header menu...")
    public void clickOnTableHeaderMenu(String column) {
        hoverOverTableHeader(column);
        WebElement cellContext = findElementWithWait(By.xpath(columnHeaderMenu.replace("{position}", column)));
        click(cellContext);
    }

    @Step("Clicking on add image button...")
    public void clickOnImageButton() {
        click(imageButton);
    }

    @Step("Entering image url...")
    public void enterImageUrl(String url) {
        inputText(imageUrl, url);
    }

    @Step("Clicking on insert button...")
    public void clickOnInsertButton() {
        click(insertButton);
    }

    @Step("Clicking on add last row button")
    public void addLastRowButton() {
        hoverOverElement(addLastRowButton);
        click(addLastRowButton);
    }

    @Step("Verify column is displayed...")
    public boolean isColumnDisplayed(String name) {
        WebElement column = waitForElementToBePresent(By.xpath(widgetColumnHeader.replace("{name}", name)));
        scrollToElement(column);
        return isElementVisible(column);
    }

    @Step("Verify all columns are displayed...")
    public void areColumnsPresent(String[] names) {
        for (int i = 0; i < names.length; i++) {
            Assert.assertTrue(isColumnDisplayed(names[i]), "Column " + names[i] + " is not displayed");
        }
    }

    @Step("Hover over Widget page hint...")
    public void hoverOverWidgetPageHint(String field) {
        WebElement fieldHint = findElementWithWait(By.xpath(widgetHint.replace("{fieldName}", field)));
        hoverOverElement(fieldHint);
    }

    @Step("Verify tooltip is present...")
    public String getTooltipText() {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip));
        return tooltip.getText();
    }

    @Step("Verify that Other tab is disabled...")
    public boolean isOtherTabDisabled() {
        return isElementVisible(disabledOtherTab);
        }

    @Step("Check if create button is disabled...")
    public boolean isCreateButtonButtonDisabled() {
        return isElementVisible(disabledCreateButtonButton);
    }

    @Step("Check if create button is disabled...")
    public void clickOnCreateTabButtonInModal() {
        createNewTabInModalButton.click();
    }

    @Step("Check if create tab buttonis enabled...")
    public boolean isCreateTabButtonEnabled() {
        return isElementVisible(createNewTabInModalButton);
    }

    @Step("Check if create tab button is disabled...")
    public boolean isCreateTabButtonDisabled() {
        return isElementVisible(createNewTabInModalButton);
    }
}
