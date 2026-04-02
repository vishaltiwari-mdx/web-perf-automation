package com.perforce.performance.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotManager - Captures page screenshots and returns report-relative paths.
 */
public final class ScreenshotManager {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotManager.class);

    private ScreenshotManager() {
    }

    public static String capture(WebDriver driver, String pageName, String screenshotDir, String reportDir) {
        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Driver does not support screenshot capture");
            return null;
        }

        try {
            Path screenshotFolder = Path.of(screenshotDir).toAbsolutePath();
            Files.createDirectories(screenshotFolder);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = sanitize(pageName) + "_" + timestamp + ".png";
            Path targetFile = screenshotFolder.resolve(fileName);

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            Path reportBase = Path.of(reportDir).toAbsolutePath();
            String relativePath;
            try {
                relativePath = reportBase.relativize(targetFile).toString();
            } catch (IllegalArgumentException e) {
                relativePath = targetFile.toString();
            }

            return relativePath.replace('\\', '/');
        } catch (Exception e) {
            log.warn("Failed to capture screenshot for {}: {}", pageName, e.getMessage());
            return null;
        }
    }

    private static String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "page";
        }
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

