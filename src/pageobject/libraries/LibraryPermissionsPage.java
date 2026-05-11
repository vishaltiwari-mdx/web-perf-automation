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
package com.methodics.phi.pageobject.libraries;

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibraryPermissionsPage extends BasePage {
    private WebDriver driver;

    public LibraryPermissionsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Main locators using data-testid
    private String userNameOnPermissionCard = "//*[@data-testid='ui-list-group-item']//div[contains(@class,'user-permission__item__grant') and @title='{name}']";
    private String permissionCard = "//li[@data-testid='ui-list-group-item']";

    // Permission badge locators using data-badge attributes
    private String userReadPermissionsSelectedIpv = "//span[@data-badge-owner-info='{name}' and contains(@class,'user-permission__item__badge-read') and text()='R']";
    private String userReadPermissionsNotSelectedIpv = "//span[@data-badge-owner-light='{name}' and contains(@class,'user-permission__item__badge-read') and text()='R']";
    private String userWritePermissionsSelectedIpv = "//span[@data-badge-owner-info='{name}' and contains(@class,'user-permission__item__badge-write') and text()='W']";
    private String userWritePermissionsNotSelectedIpv = "//span[@data-badge-owner-light='{name}' and contains(@class,'user-permission__item__badge-write') and text()='W']";
    private String userOwnerPermissionsSelectedIpv = "//span[@data-badge-owner-info='{name}' and contains(@class,'user-permission__item__badge-owner') and text()='O']";
    private String userOwnerPermissionsNotSelectedIpv = "//span[@data-badge-owner-light='{name}' and contains(@class,'user-permission__item__badge-owner') and text()='O']";

    @FindBy(xpath = "//div[@data-icon-group]")
    private WebElement groupPermissionsIcon;

    @FindBy(xpath = "//div[@data-icon-user]")
    private WebElement userPermissionsIcon;

    @Step("Verifying user permission icon is present...")
    public boolean isUserPermissionIconPresent() {
        return isElementVisible(userPermissionsIcon);
    }

    @Step("Verifying group permission icon is present...")
    public boolean isGroupPermissionIconPresent() {
        return isElementVisible(groupPermissionsIcon);
    }

    @Step("Get number of the permission card...")
    public int getNumberOfPermissionCards() {
        waitForElementsToBeVisible(driver.findElements(By.xpath(permissionCard)));
        return getNumberOfVisibleElements(driver.findElements(By.xpath(permissionCard)));
    }

    @Step("Is read permissions enabled...")
    public boolean isReadPermissionEnabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userReadPermissionsSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Is read permissions disabled...")
    public boolean isReadPermissionDisabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userReadPermissionsNotSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Is write permissions enabled...")
    public boolean isWritePermissionEnabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userWritePermissionsSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Is write permissions disabled...")
    public boolean isWritePermissionDisabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userWritePermissionsNotSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Is owner permissions enabled...")
    public boolean isOwnerPermissionEnabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userOwnerPermissionsSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Is owner permissions disabled...")
    public boolean isOwnerPermissionDisabled(String name) {
        WebElement permission = waitForElementToBeVisible(driver.findElement(By.xpath(userOwnerPermissionsNotSelectedIpv.replace("{name}", name))));
        return isElementVisible(permission);
    }

    @Step("Verifying the name is displayed on permission card...")
    public boolean isNameOnPermissionCardPresent(String name) {
        WebElement username = waitForElementToBeVisible(driver.findElement(By.xpath(userNameOnPermissionCard.replace("{name}", name))));
        return isElementVisible(username);
    }

    @Step("Get user/group name tooltip...")
    public String getNameTooltip(String name) {
        WebElement username = waitForElementToBeVisible(driver.findElement(By.xpath(userNameOnPermissionCard.replace("{name}", name))));
        return getAttributeValue(username, "title");
    }
}