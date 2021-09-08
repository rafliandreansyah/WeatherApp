package com.example.weatherapp.data.source.remote.response

import com.example.weatherapp.data.source.remote.entity.Current
import com.example.weatherapp.data.source.remote.entity.Daily
import com.example.weatherapp.data.source.remote.entity.Hourly
import com.example.weatherapp.data.source.remote.entity.Minutely

data class WeatherResponse(
    val current: Current,
    val hourly: List<Hourly>? = null,
    val lat: Double,
    val lon: Double,
    val minutely: List<Minutely>,
    val daily: List<Daily>? = null,
    val timezone: String,
    val timezone_offset: Int
)