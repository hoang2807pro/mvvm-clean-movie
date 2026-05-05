package com.fernandocejas.sample.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor that appends an Authorization header to every request.
 *
 * Usage: replace getToken() with your actual token source
 * (e.g. SharedPreferences, DataStore, EncryptedSharedPreferences).
 */
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
