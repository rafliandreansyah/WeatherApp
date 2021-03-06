package com.example.weatherapp.data.source.local.room.dao

import androidx.room.*
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.Weather
import com.example.weatherapp.data.entity.relation.CurrentWithWeathers
import com.example.weatherapp.data.entity.relation.DailyWithWeathers
import com.example.weatherapp.data.entity.relation.WeatherDataWithCurrentAndDaily
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Transaction
    @Query("SELECT * FROM WeatherResponse WHERE id = :id")
    fun getWeatherDataWithCurrentAndDaily(id: Long): Flow<WeatherDataWithCurrentAndDaily>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeathers(weathers: List<Weather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrent(current: Current): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDaily(daily: Daily): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherResponse(weatherResponse: WeatherResponse) : Long

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
        val weatherID = insertWeatherResponse(newWeatherResponse)

        // Return currentId
        val newCurrent = weatherResponse.current?.copy(id = 1, weatherDataId = weatherID)
        val currentId = newCurrent?.let { insertCurrent(it) }

        // Add current foreign key to weather
        val weathersCurrent = weatherResponse.current?.weather?.map { weather ->
            weather.copy(
                currentId = currentId
            )
        }

        //Insert weather with foreign key current
        weathersCurrent?.let { insertWeathers(it) }

        weatherResponse.daily?.forEach {
            // return dailyId
            val daily = it.copy(
                weatherDataId = weatherID
            )
            val dailyId = insertDaily(daily)

            // Add daily id to weather
            val weathersDaily = daily.weather?.map { weather ->
                weather.copy(
                    dailyId = dailyId
                )
            }

            // Insert Weather with foreign key from daily
            weathersDaily?.let { weathers -> insertWeathers(weathers) }
        }

    }

}