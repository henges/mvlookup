package dev.polluxus.mvlookup.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class FutureUtils {

    private static final CompletionStage<Object> DONE_STAGE;
    private static final CompletionStage<Object> FAILED_STAGE;

    static {
        DONE_STAGE = new CompletableFuture<>();
        DONE_STAGE.toCompletableFuture().complete(null);

        FAILED_STAGE = new CompletableFuture<>();
        FAILED_STAGE.toCompletableFuture().completeExceptionally(new RuntimeException("Invalid request"));
    }

    @SuppressWarnings("unchecked")
    public static <T> CompletionStage<T> failed() {

        return (CompletionStage<T>) FAILED_STAGE;
    }

    @SuppressWarnings("unchecked")
    public static <T> CompletionStage<T> done() {

        return (CompletionStage<T>) DONE_STAGE;
    }

    public static <T> CompletionStage<List<T>> fluxToMono(final List<CompletionStage<T>> futures) {

        return CompletableFuture
                .allOf(futures.stream()
                        .map(CompletionStage::toCompletableFuture)
                        .toArray(CompletableFuture[]::new))
                .thenApply(__ -> futures.stream()
                        .map(CompletionStage::toCompletableFuture)
                        .map(CompletableFuture::join)
                        .toList());
    }

}
