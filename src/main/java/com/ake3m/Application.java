package com.ake3m;

import com.ake3m.automation.driver.WebDriverFactory;
import com.ake3m.automation.driver.WebDriverType;
import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import com.ake3m.steps.LoginStep;
import com.ake3m.steps.VideoPageToYoutubeVideoIdsStep;
import com.ake3m.steps.VideoPageUrlsStep;
import com.ake3m.steps.YouTubeVideosToDirectoryStep;
import com.google.inject.Inject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class Application {

    @Inject
    public Application(
            LoginStep loginStep,
            ThreadPoolExecutor threadPoolExecutor,
            VideoPageUrlsStep videoPageUrlsStep,
            VideoPageToYoutubeVideoIdsStep videoPageToYoutubeVideoIdsStep,
            YouTubeVideosToDirectoryStep youTubeVideosToDirectoryStep,
            WebDriverFactory webDriverFactory
    ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ExecutionException, InterruptedException {

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println(e.getLocalizedMessage());
            threadPoolExecutor.shutdownNow();
        });

        var webDriver = webDriverFactory.getWebDriver(WebDriverType.CHROME_81_MAC);
        var page = new Page(webDriver);

        Function<List<PageToken>, CompletionStage<List<PageToken>>> getVideoIdsFromPages =
                pageTokens -> videoPageToYoutubeVideoIdsStep
                        .withPageTokens(pageTokens)
                        .run(page);

        Function<List<PageToken>, List<PageToken>> shutdownPage = pageTokens -> {
            page.close();
            return pageTokens;
        };

        Function<List<PageToken>, CompletionStage<Void>> downloadYoutubeVideos = pageTokens ->
                youTubeVideosToDirectoryStep
                        .withPageTokens(pageTokens)
                        .run(page);

        var run = loginStep.run(page)
                .thenComposeAsync(aVoid -> videoPageUrlsStep.run(page), threadPoolExecutor)
                .thenComposeAsync(getVideoIdsFromPages, threadPoolExecutor)
                .thenApply(shutdownPage)
                .thenComposeAsync(downloadYoutubeVideos, threadPoolExecutor);

        run.join();

        System.out.println("Application Complete!");

        threadPoolExecutor.shutdown();
    }
}
