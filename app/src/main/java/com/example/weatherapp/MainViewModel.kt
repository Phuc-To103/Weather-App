package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val myCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = myCurrentConditions

    fun loadData() = runBlocking{
        launch {
            myCurrentConditions.value = api.getCurrentCondition()
        }
    }
}