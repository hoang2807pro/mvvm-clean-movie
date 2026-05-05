package com.fernandocejas.sample.features.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieLocalEntity::class, MovieDetailsLocalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieDetailsDao(): MovieDetailsDao
}
