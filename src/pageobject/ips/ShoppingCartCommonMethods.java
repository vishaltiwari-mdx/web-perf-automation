package com.methodics.phi.pageobject.ips;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ShoppingCartCommonMethods extends BasePage {

    private final WebDriver driver;

    public ShoppingCartCommonMethods(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = "[data-testid=ui-btn-ui-modal-remove]")
    private WebElement removeButtonDialog;

    @FindBy(css = "[data-testid=ui-modal] .modal-header")
    private WebElement removeIpvModelHeader;

    @FindBy(xpath = "//div[@class='ag-column-select-header']//input[@aria-label='Filter Columns Input']")
    private WebElement columnSearchField;

    String threeDotsSubMenuAllIp = "//*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-dropdown-btn-toggle']";
    String threeDotsSubMenuForSpecificIp = "//*[normalize-space()='{ipName}']//parent::div//following-sibling::*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='fav-record-action-btn']";
    String addToShoppingCart = "//*[normalize-space()='{ipName}']//parent::div//following-sibling::*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-add-to-sc-btn']";
    String goToLibrary = "//*[normalize-space()='{ipName}']//parent::div//following-sibling::*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-go-to-library-btn']";
    String removeButton = "//*[normalize-space()='{ipName}']//parent::div//following-sibling::*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-remove-from-rb-btn']";
    String numberOfIpRowBoxInSCTab="//*[@data-ref='eBodyViewport']//*[@role='row']";
    String getThreeDotsSubMenuForSpecificIpShoppingCartTab = "//*[normalize-space()='{ipFqn}']//parent::span/following-sibling::div[@data-testid='col-sub-menu-btn-container']";
    String resourceTabRemoveButton = "//*[normalize-space()='{ipFqn}']//parent::span/following-sibling::div[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-remove-from-sc-btn']";
    // Home favorite
    String getGetThreeDotsSubMenuForHomeFavorite = "//*[@title='{name}']/ancestor::span[@data-favorite-item-name]//*[@data-testid='col-sub-menu-btn-container']";
    String addToShoppingCartHomeFavorite = "//*[@title='{name}']/ancestor::span[@data-favorite-item-name]//*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-add-to-sc-btn']";
    String goToLibraryHomeFavorite = "//*[@title='{name}']/ancestor::span[@data-favorite-item-name]//*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-go-to-library-btn']";
    String removeButtonHomeFavorite = "//*[@title='{name}']/ancestor::span[@data-favorite-item-name]//*[@data-testid='col-sub-menu-btn-container']//*[@data-testid='ui-btn-remove-from-fv-btn']";
    private final String itemsInHomeFavorite = "(//a[@title='{name}'])";
    private final String shieldIconForPropertySet = "//*[@data-testid='ps-{ProtectedPropSetName}-shield-icon'][contains(@class,'icon-15x12')]";
    private final String customIconForPropertySet = "//*[@data-testid='ps-{ProtectedPropSetName}-custom-icon'][@class='{iconName} fa-{iconName} fas fa-fw icon-20x16 me-1']";
    private final String defaultIconForPropertySet = "//*[@data-testid='ps-{ProtectedPropSetName}-default-icon'][@class='icon-wrapper me-1']";
    private final String propertySetCheckBox = "//*[@data-testid='ps-{propertySetName}-checkbox']//parent::div";
    private final String propertyShieldIconColumnHeader = "//*[contains(@data-testid,'column-header-{propertyName}')][contains(@class,'icon-15x12')]";
    private final String propertyCustomIconColumnHeader = "//*[contains(@data-testid,'column-header-{propertyName}')][@class='fas fa-fw icon-15x12 me-2 {icon} fa-{icon}']";
    private final String columnCheckbox = " //*[@role='dialog']//*[normalize-space()='{name}'][@role='treeitem']//div[contains(@class,'ag-checkbox-input')]";


    @Step("Verify if 'Add to Shopping Cart' button for Home Favorite is displayed...")
    public boolean isAddToShoppingCartHomeFavoriteDisplayed(String ipName) {
        final String xpath = addToShoppingCartHomeFavorite.replace("{name}", ipName);
        final WebElement addToShoppingCartButton = findElementWithFluentWait(By.xpath(xpath));
        return isElementVisible(addToShoppingCartButton);
    }

    @Step("Click on 'Add to Shopping Cart' button for Home Favorite...")
    public void clickOnAddToShoppingCartHomeFavorite(String ipName) {
        final String xpath = addToShoppingCartHomeFavorite.replace("{name}", ipName);
        final WebElement addToShoppingCartButton = findElementWithFluentWait(By.xpath(xpath));
        addToShoppingCartButton.click();
    }

    public boolean isGoToLibraryHomeFavoriteDisplayed(String latestUrl) {
        final String xpath = goToLibraryHomeFavorite.replace("{name}", latestUrl);
        final WebElement goToLibraryButton = findElementWithFluentWait(By.xpath(xpath));
        return isElementVisible(goToLibraryButton);
    }

    @Step("Click on 'Go to Library' button for Home Favorite...")
    public void clickOnGoToLibraryHomeFavorite(String ipName) {
        final String xpath = goToLibraryHomeFavorite.replace("{name}", ipName);
        final WebElement goToLibraryButton = findElementWithFluentWait(By.xpath(xpath));
        goToLibraryButton.click();
        switchToOpenedTab();
    }

    @Step("Verify if 'Items in Home Favorite' element is not visible...")
    public boolean isItemsInHomeFavoriteNotVisible(String name) {
        final String xpath = itemsInHomeFavorite.replace("{name}", name);
        waitForNumberOfElementsToBe(By.xpath(xpath), 0);
        return true;
    }

    @Step("Verify if 'Remove' button for Home Favorite is visible...")
    public boolean isRemoveButtonHomeFavoriteVisible(String name) {
        final String xpath = removeButtonHomeFavorite.replace("{name}", name);
        final WebElement removeButton = findElementWithFluentWait(By.xpath(xpath));
        return isElementVisible(removeButton);
    }

    @Step("Click on 'Remove' button for Home Favorite...")
    public void clickOnRemoveButtonHomeFavorite(String ipName) {
        final String xpath = removeButtonHomeFavorite.replace("{name}", ipName);
        final WebElement removeButton = findElementWithFluentWait(By.xpath(xpath));
        removeButton.click();
    }

    @Step("Click on the three dots sub-menu for the given favorite item name...")
    public void clickOnThreeDotsSubMenuForHomeFavorite(String name) {
        final String xpath = getGetThreeDotsSubMenuForHomeFavorite.replace("{name}", name);
        final WebElement threeDotsSubMenu = findElementWithFluentWait(By.xpath(xpath));
        threeDotsSubMenu.click();
        DriverFactory.sleep(1000);// Need to improve
    }


    @Step("Verify No IPV added image should display for empty cart...")
    public boolean isThreeDotsMenuDisplayForIp(String ipName) {
        final WebElement completeThreeDotsSubMenu = findElementWithFluentWait(By.xpath(threeDotsSubMenuForSpecificIp.replace("{ipName}", ipName)));
        return isElementVisible(completeThreeDotsSubMenu);
    }

    @Step("get number Three dot Menu display...")
    public int getNumberOfThreeDotsMenuAssignedAllIps() {
        final List<WebElement> threeDotMenu = findElementsWithFluentWait(By.xpath(threeDotsSubMenuAllIp));
       return threeDotMenu.size();
    }

    @Step("Click on the three dots sub-menu for the given IP name...")
    public void clickOnThreeDotsSubMenu(String ipName) {
        logger.info("Click on the three dots sub-menu for the given IP name: " + ipName);
        waitForPageLoaded();
        final WebElement completeThreeDotsSubMenuForIP = findElementWithFluentWait(By.xpath(threeDotsSubMenuForSpecificIp.replace("{ipName}", ipName)));
        scrollToElement(completeThreeDotsSubMenuForIP);
        waitTillClickableWithFluentWait(completeThreeDotsSubMenuForIP).click();
    }

    @Step("Close three dot sub-menu for the given IP name...")
    public void closeThreeDotsSubMenu(String ipName) {
        DriverFactory.sleep(1000); // Need to improve
        final WebElement completeThreeDotsSubMenuForIP = findElementWithFluentWait(By.xpath(threeDotsSubMenuForSpecificIp.replace("{ipName}", ipName)));
        waitTillClickableWithFluentWait(completeThreeDotsSubMenuForIP).click();
    }

    @Step("Verify No IPV added image should display for empty cart...")
    public String isAddToShoppingCartDisplay(String ipName) {
        final WebElement completeThreeDotsSubMenu = findElementWithFluentWait(By.xpath(addToShoppingCart.replace("{ipName}", ipName)));
        return completeThreeDotsSubMenu.getText();
    }

    @Step("Verify No IPV added image should display for empty cart...")
    public void clickOnAddToShoppingCartButtonForIp(String ipName) {
        final WebElement completeThreeDotsSubMenu = findElementWithFluentWait(By.xpath(addToShoppingCart.replace("{ipName}", ipName)));
        completeThreeDotsSubMenu.click();
    }

    public String isGoToLibraryDisplay(String newIpName) {
        final WebElement gotToLibrary = findElementWithFluentWait(By.xpath(goToLibrary.replace("{ipName}", newIpName)));
        return gotToLibrary.getText().trim();
    }

    @Step("Verify No IPV added image should display for empty cart...")
    public void clickOnRemoveButtonForIpFromConfirmationModal(String ipName) {
        final WebElement completeThreeDotsSubMenu = findElementWithFluentWait(By.xpath(removeButton.replace("{ipName}", ipName)));
        completeThreeDotsSubMenu.click();
    }

    public void clickOnGoToLibraryButtonOfIp(String newIpName) {
        final WebElement gotToLibrary = findElementWithFluentWait(By.xpath(goToLibrary.replace("{ipName}", newIpName)));
        gotToLibrary.click();
        switchToOpenedTab();
    }

    @Step("Count the number of IP checkboxes in the Shopping Cart tab...")
    public int countNumberOfIpCheckBoxesInSCTab(PageModelName Name) {
        final List<WebElement> ipRowInShoppingCartTab = findElementsWithFluentWait(By.xpath((getDataTestId(Name) + numberOfIpRowBoxInSCTab).trim()));
        logger.info("Number of IP checkboxes in the Shopping Cart tab: " + ipRowInShoppingCartTab.size());
        return ipRowInShoppingCartTab.size();
    }

    @Step("Click on the three dots sub-menu for the specific IP in the Shopping Cart tab...")
    public void clickOnThreeDotsSubMenuForSpecificIpShoppingCartTab(String ipFqn) {
        waitForPageLoaded();
        final String xpath = getThreeDotsSubMenuForSpecificIpShoppingCartTab.replace("{ipFqn}", ipFqn);
        final WebElement threeDotsSubMenu = findElementWithFluentWait(By.xpath(xpath));
        threeDotsSubMenu.click();
    }

    @Step("Click on the 'Remove' button for the specific IP in the Shopping Cart tab...")
    public void clickOnResourceScTabRemoveButton(String ipFqn) {
        waitForPageLoaded();
        final String xpath = resourceTabRemoveButton.replace("{ipFqn}", ipFqn);
        final WebElement removeButton = findElementWithFluentWait(By.xpath(xpath));
        waitTillClickableWithFluentWait(removeButton).click();
    }

    @Step("Click on the Remove button")
    public void clickOnRemoveButton() {
        waitForPageLoaded();
        waitTillClickableWithFluentWait(removeButtonDialog).click();
    }

    @Step("Remove' button for the specific IP in the Shopping Cart tab...")
    public void clickOnRemoveButtonForIpFromResourceBrowserShoppingCartTab(String ipFqn) {
        clickOnThreeDotsSubMenuForSpecificIpShoppingCartTab(ipFqn);
        clickOnResourceScTabRemoveButton(ipFqn);
        clickOnRemoveButton();
    }

    @Step("Verify and return text of removeIpvModel body text element")
    public String getRemoveIpvModelHeader() {
        waitForElementToBeVisible(removeIpvModelHeader);
        return removeIpvModelHeader.getText().replace("\n", " ").replace("\r", " ").trim();
    }

    @Step("get number Three dot Menu display...")
    public int getNumberOfThreeDotsMenuAssignedAllIpsNew(PageModelName pageModelName) {
        waitForPageLoaded();
        final String threeDotsSubMenuAllIp1 = (getDataTestId(pageModelName) + threeDotsSubMenuAllIp).trim();
        final List<WebElement> threeDotMenu = findElementsWithFluentWait(By.xpath(threeDotsSubMenuAllIp1));
        logger.info("Number of three Dot Menu in the Shopping Cart tab: " + threeDotMenu.size());
        return threeDotMenu.size();
    }

    @Step("Click on Specific property set checkbox have dropdown with Scroll bar...")
    public void clickOnPropertySetCheckBox(String propertySetName) {
        final String checkBox = propertySetCheckBox.replace("{propertySetName}", propertySetName);
        final Actions actions = new Actions(driver);
        logger.info("checkbox of Property set xpath: "+checkBox);
        int attempts = 0;
        while (attempts < 13) {
            try {
                final WebElement checkBoxElement = driver.findElement(By.xpath(checkBox));
                if (checkBoxElement.isDisplayed() && checkBoxElement.isEnabled()) {
                    click(checkBoxElement);
                    logger.info("Checkbox '{}' clicked successfully.", propertySetName);
                    break;
                }
            } catch (Exception e) {
                logger.warn("Attempt {}: Checkbox '{}' is not clickable yet.", attempts + 1, propertySetName);
                actions.sendKeys(Keys.TAB).perform(); // Simulate pressing Tab
                attempts++;
            }

        }

        if (attempts == 13) {
            logger.error("Checkbox '{}' could not be clicked after 13 attempts.", propertySetName);
            throw new TimeoutException("Failed to click checkbox after multiple attempts.");
        }
    }

    @Step("Verify that shield icon for property set is visible...")
    public boolean isShieldIconForProtectedPropertySetVisible(String propertySetName, PageModelName pageModelName) {
        final WebElement shieldIcon = findElementWithWait(By.xpath(getDataTestId(pageModelName) + shieldIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)));
        if(shieldIcon.isDisplayed()){
            logger.info("Shield icon for property set '{}' is displayed.", propertySetName);}
        return shieldIcon.isDisplayed();
    }

    @Step("Verify that shield icon for unprotected property set is not visible...")
    public boolean isShieldIconNotDisplayForUnProtectedPropertySetVisible(String propertySetName, PageModelName pageModelName) {
        final String xpath = getDataTestId(pageModelName) + shieldIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName);
        final List<WebElement> shieldIcons = findElementsWithWait(By.xpath(xpath), 0);
        if(shieldIcons.isEmpty()){
            logger.info("Shield icon for property set '{}' is not displayed.", propertySetName);}
        return shieldIcons.isEmpty();
    }

    @Step("Verify that custom icon for property set is visible...")
    public boolean isCustomIconForPropertySetVisible(String propertySetName, String iconName, PageModelName pageModelName) {
        final WebElement customIcon = findElementWithWait(By.xpath(getDataTestId(pageModelName) + customIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)
                .replace("{iconName}", iconName)));
        if (customIcon.isDisplayed()) {
            logger.info("Custom icon '{}' for property set '{}' is displayed.", iconName, propertySetName);
        }
        return customIcon.isDisplayed();
    }

    @Step("Verify that custom icon for property set is visible...")
    public boolean isDefaultIconForPropertySetVisible(String propertySetName, PageModelName pageModelName) {
        final WebElement customIcon = findElementWithWait(By.xpath(getDataTestId(pageModelName) + defaultIconForPropertySet.replace("{ProtectedPropSetName}", propertySetName)));
        if (customIcon.isDisplayed()) {
            logger.info("default icon for property set '{}' is displayed.", propertySetName);
        }
        return customIcon.isDisplayed();
    }
    @Step("Verify that property custom icon in column header is displayed...")
    public boolean isPropertyShieldIconColumnInHeaderDisplayed(String propertyName, PageModelName pageModelName) {
        final String xpath = propertyShieldIconColumnHeader.replace("{propertyName}", propertyName);
        final WebElement customIconColumnHeader = findElementWithWait(By.xpath(xpath));
        if (customIconColumnHeader.isDisplayed()) {
            logger.info("Property custom icon column header '{}' is displayed.", propertyName);
        } else {
            logger.warn("Property custom icon column header '{}' is not displayed.", propertyName);
        }
        return customIconColumnHeader.isDisplayed();
    }

    @Step("Verify that property shield icon column header is displayed...")
    public boolean isPropertyCustomIconColumnHeaderDisplayed(String propertyName, PageModelName pageModelName) {
        final String iconName = propertyName.substring(propertyName.lastIndexOf('_') + 1);
        final String xpath = propertyCustomIconColumnHeader
                .replace("{propertyName}", propertyName)
                .replace("{icon}", iconName);
        final WebElement shieldIconColumnHeader = findElementWithWait(By.xpath(xpath));
        if (shieldIconColumnHeader.isDisplayed()) {
            logger.info("Property shield icon column header '{}' with icon '{}' is displayed.", propertyName, iconName);
        } else {
            logger.warn("Property shield icon column header '{}' with icon '{}' is not displayed.", propertyName, iconName);
        }
        return shieldIconColumnHeader.isDisplayed();
    }

    @Step("Verify that property custom icon column header is not present...")
    public boolean isPropertyCustomIconColumnHeaderNotPresent(String propertyName, PageModelName pageModelName) {
        final String iconName = propertyName.substring(propertyName.lastIndexOf('_') + 1);
        final String xpath = propertyCustomIconColumnHeader
                .replace("{propertyName}", propertyName)
                .replace("{icon}", iconName);
        final List<WebElement> customIconColumnHeaders = driver.findElements(By.xpath(xpath));
        if (customIconColumnHeaders.isEmpty()) {
            logger.info("Property custom icon column header '{}' is not present.", propertyName);
            return true;
        } else {
            logger.warn("Property custom icon column header '{}' is present.", propertyName);
            return false;
        }
    }

    @Step("Verify that property shield icon column header is not present...")
    public boolean isPropertyShieldIconColumnHeaderNotPresent(String propertyName, PageModelName pageModelName) {
        final String xpath = propertyShieldIconColumnHeader.replace("{propertyName}", propertyName);
        final List<WebElement> shieldIconColumnHeaders = driver.findElements(By.xpath(xpath));
        if (shieldIconColumnHeaders.isEmpty()) {
            logger.info("Property shield icon column header '{}' is not present.", propertyName);
            return true;
        } else {
            logger.warn("Property shield icon column header '{}' is present.", propertyName);
            return false;
        }
    }

    @Step("Verify that property set name '{propertySetName}' is not displayed...")
    public boolean isPropertySetNameNotDisplayed(String propertySetName, PageModelName pageModelName) {
        final String xpath = propertySetName.replace("{propertySetName}", propertySetName);
        final List<WebElement> elements = driver.findElements(By.xpath(xpath));
        if(elements.isEmpty() || !elements.get(0).isDisplayed()) {
            logger.info("Property set name '{}' is not displayed.", propertySetName);
            return true;
        }
        else {
            logger.warn("Property set name '{}' is displayed.", propertySetName);
            return false;
        }
    }

    @Step("Verify Property column is not present in column dropdown...")
    public boolean propertyColumnNotPresentInColumnDropdown(String column, PageModelName pageModelName) {
        inputText(columnSearchField, "");
        enterTextSlowly(columnSearchField, column);
        DriverFactory.sleep(1500);
        final List<WebElement> checkCheckboxes = driver.findElements(By.xpath(columnCheckbox.replace("{name}", column)));
        if (checkCheckboxes.isEmpty() || checkCheckboxes.get(0).isDisplayed()) {
            logger.info("Property column is not present in column dropdown", column);
            return true;
        } else {
            return false;
        }
    }
}
