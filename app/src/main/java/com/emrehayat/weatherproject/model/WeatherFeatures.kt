package com.emrehayat.weatherproject.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WeatherFeatures(
    @PrimaryKey val id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val cityName: String,
    val temperature: Double,
    @ColumnInfo(name = "features_humidity") val humidity: Int, // Sütun adı özelleştirildi
    val windSpeed: Double,
    val rainfall: Double?,
    val weatherIcon: String?,
    val current: Current
)

@Entity
data class Current(
    @PrimaryKey val weatherFeaturesId: Int,
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    @ColumnInfo(name = "current_humidity") val humidity: Long, // Sütun adı özelleştirildi
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>
)

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0,
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
    val weatherFeaturesId: Int // WeatherFeatures ile ilişki kuran foreign key
)

data class WeatherWithDetails(
    @Embedded val weatherFeatures: WeatherFeatures,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherFeaturesId"
    )
    val weather: List<Weather>,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherFeaturesId"
    )
    val current: Current?
)



/*data class WeatherFeatures(
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
@Entity
data class Weather (
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0,
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)*/