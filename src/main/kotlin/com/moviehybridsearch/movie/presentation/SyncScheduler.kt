package com.moviehybridsearch.movie.presentation

import com.moviehybridsearch.sync.usecase.SyncMovieUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SyncScheduler(
    private val syncMovieUseCase: SyncMovieUseCase,
) {
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(initialDelay = 1000)
    fun syncMovies() {
        GlobalScope.launch { syncMovieUseCase.execute(2024) }
    }
}
