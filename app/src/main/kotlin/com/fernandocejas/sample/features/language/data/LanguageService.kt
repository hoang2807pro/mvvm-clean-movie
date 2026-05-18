package com.fernandocejas.sample.features.language.data

import retrofit2.Retrofit

class LanguageService(retrofit: Retrofit) : LanguageApi {
    private val api by lazy { retrofit.create(LanguageApi::class.java) }

    override suspend fun getAll() = api.getAll()
}
