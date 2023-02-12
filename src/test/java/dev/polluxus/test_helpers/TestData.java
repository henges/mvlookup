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
    }

    public static final TmdbSearchResponse TMDB_WAVELENGTH_RESPONSE =
            Helpers.assertJsonParse(Json.TMDB_WAVELENGTH_RESPONSE_JSON, TmdbSearchResponse.class);

    public static final TmdbSearchResponse TMDB_UNCLE_BOONMEE_RESPONSE =
            Helpers.assertJsonParse(Json.TMDB_UNCLE_BOONMEE_RESPONSE_JSON, TmdbSearchResponse.class);

    public static final String MVLOOKUP_WAVELENGTH_RESPONSE_SHORT =
            """
            *Wavelength* \\(1967\\-03\\-17\\) \\- [Letterboxd](https://letterboxd.com/tmdb/88421) \\| [TMDB](https://www.themoviedb.org/movie/88421)
            """;

    public static final String MVLOOKUP_UNCLE_BOONMEE_RESPONSE_SHORT =
            """
            *Uncle Boonmee Who Can Recall His Past Lives* \\(2010\\-06\\-25\\) \\- [Letterboxd](https://letterboxd.com/tmdb/38368) \\| [TMDB](https://www.themoviedb.org/movie/38368)
            """;


}
