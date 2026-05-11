/**
 * ################################################################################
 * # Copyright (c) 2010-2025 Methodics, Inc.
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

import com.methodics.phi.pageobject.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class TitleDashboardPage extends BasePage {
    private WebDriver driver;
    public TitleDashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[@type='button']//span[normalize-space()='IP title']")
    private WebElement titleCollapseExpandButton;

    @FindBy(xpath = "//i[not(contains(@class,'rotate'))]/ancestor::div/span[normalize-space()='IP title']")
    private WebElement titleCollapseExpandButtonExpanded;

    @FindBy(xpath = "//i[contains(@class,'rotate')]/ancestor::div/span[normalize-space()='IP title']")
    private WebElement titleCollapseExpandButtonCollapsed;

    @FindBy(xpath = "//div[contains(@class,'title-dashboard')]")
    private WebElement titleDashboard;

    @FindBy(xpath = "//div[contains(@class,'title-dashboard')]//div[contains(@class,'widget__')]")
    private List<WebElement> titleWidgetAttributes;

    public boolean verifyThatTitleCollapseExpandButtonIsDisplayed() {
        return isElementVisible(titleCollapseExpandButton);
    }

    public boolean verifyThatTitleCollapseExpandButtonIsNotDisplayed() {
        return isElementNotVisible(titleCollapseExpandButton);
    }

    public void clickTitleCollapseExpandButton() {
        click(titleCollapseExpandButton);
    }

    public boolean verifyThatTitleCollapseExpandButtonIsExpanded() {
        return isElementVisible(titleCollapseExpandButtonExpanded);
    }
    public boolean verifyThatTitleCollapseExpandButtonIsCollapsed() {
        return isElementVisible(titleCollapseExpandButtonCollapsed);
    }

    public boolean verifyThatTitleDashboardIsDisplayed() {
        return isElementVisible(titleDashboard);
    }
    public boolean verifyThatTitleDashboardIsNotDisplayed() {
        return isElementNotVisible(titleDashboard);
    }
}
