package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.gateway.MovieGateway
import com.moviehybridsearch.movie.gateway.dto.MovieDTO
import com.moviehybridsearch.movie.repo.MoveRepository
import com.moviehybridsearch.movie.shared.MovieMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CacheMoviesUseCase(
    private val movieGateway: MovieGateway,
    private val movieMapper: MovieMapper,
    private val moveRepository: MoveRepository,
) {
    suspend fun execute(): Result<Unit> {
        try {
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
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private suspend fun saveMovies(movies: List<MovieDTO>) {
        val entities = movies.map { movieMapper.dtoToEntity(it) }
        withContext(Dispatchers.IO) {
            moveRepository.saveAll(entities)
        }
    }
}
