package dev.polluxus.mvlookup;

import dev.polluxus.mvlookup.model.MovieLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final Logger log = LoggerFactory.getLogger(TextUtils.class);

    private static final Pattern PATTERN = Pattern.compile("\\[\\[([^|\\]]*)[|]?([^|\\]]*)]]");

    public static MovieLookup parseAsMovieLookup(final String input) {

        final Matcher m = PATTERN.matcher(input);

        if (!m.find()) {
            log.trace("No match for input string {}", input);
            return null;
        }

        final String name = m.group(1);
        final String year = m.group(2);
        if (isBlank(name)) {
            log.error("Invalid state for input {}: `name` was blank", input);
            return null;
        }
        final Optional<Year> parsedYear = Optional.ofNullable(year)
                .filter(s -> !isBlank(s))
                .map(String::trim)
                .flatMap(TextUtils::parseYear);

        return new MovieLookup(name.trim(), parsedYear);
    }

    public static boolean isBlank(final String input) {

        return input == null || input.length() == 0;
    }

    public static Optional<Year> parseYear(final String input) {
        try {
            return Optional.of(Year.parse(input));
        } catch (DateTimeParseException e) {
            log.error("Input {} was not parseable to year", input, e);
            return Optional.empty();
        }
    }
}
