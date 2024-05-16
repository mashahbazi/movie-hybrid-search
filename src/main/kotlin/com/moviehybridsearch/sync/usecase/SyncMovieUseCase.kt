package com.moviehybridsearch.sync.usecase

import com.moviehybridsearch.movie.usecase.CacheMoviesUseCase
import com.moviehybridsearch.movie.usecase.EmbedMoviesUseCase
import com.moviehybridsearch.shared.extension.logger
import com.moviehybridsearch.sync.repo.SyncEntity
import com.moviehybridsearch.sync.repo.SyncRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class SyncMovieUseCase(
    private val cacheMoviesUseCase: CacheMoviesUseCase,
    private val embedMoviesUseCase: EmbedMoviesUseCase,
    private val syncRepository: SyncRepository,
) {
    suspend fun execute(year: Int) {
        val syncEntity =
            withContext(Dispatchers.IO) {
                syncRepository.findByYear(year) ?: syncRepository.save(SyncEntity().apply { this.year = year })
            }

        if (syncEntity.synced) {
            logger.info("Movies already synced")
            return
        }
        logger.info("Syncing movies...")

        if (!syncEntity.cached) {
            logger.info("Caching movies...")
            val cacheResult = cacheMoviesUseCase.execute(year)
            if (cacheResult.isFailure) {
                logger.error("Caching movies failed")
                return
            }
            logger.info("Caching movies successful")
            syncEntity.cached = true
            withContext(Dispatchers.IO) {
                syncRepository.save(syncEntity)
            }
        }

        logger.info("Embedding movies...")
        val embedResult = embedMoviesUseCase.execute()
        embedResult.fold(
            onSuccess = {
                withContext(Dispatchers.IO) {
                    syncEntity.synced = true
                    syncRepository.save(SyncEntity().apply { this.year = year })
                }
                logger.info("Embedding movies successful")
                logger.info("Syncing movies successful")
            },
            onFailure = { logger.error("Embedding movies failed") },
        )
    }
}
