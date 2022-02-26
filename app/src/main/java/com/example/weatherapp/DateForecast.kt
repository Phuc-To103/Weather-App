package com.example.weatherapp

data class DateForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempForecast,
    val humidity: Int,
    val pressure: Float,
    val weather : List<WeatherCondition>
)
