/**
 * ################################################################################
 * # Copyright (c) 2010-2023 Methodics, Inc.
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ParentsTabPage extends BasePage {
    private WebDriver driver;

    public ParentsTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@class='ms-2 text-truncate']")
    private List<WebElement> parents;

    private String parentIpLine = "//div[@title='{ip}']/following::div[contains(@class,'ag-cell-value')]/span[@title='{line}']";
    private String goToParentIpBtn = "//span[@title='{line}']/parent::*/preceding-sibling::*//*[@title='{ip}']/following-sibling::a";

    @Step("Get the number of parents...")
    public int getNumberOfParents() {
        DriverFactory.sleep(1000);
        return getNumberOfElements(parents);
    }

    @Step("Verify parent IPL is displayed in Parents tab...")
    public boolean isParentIpDisplayed(String ip, String line) {
        WebElement parentIpl = findElementWithFluentWait(By.xpath(parentIpLine.replace("{ip}", ip)
                .replace("{line}", line)));
        return isElementVisible(parentIpl);
    }

    @Step("Go to parent IPL...")
    public IpDetailsPage goToParentIp(String ip, String line) {
        WebElement goToIpButton = findElementWithFluentWait(By.xpath(goToParentIpBtn.replace("{ip}", ip)
                .replace("{line}", line)));
        click(goToIpButton);
        return new IpDetailsPage(driver);
    }

    @Step("Hover over parent Go to IP icon...")
    public void hoverParentGoToIpButton(String ip, String line) {
        WebElement goToIpButton = findElementWithFluentWait(By.xpath(goToParentIpBtn.replace("{ip}", ip)
                .replace("{line}", line)));
        hoverOverElement(goToIpButton);
    }
}
