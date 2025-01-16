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

    private val updateTime = 10 * 60 * 1000L

    private val weatherAPIService = WeatherAPIService()
    private val specialSharedPreferences = SpecialSharedPreferences(getApplication())

    private val turkishCities = listOf(
        Triple("Adana", 37.0000, 35.3213),
        Triple("Adıyaman", 37.7648, 38.2786),
        Triple("Afyonkarahisar", 38.7507, 30.5567),
        Triple("Ağrı", 39.7191, 43.0503),
        Triple("Amasya", 40.6499, 35.8353),
        Triple("Ankara", 39.9208, 32.8541),
        Triple("Antalya", 36.8841, 30.7056),
        Triple("Artvin", 41.1828, 41.8183),
        Triple("Aydın", 37.8560, 27.8416),
        Triple("Balıkesir", 39.6484, 27.8826),
        Triple("Bilecik", 40.1451, 29.9799),
        Triple("Bingöl", 38.8855, 40.4966),
        Triple("Bitlis", 38.4006, 42.1095),
        Triple("Bolu", 40.7392, 31.6089),
        Triple("Burdur", 37.7765, 30.2903),
        Triple("Bursa", 40.1885, 29.0610),
        Triple("Çanakkale", 40.1553, 26.4142),
        Triple("Çankırı", 40.6013, 33.6134),
        Triple("Çorum", 40.5506, 34.9556),
        Triple("Denizli", 37.7765, 29.0864),
        Triple("Diyarbakır", 37.9144, 40.2306),
        Triple("Edirne", 41.6818, 26.5623),
        Triple("Elazığ", 38.6810, 39.2264),
        Triple("Erzincan", 39.7500, 39.5000),
        Triple("Erzurum", 39.9000, 41.2700),
        Triple("Eskişehir", 39.7767, 30.5206),
        Triple("Gaziantep", 37.0662, 37.3833),
        Triple("Giresun", 40.9128, 38.3895),
        Triple("Gümüşhane", 40.4386, 39.5086),
        Triple("Hakkari", 37.5833, 43.7333),
        Triple("Hatay", 36.4018, 36.3498),
        Triple("Isparta", 37.7648, 30.5566),
        Triple("Mersin", 36.8000, 34.6333),
        Triple("İstanbul", 41.0082, 28.9784),
        Triple("İzmir", 38.4189, 27.1287),
        Triple("Kars", 40.6013, 43.0975),
        Triple("Kastamonu", 41.3887, 33.7827),
        Triple("Kayseri", 38.7312, 35.4787),
        Triple("Kırklareli", 41.7333, 27.2167),
        Triple("Kırşehir", 39.1425, 34.1709),
        Triple("Kocaeli", 40.8533, 29.8815),
        Triple("Konya", 37.8667, 32.4833),
        Triple("Kütahya", 39.4167, 29.9833),
        Triple("Malatya", 38.3552, 38.3095),
        Triple("Manisa", 38.6191, 27.4289),
        Triple("Kahramanmaraş", 37.5858, 36.9371),
        Triple("Mardin", 37.3212, 40.7245),
        Triple("Muğla", 37.2153, 28.3636),
        Triple("Muş", 38.7432, 41.5064),
        Triple("Nevşehir", 38.6244, 34.7144),
        Triple("Niğde", 37.9667, 34.6833),
        Triple("Ordu", 40.9839, 37.8764),
        Triple("Rize", 41.0201, 40.5234),
        Triple("Sakarya", 40.7569, 30.3781),
        Triple("Samsun", 41.2928, 36.3313),
        Triple("Siirt", 37.9333, 41.9500),
        Triple("Sinop", 42.0231, 35.1531),
        Triple("Sivas", 39.7477, 37.0179),
        Triple("Tekirdağ", 40.9833, 27.5167),
        Triple("Tokat", 40.3167, 36.5500),
        Triple("Trabzon", 41.0015, 39.7178),
        Triple("Tunceli", 39.1079, 39.5401),
        Triple("Şanlıurfa", 37.1591, 38.7969),
        Triple("Uşak", 38.6823, 29.4082),
        Triple("Van", 38.4891, 43.4089),
        Triple("Yozgat", 39.8181, 34.8147),
        Triple("Zonguldak", 41.4564, 31.7987),
        Triple("Aksaray", 38.3687, 34.0370),
        Triple("Bayburt", 40.2552, 40.2249),
        Triple("Karaman", 37.1759, 33.2287),
        Triple("Kırıkkale", 39.8468, 33.5153),
        Triple("Batman", 37.8812, 41.1351),
        Triple("Şırnak", 37.5164, 42.4611),
        Triple("Bartın", 41.6344, 32.3375),
        Triple("Ardahan", 41.1105, 42.7022),
        Triple("Iğdır", 39.9167, 44.0333),
        Triple("Yalova", 40.6500, 29.2667),
        Triple("Karabük", 41.2061, 32.6204),
        Triple("Kilis", 36.7184, 37.1212),
        Triple("Osmaniye", 37.0742, 36.2467),
        Triple("Düzce", 40.8438, 31.1565)
    )

    fun refreshData() {
        val saveTime = specialSharedPreferences.getTime()
        if (saveTime != null && saveTime != 0L && System.currentTimeMillis() - saveTime < updateTime) {
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
                val weatherList = mutableListOf<WeatherFeatures>()
                val currentList = mutableListOf<Current>()
                val weatherDetailsList = mutableListOf<List<Weather>>()

                turkishCities.sortedBy { it.first }.forEach { (cityName, lat, lon) ->
                    try {
                        val apiResponse = weatherAPIService.getWeatherData(
                            lat = lat,
                            lon = lon,
                            apiKey = BuildConfig.WEATHER_API_KEY
                        )

                        val weatherFeatures = WeatherFeatures(
                            id = System.currentTimeMillis().toInt() + cityName.hashCode(),
                            lat = apiResponse.coord.lat,
                            lon = apiResponse.coord.lon,
                            timezone = apiResponse.name,
                            timezoneOffset = apiResponse.timezone.toLong(),
                            cityName = cityName,
                            temperature = apiResponse.main.temp,
                            humidity = apiResponse.main.humidity,
                            windSpeed = apiResponse.wind.speed,
                            rainfall = when {
                                apiResponse.rain?.h1 != null -> apiResponse.rain.h1
                                apiResponse.rain?.h3 != null -> apiResponse.rain.h3
                                apiResponse.weather.any { it.main.contains("rain", ignoreCase = true) } -> 0.1
                                else -> 0.0
                            },
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

                        val weatherDetails = apiResponse.weather.map { apiWeather ->
                            Weather(
                                main = apiWeather.main,
                                description = apiWeather.description,
                                icon = apiWeather.icon,
                                weatherFeaturesId = weatherFeatures.id
                            )
                        }

                        weatherList.add(weatherFeatures)
                        currentList.add(current)
                        weatherDetailsList.add(weatherDetails)
                    } catch (e: Exception) {
                        // Bir şehir için hata olursa diğerlerine devam et
                        Log.e("WeatherAPI", "Error for $cityName: ${e.message}")
                    }
                }

                withContext(Dispatchers.Main) {
                    // Önce tüm verileri sil
                    WeatherDatabase(getApplication()).weatherDao().deleteAll()
                    
                    // Sonra yeni verileri kaydet
                    weatherList.forEachIndexed { index, weatherFeatures ->
                        saveToRoom(
                            weatherFeatures,
                            currentList[index],
                            weatherDetailsList[index]
                        )
                    }
                    
                    // Tüm listeyi göster
                    showWeather(weatherList)
                    weatherLoading.value = false
                }
            } catch (e: Exception) {
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

    private fun saveToRoom(weatherFeatures: WeatherFeatures, current: Current, weatherList: List<Weather>, showAllCities: Boolean = true) {
        viewModelScope.launch {
            try {
                val dao = WeatherDatabase(getApplication()).weatherDao()
                dao.insertAll(weatherFeatures, current, weatherList)
                
                if (showAllCities) {
                    val allWeather = dao.getAllWeather().sortedBy { it.cityName }
                    showWeather(allWeather)
                } else {
                    showWeather(listOf(weatherFeatures))
                }
                
                specialSharedPreferences.saveTime(System.currentTimeMillis())
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Room Kaydetme Hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun searchCity(cityName: String) {
        weatherLoading.value = true

        // Önce Türkiye illeri listesinde ara
        val cityMatch = turkishCities.find { 
            it.first.equals(cityName, ignoreCase = true) || 
            it.first.replace("İ", "I").equals(cityName.replace("İ", "I"), ignoreCase = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = if (cityMatch != null) {
                    weatherAPIService.getWeatherData(
                        lat = cityMatch.second,
                        lon = cityMatch.third,
                        apiKey = BuildConfig.WEATHER_API_KEY
                    )
                } else {
                    weatherAPIService.getWeatherByCity(
                        cityName = cityName,
                        apiKey = BuildConfig.WEATHER_API_KEY
                    )
                }

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
                    // Sadece aranan şehri göstermek için showAllCities = false
                    saveToRoom(weatherFeatures, current, weatherList, showAllCities = false)
                    weatherLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherLoading.value = false
                    weatherErrorMessage.value = true
                    Toast.makeText(getApplication(), "Şehir bulunamadı: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}