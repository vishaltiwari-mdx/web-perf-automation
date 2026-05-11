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

public class ResourcesPage extends BasePage {
    private WebDriver driver;

    public ResourcesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button/i[@class='fa-solid fa-plus']")
    private WebElement plusButton;

    @FindBy(xpath = "//button/i[@class='fa-solid fa-minus']")
    private WebElement minusButton;

    @FindBy(xpath = "//button/i[@class='fa-solid fa-pen']")
    private WebElement editButton;

    @FindBy(xpath = "//*[@data-testid='ui-modal']//button[normalize-space()='Add']")
    private WebElement addButton;

    @FindBy(xpath = "//*[@data-testid='ui-modal']//button[normalize-space()='Confirm']")
    private WebElement confirmButton;

    @FindBy(xpath = "//*[@data-testid='ui-modal']//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[contains(@class,'tab-resources__overlay__subtitle')][contains(., 'No resources have been added.')]")
    private WebElement noResourcesAddedMessage;

    @FindBy(xpath = "//span[@class='badge font-weight-normal badge-info']")
    private WebElement resourcesCountIcon;

    @FindBy(xpath = "//a[@role='tab' and @aria-selected='true' and text()='IP Lines & Versions']")
    private WebElement linesAndVersionsTabSelected;

    @FindBy(xpath = "//a[@role='tab' and @aria-selected='false' and text()='IP Lines & Versions']")
    private WebElement linesAndVersionsTab;

    @FindBy(xpath = "//a[@role='tab' and text()='Unique aliases']")
    private WebElement uniqueAliasesTab;

    @FindBy(xpath = "//div[contains(@class,'text-center') and contains(., 'No unique aliases')]")
    private WebElement noUniqueAliasesImage;

    @FindBy(xpath = "//input[@placeholder='Search IPs']")
    private WebElement searchInputField;

    @FindBy(xpath = "//div[@class='custom-control custom-checkbox']/input[@type='checkbox']")
    private WebElement privateIpCheckbox;

    @FindBy(xpath = "//input[@disabled='disabled']/following-sibling::label[normalize-space()='Private resource']")
    private WebElement privateIpCheckboxDisabled;

    private String xpathAddedResource = "//span[@class='resource_cell__name text-truncate'][@title='{ipv}'][normalize-space()='{ipv}']";
    private String xpathAddedPrivateResource = "//i[@title='Added as private resource']/following-sibling::span[normalize-space()='{ip}']";
    private String xpathResource = "//button[contains(@class, 'list-group-item')]//div[contains(@class, 'text-truncate')]//span[normalize-space()='{ipName}']";
    private String xpathLinesAndVersionAlias = "//button[contains(@class, 'list-group-item')]//div[normalize-space()='{text}']";
    private String xpathSelectedVersion = "//div[@class='text-truncate col']/label[normalize-space()='Selected resource:']/following-sibling::span[normalize-space()='{ipv}']";
    private String xpathHighlightedElement =
            "//div[contains(@class,'font-weight-bold')][normalize-space()='{name}']/following-sibling::i[contains(@class,'fa-circle-check')]";
    private String xpathSelectedResource =
            "//span[contains(@class,'font-weight-bold')][contains(.,'{name}')]/parent::div/following-sibling::i[contains(@class,'fa-circle-check')]";
    private String xpathLockVersion = "//div[normalize-space()='{version}']/i[@class='fa-regular fa-lock fa-fw text-danger']";
    private String addedResources = "//div[@col-id='name']";
    private String xpathResources = "//div[contains(., 'Select IP')][@class='resources-ip-list']//button[contains(@class, 'list-group-item')]";
    private String lockIcons = "//div[@class='text-truncate font-weight-bold']/i[@class='fa-regular fa-lock fa-fw text-danger']";

    @Step("Getting tooltip message resources modal window resource ip...")
    public String getResourceTooltipMessage(String ipName) {
        WebElement resource = findElementWithWait(By.xpath(xpathResource.replace("{ipName}", ipName)));
        return getAttributeValue(resource, "title");
    }

    @Step("Getting tooltip message on resource modal window...")
    public String getTooltipMessage(String text) {
        WebElement resource = findElementWithWait(By.xpath(xpathLinesAndVersionAlias.replace("{text}", text)));
        return getAttributeValue(resource, "title");
    }

    @Step("Get selected resource tooltip on resources modal...")
    public String getSelectedVersionTooltipMessage(String name) {
        WebElement resource = findElementWithWait(By.xpath(xpathSelectedVersion.replace("{ipv}", name)));
        return getAttributeValue(resource, "title");
    }

    @Step("Verify text is truncated...")
    public boolean isLongStringTruncated(String text) {
        WebElement selectAlias = findElementWithWait(By.xpath(xpathLinesAndVersionAlias.replace("{text}", text)));
        return getAttributeValue(selectAlias, "class").contains("text-truncate");
    }

    @Step("Click on Plus Button to add the resource...")
    public void clickOnPlusButton() {
        click(plusButton);
    }

    @Step("Verify Add Resource button is present...")
    public boolean isPlusButtonPresent() {
        return isElementVisible(plusButton);
    }

    @Step("Click on IP to add as a resource...")
    public void selectIp(String ipName) {
        WebElement ipRow = findElementWithWait(By.xpath(xpathResource.replace("{ipName}", ipName)));
        click(ipRow);
    }

    @Step("Click on Add button...")
    public void clickOnAddButton() {
        // everything is happening so fast that selenium looks like doesn't know what is going on anc doesn't really save anything
        DriverFactory.sleep(2000);
        click(addButton);
    }

    @Step("Click on Confirm button...")
    public void clickOnConfirmButton() {
        click(confirmButton);
    }


    @Step("Click on Minus Button to remove the resource...")
    public void clickOnMinusButton() {
        click(minusButton);
    }

    @Step("Click on a resource {0}...")
    public void clickOnResource(String resourceName) {
        WebElement resourceRow = findElementWithWait(By.xpath(xpathAddedResource.replace("{ipv}", resourceName)));
        click(resourceRow);
    }

    @Step("Verify resource: {0} is displayed...")
    public boolean isResourcePresent(String resourceName) {
        WebElement resourceRow = findElementWithWait(By.xpath(xpathAddedResource.replace("{ipv}", resourceName)));
        return isElementVisible(resourceRow);
    }

    @Step("Click on Edit Button...")
    public void clickOnEditButton() {
        click(editButton);
    }

    @Step("Click on Cancel Button...")
    public void clickCancelOnModal() {
        click(cancelButton);
    }

    @Step("Get number of resources...")
    public int getNumberOfResources(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(addedResources), counter).size();
    }

    @Step("Verify No Resources Added Message is displayed...")
    public boolean isNoResourcesMsgPresent() {
        return isElementVisible(noResourcesAddedMessage);
    }

    @Step("Get number of IP resources from icon...")
    public String getNumberOfResourcesFromIcon() {
        return getText(resourcesCountIcon);
    }

    @Step("Open Lines&Versions tab...")
    public void selectLinesAndVersionsTab() {
        click(linesAndVersionsTab);
    }

    @Step("Is Lines&Versions tab selected...")
    public boolean isLinesAndVersionsTabPreselected() {
        return isElementVisible(linesAndVersionsTabSelected);
    }

    @Step("Open Unique Aliases tab...")
    public void selectUniqueAliasesTab() {
        click(uniqueAliasesTab);
    }

    @Step("Verify No Unique Aliases message is present...")
    public boolean isNoUniqueAliasesMssgPresent() {
        return isElementVisible(noUniqueAliasesImage);
    }

    @Step("Enter text into the search IPs field...")
    public void enterIpNameToInputField(String ipName) {
        enterTextSlowly(searchInputField, ipName);
        DriverFactory.sleep(3000);
    }

    @Step("Remove IP name from the search field...")
    public void clearSearchField() {
        clearInputField(searchInputField);
    }

    @Step("Get number of IPs on Resources modal...")
    public int getNumberOfIps(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathResources), counter).size();
    }

    @Step("Check private IP checkbox...")
    public void markResourceAsPrivate() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", privateIpCheckbox);
    }

    @Step("Verify IP checkbox is disabled...")
    public boolean isIpMarkedAsPrivate() {
        return isElementVisible(privateIpCheckboxDisabled);
    }

    @Step("Get number of lock icons...")
    public int getNumberOfLockIcons(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(lockIcons), counter).size();
    }

    @Step("Verify lock icon is present for selected version...")
    public boolean isLockIconPresentForSelectedAlias(String version) {
        WebElement lockVersion = findElementWithWait(By.xpath(xpathLockVersion.replace("{version}", version)));
        return isElementVisible(lockVersion);
    }

    @Step("Verify Private Resource is present...")
    public boolean isPrivateResourceIconPresent(String resourceName) {
        WebElement resource = findElementWithWait(By.xpath(xpathAddedPrivateResource.replace("{ip}", resourceName)));
        return isElementVisible(resource);
    }

    @Step("Get number of Private Resources icon...")
    public int getNumberPrivateResourcesIcon(String resourceName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathAddedPrivateResource.replace("{name}", resourceName)), counter).size();
    }

    @Step("Click on a private resource {0}...")
    public void clickOnPrivateResource(String resourceName) {
        WebElement resourceRow = findElementWithWait(By.xpath(xpathAddedPrivateResource.replace("{ip}", resourceName)));
        click(resourceRow);
    }

    @Step("Toggle Show all IPs Button...")
    public void toggleAllIpsButton() {
        // we need this sleep for the JS operations
        DriverFactory.sleep(2000);
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getBrowserInstance();
        js.executeScript("document.querySelector('label.custom-control-label','::before').click();");
        DriverFactory.sleep(1000);
    }

    @Step("Click on Line {0}...")
    public void clickOnLine(String lineName) {
        WebElement line = findElementWithWait(By.xpath(xpathLinesAndVersionAlias.replace("{text}", lineName)));
        click(line);
    }

    @Step("Click on Unique Alias/Version {0}...")
    public void selectIpVersion(String text) {
        WebElement version = findElementWithWait(By.xpath(xpathLinesAndVersionAlias.replace("{text}", text)));
        click(version);
    }

    @Step("Verify selected resource is highlighted...")
    public boolean isSelectedResourceHighlighted(String resourceName) {
        WebElement selectedResource = findElementWithWait(By.xpath(xpathSelectedResource.replace("{name}", resourceName)));
        return isElementVisible(selectedResource);
    }

    @Step("Verify selected line is highlighted...")
    public boolean isSelectedLineHighlighted(String line) {
        WebElement selectedLine = findElementWithWait(By.xpath(xpathHighlightedElement.replace("{name}", line)));
        return isElementVisible(selectedLine);
    }

    @Step("Verify selected version is highlighted...")
    public boolean isSelectedVersionHighlighted(String version) {
        WebElement selectedVersion = findElementWithWait(By.xpath(xpathHighlightedElement.replace("{name}", version)));
        return isElementVisible(selectedVersion);
    }

    @Step("Get number of highlighted resources...")
    public int getNumberOfHighlightedResources(String resourceName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathSelectedResource.replace("{name}", resourceName)), counter).size();
    }

    @Step("Get number of IPs on Resources modal by scrolling...")
    public int getNumberOfIpsByScroll() {
        String xpathForFirstScroll = "(//div[contains(., 'Select IP')][@class='resources-ip-list']//button[contains(@class, 'group-item')])[20]";
        String xpathForFirstScroll2 = "(//div[contains(., 'Select IP')][@class='resources-ip-list']//button[contains(@class, 'group-item')])[38]";
        return getNumberOfRowsForLazyLoad(xpathResources, xpathForFirstScroll, xpathForFirstScroll2);
    }

    @Step("Get number of aliases...")
    public int getNumberOfAliases(String alias, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathLinesAndVersionAlias.replace("{text}", alias)), counter).size();
    }

    @Step("Add Resource...")
    public void addResource(String searchBy, String ipName, String lineName, String ipVersion) {
        enterIpNameToInputField(searchBy);
        selectIp(ipName);
        DriverFactory.sleep(500);
        clickOnLine(lineName);
        DriverFactory.sleep(500);
        selectIpVersion(ipVersion);
        DriverFactory.sleep(500);
        clickOnAddButton();
    }

    @Step("Add Resource without version specified...")
    public void addResourceNoVersion(String searchBy, String ipName, String lineName) {
        enterIpNameToInputField(searchBy);
        selectIp(ipName);
        DriverFactory.sleep(500);
        clickOnLine(lineName);
        DriverFactory.sleep(500);
    }
}
