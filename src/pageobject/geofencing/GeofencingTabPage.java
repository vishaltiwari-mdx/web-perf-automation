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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GeofencingTabPage extends BasePage {
    private WebDriver driver;

    public GeofencingTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//a[@data-testid='geofencing-tab']")
    private WebElement geofencingTab;

    @FindBy(css = ".geos-list-item__icon-remove")
    private WebElement removeSelectedGeos;

    private String xpathSelectedGeo = "//label[contains(.,'{geoFencesLabelName}')]/following-sibling::div[@class='geos-multiselect']//a[.='{name}']";
    private String xpathGeoDropdownMenuChoice = "//label[contains(.,'{geoFencesLabelName}')]/following::div[@class='geos-multiselect']//span[normalize-space()='{name}'][@title]";
    private String geoFencesSelector = "//label[contains(.,'{geoFencesLabelName}')]/../div//*[@class='caret-icon__wrapper']";
    private String gpsGeoIconColor = "//label[contains(.,'{geoType}')]/following::div[@class='geos-multiselect']//i[contains(@data-color, '{gpsIconColor}')]/following::span[normalize-space()='{name}']";

    @Step("Navigate to Geofencing Tab...")
    public void goToGeofencingSection() {
        click(geofencingTab);
    }

    @Step("Verify Geofencing Tab is visible...")
    public boolean isGeofencingTabVisible() {
        return isElementVisible(geofencingTab);
    }

    @Step("Click on remove selected geos...")
    public void clickOnRemoveSelectedGeos(String geoType, String geoIconColor, String geoName) {
        WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsGeoIconColor.replace("{geoType}", geoType).replace("{gpsIconColor}", geoIconColor).replace("{name}", geoName)));
        hoverOverElement(gpsColor);
        click(removeSelectedGeos);
    }

    @Step("Select geos from dropdown...")
    public void selectGeos(String geoFencesLabelName, String... geoNames) {
        for (String geo : geoNames) {
            WebElement geoSelector = waitTillClickableWithFluentWait(driver.findElement(By.xpath(geoFencesSelector.replace("{geoFencesLabelName}", geoFencesLabelName))));
            click(geoSelector);
            WebElement dropdownItem = findElementWithFluentWait(By.xpath(xpathGeoDropdownMenuChoice.replace("{geoFencesLabelName}", geoFencesLabelName).replace("{name}", geo)));
            click(dropdownItem);
        }
    }

    @Step("Get geo name tooltip message in dropdown list...")
    public String getDropdownListGeoNameTooltipMessage(String geoFencesLabelName, String geoName) {
        WebElement geoSelector = waitForElementToBeClickable(driver.findElement(By.xpath(geoFencesSelector.replace("{geoFencesLabelName}", geoFencesLabelName))));
        click(geoSelector);
        WebElement selectedGeosName = driver.findElement(By.xpath(xpathGeoDropdownMenuChoice.replace("{geoFencesLabelName}", geoFencesLabelName).replace("{name}", geoName)));
        return getAttributeValue(selectedGeosName, "title");
    }

    @Step("Get the number of selecting dropdowns...")
    public int getNumberOfSelectingGeoDropdown(String geoType, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(geoFencesSelector.replace("{geoType}", geoType)), counter).size();
    }

    @Step("Verify selected geos are visible...")
    public boolean isSelectedGeosVisible(String geoFencesLabelName, String geoName) {
        WebElement selectedGeosName = driver.findElement(By.xpath(xpathSelectedGeo.replace("{geoFencesLabelName}", geoFencesLabelName).replace("{name}", geoName)));
        return isElementVisible(selectedGeosName);
    }

    @Step("Verify gps icon color...")
    public boolean isGpsIconColorDisplayed(String geoType, String geoIconColor, String geoName) {
        WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsGeoIconColor.replace("{geoType}", geoType).replace("{gpsIconColor}", geoIconColor).replace("{name}", geoName)));
        return isElementVisible(gpsColor);
    }

    @Step("Verify removed geos icon visible...")
    public boolean isRemoveGeoIconDisplayed(String geoFencesLabelName, String geoName) {
        WebElement selectedGeosName = waitForElementToBeVisible(findElementWithFluentWait(By.xpath(xpathSelectedGeo.replace("{geoFencesLabelName}", geoFencesLabelName).replace("{name}", geoName))));
        hoverOverElement(selectedGeosName);
        DriverFactory.sleep(3000);
        return isElementVisible(removeSelectedGeos);
    }

    @Step("Verify that selected Allowed geos in lists do not appear in Restricted geos list...")
    public int getNumberOfSelectedGeosInList(String geoName, String geoFencesLabelName, int counter) {
        waitForPageLoaded();
        return waitForNumberOfElementsToBe(By.xpath(xpathSelectedGeo.replace("{geoFencesLabelName}", geoFencesLabelName).replace("{name}", geoName)), counter).size();
    }
}
