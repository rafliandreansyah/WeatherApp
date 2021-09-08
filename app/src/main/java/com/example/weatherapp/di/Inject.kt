package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.source.local.LocalDataSource
import com.example.weatherapp.data.source.remote.RemoteDataSource
import com.example.weatherapp.utlis.AppExecutor

object Inject {
        fun provideRepository(context: Context): WeatherRepository? {

//            val database = AppDB.getInstance(context)

            val remoteDataSource = RemoteDataSource.getInstance()
//            val localDataSource = LocalDataSource.getInstance(database.weatherDao())
//            val appExecutor = AppExecutor()

            return remoteDataSource?.let {
                WeatherRepository.getInstance(remoteDataSource)
            }
        }
}