package com.moviehybridsearch.sync.repo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class SyncEntity {
    @Id
    @GeneratedValue
    var id: Long = 0

    @Column(nullable = false, unique = true)
    var year: Int = 0

    @Column(nullable = false)
    var cached: Boolean = false

    @Column(nullable = false)
    var synced: Boolean = false
}
