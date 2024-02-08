package com.example.openweather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.app.ActivityCompat
import com.example.network.model.weather.WeatherResponse
import com.example.openweather.ui.SearchBar
import com.example.openweather.ui.WeatherResults
import com.example.openweather.ui.theme.OpenWeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state by vm.state
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity)

        setContent {
            OpenWeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LocationEffect(fusedLocationProviderClient)
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
                vm.getWeather(it.lat, it.lon)
                expanded = false
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeatherResults(state.weather)
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun LocationEffect(fusedLocationProviderClient: FusedLocationProviderClient) {
        var permissionsGranted by remember { mutableStateOf(areLocationPermissionsGranted()) }
        val locationPermissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                    acc || isPermissionGranted
                }
            },
        )
        LaunchedEffect(permissionsGranted) {
            if (areLocationPermissionsGranted()) {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    TimeUnit.SECONDS.toMillis(1),
                )
                    .setMinUpdateIntervalMillis(0)
                    .setMaxUpdates(1)
                    .build()

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(it: LocationResult) {
                            super.onLocationResult(it)
                            val location = it.locations.last()
                            vm.getWeather(location.latitude, location.longitude)
                        }
                    },
                    Looper.getMainLooper(),
                )
            } else {
                locationPermissionLauncher.launch(locationPermissions)
            }
        }
    }

    private fun areLocationPermissionsGranted(): Boolean = ActivityCompat.checkSelfPermission(
        this@MainActivity, ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        this@MainActivity, ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
