package com.fernandocejas.sample.features.start.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StartLocalEntity::class], version = 1, exportSchema = false)
abstract class StartDatabase : RoomDatabase() {
    abstract fun startDao(): StartDao
}
