package com.example.weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempForecast(
    val day: Float,
    val min: Float,
    val max: Float
) : Parcelable
