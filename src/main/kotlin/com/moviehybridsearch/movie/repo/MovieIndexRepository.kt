package com.moviehybridsearch.movie.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieIndexRepository : JpaRepository<MovieIndexEntity, Long>
