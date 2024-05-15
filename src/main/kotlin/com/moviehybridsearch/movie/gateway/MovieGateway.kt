package com.moviehybridsearch.movie.gateway

import com.moviehybridsearch.movie.gateway.dto.ListMovieDTO
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class MovieGateway(
    private val restTemplate: RestTemplate,
) {
    fun getMovies(page: Int): Result<ListMovieDTO> {
        try {
            val uri =
                UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org")
                    .path("3/discover/movie")
                    .queryParam("include_adult", false)
                    .queryParam("include_video", false)
                    .queryParam("language", "en")
                    .queryParam("primary_release_date.gte", "2024-01-01")
                    .queryParam("sort_by", "popularity.desc")
                    .queryParam("vote_count.gte", 10)
                    .queryParam("page", page)
                    .build()
                    .toUri()

            val response = restTemplate.getForEntity(uri, ListMovieDTO::class.java)
            return response.body?.let { Result.success(it) } ?: run { Result.failure(Exception("No response body")) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
