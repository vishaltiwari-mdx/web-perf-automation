package com.perforce.performance.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Reads application configuration from config.properties
 * Update config.properties with your app URL, credentials, and screen paths.
 */
public class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static ConfigReader instance;

    private ConfigReader() {
        loadProperties();
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
            log.info("Configuration loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value.trim();
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    // ─── Convenience getters ───────────────────────────────────────────────────

    public String getBaseUrl()         { return get("app.base.url"); }
    public String getLoginUrl()        { return get("app.login.url", getBaseUrl() + "/login"); }
    public String getUsername()        { return get("app.username"); }
    public String getPassword()        { return get("app.password"); }
    public String getBrowser()         { return get("browser", "chrome"); }
    public boolean isHeadless()        { return getBoolean("browser.headless", false); }
    public int getPageLoadTimeout()    { return getInt("timeout.pageload", 30); }
    public int getImplicitWait()       { return getInt("timeout.implicit", 10); }
    public int getPerformanceRuns()    { return getInt("performance.runs", 3); }
    public String getReportPath()      { return get("report.output.path", "reports"); }
    public String getScreenshotPath()  { return get("report.screenshot.path", getReportPath() + "/screenshots"); }
}
