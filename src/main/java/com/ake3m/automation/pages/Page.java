package com.ake3m.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Page {

    WebDriver webDriver;

    public Page(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void waitForLocator(By locator) {
        var wait = new WebDriverWait(this.webDriver, 10);
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void open(String url) {
        this.webDriver.get(url);
    }

    public void close() {
        webDriver.close();
    }
}
