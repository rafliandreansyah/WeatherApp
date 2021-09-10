package com.example.weatherapp.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Current(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "clouds")
    val clouds: Int,

    @ColumnInfo(name = "dew_point")
    val dew_point: Double,

    @ColumnInfo(name = "dt")
    val dt: Int,

    @ColumnInfo(name = "feels_like")
    val feels_like: Double,

    @ColumnInfo(name = "humidity")
    val humidity: Int,

    @ColumnInfo(name = "pressure")
    val pressure: Int,

    @ColumnInfo(name = "sunrise")
    val sunrise: Int,

    @ColumnInfo(name = "sunset")
    val sunset: Int,

    @ColumnInfo(name = "temp")
    val temp: Double,

    @ColumnInfo(name = "uvi")
    val uvi: Double,

    @ColumnInfo(name = "visibility")
    val visibility: Int,

    @ColumnInfo(name = "wind_deg")
    val wind_deg: Int,

    @ColumnInfo(name = "wind_gust")
    val wind_gust: Double,

    @ColumnInfo(name = "wind_speed")
    val wind_speed: Double
)