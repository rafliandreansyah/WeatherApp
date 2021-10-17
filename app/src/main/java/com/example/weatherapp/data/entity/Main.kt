package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Main(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var feels_like: Double?,
    var grnd_level: Int?,
    var humidity: Int?,
    var pressure: Int?,
    var sea_level: Int?,
    var temp: Double?,
    var temp_max: Double?,
    var temp_min: Double?
)