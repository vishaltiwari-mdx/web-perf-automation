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
package com.methodics.phi.pageobject.attributes;

import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.util.imagecomparison.ImageComparisonHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.util.List;

public class AttributesTabPage extends BasePage {
    private WebDriver driver;

    public AttributesTabPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[contains(@class,'card widget')]")
    private List<WebElement> attributes;

    @FindBy(xpath = "//div[contains(@class,'widget__')]")
    private List<WebElement> widgetAttributes;

    @FindBy(xpath = "//*[local-name() = 'g' and @class='c3-axis c3-axis-x']")
    private WebElement xAxis;

    @FindBy(xpath = "//*[local-name() = 'g' and @class='c3-axis c3-axis-y']")
    private WebElement yAxis;

    @FindBy(css = "div[class='card-text'] a")
    private List<WebElement> links;

    @FindBy(xpath = "//div[@class='tooltip-inner']")
    private WebElement tooltip;

    @FindBy(xpath = "//div[@class='ag-center-cols-container']//div[@role='row']")
    private List<WebElement> tableAttributeRow;

    @FindBy(xpath = "//div[@data-testid='footer-widget-hover-attribute-name']//span")
    private WebElement widgetAttributeName;

    @FindBy(xpath = "//a[contains(@class, 'widget-button')]")
    private WebElement buttonWidget;

    @FindBy(css = "div[data-testid='ui-card'] > div[data-testid='ui-tabs'] li[role='presentation']")
    private List<WebElement> subTabsList;

    @FindBy(xpath = "//div[@class='mermaid']/ancestor::div[@class='widget-html']")
    private WebElement mermaidClassDiagram;

    private String xpathAttribute = "//div[@class='card widget-card px-3 pb-3 w-100 h-100 pt-3 widget-card_locked widget-{attributeType} widget__{name}' or @class='card widget-card px-3 pb-3 w-100 h-100 widget-card_locked widget-{attributeType} widget__{name}']";
    private String linkRow = "//a[normalize-space()='{name}']";
    private String xpathTooltip = "//div[@class='tooltip-inner' and text()='{text}']";
    private String pathToWidgetElement = "//div[contains(@class, '{name}')]";
    private String tableWidgetValue = "//div[@col-id='{colIdName}']//div[contains(@data-cell-value, '{value}')]";
    private String xpathWidgetDownloadLinkWrongPathMessage = "//body";
    private String tab = "//a[@title='{name}']";
    private String iframeWidgetExcel = "//iframe[@id='embedOfficeFrame']";
    private String iframeWidgetMainAppExcelBody = "//body[@id='MainApp']";

    @Step("Get number of widget attributes...")
    public int getNumberOfAttributes() {
        return getNumberOfElements(attributes);
    }

    @Step("Get number of widget attributes...")
    public int getNumberOfWidgetAttributes() {
        return getNumberOfElements(widgetAttributes);
    }

    @Step("Verify attribute is present...")
    public boolean isAttributePresent(String attributeName, String name) {
        WebElement attrName = findElementWithWait(By.xpath(xpathAttribute.replace("{attributeType}", attributeName).replace("{name}", name)));
        return isElementVisible(attrName);
    }

    @Step("Verify that X axis of graph widget is present...")
    public boolean isHorizontalAxisPresent() {
        return isElementVisible(xAxis);
    }

    @Step("Verify that Y axis of graph widget is present...")
    public boolean isVerticalAxisPresent() {
        return isElementVisible(yAxis);
    }

    @Step("Get the number of links..")
    public int getNumberOfLinks() {
        return getNumberOfElements(links);
    }

    @Step("Hover over link row...")
    public void hoverOverLinkRow(String url) {
        WebElement link = findElementWithWait(By.xpath(linkRow.replace("{name}", url)));
        hoverOverElement(link);
    }

    @Step("Get tooltip text...")
    public String getLinkTooltip() {
        return getText(tooltip);
    }

    @Step("Wait for tooltip to disappear...")
    public void waitForTooltipToDisappear(String text) {
        waitForNumberOfElementsToBe(By.xpath(xpathTooltip.replace("{text}", text)), 0);
    }

    @Step("Click on link in Links widget...")
    public void clickOnLinkInLinksWidget(String url) {
        WebElement link = findElementWithWait(By.xpath(linkRow.replace("{name}", url)));
        click(link);
        switchToOpenedTab();
    }

    @Step("Getting the name of the widget attribute")
    public WebElement getWidgetElement(String attributeName) {
        return driver.findElement(By.xpath(pathToWidgetElement.replace("{name}", attributeName)));
    }

    @Step("Getting value from Table Widget")
    public String getTableWidgetValue(String colId, String tableWidgetFieldValue) {
        WebElement widgetTableValue = driver.findElement(By.xpath(tableWidgetValue.replace("{value}", tableWidgetFieldValue).replace("{colIdName}", colId)));
        return getText(widgetTableValue);
    }

    @Step("Clicking on link in Table widget...")
    public void clickOnLinkInTableWidget(String url) {
        WebElement link = findElementWithWait(By.xpath("//a[normalize-space()='{name}']".replace("{name}", url)));
        click(link);
        switchToOpenedTab();
    }

    @Step("Clicking on download link in widget...")
    public void clickOnDownloadingLinkInWidget(String url) {
        WebElement link = findElementWithWait(By.xpath("//a[normalize-space()='{name}']".replace("{name}", url)));
        click(link);
    }

    @Step("Verify on download link in widget is displayed...")
    public boolean isDownloadingLinkInWidget(String url) {
        WebElement link = findElementWithWait(By.xpath("//a[normalize-space()='{name}']".replace("{name}", url)));
        return isElementVisible(link);
    }

    @Step("Getting wrong widget link path message...")
    public String getTextFromWidgetWrongDownloadLinkMessage() {
        switchToOpenedTab();
        WebElement previewText = findElementWithWait(By.xpath(xpathWidgetDownloadLinkWrongPathMessage));
        return getText(previewText);
    }

    @Step("Verifying that all rows in the table attribute are present...")
    public int getNumberOfRowsInTableWidget() {
        return getNumberOfElements(tableAttributeRow);
    }

    @Step("Getting widget name from footer...")
    public String getAttributeName(String widgetName) {
        WebElement widget = findElementWithWait(By.xpath(pathToWidgetElement.replace("{name}", widgetName)));
        hoverOverElement(widget);
        return widgetAttributeName.getText().replace("Attribute name: ", "").trim();
    }

    @Step("Verifying widget attribute visible...")
    public int isAttributeVisible(String widgetName, int counter) {
        return findElementsWithWait(By.xpath(pathToWidgetElement.replace("{name}", widgetName)), counter).size();
    }

    @Step("Getting button widget text...")
    public String getButtonWidgetText() {
        return getText(buttonWidget);
    }

    @Step("Hover over button widget...")
    public void hoverOverWidgetButton() {
        hoverOverElement(buttonWidget);
    }

    @Step("Clicking on button widget...")
    public void clickOnButtonWidget() {
        click(buttonWidget);
    }

    @Step("Getting button widget attribute name in footer...")
    public String getButtonWidgetAttributeName() {
        hoverOverElement(buttonWidget);
        return widgetAttributeName.getText().replace("Attribute name: ", "").trim();
    }

    @Step("Create screenshot of button widget...")
    public void createButtonWidgetScreenshot(String filename) throws IOException {
        ImageComparisonHelper helper = new ImageComparisonHelper();
        helper.createScreenshotOfAnElement(buttonWidget, filename);
    }

    @Step("Compare screenshot of button widget...")
    public void compareButtonWidgetScreenshots(String filename) throws IOException {
        ImageComparisonHelper helper = new ImageComparisonHelper();
        helper.compareScreenshotElement(buttonWidget, filename);
    }

    @Step("Getting list of subtabs...")
    public int getNumberOfSubtabs() {
        return getNumberOfElements(subTabsList);
    }

    @Step("Clicking on tab...")
    public void clickOnSubTab(String tabName) {
        WebElement subtab = findElementWithWait(By.xpath(tab.replace("{name}", tabName)));
        click(subtab);
    }

    @Step("Clicking on subtab and getting the name...")
    public String clickOnSubTabAndGetAttributeName(String tabName, String attributeName) {
        clickOnSubTab(tabName);
        return getAttributeName(attributeName);
    }

    @Step("Verify that Iframe Excel is shown... ")
    public boolean verifyIframeExcelIsPresent() {
        driver.switchTo().frame(0);
        WebElement excel = findElementWithWait(By.xpath(iframeWidgetExcel));
        boolean presentExcel = isElementVisible(excel);
        driver.switchTo().frame(0);
        WebElement excelBody = findElementWithWait(By.xpath(iframeWidgetMainAppExcelBody));
        boolean presentExcelBody = isElementVisible(excelBody);
        driver.switchTo().defaultContent();
        return presentExcel == presentExcelBody;
    }

    @Step("Verify that Mermaid class diagram is shown... ")
    public boolean verifyMermaidClassDiagramIsPresent() {
        return isElementVisible(mermaidClassDiagram);
    }
}
