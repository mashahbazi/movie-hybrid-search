package com.moviehybridsearch.movie.presentation

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.usecase.SearchMovieUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movie/search")
class MovieSearchController(
    private val searchMovieUseCase: SearchMovieUseCase,
) {
    @GetMapping("")
    fun search(
        @RequestParam("term") term: String,
    ): List<Movie> {
        return searchMovieUseCase.execute(term).toList()
    }
}
