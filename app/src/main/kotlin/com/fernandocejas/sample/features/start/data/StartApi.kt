package com.fernandocejas.sample.features.start.data

import retrofit2.http.GET

internal interface StartApi {

    // TODO: thay bằng endpoint thực tế
    @GET("starts.json")
    suspend fun getAll(): List<StartEntity>
}
