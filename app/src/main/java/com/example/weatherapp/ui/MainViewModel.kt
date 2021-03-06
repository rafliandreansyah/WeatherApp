package com.example.weatherapp.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Ignore
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.entity.City
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Weather
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Helper
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val context: Context

    ): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(true)
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
                    _isLoading.postValue(false)
                    if (!Helper.connectionIsActive(context)){
                        getWeather("${weatherByCity.data?.coord?.lat}", "${weatherByCity.data?.coord?.lon}")
                        _errorMessage.postValue("Error get data: You're offline, please check your connection")
                        return@launch
                    }
                    _errorMessage.postValue("Error get data: ${weatherByCity.message}")
                }
                else -> {
                    _errorMessage.postValue("Not found!")
                }
            }
        }
    }

    private fun getWeather(lat: String, lon: String) {
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.getDataWeatherWithCurrentAndDaily(lat, lon).collect {  weatherWithCurrentAndDaily ->
                when(weatherWithCurrentAndDaily){
                    is Resource.Loading -> {
                        if (weatherWithCurrentAndDaily.data != null){
                            val data = Helper.convertWeatherDataWithCurrentAndDailyToWeatherResponse(weatherWithCurrentAndDaily.data)
                            _weatherResponse.postValue(data)
                        }
                        _isLoading.postValue(true)
                    }
                    is Resource.Success -> {
                        //Convert object helper
                        val data = Helper.convertWeatherDataWithCurrentAndDailyToWeatherResponse(weatherWithCurrentAndDaily.data)

                        _weatherResponse.postValue(data)
                        _isLoading.postValue(false)
                    }
                    is Resource.Error -> {
                        _isLoading.postValue(false)
                        if (!Helper.connectionIsActive(context)){
                            _errorMessage.postValue("Error get data: You're offline, please check your connection")
                            return@collect
                        }
                        _errorMessage.postValue("Error get data: ${weatherWithCurrentAndDaily.message}")
                    }
                    else -> {
                        _errorMessage.postValue("Not found!")
                        _isLoading.postValue(false)
                    }
                }
            }

        }
    }

    fun getWeatherFirstTime(lat: String, lon: String){
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.getDataWeatherWithCurrentAndDaily(lat, lon).collect {  weatherWithCurrentAndDaily ->
                when(weatherWithCurrentAndDaily){
                    is Resource.Loading -> {
                        _isLoading.postValue(true)
                        Log.e("ViewModel Loading", weatherWithCurrentAndDaily?.data.toString())
                    }
                    is Resource.Success -> {
                        Log.e("ViewModel Success", weatherWithCurrentAndDaily?.data.toString())

                        //Convert object helper
                        val data = Helper.convertWeatherDataWithCurrentAndDailyToWeatherResponse(weatherWithCurrentAndDaily.data)

                        _weatherResponse.postValue(data)
                        _isLoading.postValue(false)
                    }
                    is Resource.Error -> {
                        weatherWithCurrentAndDaily.message?.let { Log.e("ViewModel Error", it) }
                        _errorMessage.postValue("Error get data: ${weatherWithCurrentAndDaily?.message}")
                        _isLoading.postValue(false)
                    }
                    else -> {
                        _errorMessage.postValue("Not found!")
                        _isLoading.postValue(false)
                    }
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