package com.fernandocejas.sample.features.start.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.start.interactor.StartModel

@Entity(tableName = "starts")
data class StartLocalEntity(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toDomain() = StartModel(id, name)
}
