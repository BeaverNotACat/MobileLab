package me.beavernotacat.thecatapi.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.beavernotacat.thecatapi.repositories.CatApiRepository
import me.beavernotacat.thecatapi.repositories.CatApiService
import me.beavernotacat.thecatapi.repositories.DefaultCatApiRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = "https://api.thecatapi.com/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory {
        val json = Json { ignoreUnknownKeys = true }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideCatApiService(okHttpClient: OkHttpClient, jsonConverterFactory: Converter.Factory): CatApiService {
        return Retrofit.Builder()
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create()
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class CatApiRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCatApiRepository(repository: DefaultCatApiRepository): CatApiRepository
}
