package com.example.openweather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.network.model.weather.Main
import com.example.network.model.weather.Weather
import com.example.network.model.weather.WeatherResponse
import com.example.network.model.weather.Wind
import com.example.openweather.ui.theme.Typography

@Composable
fun WeatherResults(weatherResponse: WeatherResponse) = weatherResponse.run {
    if (name.isNotEmpty()) {
        Column {
            MainCard(weatherResponse)
            Spacer(modifier = Modifier.size(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    item { PressureCard(main) }
                    item { HumidityCard(main.humidity) }
                    item { CloudsCard(clouds.all) }
                    item { VisibilityCard(visibility) }
                    item { WindCard(wind) }
                },
            )
        }
    }
}

@Composable
private fun MainCard(weatherResponse: WeatherResponse) = weatherResponse.run {
    Card {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = name, style = Typography.titleLarge) // City
            Spacer(modifier = Modifier.height(16.dp))
            Temperature(weatherResponse)
        }
    }
}

@Composable
private fun Temperature(weatherResponse: WeatherResponse) = weatherResponse.main.run {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Temperature
        Text(text = "${temp}\u00B0", style = Typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        WeatherIcon(weatherResponse.weather.first())
    }
    Spacer(modifier = Modifier.height(16.dp))
    // Max / Min Feels Like
    Text(text = "$tempMax\u00B0 / $tempMin\u00B0 Feels like $feelsLike\u00B0")
}

@Composable
private fun WeatherIcon(weather: Weather) = weather.run {
    Column {
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${icon}@2x.png",
            contentDescription = description,
            modifier = Modifier.size(64.dp)
        )
        Text(text = description)
    }
}

@Composable
private fun PressureCard(main: Main) = main.run {
    Card {
        Text(
            text = buildString {
                append("Pressure:\n")
                append("$pressure hPa.\n")
                append("${"%.2f".format(pressureInHg)} inHg.")
            },
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun HumidityCard(humidity: Int) = PercentCard(title = "Humidity:", value = humidity)

@Composable
private fun CloudsCard(clouds: Int) = PercentCard(title = "Cloud Cover:", value = clouds)


@Composable
private fun PercentCard(title: String, value: Int) = Card {
    Text(text = "$title\n$value %", modifier = Modifier.padding(16.dp))
}


@Composable
private fun VisibilityCard(visibility: Int) = Card {
    Text(
        text = buildString {
            append("Visibility:\n")
            append(
                when {
                    visibility >= 10000 -> "Unlimited"
                    else -> "$visibility m."
                }
            )
        },
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun WindCard(wind: Wind) = wind.run {
    Card {
        Text(
            text = buildString {
                append("Wind:\n")
                append("$speed mph.\n")
                append("Gust:\n")
                append("$gust mph.\n")
                append("Direction:\n")
                append("$deg°")
            },
            modifier = Modifier.padding(16.dp),
        )
    }
}
