package com.example.weatherapp.data.source.local.room.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.source.local.room.AppDB
import com.example.weatherapp.data.source.local.room.CityDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, "weather-db").build()
    }

    @Singleton
    @Provides
    fun provideCityDao(database: AppDB): CityDao {
        return database.cityDao()
    }

}