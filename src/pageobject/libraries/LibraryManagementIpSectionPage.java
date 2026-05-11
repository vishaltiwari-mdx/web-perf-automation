/**
 *  ################################################################################
 *  # Copyright (c) 2010-2022 Methodics, Inc.
 *  # All Rights Reserved.
 *  #
 *  # This source file is confidential and the proprietary information of
 *  # Methodics, Inc. and its receipt or possession does not convey any rights to
 *  # reproduce or disclose its contents, or to manufacture, use or sell anything
 *  # it may describe. Reproduction, disclosure or use without Methodics, Inc.
 *  # specific written authorization is strictly forbidden.
 *  ################################################################################
 */
package com.methodics.phi.pageobject.libraries;

import com.methodics.phi.common.DriverFactory;
import com.methodics.phi.pageobject.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LibraryManagementIpSectionPage extends BasePage {
	private WebDriver driver;
	
	public LibraryManagementIpSectionPage(WebDriver driver) {
		this.driver = driver;
	}

	private String elementToScroll = "(//div[@col-id='fqn.ip'])[9]";
	private String gridTitle = "//div[contains(@class,'col text-truncate')]//span[@title='IPs in the {name} Library.']";
	
	@FindBy(xpath = "//span[contains(@class, 'libraries-ips-count')]")
	private WebElement numberOfIpsIcon;

	@FindBy(xpath = "//input[contains(@class, 'ips-search-input')]")
	private WebElement ipsSearchField;

	@FindBy(xpath = "//*[contains(@class, 'search-ips-icon')]")
	private WebElement clearSearchButton;

	@Step("Get number of IPs in library from the icon...")
	public String getNumberOfIpsFromIcon() {
        DriverFactory.sleep(2000);
        waitTillVisibleWithFluentWait(numberOfIpsIcon);
		return getText(numberOfIpsIcon);
	}

	@Step("Verify IPs count badge is displayed...")
	public boolean isIPsNumberBadgePresent() {
		return isElementVisible(numberOfIpsIcon);
	}

	@Step("Enter IP name to the search field...")
	public void searchIP(String ipName) {
        inputText(ipsSearchField, ipName);
        waitForPageLoaded();
	}

	@Step("Remove IP name from the search field by clicking on button...")
	public void clickOnClearSearchButton() {
		click(clearSearchButton);
	}

	@Step("Verify message above the grid is present...")
	public boolean isGridMessagePresent(String libName) {
		WebElement gridMessage = findElementWithWait(By.xpath(gridTitle.replace("{name}", libName)));
		return isElementVisible(gridMessage);
	}
}
