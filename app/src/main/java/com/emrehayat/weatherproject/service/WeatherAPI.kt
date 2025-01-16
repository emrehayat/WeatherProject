package com.emrehayat.weatherproject.service


import com.emrehayat.weatherproject.BuildConfig
import com.emrehayat.weatherproject.model.WeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    //https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}

    //BASE URL -> https://api.openweathermap.org/
    //ENDPOINT -> data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}

    //@GET("data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=${BuildConfig.API_KEY}")
    //suspend fun getWeather() : List<WeatherFeatures>

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "tr"
    ): WeatherApiResponse

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "tr"
    ): WeatherApiResponse
}