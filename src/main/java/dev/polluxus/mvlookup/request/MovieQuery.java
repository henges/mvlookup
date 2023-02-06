package dev.polluxus.mvlookup.request;

import java.time.Year;
import java.util.Optional;

public record MovieQuery(
        String name,
        Optional<Year> year
) {

    public static MovieQuery of(final String name, final String year) {
        return new MovieQuery(name, Optional.of(Year.parse(year)));
    }

    public static MovieQuery of(final String name) {
        return new MovieQuery(name, Optional.empty());
    }
}
