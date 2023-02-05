package dev.polluxus.mvlookup.model;

public record FilmResponseMessage(
        String title,
        String releaseDate,
        String overview,
        int id
) {

    public String toMarkdownMessage() {

        return String.format(
                """
                **{%s}** ({%s})
                                
                {%s}
                                
                [View on TMDB](https://www.themoviedb.org/movie/{%d})
                """,
                title, releaseDate, overview, id
        );
    }
}
