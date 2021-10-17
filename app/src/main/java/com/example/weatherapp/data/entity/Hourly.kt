package com.example.weatherapp.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Hourly(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var clouds: Int?,
    var dew_point: Double?,
    var dt: Int?,
    var feels_like: Double?,
    var humidity: Int?,
    var pop: Double?,
    var pressure: Int?,
    @Embedded var rain: Rain?,
    var temp: Double?,
    var uvi: Double?,
    var visibility: Int?,
    @Ignore var weather: List<Weather>?,
    var wind_deg: Int?,
    var wind_gust: Double?,
    var wind_speed: Double?
)