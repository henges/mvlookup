package dev.polluxus.mvlookup.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import dev.polluxus.mvlookup.config.ConfigContainer.ServiceConfig;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.mvlookup.utils.FutureUtils;
import dev.polluxus.mvlookup.utils.MessageFormatters;
import dev.polluxus.mvlookup.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);
    public static final int DOWNSTREAM_RATE_LIMIT = 5;

    private final LookupService lookupService;

    private final TelegramBot bot;

    private final ServiceConfig config;

    @Inject
    public TelegramService(final LookupService lookupService, final TelegramBot bot, final ServiceConfig config) {
        this.lookupService = lookupService;
        this.bot = bot;
        this.config = config;
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

        final List<MovieQuery> queries = TextUtils.parseAsMovieQueries(text);
        if (queries.isEmpty()) {
            return FutureUtils.done();
        }

        log.info("Looking up {} for {}", queries, update.message().from().username());

        final List<CompletionStage<String>> formattedResults = queries.stream()
                .distinct()                             // Deduplicate
                .limit(config.downstreamRateLimit())    // Don't overload our data sources
                .map(q -> lookupService.lookupTmdb(q)
                        .thenApply(r -> MessageFormatters.tmdbShortFormat(q, r)))
                .toList();

        return FutureUtils.fluxToMono(formattedResults)
                .thenAccept(r -> {
                    final String body = String.join("\n", r);
                    final SendMessage req = new SendMessage(msg.chat().id(), body).parseMode(ParseMode.MarkdownV2);
                    log.debug("Final message body (plus method field): {}", req.toWebhookResponse());

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
