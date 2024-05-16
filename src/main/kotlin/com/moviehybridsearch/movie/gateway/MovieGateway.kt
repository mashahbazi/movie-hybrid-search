package com.moviehybridsearch.movie.gateway

import com.moviehybridsearch.movie.gateway.dto.ListMovieDTO
import com.moviehybridsearch.movie.shared.MovieConfig
import com.moviehybridsearch.shared.extension.logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class MovieGateway(
    private val restTemplate: RestTemplate,
    private val movieConfig: MovieConfig,
) {
    fun getMovies(
        year: Int,
        page: Int,
    ): Result<ListMovieDTO> {
        try {
            val uri =
                UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org")
                    .path("3/discover/movie")
                    .queryParam("include_adult", false)
                    .queryParam("include_video", false)
                    .queryParam("language", "en")
                    .queryParam("primary_release_date.gte", "$year-01-01")
                    .queryParam("primary_release_date.lte", "${year + 1}-01-01")
                    .queryParam("sort_by", "popularity.desc")
                    .queryParam("vote_count.gte", 10)
                    .queryParam("page", page)
                    .build()
                    .toUri()

            val httpEntity =
                HttpEntity<String>(
                    HttpHeaders().apply {
                        set("Authorization", "Bearer ${movieConfig.apiToken}")
                    },
                )

            val response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, ListMovieDTO::class.java)
            return response.body?.let { Result.success(it) } ?: run { Result.failure(Exception("No response body")) }
        } catch (e: Exception) {
            logger.error(e.toString())
            return Result.failure(e)
        }
    }
}
