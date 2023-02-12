package dev.polluxus.mvlookup;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import dev.polluxus.mvlookup.config.ConfigContainer;
import dev.polluxus.mvlookup.config.ConfigContainer.BotConfig;
import dev.polluxus.mvlookup.service.TelegramService;
import dev.polluxus.mvlookup.utils.FutureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelegramMvLookupController {

    private static final Logger log = LoggerFactory.getLogger(TelegramMvLookupController.class);

    @Inject
    TelegramService telegramService;

    @Inject
    BotConfig botConfig;

    @POST
    public CompletionStage<Void> receiveUpdate(String body, @HeaderParam(ConfigContainer.X_TELEGRAM_BOT_API_SECRET_TOKEN) String token) {

        if (botConfig.sharedSecret().map(s -> !s.equals(token)).orElse(false)) {
            log.warn("Unauthenticated request received! Request body {}", body);
            return FutureUtils.done();
        }

        final Update update = BotUtils.parseUpdate(body);

        log.trace("Got incoming update {}", update);

        return telegramService.handleUpdate(update);
    }
}
