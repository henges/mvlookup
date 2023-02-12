package dev.polluxus.mvlookup.client.tmdb;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.config.ConfigContainer.TmdbClientConfig;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.client.predicate.ResponsePredicate;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TmdbClient {

    private static final Logger log = LoggerFactory.getLogger(TmdbClient.class);

    private final TmdbClientConfig config;

    private final WebClient webClient;

    @Inject
    public TmdbClient(final TmdbClientConfig config, final WebClient webClient) {
        this.config = config;
        this.webClient = webClient;
    }

    public CompletionStage<TmdbSearchResponse> searchMovie(final String name, final Year year) {

        final String endpoint = "/search/movie";

        final var reqWithParams = webClient.getAbs(config.baseUrl() + endpoint)
                .addQueryParam("api_key", config.apiToken())
                .addQueryParam("query", name);

        if (year != null) {
            reqWithParams
                    .addQueryParam("year", year.toString());
        }

        log.trace("Querying TMDB: {}", reqWithParams.uri() + reqWithParams.queryParams());

        return reqWithParams
                .expect(ResponsePredicate.SC_OK)
                .as(BodyCodec.json(TmdbSearchResponse.class))
                .send()
                .subscribeAsCompletionStage()
                .whenComplete((h, ex) -> {
                    if (ex != null) {
                        log.error("Got an error from TMDB", ex);
                    } else {
                        log.trace("Got response from TMDB: {}", h.statusCode());
                    }
                })
                .thenApply(HttpResponse::body);
    }
}
