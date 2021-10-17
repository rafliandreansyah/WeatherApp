package com.example.weatherapp.data.source.remote.response

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Hourly
import com.example.weatherapp.data.entity.Minutely

@Entity
data class WeatherResponse(
    @PrimaryKey var id: Long = 0L,
    @Ignore var current: Current? = null,
    @Ignore var hourly: List<Hourly>? = null,
    var lat: Double? = 0.0,
    var lon: Double? = 0.0,
    @Ignore var minutely: List<Minutely>? = null,
    @Ignore var daily: List<Daily>? = null,
    var timezone: String? = null,
    var timezone_offset: Int? = 0
)