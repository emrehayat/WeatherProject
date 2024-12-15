package com.emrehayat.weatherproject.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emrehayat.weatherproject.model.Current
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures

@Database(entities = [WeatherFeatures::class, Weather::class, Current::class], version = 1, exportSchema = false)
//@Database(entities = [Weather::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDAO

    companion object {

        @Volatile
        private var instance: WeatherDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "besindatabase"
        ).build()
    }
}