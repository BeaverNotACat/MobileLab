package me.beavernotacat.nager.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class HolidayInfo(
    val date: String,
    val localName: String,
    val name: String,
    val countryCode: String,
    val fixed: Boolean,
    val global: Boolean,
    val counties: List<String>?,
    val launchYear: Int?,
    val types: List<String>,
)

@Serializable
data class CountryInfo(
    val countryCode: String,
    val name: String,
)

val BASE_URL =
    "https://date.nager.at/api/v3/"

private fun createClient(): OkHttpClient {

    val logging = HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build();
    return client
}

val client = createClient()

val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

val apiService: NagerDateApi = retrofit.create<NagerDateApi>()

interface NagerDateApi {
    @GET("AvailableCountries")
    suspend fun availableCountries(): List<CountryInfo>

    @GET("PublicHolidays/{year}/{countryCode}")
    suspend fun listHolidays(@Path("year") year: Int, @Path("countryCode") countryCode: String): List<HolidayInfo>
}

