package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class  ForecastViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val myForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = myForecast

    fun loadData() = runBlocking {
        launch {
            myForecast.value = api.getForecast()
        }
    }


}
