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
package com.methodics.phi.pageobject.footer;

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.stream.Collectors;

@FindBy(css = ".navbar")
public class FooterPage extends BasePage {
    private WebDriver driver;

    public FooterPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class, 'app-footer')]")
    private WebElement footer;

    @FindBy(xpath = "//div[contains(@class, 'app-footer')]//div[@class='text-right col']")
    private WebElement attributeHoveredText;

    @Step("Get copyright text...")
    public String getFooterCopyright() {
        return footer.getText()
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z]{2,})([A-Z][a-z])", "$1 $2")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Step("Get attribute hovered text...")
    public String getAttributeHoveredText() {
        return getText(attributeHoveredText);
    }
}
