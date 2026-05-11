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
package com.methodics.phi.pageobject.dashboard;

import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardHistoryPage extends BasePage {
    private WebDriver driver;

    public DashboardHistoryPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[normalize-space()='Show more']")
    private WebElement showMoreButton;

    @FindBy(xpath = "//div[contains(@class, 'history-list__empty')]")
    private WebElement emptyHistory;

    @FindBy(xpath = "(//div[contains(@class,'collapse')]//li[@data-testid='ui-list-group-item'])[1]")
    private WebElement firstItem;

    @FindBy(css = ".history-list__toggle i.fa-angle-up")
    private WebElement caretDownIcon;

    @FindBy(css = ".history-list__toggle i.rotate")
    private WebElement caretUpIcon;

    @FindBy(css = ".history-list__toggle__date")
    private WebElement xpathDate;

    private String xpathItem = "//span[@data-history-item-name='{name}']";
    private String xpathTypeIcon = "//i[@data-history-item-name='{name}']";
    private String xpathItemsCount = "//span[contains(@class,'items-amount')]";
    private String xpathHistoryStarIcon = "//i[@data-history-item-star='{name}']";
    private String itemsXpath = "//div[contains(@class,'show')]//span[@data-history-item-name]";
    private String collapsedItem = "//div[@class='collapse']//span[@data-history-item-name]";
    private String xpathAddToShoppingCartButtonSpecificItem = "//i[@data-history-item-sc='{name}']";

    @Step("Click on item in History...")
    public void clickOnHistoryItem(String objectName) {
        WebElement item = findElementWithWait(By.xpath(xpathItem.replace("{name}", objectName)));
        click(item);
    }

    @Step("Open Dashboard History link in a new tab...")
    public void openDashboardHistoryLinkInNewTab(String objectName) throws AWTException {
        WebElement xpathHistoryLink = findElementWithWait(By.xpath(xpathItem.replace("{name}", objectName)));
        openNewTabBrowserContextMenu(xpathHistoryLink);
        switchToOpenedTab();
    }

    @Step("Verify History empty state message is displayed...")
    public String getEmptyHistoryMessage() {
        return getText(emptyHistory);
    }

    @Step("Verify Show More button is not displayed...")
    public boolean isShowMoreButtonNotDisplayed() {
        return !isElementVisible(showMoreButton);
    }

    @Step("Verify item is added to Favorites...")
    public boolean isItemAddedToFavorites(String objectName) {
        WebElement starIcon = findElementWithFluentWait(By.xpath(xpathHistoryStarIcon.replace("{name}", objectName)));
        return isElementVisible(starIcon);
    }

    @Step("Verify count of items is correct...")
    public String getItemsCountCorrect() {
        WebElement itemsCount = findElementWithWait(By.xpath(xpathItemsCount));
        return itemsCount.getText();
    }

    @Step("Verify object type icon is displayed...")
    public boolean isObjectTypeIconDisplayed(String icon, String objectName) {
        waitForPageLoaded();
        WebElement objectTypeIcon = findElementWithFluentWait(By.xpath(xpathTypeIcon
                .replace("{icon}", icon).replace("{name}", objectName)));
        return isElementVisible(objectTypeIcon);
    }

    @Step("Get number of collapsed items...")
    public boolean getNumberOfCollapsedItems(int counter) {
        waitForNumberOfElementsToBe(By.xpath(collapsedItem), counter);
        return true;
    }

    @Step("Get number of open items...")
    public boolean getNumberOfOpenItems(int counter) {
        waitForNumberOfElementsToBe(By.xpath(itemsXpath), counter);
        return true;
    }

    @Step("Get the first item in the list..")
    public String getFirstItemInList() {
        return getText(firstItem);
    }

    @Step("Verify item is displayed in history...")
    public boolean isItemInHistory(String objectName) {
        waitForPageLoaded();
        WebElement item = findElementWithWait(By.xpath(xpathItem.replace("{name}", objectName)));
        return isElementVisible(item);
    }

    @Step("Collapse group of items ...")
    public void collapseGroupOfItems() {
        click(caretDownIcon);
    }

    @Step("Expand group of items ...")
    public void expandGroupOfItems() {
        click(caretUpIcon);
    }

    @Step("Verify date is displayed...")
    public boolean isDateDisplayed(String date) {
        final String actualDate = waitTillVisibleWithFluentWait(xpathDate).getText().trim();
        logger.info("Actual date is: {}", actualDate);
        return actualDate.contains(date);
    }

    @Step("Verify star icon is not displayed.in history list..")
    public boolean isStarIconNotDisplayedInHistoryList(String objectName) {
        waitForNumberOfElementsToBe(By.xpath(xpathHistoryStarIcon.replace("{name}", objectName)), 0);
        return true;
    }

    @Step("Verify no items in history list..")
    public boolean isNoItemsInHistoryList(String objectName) {
        waitForNumberOfElementsToBe(By.xpath(xpathItem.replace("{name}", objectName)), 0);
        return true;
    }

    @Step("Verify Add to Shopping Cart button is present for item: {objectName}")
    public boolean isAddToShoppingCartButtonDisplayforItem(String objectName) {
        try {
            WebElement item = findElementWithWait(By.xpath(xpathItem.replace("{name}", objectName)));
            hoverOverElement(item);
            WebElement addToShoppingCartButton = findElementWithWait(By.xpath(xpathAddToShoppingCartButtonSpecificItem
                    .replace("{name}", objectName)));
            return isElementVisible(addToShoppingCartButton);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Click on Add to Shopping button of item in History...")
    public void clickOnAddToShoppingCartButtonOfHistoryItem(String objectName) {
        WebElement item = findElementWithFluentWait(By.xpath(xpathItem.replace("{name}", objectName)));
        hoverOverElement(item);
        WebElement addToShoppingCartButton = waitForElementToBePresentFluentWait(By.xpath(xpathAddToShoppingCartButtonSpecificItem
                .replace("{name}", objectName)));
        click(waitForElementToBeClickable(addToShoppingCartButton));
    }
}
