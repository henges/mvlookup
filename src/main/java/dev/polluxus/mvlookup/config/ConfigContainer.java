package dev.polluxus.mvlookup.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

public interface ConfigContainer {

    @ConfigMapping(prefix = "mvlookup")
    interface BotConfig {

        @WithName("bot.token")
        String botToken();

        @WithName("bot.url")
        String botUrl();

        @WithName("enable.webhooks")
        @WithDefault("false")
        boolean enableWebhooks();

        @WithName("ssl.cert.path")
        @WithDefault("none")
        String sslCertPath();

        @WithName("shared.secret")
        @WithDefault("none")
        String sharedSecret();

        @WithName("telegram.api.url")
        @WithDefault("https://api.telegram.org/bot")
        String telegramUrl();
    }

    @ConfigMapping(prefix = "mvlookup.tmdb")
    interface TmdbClientConfig {

        @WithName("api.url")
        @WithDefault("https://api.themoviedb.org/3")
        String baseUrl();

        @WithName("api.token")
        String apiToken();
    }

    @ConfigMapping(prefix = "letterboxd")
    interface LetterboxdClientConfig {

        @WithName("base.url")
        String baseUrl();

        @WithName("username")
        String username();

        @WithName("password")
        String password();
    }
}
