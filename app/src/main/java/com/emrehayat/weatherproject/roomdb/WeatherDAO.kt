package com.emrehayat.weatherproject.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.emrehayat.weatherproject.model.Current
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.model.WeatherWithDetails

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherFeatures(weatherFeatures: WeatherFeatures): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrent(current: Current)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: List<Weather>)

    @Transaction
    suspend fun insertAll(weatherFeatures: WeatherFeatures, current: Current, weather: List<Weather>) {
        val id = insertWeatherFeatures(weatherFeatures)
        current.copy(weatherFeaturesId = id.toInt()).let { insertCurrent(it) }
        weather.map { it.copy(weatherFeaturesId = id.toInt()) }.let { insertWeather(it) }
    }

    @Query("SELECT * FROM weather_features")
    suspend fun getAllWeather(): List<WeatherFeatures>

    @Query("DELETE FROM weather_features")
    suspend fun deleteAllWeather()

    @Query("DELETE FROM current_weather")
    suspend fun deleteAllCurrent()

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeatherDetails()

    @Transaction
    suspend fun deleteAll() {
        deleteAllWeather()
        deleteAllCurrent()
        deleteAllWeatherDetails()
    }

    @Transaction
    @Query("SELECT * FROM weather_features WHERE id = :uuid")
    suspend fun getWeatherWithDetails(uuid: Int): WeatherWithDetails
}