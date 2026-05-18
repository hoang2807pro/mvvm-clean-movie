package com.fernandocejas.sample.features.language.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LanguageLocalEntity::class], version = 1, exportSchema = false)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}
