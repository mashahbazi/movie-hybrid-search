package com.moviehybridsearch.movie.shared

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.gateway.dto.MovieDTO
import com.moviehybridsearch.movie.repo.MovieEntity
import org.springframework.stereotype.Component

@Component
class MovieMapper {
    fun dtoToDomain(dto: MovieDTO): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            description = dto.description,
        )
    }

    fun dtoToEntity(dto: MovieDTO): MovieEntity {
        return MovieEntity().apply {
            id = dto.id
            title = dto.title
            description = dto.description
        }
    }
}
