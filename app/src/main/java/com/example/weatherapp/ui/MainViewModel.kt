package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.entity.City
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
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

    private val _current = MutableLiveData<Current>()
    val current: LiveData<Current> = _current

    private val _upcoming = MutableLiveData<List<Daily>>()
    val upcoming: LiveData<List<Daily>> = _upcoming

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
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.getDataWeather(lat, lon).collect {  weatherResponse ->
                when(weatherResponse){
                    is Resource.Loading -> {
                        _isLoading.postValue(true)
                    }
                    is Resource.Success -> {
                        _weatherResponse.postValue(weatherResponse.data)
                        _isLoading.postValue(false)
                    }
                    is Resource.Error -> _errorMessage.postValue("Error get data: ${weatherResponse.message}")
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
            weatherRepository.getDataWeather(lat, lon).collect {  weatherResponse ->
                when(weatherResponse){
                    is Resource.Loading -> {
                        _isLoading.postValue(true)
                        Log.e("ViewModel Loading", weatherResponse.data.toString())
                    }
                    is Resource.Success -> {
                        Log.e("ViewModel Success", weatherResponse.data.toString())
                        _weatherResponse.postValue(weatherResponse.data)
                        getCurrentData()
                        getDaily()
                        _isLoading.postValue(false)
                    }
                    is Resource.Error -> {
                        weatherResponse.message?.let { Log.e("ViewModel Error", it) }
                        _errorMessage.postValue("Error get data: ${weatherResponse.message}")
                    }
                    else -> {
                        _errorMessage.postValue("Not found!")
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }

    private fun getCurrentData(){
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.getCurrent().collect { currentWithWeathers ->
                val current = Current()
                current.id = currentWithWeathers.current.id
                current.clouds = currentWithWeathers.current.clouds
                current.dew_point = currentWithWeathers.current.dew_point
                current.dt = currentWithWeathers.current.dt
                current.feels_like = currentWithWeathers.current.feels_like
                current.humidity = currentWithWeathers.current.humidity
                current.pressure = currentWithWeathers.current.pressure
                current.sunrise = currentWithWeathers.current.sunrise
                current.sunset = currentWithWeathers.current.sunset
                current.temp = currentWithWeathers.current.temp
                current.uvi = currentWithWeathers.current.uvi
                current.visibility = currentWithWeathers.current.visibility
                current.weather = currentWithWeathers.weathers
                current.wind_deg = currentWithWeathers.current.wind_deg
                current.wind_gust = currentWithWeathers.current.wind_gust
                current.wind_speed = currentWithWeathers.current.wind_speed

                _current.postValue(current)
            }
        }
    }

    private fun getDaily(){
        _isLoading.value = true
        viewModelScope.launch {
            weatherRepository.getListUpComming().collect { listDailyWithWeathers ->
                val listDaily = ArrayList<Daily>()
                listDailyWithWeathers.forEach {
                    var daily = Daily()
                    daily.id = it.daily.id
                    daily.clouds = it.daily.clouds
                    daily.dew_point = it.daily.dew_point
                    daily.dt = it.daily.dt
                    daily.feels_like = it.daily.feels_like
                    daily.humidity = it.daily.humidity
                    daily.moon_phase = it.daily.moon_phase
                    daily.moonrise = it.daily.moonrise
                    daily.moonset = it.daily.moonset
                    daily.pop = it.daily.pop
                    daily.pressure = it.daily.pressure
                    daily.sunrise = it.daily.sunrise
                    daily.sunset = it.daily.sunset
                    daily.temp = it.daily.temp
                    daily.uvi = it.daily.uvi
                    daily.weather = it.weathers
                    daily.wind_deg = it.daily.wind_deg
                    daily.wind_gust = it.daily.wind_gust
                    daily.wind_speed = it.daily.wind_speed

                    listDaily.add(daily)
                }

                _upcoming.postValue(listDaily)
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