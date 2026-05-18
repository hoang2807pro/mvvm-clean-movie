package com.fernandocejas.sample.features.start.data

import com.fernandocejas.sample.features.start.interactor.StartModel

// TODO: thêm/đổi field khớp với JSON response
data class StartEntity(
    val id: Int,
    val name: String
) {
    fun toDomain() = StartModel(id, name)
}
