package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.source.local.LocalDataSource
import com.example.weatherapp.data.source.remote.RemoteDataSource
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.AppExecutor
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    companion object{
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource
        ): WeatherRepository? =
            instance ?: synchronized(this){
                instance ?: WeatherRepository(remoteDataSource).apply {
                    instance = this
                }
            }

    }



    fun getDataWeatherByCity(cityName: String): LiveData<Resource<WeatherCityResponse>> = remoteDataSource.getWeatherByCity(cityName)

    fun getDataWeather(lat: String, lon: String): LiveData<Resource<WeatherResponse>> = remoteDataSource.getWeatherByLatLon(lat, lon)
}