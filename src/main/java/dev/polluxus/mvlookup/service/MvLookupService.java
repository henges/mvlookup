package dev.polluxus.mvlookup.service;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.client.tmdb.TmdbClient;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.mvlookup.utils.FutureUtils;
import org.graalvm.collections.Pair;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MvLookupService {

    @Inject
    TmdbClient tmdbClient;

    public CompletionStage<TmdbSearchResponse> lookupTmdb(final MovieQuery lookup) {

        return tmdbClient.searchMovie(lookup.name(), lookup.year().orElse(null));
    }

    public CompletionStage<List<Pair<MovieQuery, TmdbSearchResponse>>> lookupTmdb(final List<MovieQuery> lookups) {

        final var queries = lookups.stream()
                .map(l -> lookupTmdb(l).thenApply(r -> Pair.create(l, r)))
                .toList();

        return FutureUtils.fluxToMono(queries);
    }
}
