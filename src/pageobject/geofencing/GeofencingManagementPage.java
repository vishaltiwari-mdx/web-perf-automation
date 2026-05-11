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
package com.methodics.phi.pageobject.geofencing;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GeofencingManagementPage extends BasePage {
    private WebDriver driver;

    public GeofencingManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[@data-testid='ui-btn-geo-fencing-manage-create-geo']")
    private WebElement createGeoButton;

    //save button is different test dataid at the time of creation so use UI test
    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//button[contains(@class,'delete-button')]")
    private WebElement deleteButton;

    @FindBy(xpath = "//button[@data-testid='ui-btn-geo-fencing-create' and @disabled]")
    private WebElement saveButtonDisabled;

    @FindBy(xpath = "//button[@data-testid='ui-btn-geo-fencing-cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//span[contains(@class, 'geofencing-edit-panel__add-subnet-button')]")
    private WebElement addSubnetButton;

    @FindBy(xpath = "//span[contains(@class, 'show-more-button')]")
    private WebElement showMoreButton;

    @FindBy(xpath = "//span[contains(@class, 'show-more-button_expanded')]")
    private WebElement showLessButton;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='subnets']")
    private WebElement firstRowSubnetsCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='creation_timestamp']/div[contains(@class, 'ag-cell-value')][@title]")
    private WebElement firstRowCreatedOnCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='creator.name'][contains(@class, 'ag-cell-value')]/span[@title]")
    private WebElement firstRowCreatedByCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='last_update_timestamp']/div[contains(@class, 'ag-cell-value')][@title]")
    private WebElement firstRowEditedOnCell;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='last_updater.name'][contains(@class, 'ag-cell-value')]/span[@title]")
    private WebElement firstRowEditedByCell;

    @FindBy(xpath = "//input[@data-testid='ui-in-geo-fencing-edit-geo-name']")
    private WebElement createGeoNameInputField;

    @FindBy(xpath = "//input[@data-testid='ui-in-geo-fencing-edit-subnet']")
    private WebElement subnetInputField;

    @FindBy(xpath = "//input[@data-testid='ui-in-geo-fencing-edit-short-name']")
    private WebElement shortNameInputField;

    @FindBy(xpath = "//input[@data-testid='ui-in-geo-fencing-edit-geo-color-text']")
    private WebElement colorInputField;

    @FindBy(xpath = "//textarea[@data-testid='geo-fencing-edit-description']")
    private WebElement descriptionInputField;

    @FindBy(xpath = "//div[contains(@class, 'app-subheader')]//span[normalize-space()='Geofencing']")
    private WebElement geofencingManagementPageHeader;

    @FindBy(xpath = "//i[@class='fa-solid fa-location-dot fa-fw']")
    private WebElement gpsColorIcon;

    @FindBy(xpath = "//input[contains(@class,'geofencing-edit-panel__short_name-input')]/ancestor::div[contains(@class,'field-validator')]/following-sibling::div[contains(@class,'form-text')]")
    private WebElement shortNameCharacterLimitNotice;

    @FindBy(xpath = "//div[@col-id='subnets']//div[@role='button']")
    private WebElement subnetExpansionButton;

    private String firstRowNameCell = "//div[@row-index='0']/div[@col-id='name']/div[contains(@class, 'ag-cell-value')]/span";
    private String firstRowShortNameCell = "//div[@row-index='0']/div[@col-id='short_name'][contains(@class, 'ag-cell-value')]";
    private String firstRowDescriptionCell = "//div[@row-index='0']/div[@col-id='description'][contains(@class, 'ag-cell-value')]/span";
    private String ipsForSubnets = "//div[@class='geofencing-subnet-cell__subnet']";
    private String xpathNoGeoFound = "//div[@class='ag-overlay-wrapper ag-layout-normal ag-overlay-no-rows-wrapper' and normalize-space()='No geos found.']";
    private String selectGeo = "//div[@col-id='name' and normalize-space()='{name}']";
    private String numberOfVisibleSubnets = "//div[@col-id='subnets']//div[@class='geofencing-subnet-cell__subnet-wrap']";
    private String subnetExpandButton = "//div[@col-id='subnets']//div[@role='button']/span[normalize-space()='+{numberOfHiddenGeos}']";
    private String subnetCollapseButton = "//div[@col-id='subnets']//div[@data-expanded-btn='true']/i";


    @Step("Verify Create button is disabled...")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(saveButtonDisabled);
    }

    @Step("Verify 'Created on' from the first row is visible...")
    public String isCreatedOnFromFirstTableRowVisible() {
        return getText(firstRowCreatedOnCell);
    }

    @Step("Get created by from the first row...")
    public String getCreatedByFromFirstTableRow() {
        return getText(firstRowCreatedByCell);
    }

    @Step("Verify 'Edited on' from the first row is visible...")
    public String isEditedOnFromFirstTableRowVisible() {
        return getText(firstRowEditedOnCell);
    }

    @Step("Get short name character limit notice...")
    public String getShortNameCharacterLimitNotice() {
        return getText(shortNameCharacterLimitNotice);
    }

    @Step("Verify Geofencing Management page header is visible...")
    public boolean isGeofencingManagementPageHeaderVisible() {
        return isElementVisible(geofencingManagementPageHeader);
    }

    @Step("Verify subnet expansion button is visible...")
    public boolean isSubnetExpansionButtonVisible() {
        DriverFactory.sleep(1000);
        return isElementVisible(subnetExpansionButton);
    }

    @Step("Verify No geo found message visible...")
    public boolean isNoGeoFoundMessageVisible() {
        WebElement noGeoFoundMessage = driver.findElement(By.xpath(xpathNoGeoFound));
        return isElementVisible(noGeoFoundMessage);
    }

    @Step("Click on Edit geos...")
    public void clickOnEdit(String geoName) {
        WebElement selectGeos = driver.findElement(By.xpath(selectGeo.replace("{name}", geoName)));
        click(selectGeos);
    }

    @Step("Click on Save...")
    public void clickOnSave() {
        click(saveButton);
    }

    @Step("Click on Delete geos...")
    public void clickOnDelete() {
        waitForPageLoaded();
        DriverFactory.sleep(1000);// need to be improve
        click(deleteButton);
    }

    @Step("Click on Create geos...")
    public void clickOnCreate() {
        click(createGeoButton);
    }

    @Step("Click on Cancel create geos...")
    public void clickOnCancel() {
        click(cancelButton);
    }

    @Step("Click on Show More Button...")
    public void clickShowMoreButton() {
        click(showMoreButton);
    }

    @Step("Click on Show More Button...")
    public void clickShowLessButton() {
        click(showLessButton);
    }

    @Step("Click on Add IP for subnets...")
    public void clickAddIpForSubnets() {
        click(addSubnetButton);
    }

    @Step("Click on subnets expand button...")
    public void clickOnSubnetsExpandButton(String numberOfHiddenGeos) {
        // Button needs time to load.
        DriverFactory.sleep(1000);
        WebElement expandButton = findElementWithWait(By.xpath(subnetExpandButton.replace("{numberOfHiddenGeos}", numberOfHiddenGeos)));
        click(expandButton);
    }

    @Step("Click on subnets collapse button...")
    public void clickOnSubnetCollapseButton() {
        WebElement expandButton = findElementWithWait(By.xpath(subnetCollapseButton));
        click(expandButton);
    }

    @Step("Get edited by from the first row...")
    public String getEditedByFromFirstTableRow() {
        return getText(firstRowEditedByCell);
    }

    @Step("Get geo icon color...")
    public String getIconColorFromFirstTableRow() {
        return gpsColorIcon.getAttribute("data-color");
    }

    @Step("Get name from the first row...")
    public String getNameFromFirstTableRow() {
        WebElement nameCell = waitForElement(By.xpath(firstRowNameCell));
        return getAttributeValue(nameCell, "title");
    }

    @Step("Get the number of the subnets visible...")
    public int getNumberOfVisibleSubnets() {
        List<WebElement> restricted = DriverFactory.getBrowserInstance().findElements(By.xpath(numberOfVisibleSubnets));
        return restricted.size();
    }

    @Step("Get name from the first row...")
    public String getShortNameFromFirstTableRow() {
        WebElement nameCell = waitForElementToBePresent(By.xpath(firstRowShortNameCell));
        return getText(nameCell);
    }

    @Step("Get name from the first row...")
    public String getDescriptionFromFirstTableRow() {
        WebElement descriptionCell = driver.findElement(By.xpath(firstRowDescriptionCell));
        return getText(descriptionCell);
    }

    @Step("Get description tooltip...")
    public String getDescriptionTooltip() {
        WebElement descriptionCell = driver.findElement(By.xpath(firstRowDescriptionCell));
        return getAttributeValue(descriptionCell, "title");
    }

    @Step("Get subnet from the first row...")
    public String getSubnetFromFirstTableRow() {
        return getText(firstRowSubnetsCell);
    }

    @Step("Get the number of IPs for subnets...")
    public int getNumberOfIpsForSubnets(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(ipsForSubnets), counter).size();
    }

    @Step("Get the number of IPs for subnets...")
    public int getNumberOfGeos(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(firstRowNameCell), counter).size();
    }

    @Step("Get text from Show More Button...")
    public String getShowMoreButtonText() {
        return getText(showMoreButton);
    }

    @Step("Enter geos name to the input field...")
    public void enterGeosNameToInputField(String geosName) {
        inputText(createGeoNameInputField, geosName);
    }

    @Step("Enter subnets to the input field...")
    public void enterSubnetToInputField(String subnets) {
        inputText(subnetInputField, subnets);
    }

    @Step("Enter short name to the input field...")
    public void enterShortNameToInputField(String shortName) {
        inputText(shortNameInputField, shortName);
    }

    @Step("Enter short name to the input field...")
    public void enterColorToInputField(String colorCode) {
        inputText(colorInputField, colorCode);
    }

    @Step("Enter description name to the input field...")
    public void enterDescriptionToInputField(String description) {
        inputText(descriptionInputField, description);
    }
}
