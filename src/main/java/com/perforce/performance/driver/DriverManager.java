package com.perforce.performance.driver;

import com.perforce.performance.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * DriverManager - Manages WebDriver lifecycle with Chrome DevTools Protocol (CDP) support.
 * Uses WebDriverManager to auto-download and configure ChromeDriver.
 */
public class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static WebDriver initDriver() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getBrowser().toLowerCase();

        if (!"chrome".equals(browser)) {
            throw new UnsupportedOperationException("Only Chrome is supported for performance testing (CDP required). Got: " + browser);
        }

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Enable performance logging via Chrome DevTools
        options.addArguments("--enable-precise-memory-info");

        if (config.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            log.info("Running in HEADLESS mode");
        }

        ChromeDriver driver = new ChromeDriver(options);

        // Set timeouts
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));

        driverThread.set(driver);
        log.info("ChromeDriver initialized successfully");
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("ChromeDriver closed");
        }
    }
}
