package dev.polluxus.mvlookup.client.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbSearchResponse(
        @JsonProperty("page") int page,
        @JsonProperty("results") List<MovieSearchResult> results,
        @JsonProperty("total_results") int totalResults,
        @JsonProperty("total_pages") int totalPages

) {

    public record MovieSearchResult(
            @JsonProperty("poster_path") String posterPath,
            @JsonProperty("adult") boolean adult,
            @JsonProperty("overview") String overview,
            @JsonProperty("release_date") String releaseDate,
            @JsonProperty("genre_ids") List<Integer> genreIds,
            @JsonProperty("id") int id,
            @JsonProperty("original_title") String originalTitle,
            @JsonProperty("original_language") String originalLanguage,
            @JsonProperty("title") String title,
            @JsonProperty("backdrop_path") String backdropPath,
            @JsonProperty("popularity") double popularity,
            @JsonProperty("vote_count") int voteCount,
            @JsonProperty("video") boolean video,
            @JsonProperty("vote_average") int voteAverage
    ) {}
}
