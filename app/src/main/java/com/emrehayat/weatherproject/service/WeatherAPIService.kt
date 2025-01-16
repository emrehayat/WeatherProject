package com.emrehayat.weatherproject.service

import com.emrehayat.weatherproject.model.WeatherApiResponse
import com.emrehayat.weatherproject.model.WeatherFeatures
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log

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

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url.toString()
            Log.d("WeatherAPI", "Request URL: $url")
            
            try {
                val response = chain.proceed(originalRequest)
                Log.d("WeatherAPI", "Response Code: ${response.code}")
                Log.d("WeatherAPI", "Response Body: ${response.peekBody(Long.MAX_VALUE).string()}")
                response
            } catch (e: Exception) {
                Log.e("WeatherAPI", "Error during request", e)
                throw e
            }
        }
        .build()

    private val api: WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    suspend fun getWeatherData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): WeatherApiResponse {
        Log.d("WeatherAPI", "Making API call with key: $apiKey")
        return api.getWeather(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )
    }

    suspend fun getWeatherByCity(
        cityName: String,
        apiKey: String
    ): WeatherApiResponse {
        return api.getWeatherByCity(
            cityName = cityName,
            apiKey = apiKey
        )
    }
}