package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sys(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var country: String?,
    var sunrise: Int?,
    var sunset: Int?
)