package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _weatherResponse = MutableLiveData<WeatherResponse?>()
    val weatherResponse: LiveData<WeatherResponse?> = _weatherResponse

    private val _weatherResponseByCity = MutableLiveData<WeatherCityResponse?>()
    val weatherResponseByCity: LiveData<WeatherCityResponse?> = _weatherResponseByCity

    fun getWeatherByCity(cityName: String) {
        _isLoading.value = true
        Log.e("ViewModel", weatherRepository.toString())
        viewModelScope.launch {
            when(val weatherByCity = weatherRepository.getDataWeatherByCity(cityName)){
                is Resource.Success -> {
                    _weatherResponseByCity.postValue(weatherByCity.data)
                    getWeather("${weatherByCity.data?.coord?.lat}", "${weatherByCity.data?.coord?.lon}")
                }
                is Resource.Error -> {
                    _errorMessage.postValue("Error get data: ${weatherByCity.message}")
                }
                else -> {
                    _errorMessage.postValue("Not found!")
                }
            }
        }
    }




    private fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            when(val weather = weatherRepository.getDataWeather(lat, lon)){
                is Resource.Success -> {
                    _weatherResponse.postValue(weather.data)
                    _isLoading.postValue(false)
                }
                is Resource.Error -> _errorMessage.postValue("Error get data: ${weather.message}")
                else -> {
                    _errorMessage.postValue("Not found!")
                    _isLoading.postValue(false)
                }
            }
        }
    }

}