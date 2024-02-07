package com.example.openweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.network.model.weather.WeatherResponse
import com.example.openweather.ui.SearchBar
import com.example.openweather.ui.WeatherResults
import com.example.openweather.ui.theme.OpenWeatherTheme
import kotlinx.coroutines.flow.firstOrNull

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state by vm.state

        setContent {
            OpenWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LoadLastWeather()
                    MainColumn(state)
                    UpdateLastWeather(state)
                }
            }
        }
    }

    @Composable
    private fun LoadLastWeather() = LaunchedEffect(Unit) {
        vm.updateWeatherResponse(dataStore.lastWeather.firstOrNull() ?: WeatherResponse())
    }

    @Composable
    private fun UpdateLastWeather(state: MainState) = LaunchedEffect(state.weather) {
        dataStore.updateLastWeather(state.weather)
    }

    @Composable
    private fun MainColumn(state: MainState) = Column(modifier = Modifier.padding(16.dp)) {
        var expanded by remember(state.searchResults) { mutableStateOf(state.searchResults.isNotEmpty()) }
        SearchBar(
            state,
            expanded,
            onValueChange = {
                vm.updateQuery(it)
                if (it.isNotEmpty()) {
                    vm.getGeoCode("$it,,US", 5)
                } else {
                    expanded = false
                }
            },
            onDismissRequest = { expanded = false },
            onItemClick = {
                vm.getWeather(it.lat, it.lon, "imperial")
                expanded = false
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeatherResults(state.weather)
    }
}
