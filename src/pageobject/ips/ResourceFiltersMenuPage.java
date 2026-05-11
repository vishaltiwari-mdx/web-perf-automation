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
import static com.methodics.phi.util.Constants.ALL_LIBRARIES;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResourceFiltersMenuPage extends BasePage {
    private final WebDriver driver;

    public ResourceFiltersMenuPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class,'label-selector')]")
    private WebElement labelSelector;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//div[contains(@class,'label-selector')]//input")
    private WebElement labelsInputField;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class, 'active multiselect label-selector')]//i[contains(@class, 'caret')]")
    private WebElement closeLabelsDropdownButton;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class,'library-selector')]")
    private WebElement librarySelector;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class,'library-selector')]//input")
    private WebElement libraryInputField;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class, 'library-selector input-dropdown_regular mb-3 multiselect--active')]//i[contains(@class, 'caret')]")
    private WebElement closeLibsDropdownButton;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[@class='quick-filters']")
    private List<WebElement> quickFiltersMenu;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//span[normalize-space()='No property filters configured.']")
    private WebElement noQuickFiltersMessage;

    @FindBy(xpath = "//*[@data-testid='tabpanel-resource-browser-tab']//div[contains(@class,'quick-filters__select')]")
    private List<WebElement> quickFilters;

    @FindBy(xpath = "//*[@data-testid='ui-btn-modal-header-close']")
    private WebElement closeResourcePage;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//button[contains(.,'Cancel')]")
    private WebElement cancelAddResource;

    @FindBy(xpath = "//*[@data-testid='modal-resources']//button[contains(@class,'clear-button')]")
    private WebElement clearButton;

    private final String labels = "//*[@data-testid='modal-resources']//div[contains(@class,'label')]//li[@class='multiselect__element']";
    private final String dropdownLabel = "//*[@data-testid='modal-resources']//div[@class='d-inline text-truncate']/span[@title='{label}']";
    private final String noLabels = "//*[@data-testid='modal-resources']//div[contains(@class,'label')]//span[normalize-space()='No elements found.']";
    private final String appliedLabels = "//*[@data-testid='modal-resources']//span[contains(@class,'badge-tag_label')]";
    private final String appliedLabel = "//*[@data-testid='modal-resources']//span[contains(@class,'tag_label')]/span[@class='text-truncate'][@title='{label}']";
    private final String removeLabelButton = "//*[@data-testid='modal-resources']//span[normalize-space()='{label}']//*[contains(@class,'remove')]";
    private final String libs = "//*[@data-testid='modal-resources']//li[@class='multiselect__element']//i[contains(@class, 'folder')]";
    private final String dropdownLib = "//*[@data-testid='modal-resources']//div[contains(@class, truncate)]/span[@title='{lib}'][@class='library-selector__option-title']";
    private final String noLibraries = "//*[@data-testid='modal-resources']//div[contains(@class,'library')]//span[normalize-space()='No elements found.']";
    private final String selectedLibrary = "//*[@data-testid='modal-resources']//div[contains(@class,'library-selector') and @title='{lib}']";
    private final String property = "//*[@data-testid='modal-resources']//span[contains(@class,'multiselect__placeholder')][contains(@class, 'text-truncate')][@title='{name}']";
    private final String value = "//*[@data-testid='modal-resources']//div[contains(@class,'multiselect--active')]//div[contains(@class,'truncate')]//span[@title='{option}']";
    private final String appliedFilter = "//*[@data-testid='modal-resources']//span[contains(@class, 'quick-filter')]/span[contains(@class,'truncate')][@title='{value}']";
    private final String appliedFilters = "//*[@data-testid='modal-resources']//span[contains(@class, 'quick-filter')]";
    private final String removeAppliedFilterButton = "//*[@data-testid='modal-resources']//span[normalize-space()='{text}']//*[contains(@class,'remove')]";
    private final String xpathAGridRow = "//*[@data-testid='modal-resources']//div[@class='ag-center-cols-container']/div[@row-index='{rowIndex}']";
    private final String xpathLibraryDropdownList = "//*[@data-testid='modal-resources']//div[@class='multiselect library-selector multiselect--active']/div[@class='multiselect__content-wrapper']";
    private final String xpathLabelsDropdownList = "//*[@data-testid='modal-resources']//div[@class='multiselect label-selector multiselect--active']/div[@class='multiselect__content-wrapper']";

    @Step("Getting aGrid row by index...")
    public WebElement getAGridRow(String rowIndex) {
        final WebElement aGridRow = findElementWithWait(By.xpath(xpathAGridRow.replace("{rowIndex}", rowIndex)));
        return aGridRow;
    }

    @Step("Getting libraries dropdown element...")
    public WebElement getLibraryDropdownList() {
        final WebElement libraryDropDownList = findElementWithWait(By.xpath(xpathLibraryDropdownList));
        return libraryDropDownList;
    }

    @Step("Getting labels dropdown element...")
    public WebElement getLabelsDropdownList() {
        final WebElement labelsDropdownList = findElementWithWait(By.xpath(xpathLabelsDropdownList));
        return labelsDropdownList;
    }

    @Step("Open Labels dropdown...")
    public void openLabelsDropdown() {
        waitTillClickableWithFluentWait(labelSelector).click();
        DriverFactory.sleep(500);
    }

    @Step("Enter label name...")
    public void enterLabelName(String labelName) {
        enterTextSlowly(labelsInputField, labelName);
        DriverFactory.sleep(500);
    }

    @Step("Remove label name from the search field...")
    public void removeLabelName() {
        waitForElementToBeVisible(labelsInputField);
        clearTextByBackspace(labelsInputField);
        DriverFactory.sleep(500);
    }

    @Step("Close Labels dropdown...")
    public void closeLabelsDropdown() {
        click(closeLabelsDropdownButton);
    }

    @Step("Get number of labels...")
    public int getNumberOfLabels(int counter) {
        waitForNumberOfElementsToBe(By.xpath(labels), counter);
        return findElementsWithWait(By.xpath(labels), counter).size();
    }

    @Step("Verify no elements found message is displayed...")
    public boolean noLabelsFound() {
        final WebElement message = findElementWithWait(By.xpath(noLabels));
        return isElementVisible(message);
    }

    @Step("Verify label is present in dropdown...")
    public int isLabelDisplayedInDropdown(String label, int counter) {
        return waitForElements(By.xpath(dropdownLabel.replace("{label}", label)), counter).size();
    }

    @Step("Select label from dropdown...")
    public void selectLabel(String label) {
        openLabelsDropdown();
        final WebElement labelName = findElementWithWait(By.xpath(dropdownLabel.replace("{label}", label)));
        click(labelName);
        DriverFactory.sleep(500);
    }

    @Step("Verify Label is selected...")
    public boolean isLabelSelected(String labelName) {
        final WebElement label = findElementWithWait(By.xpath(appliedLabel.replace("{label}", labelName)));
        return isElementVisible(label);
    }

    @Step("Get number of applied labels......")
    public int getNumberOfAppliedLabels(int counter) {
        return findElementsWithWait(By.xpath(appliedLabels), counter).size();
    }

    @Step("Remove label...")
    public void removeLabel(String label) {
        final WebElement deleteButton = findElementWithWait(By.xpath(removeLabelButton.replace("{label}", label)));
        click(deleteButton);
    }

    @Step("Open Libraries dropdown...")
    public void openLibrariesDropdown() {
        waitTillClickableWithFluentWait(librarySelector).click();
    }

    @Step("Enter library name...")
    public void enterLibraryName(String libName) {
        inputText(libraryInputField, libName);
    }

    @Step("Type library name char...")
    public void typeLibraryName(String libName) {
        enterTextSlowly(libraryInputField, libName);
    }

    @Step("Remove library name from the search field...")
    public void removeLibraryName() {
        waitForElementToBeVisible(libraryInputField);
        clearTextByBackspace(libraryInputField);
    }

    @Step("Close Libraries dropdown...")
    public void closeLibrariesDropdown() {
        click(closeLibsDropdownButton);
    }

    @Step("Get number of libraries...")
    public int getNumberOfLibraries() {
        final List<WebElement> visibleElements = findElementsWithFluentWait(By.xpath(libs));
        return getNumberOfVisibleElements(visibleElements);
    }

    @Step("Verify library is present in dropdown...")
    public int isLibraryDisplayed(String libName, int counter) {
        return waitForElements(By.xpath(dropdownLib.replace("{lib}", libName)), counter).size();
    }

    @Step("Click on library...")
    public void clickOnLibrary(String libName) {
        final WebElement library = findElementWithWait(By.xpath(dropdownLib.replace("{lib}", libName)));
        click(library);
    }

    @Step("Select library from dropdown...")
    public void selectLibrary(String library) {
        openLibrariesDropdown();
        DriverFactory.sleep(1500);
        clickOnLibrary(library);
    }

    @Step("Verify Library is selected...")
    public boolean isLibrarySelected(String libName) {
        final WebElement library = findElementWithWait(By.xpath(selectedLibrary.replace("{lib}", libName)));
        return isElementVisible(library);
    }

    @Step("Verify no library is selected...")
    public boolean noLibrarySelected(String libName) {
        waitForNumberOfElementsToBe(By.xpath(selectedLibrary.replace("{lib}", libName)), 0);
        return true;
    }

    @Step("Select all libraries...")
    public void selectAllLibraries() {
        openLibrariesDropdown();
        clickOnLibrary(ALL_LIBRARIES);
    }

    @Step("Verify no elements found message is displayed...")
    public boolean noLibrariesFound() {
        final WebElement message = findElementWithWait(By.xpath(noLibraries));
        return isElementVisible(message);
    }

    @Step("Verify Quick Filters menu is not displayed...")
    public boolean quickFiltersMenuNotDisplayed() {
        return quickFiltersMenu.size() == 0;
    }

    @Step("Verify No Quick Filters Configured message is displayed...")
    public boolean isNoQuickFiltersNoticePresent() {
        return isElementVisible(noQuickFiltersMessage);
    }

    @Step("Get number of quick filters...")
    public int getNumberOfQuickFilters() {
        return quickFilters.size();
    }

    @Step("Select property value from dropdown...")
    public void selectPropertyValue(String propName, String... options) {
        for (final String choice : options) {
            final WebElement quickFilterSelector = findElementWithWait(By.xpath(property.replace("{name}", propName)));
            click(quickFilterSelector);
            final WebElement choiceValue = findElementWithWait(By.xpath(value.replace("{option}", choice)));
            click(choiceValue);
            DriverFactory.sleep(500);
        }
    }

    @Step("Remove applied quick filter...")
    public void removePropertyValue(String... choices) {
        for (final String choice : choices) {
            final WebElement removeButton = findElementWithWait(By.xpath(removeAppliedFilterButton
                    .replace("{text}", choice)));
            click(removeButton);
        }
    }

    @Step("Get number of applied quick filters...")
    public int getNumberOfAppliedFilters(int counter) {
        return findElementsWithWait(By.xpath(appliedFilters), counter).size();
    }

    @Step("Clear all applied quick filters...")
    public void clearAllQuickFilters() {
        click(clearButton);
    }

    @Step("Verify that remove button is present...")
    public boolean isDeleteButtonPresent(String name) {
        final WebElement deleteButton = findElementWithWait(By.xpath(removeLabelButton.replace("{label}", name)));
        return isElementVisible(deleteButton);
    }

    @Step("Close resource screen ...")
    public void closeResourcePage() {
        click(closeResourcePage);
    }

    public void clickOnCancelButton() {
        click(cancelAddResource);
    }
}