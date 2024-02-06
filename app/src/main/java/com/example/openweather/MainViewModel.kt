package com.example.openweather

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.logErrorResponse
import com.example.network.logSuccessResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val tag = this::class.simpleName
    private val _state: MutableState<String> = mutableStateOf("")
    val state: State<String> = _state

    fun getGeoCode(query: String) {
        viewModelScope.launch {
            Repo.getGeoCode(query).run {
                if (isSuccessful) {
                    logSuccessResponse(tag)
                    val body = body()
                    body?.let {
                        _state.value = it.toString()
                    }
                } else {
                    logErrorResponse(tag)
                }
            }
        }
    }

    fun getWeather(lat: Float, lon: Float) {
        viewModelScope.launch {
            Repo.getWeather(lat, lon).run {
                if (isSuccessful) {
                    logSuccessResponse(tag)
                    val body = body()
                    body?.let {
                        _state.value = it.toString()
                    }
                } else {
                    logErrorResponse(tag)
                }
            }
        }
    }

    fun getWeatherByName(query: String) {
        viewModelScope.launch {
            Repo.getGeoCode(query).run {
                if (isSuccessful) {
                    logSuccessResponse(tag)
                    val body = body()
                    body?.let {
                        _state.value = it.toString()
                        it.first().run {
                            getWeather(lat,lon)
                        }
                    }
                } else {
                    logErrorResponse(tag)
                }
            }
        }
    }
}