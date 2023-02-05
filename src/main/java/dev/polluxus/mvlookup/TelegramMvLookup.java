package dev.polluxus.mvlookup;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
    MvLookupService mvLookupService;

    @PostConstruct
    public void create() {
        final TelegramBot bot = new TelegramBot("");

//        SetWebhook wh = new SetWebhook()
//                .allowedUpdates("message")
//                .certificate("todo")
//                .url("todo");
//
//        bot.execute(wh, new Callback<SetWebhook, BaseResponse>() {
//            @Override
//            public void onResponse(SetWebhook request, BaseResponse response) {
//                if (!response.isOk()) {
//                    log.error("Error mounting webhook, error code {}", response.errorCode());
//                    System.exit(1);
//                }
//            }
//
//            @Override
//            public void onFailure(SetWebhook request, IOException e) {
//                log.error("IO error while mounting webhook", e);
//                System.exit(1);
//            }
//        });
    }

    @POST
    public void msg(String body) {

        final Update update = BotUtils.parseUpdate(body);
        mvLookupService.handleUpdate(update);
    }
}
