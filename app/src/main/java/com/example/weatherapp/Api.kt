package com.example.weatherapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    fun getCurrentCondition(
        @Query("zip") zip: String = "55016",
        @Query("units") units: String = "imperial",
        @Query("appid") appID: String = "a12bec12393f5199d393a0108ff06758",
    ) : retrofit2.Call<CurrentConditions>

    @GET("daily")
    fun getForecast(
        @Query("zip") zip: String = "55016",
        @Query("units") units: String = "imperial",
        @Query("appid") appID: String = "a12bec12393f5199d393a0108ff06758",
        @Query("cnt") count: Int = 16
    ) : retrofit2.Call<Forecast>
}