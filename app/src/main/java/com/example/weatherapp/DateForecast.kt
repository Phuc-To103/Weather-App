package com.example.weatherapp

data class DateForecast(
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempForecast,
    val humidity: Int,
    val pressure: Float
) {
}