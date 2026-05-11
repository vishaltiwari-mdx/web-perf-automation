
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
package com.methodics.phi.pageobject.ips;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import com.methodics.phi.pageobject.ip_catalog.IpCatalogPage;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PlanningBOMPage extends BasePage {
    private final WebDriver driver;

    public PlanningBOMPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[contains(normalize-space(.),'Create version')]")
    private WebElement createVersionButton;

    @FindBy(xpath = "//div[@data-testid='ui-col']//button[@data-testid='ui-btn-create-version'][contains(@class, 'disabled')][normalize-space()='Create version']")
    private WebElement createVersionButtonDisabled;

    @FindBy(css = "button[data-testid='ui-btn-create-version']")
    private WebElement makeLatestButton;

    @FindBy(xpath = "//div[@data-testid='ui-col']//button[@data-testid='ui-btn-create-version'][contains(@class, 'disabled')][normalize-space()='Make latest']")
    private WebElement makeLatestButtonDisabled;

    @FindBy(xpath = "//*[@class='fa fa-solid fa-plus']")
    private WebElement addPlannedIpButton;

    @FindBy(css = "button[data-testid='ui-btn-planning-bom-add-pip-button'][disabled] i[class*='plus']")
    private WebElement addPlannedIpButtonDisabled;

    @FindBy(xpath = "//span[contains(text(),'IP Name')]")
    private WebElement theIPNameLabel;

    @FindBy(xpath = "//button[normalize-space()='Create Planning BOM']")
    private WebElement createPlanningBom;

    @FindBy(xpath = "//button[contains(@class, 'disabled')][normalize-space()='Create Planning BOM']")
    private WebElement createPlanningBomDisabled;

    @FindBy(xpath = "//span[contains(text(),'IP Description')]")
    private WebElement theIPDescriptionLabel;

    @FindBy(xpath = "//span[contains(text(),'Notes')]")
    private WebElement theNotesLabel;

    @FindBy(xpath = "//div[@class='ag-header-viewport']")
    private WebElement tableHeader;

    @FindBy(css = "button[data-testid='ui-btn-ip-planning-bom-go-to-latest']")
    private WebElement goToLatestLink;

    @FindBy(xpath = "//input[@placeholder='Search for an IP']")
    private WebElement searchAddIPInput;

    @FindBy(xpath = "//input[@placeholder='Search IPs']")
    private WebElement planningBomTabSearchIpsInput;

    @FindBy(xpath = "//button[contains(normalize-space(),'Add')][@role='button save']")
    private WebElement addIPButton;

    @FindBy(css = "button[data-testid='ui-btn-shopping-cart-modal-add'][disabled]")
    private WebElement addIPButtonDisabled;

    @FindBy(xpath = "//a[@href='https://help.perforce.com/helix-iplm/public-latest/latest/Default.htm?cshid=Planning_BOM']")
    private WebElement planningBomHelpButton;

    @FindBy(xpath = "//div[@data-testid='planning-bom-grid']//div[@aria-colindex='1']//span[normalize-space()='Double-click to edit'][contains(@class, 'text-body')]")
    private List<WebElement> emptyRows;

    @FindBy(css = "[data-testid='planning-bom-grid']")
    private WebElement planningBomGrid;

    @FindBy(css = "div[class*='modal-footer'] span[title]")
    private WebElement shoppingCartSelectedIp;

    @FindBy(xpath = "//div[contains(@class,'tab-planning-bom__contents')]/h5")
    private WebElement permissionsCardMessage;

    @FindBy(xpath = "//div[contains(@class,'library-selector')]")
    private WebElement librarySelector;

    @FindBy(css = "span[data-testid='ui-tooltip-html-content']")
    private WebElement xpathTooltip;

    private final String theSelectedVersion = "//li[@class='multiselect__element']//span[contains(text(),'{value}')]";
    private final String thePlannedIPInput = "//div[contains(@class, 'ag-cell-editor')]//input";
    private final String thePlannedDescrIPInput = "//div[contains(@class, 'cell-ip-description')]//input";
    private final String thePlannedNoteIPInput = "//div[contains(@class, 'cell-ip-notes')]//input";
    private final String theDragAndDropIcon = "//div[contains(@class, 'ag-drag-handle')]";
    private final String plannedIpCell = "//div[@row-index='{row_index}']//div[@aria-colindex='{column_index}']";
    private final String ipCellShoppingCart = "//div[@row-index='{row_index}']//div[@col-id='{column_name}']";
    private final String lineInDropdown = "//select//option[@value='{line_value}']";
    private final String autoSaving = "//span[contains(@class, 'fade-enter-active')][normalize-space()='Autosaving...']";
    private final String xpathTheLinkIcon = "//div[@row-index='{row_index}']//div[@aria-colindex='{column_index}']//a";
    private final String findAddedGeos = "//div[@col-id='{geoType}']//span[normalize-space()='{geoShortName}'][@class='text-truncate']";
    private final String gpsIconColor = "//div[@col-id='{geoType}']//*[@data-color='{geoColorHex}']/following-sibling::span[normalize-space()='{geoName}']";
    private final String getGeosCell = "//span[@title='{ipName}']/ancestor::div/div[contains(@col-id, '{geoType}')]";
    private final String plannedIpCellTitle = "//div[@row-index='{row_index}']//div[@col-id='{columnTitle}']//div[contains(@class,'col-title')]//*[(name()='a' or name()='span') and @title]";
    private final String xpathVersionSelect = "//span[@class='multiselect__single' and contains(normalize-space(.),'{version}')]";
    private final String bomBadge = "//div[@row-index='{rowIndex}']//div[@aria-colindex='1']//div[contains(@class, 'ip-badge')]";
    private final String bomErrorBadge = "//div[@row-index='{rowIndex}']//div[@aria-colindex='1']//div[contains(@class,'badge--error')]//div[contains(@class,'ip-badge ip-badge--danger')]";
    private final String countRows = "//span[contains(@class,'planning-bom__title-wrapper') and not(contains(text(),'Double-click to edit'))] | //a[contains(@class,'planning-bom__title-wrapper')][@title]";
    private final String emptyRow = "//div[@row-index='{rowIndex}']//div[@col-id='ag-Grid-AutoColumn']//span[contains(@class, 'text-muted') or contains(@class, 'text-body-secondary')]";
    private final String xpathPipButtons = "//div[@row-index='{row_index}']//div[@aria-colindex='{column_index}']//i[contains(@class,'cell-action--{buttonName}')]";
    private final String collapseButton = "//div[@row-index='{row_index}']//span[@class='ag-group-expanded']//span[contains(@class, 'icon-tree-open')]";
    private final String expandButton = "//div[@row-index='{row_index}']//span[@class='ag-group-contracted']//span[contains(@class, 'icon-tree-close')]";
    private final String xpathSelectedLine = "//select[@data-testid='ui-form-select'][@title='{name}']";
    private final String dropdownLib = "//div[contains(@class, truncate)]/span[@title='{lib}'][@class='library-selector__option-title']";
    private final String theDragAndDropIconHidden = "//div[@row-index='{row_index}']//div[contains(@class, 'ag-drag-handle') and aria-hidden='true']";
    private final String threeDotSubMenuButton = "//*[@row-index='{row_index}']//*[@data-testid='col-sub-menu-btn-container']";
    private final String threeDotSubMenu = "//ul[@class='dropdown-menu popup-menu show']";
    private final String threeDotSubMenuItems = threeDotSubMenu + "/li[contains(normalize-space(.), '{menuOption}')]";


    @Step("Verify Line placeholder displayed ...")
    public String getLinesColumnCellValue(String rowPosition, String columnPosition) {
        final WebElement linePlaceholder = findElementWithWait(By.xpath(ipCellShoppingCart.replace("{row_index}", rowPosition).replace("{column_name}", columnPosition)));
        return getText(linePlaceholder);
    }

    @Step("Get geos cell...")
    public WebElement getGeosCell(String ipName, String geoType) {
        return findElementWithWait(By.xpath(getGeosCell.replace("{ipName}", ipName).replace("{geoType}", geoType)));
    }

    @Step("Click Create Planning Bom ...")
    public void clickCreatePlanningBom() {
        waitForElementToBeClickable(createPlanningBom).click();
    }

    @Step("Click Planning Bom help button ...")
    public void clickPlanningBomHelpButton() {
        click(planningBomHelpButton);
    }

    @Step("Hover over Planning Bom cart help button...")
    public void hoverOverPlanningBomHelpButton() {
        hoverOverElement(planningBomHelpButton);
    }

    @Step("Click Make latest button ...")
    public void clickMakeLatestButton() {
        click(makeLatestButton);
    }

    @Step("Verify new version created ...")
    public Boolean isVersionCreated(String versionText) {
        final WebElement theVersion = findElementWithWait(By.xpath(xpathVersionSelect.replace("{version}", versionText)));
        return isElementVisible(theVersion);
    }

    @Step("Verify if planning BOM page is visible...")
    public Boolean isPlanningBomGridVisible() {
        final WebElement planningBomGridElement = waitTillVisibleWithFluentWait(planningBomGrid);
        return isElementVisible(planningBomGridElement);
    }

    @Step("Click Planning Bom link ...")
    public void clickPlanningBomLink(String rowPosition, String columnPosition) {
        final WebElement theLinkIcon = findElementWithWait(By.xpath(xpathTheLinkIcon.replace("{row_index}", rowPosition).replace("{column_index}", columnPosition)));
        click(theLinkIcon);
    }

    @Step("Verify if Planning Bom link not displayed...")
    public Boolean isPlanningBomLinkNotDisplayed(String rowPosition, String columnPosition) {
        return findElementsWithWait(By.xpath(xpathTheLinkIcon.replace("{row_index}", rowPosition).replace("{column_index}", columnPosition)), 0).size() == 0;
    }

    @Step("Verify create planning bom button visible ...")
    public Boolean isCreatePlanningBomDisplayed() {
        return isElementVisible(createPlanningBom);
    }

    @Step("Verify Create Planning Bom button is disabled...")
    public boolean isCreatePlanningBomDisabled() {
        return isElementVisible(createPlanningBomDisabled);
    }

    @Step("DragAndDrop IP  ...")
    public void dragAndDropIP(String rowSourcePosition, String rowTargerPosition) {
        final String activeCell = plannedIpCell.replace("{row_index}", rowSourcePosition).replace("{column_index}", "1");
        final WebElement sourceCell = findElementWithWait(By.xpath(activeCell + theDragAndDropIcon));

        final String activeCell2 = plannedIpCell.replace("{row_index}", rowTargerPosition).replace("{column_index}", "1");
        final WebElement targetCell = findElementWithWait(By.xpath(activeCell2));

        dragAndDropElement(sourceCell, targetCell);
        // Saving needs more time.
        DriverFactory.sleep(3500);
    }

    @Step("Enter planned IP ...")
    public void enterPlannedIP(String rowPosition, String theIPValue) {
        DriverFactory.sleep(3000);
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1");
        final WebElement editCell = findElementWithFluentWait(By.xpath(activeCell));
        doubleClickOnElement(editCell);

        // Try to double-click, retry if placeholder text still visible
        if ("Double-click to edit".equals(editCell.getText())) {
            // First attempt failed, try once more
            DriverFactory.sleep(500);
            doubleClickOnElement(editCell);
        }

        final WebElement theIP = editCell.findElement(By.xpath(thePlannedIPInput));
        inputText(theIP, theIPValue);
        pressEnter(theIP);
        DriverFactory.sleep(1000);// Need time to ip name display after press.
    }

    @Step("Enter planned IP description...")
    public void enterPlannedIPDescr(String rowPosition, String Value) {
        DriverFactory.sleep(2000); // Need time before entering text.
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "2");
        final WebElement editCell = findElementWithFluentWait(By.xpath(activeCell));
        waitTillClickableWithFluentWait(editCell);
        hoverOverElement(editCell);
        doubleClickOnElement(editCell);

        // Try to double-click, retry if placeholder text still visible
        if ("Double-click to edit".equals(editCell.getText())) {
            // First attempt failed, try once more
            DriverFactory.sleep(500);
            doubleClickOnElement(editCell);
        }

        final WebElement theIP = findElementWithWait(By.xpath(thePlannedDescrIPInput));
        inputText(theIP, Value);
        pressEnter(theIP);
    }

    @Step("Enter planned IP notes...")
    public void enterPlannedIPNotes(String rowPosition, String Value) {
        DriverFactory.sleep(2000);
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "3");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        doubleClickOnElement(editCell);

        // Try to double-click, retry if placeholder text still visible
        if ("Double-click to edit".equals(editCell.getText())) {
            // First attempt failed, try once more
            DriverFactory.sleep(500);
            doubleClickOnElement(editCell);
        }

        final WebElement theIP = findElementWithWait(By.xpath(thePlannedNoteIPInput));
        inputText(theIP, Value);
        pressEnter(theIP);
        // Needed autosave saving data.
        DriverFactory.sleep(2500);
    }

    @Step("Get planned IP ...")
    public String getPlannedIP(String rowPosition) {
        final String activeCell = plannedIpCellTitle.replace("{row_index}", rowPosition).replace("{columnTitle}", "ag-Grid-AutoColumn");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        return editCell.getText().trim();
    }

    @Step("Get planned IP description...")
    public String getPlannedIPDescr(String rowPosition) {
        final WebElement activeCell = waitForElement(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "2")));
        final WebElement editCell = waitForElementToBeVisible(activeCell);
        return editCell.getText();
    }

    @Step("Get planned IP notes...")
    public String getPlannedIPNote(String rowPosition) {
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "3");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        return editCell.getText();
    }

    @Step("Verify add existing IP button displayed...")
    public boolean isAddExistingIPButtonDisplayed(String rowPosition, String buttonName) {
        final WebElement editCell = waitForElement(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1")));
        waitForElementToBeClickable(editCell);
        hoverOverElement(editCell);
        final WebElement addExistingIPButton = findElementWithWait(By.xpath(xpathPipButtons.replace("{row_index}", rowPosition).replace("{column_index}", "1").replace("{buttonName}", buttonName)));
        return isElementVisible(addExistingIPButton);
    }

    @Step("Get the number of geos...")
    public int getNumberOfGeos(String geoType, String geoShortName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(findAddedGeos.replace("{geoType}", geoType).replace("{geoShortName}", geoShortName)), counter).size();
    }

    @Step("Get number of IPs...")
    public int getNumberOfIps(int counter) {
        return waitForNumberOfElementsToBe(By.xpath(countRows), counter).size();
    }

    @Step("Clicking on Create Version button...")
    public void clickOnCreateVesion() {
        click(createVersionButton);
    }

    @Step("Insert text in search for an IP...")
    public void addTextInSearchInput(String nameIP) {
        inputText(searchAddIPInput, nameIP);
    }

    @Step("Insert text in search for an Planning Bom IPs...")
    public void searchPlanningBomIps(String ipName) {
        inputText(planningBomTabSearchIpsInput, ipName);
        // Need time to load
        DriverFactory.sleep(1000);
    }

    @Step("Open Shopping Chart ..")
    public void openShoppingChart(String rowPosition, String buttonName) {
        final WebElement editCell = findElementWithWait(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1")));
        hoverOverElement(editCell);
        final WebElement addExistingIPButton = findElementWithWait(By.xpath(xpathPipButtons.replace("{row_index}", rowPosition).replace("{column_index}", "1").replace("{buttonName}", buttonName)));
        click(addExistingIPButton);
    }

    @Step("Adding  existing IP ..")
    public void addExistingIP(String rowPosition, String nameIP, String lineName, String buttonName) {
        final WebElement editCell = findElementWithWait(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1")));
        hoverOverElement(editCell);
        final WebElement addExistingIPButton = findElementWithWait(By.xpath(xpathPipButtons.replace("{row_index}", rowPosition).replace("{column_index}", "1").replace("{buttonName}", buttonName)));
        click(addExistingIPButton);
        inputText(searchAddIPInput, nameIP);
        final String activeIPCell = ipCellShoppingCart.replace("{row_index}", "0").replace("{column_name}", "selectedLine");
        // we need to keep sleeps here because everything is happening too fast for selenium
        DriverFactory.sleep(1000);
        final WebElement currNameIP = findElementWithWait(By.xpath(activeIPCell));
        click(currNameIP);
        DriverFactory.sleep(1000);
        final String activeLineCell = ipCellShoppingCart.replace("{row_index}", "0").replace("{column_name}", "selectedLine");
        final WebElement selectorLine = findElementWithWait(By.xpath(activeLineCell));
        click(selectorLine);

        final String activeLine = lineInDropdown.replace("{line_value}", lineName);
        final WebElement currLine = findElementWithFluentWait(By.xpath(activeLine));
        waitForElementToBeClickable(currLine);
        click(currLine);

        clickOnAddButton();
        waitForPageToLoad(driver.getCurrentUrl());
        DriverFactory.sleep(2000); // for stable test execution, need to scope for improvement
    }

    @Step("Clicking on particular version...")
    public void clickOnParticularVersion(String versionText) {
        final WebElement versionSelect = findElementWithWait(By.xpath(xpathVersionSelect.replace("{version}", "Latest")));
        click(versionSelect);
        final WebElement theVersion = findElementWithWait(By.xpath(theSelectedVersion.replace("{value}", versionText)));
        click(theVersion);
        DriverFactory.sleep(3000);
    }

    @Step("Clicking on  Go to Latest link...")
    public void clickOnGoToLatest() {
        click(goToLatestLink);
    }

    @Step("Clicking on Add Planned IP button...")
    public void clickOnAddPlannedIpButton() {
        click(addPlannedIpButton);
    }

    @Step("Waiting for loading Plannig BOM table content...")
    public void waitForTableLoad() {
        waitTillClickableWithFluentWait(addPlannedIpButton);
    }

    @Step("Verifying if Versions select is present...")
    public boolean isVersionSelectPresent() {
        final WebElement versionSelect = findElementWithWait(By.xpath(xpathVersionSelect.replace("{version}", "Latest")));
        return isElementVisible(versionSelect);
    }

    @Step("Verifying if Create Version button is present...")
    public boolean isCreateVersionButtonPresent() {
        return isElementVisible(createVersionButton);
    }

    @Step("Verifying Add IP button is disabled...")
    public boolean isAddIpButtonDisabled() {
        return isElementVisible(addIPButtonDisabled);
    }

    @Step("Verifying if BOM table header is present...")
    public boolean isTableHeaderPresent() {
        return isElementVisible(tableHeader);
    }

    @Step("Verifying if Go to Latest link is present...")
    public boolean isGoToLatestPresent() {
        return isElementVisible(goToLatestLink);
    }

    @Step("Verifying Add Planned IP button is disabled...")
    public boolean isAddPlannedIpButtonDisabled() {
        return isElementVisible(addPlannedIpButtonDisabled);
    }

    @Step("Verifying Add Planned IP button is visible...")
    public WebElement isAddPlannedIpButtonVisible() {
        return waitTillVisibleWithFluentWait(addPlannedIpButton);
    }

    @Step("Verify geo icon color...")
    public boolean isGpsIconColorDisplayed(String geoType, String geoName, String geoIconColorHex) {
        final WebElement gpsColor = waitForElementToBePresent(By.xpath(gpsIconColor.replace("{geoType}", geoType).replace("{geoName}", geoName).replace("{geoColorHex}", geoIconColorHex)));
        return isElementVisible(gpsColor);
    }

    @Step("Wait for Autosaving message to disappear...")
    public void waitForChangesToBeSaved() {
        waitForNumberOfElementsToBe(By.xpath(autoSaving), 0);
    }

    @Step("Open Planning Bom Catalog ...")
    public IpCatalogPage openPlanningBomCatalog() {
        waitForTableLoad();
        waitTillVisibleWithFluentWait(planningBomGrid);
        final String activeCell = plannedIpCell.replace("{row_index}", "0").replace("{column_index}", "1");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        hoverOverElement(editCell);
        final WebElement addExistingIPButton = findElementWithWait(By.xpath(xpathPipButtons.replace("{row_index}", "0").replace("{column_index}", "1").replace("{buttonName}", "link")));
        click(addExistingIPButton);
        DriverFactory.sleep(500);
        return new IpCatalogPage(driver);
    }

    @Step("Get cell value...")
    public String getCellValue(String rowIndex, String columnTitle) {
        final WebElement activeCellElement = findElementWithWait(By.xpath(plannedIpCellTitle
                .replace("{row_index}", rowIndex).replace("{columnTitle}", columnTitle)));
        return activeCellElement.getAttribute("title");
    }

    @Step("Get badge name...")
    public String getBomBadgeName(String row) {
        final WebElement badge = findElementWithWait(By.xpath(bomBadge.replace("{rowIndex}", row)));
        return getText(badge);
    }

    @Step("Hover over error badge...")
    public void hoverOverIpBadge(String row) {
        final WebElement badge = findElementWithWait(By.xpath(bomBadge.replace("{rowIndex}", row)));
        hoverOverElement(badge);
    }

    @Step("Verify that error badge is present...")
    public boolean isErrorBadgePresent(String row) {
        final WebElement badge = findElementWithWait(By.xpath(bomErrorBadge.replace("{rowIndex}", row)));
        return isElementVisible(badge);
    }

    @Step("Hover over error badge...")
    public void hoverOverErrorBadge(String row) {
        final WebElement badge = findElementWithWait(By.xpath(bomErrorBadge.replace("{rowIndex}", row)));
        hoverOverElement(badge);
    }

    @Step("Hover over Add Planned IP button...")
    public void hoverOverAddPlannedIpButton() {
        hoverOverElement(addPlannedIpButton);
    }

    @Step("Get number of empty rows ...")
    public int getNumberOfEmptyRows() {
        return getNumberOfVisibleElements(emptyRows);
    }

    @Step("Get empty row muted text...")
    public String getEmptyRowText(String rowIndex) {
        final WebElement row = findElementWithWait(By.xpath(emptyRow.replace("{rowIndex}", rowIndex)));
        return getText(row);
    }

    @Step("Get shopping cart selected ip...")
    public String getPreSelectedIp() {
        return getText(shoppingCartSelectedIp);
    }

    @Step("Get shopping cart selected IP title...")
    public String getPreSelectedIpTitle() {
        return getAttributeValue(shoppingCartSelectedIp, "title");
    }

    @Step("Verifying that ip interaction button is not available...")
    public boolean isIpInteractionButtonNotAvailable(String rowPosition, String buttonName) {
        final WebElement editCell = findElementWithWait(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1")));
        hoverOverElement(editCell);
        DriverFactory.sleep(1000);
        return getNumberOfVisibleElements(driver.findElements(By.xpath(xpathPipButtons.replace("{row_index}", rowPosition).replace("{column_index}", "1").replace("{buttonName}", buttonName)))) == 0;
    }

    @Step("Clicking on pip button...")
    public void interactWithPip(String rowPosition, String buttonName) {
        final WebElement editCell = findElementWithWait(By.xpath(plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1")));
        hoverOverElement(editCell);
        DriverFactory.sleep(2000);
        final WebElement pipButton = findElementWithWait(By.xpath(xpathPipButtons.replace("{row_index}", rowPosition).replace("{column_index}", "1").replace("{buttonName}", buttonName)));
        click(pipButton);
        DriverFactory.sleep(3500);
    }

    @Step("Clicking on Add button in shopping cart...")
    public void clickOnAddButton() {
        click(addIPButton);
    }

    @Step("Verify description field is not editable...")
    public boolean descriptionFieldNotEditable(String rowPosition) {
        DriverFactory.sleep(1000);
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "2");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        doubleClickOnElement(editCell);
        DriverFactory.sleep(1000);
        return getNumberOfElements(driver.findElements(By.xpath(thePlannedDescrIPInput))) == 0;
    }

    @Step("Notes field is not editable...")
    public boolean notesFieldNotEditable(String rowPosition) {
        DriverFactory.sleep(1000);
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "3");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        doubleClickOnElement(editCell);
        DriverFactory.sleep(1000);
        return getNumberOfElements(driver.findElements(By.xpath(thePlannedNoteIPInput))) == 0;
    }

    @Step("Name field is not editable...")
    public boolean nameFieldNotEditable(String rowPosition) {
        DriverFactory.sleep(1000);
        final String activeCell = plannedIpCell.replace("{row_index}", rowPosition).replace("{column_index}", "1");
        final WebElement editCell = findElementWithWait(By.xpath(activeCell));
        doubleClickOnElement(editCell);
        DriverFactory.sleep(1000);
        return getNumberOfElements(driver.findElements(By.xpath(thePlannedIPInput))) == 0;
    }

    @Step("Get permissions card message...")
    public String getPermissionCardMessage() {
        return getText(permissionsCardMessage);
    }

    @Step("Collapse tree...")
    public void collapseTree(String rowIndex) {
        click(findElementWithWait(By.xpath(collapseButton.replace("{row_index}", rowIndex))));
    }

    @Step("Expand tree...")
    public void expandTree(String rowIndex) {
        click(findElementWithWait(By.xpath(expandButton.replace("{row_index}", rowIndex))));
    }

    @Step("Verify line column value is selected...")
    public boolean isLineSelected(String line) {
        final WebElement selectedLine = findElementWithWait(By.xpath(xpathSelectedLine.replace("{name}", line)));
        return isElementVisible(selectedLine);
    }

    @Step("Select library from dropdown...")
    public void selectLibrary(String library) {
        openLibrariesDropdown();
        clickOnLibrary(library);
    }

    @Step("Click on line selector of planning BOM catalog...")
    public void clickOnLineSelector(String rowIndex, String columnName) {
        final WebElement lineDropdown = findElementWithWait(By.xpath(ipCellShoppingCart
                .replace("{row_index}", rowIndex)
                .replace("{column_name}", columnName)));
        doubleClickOnElement(lineDropdown);
    }

    @Step("Select a line from the line selector of planning BOM catalog...")
    public void selectLineFromLineSelector(String lineName) {
        final String xpathLine = lineInDropdown.replace("{line_value}", lineName);
        final WebElement lineToSelect = findElementWithWait(By.xpath(xpathLine));
        click(lineToSelect);
    }

    @Step("Click on library...")
    public void clickOnLibrary(String libName) {
        final WebElement library = findElementWithWait(By.xpath(dropdownLib.replace("{lib}", libName)));
        click(library);
    }

    @Step("Open Libraries dropdown...")
    public void openLibrariesDropdown() {
        click(librarySelector);
    }

    @Step("Verify that Create version button is visible, but disabled...")
    public boolean isCreateVersionButtonDisabled() {
        return isElementVisible(createVersionButtonDisabled);
    }

    @Step("Verify that Make latest button is visible, but disabled...")
    public boolean isMakeLatestButtonDisabled() {
        return isElementVisible(makeLatestButtonDisabled);
    }

    @Step("Verifying that the version is selected...")
    public boolean isVersionSelected(String versionText) {
        final WebElement theVersion = findElementWithWait(By.xpath(xpathVersionSelect.replace("{version}", versionText)));
        return isElementVisible(theVersion);
    }

    @Step("Verify that drag and drop icon is hidden")
    public boolean isDragAndDropIconHidden(String rowPosition) {
        return getNumberOfElements(driver.findElements(By.xpath(theDragAndDropIconHidden.replace("{row_index}", rowPosition)))) == 0;
    }

    public boolean verifyIfThreeDotSubMenuDisplayed(String row_index) {
        return isElementVisible(threeDotSubMenuButton.replace("{row_index}", row_index));
    }

    public void clickOnThreeDotSubMenu(String row_index) {
        final WebElement threeDotSubMenu = findElementWithWait(By.xpath(threeDotSubMenuButton.replace("{row_index}", row_index)));
        click(threeDotSubMenu);
    }

    public boolean verifySubMenuOptionDisplayed(String menuOption) {
        return isElementVisible(threeDotSubMenuItems.replace("{menuOption}", menuOption));
    }

    public void closeThreeDotSubMenu(String row_index) {
        final WebElement threeDotSubMenu = findElementWithWait(By.xpath(threeDotSubMenuButton.replace("{row_index}", row_index)));
        click(threeDotSubMenu);
    }

    public boolean verifyIfThreeDotSubMenuClose() {
        return isElementNotVisible(threeDotSubMenu);
    }

    public void clickOnThreeDotSubMenuItem(String manuItem) {
        final WebElement threeDotSubMenuItem = findElementWithWait(By.xpath(threeDotSubMenuItems.replace("{menuOption}", manuItem)));
        click(threeDotSubMenuItem);
    }

    @Step("Check if the tooltip with text '{text}' is present")
    public String isTooltipPresent() {
        return getText(xpathTooltip);
    }
}
