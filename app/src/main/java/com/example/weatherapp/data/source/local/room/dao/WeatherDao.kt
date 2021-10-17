package com.example.weatherapp.data.source.local.room.dao

import androidx.room.*
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Weather
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherResponse WHERE id = :id")
    fun getWeather(id: Long): Flow<WeatherResponse>

    @Query("SELECT * FROM Current WHERE id = :id")
    fun getCurrentWithWeathers(id: Long): Flow<CurrentWithWeathers>

    @Query("SELECT * FROM Daily")
    fun getDailyWithWeathers(): Flow<List<DailyWithWeathers>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeathers(weathers: List<Weather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrent(current: Current): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDaily(daily: Daily): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDaily(daily: List<Daily>)

    @Query("DELETE FROM Current")
    fun deleteCurrentData()

    @Query("DELETE FROM Daily")
    fun deleteDailyData()

    @Query("DELETE FROM Weather")
    fun deleteWeather()

    @Transaction
    suspend fun updateData(weatherResponse: WeatherResponse){
        deleteCurrentData()
        deleteDailyData()
        deleteWeather()

        val newWeatherResponse = weatherResponse.copy(id = 1)
        insertWeatherResponse(newWeatherResponse)

        // Return currentId
        val newCurrent = weatherResponse.current?.copy(id = 1)
        val currentId = newCurrent?.let { insertCurrent(it) }

        // Add current foreign key to weather
        val weathersCurrent = weatherResponse.current?.weather?.map { weather ->
            weather.copy(
                currentId = currentId
            )
        }

        //Insert weather with foreign key current
        weathersCurrent?.let { insertWeathers(it) }

        weatherResponse.daily?.forEach { daily ->
            // return dailyId
            val dailyId = insertDaily(daily)

            // Add daily id to weather
            val weathersDaily = daily.weather?.map { weather ->
                weather.copy(
                    dailyId = dailyId
                )
            }

            // Insert Weather with foreign key from daily
            weathersDaily?.let { insertWeathers(it) }
        }

    }

}