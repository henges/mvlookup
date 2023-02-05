package dev.polluxus.mvlookup;

import dev.polluxus.mvlookup.client.MovieSearchResponse;
import dev.polluxus.mvlookup.model.MovieLookup;
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
public class RestMvLookup {

    private static final Logger log = LoggerFactory.getLogger(RestMvLookup.class);

    @Inject
    MvLookupService mvLookupService;

    @POST
    public CompletionStage<MovieSearchResponse> getMovie(MovieLookup req) {
        log.info("Incoming request to lookup movie {}", req);

        return mvLookupService.lookup(req);
    }
}
