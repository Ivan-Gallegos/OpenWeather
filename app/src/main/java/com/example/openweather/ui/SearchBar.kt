package com.example.openweather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.PopupProperties
import com.example.network.model.geo.GeoCode
import com.example.openweather.MainState

@Composable
fun SearchBar(
    state: MainState,
    expanded: Boolean,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (geoCode: GeoCode) -> Unit,
) = Box {
    state.run {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(text = "City", color = Color.Gray) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(),
            modifier = Modifier.fillMaxWidth()
        ) {
            searchResults.forEach {
                SearchResult(it, onItemClick)
            }
        }
    }
}

@Composable
private fun SearchResult(geoCode: GeoCode, onItemClick: (GeoCode) -> Unit) = geoCode.run {
    DropdownMenuItem(
        text = { Text(text = "$name, $state, $country") },
        onClick = { onItemClick(geoCode) },
    )
}