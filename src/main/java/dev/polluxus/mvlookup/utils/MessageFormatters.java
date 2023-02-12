package dev.polluxus.mvlookup.utils;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;
import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse.TmdbMovieSearchResult;
import dev.polluxus.mvlookup.request.MovieQuery;

/**
 * Class for functions that transform a result into a MarkdownV2-compliant
 * string. Please be wary of the <a href="https://core.telegram.org/bots/api#markdownv2-style">requirements</a>
 * of the Telegram-side Markdown parser.
 */
public class MessageFormatters {

    private static String empty(MovieQuery req) {

        return String.format("No movies found with title %s%s\\.",
                TextUtils.markdownV2Escape(req.name()),
                req.year()
                        .map(y -> " in year " + y)
                        .orElse(""));
    }

    public static String tmdbShortFormat(MovieQuery req, TmdbSearchResponse resp) {

        if (resp.results().isEmpty()) { return empty(req); }

        final TmdbMovieSearchResult res = resp.results().get(0);

        return String.format(
                """
                *%s* \\(%s\\) \\- [Letterboxd](https://letterboxd.com/tmdb/%d) \\| [TMDB](https://www.themoviedb.org/movie/%d)
                """,
                TextUtils.markdownV2Escape(res.title()),
                TextUtils.markdownV2Escape(res.releaseDate()),
                res.id(),
                res.id()
        );
    }

    public static String tmdbLongFormat(MovieQuery req, TmdbSearchResponse resp) {

        if (resp.results().isEmpty()) {
            return empty(req);
        }

        final TmdbMovieSearchResult res = resp.results().get(0);

        return String.format(
                """
                *%s* \\(%s\\)
                                
                %s
                                
                [Letterboxd](https://letterboxd.com/tmdb/%d) \\| [TMDB](https://www.themoviedb.org/movie/%d)
                """,
                TextUtils.markdownV2Escape(res.title()),
                TextUtils.markdownV2Escape(res.releaseDate()),
                TextUtils.markdownV2Escape(res.overview()),
                res.id(),
                res.id()
        );
    }
}
