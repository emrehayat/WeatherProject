package com.emrehayat.weatherproject.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emrehayat.weatherproject.model.Weather

@Database(entities = [Weather::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDAO
}