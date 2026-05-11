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

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UsageTabPage extends BasePage {
    private WebDriver driver;

    public UsageTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[@class='ag-group-contracted']//span[contains(@class,'ag-icon-tree-closed')]")
    private WebElement expandUsageTreeButton;

    @FindBy(xpath = "//span[@class='ag-group-expanded']//span[contains(@class,'ag-icon-tree-open')]")
    private WebElement collapseUsageTreeButton;

    @FindBy(xpath = "//div[normalize-space()='Show immediate parents only']//span[@class='toggle__switch']")
    private WebElement parentsToggleButton;

    @FindBy(xpath = "//input[@placeholder='Search IPVs']")
    private WebElement searchIPVs;

    @FindBy(xpath = "//i[contains(@class, 'circle-xmark')]")
    private WebElement clearSearchButton;

    @FindBy(xpath = "//div[@class='ipv-cell__fqn text-truncate'][@title]")
    private List<WebElement> ipvRows;

    private String resourceIpv = "//div[contains(@class, 'highlighted')]//div[contains(@class, 'truncate')][normalize-space()='{ipv}'][@title='{ipv}']";
    private String parentIpv = "//div[@row-index='{index}'][contains(@class, 'expanded')]//div[contains(@class, 'truncate')][normalize-space()='{ipv}'][@title='{ipv}']";
    private String parentIpvNoIndx = "//div[contains(@class, 'expanded')]//div[contains(@class, 'truncate')][normalize-space()='{ipv}'][@title='{ipv}']";
    private String goToIpvBtn = "//div[normalize-space()='{ipv}']/following-sibling::a";

    @Step("Click on Expand Usage tree caret...")
    public void expandTreeNode() {
        click(expandUsageTreeButton);
    }

    @Step("Click on Collapse Usage tree caret...")
    public void collapseTreeNode() {
        int rowsBefore = driver.findElements(By.xpath(ipvRows.toString())).size();
        click(collapseUsageTreeButton);
        initWait(5).until(d -> d.findElements(By.xpath(ipvRows.toString())).size() < rowsBefore);
    }

    @Step("Click on Parents toggle button...")
    public void showImmediateParents() {
        click(parentsToggleButton);
    }

    @Step("Search for IPV in Usage tab...")
    public void searchIPVsUsageTab(String ipvName) {
        inputText(searchIPVs, ipvName);
    }

    @Step("Clear search field by clicking on clear button...")
    public void clearSearchField() {
        hoverOverElement(searchIPVs);
        click(clearSearchButton);
    }

    @Step("Get number of IPV rows...")
    public int getNumberOfIpvRows() {
        return getNumberOfVisibleElements(ipvRows);
    }

    @Step("Verify resource IPV is displayed in Usage table...")
    public boolean isResourceIpvDisplayed(String ipv) {
        WebElement resource = findElementWithWait(By.xpath(resourceIpv.replace("{ipv}", ipv)));
        return isElementVisible(resource);
    }

    @Step("Verify parent IPV is displayed in Usage table...")
    public boolean isParentIpvDisplayed(String ipIdentifier, String indexNumber) {
        waitForPageLoaded();
        return isElementVisible(findElementWithFluentWait(By.xpath(parentIpv.replace("{index}", indexNumber).replace("{ipv}", ipIdentifier))));
    }

    @Step("Verify parent IPV is displayed without specify of index in Usage table...")
    public boolean isParentIpvDisplayedNoIndx(String ipIdentifier) {
        return isElementVisible(findElementWithWait(By.xpath(parentIpvNoIndx.replace("{ipv}", ipIdentifier))));
    }

    @Step("Verify parent IPV is not displayed in Usage table...")
    public int parentIpvNotDisplayed(String ipIdentifier, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(parentIpv.replace("{index}", "0").replace("{ipv}", ipIdentifier)), counter).size();
    }

    @Step("Go to parent IPV...")
    public IpDetailsPage goToParentIpv(String ipv) {
        click(findElementWithWait(By.xpath(goToIpvBtn.replace("{ipv}", ipv))));
        return new IpDetailsPage(driver);
    }

    @Step("Verify parent IPV is displayed in Usage table without row indexing...")
    public boolean isParentIpvDisplayedNoIndex(String ipIdentifier) {
        waitForPageLoaded();
        return isElementVisible(findElementWithFluentWait(By.xpath(parentIpvNoIndx.replace("{ipv}", ipIdentifier))));
    }
}
