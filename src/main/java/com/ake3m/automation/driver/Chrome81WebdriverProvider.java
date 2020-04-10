package com.ake3m.automation.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Chrome81WebdriverProvider implements WebdriverProvider {

    private final WebDriver webDriver;

    public Chrome81WebdriverProvider() {

        System.setProperty("webdriver.chrome.driver", getDriverUrl());
        var options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu","--ignore-certificate-errors");
        this.webDriver = new ChromeDriver(options);

        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    private String getDriverUrl() {

        var clazz = Chrome81WebdriverProvider.class;

        URL url = clazz
                .getClassLoader()
                .getResource("chromedriver-81-mac");

        assert url != null;

        return url.getFile();
    }
}
