package com.example.weatherapp.utlis

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

val Context.cityDataStore: DataStore<Preferences> by preferencesDataStore(name = "city")

class DataStore @Inject constructor(context: Context) {

    private val appContext = context.applicationContext

    private val PREFERENCE_KEY = stringPreferencesKey("selected_city")
    val citySelected : Flow<String> = appContext.cityDataStore.data.map { preference ->
        preference[PREFERENCE_KEY] ?: "not found"
    }

    suspend fun addSelectedCity(cityName: String) {
        appContext.cityDataStore.edit { preferences ->
            preferences[PREFERENCE_KEY] = cityName
        }
    }

    suspend fun clearDataStore() {
        appContext.cityDataStore.edit { preferences ->
            preferences.clear()
        }
    }

}