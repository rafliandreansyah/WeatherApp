package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    var description: String? = null,
    var icon: String? = null,
    var id: Long = 0,
    @PrimaryKey(autoGenerate = true)
    var weatherIdLocal: Long = 0,
    var currentId: Long? = 0,
    var dailyId: Long? = 0,
    var main: String? = null,
)