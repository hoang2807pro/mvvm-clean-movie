package com.fernandocejas.sample.features.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HomeLocalEntity::class], version = 1, exportSchema = false)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
}
