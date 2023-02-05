package dev.polluxus.mvlookup.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FutureUtils {

    private static final CompletionStage<Object> FAILED_STAGE;

    static {
        FAILED_STAGE = new CompletableFuture<>();
        FAILED_STAGE.toCompletableFuture().completeExceptionally(new RuntimeException("Invalid request"));
    }

    @SuppressWarnings("unchecked")
    public static <T> CompletionStage<T> failed() {

        return (CompletionStage<T>) FAILED_STAGE;
    }

}
