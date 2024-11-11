package com.emrehayat.weatherproject.model

data class WeatherFeatures(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current
)

data class Current (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>
)

data class Weather (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)