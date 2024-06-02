package com.moviehybridsearch.movie.repo

import jakarta.persistence.*
import org.hibernate.annotations.Array
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "movies_index")
class MovieIndexEntity {
    @Id
    @SequenceGenerator(name = "movies_index_seq", sequenceName = "movies_index_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_index_seq")
    var id: Long? = null

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    var movie: MovieEntity? = null

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    var content: String? = null

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    var embedding: FloatArray? = null
}
