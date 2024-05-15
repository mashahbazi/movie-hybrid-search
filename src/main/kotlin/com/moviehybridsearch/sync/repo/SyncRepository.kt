package com.moviehybridsearch.sync.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SyncRepository : CrudRepository<SyncEntity, Long> {
    fun findByYear(year: Int): SyncEntity?
} 
