package com.ake3m.steps;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import com.ake3m.tasks.AuthenticateTask;
import org.openqa.selenium.By;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LoginStep implements ApplicationStep<Void> {

    @Inject
    @Named("website.url")
    private String websiteUrl;

    @Inject
    @Named("authentication.password.value")
    private String password;

    @Inject
    @Named("authentication.password.selector")
    private String passwordSelector;

    @Inject
    @Named("authentication.submit.selector")
    private String submitSelector;

    public CompletableFuture<Void> run(Page page) {

        page.open(websiteUrl);

        page.waitForLocator(By.cssSelector(passwordSelector));

        return new AuthenticateTask(page)
                .withPassword(passwordSelector, password)
                .withSubmitSelector(submitSelector)
                .execute();
    }

    @Override
    public LoginStep withPageTokens(List<PageToken> pageTokens) {
        return this;
    }
}
