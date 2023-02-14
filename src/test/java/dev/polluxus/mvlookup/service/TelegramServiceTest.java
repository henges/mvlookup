package dev.polluxus.mvlookup.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.config.ConfigContainer.ServiceConfig;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.test_helpers.Helpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.polluxus.test_helpers.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    LookupService lookupService;

    @Mock
    TelegramBot bot;

    @Captor
    ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    TelegramService service;

    @BeforeEach
    public void setup() {
        service = new TelegramService(lookupService, bot, new ServiceConfig() {
            @Override
            public int downstreamRateLimit() {
                return Integer.parseInt(ServiceConfig.DOWNSTREAM_RATE_LIMIT_DEFAULT);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("testHandleUpdateSucceedsArgs")
    public void testHandleUpdateSucceeds(final String query, final String username,
                                 final Long chatId, Map<MovieQuery, TmdbSearchResponse> apiResponses, String expected) {

        final Update update = Helpers.createMessageUpdate(query, username, chatId);

        apiResponses.forEach((key, value) ->
                when(lookupService.lookupTmdb(key)).thenReturn(CompletableFuture.completedFuture(value))
        );

        doAnswer(i -> null).when(bot).execute(any(SendMessage.class), argThat(i -> true));

        service.handleUpdate(update).toCompletableFuture().join();

        verify(bot).execute(sendMessageArgumentCaptor.capture(), any());
        final Map<String, Object> actual = sendMessageArgumentCaptor.getValue().getParameters();

        assertEquals(actual.get("chat_id"), chatId);
        assertEquals(actual.get("text"), expected);
    }

    public static Stream<Arguments> testHandleUpdateSucceedsArgs() {
        return Stream.of(
                arguments(
                        "wanna watch [[wavelength|1967]]?", "alex", 123L,
                        Map.of(MovieQuery.of("wavelength", "1967"), TMDB_WAVELENGTH_RESPONSE),
                        MVLOOKUP_WAVELENGTH_RESPONSE_SHORT
                ),
                arguments(
                        "before [[ลุงบุญมีระลึกชาติ]] and other text", "ella", 456L,
                        Map.of(MovieQuery.of("ลุงบุญมีระลึกชาติ"), TMDB_UNCLE_BOONMEE_RESPONSE),
                        MVLOOKUP_UNCLE_BOONMEE_RESPONSE_SHORT
                ),
                arguments(
                        "before [[the third man]] and other text [[wavelength]] more", "ella", 456L,
                        Map.of(
                                MovieQuery.of("the third man"), TMDB_THE_THIRD_MAN_RESPONSE,
                                MovieQuery.of("wavelength"), TMDB_WAVELENGTH_RESPONSE
                        ),
                        String.join("\n",
                                MVLOOKUP_THE_THIRD_MAN_RESPONSE_SHORT,
                                MVLOOKUP_WAVELENGTH_RESPONSE_SHORT
                        )
                ),
                // Test deduplication
                arguments(
                        Stream.iterate("[[the third man]]", UnaryOperator.identity())
                                .limit(Long.parseLong(ServiceConfig.DOWNSTREAM_RATE_LIMIT_DEFAULT) + 1)
                                .collect(Collectors.joining(" ")), "ella", 456L,
                        Map.of(
                                MovieQuery.of("the third man"), TMDB_THE_THIRD_MAN_RESPONSE
                        ),
                        MVLOOKUP_THE_THIRD_MAN_RESPONSE_SHORT
                ),
                testHandleUpdateSucceeds_rateLimitArgs()
        );
    }

    static Arguments testHandleUpdateSucceeds_rateLimitArgs() {

        final int limit = Integer.parseInt(ServiceConfig.DOWNSTREAM_RATE_LIMIT_DEFAULT);

        return arguments(
                        Stream.iterate(0, e -> e + 1)
                                .map(e -> "[[" + e + "]]")
                                .limit(limit + 5)
                                .collect(Collectors.joining(" ")), "ella", 456L
                        ,
                        Stream.iterate(0, e -> e + 1)
                                .limit(limit)
                                .collect(Collectors.toMap(
                                        e -> MovieQuery.of(String.valueOf(e)),
                                        e -> Helpers.createTmdbSearchResponse(String.valueOf(e), "2000-01-01", e)
                                ))
                        ,
                        Stream.iterate(0, e -> e + 1)
                                .limit(limit)
                                .map(e -> String.format(
                                        """
                                        *%d* \\(2000\\-01\\-01\\) \\- [Letterboxd](https://letterboxd.com/tmdb/%d) \\| [TMDB](https://www.themoviedb.org/movie/%d)
                                        """, e, e, e
                                ))
                                .collect(Collectors.joining("\n"))
                );
    }

    @ParameterizedTest
    @MethodSource("testHandleUpdate_missingFieldsParams")
    public void testHandleUpdate_missingFields_exitsWithoutLookup(final Update update) {

        service.handleUpdate(update).toCompletableFuture().join();

        verify(lookupService, times(0)).lookupTmdb(any());
    }

    static Stream<Arguments> testHandleUpdate_missingFieldsParams() {
        return Stream.of(
                arguments(new Update()),
                arguments(BotUtils.parseUpdate("{\"message\": {} }")),
                arguments(BotUtils.parseUpdate("{\"message\": { \"text\": \"\" } }")),
                arguments(BotUtils.parseUpdate("{\"message\": { \"text\": \"no query here\" } }"))
        );
    }
}
