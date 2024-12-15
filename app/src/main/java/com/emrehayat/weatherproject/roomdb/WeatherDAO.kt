package com.emrehayat.weatherproject.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherWithDetails

@Dao
interface WeatherDAO {
    @Insert
    suspend fun insertAll(vararg weather: Weather) : List<Long>

    @Query("SELECT * FROM weather")
    suspend fun getAllWeather() : List<Weather>

    @Query("SELECT * FROM weather WHERE uuid = :weatherId")
    suspend fun getWeather(weatherId : Int) : Weather

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()

    @Transaction
    @Query("SELECT * FROM WeatherFeatures WHERE id = :uuid")
    suspend fun getWeatherWithDetails(uuid: Int): WeatherWithDetails
}