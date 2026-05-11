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
import static com.methodics.phi.util.CommonUrls.LIBRARY_MANAGEMENT_PAGE;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibrariesPage extends BasePage {
    private final WebDriver driver;
    private static final String LIBRARY_MANAGEMENT_TITLE = "Libraries";

    public LibrariesPage(WebDriver driver) {
        this.driver = driver;

        if (driver != null) {
            driver.get(DriverFactory.getFullUrl(LIBRARY_MANAGEMENT_PAGE));
        }

        waitForTitle(LIBRARY_MANAGEMENT_TITLE);
        if (!driver.getTitle().contains("Libraries")) {
            throw new IllegalStateException("This is not Libraries Page, current page is: " + driver.getTitle());
        }
    }

    @FindBy(xpath = "//input[contains(@class,'libraries-search')]")
    private WebElement searchLibrariesInput;

    @FindBy(xpath = "//*[@role='button-clear-libraries']")
    private WebElement clearSearchButton;

    @FindBy(xpath = "//*[@role='Libraries']//*[contains(@class, 'page-name')]")
    private WebElement librarySubHeader;

    @FindBy(xpath = "//span[text()='Create a Library or select a Library to edit.']")
    private WebElement createLibraryHeader;

    @FindBy(xpath = "//div[contains(@class, 'info-description')]")
    private WebElement pageDescription;

    @FindBy(xpath = "//div[contains(@class, 'description')]/i[contains(@class, 'folder')]")
    private WebElement pageIcon;

    @FindBy(xpath = "//div[@class='mb-3' and normalize-space('No libraries found.')]")
    private WebElement noResultsMessage;

    // In `src/main/java/com/methodics/phi/pageobject/libraries/LibrariesPage.java`
    private final String xpathLibraryHeader =
            "//div[contains(@class,'page-name')][./span[1][normalize-space()='Libraries']]"
                    + "/span[@class='text-truncate' and normalize-space(@title)='{name}']";
    private final String xpathLibrary = "//div[@title={name}][@class='flex-grow-1 text-truncate']";
    private final String xpathLibraries = "//div[@class='d-flex']//div[@role='library-name']";
    private final String numberOfIpsBadge = "//div[@title='{name}']/following-sibling::*//span[contains(@class, 'badge')]";
    private final String libraryIcon = "//div[@title='{name}']/parent::*//i[contains(@class,'{icon}')]";

    @Step("Enter library name to the search field...")
    public void enterLibraryNameToSearchFieldAndPressEnter(String libName) {
        inputText(searchLibrariesInput, libName);
        pressEnterKey(searchLibrariesInput);
    }

    @Step("Remove library name from the search field by clicking on button...")
    public void clickOnClearSearchButton() {
        click(clearSearchButton);
    }

    @Step("Clear library search field...")
    public void clearSearchField() {
        waitForElementToBeClickable(searchLibrariesInput);
        clearInputFieldWithBackspace(searchLibrariesInput);
        DriverFactory.sleep(1000);
    }

    @Step("Click on library...")
    public void clickOnLibrary(String libName) {
        final String name = wrapObjNameForXPath(libName);
        scrollToElementForLazyLoading(driver.findElement(By.xpath(xpathLibrary.replace("{name}", name))));
        final WebElement library = waitForElementToBeVisible(driver.findElement(By.xpath(xpathLibrary.replace("{name}", name))));
        click(library);
    }

    @Step("Verify library is displayed...")
    public boolean isLibraryDisplayed(String libName) {
        final String name = wrapObjNameForXPath(libName);
        final WebElement library = waitForElementToBeVisible(driver.findElement(By.xpath(xpathLibrary.replace("{name}", name))));
        return isElementVisible(library);
    }

    @Step("Verify No Reselts Found message displayed...")
    public boolean noResulsFound() {
        return isElementVisible(noResultsMessage);
    }

    @Step("Get number of the libraries or 0 if none found")
    public int getNumberOfLibraries(int expectedCount) {
        //it looks like without this sleep selenium doesn't wait at all for the list with the libraries changes.
        // It starts looking for the element even before previous command has finished
        DriverFactory.sleep(1000);
        try {
            final List<WebElement> elements = findElementsWithFluentWait(By.xpath(xpathLibraries));
            return elements.size();
        } catch (Exception e) {
            // Return 0 if no libraries are found
            return 0;
        }
    }

    @Step("Scroll and get the number of libraries...")
    public int scrollAndGetNumberOfLibrariesFromSideMenu(int libraries, int numberOfScrolls) {
        int actual = 0;
        final int expected = libraries;
        for (int scrolls = 0; scrolls < numberOfScrolls; scrolls++)
            do {
                if (scrolls == numberOfScrolls)
                    break;
                actual = getNumberOfElements(driver.findElements(By.xpath(xpathLibraries)));
                scrollDownUsingElement(driver.findElement(By.xpath("//div[@class= 'list']")));
                scrolls++;
            }
            while (actual != expected);
        return actual;
    }

    @Step("Get number of IPs in the library...")
    public String getNumberOfIpsInLibrary(String libName) {
        return getText(driver.findElement(By.xpath(numberOfIpsBadge.replace("{name}", libName))));
    }

    @Step("Verify Library sub-header is displayed...")
    public boolean isLibrarySubHeaderPresent() {
        return isElementVisible(librarySubHeader);
    }

    @Step("Verify sub-header for selected library is displayed...")
    public boolean isSelectedLibrarySubHeaderPresent(String libName) {
        final WebElement libHeader = waitTillVisibleWithFluentWait(driver.findElement(By.xpath(xpathLibraryHeader.replace("{name}", libName))));
        return isElementVisible(libHeader);
    }

    @Step("Verify create new library info message is displayed...")
    public boolean isCreateLibraryPanelHeaderDisplayed() {
        return isElementVisible(createLibraryHeader);
    }

    @Step("Verify info description displayed...")
    public boolean isPageDescriptionPresent() {
        return isElementVisible(pageDescription);
    }

    @Step("Get page description...")
    public String getPageDescription() {
        return getText(pageDescription);
    }

    @Step("Verify page icon is displayed...")
    public boolean isPageIconPresent() {
        return isElementVisible(pageIcon);
    }

    @Step("Verify library icon is present...")
    public boolean isLibraryIconPresent(String libName, String icon) {
        final WebElement libIcon = findElementWithWait(By.xpath(libraryIcon.replace("{name}", libName)
                .replace("{icon}", icon)));
        return isElementVisible(libIcon);
    }

    @Step("Go to Library Management page...")
    public void goToLibrariesPage(String url) {
        goTo(url);
    }
}
