package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.MainViewModel

class ViewModelFactory<VM: ViewModel>(val provider: () -> VM): ViewModelProvider.NewInstanceFactory() {

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
        return provider() as T
    }

}