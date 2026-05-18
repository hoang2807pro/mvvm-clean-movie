package com.fernandocejas.sample.features.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.home.interactor.HomeModel

@Entity(tableName = "homes")
data class HomeLocalEntity(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toDomain() = HomeModel(id, name)
}
