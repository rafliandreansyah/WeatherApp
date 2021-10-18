package com.example.weatherapp.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Daily(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var clouds: Int? = null,
    var dew_point: Double? = null,
    var dt: Int? = null,
    @Embedded var feels_like: FeelsLike? = null,
    var humidity: Int? = null,
    var moon_phase: Double? = null,
    var moonrise: Int? = null,
    var moonset: Int? = null,
    var pop: Double? = null,
    var pressure: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    @Embedded var temp: Temp? = null,
    var uvi: Double? = null,
    @Ignore var weather: List<Weather>? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var wind_speed: Double? = null,
    var weatherDataId: Long? = null
)