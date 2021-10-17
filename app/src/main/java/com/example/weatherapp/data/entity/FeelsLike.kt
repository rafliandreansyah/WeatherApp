package com.example.weatherapp.data.entity

import com.google.gson.annotations.SerializedName


data class FeelsLike(
    @SerializedName("day") var day_feels_like: Double,
    @SerializedName("eve") var eve_feels_like: Double,
    @SerializedName("morn") var morn_feels_like: Double,
    @SerializedName("night") var night_feels_like: Double
)