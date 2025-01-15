package com.emrehayat.weatherproject.model

data class WeatherApiResponse(
    val coord: Coord,
    val weather: List<WeatherItem>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
) 