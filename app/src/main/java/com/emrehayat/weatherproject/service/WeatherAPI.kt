package com.emrehayat.weatherproject.service

import com.emrehayat.weatherproject.model.WeatherFeatures
import retrofit2.http.GET

interface WeatherAPI {
    //https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=ce77025c3bbb2b5913f1a410ba3355ca

    //BASE URL -> https://api.openweathermap.org/
    //ENDPOINT -> data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=ce77025c3bbb2b5913f1a410ba3355ca

    @GET("data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid=ce77025c3bbb2b5913f1a410ba3355ca")
    suspend fun getWeather() : List<WeatherFeatures>
}