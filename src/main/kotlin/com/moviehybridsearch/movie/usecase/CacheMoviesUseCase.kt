package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.gateway.MovieGateway
import com.moviehybridsearch.movie.gateway.dto.MovieDTO
import com.moviehybridsearch.movie.repo.MoveRepository
import com.moviehybridsearch.movie.shared.MovieMapper
import com.moviehybridsearch.shared.extension.logger
import org.springframework.stereotype.Component

@Component
class CacheMoviesUseCase(
    private val movieGateway: MovieGateway,
    private val movieMapper: MovieMapper,
    private val moveRepository: MoveRepository,
) {
    fun execute(year: Int): Result<Unit> {
        try {
            var page = 1
            val firstPageResult =
                movieGateway.getMovies(year, page).fold(
                    onSuccess = { it },
                    onFailure = { return Result.failure(it) },
                )
            saveMovies(firstPageResult.results)
            val totalPages = firstPageResult.totalPages
            while (page < totalPages) {
                page++
                val pageResult =
                    movieGateway.getMovies(year, page).fold(
                        onSuccess = { it },
                        onFailure = { return Result.failure(it) },
                    )
                saveMovies(pageResult.results)
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            logger.error(e.toString())
            return Result.failure(e)
        }
    }

    private fun saveMovies(movies: List<MovieDTO>) {
        val entities = movies.map { movieMapper.dtoToEntity(it) }
        moveRepository.saveAll(entities)
    }
}
