package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.gateway.MovieGateway
import com.moviehybridsearch.movie.gateway.dto.MovieDTO

class CacheMoviesUseCase(
    private val movieGateway: MovieGateway,
) {
    fun execute(): Result<Unit> {
        var page = 0
        val firstPageResult =
            movieGateway.getMovies(page).fold(
                onSuccess = { it },
                onFailure = { return Result.failure(it) },
            )
        saveMovies(firstPageResult.results)
        val totalPages = firstPageResult.totalPages
        while (page < totalPages) {
            page++
            val pageResult =
                movieGateway.getMovies(page).fold(
                    onSuccess = { it },
                    onFailure = { return Result.failure(it) },
                )
            saveMovies(pageResult.results)
        }
        return Result.success(Unit)
    }

    private fun saveMovies(movies: List<MovieDTO>) {
    }
}
