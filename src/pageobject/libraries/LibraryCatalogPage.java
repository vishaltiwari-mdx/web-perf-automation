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

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.awt.AWTException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibraryCatalogPage extends BasePage {
    private final WebDriver driver;

    public LibraryCatalogPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "div.app-subheader .page-name")
    private WebElement libraryCatalogSubHeader;

    @FindBy(css = "div.library__item-name.text-truncate")
    private WebElement libraryNameElement;

    @FindBy(xpath = "//input[@data-input='libraries']")
    private WebElement librarySearchInput;

    @FindBy(css = "i.library__item-icon")
    private List<WebElement> libraryIconElements;

    @FindBy(css = "button.libraries-catalog__label-list__item span[data-testid]")
    private List<WebElement> labelRows;

    @FindBy(xpath = "//div[@data-libraries-labels='{labelName}']")
    private WebElement labelNameDynamic;

    @FindBy(xpath = "//span[@data-label-badge='{labelName}']")
    private WebElement labelBadgeDynamic;

    @FindBy(xpath = "//input[@data-input='labels']")
    private WebElement labelSearchInput;

    @FindBy(css = "i[data-icon-search='labels'].fa-magnifying-glass")
    private WebElement labelSearchButton;

    @FindBy(css = "i[data-icon-clear='labels']")
    private WebElement clearLabelSearchButton;

    @FindBy(css = "a.library__item")
    private List<WebElement> libraryRows;

    @FindBy(xpath = "//div[@data-name='{libraryName}']")
    private WebElement libraryNameDynamic;

    @FindBy(xpath = "//span[@data-ips-count='{libraryName}']")
    private WebElement libraryBadgeDynamic;


    @FindBy(css = "i[data-icon-search='libraries'].fa-magnifying-glass")
    private WebElement librarySearchButton;

    @FindBy(css = "i[data-icon-clear='libraries']")
    private WebElement clearLibrarySearchButton;

    @FindBy(css = "div.tooltip-inner")
    private WebElement attachedLibLabelTooltip;

    @FindBy(css = "div.libraries-catalog__labels")
    private WebElement labelsSidebar;

    @FindBy(css = "i[data-icon-clear='labels']")
    private WebElement clearLabelsSearchButton;

    @FindBy(css = "i[data-icon-clear='libraries']")
    private WebElement clearLibrariesSearchButton;

    @FindBy(css = "i[data-icon-search='labels'].fa-magnifying-glass")
    private WebElement labelsSearchButton;

    @FindBy(css = "i[data-icon-search='libraries'].fa-magnifying-glass")
    private WebElement librariesSearchButton;

    @FindBy(xpath = "//div[contains(@class,'text-center') and contains(.,'No Libraries found.')]")
    private WebElement noLibrariesMessage;

    private final String searchLabelsInputField = "//input[@data-input='labels']";
    private final String allLibrariesButton = "//div[@data-all-libraries-labels]";
    private final String libraryIcon = "//div[contains(text(), \"{libraryName}\")]";
    private final String labelRow = "//button[contains(@class, 'label-list')]//div[contains(@class, 'truncate')][normalize-space()='{labelName}']";
    private final String labelIcon = "//span[@data-label-badge =\"{labelName}\"]";
    private final String customIcon = "//i[contains(@class,'{icon}')]/following-sibling::div[@data-name='{name}']";
    private final String numberOfIpsInLibrary = "//span[@data-ips-count='{libraryName}']";

    @Step("Clicking on the library icon...")
    public LibraryDetailsPage clickOnLibraryCard(String libName) {
        final WebElement libraryName = waitForElementToBeVisible(driver.findElement(By.xpath(libraryIcon.replace("{libraryName}", libName))));
        click(libraryName);
        return new LibraryDetailsPage(driver);
    }

    @Step("Entering library name to the input field...")
    public void enterLibraryNameToSearchLibrariesInputField(String libraryName) {
        enterTextSlowly(librarySearchInput, libraryName);
        waitForPageLoaded();
    }

    @Step("Clear library name from the input field...")
    public void clearLibraryNameFromLibrariesInputField() {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", clearLibrariesSearchButton);
    }

    @Step("Entering label name to the input field...")
    public void enterLabelNameToSearchLibrariesInputField(String labelName) {
        final WebElement inputField = findElementWithWait(By.xpath(searchLabelsInputField));
        enterTextSlowly(inputField, labelName);
    }

    @Step("Clear label name from the input field...")
    public void clearLabelNameFromLibrariesInputField() {
        clearLabelsSearchButton.click();
    }

    @Step("Getting number of Libraries...")
    public int getNumberOfLibrariesIcons() {
        return getNumberOfVisibleElements(libraryIconElements);
    }

    @Step("Get Library name from the Icon...")
    public String getNameOfTheLibrary() {
        waitTillVisibleWithFluentWait(libraryNameElement);
        return getText(libraryNameElement);
    }

    @Step("Get Label name attribute value...")
    public String getLabelNameTooltip(String label) {
        final WebElement labelName = findElementWithWait(By.xpath(labelRow.replace("{labelName}", label)));
        return getAttributeValue(labelName, "title");
    }

    @Step("Getting number of Ips from Library icon...")
    public String getNumberOfIpsInLibrary(String identifier) {
        final WebElement ipsCountIcon = waitTillVisibleWithFluentWait(findElementWithWait(By.xpath(numberOfIpsInLibrary.replace("{libraryName}", identifier))));
        return getText(ipsCountIcon);
    }

    @Step("Clicking on the label row...")
    public void clickOnLabelRow(String label) {
        final WebElement labelName = findElementWithWait(By.xpath(labelRow.replace("{labelName}", label)));
        click(labelName);
    }

    @Step("Getting number of Labels...")
    public int getNumberOfLabels() {
        return labelRows.size();
    }

    @Step("Verifying library search button is present...")
    public boolean isLibrariesSearchButtonPresent() {
        return isElementVisible(librariesSearchButton);
    }

    @Step("Verifying label search button is present...")
    public boolean isLabelSearchButtonPresent() {
        return isElementVisible(labelsSearchButton);
    }

    @Step("Getting number of Libraries from the Label icon...")
    public String getNumberOfLibsFromLabelIcon(String labelName) {
        final WebElement icon = findElementWithWait(By.xpath(labelIcon.replace("{labelName}", labelName)));
        return getText(icon);
    }

    @Step("Get tooltip text...")
    public String getTooltipText() {
        return getText(attachedLibLabelTooltip);
    }

    @Step("Clicking on All Libraries button...")
    public void clickOnAllLibrariesButton() {
        final WebElement allLibraries = findElementWithWait(By.xpath(allLibrariesButton));
        click(allLibraries);
    }

    @Step("Go to Library Catalog page...")
    public void goToLibraryCatalogPage(String url) {
        goTo(url);
    }

    @Step("Paste text from clipboard to the search Libraries input field..")
    public void pasteFromClipboardToSearchLibField() {
        waitForElementToBeVisible(librarySearchInput).click();
        pasteFromClipboard(librarySearchInput);
        waitForPageLoaded();
    }

    @Step("Verifying Library Catalog sub-header is displayed...")
    public boolean isLibraryCatalogSubHeaderPresent() {
        return isElementVisible(libraryCatalogSubHeader);
    }

    @Step("Verify 'No Libraries' message is present...")
    public boolean isNoLibrariesMessagePresent() {
        return isElementVisible(noLibrariesMessage);
    }

    @Step("Hover over libraries count badge...")
    public void hoverLibrariesCount(String labelName) {
        final WebElement icon = findElementWithWait(By.xpath(labelIcon.replace("{labelName}", labelName)));
        hoverOverElement(icon);
    }

    @Step("Verify label is not present...")
    public boolean noLabelPresent(String label) {
        waitForNumberOfElementsToBe(By.xpath(labelRow.replace("{labelName}", label)), 0);
        return true;
    }

    @Step("Verify custom library icon is present...")
    public boolean isLibraryIconPresent(String libName, String icon) {
        final WebElement libIcon = findElementWithWait(By.xpath(customIcon.replace("{name}", libName)
                .replace("{icon}", icon)));
        return isElementVisible(libIcon);
    }

    @Step("Verify that labels sidebar is present...")
    public boolean isLabelsSidebarPresent() {
        return isElementVisible(labelsSidebar);
    }

    @Step("Open Library Catalog page IP count link in a new tab..")
    public void openLibraryCatalogCountIpLinkInNewTab(String identifier) throws AWTException {
        final WebElement ipsInLibraryCount = findElementWithWait(By.xpath(numberOfIpsInLibrary.replace("{libraryName}", identifier)));
        openNewTabBrowserContextMenu(ipsInLibraryCount);
        switchToOpenedTab();
        // Opened tab needs time to load.
        DriverFactory.sleep(1000);
    }
}
