package com.example.weatherapp.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Weather

data class CurrentWithWeathers(
    @Embedded val current: Current,
    @Relation(
        parentColumn = "id",
        entityColumn = "currentId",
    )
    val weathers: List<Weather>
)
