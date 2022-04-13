package com.example.weatherapp.Service

import com.example.weatherapp.Model.CurrentConditions
import com.example.weatherapp.Model.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentCondition(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f"
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f",
        @Query("cnt") cnt: Int = 16
    ): Forecast

    @GET("weather")
    suspend fun getCurrentConditionLatLong(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f"
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecastLatLon(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "0e3745b8b10701ce9dc29c580c91b68f",
        @Query("cnt") cnt: Int = 16
    ): Forecast
}