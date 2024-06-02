package com.moviehybridsearch.movie.repo

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : CrudRepository<MovieEntity, Long> {
    @Query(
        nativeQuery = true,
        value = """
            SELECT 
                m.id AS id,
                m.title AS title,
                m.overview AS overview
            FROM movies m
            LEFT OUTER JOIN movies_index mi ON m.id = mi.movie_id
            WHERE mi.movie_id IS NULL
            """,
    )
    fun findUnEmbedded(): List<MovieEntity>
}
