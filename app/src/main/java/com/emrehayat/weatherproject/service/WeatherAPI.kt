package com.emrehayat.weatherproject.service


import com.emrehayat.weatherproject.BuildConfig
import com.emrehayat.weatherproject.model.WeatherFeatures
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    //https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}

    //BASE URL -> https://api.openweathermap.org/
    //ENDPOINT -> data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}

    //@GET("data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}")
    //suspend fun getWeather() : List<WeatherFeatures>

    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): WeatherFeatures
}