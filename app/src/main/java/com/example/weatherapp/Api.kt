package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentCondition(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appID: String = "a12bec12393f5199d393a0108ff06758",
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appID: String = "a12bec12393f5199d393a0108ff06758",
        @Query("cnt") count: Int = 16
    ): Forecast


}