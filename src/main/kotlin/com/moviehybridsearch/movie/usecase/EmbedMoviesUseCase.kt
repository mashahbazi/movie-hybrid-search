package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.repo.MovieIndexEntity
import com.moviehybridsearch.movie.repo.MovieIndexRepository
import com.moviehybridsearch.movie.repo.MovieRepository
import com.moviehybridsearch.shared.extension.logger
import com.moviehybridsearch.vector.usecase.PCAUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
@Lazy
class EmbedMoviesUseCase(
    private val pcaUseCase: PCAUseCase,
    private val movieRepository: MovieRepository,
    private val embeddingClient: EmbeddingClient,
    private val movieIndexRepository: MovieIndexRepository,
) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun execute(): Result<Unit> {
        return try {
            val allUnEmbeddedMoviesPage = withContext(Dispatchers.IO) { movieRepository.findUnEmbedded() }
            allUnEmbeddedMoviesPage.chunked(10).map { unEmbeddedMoviesPage ->
                val entities =
                    unEmbeddedMoviesPage.map {
                        GlobalScope.async {
                            val content = "Title:${it.title}, Overview:${it.overview}"

                            val embeddedMovieVector =
                                embeddingClient.embed(content).let {
                                    pcaUseCase.execute(it.toDoubleArray())
                                }

                            MovieIndexEntity().apply {
                                movie = it
                                embedding = embeddedMovieVector.map { it.toFloat() / 100 }.toFloatArray()
                                this.content = content
                            }
                        }
                    }.awaitAll()
                movieIndexRepository.saveAll(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            logger.error("Error while embedding movies", e)
            Result.failure(e)
        }
    }
}
