package com.example.network

import com.example.network.model.geo.GeoCode
import com.example.network.model.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherService {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
    @GET("geo/1.0/direct")
    suspend fun getGeoCode(@Query("q") query: String): Response<List<GeoCode>>

    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("lat") lat: Float, @Query("lon") lon: Float): Response<WeatherResponse>

}