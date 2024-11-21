package com.emrehayat.weatherproject.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.service.WeatherAPIService
import androidx.lifecycle.viewModelScope
import com.emrehayat.weatherproject.roomdb.WeatherDatabase
import com.emrehayat.weatherproject.util.SpecialSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherListViewModel(application: Application) : AndroidViewModel(application) {
    val weatherValues = MutableLiveData<List<WeatherFeatures>>()
    val weatherErrorMessage = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()

    private var updateTime = 10 * 60 * 1000 * 1000 * 1000L

    private val weatherAPIService = WeatherAPIService()
    private val specialSharedPreferences = SpecialSharedPreferences(getApplication())

    fun refreshData() {
        val saveTime = specialSharedPreferences.getTime()
        if (saveTime != null && saveTime != 0L && (System.nanoTime() - saveTime < updateTime)) {
            getDataFromRoom()
        } else {
            getDataFromInternet()
        }
    }

    fun refreshDataFromInternet() {
        getDataFromInternet()
    }

    private fun getDataFromRoom() {
        weatherLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val weatherList = WeatherDatabase(getApplication()).weatherDao().getAllWeather()
            withContext(Dispatchers.Main) {
                //showWeather(weatherList)
                Toast.makeText(getApplication(), "Besinleri Room'dan aldık.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDataFromInternet() {
        weatherLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val weatherList = weatherAPIService.getData()
            withContext(Dispatchers.Main) {
                weatherLoading.value = false
                saveToRoom(weatherList)
                Toast.makeText(getApplication(), "Besinleri internetten aldık.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showWeather(weatherList: List<WeatherFeatures>) {
        weatherValues.value = weatherList
        weatherErrorMessage.value = false
        weatherLoading.value = false
    }

    private fun saveToRoom(weatherList: List<WeatherFeatures>) {
        viewModelScope.launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            dao.deleteAllWeather()
            //val uuidList = dao.insertAll(*weatherList.toTypedArray())
            var i = 0
            while (i < weatherList.size) {
                //weatherList[i].current.weather[0].uuid = uuidList[i].toInt()
                i += 1
            }
            showWeather(weatherList)
        }
        specialSharedPreferences.saveTime(System.nanoTime())
    }
}