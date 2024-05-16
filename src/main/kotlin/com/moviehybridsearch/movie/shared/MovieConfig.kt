package com.moviehybridsearch.movie.shared

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties("movie")
@Validated
class MovieConfig {
    var apiToken = ""
}
