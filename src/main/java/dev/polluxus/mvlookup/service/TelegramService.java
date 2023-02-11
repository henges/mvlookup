package dev.polluxus.mvlookup.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.mvlookup.utils.FutureUtils;
import dev.polluxus.mvlookup.utils.MessageFormatters;
import dev.polluxus.mvlookup.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    private final MvLookupService mvLookupService;

    private final TelegramBot bot;

    @Inject
    public TelegramService(final MvLookupService mvLookupService, final TelegramBot bot) {
        this.mvLookupService = mvLookupService;
        this.bot = bot;
    }

    public CompletionStage<Void> handleUpdate(final Update update) {

        final Message msg = update.message();
        if (msg == null) {
            return FutureUtils.done();
        }

        final String text = msg.text();
        if (text == null || text.length() == 0) {
            return FutureUtils.done();
        }

        final MovieQuery query = TextUtils.parseAsMovieQuery(text);
        if (query == null) {
            return FutureUtils.done();
        }

        log.info("Looking up {} for {}", query, update.message().chat().username());

        return mvLookupService.lookup(query)
                .thenApply(r -> MessageFormatters.tmdbShortFormat(query, r))
                .thenAccept(r -> {
                    final SendMessage req = new SendMessage(msg.chat().id(), r).parseMode(ParseMode.MarkdownV2);
                    log.trace("Final message body (plus method field): {}", req.toWebhookResponse());

                    bot.execute(req, logOnFailureCallback(msg));
                });
    }



    private Callback<SendMessage, SendResponse> logOnFailureCallback(Message msg) {

        return new Callback<>() {

            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                if (!response.isOk()) {
                    log.error("Error sending message to chat {}, error code {}",
                            msg.chat().id(), response.errorCode());
                }
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                log.error("IO error while sending message to chat {}", msg.chat().id(), e);
            }
        };
    }
}
