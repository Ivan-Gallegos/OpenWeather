package com.example.network.model.geo


import com.google.gson.annotations.SerializedName

data class GeoCode(
    @SerializedName("country")
    val country: String = "",
    @SerializedName("lat")
    val lat: Float = 0.0f,
    @SerializedName("local_names")
    val localNames: LocalNames = LocalNames(),
    @SerializedName("lon")
    val lon: Float = 0.0f,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("state")
    val state: String = ""
)