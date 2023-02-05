package dev.polluxus.mvlookup.model;

import dev.polluxus.mvlookup.utils.TextUtils;

public record FilmResponseMessage(
        String title,
        String releaseDate,
        String overview,
        int id
) {

    public String toMarkdownMessage() {

        return String.format(
                """
                *%s* \\(%s\\)
                                
                %s
                                
                [Letterboxd](https://letterboxd.com/tmdb/%d) \\| [TMDB](https://www.themoviedb.org/movie/%d)
                """,
                TextUtils.markdownV2Escape(title),
                TextUtils.markdownV2Escape(releaseDate),
                TextUtils.markdownV2Escape(overview),
                id,
                id
        );
    }
}
