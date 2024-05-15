package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.repo.MoveRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class EmbedMoviesUseCase(
    private val moveRepository: MoveRepository,
    private val embeddingClient: EmbeddingClient,
    private val vectorStore: VectorStore,
) {
    suspend fun execute(): Result<Unit> {
        return try {
            val pageable = Pageable.ofSize(10).withPage(-1)
            do {
                pageable.next()
                val unEmbeddedMoviesPage =
                    withContext(Dispatchers.IO) {
                        moveRepository.findByEmbedded(false, pageable)
                    }
                if (unEmbeddedMoviesPage.content.isEmpty()) break

                val documents =
                    unEmbeddedMoviesPage.content.map {
                        GlobalScope.async {
                            val embeddedMovieVector =
                                embeddingClient.embed("Title:${it.title}, Description:${it.description}")

                            Document(
                                it.id.toString(),
                                "Title:${it.title}, Description:${it.description}",
                                null,
                            ).apply {
                                embedding = embeddedMovieVector
                            }
                        }
                    }.awaitAll()

                vectorStore.add(documents)
            } while (unEmbeddedMoviesPage.hasNext())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
