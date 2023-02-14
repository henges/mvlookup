package dev.polluxus.mvlookup.utils;

import dev.polluxus.mvlookup.request.MovieQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TextUtilsTest {

    @ParameterizedTest
    @MethodSource("parseAsMovieQueries_Single_SucceedsParams")
    public void test_parseAsMovieQueries_Single_Succeeds(final String input, final MovieQuery expected) {

        final List<MovieQuery> actual = TextUtils.parseAsMovieQueries(input);
        assertEquals(expected, actual.get(0));
    }

    static Stream<Arguments> parseAsMovieQueries_Single_SucceedsParams() {
        return Stream.of(
                arguments("[[rashomon|1950]]", MovieQuery.of("rashomon", "1950")),
                arguments("[[rashomon]]", MovieQuery.of("rashomon")),
                arguments("[[    rashomon   |   1950    ]]", MovieQuery.of("rashomon", "1950")),
                arguments("does anybody want to watch [[rashomon]] sometime soon?", MovieQuery.of("rashomon")),
                arguments("[[rashomon]] [[the third man]]", MovieQuery.of("rashomon"))
        );
    }

    @ParameterizedTest
    @MethodSource("parseAsMovieQueries_List_SucceedsParams")
    public void test_parseAsMovieQueries_List_Succeeds(final String input, final List<MovieQuery> expected) {

        final List<MovieQuery> actual = TextUtils.parseAsMovieQueries(input);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> parseAsMovieQueries_List_SucceedsParams() {
        return Stream.of(
                arguments("[[rashomon]] [[the third man]]", List.of(
                        MovieQuery.of("rashomon"),
                        MovieQuery.of("the third man"))
                ),
                arguments("something [[rashomon]] something [[the third man]] something", List.of(
                        MovieQuery.of("rashomon"),
                        MovieQuery.of("the third man"))
                ),
                arguments("""
                        [[a]] [[a]] [[a]] [[a]] [[a]]
                        [[a]] [[a]] [[a]] [[a]] [[a]]
                        [[a]] [[a]] [[a]] [[a]] [[a]]
                        [[a]] [[a]] [[a]] [[a]] [[a]]
                        [[a]] [[a]] [[a]] [[a]] [[a]]
                        """, Stream.iterate(MovieQuery.of("a"), UnaryOperator.identity())
                                    .limit(25)
                                    .toList()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("parseAsMovieQueriesFailsParams")
    public void test_parseAsMovieQueriesFails(final String input) {

        final List<MovieQuery> actual = TextUtils.parseAsMovieQueries(input);
        assertEquals(0, actual.size());
    }

    static Stream<Arguments> parseAsMovieQueriesFailsParams() {
        return Stream.of(
                arguments("[[rashomon"),
                arguments("rashomon]]"),
                arguments("rashomon"),
                arguments("[[]]"),
                arguments("[[|]]"),
                arguments("[]"),
                arguments("[|]")
        );
    }
}
