package com.example.weatherapp.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "city_name")
    val cityName: String? = null
)
