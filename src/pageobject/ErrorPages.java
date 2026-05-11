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
package com.methodics.phi.pageobject;

import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ErrorPages extends BasePage {
    private WebDriver driver;

    public ErrorPages(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class, 'app-subheader')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//h1[contains(@class, 'error')]")
    private WebElement statusCode;

    @FindBy(xpath = "//span[normalize-space()='Go to IP catalog']")
    private WebElement goToCatalogButton;

    @FindBy(xpath = "//span[normalize-space()='Go back']")
    private WebElement goBackButton;

    @Step("Get error message...")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Get status code...")
    public String getStatusCode() {
        return statusCode.getText();
    }

    @Step("Click on Go to Catalog button...")
    public IpCatalogPage goToIpCatalog() {
        click(goToCatalogButton);
        return new IpCatalogPage(driver);
    }

    @Step("Click on Go Back button...")
    public void clickOnGoBackButton() {
        click(goBackButton);
    }

    @Step("Verify Go to Catalog button is present...")
    public boolean isGoToCatalogButtonPresent() {
        return isElementVisible(goToCatalogButton);
    }

    @Step("Verify Go Back button is present..")
    public boolean isGoBackButtonPresent() {
        return isElementVisible(goBackButton);
    }
}
