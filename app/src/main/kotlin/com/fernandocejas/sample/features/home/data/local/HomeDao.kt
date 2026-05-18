package com.fernandocejas.sample.features.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeDao {

    @Query("SELECT * FROM homes")
    suspend fun getAll(): List<HomeLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeLocalEntity>)

    @Query("DELETE FROM homes")
    suspend fun deleteAll()
}
