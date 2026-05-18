package com.fernandocejas.sample.features.language.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LanguageDao {

    @Query("SELECT * FROM languages")
    suspend fun getAll(): List<LanguageLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LanguageLocalEntity>)

    @Query("DELETE FROM languages")
    suspend fun deleteAll()
}
