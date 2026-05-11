package com.methodics.phi.pageobject.widgets;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class WidgetsSeriesPage extends BasePage {
    private WebDriver driver;

    public WidgetsSeriesPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[text()='Add series']")
    private WebElement addSeries;

    @FindBy(css = "button[data-testid='ui-btn-series-item-name-cancel']")
    private WebElement canselNewSeriesButton;

    @FindBy(xpath = "//button[contains(@class,'ok-button')]")
    private WebElement checkmarkNewSeriesButton;

    @FindBy(css = "input[data-testid='ui-in-series-item-name-input']")
    private WebElement newSeriesNameField;

    @FindBy(css = "button[data-testid='ui-btn-data-editor-save-button']")
    private WebElement saveSeriesDataButton;

    @FindBy(xpath = "//span[@title='Label']")
    private WebElement seriesLabel;

    @FindBy(xpath = "//span[@title='Value']")
    private WebElement seriesValue;

    private String seriesItems = "//div[contains(@class,'series-editor')]/div[contains(@class,'series-item')]";
    private String disabledCheckmarkNewSeriesButton = "//button[contains(@class,'ok-button') and @disabled]";
    private String seriesRow = "//div[@title='{seriesName}']";
    private String deleteSeriesButton = "//div[@title='{seriesName}']/parent::div//i[contains(@class, 'trash')]/parent::button";
    private String renameSeriesButton = "//div[@title='{seriesName}']/parent::div//i[contains(@class, 'fa-pen')]/parent::button";
    private String editSeriesButton = "//div[@title='{seriesName}']/parent::div//i[contains(@class, 'fa-table-list')]/parent::button";
    private String seriesTabButton = "//div[contains(@class, 'justify-content-end')]//button[normalize-space()='{buttonName}']";
    private String inputSeriesData = "//div[@role='row' and @row-index='{rowNumber}']//div[@aria-colindex='{columnNumber}']";
    private String invalidSeriesDataIcon = "//div[@role='row' and @row-index='{rowNumber}']//div[@aria-colindex='{columnNumber}']//i[contains(@class, 'icon')]";
    private String xpathTooltip = "//div[@class='tooltip-inner']";

    @Step("Checking if series label is present in series table...")
    public boolean isSeriesLabelPresent() {
        return isElementVisible(seriesLabel);
    }

    @Step("Verify that expected count of Series are present...")
    public boolean isAppropriateCountOfSeriesPresent(Integer expectedCount) {
        return getNumberOfElements(driver.findElements(By.xpath(seriesItems))) == expectedCount;
    }

    @Step("Click Add new Series button ...")
    public void clickAddSeriesButton() {
        click(addSeries);
    }

    @Step("Verify that checkmark for New Series is disabled...")
    public boolean ischeckmarkNewSeriesButtonDisabled() {
        List<WebElement> size = driver.findElements(By.xpath(disabledCheckmarkNewSeriesButton));
        return size.size() != 0;
    }

    @Step("Click Cancel new series button ...")
    public void clickCancelNewSeriesButton() {
        click(canselNewSeriesButton);
    }

    @Step("Entering series name...")
    public void enterSeriesName(String name) {
        inputText(newSeriesNameField, name);
    }

    @Step("Click Cancel new series button...")
    public void clickCheckmarkNewSeriesButton() {
        click(checkmarkNewSeriesButton);
    }

    @Step("Hovering over series...")
    public void hoverOverSeries(String seriesNames) {
        WebElement series = findElementWithWait(By.xpath(seriesRow.replace("{seriesName}", seriesNames)));
        hoverOverElement(series);
    }

    @Step("Click delete specified series...")
    public void deleteSeriesButton(String seriesNames) {
        hoverOverSeries(seriesNames);
        WebElement item = findElementWithWait(By.xpath(deleteSeriesButton.replace("{seriesName}", seriesNames)));
        click(item);
    }

    @Step("Click rename specified series...")
    public void clickOnRenameSeriesButton(String seriesNames) {
        hoverOverSeries(seriesNames);
        WebElement item = findElementWithWait(By.xpath(renameSeriesButton.replace("{seriesName}", seriesNames)));
        click(item);
    }

    @Step("Click edit specified series...")
    public void clickOnEditSeriesButton(String seriesNames) {
        hoverOverSeries(seriesNames);
        WebElement item = findElementWithWait(By.xpath(editSeriesButton.replace("{seriesName}", seriesNames)));
        click(item);
    }

    @Step("Click specified button in Series Tab...")
    public void clickButtonSeriesTab(String buttonName) {
        WebElement item = findElementWithWait(By.xpath(seriesTabButton.replace("{buttonName}", buttonName)));
        click(item);
    }

    @Step("Click Discard button in Series Tab...")
    public void clickDiscardButtonSeriesTab() {
        clickButtonSeriesTab("Discard");
    }

    @Step("Input text into specified Series Data row and column ...")
    public void inputTextIntoSeriesDataField(String rowNumber, String columnNumber, String value) {
        WebElement item = findElementWithWait(By.xpath(inputSeriesData.replace("{rowNumber}", rowNumber).replace("{columnNumber}", columnNumber)));
        doubleClickOnElement(item);
        WebElement item2 = findElementWithWait(By.xpath(inputSeriesData.replace("{rowNumber}", rowNumber).replace("{columnNumber}", columnNumber) + "//input"));
        inputText(item2, value);
        pressEnterKey(item2);
    }

    @Step("Input text into first column of specified Series Data row ...")
    public void inputTextIntoFirstColumnSeriesDataField(String rowNumber, String value) {
        inputTextIntoSeriesDataField(rowNumber, "1", value);
        DriverFactory.sleep(500);
    }

    @Step("Input text into second column of specified Series Data row ...")
    public void inputTextIntoSecondColumnSeriesDataField(String rowNumber, String value) {
        inputTextIntoSeriesDataField(rowNumber, "2", value);
        DriverFactory.sleep(500);
    }

    @Step("Input text into specified Series Data row ...")
    public void inputTextIntoSpecifiedRowSeriesDataField(String rowNumber, String value1, String value2) {
        inputTextIntoFirstColumnSeriesDataField(rowNumber, value1);
        inputTextIntoSecondColumnSeriesDataField(rowNumber, value2);
    }

    @Step("Check that invalid value icon present for specified Series Data row and column ...")
    public String getSeriesDataInformationIconTooltip(String rowNumber, String columnNumber, Integer iconNumber) {
        List<WebElement> icons = driver.findElements(By.xpath(invalidSeriesDataIcon.replace("{rowNumber}", rowNumber).replace("{columnNumber}", columnNumber)));
        hoverOverElement(icons.get(iconNumber));
        return getTooltipMessage();
    }

    @Step("Get tooltip message...")
    public String getTooltipMessage() {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip));
        return getText(tooltip);
    }

    @Step("Clicking on save button...")
    public void clickOnSaveSeriesDataButton() {
        click(saveSeriesDataButton);
    }

    @Step("Verifying that chart series contains value")
    public boolean isValueFieldPresent() {
        return isElementVisible(seriesValue);
    }

    @Step("Verifying that chart series contains label")
    public boolean isLabelFieldPresent() {
        return isElementVisible(seriesLabel);
    }

    @Step("Entering value into multiple rows...")
    public void inputTextIntoSeriesDataFields(String[] values, String columNumber) {
        for (int i = 0; i < values.length; i++) {
            inputTextIntoSeriesDataField(String.valueOf(i), columNumber, values[i]);
        }
    }
}
