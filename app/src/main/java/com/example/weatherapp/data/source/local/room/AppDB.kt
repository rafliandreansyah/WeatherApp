package com.example.weatherapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.entity.City
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Weather
import com.example.weatherapp.data.source.local.room.dao.CityDao
import com.example.weatherapp.data.source.local.room.dao.WeatherDao
import com.example.weatherapp.data.source.remote.response.WeatherResponse


@Database(entities =
    [
        City::class,
        WeatherResponse::class,
        Current::class,
        Weather::class,
        Daily::class
    ] ,
    version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun weatherDao(): WeatherDao

}