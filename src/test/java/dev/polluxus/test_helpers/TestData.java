package dev.polluxus.test_helpers;

import dev.polluxus.mvlookup.client.tmdb.response.TmdbSearchResponse;

public class TestData {

    static class Json {

        public static final String TMDB_WAVELENGTH_RESPONSE_JSON =
                """
                {
                    "page": 1,
                    "results": [
                        {
                            "adult": false,
                            "backdrop_path": "/sqb6J9GohKpqoeTLsG0pgP7AXg7.jpg",
                            "genre_ids": [
                                18
                            ],
                            "id": 88421,
                            "original_language": "en",
                            "original_title": "Wavelength",
                            "overview": "Wavelength consists of almost no action, and what action does occur is largely elided. If the film could be said to have a conventional plot, this would presumably refer to the three “character” scenes. In the first scene two people enter a room, chat briefly, and listen to “Strawberry Fields Forever” on the radio. Later, a man (played by filmmaker Hollis Frampton) enters inexplicably and dies on the floor. And last, the female owner of the apartment is heard and seen on the phone, speaking, with strange calm, about the dead man in her apartment whom she has never seen before.",
                            "popularity": 3.778,
                            "poster_path": "/m4tyxFi2I2W70Z5gQCobR7WG6S3.jpg",
                            "release_date": "1967-03-17",
                            "title": "Wavelength",
                            "video": false,
                            "vote_average": 5.3,
                            "vote_count": 72
                        }
                    ],
                    "total_pages": 1,
                    "total_results": 1
                }
                """;
        public static final String TMDB_UNCLE_BOONMEE_RESPONSE_JSON =
                """
                {
                    "page": 1,
                    "results": [
                        {
                            "adult": false,
                            "backdrop_path": "/6KvTXWweemPG4wK3XCFsCPxKZp1.jpg",
                            "genre_ids": [
                                18,
                                14
                            ],
                            "id": 38368,
                            "original_language": "th",
                            "original_title": "ลุงบุญมีระลึกชาติ",
                            "overview": "Suffering from acute kidney failure, Boonmee has chosen to spend his final days surrounded by his loved ones in the countryside. Surprisingly, the ghost of his deceased wife appears to care for him, and his long lost son returns home in a non-human form. Contemplating the reasons for his illness, Boonmee treks through the jungle with his family to a mysterious hilltop cave—the birthplace of his first life.",
                            "popularity": 8.491,
                            "poster_path": "/iZinYNVaBLg3pj4e8IXwvfXAtrI.jpg",
                            "release_date": "2010-06-25",
                            "title": "Uncle Boonmee Who Can Recall His Past Lives",
                            "video": false,
                            "vote_average": 6.6,
                            "vote_count": 301
                        }
                    ],
                    "total_pages": 1,
                    "total_results": 1
                }
                """;

        // Actual response trimmed for space + relevance reasons
        public static final String TMDB_THE_THIRD_MAN_RESPONSE_JSON =
                """
                {
                    "page": 1,
                    "results": [
                        {
                            "adult": false,
                            "backdrop_path": "/mYI1VlxvuSnHoK6GYwkFzAgcJXh.jpg",
                            "genre_ids": [
                                53,
                                9648
                            ],
                            "id": 1092,
                            "original_language": "en",
                            "original_title": "The Third Man",
                            "overview": "In postwar Vienna, Austria, Holly Martins, a writer of pulp Westerns, arrives penniless as a guest of his childhood chum Harry Lime, only to learn he has died. Martins develops a conspiracy theory after learning of a \\"third man\\" present at the time of Harry's death, running into interference from British officer Major Calloway, and falling head-over-heels for Harry's grief-stricken lover, Anna.",
                            "popularity": 17.846,
                            "poster_path": "/oIF3l7Dxp7Eyye10BNyM611wtKa.jpg",
                            "release_date": "1949-08-31",
                            "title": "The Third Man",
                            "video": false,
                            "vote_average": 7.981,
                            "vote_count": 1514
                        }
                    ],
                    "total_pages": 1,
                    "total_results": 17
                }
                """;
    }

    public static final TmdbSearchResponse TMDB_WAVELENGTH_RESPONSE =
            Helpers.assertJsonParse(Json.TMDB_WAVELENGTH_RESPONSE_JSON, TmdbSearchResponse.class);

    public static final TmdbSearchResponse TMDB_UNCLE_BOONMEE_RESPONSE =
            Helpers.assertJsonParse(Json.TMDB_UNCLE_BOONMEE_RESPONSE_JSON, TmdbSearchResponse.class);

    public static final TmdbSearchResponse TMDB_THE_THIRD_MAN_RESPONSE =
            Helpers.assertJsonParse(Json.TMDB_THE_THIRD_MAN_RESPONSE_JSON, TmdbSearchResponse.class);


    public static final String MVLOOKUP_WAVELENGTH_RESPONSE_SHORT =
            """
            *Wavelength* \\(1967\\-03\\-17\\) \\- [Letterboxd](https://letterboxd.com/tmdb/88421) \\| [TMDB](https://www.themoviedb.org/movie/88421)
            """;

    public static final String MVLOOKUP_UNCLE_BOONMEE_RESPONSE_SHORT =
            """
            *Uncle Boonmee Who Can Recall His Past Lives* \\(2010\\-06\\-25\\) \\- [Letterboxd](https://letterboxd.com/tmdb/38368) \\| [TMDB](https://www.themoviedb.org/movie/38368)
            """;


    public static final String MVLOOKUP_THE_THIRD_MAN_RESPONSE_SHORT =
            """
            *The Third Man* \\(1949\\-08\\-31\\) \\- [Letterboxd](https://letterboxd.com/tmdb/1092) \\| [TMDB](https://www.themoviedb.org/movie/1092)
            """;

}
