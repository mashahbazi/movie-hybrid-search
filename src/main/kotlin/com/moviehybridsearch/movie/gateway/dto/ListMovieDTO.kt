package com.moviehybridsearch.movie.gateway.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ListMovieDTO(
    val results: List<MovieDTO>,
    val page: Int,
    @JsonProperty("total_results")
    val totalResults: Int,
    @JsonProperty("total_pages")
    val totalPages: Int,
)
