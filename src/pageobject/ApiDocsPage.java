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

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ApiDocsPage extends BasePage {
    private WebDriver driver;
    private static final String API_DOCS_TITLE = "Pi Server API";

    public ApiDocsPage(WebDriver driver) {
        this.driver = driver;

        waitForTitle(API_DOCS_TITLE);
        if (!driver.getTitle().contains("Pi Server API")) {
            throw new IllegalStateException("This is not a API Docs Page, current page is: " + driver.getTitle());
        }
    }

    private String publicDocsTitle = "//h1[contains(.,'Pi Server API')]";

    @Step("Verifying Public API Documentation title present...")
    public boolean isPublicDocsTitlePresent() {
        WebElement element = findElementWithWait(By.xpath(publicDocsTitle));
        return isElementVisible(element);
    }
}
