package dev.polluxus.mvlookup.service;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.client.tmdb.TmdbClient;
import dev.polluxus.mvlookup.request.MovieQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class LookupService {

    @Inject
    TmdbClient tmdbClient;

    public CompletionStage<TmdbSearchResponse> lookupTmdb(final MovieQuery lookup) {

        return tmdbClient.searchMovie(lookup.name(), lookup.year().orElse(null));
    }
}
