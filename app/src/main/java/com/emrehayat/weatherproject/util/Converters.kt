package com.emrehayat.weatherproject.util

import androidx.room.TypeConverter
import com.emrehayat.weatherproject.model.Current
import com.emrehayat.weatherproject.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Current nesnesini JSON'a çevirme
    @TypeConverter
    fun fromCurrent(current: Current?): String? {
        return gson.toJson(current)
    }

    // JSON'dan Current nesnesine dönüştürme
    @TypeConverter
    fun toCurrent(data: String?): Current? {
        val type = object : TypeToken<Current>() {}.type
        return gson.fromJson(data, type)
    }

    // Weather listesini JSON'a çevirme
    @TypeConverter
    fun fromWeatherList(weather: List<Weather>?): String? {
        return gson.toJson(weather)
    }

    // JSON'dan Weather listesine dönüştürme
    @TypeConverter
    fun toWeatherList(data: String?): List<Weather>? {
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(data, type)
    }
}