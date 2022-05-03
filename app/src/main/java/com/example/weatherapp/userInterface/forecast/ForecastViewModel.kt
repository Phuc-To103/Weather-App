package com.example.weatherapp.userInterface.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.service.Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class  ForecastViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val myForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = myForecast

    fun loadData(zipCode: String) = runBlocking {
        launch {
            myForecast.value = api.getForecast(zipCode)
        }
    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch {
            myForecast.value = api.getForecastLatLon(latitude, longitude)
        }
    }


}
