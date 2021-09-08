package com.example.weatherapp.data.source.local.entity

import androidx.room.PrimaryKey

data class Weather(
    val description: String,
    val icon: String,
    @PrimaryKey
    val id: Int,
    val main: String
)