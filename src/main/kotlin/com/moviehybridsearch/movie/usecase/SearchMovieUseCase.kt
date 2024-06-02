package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.repo.MovieRepository
import com.moviehybridsearch.movie.shared.MovieMapper
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.stereotype.Component

@Component
class SearchMovieUseCase(
    private val embeddingClient: EmbeddingClient,
    private val movieRepository: MovieRepository,
    private val movieMapper: MovieMapper,
) {
    fun execute(term: String): Iterable<Movie> {
        val vector = embeddingClient.embed(term)
        val embedding = "[${vector.joinToString(",")}]"
        return movieRepository.findNearestMovies(term, embedding, 10).map { movieMapper.entityToDomain(it) }
    }
}
