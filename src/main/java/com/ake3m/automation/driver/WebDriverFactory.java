package com.ake3m.automation.driver;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    private static final Map<WebDriverType, Class<? extends WebdriverProvider>> webDriverTypes = new HashMap<>();

    static {
        webDriverTypes.put(WebDriverType.CHROME_81_MAC, Chrome81WebdriverProvider.class);
    }

    public WebDriver getWebDriver(WebDriverType webDriverType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if (webDriverTypes.containsKey(webDriverType)) {
            var aClass = webDriverTypes.get(webDriverType);
            var webdriverProvider = aClass.getConstructor().newInstance();
            return webdriverProvider.getWebDriver();
        }

        throw new IllegalArgumentException(String.format("No webdriver type found for WebDriverType type %s", webDriverType));
    }
}
