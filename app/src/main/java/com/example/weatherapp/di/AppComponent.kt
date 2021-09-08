package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.source.remote.ApiConfig
import com.example.weatherapp.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)

}