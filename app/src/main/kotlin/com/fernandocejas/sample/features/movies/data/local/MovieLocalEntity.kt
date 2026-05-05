package com.fernandocejas.sample.features.movies.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.movies.interactor.Movie

@Entity(tableName = "movies")
data class MovieLocalEntity(
    @PrimaryKey val id: Int,
    val poster: String
) {
    fun toMovie() = Movie(id, poster)
}
