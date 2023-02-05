package dev.polluxus.mvlookup.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import dev.polluxus.mvlookup.client.MovieSearchResponse.MovieSearchResult;
import dev.polluxus.mvlookup.model.FilmResponseMessage;
import dev.polluxus.mvlookup.utils.TextUtils;
import dev.polluxus.mvlookup.model.MovieLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    @Inject
    MvLookupService mvLookupService;

    @Inject
    TelegramBot bot;

    public void handleUpdate(final Update update) {

        final Message msg = update.message();
        if (msg == null) {
            return;
        }

        final String text = msg.text();
        if (text == null || text.length() == 0) {
            return;
        }

        final MovieLookup lookup = TextUtils.parseAsMovieLookup(text);
        if (lookup == null) {
            return;
        }

        mvLookupService.lookup(lookup)
                .thenAccept(r -> {

                    final String mdContent;

                    if (r.results().isEmpty()) {
                        mdContent =
                                String.format("No movies found with title %s%s.",
                                        lookup.name(),
                                        lookup.year()
                                                .map(y -> " in year " + y)
                                                .orElse(""));
                    } else {

                        final MovieSearchResult res = r.results().get(0);

                        mdContent = new FilmResponseMessage(
                                res.title(),
                                res.releaseDate(),
                                res.overview(),
                                res.id()
                        ).toMarkdownMessage();
                    }

                    final SendMessage req = new SendMessage(msg.chat().id(), mdContent)
                            .parseMode(ParseMode.MarkdownV2);

                    bot.execute(req, new Callback<SendMessage, SendResponse>() {
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
                    });
                });
    }
}
