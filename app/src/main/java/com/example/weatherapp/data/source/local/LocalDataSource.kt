package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.local.room.WeatherDao

class LocalDataSource(private val cityDao: WeatherDao) {

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(weatherDao: WeatherDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(weatherDao)
            }
    }

}