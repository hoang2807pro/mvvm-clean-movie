package com.fernandocejas.sample.features.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDetailsDao {

    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    suspend fun getById(movieId: Int): MovieDetailsLocalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details: MovieDetailsLocalEntity)
}
