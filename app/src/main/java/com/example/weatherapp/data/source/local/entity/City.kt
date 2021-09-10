package com.example.weatherapp.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "city_name")
    var cityName: String? = null
)
