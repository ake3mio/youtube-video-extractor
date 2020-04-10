package com.ake3m.tasks;

import com.ake3m.automation.pages.Input;
import com.ake3m.automation.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.CompletableFuture;

public class AuthenticateTask implements ApplicationTask<Void> {

    private WebDriver webDriver;
    private Input password;
    private String submitSelector;

    public AuthenticateTask(Page page) {
        this.webDriver = page.getWebDriver();
    }

    public AuthenticateTask withPassword(String selector, String password) {
        this.password = new Input(selector, password);
        return this;
    }

    public AuthenticateTask withSubmitSelector(String submitSelector) {
        this.submitSelector = submitSelector;
        return this;
    }

    public CompletableFuture<Void> execute() {
        return CompletableFuture.runAsync(() -> {
            var element = webDriver.findElement(By.cssSelector(password.selector()));
            element.sendKeys(password.value());
            webDriver.findElement(By.cssSelector(submitSelector)).click();
        });
    }
}
