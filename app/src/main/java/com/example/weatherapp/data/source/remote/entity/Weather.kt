package com.example.weatherapp.data.source.remote.entity

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)