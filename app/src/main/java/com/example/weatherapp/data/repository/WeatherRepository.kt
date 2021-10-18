package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.entity.City
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
import com.example.weatherapp.data.entity.relation.WeatherDataWithCurrentAndDaily
import com.example.weatherapp.data.source.local.LocalDataSource
import com.example.weatherapp.data.source.remote.ApiInterface
import com.example.weatherapp.data.source.remote.BaseResponse
import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import com.example.weatherapp.utlis.DataStore
import com.example.weatherapp.utlis.Helper
import com.example.weatherapp.utlis.Resource
import com.example.weatherapp.utlis.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: ApiInterface,
    private val localDataSource: LocalDataSource,
    private val dataStore: DataStore,
    private val context: Context
): BaseResponse() {

    suspend fun getDataWeatherByCity(cityName: String): Resource<WeatherCityResponse> =
        withContext(Dispatchers.IO){
            safeCallApi {
                remoteDataSource.getDataWeatherByCity(cityName, BuildConfig.API_KEY, BuildConfig.UNITS)
            }
        }


    suspend fun getDataWeatherWithCurrentAndDaily(lat: String, lon: String): Flow<Resource<WeatherDataWithCurrentAndDaily>?> =
        networkBoundResource(
            query = {
                localDataSource.getWeatherDataWithCurrentAndDaily()
            },
            fetch = {
                remoteDataSource.getDataWeather(lat, lon, "minutely,hourly", BuildConfig.API_KEY, BuildConfig.UNITS)
            },
            saveFetchResult = {
                localDataSource.updateData(it)
            },
            shouldFetch = {
                Helper.connectionIsActive(context)
            }
        )


    suspend fun getDataCityLocal(): List<City> =
        withContext(Dispatchers.IO){
            localDataSource.getAllCity()
        }

    suspend fun insertCityLocal(city: City) =
        withContext(Dispatchers.IO){
            localDataSource.insertCity(city)
        }

    suspend fun addSelectedCity(cityName: String) =
        withContext(Dispatchers.IO){
            dataStore.addSelectedCity(cityName)
        }

    val getSelectedCity: Flow<String> = dataStore.citySelected

    suspend fun clearDataStore() =
        withContext(Dispatchers.IO){
            dataStore.clearDataStore()
        }

}