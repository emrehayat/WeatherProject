package com.emrehayat.weatherproject.service

import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {
    /*private val retrofit = Retrofit.Builder().
            baseUrl("https://api.openweathermap.org/").
            addConverterFactory(GsonConverterFactory.create()).
            build().
            create(WeatherAPI::class.java)

    suspend fun getData() : List<WeatherFeatures> {
        return retrofit.getWeather()
    }*/

    private val BASE_URL = "https://api.openweathermap.org/"

    val api: WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    suspend fun getWeatherData(lat: Double, lon: Double, exclude: String, apiKey: String): WeatherFeatures {
        return api.getWeather(lat, lon, exclude, apiKey)
    }
}