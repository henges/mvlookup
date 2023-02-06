package dev.polluxus.mvlookup.service;

import dev.polluxus.mvlookup.client.response.MovieSearchResponse;
import dev.polluxus.mvlookup.client.TmdbClient;
import dev.polluxus.mvlookup.request.MovieQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MvLookupService {

    @Inject
    TmdbClient tmdbClient;

    public CompletionStage<MovieSearchResponse> lookup(final MovieQuery lookup) {

        return tmdbClient.searchMovie(lookup.name(), lookup.year().orElse(null));
    }
}
