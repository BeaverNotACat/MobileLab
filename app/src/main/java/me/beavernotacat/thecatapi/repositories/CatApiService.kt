package me.beavernotacat.thecatapi.repositories

import me.beavernotacat.thecatapi.models.dto.CatImageDto
import me.beavernotacat.thecatapi.models.dto.CatInfoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {
    @GET("breeds/search")
    suspend fun searchBreeds(@Query("name") name: String): List<CatInfoDto>

    @GET("breeds/{id}")
    suspend fun fetchBreed(@Path("id") id: String): CatInfoDto

    @GET("images/{id}")
    suspend fun fetchImageUrl(@Path("id") id: String): CatImageDto
}
