package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.weatherapp.data.source.remote.BaseResponse
import com.example.weatherapp.data.source.remote.RemoteDataSource
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): BaseResponse() {

    fun getDataWeatherByCity(cityName: String): LiveData<Resource<WeatherCityResponse>> =
        remoteDataSource.getWeatherByCity(cityName)

    fun getDataWeather(lat: String, lon: String): LiveData<Resource<WeatherResponse>> = remoteDataSource.getWeatherByLatLon(lat, lon)
}