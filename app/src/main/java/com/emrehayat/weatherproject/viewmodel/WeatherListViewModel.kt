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
            try {
                val weatherList = WeatherDatabase(getApplication()).weatherDao().getAllWeather()
                withContext(Dispatchers.Main) {
                    showWeather(weatherList)
                    Toast.makeText(getApplication(), "Veriler Room'dan alındı.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherLoading.value = false
                    weatherErrorMessage.value = true
                    Toast.makeText(getApplication(), "Room Hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getDataFromInternet() {
        weatherLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = weatherAPIService.getWeatherData(apiKey = "ce77025c3bbb2b5913f1a410ba3355ca", lat = 39.92077, lon = 32.85411, exclude = "minutely")

                val weatherFeatures = WeatherFeatures(
                    id = apiResponse.id,
                    lat = apiResponse.lat,
                    lon = apiResponse.lon,
                    timezone = apiResponse.timezone,
                    timezoneOffset = apiResponse.timezoneOffset,
                    cityName = apiResponse.cityName,
                    temperature = apiResponse.current.temp,
                    humidity = apiResponse.current.humidity.toInt(),
                    windSpeed = apiResponse.current.windSpeed,
                    rainfall = 0.0,
                    weatherIcon = apiResponse.current.weather.getOrNull(0)?.icon,
                    current = apiResponse.current
                )

                val weatherList = listOf(weatherFeatures)

                withContext(Dispatchers.Main) {
                    saveToRoom(weatherList)
                    Toast.makeText(getApplication(), "Veriler internetten alındı.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherLoading.value = false
                    weatherErrorMessage.value = true
                    Toast.makeText(getApplication(), "API Hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
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
            try {
                val dao = WeatherDatabase(getApplication()).weatherDao()
                dao.deleteAllWeather()
                val uuidList = dao.insertAll(*weatherList.toTypedArray())

                weatherList.forEachIndexed { index, weatherFeature ->
                    weatherFeature.current.weather.getOrNull(0)?.uuid = uuidList[index].toInt()
                }

                showWeather(weatherList)
                specialSharedPreferences.saveTime(System.nanoTime())
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Room Kaydetme Hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}


/*package com.emrehayat.weatherproject.viewmodel

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
                showWeather(weatherList)
                Toast.makeText(getApplication(), "Besinleri Room'dan aldık.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /*private fun getDataFromInternet() {
        weatherLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = weatherAPIService.getData()

                val weather = WeatherFeatures(
                    cityName = apiResponse.cityName,
                    temperature = apiResponse.main.temp,
                    humidity = apiResponse.main.humidity,
                    windSpeed = apiResponse.wind.speed,
                    rainfall = apiResponse.rain?.lastHour,
                    weatherIcon = apiResponse.weather[0].icon
                )

                val weatherList = listOf(weather)

                withContext(Dispatchers.Main) {
                    saveToRoom(weatherList)
                    Toast.makeText(getApplication(), "Veriler internetten alındı.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherLoading.value = false
                    weatherErrorMessage.value = true
                    Toast.makeText(getApplication(), "Hata: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }*/


    private fun getDataFromInternet() {
        weatherLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val weatherList = weatherAPIService.getWeatherData()
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
            val uuidList = dao.insertAll(*weatherList.toTypedArray())
            weatherList.forEachIndexed { index, weatherFeature ->
                weatherFeature.current.weather.getOrNull(0)?.uuid = uuidList[index].toInt()

            /*val dao = WeatherDatabase(getApplication()).weatherDao()
            dao.deleteAllWeather()
            val uuidList = dao.insertAll(*weatherList.toTypedArray())
            var i = 0
            while (i < weatherList.size) {
                //weatherList[i].current.weather[0].uuid = uuidList[i].toInt()
                weatherList[i].current.weather.getOrNull(0)?.uuid = uuidList[i].toInt()
                i += 1*/
            }
            showWeather(weatherList)
        }
        specialSharedPreferences.saveTime(System.nanoTime())
    }
}*/