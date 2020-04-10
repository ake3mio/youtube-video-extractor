package com.ake3m.steps;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import com.ake3m.tasks.ReturnPageYoutubeVideoIdsTask;
import org.openqa.selenium.By;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VideoPageToYoutubeVideoIdsStep implements ApplicationStep<List<PageToken>> {

    @Inject
    @Named("website.video.links.matcher")
    private String matcher;
    @Inject
    @Named("website.video.links.attribute")
    private String attribute;

    private List<PageToken> pageTokens  = Collections.emptyList();

    public VideoPageToYoutubeVideoIdsStep withPageTokens(List<PageToken> pageTokens){
        this.pageTokens = pageTokens;
        return this;
    }

    public CompletableFuture<List<PageToken>> run(Page page) {


        Stream<CompletableFuture<PageToken>> futureStream = pageTokens.stream().map(pageToken -> {

            CompletableFuture<List<String>> videoLinksFuture = CompletableFuture.supplyAsync(() -> {
                page.open(pageToken.url());
                By xpath = By.xpath(String.format("//*[contains(@%s, \"%s\")]", attribute, matcher));
                page.waitForLocator(xpath);
                var execute = new ReturnPageYoutubeVideoIdsTask(page).withVideoLinksConfig(attribute, matcher).execute();
                try {
                    return execute.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            return videoLinksFuture.thenApply(links -> new PageToken(pageToken.name(), pageToken.url(), links));
        });

        return CompletableFuture.supplyAsync(() -> futureStream.map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList()));
    }
}
