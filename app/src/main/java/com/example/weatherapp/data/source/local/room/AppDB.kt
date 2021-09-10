package com.example.weatherapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.source.local.entity.City 


@Database(entities =[City::class] , version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {

    abstract fun cityDao(): CityDao

}