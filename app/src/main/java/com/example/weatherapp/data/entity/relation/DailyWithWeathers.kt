package com.example.weatherapp.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Weather

data class DailyWithWeathers(
    @Embedded val daily: Daily,
    @Relation(
        parentColumn = "id",
        entityColumn = "dailyId"
    )
    val weathers: List<Weather>
)
