package com.example.weatherapp.data.source.local.entity

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.example.weatherapp.data.source.remote.entity.Temp

data class Daily(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    @Embedded val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val uvi: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)