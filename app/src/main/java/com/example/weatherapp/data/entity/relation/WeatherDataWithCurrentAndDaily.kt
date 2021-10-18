package com.example.weatherapp.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.source.remote.response.WeatherResponse


data class WeatherDataWithCurrentAndDaily(
    @Embedded val weatherResponse: WeatherResponse?,

    @Relation(
        entity = Current::class,
        parentColumn = "id",
        entityColumn = "weatherDataId"
    )
    val currentWithWeathers: CurrentWithWeathers?,

    @Relation(
        entity = Daily::class,
        parentColumn = "id",
        entityColumn = "weatherDataId"
    )
    val listDaily: List<DailyWithWeathers>?
)
