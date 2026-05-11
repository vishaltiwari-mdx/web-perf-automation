/**
 * ################################################################################
 * # Copyright (c) 2010-2024 Methodics, Inc.
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
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpProjectPropertiesTab extends BasePage {
    private WebDriver driver;

    public IpProjectPropertiesTab(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//*[contains(@class,'ip-details-project-properties__list')]//*[contains(@class,'project-properties-card')]//*[contains(@class,'number')]")
    private List<WebElement> projectPropertiesCardsIndices;

    @FindBy(xpath = "//div[contains(@class,'project-properties-filter-mode-select')]//button[@data-testid='menu-item']")
    private List<WebElement> filterDropdownOptions;

    @FindBy(xpath = "//div[contains(@class,'project-properties-filter-mode-select')]//*[contains(@class,'dropdown-menu')]")
    private WebElement filterDropdown;

    @FindBy(xpath = "//div[@class='tooltip-inner']")
    private WebElement tooltip;

    private String searchInputField = "//div[contains(@class, 'ip-details-project-properties')]//input";
    private String projectPropertiesCards = "//*[contains(@class,'ip-details-project-properties__list')]//*[contains(@class,'project-properties-card')]";
    private String ipDetailsProjectPropertiesListWrapper = "//*[contains(@class,'ip-details-project-properties__list')]";
    private String projPropsNotFoundOrAddedOverlay = "//*[contains(@class,'ip-details-project-properties__list')]//h5[normalize-space()='{text}']";
    private String projectProperties = "//*[contains(@class,'ip-details-project-properties__list')]//*[contains(@class,'project-properties-card')]//div[@data-testid='{property}']";
    private String property = "(//*[contains(@class,'project-properties-card')])[{index}]//div[@data-testid='{property}']";
    private String propertyCardBodyToHover = "(//*[contains(@class,'project-properties-card')])[{index}]//*[contains(@class,'body')]";
    private String copyToClipboardButton = "(//*[contains(@class,'project-properties-card')])[{index}]//div[@data-testid='{property}']//div[@data-testid='copy-button']";
    private String showMoreButton = "(//*[contains(@class,'project-properties-card')])[{index}]//div[@data-testid='{property}']//*[contains(@class,'fa-ellipsis')]";
    private String showLessButton = "(//*[contains(@class,'project-properties-card')])[{index}]//div[@data-testid='{property}']//*[contains(@class,'fa-angle-up')]";
    private String truncatedElement = "(//*[contains(@class,'project-properties-card')])[{index}]//div[@data-testid='{property}']//*[contains(@class,'default')]";
    private String filterButton = "//div[contains(@class,'project-properties-filter-mode-select')]//button[.//span[normalize-space(text())='{text}']]";
    private String filterButtonChecked = "//div[contains(@class,'project-properties-filter-mode-select')]//button[.//span[normalize-space(text())='{text}']]//*[contains(@class,'menu-item__top-row__checkmark')]";
    private String filterDropdownName = "//*[@data-testid='ui-input-group']//button[@data-testid='ui-btn-menu-item']//*[@class='flex-grow-1 text-truncate menu-item-text' and text()='{name}']";

    @Step("Get number Project Properties List Wrapper...")
    public int getNumberOfProjectPropertiesWrapper() {
        List<WebElement> ipDetailsProjectPropertiesListWrapperElm = findElements(By.xpath(ipDetailsProjectPropertiesListWrapper));
        return ipDetailsProjectPropertiesListWrapperElm.size();
    }

    @Step("Verify is Project Properties cards present...")
    public boolean isProjectPropertiesCardsNotPresent() {
        List<WebElement> projectPropertiesCardsElm = findElements(By.xpath(projectPropertiesCards));
        return isListOfElementsNotToBePresentWithoutWait(projectPropertiesCardsElm);
    }

    @Step("Get number Project Properties cards...")
    public int getNumberOfProjectPropertiesCards() {
        List<WebElement> projectPropertiesCardsElm = findElements(By.xpath(projectPropertiesCards));
        return projectPropertiesCardsElm.size();
    }

    @Step("Get number Project Properties...")
    public int getNumberOfProjectProperties(String propertyTestId,int counter) {
        return waitForNumberOfElementsToBe(By.xpath(projectProperties.replace("{property}", propertyTestId)), counter).size();
    }

    @Step("Get list of Project Properties cards indices...")
    public List<String> getListOfProjectPropertiesCardsIndices() {
        return getTextsForWebElementsElements(projectPropertiesCardsIndices);
    }

    @Step("Verify the Property element not present...")
    public boolean isPropertyNotPresent(String propertyName,String index) {
        return isElementNotToBePresentWithoutWait(property.replace("{property}", propertyName).replace("{index}", index));
    }

    @Step("Get the text of the Property element present ...")
    public String getPropertyText(String propertyName,String index) {
        WebElement propertyElement = findElementWithWait(By.xpath(property.replace("{property}", propertyName).replace("{index}", index)));
        return getText(propertyElement);
    }

    @Step("Hover over copy-to-clipboard icon...")
    public void hoverCopyToClipboardIcon(String index) {
        WebElement copyToClipboardElement = findElementWithWait(By.xpath(propertyCardBodyToHover.replace("{index}", index)));
        hoverOverElement(copyToClipboardElement);
    }

    @Step("Click on copy-to-clipboard button...")
    public void clickOnCopyToClipboard(String propertyName,String index) {
        WebElement copyToClipboardElement = findElementWithWait(By.xpath(copyToClipboardButton.replace("{property}", propertyName).replace("{index}", index)));
        click(copyToClipboardElement);
    }

    @Step("Verify the Property element not present...")
    public boolean isCopyToClipboardNotPresent(String propertyName,String index) {
        return isElementNotToBePresentWithoutWait(copyToClipboardButton.replace("{property}", propertyName).replace("{index}", index));
    }

    @Step("Click on Show More...")
    public void clickOnShowMoreButton(String propertyName,String index) {
        WebElement showMoreBtn = findElementWithWait(By.xpath(showMoreButton.replace("{property}", propertyName).replace("{index}", index)));
        click(showMoreBtn);
    }

    @Step("Click on Show Less...")
    public void clickOnShowLessButton(String propertyName,String index) {
        WebElement showLessBtn = findElementWithWait(By.xpath(showLessButton.replace("{property}", propertyName).replace("{index}", index)));
        click(showLessBtn);
    }

    @Step("Get the style property value by property...")
    public String getTheStyleValByProperty(String propertyName,String index,String styleProperty) {
        WebElement truncatedElm = findElementWithWait(By.xpath(truncatedElement.replace("{property}", propertyName).replace("{index}", index)));
        return truncatedElm.getCssValue(styleProperty);
    }
    //Filter
    @Step("verify the Filter button with dynamic text")
    public boolean verifyTheFilterButtonWithDynamicText(String buttonText) {
        return isElementVisible(filterButton.replace("{text}", buttonText));
    }

    @Step("verify No project properties found/added...")
    public boolean verifyNoProjectPropertiesWithDynamicText(String textMessage) {
        return isElementVisible(projPropsNotFoundOrAddedOverlay.replace("{text}", textMessage));
    }

    @Step("verify No project properties found/added not present without wait...")
    public boolean verifyNoProjectPropertiesNotPresentWithDynamicText(String textMessage) {
        return isElementNotToBePresentWithoutWait(projPropsNotFoundOrAddedOverlay.replace("{text}", textMessage));
    }

    @Step("click the dropdown buttons by text...")
    public void clickTheFilterDropdownButtonsByText(String buttonText) {
        WebElement filterBtn = findElementWithWait(By.xpath(filterButton.replace("{text}", buttonText)));
        click(filterBtn);
    }

    @Step("verify the Filter dropdown is visible...")
    public boolean verifyTheFilterDropdownVisible() {
            return isElementVisible(filterDropdown);
    }

    @Step("verify the Filter dropdown option has check tic visible...")
    public boolean verifyTheSelectedFilterOptionHasSelectedIcon(String buttonText) {
        return isElementVisible(filterButtonChecked.replace("{text}", buttonText));
    }

    @Step("verify the Filter dropdown options list texts...")
    public Boolean verifyTheFilterDropdownOptionsText(String value) {
        WebElement dropDownValue = findElementWithWait(By.xpath(filterButton.replace("{text}", value)));
        return isElementVisible(dropDownValue);
    }

    @Step("verify element is enabled...")
    public boolean verifyTheElementIsEnabled(String buttonText) {
        WebElement filterBtn = findElementWithWait(By.xpath(filterButton.replace("{text}", buttonText)));
        return isElementEnabled(filterBtn);
    }

    @Step("input search properties...")
    public void inputPropertiesInSearch(String searchTem) {
        WebElement searchInputFieldElm = findElementWithWait(By.xpath(searchInputField));
        searchInputFieldElm.clear();
        searchInputFieldElm.click();
        enterTextSlowly(searchInputFieldElm, searchTem);
        DriverFactory.sleep(1000);
    }

    @Step("clear search term...")
    public void clearPropertiesInSearch() {
        WebElement searchInputFieldElm = findElementWithWait(By.xpath(searchInputField));
        clearInputFieldWithBackspace(searchInputFieldElm);
    }

    @Step("get Project Properties Tooltip value...")
    public String getProjectPropertiesTooltip(String propertyName,String index) {
        try {
            hoverOverElement(findElementWithWait(By.xpath(copyToClipboardButton.replace("{property}", propertyName).replace("{index}", index))));
            return getObjectToolTip();
        } catch (TimeoutException t) {
            logger.warn("IP Hook Copy button/tooltip is not visible");
            return null;
        }
    }

    @Step("Get tooltip text...")
    public String getObjectToolTip() {
        waitTillVisibleWithFluentWait(tooltip);
        return tooltip.getText().trim();
    }

    @Step("click copy button project properties fields...")
    public void clickCopyButtonProjectPropertyFields(String propertyName,String index) {
        hoverOverElement(findElementWithWait(By.xpath(propertyCardBodyToHover.replace("{index}", index))));
        WebElement copyButton = findElementWithWait(By.xpath(copyToClipboardButton.replace("{property}", propertyName).replace("{index}", index)));
        copyButton.click();
    }
}
