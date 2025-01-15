package com.emrehayat.weatherproject.model

import androidx.room.*

@Entity(tableName = "weather_features")
data class WeatherFeatures(
    @PrimaryKey val id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @ColumnInfo(name = "timezone_offset") val timezoneOffset: Long,
    @ColumnInfo(name = "city_name") val cityName: String,
    val temperature: Double,
    @ColumnInfo(name = "features_humidity") val humidity: Int,
    @ColumnInfo(name = "features_wind_speed") val windSpeed: Double,
    val rainfall: Double?,
    @ColumnInfo(name = "weather_icon") val weatherIcon: String?
)

@Entity(tableName = "current_weather")
data class Current(
    @PrimaryKey
    @ColumnInfo(name = "weather_features_id") val weatherFeaturesId: Int,
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    @ColumnInfo(name = "feels_like") val feelsLike: Double,
    val pressure: Long,
    @ColumnInfo(name = "current_humidity") val humidity: Long,
    @ColumnInfo(name = "dew_point") val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    @ColumnInfo(name = "current_wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "wind_deg") val windDeg: Long,
    @ColumnInfo(name = "wind_gust") val windGust: Double
)

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0,
    val main: String,
    val description: String,
    val icon: String,
    @ColumnInfo(name = "weather_features_id") val weatherFeaturesId: Int
)

data class WeatherWithDetails(
    @Embedded val weatherFeatures: WeatherFeatures,
    @Relation(
        parentColumn = "id",
        entityColumn = "weather_features_id"
    )
    val current: Current,
    @Relation(
        parentColumn = "id",
        entityColumn = "weather_features_id"
    )
    val weather: List<Weather>
)