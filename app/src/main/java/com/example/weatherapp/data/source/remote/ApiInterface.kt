package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @GET("onecall")
    suspend fun getDataWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun getDataWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<WeatherCityResponse>

}