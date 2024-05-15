package com.moviehybridsearch.movie.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MoveRepository : CrudRepository<MovieEntity, Long> {
    fun findByEmbedded(
        embedded: Boolean,
        pageable: Pageable,
    ): Page<MovieEntity>
}
