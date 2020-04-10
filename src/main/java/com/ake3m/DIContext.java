package com.ake3m;

import com.ake3m.automation.driver.WebDriverFactory;
import com.ake3m.config.ConfigProvider;
import com.ake3m.steps.LoginStep;
import com.ake3m.steps.VideoPageToYoutubeVideoIdsStep;
import com.ake3m.steps.VideoPageUrlsStep;
import com.ake3m.steps.YouTubeVideosToDirectoryStep;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DIContext {

    private final Injector injector;

    public static <T> void initialise(Class<T> entryPointClass) {
        var context = new DIContext();
        context.initialiseContext(entryPointClass);
    }

    private DIContext() {
        this.injector = Guice.createInjector(new ApplicationModule());
    }

    private <T> void initialiseContext(Class<T> aClass) {
        injector.getInstance(aClass);
    }

    private static class ApplicationModule extends AbstractModule {

        @Override
        protected void configure() {
            var properties = ConfigProvider.getProperties();
            Names.bindProperties(binder(), properties);
            bind(WebDriverFactory.class).in(Scopes.SINGLETON);
            bind(VideoPageUrlsStep.class).in(Scopes.SINGLETON);
            bind(VideoPageToYoutubeVideoIdsStep.class).in(Scopes.SINGLETON);
            bind(YouTubeVideosToDirectoryStep.class).in(Scopes.SINGLETON);
            bind(LoginStep.class).in(Scopes.SINGLETON);
            bind(ThreadPoolExecutor.class).toInstance((ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        }
    }

}
