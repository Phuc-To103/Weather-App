package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}