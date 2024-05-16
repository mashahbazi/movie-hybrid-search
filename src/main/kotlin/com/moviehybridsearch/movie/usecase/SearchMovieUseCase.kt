package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.repo.MovieRepository
import com.moviehybridsearch.movie.shared.MovieMapper
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Component

@Component
class SearchMovieUseCase(
    private val vectorStore: VectorStore,
    private val movieRepository: MovieRepository,
    private val movieMapper: MovieMapper,
) {
    fun execute(term: String): Iterable<Movie> {
        val documents = vectorStore.similaritySearch(term)
        val movieIds = documents.mapNotNull { (it.metadata["movieId"] as Int?)?.toLong() }
        return movieRepository.findAllById(movieIds).map { movieMapper.entityToDomain(it) }
    }
}
