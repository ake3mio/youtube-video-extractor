package com.ake3m.steps;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicationStep<T> {

    CompletableFuture<T> run(Page page);

    ApplicationStep<T> withPageTokens(List<PageToken> pageTokens);
}
