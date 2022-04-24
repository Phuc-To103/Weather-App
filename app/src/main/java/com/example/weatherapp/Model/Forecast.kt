package com.example.weatherapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val list: List<DateForecast>
) : Parcelable
