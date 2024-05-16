package com.moviehybridsearch.movie.shared

import com.moviehybridsearch.movie.domain.Movie
import com.moviehybridsearch.movie.gateway.dto.MovieDTO
import com.moviehybridsearch.movie.repo.MovieEntity
import org.springframework.stereotype.Component

@Component
class MovieMapper {
    fun dtoToEntity(dto: MovieDTO): MovieEntity {
        return MovieEntity().apply {
            id = dto.id
            title = dto.title
            overview = dto.overview
        }
    }

    fun entityToDomain(entity: MovieEntity): Movie {
        return Movie(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
        )
    }
}
