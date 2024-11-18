package com.emrehayat.weatherproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.roomdb.WeatherDatabase
import kotlinx.coroutines.launch

class WeatherDetailViewModel(application: Application) : AndroidViewModel(application) {
    val weatherLiveData = MutableLiveData<Weather>()

    fun getRoomData(uuid: Int) {
        viewModelScope.launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            val weather = dao.getWeather(uuid)
            weatherLiveData.value = weather
        }
    }
}