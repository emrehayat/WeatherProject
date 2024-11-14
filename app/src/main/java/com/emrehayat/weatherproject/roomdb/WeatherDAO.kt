package com.emrehayat.weatherproject.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emrehayat.weatherproject.model.Weather

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
}