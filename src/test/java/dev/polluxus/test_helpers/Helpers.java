package dev.polluxus.test_helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;

public class Helpers {

    private static ObjectMapper objectMapper;

    /**
     * Decodes {@code input} to an object of class {@code klazz}, failing at runtime
     * if any exception occurs.
     */
    public static <T> T assertJsonParse(final String input, Class<T> klazz) {

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(input, klazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("FATAL: input %s was not parseable to klazz %s", input, klazz.getName())
            );
        }
    }

    /**
     * Creates a {@link Update} object containing a message populated
     * with the provided parameters.
     */
    public static Update createMessageUpdate(final String msgText, final String username, final Long chatId) {

        return BotUtils.fromJson(String.format("""
                {
                    "message": {
                        "text": "%s",
                        "from": {
                            "username": "%s"
                        },
                        "chat": {
                            "id": %d
                        }
                    }
                }
                """, msgText, username, chatId), Update.class);
    }
}
