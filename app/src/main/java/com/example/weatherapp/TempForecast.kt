package com.example.weatherapp

import com.squareup.moshi.Json

data class TempForecast(
    @Json(name = "day") val day: Float,
    @Json(name = "min") val min: Float,
    @Json(name = "max") val max: Float,
//    val icon: String

)
