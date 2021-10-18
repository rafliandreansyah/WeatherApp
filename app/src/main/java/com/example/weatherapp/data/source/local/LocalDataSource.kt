package com.example.weatherapp.data.source.local

import android.util.Log
import com.example.weatherapp.data.entity.City
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
import com.example.weatherapp.data.entity.relation.WeatherDataWithCurrentAndDaily
import com.example.weatherapp.data.source.local.room.dao.CityDao
import com.example.weatherapp.data.source.local.room.dao.WeatherDao
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao
) {

    suspend fun getAllCity() = cityDao.getAllCity()
    suspend fun insertCity(city: City) = cityDao.insertCity(city)


    suspend fun updateData(weatherResponse: WeatherResponse) = weatherDao.updateData(weatherResponse)
    fun getWeatherDataWithCurrentAndDaily(): Flow<WeatherDataWithCurrentAndDaily> = weatherDao.getWeatherDataWithCurrentAndDaily(1)

}