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
package com.methodics.phi.pageobject.column_templates;

import static com.methodics.phi.actions.WebElementActions.PageModelName.none;
import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TemplatesSelectorPage extends BasePage {
    private final WebDriver driver;

    public TemplatesSelectorPage(WebDriver driver) {
        this.driver = driver;
    }

    // Use data-testid/CSS selectors for stable element targeting
    @FindBy(css = "button[data-testid='ui-btn-template-edit']")
    private List<WebElement> editButton;

    @FindBy(css = "button[data-testid='ui-btn-template-edit'][aria-hidden='false']")
    private WebElement activeEditButton;

    @FindBy(css = "button[data-testid='ui-btn-template-save-update'][aria-hidden='false']")
    private List<WebElement> updateButton;

    @FindBy(xpath = "//div[@class='tooltip-inner']")
    private WebElement tooltip;


    @FindBy(xpath = "//div[contains(@class, 'modal-dialog')]//button[normalize-space(text())='Cancel']")
    private WebElement cancelButton;

    private final String templatesSearchField = "//div[contains(@class, 'template-selector__search')]//input[@data-testid='ui-in-input-field-input-value']";
    private final String clearSearchButton = "//div[contains(@class, 'template-selector__dropdown__button')]//div[contains(@class, 'template-selector__search')]//i[contains(@class, 'base-input-field__clear-icon-inside')]";
    private final String allTemplateList = "//div[contains(@class, 'template-selector__content')]/li[@data-testid='ui-dropdown-item']";
    private final String getTemplatesCount = "//div[contains(@class, 'template-selector__content')]//li[@data-testid='ui-dropdown-item']//*[contains(@class, 'menu-item-text')]";
    private final String createNewTemplate = "//button[contains(@class, 'template-selector__create-btn')]";
    private final String templateSelector = "//div[contains(@class, 'template-selector__dropdown__button')]/button";
    private final String template = "//*[@data-testid='ui-btn-menu-item']//span[contains(@class,'menu-item-text') and normalize-space()='{name}']";
    private final String selectedTemplate = "//*[contains(@class,'template-selector')]//*[@data-testid='ui-dropdown-btn-toggle']//span[normalize-space()='{name}']";
    private final String defaultTemplate = "//*[@data-testid='ui-btn-menu-item']//span[contains(@class,'menu-item-text') and normalize-space()='{name}']/following-sibling::span[normalize-space()='Default']";
    private final String privateTemplate = "//*[@data-testid='ui-btn-menu-item' and contains(@class,'private')]//span[contains(@class,'menu-item-text') and normalize-space()='{name}']";
    private final String publicTemplate = "//*[@data-testid='ui-btn-menu-item'  and contains(@class,'public')]//span[contains(@class,'menu-item-text') and normalize-space()='{name}']";
    private final String TemplateSelectorButton = "//*[contains(@class,'template-selector__dropdown__button')]";
    private final String xpathTemplateSelector = TemplateSelectorButton + "//span[@title='{name}'][contains(@class, 'truncate')]";
    private final String noSearchResults = "//div[normalize-space()='{message}']";

    @Step("Hover over templates dropdown...")
    public void hoverOverTemplatesButton() {
        hoverOverTemplatesButton(none);
    }

    @Step("Hover over templates dropdown...")
    public void hoverOverTemplatesButton(PageModelName modelNameForId) {
        final String elementSelector = getDataTestId(modelNameForId);
        final WebElement templatesButton = findElementWithWait(By.xpath(elementSelector + templateSelector));
        hoverOverElement(templatesButton);
    }

    @Step("Get tooltip...")
    public String getTooltipText() {
        return getText(tooltip);
    }

    @Step("Click on template selector...")
    public void openTemplatesDropdown(PageModelName modelNameForId) {
        waitForPageLoaded();
        final String elementSelector = (getDataTestId(modelNameForId) + templateSelector).trim();
        waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(elementSelector))).click();
        DriverFactory.sleep(1000);
    }

    @Step("Click on template selector...")
    public void openTemplatesDropdown() {
        openTemplatesDropdown(none);
    }

    @Step("Verify templates button is not displayed...")
    public boolean noTemplatesCreated() {
        return getNumberOfElements(driver.findElements(By.xpath(templateSelector))) == 0;
    }

    @Step("Check if template '{name}' is selected (default context)...")
    public boolean isTemplateSelected(String name) {
        return isTemplateSelected(name, none);
    }

    @Step("Check if template '{name}' is selected in context '{modelNameForId}'...")
    public boolean isTemplateSelected(String name, PageModelName modelNameForId) {
        final String elementSelector = (getDataTestId(modelNameForId) + xpathTemplateSelector).trim(); // add model test dataid prefix if needed
        waitForPageLoaded();
        final String dropdown = "(" + (getDataTestId(modelNameForId) + TemplateSelectorButton + ")[last()]").trim();  // if there are multiple selectors, target the last one which is usually the active one
        waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(dropdown)));
        DriverFactory.sleep(2000); // Scope to improve wait stability
        return isElementVisible(elementSelector.replace("{name}", name));
    }

    @Step("Hover over template...")
    public void hoverOverTemplate(String name) {
        hoverOverTemplate(name, none);
    }

    @Step("Hover over template...")
    public void hoverOverTemplate(String name, PageModelName modelNameForId) {
        final String elementSelector = getDataTestId(modelNameForId);
        final WebElement templateName = findElementWithWait(By.xpath(elementSelector + template.replace("{name}", name)));
        hoverOverElement(templateName);
    }

    @Step("Select template...")
    public void selectTemplate(String name) {
        selectTemplate(name, none);
    }

    @Step("Select template...")
    public void selectTemplate(String name, PageModelName modelNameForId) {
        openTemplatesDropdown(modelNameForId);
        final String elementSelector = getDataTestId(modelNameForId);
        final WebElement templateName = waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(elementSelector + template.replace("{name}", name))));
        hoverOverElement(templateName);
        waitTillClickableWithFluentWait(templateName).click();
        DriverFactory.sleep(2000);
    }

    @Step("Click on Edit button...")
    public ColumnTemplateModalPage clickOnEditButton() {
        waitTillClickableWithFluentWait(activeEditButton).click();
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Hover over Edit dropdown...")
    public void hoverOverEditButton() {
        hoverOverElement(activeEditButton);
    }

    @Step("Verify Edit button is disabled...")
    public boolean isEditButtonNotPresent(String tempName) {
        return isEditButtonNotPresent(tempName, none);
    }

    @Step("Verify Edit button is disabled...")
    public boolean isEditButtonNotPresent(String tempName, PageModelName modelNameForId) {
        hoverOverTemplate(tempName, modelNameForId);
        return getNumberOfVisibleElements(editButton) == 0;
    }

    @Step("Click on Update button...")
    public ColumnTemplateModalPage clickOnUpdateButton() {
        clickOnVisibleElement(updateButton);
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Hover over Update dropdown...")
    public void hoverOverUpdateButton() {
        hoverOverVisibleElement(updateButton);
    }

    @Step("Verify Update button is disabled...")
    public boolean isUpdateButtonNotPresent(String tempName) {
        return isUpdateButtonNotPresent(tempName, none);
    }

    @Step("Verify Update button is disabled...")
    public boolean isUpdateButtonNotPresent(String tempName, PageModelName modelNameForId) {
        hoverOverTemplate(tempName, modelNameForId);
        return getNumberOfVisibleElements(updateButton) == 0;
    }

    @Step("Verify template is not present...")
    public boolean templateNotDisplayed(String name) {
        return templateNotDisplayed(name, none);
    }

    @Step("Verify template is not present...")
    public boolean templateNotDisplayed(String name, PageModelName modelNameForId) {
        final String elementSelector = getDataTestId(modelNameForId);
        return findElementsWithWait(By.xpath(elementSelector + template.replace("{name}", name)), 0).size() == 0;
    }

    @Step("Verify template is selected...")
    public boolean isTemplateMarkedAsSelected(String name) {
        return isTemplateMarkedAsSelected(name, none);
    }

    @Step("Verify template is selected...")
    public boolean isTemplateMarkedAsSelected(String name, PageModelName modelNameForId) {
        final String elementSelector = getDataTestId(modelNameForId) + selectedTemplate;
        final WebElement template = findElementWithFluentWait(By.xpath(elementSelector.replace("{name}", name)));
        return isElementVisible(template);
    }

    @Step("Verify template is set to default...")
    public boolean isTemplateDefault(String name) {
        return isTemplateDefault(name, none);
    }

    @Step("Verify template is set to default...")
    public boolean isTemplateDefault(String name, PageModelName modelNameForId) {
        final String defaultTemplateLocator = getDataTestId(modelNameForId) + defaultTemplate;
        final WebElement template = waitForElementToBePresentFluentWait(By.xpath(defaultTemplateLocator.replace("{name}", name)));
        return isElementVisible(template);
    }

    @Step("Verify template is not set to default...")
    public boolean templateIsNotDefault(String name, PageModelName modelNameForId) {
        final String defaultTemplateLocator = getDataTestId(modelNameForId) + defaultTemplate;
        return !isElementVisible(defaultTemplateLocator.replace("{name}", name));
    }
    @Step("Verify template is not set to default...")
    public boolean templateIsNotDefault(String name) {
        return templateIsNotDefault(name, none);
    }

    @Step("Verify template is marked as public...")
    public boolean isTemplateMarkedAsPublic(String name) {
        return isTemplateMarkedAsPublic(name, none);
    }

    @Step("Verify template is marked as public...")
    public boolean isTemplateMarkedAsPublic(String name, PageModelName modelNameForId) {
        try {
            final String elementSelector = getDataTestId(modelNameForId) + publicTemplate;
            final WebElement template = findElementWithWait(By.xpath(elementSelector.replace("{name}", name)));
            return isElementVisible(template);
        } catch (TimeoutException e) {
            logger.info("Template is not public");
            return false;
        }
    }


    @Step("Verify template is marked as private...")
    public boolean isTemplateMarkedAsPrivate(String name) {
        return isTemplateMarkedAsPrivate(name, none);
    }

    @Step("Verify template is marked as private...")
    public boolean isTemplateMarkedAsPrivate(String name, PageModelName modelNameForId) {
        final String elementSelector = getDataTestId(modelNameForId) + privateTemplate;
        return isElementVisible(elementSelector.replace("{name}", name));
    }

    @Step("Click on Create new template button...")
    public ColumnTemplateModalPage clickOnCreateNewTemplate() {
        return clickOnCreateNewTemplate(none);
    }

    @Step("Click on Create new template button...")
    public ColumnTemplateModalPage clickOnCreateNewTemplate(PageModelName modelNameForId) {
        final String elementSelector = (getDataTestId(modelNameForId) + createNewTemplate).trim();
        final WebElement createButton = findElementWithFluentWait(By.xpath(elementSelector));
        hoverOverCreateTemplatesButton(modelNameForId);
        waitForElementToBeClickable(createButton).click();
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Hover over Create templates button...")
    public void hoverOverCreateTemplatesButton() {
        hoverOverCreateTemplatesButton(none);
    }

    @Step("Hover over Create templates button...")
    public void hoverOverCreateTemplatesButton(PageModelName modelNameForId) {
        final String elementSelector = (getDataTestId(modelNameForId) + createNewTemplate).trim();
        final WebElement createTemplatesBtn = findElementWithWait(By.xpath(elementSelector));
        hoverOverElement(createTemplatesBtn);
    }

    @Step("Open create template modal...")
    public ColumnTemplateModalPage openCreateTemplateModal(String name) {
        return openCreateTemplateModal(name, none);
    }

    @Step("Open create template modal...")
    public ColumnTemplateModalPage openCreateTemplateModal(String name, PageModelName modelNameForId) {
        final ColumnTemplateModalPage columnTemplateModalPage = clickOnCreateNewTemplate(modelNameForId);
        columnTemplateModalPage.enterTemplateName(name);
        DriverFactory.sleep(1500);
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Create private template...")
    public ColumnTemplateModalPage createTemplate(String name) {
        return createTemplate(name, none);
    }

    @Step("Create private template...")
    public ColumnTemplateModalPage createTemplate(String name, PageModelName modelNameForId) {
        final ColumnTemplateModalPage columnTemplateModalPage = clickOnCreateNewTemplate(modelNameForId);
        columnTemplateModalPage.enterTemplateName(name);
        columnTemplateModalPage.clickOnCreateButton();
        waitForPageLoaded();
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Create public template...")
    public void createPublicTemplate(String name) {
        createPublicTemplate(name, none);
    }

    @Step("Create public template...")
    public void createPublicTemplate(String name, PageModelName modelNameForId) {
        final ColumnTemplateModalPage columnTemplateModalPage = clickOnCreateNewTemplate(modelNameForId);
        columnTemplateModalPage.enterTemplateName(name);
        columnTemplateModalPage.checkPublicCheckbox();
        columnTemplateModalPage.clickOnCreateButton();
    }

    @Step("Create default template...")
    public void createDefaultTemplate(String name) {
        createDefaultTemplate(name, none);
    }

    @Step("Create default template...")
    public void createDefaultTemplate(String name, PageModelName modelNameForId) {
        final ColumnTemplateModalPage columnTemplateModalPage = clickOnCreateNewTemplate(modelNameForId);
        columnTemplateModalPage.enterTemplateName(name);
        columnTemplateModalPage.checkPublicCheckbox();
        columnTemplateModalPage.checkDefaultCheckbox();
        columnTemplateModalPage.clickOnCreateButton();
    }

    @Step("Open Edit Template modal dialog...")
    public ColumnTemplateModalPage openEditTemplateModal(String templateName) {
        return openEditTemplateModal(templateName, none);
    }

    @Step("Open Edit Template modal dialog...")
    public ColumnTemplateModalPage openEditTemplateModal(String templateName, PageModelName modelNameForId) {
        openTemplatesDropdown(modelNameForId);
        hoverOverTemplate(templateName, modelNameForId);
        clickOnEditButton();
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Open Edit Template modal dialog...")
    public ColumnTemplateModalPage openUpdateTemplateModal(String templateName) {
        return openUpdateTemplateModal(templateName, none);
    }

    @Step("Open Edit Template modal dialog...")
    public ColumnTemplateModalPage openUpdateTemplateModal(String templateName, PageModelName modelNameForId) {
        openTemplatesDropdown(modelNameForId);
        hoverOverTemplate(templateName, modelNameForId);
        clickOnUpdateButton();
        return new ColumnTemplateModalPage(driver);
    }

    @Step("Delete template...")
    public void deleteTemplate(String templateName) {
        deleteTemplate(templateName, none);
    }

    @Step("Delete template...")
    public void deleteTemplate(String templateName, PageModelName modelNameForId) {
        final ColumnTemplateModalPage columnTemplateModalPage = openEditTemplateModal(templateName, modelNameForId);
        columnTemplateModalPage.clickOnDeleteButton();
        columnTemplateModalPage.clickOnDeleteButton();
    }

    @Step("Enter template name into the search field...")
    public void searchForTemplate(String text) {
        searchForTemplate(text, none);
    }

    @Step("Enter template name into the search field...")
    public void searchForTemplate(String text, PageModelName modelNameForId) {
        try {
            waitForUiModule();
            final String searchFieldLocator = getDataTestId(modelNameForId) + templatesSearchField;
            final WebElement searchField = waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(searchFieldLocator)));
            waitTillClickableWithFluentWait(searchField).click();
            clearInputField(searchField);
            inputText(searchField, text);
            waitForPageLoaded();
            logger.info("Successfully searched for template: {}", text);
        } catch (Exception e) {
            logger.error("Failed to search for template: {}. Error: {}", text, e.getMessage());
            throw e;
        }
    }

    @Step("Click on clear templates search button...")
    public void clickOnClearSearchButton() {
        clickOnClearSearchButton(none);
    }

    @Step("Click on clear templates search button...")
    public void clickOnClearSearchButton(PageModelName modelNameForId) {
        try {
            final String searchFieldLocator = getDataTestId(modelNameForId) + templatesSearchField;
            final String clearButtonLocator = getDataTestId(modelNameForId) + clearSearchButton;

            waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(searchFieldLocator)));
            final WebElement clearButton = waitTillVisibleWithFluentWait(findElementWithFluentWait(By.xpath(clearButtonLocator)));
            waitTillClickableWithFluentWait(clearButton);
            clickWithJS(clearButton);
            waitForPageLoaded();
            logger.info("Successfully clicked clear search button");
        } catch (Exception e) {
            logger.warn("Failed to click clear search button using JS, trying regular click. Error: {}", e.getMessage());
            try {
                final String clearButtonLocator = getDataTestId(modelNameForId) + clearSearchButton;
                final WebElement clearButton = findElementWithFluentWait(By.xpath(clearButtonLocator));
                click(clearButton);
                waitForPageLoaded();
            } catch (Exception ex) {
                logger.error("Failed to clear search field. Error: {}", ex.getMessage());
                throw ex;
            }
        }
    }

    @Step("Clear templates search field input...")
    public void clearTemplatesSearchField() {
        clearTemplatesSearchField(none);
    }

    @Step("Clear templates search field input...")
    public void clearTemplatesSearchField(PageModelName modelNameForId) {
        final String searchFieldLocator = getDataTestId(modelNameForId) + templatesSearchField;
        final WebElement searchField = findElementWithFluentWait(By.xpath(searchFieldLocator));
        waitForElementToBeVisible(searchField);
        clearInputFieldWithBackspace(searchField);
    }

    @Step("Get number of templates...")
    public int getNumberOfTemplates() {
        return getNumberOfTemplates(none);
    }

    /**
     * Gets the number of templates in the dropdown.
     * Returns 0 if no templates are found or if the dropdown is not loaded.
     *
     * @param modelNameForId The page model context for data-testid scoping
     * @return The count of templates, or 0 if none found
     */
    @Step("Get number of templates for context: {modelNameForId}")
    public int getNumberOfTemplates(PageModelName modelNameForId) {
        try {
            final String elementSelector = (getDataTestId(modelNameForId) + getTemplatesCount).trim();
            final List<WebElement> templateElements = findElementsWithFluentWait(By.xpath(elementSelector));

            if (templateElements==null || templateElements.isEmpty()) {
                logger.info("No templates found for context: {}", modelNameForId);
                return 0;
            }

            final List<WebElement> visibleElements = waitForElementsToBeVisible(templateElements);
            final int count = visibleElements.size();
            logger.info("Found {} template(s) for context: {}", count, modelNameForId);
            return count;

        } catch (TimeoutException e) {
            logger.warn("Timeout waiting for templates to be visible for context: {}. Returning 0.", modelNameForId);
            return 0;
        } catch (Exception e) {
            logger.error("Error getting number of templates for context: {}. Error: {}", modelNameForId, e.getMessage());
            return 0;
        }
    }

    @Step("Verify no search results message is present...")
    public boolean noSearchResultsPresent(String text) {
        return isElementVisible(findElementWithWait(By.xpath(noSearchResults.replace("{message}", text))));
    }

    public List<String> getAllTemplateName() {
        return getAllTemplateName(none);
    }

    public List<String> getAllTemplateName(PageModelName modelNameForId) {
        final String allTemplateListLocator = getDataTestId(modelNameForId) + allTemplateList;
        final List<WebElement> templates = findElementsWithFluentWait(By.xpath(allTemplateListLocator));
        final List<String> templateNames = new ArrayList<>();
        for (final WebElement template : templates) {
            templateNames.add(template.getText());
        }
        return templateNames;
    }

    @Step("Click on Cancel button in Create Template modal...")
    public void clickOnCancelCreateTemplateButton() {
        waitForElementToBeClickable(cancelButton).click();
    }
}
