package com.emrehayat.weatherproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emrehayat.weatherproject.model.Current
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.model.WeatherWithDetails
import com.emrehayat.weatherproject.roomdb.WeatherDatabase
import kotlinx.coroutines.launch

class WeatherDetailViewModel(application: Application) : AndroidViewModel(application) {
    val weatherLiveData = MutableLiveData<WeatherWithDetails>()

    fun getRoomData(uuid: Int) {
        viewModelScope.launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            val weatherWithDetails = dao.getWeatherWithDetails(uuid)
            weatherLiveData.value = weatherWithDetails
        }
    }
}