package com.example.weatherapp

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TempForecast(
    val day: Float,
    val min: Float,
    val max: Float
) : Parcelable
