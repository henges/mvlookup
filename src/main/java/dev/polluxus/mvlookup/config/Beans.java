package dev.polluxus.mvlookup.config;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import dev.polluxus.mvlookup.config.ConfigContainer.BotConfig;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class Beans {

    private static final Logger log = LoggerFactory.getLogger(Beans.class);

    @Inject
    Vertx vertx;

    @Inject
    BotConfig botConfig;

    @Produces
    @ApplicationScoped
    public WebClient webClient() {
        return WebClient.create(vertx);
    }

    @Produces
    @ApplicationScoped
    public TelegramBot telegramBot() {

        final TelegramBot bot = new TelegramBot(botConfig.botToken());

        if (!botConfig.enableWebhooks()) {
            log.info("Webhooks not enabled!");
            return bot;
        }

        final SetWebhook wh = new SetWebhook()
                .allowedUpdates("message")
                .certificate(new File(botConfig.sslCertPath()))
                .url(botConfig.botUrl());

        bot.execute(wh, new Callback<SetWebhook, BaseResponse>() {
            @Override
            public void onResponse(SetWebhook request, BaseResponse response) {
                if (!response.isOk()) {
                    log.error("Error mounting webhook, error code {}", response.errorCode());
                    System.exit(1);
                }
            }

            @Override
            public void onFailure(SetWebhook request, IOException e) {
                log.error("IO error while mounting webhook", e);
                System.exit(1);
            }
        });

        return bot;
    }
}
