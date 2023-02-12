package dev.polluxus.mvlookup.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.Optional;

public interface ConfigContainer {

    String X_TELEGRAM_BOT_API_SECRET_TOKEN = "X-Telegram-Bot-Api-Secret-Token";

    @ConfigMapping(prefix = "mvlookup")
    interface BotConfig {

        @WithName("bot.token")
        String botToken();

        @WithName("bot.url")
        String botUrl();

        @WithName("shared.secret")
        Optional<String> sharedSecret();

        @WithName("enable.webhooks")
        @WithDefault("false")
        boolean enableWebhooks();

        @WithName("telegram.api.url")
        @WithDefault("https://api.telegram.org/bot")
        String telegramUrl();
    }

    @ConfigMapping(prefix = "tmdb")
    interface TmdbClientConfig {

        @WithName("api.url")
        @WithDefault("https://api.themoviedb.org/3")
        String baseUrl();

        @WithName("api.token")
        String apiToken();
    }
}
