package com.ake3m.tasks;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CollectVideoPageURLsTask implements ApplicationTask<List<PageToken>> {

    private WebDriver webDriver;
    private By locator;

    public CollectVideoPageURLsTask(Page page) {
        this.webDriver = page.getWebDriver();
    }

    public CollectVideoPageURLsTask withLocator(By locator) {
        this.locator = locator;
        return this;
    }

    public CompletableFuture<List<PageToken>> execute() {
        return CompletableFuture.supplyAsync(() -> {
            List<WebElement> elements = webDriver.findElements(locator);
            return elements.stream().map(webElement -> {
                var name = webElement.getText();
                var url = webElement.getAttribute("href");
                return new PageToken(name, url, Collections.emptyList());
            }).collect(Collectors.toList());
        });
    }
}
