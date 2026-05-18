package com.fernandocejas.sample.features.start.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StartDao {

    @Query("SELECT * FROM starts")
    suspend fun getAll(): List<StartLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<StartLocalEntity>)

    @Query("DELETE FROM starts")
    suspend fun deleteAll()
}
