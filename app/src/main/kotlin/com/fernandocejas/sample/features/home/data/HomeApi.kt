package com.fernandocejas.sample.features.home.data

import retrofit2.http.GET

internal interface HomeApi {

    // TODO: thay bằng endpoint thực tế
    @GET("homes.json")
    suspend fun getAll(): List<HomeEntity>
}
