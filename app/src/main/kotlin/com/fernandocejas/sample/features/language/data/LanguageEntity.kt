package com.fernandocejas.sample.features.language.data

import com.fernandocejas.sample.features.language.interactor.LanguageModel

// TODO: thêm/đổi field khớp với JSON response
data class LanguageEntity(
    val id: Int,
    val name: String
) {
    fun toDomain() = LanguageModel(id, name)
}
