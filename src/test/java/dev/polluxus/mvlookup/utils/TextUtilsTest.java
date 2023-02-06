package dev.polluxus.mvlookup.utils;

import dev.polluxus.mvlookup.request.MovieQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TextUtilsTest {

    @ParameterizedTest
    @MethodSource("parseAsMovieLookupSucceedsParams")
    public void test_parseAsMovieLookupSucceeds(final String input, final MovieQuery expected) {

        final MovieQuery actual = TextUtils.parseAsMovieQuery(input);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> parseAsMovieLookupSucceedsParams() {
        return Stream.of(
                arguments("[[rashomon|1950]]", MovieQuery.of("rashomon", "1950")),
                arguments("[[rashomon]]", MovieQuery.of("rashomon")),
                arguments("[[    rashomon   |   1950    ]]", MovieQuery.of("rashomon", "1950")),
                arguments("does anybody want to watch [[rashomon]] sometime soon?", MovieQuery.of("rashomon")),
                arguments("[[rashomon]] [[the third man]]", MovieQuery.of("rashomon"))
        );
    }

    @ParameterizedTest
    @MethodSource("parseAsMovieLookupFailsParams")
    public void test_parseAsMovieLookupFails(final String input) {

        final MovieQuery actual = TextUtils.parseAsMovieQuery(input);
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
