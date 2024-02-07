package com.example.openweather

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.network.model.weather.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val LAST_WEATHER = stringPreferencesKey("last_weather")
val DataStore<Preferences>.lastWeather: Flow<WeatherResponse>
    get() = data.map { preferences ->
        Gson().fromJson(preferences[LAST_WEATHER], WeatherResponse::class.java)
    }

suspend fun DataStore<Preferences>.updateLastWeather(weatherResponse: WeatherResponse) =
    edit { settings ->
        settings[LAST_WEATHER] = Gson().toJson(weatherResponse)
    }
