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
package com.methodics.phi.pageobject.dashboard;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class FavoritesPage extends BasePage {
    private WebDriver driver;

    public FavoritesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[@title='Show more favorites']")
    private WebElement showMoreButton;

    @FindBy(xpath = "//div[contains(@class, 'type-header')]")
    private WebElement typeColumnHeader;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='type']")
    private WebElement firstRowItemType;

    @FindBy(xpath = "//div[@row-index='0']/div[@col-id='name']/span//a")
    private WebElement firstRowItemName;

    @FindBy(xpath = "//h5[normalize-space()='No favorites added']")
    private WebElement favoritesDashboardEmpty;

    @FindBy(xpath = "//div[@class= 'favorites-list__grid']")
    private WebElement favoritesDashboardNotEmpty;

    private String starIcon = "//*[not(@style='display: none;')]/button[not(contains(@class, 'action-selected-favorite'))]/i[contains(@class, 'star')]";
    private String starIconNav = "//*[contains(@class,'mdx-nav-item__div__favorite-button') and not(@style='display: none;')]//child::button[not(contains(@class, 'selected-favorite')) and not(contains(@class, 'menu-item__inner-element'))]//i";
    private String starIconTab = "//span[normalize-space()='{tab}']//following::button[1]//i[contains(@class, 'fas fa-star')]";
    private String removeFavoriteBtn = "//button[contains(@class, 'selected-favorite')]//i[contains(@class, 'star')]";
    private String removeFavoriteTabBtn = "//span[normalize-space()='{tab}']//following::button[contains(@class, 'selected-favorite-regular')][1]//i[contains(@class, 'fa-star')]";
    private String removeFavoriteActiveTabBtn = "//span[normalize-space()='{tab}']/parent::div[contains(@class,'selected')]//button[contains(@class,'btn-line-action-selected-favorite-regular')]";
    private String xpathItemFavorite = "//span[@data-favorite-item-name='{name}']//ancestor::a";
    private String favoriteTitle = "//a[@title='{name}']";
    private String historyItemPath = "//span[contains(@data-history-item-name, '{name}')]";
    private String xpathRow = "//span[@data-favorite-item-name='{name}']/ancestor::div[@role='row']";
    private String xpathTypeIcon = "//span[@data-favorite-item-type='{icon}' and @data-favorite-item-name='{name}']";
    private String xpathSortType = "//span[@class='text-body-secondary']";
    private String row = "(//a[@title])";
    private String typeCell = "//div[@role='row'][contains(.,'{object}')][contains(., '{tab}')]/div[@col-id='type']";
    private String favoriteVersion = "//ul[@class='dropdown-menu dropdown-menu-end show']//child::span[text()='{version}']";
    private String favoriteVersionSelected = "//div[contains(@class, 'selected')][contains(.,'{tab}')]//i[contains(@style,'app-colors-{color}')]";
    private String selectedBoth = "//div[contains(@class, 'selected')][contains(.,'{tab}')]//i[contains(@class,'fa-star-half')]";
    private String deleteButtonFavoriteList = "//a[@data-favorite-item-name='{name}']/parent::span//i[contains(@class, 'favorites-list__name-icon_remove icon-hidden')]";
    private String notSelectedTabBoth = "//div[not(contains(@class, 'selected'))][contains(.,'{tab}')]//i[contains(@class,'fa-star-half')]";

    @Step("Click on Favorites star icon...")
    public void clickOnStarIcon() {
        try {
            final WebElement element = findElementWithWait((By.xpath(starIcon)));
        click(waitForElementToBeClickable(element));
        } catch (Exception e) {
            logger.info("already mark as favorite");
        }
    }

    @Step("Hover over star icon...")
    public void hoverOverStarIcon() {
        hoverOverElement(findElementWithWait(By.xpath(starIcon)));
    }

    @Step("Verify Favorites star icon is visible on nav panel...")
    public boolean isStarIconVisibleOnNavPanel() {
        return isElementVisible(findElementWithWait(By.xpath(starIcon)));
    }

    @Step("Verify star icon is displayed for tab...")
    public boolean isSaveTabToFavoritesButtonPresent(String tab) {
        WebElement starIcon = findElementWithWait(By.xpath(starIconTab.replace("{tab}", tab)));
        scrollToElement(starIcon);
        return isElementVisible(starIcon);
    }

    @Step("Save tab to favorites...")
    public void clickOnSaveTabToFavorites(String tab) {
        waitForPageLoaded();
        clickWithJS(findElementWithFluentWait(By.xpath(starIconTab.replace("{tab}", tab))));
    }

    @Step("Get number of Save to Favorites buttons..")
    public int getNumberOfSaveToFavoritesButtons() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(starIconNav)));
    }

    @Step("Click on filled star icon...")
    public void clickOnFilledStarIcon() {
        click(findElementWithWait(By.xpath(removeFavoriteBtn)));
    }

    @Step("Hover over filled star icon...")
    public void hoverOverFilledStarIcon() {
        hoverOverElement(findElementWithWait(By.xpath(removeFavoriteBtn)));
    }

    @Step("Verify object is marked as favorite...")
    public boolean isStarIconFilled() {
        return isElementVisible(findElementWithWait(By.xpath(removeFavoriteBtn)));
    }

    @Step("Get number of Remove from Favorites buttons..")
    public int getNumberOfRemoveFromFavoritesButtons() {
        return getNumberOfVisibleElements(driver.findElements(By.xpath(removeFavoriteBtn)));
    }

    @Step("Verify tab is marked as favorite...")
    public void areRemoveTabFromFavoritesButtonPresent(String[] tabs) {
        for (String tab : tabs) {
            Assert.assertTrue(isRemoveTabFromFavoritesButtonPresent(tab), "Option " + tabs + " is not displayed");
        }
    }

    @Step("Verify tab is marked as favorite...")
    public boolean isRemoveTabFromFavoritesButtonPresent(String tab) {
        WebElement starIcon = findElementWithWait(By.xpath(removeFavoriteTabBtn.replace("{tab}", tab)));
        return isElementVisible(starIcon);
    }

    @Step("Verify tab is not marked as favorite...")
    public boolean isTabNotMarkedAsFavorite(String tab) {
        waitForNumberOfElementsToBe(By.xpath(removeFavoriteTabBtn.replace("{tab}", tab)), 0);
        return true;
    }

    @Step("Click on remove tab Favorites star icon ...")
    public void clickOnRemoveTabFromFavorites(String tab) {
        WebElement filledStarIcon = findElementWithWait(By.xpath(removeFavoriteTabBtn.replace("{tab}", tab)));
        click(filledStarIcon);
    }

    @Step("Verify active tab is marked as favorite...")
    public boolean isStarIconFilledActiveTab(String tab) {
        WebElement starIcon = findElementWithWait(By.xpath(removeFavoriteActiveTabBtn.replace("{tab}", tab)));
        scrollToElement(starIcon);
        return isElementVisible(starIcon);
    }

    @Step("Verify Favorites widget info message is displayed...")
    public boolean isFavoritesListEmpty() {
        return isElementVisible(favoritesDashboardEmpty);
    }

    @Step("Get number of items..")
    public int getNumberOfRows(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(row), counter).size();
    }

    @Step("Verify item is displayed in Favorites list...")
    public boolean isFavoriteDisplayed(String objectName) {
        WebElement item = findElementWithWait(By.xpath(favoriteTitle.replace("{name}", objectName)));
        return isElementVisible(item);
    }

    @Step("Get number of IPs when do lazy loading...")
    public int getNumberOfIpsForLazyLoad() {
        waitForPageLoaded();
        return getNumberOfRowsForLazyLoad(row, row + "[11]");
    }

    @Step("Verify item is not displayed in Favorites list...")
    public boolean isItemNotInFavorites(String objectName) {
        waitForNumberOfElementsToBe(By.xpath(xpathItemFavorite.replace("{name}", objectName)), 0);
        return true;
    }

    @Step("Click on Favorites item...")
    public void clickOnFavorite(String objectName) {
        WebElement item = findElementWithWait(By.xpath(xpathItemFavorite.replace("{name}", objectName)));
        click(item);
    }

    @Step("Verify object type icon is displayed...")
    public boolean isObjectTypeIconDisplayed(String icon, String objectName) {
        WebElement objectTypeIcon = findElementWithWait(By.xpath(xpathTypeIcon.replace("{icon}", icon).replace("{name}", objectName)));
        return isElementVisible(objectTypeIcon);
    }

    @Step("Delete item from Favorites list...")
    public void deleteItemFromList(String objectName) {
        WebElement itemRow = findElementWithWait(By.xpath(xpathRow.replace("{name}", objectName)));
        click(itemRow);
        WebElement deleteButton = findElementWithWait(By.xpath(deleteButtonFavoriteList.replace("{name}", objectName)));
        waitForElementToBeClickable(deleteButton);
        click(deleteButton);
    }

    @Step("Click on Show More button...")
    public void clickOnShowMoreButton() {
        scrollToElement(showMoreButton);
        click(showMoreButton);
    }

    @Step("Click on Type header to sort items...")
    public void clickOnTypeHeader() {
        click(typeColumnHeader);
    }

    @Step("Verify sorting type is displayed in header...")
    public String getSortingType() {
        return findElementWithWait(By.xpath(xpathSortType)).getText();
    }

    @Step("Get object type from the top row...")
    public String getTypeFromFirstRow() {
        return getText(firstRowItemType);
    }

    @Step("Get object name from the top row...")
    public String getNameFromFirstRow() {
        return getText(firstRowItemName);
    }

    @Step("Get object type...")
    public String getObjectType(String object, String tab) {
        WebElement type = driver.findElement(By.xpath(typeCell.replace("{tab}", tab).replace("{object}", object)));
        return getText(type);
    }

    @Step("Drag and Drop Item to favorites")
    public void addItemToFavorite(String objectName) {
        WebElement item = driver.findElement(By.xpath(historyItemPath.replace("{name}", objectName)));
        if (isFavoritesListEmpty()) {
            dragAndDropElement(item, favoritesDashboardEmpty);
        } else {
            dragAndDropElement(item, favoritesDashboardNotEmpty);
        }
    }

    @Step("Selecting version from favorites dropdown...")
    public void selectVersionFromFavoritesDropdown(String version) {
        WebElement favoriteVers = findElementWithWait(By.xpath(favoriteVersion.replace("{version}", version)));
        scrollToElement(favoriteVers);
        click(favoriteVers);
        DriverFactory.sleep(1000);
    }

    @Step("Unselecting version from favorites dropdown...")
    public void unselectVersionFromFavoritesDropdown(String version, String color) {
        click(findElementWithWait(By.xpath(favoriteVersionSelected.replace("{tab}", version).replace("{color}", color))));
    }

    @Step("Checking if version is selected as favorite..")
    public boolean isVersionSelectedAsFavorite(String tab, String color) {
        WebElement favorite = findElementWithWait(By.xpath(favoriteVersionSelected.replace("{tab}", tab).replace("{color}", color)));
        return isElementVisible(favorite);
    }

    @Step("Checking if both current and latest versions are selected as favorite..")
    public boolean isCurrentAndLatestVersionSelectedAsFavorite(String tab) {
        WebElement favorite = findElementWithWait(By.xpath(selectedBoth.replace("{tab}", tab)));
        return isElementVisible(favorite);
    }

    @Step("Checking if both current and latest versions are selected as favorite on not selected tab..")
    public boolean isCurrentAndLatestVersionSelectedAsFavoriteOnNotSelectedTab(String tab) {
        WebElement favorite = findElementWithWait(By.xpath(notSelectedTabBoth.replace("{tab}", tab)));
        return isElementVisible(favorite);
    }

    @Step("Hover over tab star icon...")
    public void hoverOverTabStarIcon(String tabName) {
        hoverOverElement(findElementWithWait(By.xpath(starIconTab.replace("{tab}", tabName))));
    }

    @Step("Removi favorite from selected tab...")
    public void removeFavoriteFromSelectedTab(String tab) {
        WebElement filledStarIcon = findElementWithWait(By.xpath(removeFavoriteActiveTabBtn.replace("{tab}", tab)));
        click(filledStarIcon);
    }

}
