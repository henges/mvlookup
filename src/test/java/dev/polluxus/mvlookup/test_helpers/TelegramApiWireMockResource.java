package dev.polluxus.mvlookup.test_helpers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import javax.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public class TelegramApiWireMockResource implements QuarkusTestResourceLifecycleManager {

    WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        wireMockServer.start();

        final String telegramUrl = "http://localhost:" + wireMockServer.port() + "/telegram";
        final String tmdbUrl = "http://localhost:" + wireMockServer.port() + "/tmdb";

        return Map.of(
                "mvlookup.telegram.api.url", telegramUrl,
                "mvlookup.tmdb.api.url", tmdbUrl);
    }

    @Override
    public synchronized void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface InjectWireMock {


    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class,
                WireMockServer.class));
    }
}
