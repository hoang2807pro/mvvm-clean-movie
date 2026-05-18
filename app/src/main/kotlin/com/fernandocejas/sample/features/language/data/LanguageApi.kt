package com.fernandocejas.sample.features.language.data

import retrofit2.http.GET

internal interface LanguageApi {

    // TODO: thay bằng endpoint thực tế
    @GET("languages.json")
    suspend fun getAll(): List<LanguageEntity>
}
