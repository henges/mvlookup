package dev.polluxus.mvlookup;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;

@ApplicationScoped
public class MvLookupService {

    public static final Pattern PATTERN = Pattern.compile("\\[\\[([^|\\]]*)[|]?([^|\\]]*)");

    public void handleUpdate(final Update update) {

        final Message msg = update.message();
        if (msg == null) {
            return;
        }

        final String text = msg.text();
        if (text == null || text.length() == 0) {
            return;
        }
    }
}
