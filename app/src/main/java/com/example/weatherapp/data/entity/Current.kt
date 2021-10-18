package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Current(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var clouds: Int? = null,
    var dew_point: Double? = null,
    var dt: Int? = null,
    var feels_like: Double? = null,
    var humidity: Int? = null,
    var pressure: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Double? = null,
    var uvi: Double? = null,
    var visibility: Int? = null,
    @Ignore var weather: List<Weather>? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var wind_speed: Double? = null,
    var weatherDataId: Long? = null
)