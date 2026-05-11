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

import com.methodics.phi.common.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadDocumentPage extends BasePage {

    public UploadDocumentPage(WebDriver driver) {
        this.driver = driver;
    }

    WebDriver driver = DriverFactory.getBrowserInstance();


    @FindBy(xpath = "//form[@id='upload-form']//input[@id='file-upload-control']")
    private WebElement uploadDocumentButton;

    public boolean isUploadButtonPresent() {
        return isElementVisible(uploadDocumentButton);
    }

    /**
     * Uploads a file.
     */
    public void uploadDocument(String documentName) {
        Path absolutePathToFile = Paths.get(System.getProperty("user.dir"), "src/test/resources/files", documentName);
        DriverFactory.sleep(1000);
        // we need to use JS here to make the upload button visible
        String baseJSPart = "document.getElementById('upload-form').children[1].children[0].";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(baseJSPart + "style.display = 'block !important;'");
//		js.executeScript(baseJSPart + "style.opacity = 100;");
//		js.executeScript(baseJSPart + "style.width = '100px';");
//		js.executeScript(baseJSPart + "style.height = '20px';");

        // in sendKeys you have to specify the absolute path of the content you want to upload
        uploadDocumentButton.sendKeys(absolutePathToFile.toString());
    }
}
