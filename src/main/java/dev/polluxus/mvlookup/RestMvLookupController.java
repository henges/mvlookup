package dev.polluxus.mvlookup;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.mvlookup.service.MvLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestMvLookupController {

    private static final Logger log = LoggerFactory.getLogger(RestMvLookupController.class);

    @Inject
    MvLookupService mvLookupService;

    @POST
    public CompletionStage<TmdbSearchResponse> getMovie(MovieQuery req) {
        log.info("Incoming request to lookup movie {}", req);

        return mvLookupService.lookupTmdb(req);
    }
}
