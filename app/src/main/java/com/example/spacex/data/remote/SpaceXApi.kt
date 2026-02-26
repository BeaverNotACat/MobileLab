package com.example.spacex.data.remote

import retrofit2.http.GET
import retrofit2.http.Path


interface SpaceXApi {
    @GET("crew/")
    suspend fun list(): List<CrewDto>

    @GET("crew/{name}")
    suspend fun load(@Path("name") name: String): CrewDto
}