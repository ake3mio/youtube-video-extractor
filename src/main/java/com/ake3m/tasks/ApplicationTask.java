package com.ake3m.tasks;

import java.util.concurrent.CompletableFuture;

public interface ApplicationTask<T> {
    CompletableFuture<T> execute();
}
