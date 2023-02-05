package dev.polluxus.mvlookup.model;

import java.time.Year;
import java.util.Optional;

public record MovieLookup(
        String name,
        Optional<Year> year
) {

    public static MovieLookup of(final String name, final String year) {
        return new MovieLookup(name, Optional.of(Year.parse(year)));
    }

    public static MovieLookup of(final String name) {
        return new MovieLookup(name, Optional.empty());
    }
}
