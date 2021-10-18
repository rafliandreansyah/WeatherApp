package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.entity.Main
import com.example.weatherapp.data.source.local.room.di.DatabaseModule
import com.example.weatherapp.data.source.remote.ApiConfig
import com.example.weatherapp.ui.MainActivity
import com.example.weatherapp.ui.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton



@Singleton
@Component(modules = [ApiConfig::class, DatabaseModule::class])
interface AppComponent {

    val mainViewModel: MainViewModel

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)

}