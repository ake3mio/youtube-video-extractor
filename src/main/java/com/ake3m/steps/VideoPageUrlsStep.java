package com.ake3m.steps;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import com.ake3m.tasks.CollectVideoPageURLsTask;
import org.openqa.selenium.By;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VideoPageUrlsStep implements ApplicationStep<List<PageToken>> {

    @Inject
    @Named("website.page.links.xpath")
    private String xpath;

    public CompletableFuture<List<PageToken>> run(Page page) {

        By xpath = By.xpath(this.xpath);
        page.waitForLocator(xpath);

        return new CollectVideoPageURLsTask(page)
                .withLocator(xpath)
                .execute();
    }

    @Override
    public VideoPageUrlsStep withPageTokens(List<PageToken> pageTokens) {
        return this;
    }
}
