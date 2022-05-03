package com.example.weatherapp.userInterface.currentConditions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.CurrentConditions
import com.example.weatherapp.service.Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrentConditionsViewModel @Inject constructor(private val api: Api) : ViewModel(){
    private val myCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = myCurrentConditions

    fun loadData(zipCode: String) = runBlocking{
        launch {
            myCurrentConditions.value = api.getCurrentCondition(zipCode)
        }
    }
    fun loadData(latitude: Float, longitude: Float) = runBlocking{
        launch {
            myCurrentConditions.value = api.getCurrentConditionLatLong(latitude,longitude)
        }
    }
}