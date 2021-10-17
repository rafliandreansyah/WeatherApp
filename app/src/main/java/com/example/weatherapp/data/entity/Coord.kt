package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Coord(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var lat: Double?,
    var lon: Double?
)