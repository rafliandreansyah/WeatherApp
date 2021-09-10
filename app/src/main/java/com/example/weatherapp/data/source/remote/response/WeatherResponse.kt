package com.example.weatherapp.data.source.remote.response

import com.example.weatherapp.data.source.remote.entity.Current
import com.example.weatherapp.data.source.remote.entity.Daily
import com.example.weatherapp.data.source.remote.entity.Hourly
import com.example.weatherapp.data.source.remote.entity.Minutely

data class WeatherResponse(
    val current: Current? = null,
    val hourly: List<Hourly>? = null,
    val lat: Double? = 0.0,
    val lon: Double? = 0.0,
    val minutely: List<Minutely>? = null,
    val daily: List<Daily>? = null,
    val timezone: String? = null,
    val timezone_offset: Int? = 0
)