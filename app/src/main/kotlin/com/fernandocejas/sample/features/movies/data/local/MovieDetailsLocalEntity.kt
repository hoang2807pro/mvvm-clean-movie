package com.fernandocejas.sample.features.movies.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.movies.interactor.MovieDetails

@Entity(tableName = "movie_details")
data class MovieDetailsLocalEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster: String,
    val summary: String,
    val cast: String,
    val director: String,
    val year: Int,
    val trailer: String
) {
    fun toMovieDetails() = MovieDetails(id, title, poster, summary, cast, director, year, trailer)
}
