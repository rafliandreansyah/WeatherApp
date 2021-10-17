package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wind(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var deg: Int?,
    var gust: Double?,
    var speed: Double?
)