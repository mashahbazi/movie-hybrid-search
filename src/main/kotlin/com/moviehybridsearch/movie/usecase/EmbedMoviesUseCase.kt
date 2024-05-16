package com.moviehybridsearch.movie.usecase

import com.moviehybridsearch.movie.repo.MoveRepository
import com.moviehybridsearch.shared.extension.logger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class EmbedMoviesUseCase(
    private val moveRepository: MoveRepository,
    private val embeddingClient: EmbeddingClient,
    private val vectorStore: VectorStore,
) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun execute(): Result<Unit> {
        return try {
            var pageable = Pageable.ofSize(2).withPage(0)
            do {
                val unEmbeddedMoviesPage =
                    withContext(Dispatchers.IO) {
                        moveRepository.findByEmbedded(false, pageable)
                    }
                if (unEmbeddedMoviesPage.content.isEmpty()) break

                val documents =
                    unEmbeddedMoviesPage.content.map {
                        GlobalScope.async {
                            val content = "Title:${it.title}, Overview:${it.overview}"

                            val embeddedMovieVector = embeddingClient.embed(content)

                            Document(
                                UUID.nameUUIDFromBytes(it.id.toString().toByteArray()).toString(),
                                content,
                                mapOf("movieId" to it.id),
                            ).apply {
                                embedding = embeddedMovieVector
                            }
                        }
                    }.awaitAll()

                vectorStore.add(documents)
                GlobalScope.launch {
                    unEmbeddedMoviesPage.content.map { it.apply { embedded = true } }
                        .let { moveRepository.saveAll(it) }
                }

                pageable = pageable.next()
            } while (unEmbeddedMoviesPage.hasNext())
            Result.success(Unit)
        } catch (e: Exception) {
            logger.error("Error while embedding movies", e)
            Result.failure(e)
        }
    }
}
