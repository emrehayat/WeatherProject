package com.emrehayat.weatherproject.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.service.WeatherAPIService
import androidx.lifecycle.viewModelScope
import com.emrehayat.weatherproject.BuildConfig
import com.emrehayat.weatherproject.model.Current
import com.emrehayat.weatherproject.model.Weather
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
                Log.d("WeatherAPI", "API Key: ${BuildConfig.WEATHER_API_KEY}")
                Log.d("WeatherAPI", "API çağrısı başlıyor...")
                val apiResponse = weatherAPIService.getWeatherData(
                    lat = 39.92077,
                    lon = 32.85411,
                    apiKey = BuildConfig.WEATHER_API_KEY
                )
                Log.d("WeatherAPI", "API yanıtı: $apiResponse")

                val weatherFeatures = WeatherFeatures(
                    id = System.currentTimeMillis().toInt(),
                    lat = apiResponse.coord.lat,
                    lon = apiResponse.coord.lon,
                    timezone = apiResponse.name,
                    timezoneOffset = apiResponse.timezone.toLong(),
                    cityName = apiResponse.name,
                    temperature = apiResponse.main.temp,
                    humidity = apiResponse.main.humidity,
                    windSpeed = apiResponse.wind.speed,
                    rainfall = 0.0,
                    weatherIcon = apiResponse.weather.firstOrNull()?.icon
                )

                val current = Current(
                    weatherFeaturesId = weatherFeatures.id,
                    dt = apiResponse.dt,
                    sunrise = apiResponse.sys.sunrise,
                    sunset = apiResponse.sys.sunset,
                    temp = apiResponse.main.temp,
                    feelsLike = apiResponse.main.feels_like,
                    pressure = apiResponse.main.pressure.toLong(),
                    humidity = apiResponse.main.humidity.toLong(),
                    dewPoint = 0.0,
                    uvi = 0.0,
                    clouds = 0,
                    visibility = apiResponse.visibility.toLong(),
                    windSpeed = apiResponse.wind.speed,
                    windDeg = apiResponse.wind.deg.toLong(),
                    windGust = apiResponse.wind.gust ?: 0.0
                )

                val weatherList = apiResponse.weather.map { apiWeather ->
                    Weather(
                        main = apiWeather.main,
                        description = apiWeather.description,
                        icon = apiWeather.icon,
                        weatherFeaturesId = weatherFeatures.id
                    )
                }

                withContext(Dispatchers.Main) {
                    saveToRoom(weatherFeatures, current, weatherList)
                    weatherLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("WeatherAPI", "Hata oluştu", e)
                withContext(Dispatchers.Main) {
                    weatherLoading.value = false
                    weatherErrorMessage.value = true
                    Toast.makeText(getApplication(), "Hata: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showWeather(weatherList: List<WeatherFeatures>) {
        weatherValues.value = weatherList
        weatherErrorMessage.value = false
        weatherLoading.value = false
    }

    private fun saveToRoom(weatherFeatures: WeatherFeatures, current: Current, weatherList: List<Weather>) {
        viewModelScope.launch {
            try {
                val dao = WeatherDatabase(getApplication()).weatherDao()
                dao.deleteAll()
                dao.insertAll(weatherFeatures, current, weatherList)
                showWeather(listOf(weatherFeatures))
                specialSharedPreferences.saveTime(System.nanoTime())
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Room Kaydetme Hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}