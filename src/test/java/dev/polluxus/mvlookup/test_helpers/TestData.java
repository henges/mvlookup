package dev.polluxus.mvlookup.test_helpers;

public class TestData {

    public static final String TMDB_RASHOMON_QUERY =
            """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/zyO6j74DKMWfp5snWg6Hwo0T3Mz.jpg",
                        "genre_ids": [
                            80,
                            18,
                            9648
                        ],
                        "id": 548,
                        "original_language": "ja",
                        "original_title": "羅生門",
                        "overview": "Brimming with action while incisively examining the nature of truth, \\"Rashomon\\" is perhaps the finest film ever to investigate the philosophy of justice. Through an ingenious use of camera and flashbacks, Kurosawa reveals the complexities of human nature as four people recount different versions of the story of a man's murder and the rape of his wife.",
                        "popularity": 13.49,
                        "poster_path": "/vL7Xw04nFMHwnvXRFCmYYAzMUvY.jpg",
                        "release_date": "1950-08-26",
                        "title": "Rashomon",
                        "video": false,
                        "vote_average": 8.1,
                        "vote_count": 1791
                    }
                ],
                "total_pages": 1,
                "total_results": 1
            }
            """;

    public static final String TMDB_WAVELENGTH_RESPONSE =
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

    public static final String MVLOOKUP_WAVELENGTH_RESPONSE_LONG =
    """
    "*Wavelength* \\(1967\\-03\\-17\\)\n\nWavelength consists of almost no action, and what action does occur is largely elided\\. If the film could be said to have a conventional plot, this would presumably refer to the three “character” scenes\\. In the first scene two people enter a room, chat briefly, and listen to “Strawberry Fields Forever” on the radio\\. Later, a man \\(played by filmmaker Hollis Frampton\\) enters inexplicably and dies on the floor\\. And last, the female owner of the apartment is heard and seen on the phone, speaking, with strange calm, about the dead man in her apartment whom she has never seen before\\.\n\n[Letterboxd](https://letterboxd.com/tmdb/88421) \\| [TMDB](https://www.themoviedb.org/movie/88421)\n"
    """;

    public static final String MVLOOKUP_WAVELENGTH_RESPONSE_SHORT =
    """
    *Wavelength* \\(1967\\-03\\-17\\) \\- [Letterboxd](https://letterboxd.com/tmdb/88421) \\| [TMDB](https://www.themoviedb.org/movie/88421)
    """;
}
