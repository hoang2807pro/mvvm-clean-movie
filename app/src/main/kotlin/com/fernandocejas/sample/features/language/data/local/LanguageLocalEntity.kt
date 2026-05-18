package com.fernandocejas.sample.features.language.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.language.interactor.LanguageModel

@Entity(tableName = "languages")
data class LanguageLocalEntity(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toDomain() = LanguageModel(id, name)
}
