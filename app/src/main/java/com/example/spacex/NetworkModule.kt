package com.example.spacex

import com.example.spacex.data.remote.SpaceXApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.spacexdata.com/v4/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    init {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: SpaceXApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(SpaceXApi::class.java)
}