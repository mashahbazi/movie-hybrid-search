package com.moviehybridsearch.movie.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MoveRepository : CrudRepository<MovieEntity, Long>
