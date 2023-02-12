package dev.polluxus.mvlookup;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.TEXT_PLAIN)
@Path("/health")
public class HealthcheckController {

    @GET
    public String healthcheck() {
        return "OK";
    }
}
