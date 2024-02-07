package com.example.openweather

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.logErrorResponse
import com.example.network.logSuccessResponse
import com.example.network.model.geo.GeoCode
import com.example.network.model.weather.WeatherResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val tag = this::class.simpleName

    private val _state: MutableState<MainState> = mutableStateOf(MainState())
    val state: State<MainState> = _state
    fun updateQuery(query: String) {
        _state.value = state.value.copy(searchQuery = query)
    }

    fun updateWeatherResponse(weather: WeatherResponse) {
        _state.value = state.value.copy(weather = weather)
    }

    fun getGeoCode(query: String, limit: Int) = viewModelScope.launch {
        Repo.getGeoCode(query, limit).run {
            if (isSuccessful) {
                logSuccessResponse(tag)
                val body = body()
                body?.let {
                    _state.value = state.value.copy(searchResults = it)
                }
            } else {
                logErrorResponse(tag)
            }
        }
    }


    fun getWeather(lat: Double, lon: Double, units: String) {
        viewModelScope.launch {
            Repo.getWeather(lat, lon, units).run {
                if (isSuccessful) {
                    logSuccessResponse(tag)
                    val body = body()
                    body?.let {
                        _state.value = state.value.copy(weather = it)
                    }
                } else {
                    logErrorResponse(tag)
                }
            }
        }
    }
}

data class MainState(
    val searchQuery: String = "",
    val searchResults: List<GeoCode> = listOf(),
    val weather: WeatherResponse = WeatherResponse()
)