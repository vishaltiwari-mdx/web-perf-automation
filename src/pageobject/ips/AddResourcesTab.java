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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddResourcesTab extends BasePage {

    private final WebDriver driver;

    public AddResourcesTab(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[@data-testid='ui-btn-remove-resources']")
    private WebElement removeButton;

    @FindBy(xpath = "//div[@role='dialog']//button[.='Remove']")
    private WebElement removeButton_dialog;

    @FindBy(xpath = "//div[@role='dialog']//h5")
    private WebElement removeDialogHeader;

    @FindBy(xpath = "//div[@role='dialog']//div[contains(@class,'modal-body')]")
    private WebElement removeDialogHeaderBody;

    private final String getTableRow = "//div[@data-testid='resources-panel-grid']//div[@row-id='{fqn}']";
    private final String getNumberOfRow = getTableRow + "//div[@col-id='name']";
    private final String rows = "//div[@data-testid='resources-panel-grid']//div[@row-id]//div[@col-id='name']";
    private final String RowIpCheckbox = getTableRow + "//input[@type='checkbox']//..";
    //LineAction
    private final String rowIpValue = getTableRow + "//div[@col-id='{col-id}']";
    private final String rowIpLine = getTableRow + "//div[@col-id='selectLine']";
    private final String rowIpLineDropDown_icon = rowIpLine + "/div/i";
    private final String rowIpLineDropDown_searchBox = rowIpLine + "//input[@placeholder='Search IP Lines']";
    private final String RowIpLineDropDown_search_list_item = rowIpLine + "//ul[@class='dropdown-menu show']//li[contains(.,'{lineName}')]";
    private final String getResourceTableRow = "(//div[@data-testid='resources-panel-grid']//div[@row-id='{fqn}'])[1]";
    private final String xpathResourcesTab = "//div[@class='ag-center-cols-container']//div[@role='row']";
    private final String rowIpVersionBase = getTableRow + "//div[@col-id='versionCell']";
    private final String rowIpVersionDropDown_icon = rowIpVersionBase + "/div/i";
    private final String RowIpVersionDropDown_search_list_item = rowIpVersionBase + "//ul[@class='dropdown-menu show']//div[@class='versions-tab']//li[contains(.,'{versionName}')]";
    private final String rowIpLineDescriptionValue = getTableRow + "//div[@col-id='{col-id}']//div[@role='label-name']";
    private final String rowIpv = "//div[@data-testid='resources-panel-grid']//div[@title='{name}']";
    private final String rowGoToIP = "//div[@data-testid='resources-panel-grid']//div[@title='{name}']//i";

    @Step("Add IP as a resource...")
    public void addIpAsResource(String... fqn) {
        AddResourcesPage addResourcesPage = new AddResourcesPage(DriverFactory.getBrowserInstance());
        addResourcesPage.clickOnResourceTab();
        addResourcesPage.clickOnAddResourceButton();
        for (String ip : fqn) {
            AddResourcesTable addResourcesTable = new AddResourcesTable(DriverFactory.getBrowserInstance());
            String shortFqn = ip.substring(ip.lastIndexOf('.') + 1);
            addResourcesPage.enterIpNameToInputField(shortFqn);
            addResourcesTable.clickOnIpCheckBoxByFqn(ip);
        }
        addResourcesPage.clickOnNextButton();
        addResourcesPage.clickOnConfirmButton();
    }

    @Step("Delete a resource: {0}...")
    public void deleteResource(String... fqn) {
        logger.info("Delete resource with FQN: {}", fqn);
        for (String ip : fqn) {
            clickOnIpCheckBoxByFqn(ip);
        }
        clickOnRemoveButton();
        clickOnRemoveButton_dialog();
    }

    @Step("Click to IP Checkbox...")
    public void clickOnIpCheckBoxByFqn(String... fqns) {
        for (String fqn : fqns) {
            WebElement row = waitForElementToBePresent(By.xpath(getTableRow.replace("{fqn}", fqn)));
            boolean isSelected = row.getAttribute("class").contains("ag-row-selected");
            logger.info("Current checkbox state for FQN {}: {}", fqn, isSelected);
            if (!isSelected) {
                WebElement element = waitForElementToBePresent(By.xpath(RowIpCheckbox.replace("{fqn}", fqn)));
                waitTillClickableWithFluentWait(element);
                element.click();
                logger.info("Clicked on IP CheckBox with FQN: {} ", fqn);
            }
        }
    }

    @Step("Check if the Remove button is enabled")
    public boolean isRemoveButtonEnable() {
        return removeButton.isEnabled();
    }

    @Step("Click on the Remove button")
    public void clickOnRemoveButton() {
        waitForElementToBeClickable(removeButton);
        click(removeButton);
    }

    @Step("Click on the Remove button in the dialog")
    public void clickOnRemoveButton_dialog() {
        waitForElementToBeClickable(removeButton_dialog);
        click(removeButton_dialog);
        DriverFactory.sleep(1500);
    }

    @Step("Get the header text of the Remove dialog")
    public String getRemoveDialogHeader() {
        waitForElementToBeClickable(removeDialogHeader);
        return removeDialogHeader.getText();
    }

    @Step("Get the body text of the Remove dialog")
    public String getRemoveDialogHeaderBody() {
        waitForElementToBeClickable(removeDialogHeaderBody);
        return removeDialogHeaderBody.getText();
    }

    @Step("Check if the Remove dialog is visible")
    public boolean isRemoveDialogVisible() {
        return isElementVisible(removeDialogHeader);
    }

    @Step("Verify that IPs are not visible in the Resources tab table for FQNs: {fqns}")
    public boolean isIpNotVisibleOnResourcesTabTable(String... fqns) {
        for (String fqn : fqns) {
            if (isElementVisible(getTableRow.replace("{fqn}", fqn))) {
                logger.info(String.format("Info icon is visible for FQN: %s", fqn));
                return false;
            }
        }
        logger.info("IP is not visible on confirmation.");
        return true;
    }

    public boolean isResourcePresent(String fqn) {
        return isElementVisible(getResourceTableRow.replace("{fqn}", fqn));
    }

    @Step("Get cell value from the table for FQN: {fqn} and column ID: {col_id}")
    public String getTableCellValue(String fqn, String col_id) {
        logger.info("Entering getTableCellValue method with FQN: {} and Column ID: {}", fqn, col_id);
        WebElement element = findElementWithWait(By.xpath(rowIpValue.replace("{fqn}", fqn).replace("{col-id}", col_id)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        String text = element.getText().trim();
        logger.info("Retrieved cell value for FQN: {} - Column ID: {} - Value: {}", fqn, col_id, text);
        return text;
    }

    public int getNumberOfResources(String fqn, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(getNumberOfRow.replace("{fqn}", fqn)), counter).size();
    }

    public int getNumberOfResources(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(rows), counter).size();
    }

    @Step("Opening IP line dropdown for FQN: {fqn}")
    public void updateLineToIp(String fqn, String lineName) {
        openIpLineDropDown(fqn);
        enterTextLineDropDownSearchBox(fqn, lineName);
        DriverFactory.sleep(1000);
        selectIpLineFromLineDropDownToIp(fqn, lineName);
    }

    @Step("Opening IP line dropdown for FQN: {fqn}")
    public void openIpLineDropDown(String fqn) {
        WebElement element = waitForElementToBePresent(By.xpath(rowIpLineDropDown_icon.replace("{fqn}", fqn)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        doubleClickOnElement(element);
        logger.info("Opened IP line dropdown for FQN: " + fqn);
    }

    @Step("Opening IP version  dropdown for FQN: {fqn}")
    public void openIpVersionDropDown(String fqn) {
        WebElement element = waitForElementToBePresent(By.xpath(rowIpVersionDropDown_icon.replace("{fqn}", fqn)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        doubleClickOnElement(element);
        logger.info("Opened IP version dropdown for FQN: " + fqn);
    }

    @Step("Entering text slowly into IP line dropdown search box for FQN: {fqn}")
    public void enterTextLineDropDownSearchBox(String fqn, String lineName) {
        WebElement element = waitForElementToBePresent(By.xpath(rowIpLineDropDown_searchBox.replace("{fqn}", fqn)));
        enterTextSlowly(waitForElementToBeVisible(element), lineName);
        logger.info("Entered text slowly into IP line dropdown search box for FQN: " + fqn);
    }

    @Step("selecting line: {lineName} for opened IP line dropdown for FQN: {fqn} ")
    private void selectIpLineFromLineDropDownToIp(String fqn, String lineName) {
        logger.info("Before selecting line: {lineName} for opened IP line dropdown for FQN: {fqn}", lineName, fqn);
        WebElement element = waitForElementToBePresent(By.xpath(RowIpLineDropDown_search_list_item.replace("{fqn}", fqn).replace("{lineName}", lineName)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        click(element);
        logger.info("After selecting line: {lineName} for opened IP line dropdown for FQN: {fqn}", lineName, fqn);
    }

    @Step("selecting version: {version} for opened IP line dropdown for FQN: {fqn} ")
    public void selectIpVersionDropDownToIp(String fqn, String version) {
        openIpVersionDropDown(fqn);
        WebElement element = waitForElementToBePresent(By.xpath(RowIpVersionDropDown_search_list_item.replace("{fqn}", fqn).replace("{versionName}", version)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        click(element);
        logger.info("After selecting line: {lineName} for opened IP line dropdown for FQN: {fqn}", version, fqn);
    }


    @Step("Get number of IPs on Resources modal...")
    public int getNumberOfIpsInResource(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathResourcesTab), counter).size();
    }

    @Step("Get cell value from the table for FQN: {fqn} and column ID: {col_id}")
    public String getTableLineDescriptionValue(String fqn, String col_id) {
        logger.info("Entering getTableCellValue method with FQN: {} and Column ID: {}", fqn, col_id);
        WebElement element = findElementWithWait(By.xpath(rowIpLineDescriptionValue.replace("{fqn}", fqn).replace("{col-id}", col_id)));
        scrollToElement(element);
        waitForElementToBeVisible(element);
        String text = element.getText().trim();
        logger.info("Retrieved cell value for FQN: {} - Column ID: {} - Value: {}", fqn, col_id, text);
        return text;
    }

    @Step("click IPV on Resource tab..")
    public void clickOnIpvHyperLinkResourceTab(String ipName) {
        hoverOverElement(findElementWithFluentWait(By.xpath(rowIpv.replace("{name}", ipName))));
        WebElement goToIpButton = findElementWithFluentWait(By.xpath(rowGoToIP.replace("{name}", ipName)));
        click(goToIpButton);
    }
}