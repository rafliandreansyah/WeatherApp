package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    fun getWeatherByCity(cityName: String): LiveData<Resource<WeatherCityResponse>> =
        weatherRepository.getDataWeatherByCity(cityName)



    fun getWeather(lat: String, lon: String): LiveData<Resource<WeatherResponse>> =
        weatherRepository.getDataWeather(lat, lon)
}