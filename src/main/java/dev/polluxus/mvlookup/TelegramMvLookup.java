package dev.polluxus.mvlookup;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import dev.polluxus.mvlookup.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelegramMvLookup {

    private static final Logger log = LoggerFactory.getLogger(TelegramMvLookup.class);

    @Inject
    TelegramService telegramService;

    @POST
    public void receiveUpdate(String body) {

        final Update update = BotUtils.parseUpdate(body);
        telegramService.handleUpdate(update);
    }
}
