package com.fernandocejas.sample.features.home.data

import com.fernandocejas.sample.features.home.interactor.HomeModel

// TODO: thêm/đổi field khớp với JSON response
data class HomeEntity(
    val id: Int,
    val name: String
) {
    fun toDomain() = HomeModel(id, name)
}
