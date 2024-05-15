package com.moviehybridsearch.sync.repo

import jakarta.persistence.Entity

@Entity
class SyncEntity {
    var id: Long = 0
    var year: Int = 0
}
