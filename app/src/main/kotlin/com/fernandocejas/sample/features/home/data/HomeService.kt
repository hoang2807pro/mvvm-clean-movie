package com.fernandocejas.sample.features.home.data

import retrofit2.Retrofit

class HomeService(retrofit: Retrofit) : HomeApi {
    private val api by lazy { retrofit.create(HomeApi::class.java) }

    override suspend fun getAll() = api.getAll()
}
