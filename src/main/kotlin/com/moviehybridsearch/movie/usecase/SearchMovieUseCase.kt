package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.repo.MovieRepository
import com.moviehybridsearch.movie.shared.MovieMapper
import org.springframework.stereotype.Component

@Component
class SearchMovieUseCase(
    private val movieRepository: MovieRepository,
    private val movieMapper: MovieMapper,
) {
    fun execute(term: String): Iterable<Movie> {
        throw NotImplementedError("Not implemented")
    }
}
