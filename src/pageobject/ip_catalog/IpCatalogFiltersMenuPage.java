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
package com.methodics.phi.pageobject.ip_catalog;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.GridTablePage;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class IpCatalogFiltersMenuPage extends BasePage {
    private WebDriver driver;

    public IpCatalogFiltersMenuPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder='Search Labels']")
    private WebElement labelsInputField;

    @FindBy(css = "[class*='label-filter'] [class*='fa-minus']")
    private WebElement collapseLabelFilterBtn;

    @FindBy(css = "[class*='label-filter'] [class*='fa-plus']")
    private WebElement expandLabelFilterBtn;

    @FindBy(xpath = "//div[contains(@class, 'label-filter') and @style='display: none;']")
    private List<WebElement> labelsFilterHidden;

    @FindBy(xpath = "//div[contains(@class, 'label-filter-category')]//span[contains(@class, 'badge-primary')]")
    private WebElement appliedLabelsCount;

    @FindBy(xpath = "//*[@data-testid='ui-form-group']//input[@data-testid='ui-in-input-field-input-value' and @placeholder='Search Libraries']")
    private WebElement libraryInputField;

    @FindBy(css = "[class*='library-filter'] [class*='fa-minus']")
    private WebElement collapseLibFilterBtn;

    @FindBy(css = "[class*='library-filter'] [class*='fa-plus']")
    private WebElement expandLibFilterBtn;

    @FindBy(xpath = "//div[contains(@class,'library-filter-category')]//span[contains(@class,'badge-primary')]")
    private WebElement appliedLibsCount;

    @FindBy(css = "div[class*='library-filter'][data-testid='ui-card'] li")
    private List<WebElement> libraries;

    @FindBy(xpath = "//div[@role='alert'][normalize-space()='No property filters configured.']")
    private WebElement noPropertyFiltersAlert;

    @FindBy(xpath = "//div[contains(@class, 'choice-property-filter-category') and not(@style='display: none;')]")
    private List<WebElement> propertyFilters;

    @FindBy(xpath = "//div[contains(@class, 'choice-property-filter-category') and @style='display: none;']")
    private List<WebElement> propertyHiddenFilters;

    @FindBy(xpath = "//div[contains(@class,'choice-property-filter-category')]//following::span[@class='menu-item__top-row__trailing-text ms-2']")
    private List<WebElement> choiceFiltersAllPropertyFilters;

    @FindBy(xpath = "//div[contains(@aria-controls,'choice-property-filter')]/preceding-sibling::div")
    private List<WebElement> choiceProperties;

    @FindBy(xpath = "//div[@class='quick-filters-panel__categories__wrapper']//div[@title]")
    private List<WebElement> filterCategories;

    @FindBy(xpath = "//i[contains(@class,'input-field__clear-icon')]")
    private WebElement clearSearchBtn;

    @FindBy(xpath = "//div[contains(@class, 'action-button')]//i[contains(@class, 'fa-minus')]")
    private WebElement collapseAllFilters;

    @FindBy(xpath = "//div[contains(@class, 'action-button')]//i[contains(@class, 'fa-plus')]")
    private WebElement expandAllFilters;

    @FindBy(xpath = "//div[@class='card expansion-panel filter-category library-filter-category quick-filters-panel__categories__wrapper']")
    private WebElement libraryFilterSectionVisible;

    @FindBy(xpath = "//div[@role='alert'][normalize-space()='Search returned no results.']")
    private WebElement noSearchResultsAlert;

    @FindBy(xpath = "///i[contains(@class,'menu-item-header__hint-icon')]")
    private WebElement filterProfilesHintIcon;

    @FindBy(xpath = "//ul[@class='dropdown-menu show']//li[@class='menu-item']//span")
    private List<WebElement> filterProfiles;

    @FindBy(xpath = "//div[contains(@class,'quick-filters-panel__title')]//button[@data-testid='ui-dropdown-btn-toggle']//span")
    private WebElement profileSelected;

    @FindBy(xpath = "//ul[@class = 'dropdown-menu show']//i[contains(@class, 'menu-item__top-row__checkmark')]/following-sibling::span")
    private WebElement profileSelectedInMenu;

    @FindBy(xpath = "//button[contains(@class, 'btn-line-action-text-icon')]//div/i")
    private WebElement filterProfileDropdown;

    private String labels = "//*[@data-testid='ui-card'][contains(@class,'label-filter')]//*[@data-testid='menu-item']";
    private String appliedLabels = "//div[@data-testid='ui-card'][contains(@class,'label-filter-category')]//input[@data-testid='filter-option-input' and @checked]";
    private String uncheckedLabels = "//div[@data-testid='ui-card'][contains(@class,'label-filter-category')]//input[@data-testid='filter-option-input' and not(@checked)]";
    private String label = "//div[@data-testid='ui-card'][contains(@class,'label-filter-category')]//span[@title='{label}']";
    private String labelChecked = "//div[@data-testid='ui-card'][contains(@class,'label-filter-category')]//input[@data-testid='filter-option-input' and @checked and following-sibling::label//span[@title='{label}']]";
    private String labelUnchecked = "//*[contains(@class,'label-filter')]//*[normalize-space()='{label}'][contains(@class,'unchecked')]/input";
    private String labelFilterResultsCount = "//div[@data-testid='ui-card'][contains(@class,'label-filter-category')]//span[@title='{label}']/ancestor::form//span[contains(@class,'menu-item__top-row__trailing-text')]";
    private String noLabels = "//*[contains(@class,'label-filter')]//*[contains(@class,'overflow-auto my-0')]//span";
    private String libraryCheckbox = "//div[@data-testid='ui-card'][contains(@class,'library-filter-category')]//input[@data-testid='filter-option-input' and following-sibling::label//span[@title='{lib}']]";
    private String libNumberOfIps = "//div[@data-testid='ui-card'][contains(@class,'library-filter-category')]//span[@title='{lib}']/ancestor::form//span[contains(@class,'menu-item__top-row__trailing-text')]";
    private String noLibraries = "//*[contains(@class,'library-filter')]//*[contains(@class,'overflow-auto my-0')]//span";
    private String selectedLib = "//div[@data-testid='ui-card'][contains(@class,'library-filter-category')]//input[@data-testid='filter-option-input' and @checked and following-sibling::label//span[@title='{lib}']]";
    private String planningBomPropertyFilter = "//div[contains(@class, 'quick-filters__select')]//div[@class='multiselect__tags']//span[@title='{prop}'][contains(@class, 'truncate')][normalize-space()='{prop}']";
    private String defaultlibIcon = "//*[contains(@class,'badge-lib')]/following-sibling::*[normalize-space()='{lib}']";
    private String libIcon = "//div[contains(@class,'library-filter')]//i[contains(@class, '{icon}')]/following-sibling::*[normalize-space()='{lib}']";
    private String choicePropFilterNumberOfIps = "//div[contains(normalize-space(),'{propName}')]//span[normalize-space()='{choice}']/following-sibling::span";
    private String choiceCheckbox = "//span[normalize-space()='{propName}']//ancestor::div[@data-testid='ui-card']//span[normalize-space()='{choice}']//input";
    private String choiceInputFieldOnlyOneFilterMenu = "//div[contains(@class,'choice-property-filter')]//input[@data-input='filterOptions']";
    private String noChoices = "//div[@title='{propName}']//following::ul[contains(@class, 'filter-category__option-group')]/div[@class='v3-infinite-loading']/span";
    private String choiceChecked = "//div[contains(normalize-space(),'{propName}')]//div[contains(@class, 'normal_checked')]//span[normalize-space()='{choice}']";
    private String choiceUnchecked = "//div[contains(normalize-space(),'{propName}')]//div[contains(@class, 'checkbox-normal')]//span[normalize-space()='{choice}']";
    private String selectedPropOptionsCount = "//*[contains(@class, 'flex')]//*[@title='{prop}'][contains(@class, 'text-truncate')]/following-sibling::span[contains(@class,'badge-primary')]";
    private String propertyValues = "//div[@id='choice-property-filter_{prop}']//li";
    private String clearFilter = "//div[@title='{filter}']/following-sibling::button";
    private String clearAllFiltersButton = "//*[@data-testid='ui-btn-quick-filters-clear-all-filters']";
    private String checkedLibraryBox = "//div[@data-testid='ui-card'][contains(@class,'library-filter-category')]//input[@data-testid='filter-option-input' and following-sibling::label//span[@title='{lib}'] and @checked]";

    // Combine when works boths same xpath
    private String collapsePropFilterBtn = "//span[normalize-space()='{propName}']//ancestor::div[@data-testid='ui-card']//div[contains(@class, 'plus-minus expansion-panel__header__icon ms-2')]//i";
    private String expandPropFilterBtn = "//span[normalize-space()='{propName}']//ancestor::div[@data-testid='ui-card']//div[contains(@class, 'plus-minus expansion-panel__header__icon ms-2')]//i";

    private String planningBomPropValue = "//div[contains(@class,'multiselect--active')]//span[@title='{value}'][normalize-space()='{value}']";
    private String filterCategorySpinner = "//span[contains(@class, 'spinner-border-sm')]";
    private String quickFilterProfileSelection = "//ul[@class='dropdown-menu show']//span[normalize-space()='{profiles}']";
    private String filterOptions = "//div[@title='{propName}']//ancestor::div/div[@data-testid='ui-collapse']//label[@data-testid='filter-option-label']//span[@title]";
    private String xpathVisibleFilterOptions = "//div[@id='{propName}-filter' or @id='choice-property-filter_{propName}']//ul[contains(@class, 'filter-category__option-group')]";
    private String xpathPropertiesDataSfom = "//div[contains(@class,'card-header')]//div[@title='{propName}']/ancestor::div[contains(@class, 'card expansion-panel filter-category choice-property-filter-category')]";

    @Step("Collapse labels filter...")
    public void collapseLabelFilter() {
        click(collapseLabelFilterBtn);
    }

    @Step("Expand labels filter...")
    public void expandLabelFilter() {
        click(expandLabelFilterBtn);
    }

    @Step("Get number of labels in filter...")
    public int getNumberOfLabels(int counter) {
        waitForNumberOfElementsToBe(By.xpath(labels), counter);
        return findElementsWithWait(By.xpath(labels), counter).size();
    }

    @Step("Get number of applied labels...")
    public int getNumberOfAppliedLabels(int counter) {
        return findElementsWithWait(By.xpath(appliedLabels), counter).size();
    }

    @Step("Get number of not applied labels...")
    public int getNumberOfNotSelectedLabels(int counter) {
        return findElementsWithWait(By.xpath(uncheckedLabels), counter).size();
    }

    @Step("Verify label is displayed in filter...")
    public int isLabelDisplayed(String labelName, int counter) {
        return waitForElements(By.xpath(label.replace("{label}", labelName)), counter).size();
    }

    @Step("Verify label is applied...")
    public boolean isLabelSelected(String labelName) {
        WebElement label = findElementWithWait(By.xpath(labelChecked.replace("{label}", labelName)));
        return isElementVisible(label);
    }

    @Step("Verify no label is selected...")
    public boolean noLabelSelected(String labelName) {
        waitForNumberOfElementsToBe(By.xpath(labelChecked.replace("{label}", labelName)), 0);
        return true;
    }

    @Step("Apply label filter...")
    public void selectLabel(String label) {
        WebElement checkbox = findElementWithWait(By.xpath(labelUnchecked.replace("{label}", label)));
        click(checkbox);
    }

    @Step("Deselect label...")
    public void deselectLabel(String label) {
        WebElement checkbox = findElementWithWait(By.xpath(labelChecked.replace("{label}", label)));
        click(checkbox);
        GridTablePage gridTablePage = new GridTablePage(DriverFactory.getBrowserInstance());
        gridTablePage.waitForDataToLoad();
    }

    @Step("Type label name...")
    public void enterLabelName(String labelName) {
        inputText(labelsInputField, labelName);
    }

    @Step("Verify library section is visible...")
    public boolean isLibrarySectionVisible() {
        return isElementVisible(libraryFilterSectionVisible);
    }

    @Step("Verify no elements found message is displayed...")
    public String noLabelsFound() {
        WebElement message = findElementWithWait(By.xpath(noLabels));
        return getText(message);
    }

    @Step("Get number of applied labels from badge...")
    public String getNumberOfAppliedLabelsFromBadge() {
        return waitForElementToBeVisible(appliedLabelsCount).getText();
    }

    @Step("Hover over applied labels badge...")
    public void hoverOverAppliedLabelsCount() {
        hoverOverElement(waitForElementToBeVisible(appliedLabelsCount));
    }

    @Step("Get filter results count...")
    public int getNumberOfIpsForLabel(String labelName) {
        WebElement resultsCount = findElementWithWait(By.xpath(labelFilterResultsCount.replace("{label}", labelName)));
        return Integer.parseInt(resultsCount.getText());
    }

    @Step("Verify label filter category is hidden...")
    public boolean isLabelFilterHidden() {
        return labelsFilterHidden.size() == 1;
    }

    @Step("Collapse library filter...")
    public void collapseLibraryFilter() {
        click(collapseLibFilterBtn);
    }

    @Step("Expand library filter...")
    public void expandLibraryFilter() {
        click(expandLibFilterBtn);
    }

    @Step("Enter library name...")
    public void enterLibraryName(String libName) {
        inputText(libraryInputField, libName);
    }

    @Step("Remove library name from the search field...")
    public void removeLibraryName() {
        clearInputFieldWithBackspace(libraryInputField);
    }

    @Step("Getting number of libraries in library filter section...")
    public int getNumberOfLibraries() {
        waitForPageLoaded();
        return libraries.size();
    }

    @Step("Verify library is present...")
    public int isLibraryDisplayed(String libName, int counter) {
        return waitForElements(By.xpath(libraryCheckbox.replace("{lib}", libName)), counter).size();
    }

    @Step("Click on library...")
    public void clickOnLibraryCheckbox(String libName) {
        WebElement library = findElementWithWait(By.xpath(libraryCheckbox.replace("{lib}", libName)));
        click(library);
    }

    @Step("Check Library checkbox...")
    public void checkLibraryCheckbox(String library) {
        clickOnLibraryCheckbox(library);
    }

    @Step("Verify Library is selected...")
    public boolean isLibrarySelected(String libName) {
        WebElement library = findElementWithWait(By.xpath(selectedLib.replace("{lib}", libName)));
        return isElementVisible(library);
    }

    @Step("Verify no library is selected...")
    public boolean noLibrarySelected(String libName) {
        waitForNumberOfElementsToBe(By.xpath(selectedLib.replace("{lib}", libName)), 0);
        return true;
    }

    @Step("Verify no elements found message is displayed...")
    public String noLibrariesFound() {
        WebElement message = findElementWithWait(By.xpath(noLibraries));
        return getText(message);
    }

    @Step("Getting number of ips in library")
    public int getNumberOfIpsInLibrary(String library) {
        WebElement number = findElementWithWait(By.xpath(libNumberOfIps.replace("{lib}", library)));
        return Integer.parseInt(number.getText());
    }

    @Step("Select multiple libraries")
    public void selectMultipleLibraries(String... libraries) {
        for (String library : libraries) {
            clickOnLibraryCheckbox(library);
        }
    }

    @Step("Getting number of selected options in library filter")
    public int getNumberOfSelectedLibsOnBadge() {
        return Integer.parseInt(appliedLibsCount.getText());
    }

    @Step("Verify default library icon is displayed...")
    public boolean isDefaultLibIconDisplayed(String libName) {
        WebElement libIcon = findElementWithWait(By.xpath(defaultlibIcon.replace("{lib}", libName)));
        return isElementVisible(libIcon);
    }

    @Step("Verify custom library icon is displayed...")
    public boolean isCustomLibIconDisplayed(String libName, String icon) {
        WebElement libraryIcon = findElementWithWait(By.xpath(libIcon.replace("{lib}", libName).replace("{icon}", icon)));
        return isElementVisible(libraryIcon);
    }

    @Step("Verify `No property filters configured` alert is displayed...")
    public boolean noPropFiltersAlertPresent() {
        return isElementVisible(noPropertyFiltersAlert);
    }

    @Step("Click on `No property filters configured` alert...")
    public void clickOnFiltersAlert() {
        click(noPropertyFiltersAlert);
        switchToOpenedTab();
    }

    @Step("Get number of property filter categories...")
    public int getNumberOfPropertyFilters() {
        return propertyFilters.size();
    }

    @Step("PlanningBOM select property value from dropdown...")
    public void selectPropertyValue(String propName, String... options) {
        for (String choice : options) {
            WebElement quickFilterSelector = findElementWithWait(By.xpath(planningBomPropertyFilter.replace("{prop}", propName)));
            click(quickFilterSelector);
            WebElement choiceValue = findElementWithWait(By.xpath(planningBomPropValue.replace("{value}", choice)));
            click(choiceValue);
        }
    }

    @Step("Getting number of ips in Choice Property Filter")
    public int getNumberOfIpsInChoicePropFilter(String propertyName, String choiceValue) {
        WebElement number = findElementWithFluentWait(By.xpath(choicePropFilterNumberOfIps.replace("{propName}", propertyName).replace("{choice}", choiceValue)));
        return Integer.parseInt(number.getText());
    }

    @Step("QF check choice checkbox...")
    public void checkChoiceCheckbox(String propertyName, String choiceValue) {
        WebElement choice = waitForElementToBePresent(By.xpath(choiceCheckbox.replace("{choice}", choiceValue).replace("{propName}", propertyName)));
        waitForSpinnersToDisappear(Duration.ofSeconds(5));//Need to Improve
        waitForElementToBeClickable(choice).click();
    }

    @Step("QF enter choice name when only one property filter is present...")
    public void enterChoiceNameOneProperty(String choiceValue) {
        WebElement choiceInput = findElementWithWait(By.xpath(choiceInputFieldOnlyOneFilterMenu));
        inputText(choiceInput, choiceValue);
    }

    @Step("QF getting number of choices in all property filters...")
    public int getNumberOfChoicesAllPropertyFilters() {
        return choiceFiltersAllPropertyFilters.size();
    }

    @Step("QF verify no elements found message is displayed for property value filter...")
    public String noChoicesFound(String propertyName) {
        WebElement message = findElementWithWait(By.xpath(noChoices.replace("{propName}", propertyName)));
        return getText(message);
    }

    @Step("QF verify choice is selected...")
    public boolean isChoiceSelected(String propertyValue, String choiceName) {
        WebElement choice = findElementWithWait(By.xpath(choiceChecked.replace("{propName}", propertyValue).replace("{choice}", choiceName)));
        return isElementVisible(choice);
    }

    @Step("QF verify choice is not selected...")
    public boolean isChoiceNotSelected(String propertyValue, String choiceName) {
        DriverFactory.sleep(2000);// Need to improve
        WebElement choice = findElementWithWait(By.xpath(choiceUnchecked.replace("{propName}", propertyValue).replace("{choice}", choiceName)));
        return isElementVisible(choice);
    }

    @Step("QF getting number of hidden property filter sections is present...")
    public int getNumberOfHiddenPropertyFilters() {
        return propertyHiddenFilters.size();
    }

    @Step("Getting choice properties in order")
    public List<String> getOrderOfChoiceProperties() {
        List<String> props = new ArrayList<>();
        for (WebElement element : choiceProperties) {
            props.add(element.getAttribute("title"));
        }
        return props;
    }

    @Step("Getting order of quick filter categories")
    public List<String> getOrderOfFilterCategories() {
        List<String> categories = new ArrayList<>();
        List<WebElement> elements = waitForElementsToBeVisible(filterCategories);
        for (WebElement element : elements) {
            categories.add(element.getAttribute("title"));
        }
        return categories;
    }

    @Step("Getting order of quick filter categories")
    public List<String> getOrderOfFilterOptions(String propName) {
        List<String> categories = new ArrayList<>();
        List<WebElement> elements = findElements(By.xpath(filterOptions.replace("{propName}", propName)));

        for (WebElement element : elements) {
            categories.add(element.getAttribute("title"));
        }
        return categories;
    }

    @Step("Get number of selected property options from badge...")
    public String getNumberOfSelectedPropValuesOnBadge(String propName) {
        WebElement badge = findElementWithWait(By.xpath(selectedPropOptionsCount.replace("{prop}", propName)));
        return badge.getText();
    }

    @Step("Collapse property filter...")
    public void collapsePropertyFilter(String propertyName) {
        WebElement collapseButton = findElementWithWait(By.xpath(collapsePropFilterBtn.replace("{propName}", propertyName)));
        click(collapseButton);
    }

    @Step("Expand property filter...")
    public void expandPropertyFilter(String propertyName) {
        WebElement expandButton = findElementWithWait(By.xpath(expandPropFilterBtn.replace("{propName}", propertyName)));
        click(expandButton);
    }

    @Step("Clear filter search field...")
    public void clearSearchField() {
        click(clearSearchBtn);
    }

    @Step("Clear all applied quick filters...")
    public void clearAllQuickFilters() {
        click(findElementWithWait(By.xpath(clearAllFiltersButton)));
    }

    @Step("Verify Clear all filters button not displayed...")
    public boolean clearAllFiltersNotDisplayed() {
        return driver.findElements(By.xpath(clearAllFiltersButton)).isEmpty();
    }

    @Step("Hover over Clear all filters button...")
    public void hoverOverClearAllFiltersBtn() {
        hoverOverElement(findElementWithWait(By.xpath(clearAllFiltersButton)));
    }

    @Step("Click on clear filter button...")
    public void clearFilter(String filterCategory) {
        WebElement clearFilterBtn = findElementWithWait(By.xpath(clearFilter.replace("{filter}", filterCategory)));
        click(clearFilterBtn);
    }

    @Step("Hover over Clear all filters button...")
    public void hoverOverClearFiltersBtn(String filterCategory) {
        WebElement clearFilterBtn = findElementWithWait(By.xpath(clearFilter.replace("{filter}", filterCategory)));
        hoverOverElement(clearFilterBtn);
    }

    @Step("Collapse all filter categories...")
    public void collapseAllFilters() {
        click(collapseAllFilters);
    }

    @Step("Expand all filter categories...")
    public void expandAllFilters() {
        click(expandAllFilters);
    }

    @Step("No labels displayed when filter is collapsed...")
    public boolean areLabelsCollapsed() {
        List<WebElement> filterLabels = driver.findElements(By.xpath(labels));
        return getNumberOfVisibleElements(filterLabels) == 0;
    }

    @Step("No libraries displayed when filter is collapsed...")
    public boolean areLibrariesCollapsed() {
        return getNumberOfVisibleElements(libraries) == 0;
    }

    @Step("No property values displayed when filter is collapsed...")
    public boolean arePropertyValuesCollapsed(String propName) {
        List<WebElement> values = driver.findElements(By.xpath(propertyValues.replace("{prop}", propName)));
        return getNumberOfVisibleElements(values) == 0;
    }

    @Step("Verify `Search returned no results.` alert is displayed...")
    public boolean searchReturnedNoResultsAlertPresent() {
        return isElementVisible(noSearchResultsAlert);
    }

    @Step("Wait for filter category spinner to disappear...")
    public void waitForFilterOptionsToLoad() {
        waitForNumberOfElementsToBe(By.xpath(filterCategorySpinner), 0);
    }

    @Step("Wait for filter category spinner to disappear. Use custom timeout...")
    public void waitForFilterOptionsToLoad(Duration duration) {
        waitForNumberOfElementsToBe(By.xpath(filterCategorySpinner), 0, duration);
    }

    @Step("Open filter profile menu...")
    public void openFilterProfileMenu() {
        click(filterProfileDropdown);
    }

    @Step("Verify filter profile is selected...")
    public String verifyFilterProfileSelected() {
        return profileSelected.getText();
    }

    @Step("Getting name of filter profile selected in menu...")
    public String getSelectedProfileNameInMenu() {
        return profileSelectedInMenu.getText();
    }

    @Step("Getting list of profiles...")
    public List<String> getFilterProfiles() {
        List<String> profiles = new ArrayList<>();
        List<WebElement> elements = waitForElementsToBeVisible(filterProfiles);
        for (WebElement element : elements) {
            profiles.add(element.getText());
        }
        return profiles;
    }

    @Step("Select quick filter profile...")
    public void selectQuickFilterProfile(String profile) {
        WebElement qfProfile = findElementWithFluentWait(By.xpath(quickFilterProfileSelection.replace("{profiles}", profile)));
        click(qfProfile);
    }

    @Step("Get number of visible quick filter options...")
    public String getNumberOfVisibleOptions(String propName) {
        WebElement visibleOptions = findElementWithFluentWait(By.xpath(xpathVisibleFilterOptions.replace("{propName}", propName)));
        return getAttributeValue(visibleOptions, "data-display-options-count");
    }

    @Step("Get multi select properties sfom value...")
    public String getDynamicQuickFilterDataSfom(String propName) {
        DriverFactory.sleep(2000); // need improvement
        WebElement dataSfom = findElementWithFluentWait(By.xpath(xpathPropertiesDataSfom.replace("{propName}", propName)));
        return getAttributeValue(dataSfom, "data-sfom");
    }

    @Step("Click on checked library checkbox...")
    public void clickOnCheckedLibraryCheckBox(String libName) {
        WebElement library = findElementWithWait(By.xpath(checkedLibraryBox.replace("{lib}", libName)));
        click(library);
    }
}
