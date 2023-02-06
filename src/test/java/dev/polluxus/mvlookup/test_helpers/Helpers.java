package dev.polluxus.mvlookup.test_helpers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;

public class Helpers {

    public static String parseQueryString(final LoggedRequest request) {

        return URLDecoder.decode(request.getBodyAsString(), StandardCharsets.UTF_8);
    }

    public static String getTelegramRequest(final WireMockServer mocks, RequestPatternBuilder pattern) {

        return getTelegramMessages(mocks, pattern, 1).get(0);
    }

    public static List<String> getTelegramMessages(final WireMockServer mocks, RequestPatternBuilder pattern,
                                                   int count) {

        final List<LoggedRequest> reqs = mocks.findAll(pattern);
        Assertions.assertEquals(count, reqs.size());

        return reqs.stream()
                .map(Helpers::parseQueryString)
                .toList();
    }

    public static String markdownV2Of(Long chatId, String text) {

        return String.format("chat_id=%s&text=%s&parse_mode=MarkdownV2", chatId, text);
    }


    /**
     * We need to use mocks to create updates
     * @param msgText
     * @return
     */
    public static Update createUpdateMock(final String msgText) {

        return createUpdateMock(null, null, msgText);
    }

    public static Update createUpdateMock(final Long chatId, final String username, final String msgText) {

        Chat chat = Mockito.mock(Chat.class);
        when(chat.username()).thenReturn(username);
        when(chat.id()).thenReturn(chatId);

        Message msg = Mockito.mock(Message.class);
        when(msg.text()).thenReturn(msgText);
        when(msg.chat()).thenReturn(chat);

        Update ret = Mockito.mock(Update.class);
        when(ret.message()).thenReturn(msg);

        return ret;
    }


    public static String createUpdateJson(final String msgText, final Long chatId) {

        return BotUtils.toJson(createUpdate(msgText, chatId));
    }

    public static String createUpdateJson(final String msgText, final String username, final Long chatId) {

        return BotUtils.toJson(createUpdate(msgText, username, chatId));
    }

    public static Update createUpdate(final String msgText, final Long chatId) {

        return BotUtils.fromJson(String.format("""
                {
                    "message": {
                        "text": "%s",
                        "chat": {
                            "id": %d
                        }
                    }
                }
                """, msgText, chatId), Update.class);
    }

    public static Update createUpdate(final String msgText, final String username, final Long chatId) {

        return BotUtils.fromJson(String.format("""
                {
                    "message": {
                        "text": "%s",
                        "chat": {
                            "username": "%s",
                            "id": %d
                        }
                    }
                }
                """, msgText, username, chatId), Update.class);
    }
}
