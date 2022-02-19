package com.example.weatherapp

import com.squareup.moshi.Json

class Currents (
    @Json(name = "temp") val temp: Float,
    @Json(name = "feels_like") val feelsLike: Float,
    @Json(name = "temp_min") val tempMin: Float,
    @Json(name = "temp_max") val tempMax: Float,
    @Json(name = "pressure") val pressure : Float,
    @Json(name = "humidity") val humidity : Float,
)
