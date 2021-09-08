package com.example.weatherapp.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DialyWithWeathers(
    @Embedded
    val daily: Daily,
    @Relation(
        parentColumn = "id",
        entityColumn = "dailyId"
    )
    val weather: List<Weather>
)