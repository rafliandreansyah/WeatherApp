package com.example.weatherapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clouds(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var all: Int?
)