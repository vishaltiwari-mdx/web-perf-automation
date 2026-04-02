package com.perforce.performance.tests;

import com.perforce.performance.driver.DriverManager;
import com.perforce.performance.reports.HtmlReportManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * BaseTest - Sets up/tears down WebDriver and ExtentReports for all test classes.
 */
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void initReports() {
        HtmlReportManager.init();
        log.info("=== Web Performance Test Suite Started ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        driver = DriverManager.initDriver();
        log.info("--- Test started: {} ---", method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        DriverManager.quitDriver();
        log.info("--- Test finished: {} | Status: {} ---",
                result.getName(), result.isSuccess() ? "PASSED" : "FAILED");
    }

    @AfterSuite(alwaysRun = true)
    public void flushReports() {
        HtmlReportManager.flush();
        log.info("=== Web Performance Test Suite Completed ===");
        log.info("Reports saved to: reports/");
    }
}
