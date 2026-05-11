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
package com.methodics.phi.pageobject.labels;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LabelsManagementPage extends BasePage {
    private WebDriver driver;

    public LabelsManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@id='labelsSearchInput']")
    private WebElement searchLabelsInputField;

    @FindBy(xpath = "//span[@role='button-clear-labels']")
    private WebElement clearButton;

    @FindBy(xpath = "//div[@class='d-flex']//div[@role='label-name']")
    private List<WebElement> labels;

    @FindBy(css = "[data-testid='ui-btn-create-new-label']")
    private WebElement createLabelButton;

    @FindBy(css = "[data-testid='ui-btn-panel-edit-create-save']:not([disabled])")
    private WebElement saveButtonEnabled;

    @FindBy(css = "[data-testid='ui-btn-panel-edit-create-save'][disabled]")
    private WebElement saveButtonDisabled;

    @FindBy(css = "[data-testid='ui-btn-panel-edit-create-delete']")
    private WebElement deleteButton;

    @FindBy(css = "[data-testid='ui-btn-panel-edit-create-delete'][disabled]")
    private WebElement deleteButtonDisabled;

    @FindBy(css = "[data-testid='ui-btn-panel-edit-create-cancel']:not([disabled])")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]")
    private WebElement descriptionInfo;

    @FindBy(css = "[data-testid='ui-in-label_name_field']")
    private WebElement labelNameField;

    @FindBy(css = "[data-testid='ui-in-label-color-field']")
    private WebElement labelColorField;

    @FindBy(xpath = "//div[contains(@class,'page-name')][normalize-space()='Labels']")
    private WebElement labelSubHeader;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]/i[contains(@class, 'tag')]")
    private WebElement labelPageIcon;

    private String ipCountBadge = "//div[@title='{name}']/following-sibling::div//span[@data-testid='ui-badge']";
    private String xpathLabelName = "//div[@title='{name}'][contains(@class,'text-truncate')]";
    private String xpathSelectedLabel = "//button[contains(@class, 'active')]//div[@title='{name}']";
    private String xpathLabelIcon = "//div[@title='{name}']/parent::*//*[contains(@class, 'tag')]";
    private String xpathLabelHeader = "//div[contains(@class,'page-name') and contains(.,'Labels')]/span[@title='{name}']";

    @Step("Navigate to the Labels Management page")
    public void goToLabelsManagementPage(String url) {
        goTo(url);
    }

    @Step("Get the total number of labels displayed in the list")
    public int getNumberOfLabels() {
        DriverFactory.sleep(500);
        return labels.size();
    }

    @Step("Enter '{labelName}' into the label search field")
    public void enterLabelNameToSearchField(String labelName) {
        inputText(searchLabelsInputField, labelName);
    }

    @Step("Clear the label search field using the clear button")
    public void removeLabelNameFromSearchInputFieldByClickingClearButton() {
        waitForElementToBeClickable(clearButton);
        click(clearButton);
    }

    @Step("Clear the label search field using backspace")
    public void removeLabelNameFromSearchInputField() {
        clearInputFieldWithBackspace(searchLabelsInputField);
    }

    @Step("Click on the label named '{libName}' in the list")
    public LabelsManagementIpSectionPage clickOnLabel(String libName) {
        WebElement labelName = findElementWithWait(By.xpath(xpathLabelName.replace("{name}", libName)));
        click(labelName);
        return new LabelsManagementIpSectionPage(driver);
    }

    @Step("Get the number of labels with the name '{labelName}'")
    public int getNumberOfLabels(String labelName) {
        return driver.findElements(By.xpath(xpathLabelName.replace("{name}", labelName))).size();
    }

    @Step("Get the number of IPs from the badge for label '{labelName}'")
    public String getNumberOfIpsFromIcon(String labelName) {
        if (driver.findElements(By.xpath(ipCountBadge.replace("{name}", labelName))).size()==0) {
            return "0";
        }
        final WebElement icon = waitForElementToBeVisible(findElementWithFluentWait(By.xpath(ipCountBadge.replace("{name}", labelName))));
        return getText(icon);
    }

    @Step("Check if the IP count icon is not displayed for label '{labelName}'")
    public boolean ipCountIconNotDisplayed(String labelName) {
        return driver.findElements(By.xpath(ipCountBadge.replace("{name}", labelName))).size() == 0;
    }

    @Step("Click the 'Create new Label' button")
    public void clickCreateLabelButton() {
        click(createLabelButton);
    }

    @Step("Enter '{name}' as the label name")
    public void enterLabelName(String name) {
        inputText(labelNameField, name);
    }

    @Step("Create a new label with name '{name}'")
    public void createNewLabel(String name) {
        clickCreateLabelButton();
        inputText(labelNameField, name);
        clickSaveButton();
    }

    @Step("Clear the label name input field")
    public void clearLabelName() {
        clearInputField(labelNameField);
    }

    @Step("Clear the label color input field")
    public void clearLabelColor() {
        clearInputField(labelColorField);
    }

    @Step("Set the label color to '{color}'")
    public void selectLabelColor(String color) {
        inputText(labelColorField, color);
    }

    @Step("Click the 'Save' button for label changes")
    public void clickSaveButton() {
        click(saveButtonEnabled);
    }

    @Step("Check if the 'Save' button is disabled")
    public boolean isSaveButtonDisabled() {
        return isElementVisible(saveButtonDisabled);
    }

    @Step("Click the 'Delete' button for the label")
    public void clickDeleteButton() {
        waitTillClickableWithFluentWait(deleteButton).click();
    }

    @Step("Check if the 'Delete' button is disabled")
    public boolean isDeleteButtonDisabled() {
        return isElementVisible(deleteButtonDisabled);
    }

    @Step("Click the 'Cancel' button to discard label changes")
    public void clickCancelButton() {
        click(cancelButton);
    }

    @Step("Get the current value from the label name input field")
    public String getLabelName() {
        return getAttribute(labelNameField);
    }

    @Step("Get the current value from the label color input field")
    public String getLabelColor() {
        return getAttribute(labelColorField);
    }

    @Step("Check if the label sub-header is displayed")
    public boolean isLabelSubHeaderPresent() {
        return isElementVisible(labelSubHeader);
    }

    @Step("Check if the sub-header for the selected label '{label}' is displayed")
    public boolean isSelectedLabelSubHeaderPresent(String label) {
        WebElement labelHeader = findElementWithWait(By.xpath(xpathLabelHeader.replace("{name}", label)));
        return isElementVisible(labelHeader);
    }

    @Step("Check if the label '{labelName}' is selected in the list")
    public boolean isLabelSelected(String labelName) {
        WebElement selectedLabel = driver.findElement(By.xpath(xpathSelectedLabel.replace("{name}", labelName)));
        return isElementVisible(selectedLabel);
    }

    @Step("Get the icon color style for label '{labelName}'")
    public String getLabelIconColor(String labelName) {
        WebElement labelColor = findElementWithWait(By.xpath(xpathLabelIcon.replace("{name}", labelName)));
        return getAttributeValue(labelColor, "style");
    }

    @Step("Get the description info text displayed on the page")
    public String getDescriptionInfoText() {
        return getText(descriptionInfo);
    }

    @Step("Check if the page description info is present")
    public boolean isPageDescriptionPresent() {
        return isElementVisible(descriptionInfo);
    }

    @Step("Check if the label icon is displayed on the page")
    public boolean isLabelIconPresent() {
        return isElementVisible(labelPageIcon);
    }
}
