package com.example.weatherapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempForecast(
    val day: Float,
    val min: Float,
    val max: Float
) : Parcelable
