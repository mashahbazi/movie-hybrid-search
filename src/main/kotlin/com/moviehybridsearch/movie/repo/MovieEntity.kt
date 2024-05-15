package com.moviehybridsearch.movie.repo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class MovieEntity {
    @Id
    var id: Long = 0

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var description: String = ""
}
