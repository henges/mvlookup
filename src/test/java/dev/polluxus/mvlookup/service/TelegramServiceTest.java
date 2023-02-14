package dev.polluxus.mvlookup.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.request.MovieQuery;
import dev.polluxus.test_helpers.Helpers;
import dev.polluxus.test_helpers.TestData;
import dev.polluxus.mvlookup.utils.TextUtils;
import org.graalvm.collections.Pair;
import org.graalvm.polyglot.TypeLiteral;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    MvLookupService mvLookupService;

    @Mock
    TelegramBot bot;

    @Captor
    ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @InjectMocks
    TelegramService service;

    @ParameterizedTest
    @MethodSource("testHandleUpdateSucceedsArgs")
    public void testTmdbLookupSucceeds(final String query, final String username,
                                 final Long chatId, TmdbSearchResponse apiResponse, String expected) {

        final Update update = Helpers.createMessageUpdate(query, username, chatId);

        final List<MovieQuery> queries = TextUtils.parseAsMovieQueries(query);

        when(mvLookupService.lookupTmdb(queries))
                .thenReturn(CompletableFuture.completedFuture(List.of(Pair.create(queries.get(0), apiResponse))));

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
                        TestData.TMDB_WAVELENGTH_RESPONSE, TestData.MVLOOKUP_WAVELENGTH_RESPONSE_SHORT
                ),
                arguments(
                        "before [[ลุงบุญมีระลึกชาติ]] and other text", "ella", 456L,
                        TestData.TMDB_UNCLE_BOONMEE_RESPONSE,
                        TestData.MVLOOKUP_UNCLE_BOONMEE_RESPONSE_SHORT
                )
        );
    }

    final TypeLiteral<List<MovieQuery>> tt = new TypeLiteral<>() {
    };
    
    @ParameterizedTest
    @MethodSource("testHandleUpdate_missingFieldsParams")
    public void testHandleUpdate_missingFields_exitsWithoutLookup(final Update update) {

        service.handleUpdate(update).toCompletableFuture().join();

        verify(mvLookupService, times(0)).lookupTmdb(any(tt.getRawType()));
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
