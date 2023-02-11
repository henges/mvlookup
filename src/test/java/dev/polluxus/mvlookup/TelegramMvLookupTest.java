package dev.polluxus.mvlookup;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import dev.polluxus.mvlookup.config.ConfigContainer.BotConfig;
import dev.polluxus.mvlookup.config.ConfigContainer.TmdbClientConfig;
import dev.polluxus.mvlookup.test_helpers.Helpers;
import dev.polluxus.mvlookup.test_helpers.MvLookupProfile;
import dev.polluxus.mvlookup.test_helpers.TelegramApiWireMockResource;
import dev.polluxus.mvlookup.test_helpers.TelegramApiWireMockResource.InjectWireMock;
import dev.polluxus.mvlookup.test_helpers.TestData;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;

import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@QuarkusTest
@QuarkusTestResource(TelegramApiWireMockResource.class)
@TestHTTPEndpoint(TelegramMvLookup.class)
@TestProfile(MvLookupProfile.class)
public class TelegramMvLookupTest {

    public static final UrlPattern TMDB_PATTERN = urlMatching("/tmdb/.*");
    public static final UrlPattern TELEGRAM_PATTERN = urlMatching("/telegram.*");

    @InjectWireMock
    WireMockServer mocks;

    @Inject
    BotConfig botCfg;

    @Inject
    TmdbClientConfig tmdbConfig;

    @ParameterizedTest
    @MethodSource("testSucceedsArgs")
    public void test(final Update upd, String title, String year, String tmdbResult, String ourResult) {

        String updStr = BotUtils.toJson(upd);

        mocks.givenThat(get(TMDB_PATTERN)
                .withQueryParams(year == null
                        ? Map.of("query", equalTo(title))
                        : Map.of(
                        "query", equalTo(title),
                        "year", equalTo(year))
                )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(tmdbResult)));

        mocks.givenThat(post(TELEGRAM_PATTERN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"ok\": true}")));

        given()
                .body(updStr)
                .header("Content-Type", "application/json")
                .header(TelegramMvLookup.X_TELEGRAM_BOT_API_SECRET_TOKEN, "none")
        .when()
                .post()
                .then()
                .assertThat()
                .statusCode(204);

        final String actual = Helpers.getTelegramRequest(mocks, postRequestedFor(TELEGRAM_PATTERN));

        final String expected = Helpers.markdownV2Of(upd.message().chat().id(), ourResult);

        Assertions.assertEquals(actual, expected);
    }

    public static Stream<Arguments> testSucceedsArgs() {
        return Stream.of(
                arguments(
                        Helpers.createUpdate("wanna watch [[wavelength|1967]]?", "alex", 123L),
                        "wavelength", "1967", TestData.TMDB_WAVELENGTH_RESPONSE, TestData.MVLOOKUP_WAVELENGTH_RESPONSE_SHORT
                )
//                ,
//                arguments(
//                        Helpers.createUpdate("before [[ลุงบุญมีระลึกชาติ]] and other text", "ella", 456L),
//                        "ลุงบุญมีระลึกชาติ", null, TestData.TMDB_UNCLE_BOONMEE_RESPONSE,
//                        TestData.MVLOOKUP_UNCLE_BOONMEE_RESPONSE_SHORT
//                )
        );
    }


}
