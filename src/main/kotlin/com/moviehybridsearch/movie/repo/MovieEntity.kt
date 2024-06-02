package com.moviehybridsearch.movie.repo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.ColumnDefault

@Entity(name = "movies")
class MovieEntity {
    @Id
    @ColumnDefault("nextval('movies_index_seq'::regclass)")
    var id: Long = 0

    @Column(nullable = false, length = 1000)
    var title: String = ""

    @Column(nullable = false, length = 1000)
    var overview: String = ""
}
