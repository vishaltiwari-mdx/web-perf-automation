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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpInfoTab extends BasePage {
    private WebDriver driver;

    public IpInfoTab(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@id='ipInfoCard']//div[normalize-space()='DM type']//following-sibling::div")
    private WebElement dmType;

    @FindBy(xpath = "//span[contains(@class, 'badge')]/i[contains(@class, 'tag')]")
    private List<WebElement> labels;

    @FindBy(xpath = "//div[normalize-space()='Labels']/following-sibling::*[normalize-space()='No Labels selected.']")
    private WebElement noLabelsAdded;

    @FindBy(xpath = "//div[@id='ipInfoCard']//div[normalize-space()='Description']/following-sibling::*")
    private WebElement description;

    @FindBy(xpath = "//div[@id='ipInfoCard']//div[normalize-space()='Owners']/following-sibling::div//i[contains(@class, 'user ')]/parent::div")
    private WebElement userOwners;

    @FindBy(xpath = "//div[@id='ipInfoCard']//div[normalize-space()='Owners']/following-sibling::div//i[contains(@class, 'users ')]/parent::div")
    private WebElement groupOwners;

    @FindBy(css = "span[data-testid=ui-tooltip-text-content]")
    private WebElement ipCreatorName;

    @FindBy(xpath = "//div[@class='me-4' and not(@style='display: none;')]//span[@class='copy-to-clipboard-button__label']")
    private WebElement copyUuid;

    private String xpathLabel = "//span[contains(@class,'badge')]//*[@class='text-truncate'][@title='{label}']";
    private String ownersExpandButton = "//*[@id='{fieldName}']//a/span[normalize-space()='+{numberOfHiddenItems}']";
    private String xpathOwnersExpandButton = "//*[@id='{fieldName}']//div[@class='link-component d-flex align-items-center ml-2 normal']//a";
    private String versionTooltip = "//div[@data-testid='ui-tooltip-content-copy-to-clipboard-tooltip'][normalize-space()='{name}']"
    ;
    private String noGeoSelected = "//div[normalize-space()='{geoType}']/following-sibling::*//div[contains(@class, 'geos-section__no-geos')]";
    private String geo = "//div[normalize-space()='{geoType}']/following-sibling::*//span[contains(@class,'badge')]//*[@class='text-truncate'][normalize-space()='{geoShortName}']";
    private String gpsIconColor = "//div[normalize-space()='{geoType}']/following::*[contains(@data-color, '{gpsIconColor}')]/following::span[normalize-space()='{name}']";
    private String numberOfVisibleGeos = "//div[normalize-space()='{geoType}']/following-sibling::*//span[contains(@class,'badge')]//*[@class='text-truncate']";
    private String xpathGeofencingLabel = "//div[contains(@class,'ip-details-info-tab-card') and normalize-space()='{geoType}']";
    private String xpathHintIcon = "(//*[@class='card ip-details-info-tab-card px-4 py-3 mb-1']//h5[@title='IP - {fqn}']//ancestor::div[@data-testid='ui-card-header']//div[@data-testid='ui-tooltip-trigger'])[last()]";
    private String xpathModificationTime = "//div[contains(@class, 'ip-details-info-tab-card')]//div[contains(@class, 'ip-details-info-tab-card__header')]//h5[@title='{infoTabCardName}']//following-sibling::div/span";
    private String xpathGroups = "//div[@id='{fieldName}']//i[contains(@class, 'users')]/following-sibling::*/span[@class='owner__expandable-content-item text-truncate'][@title]";
    private String xpathUsers = "//div[@id='{fieldName}']//i[contains(@class, 'user ')]/following-sibling::*/span[@class='owner__expandable-content-item text-truncate'][@title]";
    private String xpathOwners = "//div[@id='{fieldName}']//div[normalize-space()='Owners']/following-sibling::div";
    private String xpathTabCard = "//h5[@title='{infoTabCardName}']";
    private String ipVersionName = "//span[@data-testid='ui-tooltip-text-content'][normalize-space(.)='{value}']";


    @Step("Get IP creator name...")
    public String getCreatorName() {
        waitTillVisibleWithFluentWait(ipCreatorName);
        String[] creator = getText(ipCreatorName).split("\\s+");
        return creator[7];
    }

    @Step("Get IP creator time...")
    public String getCreationTime() {
        String[] tooltip = getText(ipCreatorName).split("\\s+");
        return tooltip[2] + " at " + tooltip[4] + " " + tooltip[5];
    }

    public boolean isToolTipVisible(String value) {
        WebElement locator = waitForElement(By.xpath(ipVersionName.replace("{value}", value)));
        return isElementVisible(locator);
    }

    @Step("Hover over IP info card hint icon...")
    public void hoverOverHintIcon(String ipName) {
        WebElement hintIcon = findElementWithWait(By.xpath(xpathHintIcon.replace("{fqn}", ipName)));
        hoverOverElement(hintIcon);
    }

    @Step("Hover over uuid icon...")
    public void hoverCopyUuid(String tabCardName) {
        // needed to move cursor over card to get copyUuid button to be visible
        WebElement infoTabCard = findElementWithFluentWait(By.xpath(xpathTabCard.replace("{infoTabCardName}", tabCardName)));
        hoverOverElement(infoTabCard);
        hoverOverElement(copyUuid);
    }

    @Step("Click copy uuid button...")
    public void clickCopyUuid() {
        click(copyUuid);
    }

    @Step("Get IP DM type...")
    public String getIpDmType() {
        return getText(dmType);
    }

    @Step("Verify dm type field is displayed...")
    public boolean isDmTypeFieldVisible() {
        return isElementVisible(dmType);
    }

    @Step("Get the number of attached labels...")
    public int getNumberOfLabels() {
        waitTillClickableWithFluentWait(labels.get(0));
        return getNumberOfVisibleElements(labels);
    }

    @Step("Verify IP label: {0} is displayed...")
    public boolean isLabelDisplayed(String labelName) {
        WebElement label = findElementWithWait(By.xpath(xpathLabel.replace("{label}", labelName)));
        return isElementVisible(label);
    }

    @Step("Get IP description...")
    public String getIpDescription() {
        return getText(description);
    }

    @Step("Get IP modification time...")
    public String getModificationTime(String cardName) {
        WebElement modificationTime = findElementWithWait(By.xpath(xpathModificationTime.replace("{infoTabCardName}", cardName)));
        return getText(modificationTime);
    }

    @Step("Get user owners...")
    public String getUserOwners() {
        return getText(userOwners);
    }

    @Step("Get group owners...")
    public String getGroupOwners() {
        return getText(groupOwners);
    }

    @Step("Get user owners...")
    public String getOwners(String fieldName) {
        return getText(findElementWithWait(By.xpath(xpathOwners.replace("{fieldName}", fieldName))));
    }

    @Step("Is owners expand button visible...")
    public boolean isExpandBtnToOpenPermissionsVisible(String fieldName, String buttonText) {
        if (isElementVisible(ownersExpandButton.replace("{fieldName}", fieldName).replace("{numberOfHiddenItems}", buttonText)))
            return true;
        else
            return isElementVisible(findElementWithWait(By.xpath(ownersExpandButton.replace("{fieldName}", fieldName).replace("{numberOfHiddenItems}", String.valueOf(Integer.parseInt(buttonText) + 1)))));
    }

    @Step("Click on owenrs expand button to open Permissions tab...")
    public void clickOnExpandBtnToOpenPermissions(String fieldName) {
        DriverFactory.sleep(3500);
        List<WebElement> expandButton = driver.findElements(By.xpath(xpathOwnersExpandButton.replace("{fieldName}", fieldName)));
        clickOnVisibleElement(expandButton);
        DriverFactory.sleep(5000);
    }

    @Step("Get the number of users...")
    public int getNumberOfUserOwners(String fieldName) {
        DriverFactory.sleep(1000);
        List<WebElement> users = driver.findElements(By.xpath(xpathUsers.replace("{fieldName}", fieldName)));
        return getNumberOfVisibleElements(users);
    }

    @Step("Get the number of groups...")
    public int getNumberOfGroupOwners(String fieldName) {
        List<WebElement> groups = driver.findElements(By.xpath(xpathGroups.replace("{fieldName}", fieldName)));
        return getNumberOfVisibleElements(groups);
    }

    @Step("Verify 'No labels added' message is present...")
    public boolean isNoLabelsAddedMessagePresent() {
        return isElementVisible(noLabelsAdded);
    }

    @Step("Get text from empty geos row...")
    public String noGeosSelected(String geoType) {
        WebElement geosRow = driver.findElement(By.xpath(noGeoSelected.replace("{geoType}", geoType)));
        return getText(geosRow);
    }

    @Step("Get list of attached geos...")
    public boolean isGeoDisplayed(String geoType, String geoShortName) {
        WebElement geofencingLabelName = findElementWithWait(By.xpath(geo.replace("{geoType}", geoType).replace("{geoShortName}", geoShortName)));
        return isElementVisible(geofencingLabelName);
    }

    @Step("Hover over geo badge...")
    public void hoverOverGeoBadge(String geoType, String geoShortName) {
        WebElement geosBadge = findElementWithWait(By.xpath(geo.replace("{geoType}", geoType).replace("{geoShortName}", geoShortName)));
        hoverOverElement(geosBadge);
    }

    @Step("Verify removed geos are not visible...")
    public int isRemovedGeosNotVisible(String geoType, String geoShortName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(geo.replace("{geoType}", geoType).replace("{geoShortName}", geoShortName)), counter).size();
    }

    @Step("Verifying Geofencing label is visible...")
    public boolean isGeofencingLabelVisible(String geoType) {
        WebElement geofencingLabel = driver.findElement(By.xpath(xpathGeofencingLabel.replace("{geoType}", geoType)));
        return isElementVisible(geofencingLabel);
    }

    @Step("Verify gps icon color...")
    public boolean isGpsIconColorDisplayed(String geoType, String geoIconColor, String geoName) {
        WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsIconColor.replace("{geoType}", geoType).replace("{gpsIconColor}", geoIconColor).replace("{name}", geoName)));
        return isElementVisible(gpsColor);
    }

    @Step("Get the number of the geos visible...")
    public int getNumberOfVisibleGeos(String geoType) {
        List<WebElement> restricted = driver.findElements(By.xpath(numberOfVisibleGeos.replace("{geoType}", geoType)));
        return getNumberOfVisibleElements(restricted);
    }
}
