package com.example.weatherapp.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.source.local.LocalDataSource
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class RemoteDataSource {

    private val appId = "14b080a024d7244671a6e4022c6d4e00"

    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource? =
            instance ?: synchronized(this){
                instance ?: RemoteDataSource().apply {
                    instance = this
                }
            }

    }

    fun getWeatherByLatLon(lat: String, lon: String): LiveData<Resource<WeatherResponse>>{
        val weatherResult = MutableLiveData<Resource<WeatherResponse>>()
        weatherResult.value = Resource.Loading()
        ApiConfig.apiService().getDataWeather(lat, lon, "hourly,dialy", appId, "metric").enqueue(object : Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful){
                    weatherResult.value = response.body()?.let { Resource.Success(it) }
                }
                else {
                    weatherResult.value = response.errorBody()?.string()?.let { Resource.Error(it) }
                }

            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherResult.value = Resource.Error(t.message.toString())
            }

        })

        return weatherResult
    }

    fun getWeatherByCity(cityName: String): LiveData<Resource<WeatherCityResponse>>{
        val weatherResultByCity = MutableLiveData<Resource<WeatherCityResponse>>()
        weatherResultByCity.value = Resource.Loading()
        ApiConfig.apiService().getDataWeatherByCity(cityName, appId, "metric").enqueue(object : Callback<WeatherCityResponse>{
            override fun onResponse(
                call: Call<WeatherCityResponse>,
                response: Response<WeatherCityResponse>
            ) {
                if (response.isSuccessful){
                    weatherResultByCity.value = response.body()?.let { Resource.Success(it) }
                }
                else{
                    weatherResultByCity.value = response.errorBody()?.string()?.let { Resource.Error(it) }
                }
            }

            override fun onFailure(call: Call<WeatherCityResponse>, t: Throwable) {
                weatherResultByCity.value = Resource.Error(t.message.toString())
            }

        })
        return weatherResultByCity
    }


}