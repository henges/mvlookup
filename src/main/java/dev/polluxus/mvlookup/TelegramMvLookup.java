package dev.polluxus.mvlookup;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import dev.polluxus.mvlookup.config.ConfigContainer.BotConfig;
import dev.polluxus.mvlookup.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelegramMvLookup {

    private static final Logger log = LoggerFactory.getLogger(TelegramMvLookup.class);

    @Inject
    TelegramService telegramService;

    @Inject
    BotConfig botConfig;

    @POST
    public void receiveUpdate(String body, @HeaderParam("X-Telegram-Bot-Api-Secret-Token") String token) {

        if (!botConfig.sharedSecret().equals(token)) {
            log.warn("Unauthenticated request received! Request body {}", body);
            return;
        }

        final Update update = BotUtils.parseUpdate(body);
        telegramService.handleUpdate(update);
    }
}
