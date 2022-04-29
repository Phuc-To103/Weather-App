package com.example.weatherapp.userInterface.currentConditions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.CurrentConditions

class CurrentConditionsViewModel: ViewModel(){
    private val myCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = myCurrentConditions

    fun loadData(currentConditionsArg: CurrentConditions) {
        myCurrentConditions.value = currentConditionsArg
    }
}