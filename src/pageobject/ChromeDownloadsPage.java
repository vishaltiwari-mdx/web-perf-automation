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
package com.methodics.phi.pageobject;

import com.methodics.phi.common.DriverFactory;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

public class ChromeDownloadsPage extends BasePage {
    public List<String> getListOfDownloadedFiles() {
        WebDriver driver = DriverFactory.getBrowserInstance();

        DriverFactory.sleep(1000);
        WebElement manager = driver.findElement(By.cssSelector("downloads-manager"));
        SearchContext managerShadow = (SearchContext) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot;", manager);

        System.out.println(managerShadow);
        DriverFactory.sleep(1000);

        WebElement downloadsItem = managerShadow.findElement(By.cssSelector("#downloadsList downloads-item"));
        SearchContext downloadsItemShadow = (SearchContext) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot;", downloadsItem);

        List<String> downloadedFiles = new ArrayList<>();
        waitForElementsToBeVisible(downloadsItemShadow.findElements(By.cssSelector("#file-link")))
                .forEach(e -> downloadedFiles.add(e.getText()));
        return downloadedFiles;
    }
}
