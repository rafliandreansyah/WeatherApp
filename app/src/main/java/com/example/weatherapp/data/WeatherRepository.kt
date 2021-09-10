package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.source.local.entity.City
import com.example.weatherapp.data.source.local.room.CityDao
import com.example.weatherapp.data.source.remote.ApiInterface
import com.example.weatherapp.data.source.remote.BaseResponse
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: ApiInterface,
    private val localDataSource: CityDao
): BaseResponse() {

    suspend fun getDataWeatherByCity(cityName: String): Resource<WeatherCityResponse> =
        withContext(Dispatchers.IO){
            safeCallApi {
                remoteDataSource.getDataWeatherByCity(cityName, BuildConfig.API_KEY, BuildConfig.UNITS)
            }
        }




    suspend fun getDataWeather(lat: String, lon: String): Resource<WeatherResponse> =
        withContext(Dispatchers.IO){
            safeCallApi {
                remoteDataSource.getDataWeather(lat, lon, "minutely,hourly", BuildConfig.API_KEY, BuildConfig.UNITS)
            }
        }


    suspend fun getDataCityLocal(): List<City> =
        withContext(Dispatchers.IO){
            localDataSource.getAllCity()
        }

    suspend fun insertCityLocal(city: City) =
        withContext(Dispatchers.IO){
            localDataSource.insertCity(city)
        }

}