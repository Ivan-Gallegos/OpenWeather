package com.example.network.model.geo


import com.google.gson.annotations.SerializedName

data class GeoCode(
    @SerializedName("country")
    val country: String = "",
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("local_names")
    val localNames: LocalNames = LocalNames(),
    @SerializedName("lon")
    val lon: Double = 0.0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("state")
    val state: String = ""
)