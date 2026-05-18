package com.fernandocejas.sample.features.start.data

import retrofit2.Retrofit

class StartService(retrofit: Retrofit) : StartApi {
    private val api by lazy { retrofit.create(StartApi::class.java) }

    override suspend fun getAll() = api.getAll()
}
