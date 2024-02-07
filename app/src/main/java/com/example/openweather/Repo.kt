package com.example.openweather

import com.example.network.OpenWeatherService
import com.example.network.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Logger.Companion.DEFAULT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repo {

    private val client: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor(DEFAULT).apply {
            level = BODY
        })
        .addInterceptor(ApiKeyInterceptor())
        .build()

    // Instantiate Retrofit with GsonConverter to deserialize JSON response
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(OpenWeatherService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // Instantiate service using Retrofit
    private val openWeatherService = retrofit.create<OpenWeatherService>()

    suspend fun getGeoCode(query: String, limit: Int) = openWeatherService.getGeoCode(query, limit)

    suspend fun getWeather(lat: Double, lon: Double, units: String) =
        openWeatherService.getWeather(lat, lon, units)

}