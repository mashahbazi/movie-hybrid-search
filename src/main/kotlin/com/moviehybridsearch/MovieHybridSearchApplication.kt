package com.moviehybridsearch

import com.moviehybridsearch.movie.shared.MovieConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableConfigurationProperties(
    value = [
        MovieConfig::class,
    ],
)
@SpringBootApplication
@EnableScheduling
class MovieHybridSearchApplication

fun main(args: Array<String>) {
    runApplication<MovieHybridSearchApplication>(*args)
}
