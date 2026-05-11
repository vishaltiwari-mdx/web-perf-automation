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
import com.methodics.phi.pageobject.GridTablePage;
import static com.methodics.phi.util.CommonUrls.CREATE_IP_PAGE;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ShoppingCartResourcesTable extends BasePage {

    private final WebDriver driver;

    @FindBy(xpath = "//div[@class='ag-body-horizontal-scroll-viewport']")
    private WebElement horizontalScroll;

    @FindBy(xpath = "//div[contains(@class,'ag-horizontal-right-spacer')]")
    private WebElement scrollRightCorner;

    @FindBy(css = "[data-testid='ui-btn-ui-modal-remove']")
    private WebElement removeButton;

    @FindBy(css = "[data-testid='ui-btn-ui-modal-cancel']")
    private WebElement cancelButton;

    @FindBy(css = "[data-testid=ui-modal] .modal-body")
    private WebElement removeIpvModelBody;

    @FindBy(css = "[data-testid=ui-modal] h5")
    private WebElement removeIpvModelHeader;

    @FindBy(xpath = "//*[@data-testid='shopping-cart-filter-bar']//input")
    private WebElement searchIpvInput;

    @FindBy(xpath = "//h5[normalize-space()='No results found']")
    private WebElement noResultsFound;

    @FindBy(xpath = "//*[@data-testid='shopping-cart-grid']//div[@col-id='ip_version' and not(@role='columnheader')]//span[contains(@class, 'cell-text')]")
    private List<WebElement> ipvList;

    private final String agGridLastColumnCssPath = ".ag-header-cell.ag-column-last";

    private static final String TABLE_ID = "//*[@data-testid='shopping-cart-container']";
    private final String columnHeaders = TABLE_ID + "//*[@role='columnheader']";
    private final String columnHeadersByColId = columnHeaders + "[@col-id='{name}']";
    private static final String ROW_ID = "//*[@row-id='{fqn}']";
    private final String ipvThreeDotOptions = ROW_ID + "//*[@data-testid='col-sub-menu-btn-container']";
    private final String RemoveIpvOption = ROW_ID + "//*[@data-testid='ui-btn-remove-from-sc-btn']";
    private final String goToLibrary = ROW_ID + "//*[@data-testid='ui-btn-go-to-library-btn']";
    private final String removeIpvModelText = "//span[text()='{text}']";
    private final String columnName = "//div[@class='ag-header-cell-label']/span[normalize-space()='{name}']";
    private final String goToIpvLink = "//*[@data-testid='ipv-name']/*[contains(text(),'{fqn}')]";
    private final String columnNameByColId = "//*[@data-testid='shopping-cart-grid']//*[@role='columnheader'][@col-id='{name}']";
    private final String ipNameHyperlink = TABLE_ID + "//a[.='{fqn}']";

    public ShoppingCartResourcesTable(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Get IPV names in display order from shopping cart table")
    public List<String> getIpvNames() {
        waitForPageLoaded();
        final List<String> listIp = new ArrayList<>(ipvList.size());
        for (final WebElement ipv : ipvList) {
            listIp.add(ipv.getText().replace("\n", " ").replace("\r", " ").trim());
        }
        return listIp;
    }

    public Set<String> getAllColumnNameList() {
        logger.info("Scrolling horizontally to get all column names...");
        Set<String> uniqueColumns = new TreeSet<>();
        boolean lastColumnVisible = true;
        do {
            List<String> initialScroll = getTextsForElements(By.xpath(columnHeaders));
            if (isLastAgGridRowColumnDisplay(agGridLastColumnCssPath)) {
                lastColumnVisible = false;
            } else {
                try {
                    dragAndDropElement(horizontalScroll, scrollRightCorner);
                } catch (Exception e) {
                    logger.error("horizontally object not found: " + e.getMessage());
                    break;
                }
                DriverFactory.sleep(1000);
            }
            uniqueColumns.addAll(initialScroll);
        } while (lastColumnVisible);
        scrollToElement(DriverFactory.getBrowserInstance().findElement(By.cssSelector(agGridLastColumnCssPath)));
        uniqueColumns.addAll(getTextsForElements(By.xpath(columnHeaders)));
        uniqueColumns.remove("");
        logger.info("Actual column list: " + uniqueColumns);
        return uniqueColumns;
    }

    @Step("Verify column is displayed...")
    public boolean isColumnHeaderPresent(String name) {
        try {
            logger.info("Checking if column is displayed: {}", name);
            WebElement column = waitForElementToBePresent(By.xpath(columnHeadersByColId.replace("{name}", name)));
            scrollToElement(column);
            return isElementVisible(column);
        } catch (WebDriverException e) {
            logger.info(name + " column is not visible...");
            return false;
        }
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameDisplayed(String nameColumn) {
        final WebElement columnNameHeader = findElementWithWait(By.xpath(columnName.replace("{name}", nameColumn)));
        return isElementVisible(columnNameHeader);
    }

    @Step("Verify that column name display...")
    public boolean isColumnNameNotDisplayed(String nameColumn) {
        return isElementNotVisible(columnName.replace("{name}", nameColumn));
    }

    @Step("Click on the Remove button")
    public void clickOnRemoveButton() {
        click(waitTillClickableWithFluentWait(removeButton));
    }

    @Step("Click on the Cancel button")
    public void clickOnCancelButton() {
        waitTillClickableWithFluentWait(cancelButton);
        click(cancelButton);
    }

    @Step("Click on the three dots options for Specific IPV: {ipvFqn}")
    public void clickOnIpvThreeDotOptions(String ipvFqn) {
        WebElement threeDotOptionsElement = findElementWithFluentWait(By.xpath(ipvThreeDotOptions.replace("{fqn}", ipvFqn)));
        click(threeDotOptionsElement);
    }

    @Step("Remove IPV from Shopping Cart: {fqn}")
    public void removeIpvFromShoppingCart(String fqn) {
        GridTablePage gridTablePage = new GridTablePage(driver);
        gridTablePage.waitForDataToLoad();
        clickOnIpvThreeDotOptions(fqn);
        WebElement removeButtonElement = findElementWithWait(By.xpath(RemoveIpvOption.replace("{fqn}", fqn)));
        click(removeButtonElement);
        clickOnRemoveButton();
    }

    @Step("open confirmation remove modal for IPV: {fqn}")
    public void openConfirmationRemoveModal(String fqn) {
        clickOnIpvThreeDotOptions(fqn);
        WebElement removeButtonElement = findElementWithWait(By.xpath(RemoveIpvOption.replace("{fqn}", fqn)));
        click(removeButtonElement);
    }

    public void deleteAllIPVs(List<String> ipvFqns) {
        for (String fqn : ipvFqns) {
            removeIpvFromShoppingCart(fqn);
        }
    }

    @Step("Verify and return text of removeIpvModel body text element")
    public String getRemoveIpvModelHeader() {
        waitForElementToBeVisible(removeIpvModelHeader);
        return removeIpvModelHeader.getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("Verify and return text of removeIpvModel body text element")
    public String getRemoveIpvModelBodyText() {
        return removeIpvModelBody.getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("Close Library tab and switch to previous Shopping Cart tab")
    public void closeLibraryWindowAndSwitchToShoppingCart() {
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        driver.close();
        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }

    @Step("Check if remove IPV model is visible")
    public boolean isRemoveIpvModelVisible() {
        try {
            removeIpvModelHeader.isDisplayed();
            return isElementVisible(removeIpvModelHeader);
        } catch (TimeoutException e) {
            logger.info("Remove IPV model is not visible");
            return false;
        }
    }

    @Step("Input string '{input}' into the search IPV input field")
    public void inputStringInSearchIpvInput(String input) {
        waitForElementToBeVisible(searchIpvInput);
        searchIpvInput.clear();
        waitForPageLoaded();
        searchIpvInput.sendKeys(input);
        DriverFactory.sleep(1000);//Need for improvement
    }

    @Step("Check if 'No results found' message is displayed")
    public boolean isNoResultsFoundDisplayed() {
        waitForPageLoaded();
        return isElementVisible(noResultsFound);
    }

    @Step("Verify column is displayed...")
    public void isColumnPresent(String name, String url) {
        try {
            logger.info("Checking if column is displayed: {}", name);
            if (url.equals(CREATE_IP_PAGE)) {
                WebElement column = waitForElementToBePresent(By.xpath(columnHeadersByColId.replace("{name}", name)));
                scrollToElement(column);
            } else {
                WebElement column = waitForElementToBePresent(By.xpath(columnNameByColId.replace("{name}", name)));
                scrollToElement(column);
            }
            Actions action = new Actions(DriverFactory.getBrowserInstance());
            action.sendKeys(Keys.ARROW_RIGHT).perform();
        } catch (WebDriverException e) {
            logger.info(name + " column is not visible...");
        }
    }

    @Step("Verify if IPV element is present for: {fqn}")
    public boolean isRowIdElementPresent(String fqn) {
        String xpath = ROW_ID.replace("{fqn}", fqn);
        try {
            WebElement rowElement = findElementWithFluentWait(By.xpath(xpath));
            return isElementVisible(rowElement);
        } catch (TimeoutException e) {
            logger.info("ROW_ID element not found for: " + fqn);
            return false;
        }
    }

    @Step("Verify if ROW_ID element is not present for: {fqn}")
    public boolean isRowIdElementNotPresent(String fqn) {
        String xpath = ROW_ID.replace("{fqn}", fqn);
        try {
            driver.findElement(By.xpath(xpath));
            logger.info("IPV found : " + fqn);
            return false;
        } catch (NoSuchElementException e) {
            logger.info("IPV not found : " + fqn);
            return true;
        }
    }

    @Step("Click on the IP hyperlink for: {fullFqn}")
    public void clickOnIpHyperlink(String fullFqn) {
        WebElement ipHyperlink = driver.findElement(By.xpath(ipNameHyperlink.replace("{fqn}", fullFqn)));
        ipHyperlink.click();
    }
    @Step("Verify remove IPV modal is not visible after clicking Cancel")
    public boolean isRemoveIpvModelNotVisible() {
        try {
            return !isElementVisible(removeIpvModelHeader);
        } catch (NoSuchElementException e) {
            logger.info("Remove IPV model is not present in DOM");
            return true;
        } catch (TimeoutException e) {
            logger.info("Remove IPV model is not visible");
            return true;
        }
    }
}
