package dev.polluxus.mvlookup.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
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

    /**
     * Transforms the input {@code futures} from a
     * {@code List<CompletionStage<T>>} into a {@code CompletionStage<List<T>>}
     * by creating and returning a future that completes when each future in {@code futures}
     * is complete.
     * @param futures the futures to transform
     * @return a {@link CompletionStage} of the input list with all futures complete
     * @param <T> the value type of the futures
     */
    public static <T> CompletionStage<List<T>> fluxToMono(final List<CompletionStage<T>> futures) {

        return CompletableFuture
                .allOf(futures.stream()
                        .map(CompletionStage::toCompletableFuture)
                        .toArray(CompletableFuture[]::new))
                .thenApply(__ -> futures.stream()
                        .map(CompletionStage::toCompletableFuture)
                        .map(CompletableFuture::join)
                        .toList()
                );
    }

    /**
     * Transforms the input {@code futures} from a
     * {@code Map<K, CompletionStage<V>>} into a {@code CompletionStage<Map<K, V>>}
     * by creating and returning a future that completes when each future in {@code futures.values()}
     * is complete.
     * @param futures the futures to transform
     * @return a {@link CompletionStage} of the input map with all futures complete
     * @param <K> the key type of the map
     * @param <V> the value type of the future values in the map
     */

    public static <K, V> CompletionStage<Map<K, V>> fluxToMono(final Map<K, CompletionStage<V>> futures) {

        return CompletableFuture
                .allOf(futures.values().stream()
                        .map(CompletionStage::toCompletableFuture)
                        .toArray(CompletableFuture[]::new))
                .thenApply(__ -> futures.entrySet().stream()
                        .collect(Collectors.toMap(
                                Entry::getKey,
                                kv -> kv.getValue().toCompletableFuture().join()
                        ))
                );
    }

}
