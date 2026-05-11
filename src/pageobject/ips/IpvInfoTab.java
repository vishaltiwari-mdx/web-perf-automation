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
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IpvInfoTab extends BasePage {
    private WebDriver driver;

    public IpvInfoTab(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//span[contains(@class,'alias-badge')]/span[contains(@class,'truncate')]")
    private List<WebElement> aliases;

    @FindBy(xpath = "//button[contains(@class, 'edit-button')]")
    private WebElement editAliasesPencilButton;

    @FindBy(xpath = "//div[normalize-space()='Aliases']/following-sibling::*/*[normalize-space()='No aliases added.']")
    private WebElement noAliasesMsg;

    @FindBy(xpath = "//div[normalize-space()='Version message']/following-sibling::*")
    private WebElement versionMssg;

    @FindBy(xpath = "//div[@id='ipvInfoCard']//div[normalize-space()='Repo path']//following-sibling::div")
    private WebElement repoPath;

    @FindBy(xpath = "//div[@id='iplInfoCard']//div[normalize-space()='Description']//following-sibling::div//div[@class='more-text__container__text']")
    private WebElement lineDescription;

    @FindBy(xpath = "//div[normalize-space()='DM type']//following-sibling::div")
    private WebElement DmType;


    private String xpathAlias = "//span[contains(@class,'alias-badge')]/*[normalize-space()='{alias}'][@title='{alias}']";
    private String xpathHintIcon = "//div[@data-testid='tabpanel-ip-details-info-tab']//div[@data-testid='ui-card-header']//following-sibling::h5[@title='Version - {version}']//following-sibling::div//span[contains(@class, 'ip-details-info-tab-card__field-value')]//following-sibling::div[@data-testid='ui-tooltip-trigger']//i";
    private String xpathCollapseExpandInfoCard = "//div[contains(@class, 'ip-details-info-tab-card')]//div[contains(@class, 'ip-details-info-tab-card__header')]//following-sibling::h5[@title='{infoTabCardName}']/ancestor::div[contains(@class, 'card-header')]/div/i";

    @Step("Hover over Version card hint icon...")
    public void hoverOverHintIcon(String ipName) {
        waitForPageLoaded();
        waitForSpinnersToDisappear(Duration.ofSeconds(DEFAULT_FLUENT_WAIT_SECONDS_SHORT));
        DriverFactory.sleep(1000); // need to improve
        WebElement hintIcon = waitTillClickableWithFluentWait(findElementWithFluentWait(By.xpath(xpathHintIcon.replace("{version}", ipName))));
        hoverOverElement(hintIcon);
    }

    @Step("Click Version card collapse button...")
    public void clickCardCollapseExpandIcon(String infoTabCardName) {
        WebElement collapseIpvCard = waitTillClickableWithFluentWait(findElementWithWait(By.xpath(xpathCollapseExpandInfoCard.replace("{infoTabCardName}", infoTabCardName))));
        click(collapseIpvCard);
    }

    @Step("Get version message text...")
    public String getVersionMessage() {
        return getText(versionMssg);
    }

    @Step("Get IPV repository path...")
    public String getRepoPath() {
        return getText(repoPath);
    }

    @Step("Get line description text...")
    public String getLineDescription() {
        return getText(lineDescription);
    }

    @Step("Get line description text...")
    public String getDMType() {
        return getText(DmType);
    }

    @Step("Verify Repo path field is displayed...")
    public boolean isRepoPathFieldVisible() {
        return isElementVisible(repoPath);
    }

    @Step("Verify IPV alias: {0} is displayed...")
    public boolean isAliasDisplayed(String aliasName) {
        WebElement alias = findElementWithWait(By.xpath(xpathAlias.replace("{alias}", aliasName)));
        return isElementVisible(alias);
    }

    @Step("Verify IPV alias: {0} is not displayed...")
    public int isAliasNotDisplayed(String aliasName, int counter) {
        return waitForNumberOfElementsToBe(By.xpath(xpathAlias.replace("{alias}", aliasName)), counter).size();
    }

    @Step("Get the number of aliases...")
    public int getNumberOfAliases() {
        return getNumberOfVisibleElements(aliases);
    }

    @Step("Click on Edit Aliases button...")
    public EditAliasesPage clickOnEditAliases() {
        waitForPageLoaded();
        waitForElementToBeClickable(editAliasesPencilButton).click();
        return new EditAliasesPage(driver);
    }

    @Step("Verify Edit Aliases button is displayed...")
    public boolean isEditAliasesButtonDisplayed() {
        return isElementVisible(editAliasesPencilButton);
    }

    @Step("Verify no aliases are displayed...")
    public boolean noAliasesAdded() {
        return isElementVisible(noAliasesMsg);
    }
}
