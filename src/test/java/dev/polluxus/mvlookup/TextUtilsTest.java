package dev.polluxus.mvlookup;

import dev.polluxus.mvlookup.model.MovieLookup;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TextUtilsTest {

    @ParameterizedTest
    @MethodSource("parseAsMovieLookupSucceedsParams")
    public void test_parseAsMovieLookupSucceeds(final String input, final MovieLookup expected) {

        final MovieLookup actual = TextUtils.parseAsMovieLookup(input);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> parseAsMovieLookupSucceedsParams() {
        return Stream.of(
                arguments("[[rashomon|1954]]", MovieLookup.of("rashomon", "1954")),
                arguments("[[rashomon]]", MovieLookup.of("rashomon")),
                arguments("[[    rashomon   |   1954    ]]", MovieLookup.of("rashomon", "1954"))
        );
    }

    @ParameterizedTest
    @MethodSource("parseAsMovieLookupFailsParams")
    public void test_parseAsMovieLookupFails(final String input) {

        final MovieLookup actual = TextUtils.parseAsMovieLookup(input);
        assertNull(actual);
    }

    static Stream<Arguments> parseAsMovieLookupFailsParams() {
        return Stream.of(
                arguments("[[rashomon"),
                arguments("rashomon]]"),
                arguments("rashomon")
        );
    }
}
