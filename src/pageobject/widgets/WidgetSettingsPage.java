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
package com.methodics.phi.pageobject.widgets;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.File;
import java.util.List;

public class WidgetSettingsPage extends BasePage {
    private WebDriver driver;

    public WidgetSettingsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//div[@role='dialog']//button[normalize-space()='Cancel']")
    private WebElement cancelDialogButton;

    @FindBy(css = "button[data-testid='ui-btn-modal-header-close']")
    private WebElement closeDialogButton;

    @FindBy(xpath = "//div[@class='collapse show']//span[@class='multiselect__single']")
    private WebElement openChartBlockDropdown;

    @FindBy(css = "input[data-testid='ui-in-chart-settings-main-title']")
    private WebElement mainTitleInputField;

    @FindBy(css = "input[data-testid='ui-in-chart-settings-subtitle']")
    private WebElement subTitleInputField;

    @FindBy(xpath = "//button[normalize-space()='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//input[@type='file' and @accept='.csv']")
    private WebElement fileUpload;

    @FindBy(xpath = "//button[normalize-space()='Create' and @disabled]")
    private WebElement disabledCreateButton;

    @FindBy(xpath = "//i[contains(@class,'fa-trash-xmark')]")
    private WebElement deleteIcon;

    @FindBy(xpath = "//div[contains(@class,'invalid-feedback_error')]")
    private WebElement radiusOffsetError;

    @FindBy(xpath = "//div[@class='field-message' and text()='Enabled only for stacked data.']")
    private WebElement normalizationValueMessage;

    @FindBy(css = "input[data-testid='ui-in-chart-settings-normalization-value']")
    private WebElement normalizationValueInput;

    @FindBy(css = "input[data-testid='ui-in-chart-settings-normalization-value'][disabled]")
    private WebElement disabledNormalizationValueInput;

    @FindBy(css = "input[data-testid='ui-in-chart-settings-inner-radius-offset']")
    private WebElement innerRadiusOffset;

    @FindBy(xpath = "//label[@for='pie-labels_button' and not(contains(@class,'active'))]")
    private WebElement pieLabel;

    @FindBy(xpath = "//label[@for='pie-labels_button' and contains(@class,'active')]")
    private WebElement pieLabelActive;

    @FindBy(xpath = "//div[@class='axis-settings']/div[contains(@class,'multiselect--disabled')]")
    private WebElement disabledMultiselect;

    @FindBy(xpath = "//div[@class='field-message' and normalize-space()='Predefined for this chart type.']")
    private WebElement predefinedDataTypeMessage;

    @FindBy(xpath = "//div[normalize-space()='X axis data type was automatically set to \"Category\".']")
    private WebElement dataTypeChangeMessage;

    @FindBy(xpath = "//div[@data-testid='settings-block']//div[contains(@class,'show')]//span[normalize-space()='Label format']/parent::div//i")
    private WebElement labelInfoIcon;

    private String chartTypeValue = "//div[@class='collapse show']//div[@class='multiselect__content-wrapper']//span[text()='{value}']";
    private String chartSettingBlock = "//span[@data-testid='settings-block-title' and normalize-space()='{blockName}']";
    private String chartCollapsedSettingBlock = "//div[@data-testid='settings-block']//following-sibling::div[@class='collapse show']/preceding-sibling::div[@class='settings-block__header']//span[normalize-space()='{blockName}']";
    private String chartDropdownItem = "//span[text()='{blockName}']//ancestor::div[@class='settings-block']//following-sibling::div[@class='collapse show']//span[contains(@class,'multiselect__option')]//span[text()='{option}']";
    private String chartWidgetTitleToggles = "//div[@class='collapse show']//span[text()='{title}']/parent::div//label[@class='toggle__button cursor--pointer']";
    private String chartWidgetTitleActiveToggles = "//div[@class='collapse show']//span[text()='{title}']/parent::div//label[@class='toggle__button active cursor--pointer']";
    private String chartLegendToggle = "//span[text()='Legend']/parent::div/parent::div//label[@class='toggle__button cursor--pointer']//span";
    private String chartLegendActiveToggle = "//span[text()='Legend']/parent::div/parent::div//label[@class='toggle__button active cursor--pointer']//span";
    private String positionDropDown = "//div[@class='collapse show']//span[text()='Position']/parent::div//following-sibling::div//span[@class='multiselect__single']";
    private String dataTypeDropDown = "//div[@class='collapse show']//span[text()='Position']/parent::div//preceding-sibling::div//span[@class='multiselect__single']";
    private String tickIntervalDropDown = "//input[@placeholder='Enter tick interval']/parent::div//span[@class='multiselect__single']";
    private String tickPerTime = "//input[@placeholder='Enter tick per time']/parent::div//span[@class='multiselect__single']";
    private String dataTypeValue = "//div[@class='collapse show']//div[contains(@class,('multiselect-enter-active'))]//span[text()='{value}']";
    private String tickLabelsToggle = "//div[contains(@class,'collapse show')]//span[text()='Tick labels']/parent::div/parent::div//label[@class='toggle__button cursor--pointer']";
    private String tickLabelsActiveToggle = "//div[contains(@class,'collapse show')]//span[text()='Tick labels']/parent::div/parent::div//label[@class='toggle__button active cursor--pointer']";
    private String axisInputFields = "//div[@class='collapse show']//input[@placeholder='{fieldText}']";
    private String labelsToggle = "//div[@class='collapse show']//span[text()='Labels']/parent::div/parent::div//label[@class='toggle__button cursor--pointer']";
    private String labelsActiveToggle = "//div[@class='collapse show']//span[text()='Labels']/parent::div/parent::div//label[@class='toggle__button active cursor--pointer']";
    private String stackDataToggle = "//label[@for='stack-data-toggle_button' and not(contains(@class,'active'))]";
    private String stackDataActiveToggle = "//label[@for='stack-data-toggle_button' and contains(@class, 'active')]";
    private String axisInfo = "//span[normalize-space()='{axis}']/ancestor::div[@class='settings-block__header']/i";
    private String xpathTooltip = "//div[@class='tooltip-inner']";
    private String barChartInfo = "//div[@class='collapse show']//i[contains(@class, 'fa-circle-info')]";

    @Step("Clicking on Dialog close button...")
    public void clickOnDialogCloseButton() {
        click(closeDialogButton);
        DriverFactory.sleep(1000);
    }

    @Step("Check if create button is disabled...")
    public boolean isCreateButtonDisabled() {
        return isElementVisible(disabledCreateButton);
    }

    @Step("Verify Chart Setting Block is present...")
    public boolean isChartSettingBlockPresent(String title) {
        WebElement item = findElementWithWait(By.xpath(chartSettingBlock.replace("{blockName}", title)));
        return isElementVisible(item);
    }

    @Step("Verify specified Chart Type is present...")
    public boolean isChartTypePresent(String title) {
        WebElement item = findElementWithWait(By.xpath(chartTypeValue.replace("{value}", title)));
        return isElementVisible(item);
    }

    @Step("Verify Chart Setting Block is expanded...")
    public boolean isChartSettingBlockExpanded(String title) {
        List<WebElement> size = findElements(By.xpath(chartCollapsedSettingBlock.replace("{blockName}", title)));
        return size.size() != 0;
    }

    @Step("Verify all options in Chart Setting Block drop-down are displayed...")
    public void areOptionsPresent(String[] names) {
        clickChartsBlockDropDown();
        for (String name : names) {
            Assert.assertTrue(isChartTypePresent(name), "Option " + name + " is not displayed");
        }
        clickChartsBlockDropDown();
    }

    @Step("Verify Chart Type Setting Block is expanded...")
    public boolean isChartTypeSettingBlockExpanded() {
        return isChartSettingBlockExpanded("Chart type");
    }

    @Step("Clicking {title} Chart setting block...")
    public void clickChartSettingBlock(String title) {
        WebElement item = findElementWithWait(By.xpath(chartSettingBlock.replace("{blockName}", title)));
        click(item);
    }

    @Step("Expand specified Chart setting block...")
    public void expandChartSettingBlock(String title) {
        if (!isChartSettingBlockExpanded(title)) {
            clickChartSettingBlock(title);
        }
    }

    @Step("Expand Widget title Chart setting block...")
    public void expandWidgetTitleChartSettingBlock() {
        expandChartSettingBlock("Widget title");
    }

    @Step("Expand Legend Chart setting block...")
    public void expandLegendChartSettingBlock() {
        expandChartSettingBlock("Legend");
    }

    @Step("Expand Y axis Chart setting block...")
    public void expandYaxisChartSettingBlock() {
        expandChartSettingBlock("Y axis");
    }

    @Step("Expand X axis Chart setting block...")
    public void expandXaxisChartSettingBlock() {
        expandChartSettingBlock("X axis");
    }

    @Step("Expand Pie Chart setting block...")
    public void expandPieChartSettingBlock() {
        expandChartSettingBlock("Pie chart");
    }

    @Step("Expand Column Chart setting block...")
    public void expandColumnChartSettingBlock() {
        expandChartSettingBlock("Column chart");
    }

    @Step("Expand Bar Chart setting block...")
    public void expandBarChartSettingBlock() {
        expandChartSettingBlock("Bar chart");
    }

    @Step("Expand Area Chart setting block...")
    public void expandAreaChartSettingBlock() {
        expandChartSettingBlock("Area chart");
    }

    @Step("Input text into 'Axis title' field ...")
    public void inputTextIntoAxisTitleField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter axis title")));
        inputText(item, titleText);
    }

    @Step("Click Data type axis drop-down...")
    public void clickDataTypeDropDown() {
        WebElement element = driver.findElement(By.xpath(dataTypeDropDown));
        click(element);
    }

    @Step("Select specified Data Type option from drop down list...")
    public void selectOptionFromDataType(String option) {
        clickDataTypeDropDown();
        click(driver.findElement(By.xpath(dataTypeValue.replace("{value}", option))));
    }

    @Step("Select Number Data Type option from Data Type drop down list...")
    public void selectNumberDataType() {
        selectOptionFromDataType("Number");
    }

    @Step("Select Log Data Type option from Data Type drop down list...")
    public void selectLogDataType() {
        selectOptionFromDataType("Log");
    }

    @Step("Select Time Data Type option from Data Type drop down list...")
    public void selectTimeDataType() {
        selectOptionFromDataType("Time");
    }

    @Step("Select Category Data Type option from Data Type drop down list...")
    public void selectCategoryDataType() {
        selectOptionFromDataType("Category");
    }

    @Step("Click Position axis drop-down...")
    public void clickPositionDropDown() {
        WebElement element = driver.findElement(By.xpath(positionDropDown));
        click(element);
    }

    @Step("Select specified Position option from drop down list...")
    public void selectOptionFromPosition(String option) {
        clickPositionDropDown();
        click(driver.findElement(By.xpath(dataTypeValue.replace("{value}", option))));
    }

    @Step("Select Top Position option from drop down list...")
    public void selectTopFromPosition() {
        selectOptionFromPosition("Top");
    }

    @Step("Select Bottom Position option from drop down list...")
    public void selectBottomFromPosition() {
        selectOptionFromPosition("Bottom");
    }

    @Step("Select Left Position option from drop down list...")
    public void selectLeftFromPosition() {
        selectOptionFromPosition("Left");
    }

    @Step("Select Right Position option from drop down list...")
    public void selectRightFromPosition() {
        selectOptionFromPosition("Right");
    }

    @Step("Click Axis title toggle...")
    public void clickAxisTitleToggle() {
        clickChartWidgetTitleToggle("Axis title");
    }

    @Step("Verify that Chart Widget Main title toggle is active...")
    public boolean isAxisTitleActive() {
        return isChartWidgetTitleActive("Axis title");
    }

    @Step("Verify that specified Chart Widget title toggle is inactive...")
    public boolean isAxisTitleInactive() {
        return isChartWidgetTitleInactive("Axis title");
    }

    @Step("Verify that Tick labels toggle is active...")
    public boolean isTickLabelsActive() {
        return getNumberOfElements(driver.findElements(By.xpath(tickLabelsActiveToggle))) == 1;
    }

    @Step("Verify that Tick labels toggle is inactive...")
    public boolean isTickLabelsInactive() {
        return getNumberOfElements(driver.findElements(By.xpath(tickLabelsToggle))) == 1;
    }

    @Step("Click Tick labels toggle...")
    public void clickTickLabelsToggle() {
        if (isTickLabelsInactive()) {
            click(driver.findElement(By.xpath(String.valueOf(tickLabelsToggle))));
        } else {
            click(driver.findElement(By.xpath(String.valueOf(tickLabelsActiveToggle))));
        }
    }

    @Step("Input text into 'Label format' field ...")
    public void inputTextIntoLabelFormatField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter format code for label")));
        inputText(item, titleText);
    }

    @Step("Input text into 'Label rotation' field ...")
    public void inputTextIntoLabelRotationField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter label rotation")));
        inputText(item, titleText);
    }

    @Step("Input text into 'Enter tick count' field ...")
    public void inputTextIntoTickCountField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter tick count")));
        inputText(item, titleText);
    }

    @Step("Click Tick Interval axis drop-down...")
    public void clickTickIntervalDropDown() {
        WebElement element = driver.findElement(By.xpath(tickIntervalDropDown));
        click(element);
    }

    @Step("Click Tick Per Time axis drop-down...")
    public void clickTickPerTimeDropDown() {
        WebElement element = driver.findElement(By.xpath(tickPerTime));
        click(element);
    }

    @Step("Select specified Tick Interval option from drop down list...")
    public void selectOptionFromTickInterval(String option) {
        clickTickIntervalDropDown();
        click(driver.findElement(By.xpath(dataTypeValue.replace("{value}", option))));
    }

    @Step("Select specified Tick Per Time option from drop down list...")
    public void selectOptionFromTickPerTime(String option) {
        clickTickPerTimeDropDown();
        click(driver.findElement(By.xpath(dataTypeValue.replace("{value}", option))));
    }

    @Step("Select Year Tick Interval option from Tick Interval drop down list...")
    public void selectYearTickInterval() {
        selectOptionFromTickInterval("Year");
    }

    @Step("Select Month Tick Interval option from Tick Interval drop down list...")
    public void selectMonthTickInterval() {
        selectOptionFromTickInterval("Month");
    }

    @Step("Select Day Tick Interval option from Tick Interval drop down list...")
    public void selectDayTickInterval() {
        selectOptionFromTickInterval("Day");
    }

    @Step("Select Hour Tick Interval option from Tick Interval drop down list...")
    public void selectHourTickInterval() {
        selectOptionFromTickInterval("Hour");
    }

    @Step("Select Minute Tick Interval option from Tick Interval drop down list...")
    public void selectMinuteTickInterval() {
        selectOptionFromTickInterval("Minute");
    }

    @Step("Select Second Tick Interval option from Tick Interval drop down list...")
    public void selectSecondTickInterval() {
        selectOptionFromTickInterval("Second");
    }

    @Step("Select Year Tick Per Time option from Tick Per Time drop down list...")
    public void selectYearTickPerTime() {
        selectOptionFromTickPerTime("Year");
    }

    @Step("Select Month Tick Per Time option from Tick Per Time drop down list...")
    public void selectMonthTickPerTime() {
        selectOptionFromTickPerTime("Month");
    }

    @Step("Select Day Tick Per Time option from Tick Per Time drop down list...")
    public void selectDayTickPerTime() {
        selectOptionFromTickPerTime("Day");
    }

    @Step("Select Hour Tick Per Time option from Tick Per Time drop down list...")
    public void selectHourTickPerTime() {
        selectOptionFromTickPerTime("Hour");
    }

    @Step("Select Minute Tick Per Time option from Tick Per Time drop down list...")
    public void selectMinuteTickPerTime() {
        selectOptionFromTickPerTime("Minute");
    }

    @Step("Select Second Tick Per Time option from Tick Per Time drop down list...")
    public void selectSecondTickPerTime() {
        selectOptionFromTickPerTime("Second");
    }

    @Step("Input text into 'Enter tick interval' field ...")
    public void inputTextIntoTickIntervalField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter tick interval")));
        inputText(item, titleText);
    }

    @Step("Input text into 'Enter tick per time' field ...")
    public void inputTextIntoTickPerTimeField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter tick per time")));
        inputText(item, titleText);
    }

    @Step("Get tooltip message...")
    public String getTickIntervalFieldText() {
        WebElement tickIntervalText = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter tick interval")));
        return getText(tickIntervalText);
    }

    @Step("Get tooltip message...")
    public String getTickPerTimeFieldText() {
        WebElement tickIntervalText = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter tick per time")));
        return getText(tickIntervalText);
    }

    @Step("Input text into 'Enter Minimum Value' field ...")
    public void inputTextIntoMinimumValueField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter minimum value")));
        inputText(item, titleText);
    }

    @Step("Input text into 'Enter Maximum Value' field ...")
    public void inputTextIntoMaximumValueField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter maximum value")));
        inputText(item, titleText);
    }

    @Step("Input text into 'Log base' field ...")
    public void inputTextIntoLogBaseField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter log base")));
        inputText(item, titleText);
    }


    @Step("Verify that Labels toggle is active...")
    public boolean isLabelsActive() {
        return getNumberOfElements(driver.findElements(By.xpath(labelsActiveToggle))) == 1;
    }

    @Step("Verify that Labels toggle is inactive...")
    public boolean isLabelsInactive() {
        return getNumberOfElements(driver.findElements(By.xpath(labelsToggle))) == 1;
    }

    @Step("Click Labels toggle...")
    public void clickLabelsToggle() {
        if (isTickLabelsInactive()) {
            click(driver.findElement(By.xpath(String.valueOf(labelsToggle))));
        } else {
            click(driver.findElement(By.xpath(String.valueOf(labelsActiveToggle))));
        }
    }

    @Step("Verify that Stack data toggle is active...")
    public boolean isStackDataActive() {
        return getNumberOfElements(driver.findElements(By.xpath(stackDataActiveToggle))) == 1;
    }

    @Step("Verify that Stack data toggle is inactive...")
    public boolean isStackDataInactive() {
        return getNumberOfElements(driver.findElements(By.xpath(stackDataToggle))) == 1;
    }

    @Step("Click Stack data toggle...")
    public void clickStackDataToggle() {
        if (isStackDataInactive()) {
            click(driver.findElement(By.xpath(stackDataToggle)));
        } else {
            click(driver.findElement(By.xpath(stackDataActiveToggle)));
        }
    }

    @Step("Input text into 'Normalization Value' field ...")
    public void inputTextIntoNormalizationValueField(String titleText) {
        WebElement item = findElementWithWait(By.xpath(axisInputFields.replace("{fieldText}", "Enter normalization value")));
        inputText(item, titleText);
    }

    @Step("Click Charts block drop-down...")
    public void clickChartsBlockDropDown() {
        click(openChartBlockDropdown);
    }

    @Step("Select specified Chart option from Chart Type drop down list...")
    public void selectOptionFromChartType(String option) {
        clickChartsBlockDropDown();
        click(driver.findElement(By.xpath(chartDropdownItem.replace("{blockName}", "Chart type").replace("{option}", option))));
    }

    @Step("Uploading csv file...")
    public void uploadCsvFile(String path) {
        File file = new File(path);
        fileUpload.sendKeys(file.getAbsolutePath());
    }

    @Step("Select Line Chart option from Chart Type drop down list...")
    public void selectLineChartOptionFromChartType() {
        selectOptionFromChartType("Line chart");
    }

    @Step("Select Scatter Chart option from Chart Type drop down list...")
    public void selectScatterChartOptionFromChartType() {
        selectOptionFromChartType("Scatter chart");
    }

    @Step("Select Pie Chart option from Chart Type drop down list...")
    public void selectPieChartOptionFromChartType() {
        selectOptionFromChartType("Pie chart");
    }

    @Step("Select Column Chart option from Chart Type drop down list...")
    public void selectColumnChartOptionFromChartType() {
        selectOptionFromChartType("Column chart");
    }

    @Step("Select Bar Chart option from Chart Type drop down list...")
    public void selectBarChartOptionFromChartType() {
        selectOptionFromChartType("Bar chart");
    }

    @Step("Select Area Chart option from Chart Type drop down list...")
    public void selectAreaChartOptionFromChartType() {
        selectOptionFromChartType("Area chart");
    }

    @Step("Input {titleText} into Main title field from Widget title block...")
    public void inputTextIntoMainTitleField(String titleText) {
        // Ensure the block is expanded before interacting with elements
        inputText(mainTitleInputField, titleText);
    }

    @Step("Input text into Subtitle field from Widget title block...")
    public void inputTextIntoSubTitleField(String subtitleText) {
        inputText(subTitleInputField, subtitleText);
    }

    @Step("Verify that specified Chart Widget title toggle is active...")
    public boolean isChartWidgetTitleActive(String titleName) {
        List<WebElement> size = driver.findElements(By.xpath(chartWidgetTitleActiveToggles.replace("{title}", titleName)));
        return size.size() != 0;
    }

    @Step("Verify that specified Chart Widget title toggle is inactive...")
    public boolean isChartWidgetTitleInactive(String titleName) {
        List<WebElement> size = driver.findElements(By.xpath(chartWidgetTitleToggles.replace("{title}", titleName)));
        return size.size() != 0;
    }

    @Step("Click specified Chart Widget Title Toggle...")
    public void clickChartWidgetTitleToggle(String titleName) {
        if (isChartWidgetTitleInactive(titleName)) {
            click(driver.findElement(By.xpath(chartWidgetTitleToggles.replace("{title}", titleName))));
        } else {
            click(driver.findElement(By.xpath(chartWidgetTitleActiveToggles.replace("{title}", titleName))));
        }
    }

    @Step("Click Chart Widget Main title toggle...")
    public void clickChartWidgetMainTitleToggle() {
        clickChartWidgetTitleToggle("Main title");
    }

    @Step("Click Chart Widget Subtitle toggle...")
    public void clickChartWidgetSubtitleToggle() {
        clickChartWidgetTitleToggle("Subtitle");
    }

    @Step("Verify that Chart Widget Main title toggle is active...")
    public boolean isChartWidgetMainTitleActive() {
        return isChartWidgetTitleActive("Main title");
    }

    @Step("Verify that Chart Widget Subtitle toggle is active...")
    public boolean isChartWidgetSubtitleActive() {
        return isChartWidgetTitleActive("Subtitle");
    }

    @Step("Verify that Chart Widget Main title toggle is inactive...")
    public boolean isChartWidgetMainTitleInactive() {
        return isChartWidgetTitleInactive("Main title");
    }

    @Step("Verify that Chart Widget Subtitle toggle is inactive...")
    public boolean isChartWidgetSubtitleInactive() {
        return isChartWidgetTitleInactive("Subtitle");
    }

    @Step("Verify that Legend toggle is active...")
    public boolean isChartLegendActive() {
        return getNumberOfElements(driver.findElements(By.xpath(chartLegendActiveToggle))) == 1;
    }

    @Step("Verify that Legend toggle is inactive...")
    public boolean isChartLegendInactive() {
        return getNumberOfElements(driver.findElements(By.xpath(chartLegendToggle))) == 1;
    }

    @Step("Click Chart Legend toggle...")
    public void clickChartLegendToggle() {
        if (isChartLegendInactive()) {
            waitForElementToBeVisible(driver.findElement(By.xpath(String.valueOf(chartLegendToggle))));
            click(driver.findElement(By.xpath(String.valueOf(chartLegendToggle))));
        } else {
            waitForElementToBeVisible(driver.findElement(By.xpath(String.valueOf(chartLegendActiveToggle))));
            click(driver.findElement(By.xpath(String.valueOf(chartLegendActiveToggle))));
        }
    }

    @Step("Click Enable Chart Legend toggle...")
    public void enableChartLegendToggle() {
        for (int tryTimes = 1; tryTimes <= 10; ++tryTimes) {
            try {
                waitForElementToBeClickable(findElementWithWait(By.xpath(chartLegendToggle))).click();
                if (isChartLegendActive()) {
                    break;
                }
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                logger.warn("Click intercepted on attempt " + tryTimes + ", retrying...");
            }
            DriverFactory.sleep(500);
            logger.info("Legend chart toggle is not enabled through " + tryTimes + " attempt, sleeping 500ms...");
        }
    }

    @Step("Click Disable Chart Legend toggle...")
    public void disableChartLegendToggle() {
        for (int tryTimes = 1; tryTimes <= 10; ++tryTimes) {
            click(waitForElementToBeClickable(findElementWithWait(By.xpath(chartLegendActiveToggle))));
            if (isChartLegendInactive()) {
                break;
            }
            DriverFactory.sleep(50);
            logger.info("Legend chart toggle is not disabled through " + tryTimes + "attempt sleep 50ms...");
        }
    }

    @Step("Clicking on delete icon...")
    public void clickOnDeleteIcon() {
        click(deleteIcon);
    }

    @Step("Clicking on delete button...")
    public void clickOnDeleteButton() {
        click(deleteButton);
    }

    @Step("Entering number into the inner radius offset..")
    public void enterNumberIntoInnerRadiusOffset(String number) {
        inputText(innerRadiusOffset, number);
    }

    @Step("Getting radius offset incorrect data error...")
    public String getRadiusOffsetError() {
        return getText(radiusOffsetError);
    }

    @Step("Getting field normalization value message...")
    public boolean isNormalizationValueMessageVisible() {
        return isElementVisible(normalizationValueMessage);
    }

    @Step("Entering value inside normalization value field...")
    public void enterValueInsideNormalizationField(String value) {
        inputText(normalizationValueInput, value);
    }

    @Step("Verifying that normalization value field is disabled")
    public boolean isNormalizationValueFieldDisabled() {
        return isElementVisible(disabledNormalizationValueInput);
    }

    @Step("Checking if Pie label toggle is active...")
    public boolean isPieLabelActive() {
        return isElementVisible(pieLabelActive);
    }

    @Step("Click Pie label toggle...")
    public void clickPieLabelToggle() {
        if (isPieLabelActive()) {
            click(pieLabelActive);
        } else {
            click(pieLabel);
        }
    }

    @Step("Check if data type multiselect is disabled")
    public boolean isDataTypeDisabled() {
        return isElementVisible(disabledMultiselect);
    }

    @Step("Check if predefined message is spresent for data type field...")
    public boolean isPredefinedFieldMessagePresent() {
        return isElementVisible(predefinedDataTypeMessage);
    }

    @Step("Get tooltip message...")
    public String getTooltipMessage() {
        WebElement tooltip = findElementWithWait(By.xpath(xpathTooltip));
        return getText(tooltip);
    }

    @Step("Getting information from information icon on axis settings...")
    public String getAxisInformationIconTooltip(String axis) {
        WebElement icon = findElementWithWait(By.xpath(axisInfo.replace("{axis}", axis)));
        hoverOverElement(icon);
        return getTooltipMessage();
    }

    @Step("Check that pop-up with message in the bottom right corner about data type change is present...")
    public boolean isDataTypeChangeMessagePresent() {
        return isElementVisible(dataTypeChangeMessage);
    }

    @Step("Getting information from information icon on 'Bart chart' Chart Type...")
    public String getBartChartInformationIconTooltip() {
        clickChartsBlockDropDown();
        WebElement icon = findElementWithWait(By.xpath(barChartInfo));
        hoverOverElement(icon);
        String msg = getTooltipMessage();
        clickChartsBlockDropDown();
        return msg;
    }

    @Step("Hovering over Label format information icon...")
    public void hoverOverLabelFormatInformationIcon() {
        hoverOverElement(labelInfoIcon);
    }

    @Step("Clicking on Label format info icon...")
    public void clickOnLabelFormatInformationIcon() {
        click(labelInfoIcon);
    }

}

