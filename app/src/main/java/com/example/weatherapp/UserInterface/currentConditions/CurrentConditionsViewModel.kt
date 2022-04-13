package com.example.weatherapp.UserInterface.currentConditions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Model.CurrentConditions
import com.example.weatherapp.Service.Api
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