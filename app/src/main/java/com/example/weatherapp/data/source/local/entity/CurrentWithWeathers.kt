package com.example.weatherapp.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CurrentWithWeathers(
    @Embedded val current: Current,
    @Relation(
        parentColumn = "id",
        entityColumn = "currentId"
    )
    val weather: List<Weather>
)
