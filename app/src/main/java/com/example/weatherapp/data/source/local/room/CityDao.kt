package com.example.weatherapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.source.local.entity.City
import javax.inject.Singleton

@Singleton
@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    suspend fun getAllCity(): List<City>

    @Insert
    suspend fun insertCity(city: City)

}