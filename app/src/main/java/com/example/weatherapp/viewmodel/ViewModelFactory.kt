package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.ui.MainViewModel

class ViewModelFactory(private val weatherRepository: WeatherRepository): ViewModelProvider.NewInstanceFactory() {

//    companion object {
//        private var instance: ViewModelFactory? = null
//
//        fun getInstance(context: Context): ViewModelFactory? =
//            instance ?: synchronized(this) {
//                instance ?: Inject.provideRepository(context)?.let { ViewModelFactory(it) }.apply {
//                    instance = this
//                }
//            }
//    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(weatherRepository) as T
            }
            else -> throw Throwable("View Model Unknown ${modelClass.name}")
        }
    }

}