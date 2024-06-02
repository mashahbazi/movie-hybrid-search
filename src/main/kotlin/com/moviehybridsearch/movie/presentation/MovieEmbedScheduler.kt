package com.moviehybridsearch.movie.presentation

import com.moviehybridsearch.movie.usecase.EmbedMoviesUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MovieEmbedScheduler(
    private val embedMoviesUseCase: EmbedMoviesUseCase,
) {
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(initialDelay = 1000)
    fun syncMovies() {
        GlobalScope.launch { embedMoviesUseCase.execute() }
    }
}
