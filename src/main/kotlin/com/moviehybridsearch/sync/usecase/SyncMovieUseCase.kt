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
        withContext(Dispatchers.IO) {
            syncRepository.findByYear(year)
        }?.let {
            logger.info("Syncing movies for year $year already done")
            return
        }

        logger.info("Syncing movies...")
        val cacheResult = cacheMoviesUseCase.execute(year)
        if (cacheResult.isFailure) {
            logger.error("Caching movies failed")
            return
        }
        logger.info("Caching movies successful")
        logger.info("Embedding movies...")
        val embedResult = embedMoviesUseCase.execute()
        embedResult.fold(
            onSuccess = {
                logger.info("Embedding movies successful")
                logger.info("Syncing movies successful")
            },
            onFailure = { logger.error("Embedding movies failed") },
        )

        withContext(Dispatchers.IO) {
            syncRepository.save(SyncEntity().apply { this.year = year })
        }
    }
}
