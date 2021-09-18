package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.source.local.entity.City
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _weatherResponse = MutableLiveData<WeatherResponse?>()
    val weatherResponse: LiveData<WeatherResponse?> = _weatherResponse

    private val _weatherResponseFirstTime = MutableLiveData<WeatherResponse?>()
    val weatherResponseFirstTime: LiveData<WeatherResponse?> = _weatherResponseFirstTime

    private val _weatherResponseByCity = MutableLiveData<WeatherCityResponse?>()
    val weatherResponseByCity: LiveData<WeatherCityResponse?> = _weatherResponseByCity

    private val _dataCityFromLocal = MutableLiveData<ArrayList<String>>()
    val dataCityFromLocal: LiveData<ArrayList<String>> = _dataCityFromLocal

    private val _selectedCity = MutableStateFlow<String>("")
    val selectedCity: StateFlow<String> = _selectedCity

    init {
        viewModelScope.launch {
            _isLoading.value = true
            weatherRepository.getSelectedCity.collect { selectedCity ->
                _isLoading.value = false
                _selectedCity.value = selectedCity
            }
        }
    }

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

    fun getWeatherFirstTime(lat: String, lon: String){
        _isLoading.value = true
        viewModelScope.launch {
            when(val weather = weatherRepository.getDataWeather(lat, lon)){
                is Resource.Success -> {
                    _weatherResponseFirstTime.postValue(weather.data)
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

    fun getAllCityLocal(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val dataCity = weatherRepository.getDataCityLocal()
                val listCity = ArrayList<String>()
                dataCity.forEach {
                    listCity.add("${it.cityName?.capitalize()}")
                }
                _dataCityFromLocal.postValue(listCity)
                if (dataCity.isNotEmpty()){
                    _isLoading.postValue(false)
                }

            }catch (e: Exception){
                _errorMessage.postValue("Get data city local failed: ${e.message}")
            }
        }
    }

    fun insertDataCity(cityName: String){
        val city = City()
        city.cityName = cityName
        _isLoading.value = true
        viewModelScope.launch {
            try {
                weatherRepository.insertCityLocal(city)
            }catch (e: Exception){
                _errorMessage.postValue("Insert data city local failed: ${e.message}")
            }
        }
    }

    fun addSelectedCity(cityName: String){
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.addSelectedCity(cityName)
        }
    }

    fun clearDataStore(){
        viewModelScope.launch {
            weatherRepository.clearDataStore()
        }
    }

}