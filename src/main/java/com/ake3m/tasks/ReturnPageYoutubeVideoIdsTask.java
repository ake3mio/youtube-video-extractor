package com.ake3m.tasks;

import com.ake3m.automation.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReturnPageYoutubeVideoIdsTask implements ApplicationTask<List<String>> {

    WebDriver webDriver;
    String attribute;
    String matcher;

    public ReturnPageYoutubeVideoIdsTask(Page page) {
        this.webDriver = page.getWebDriver();
    }

    public ReturnPageYoutubeVideoIdsTask withVideoLinksConfig(String attribute, String matcher) {
        this.attribute = attribute;
        this.matcher = matcher;
        return this;
    }

    public CompletableFuture<List<String>> execute() {
        return CompletableFuture.supplyAsync(() -> {
            List<WebElement> elements = webDriver.findElements(By.xpath(String.format("//*[contains(@%s, \"%s\")]", attribute, matcher)));
            return elements.stream().map(webElement -> {
                var value = webElement.getAttribute(attribute);
                if (value != null) {
                    var start = value.indexOf(matcher) + matcher.length();
                    var stringStream = Arrays.stream(value.substring(start).split("/"));
                    return stringStream.filter(s -> !s.equals("")).findFirst().orElse("");
                }

                return null;
            }).collect(Collectors.toList());
        });
    }
}
