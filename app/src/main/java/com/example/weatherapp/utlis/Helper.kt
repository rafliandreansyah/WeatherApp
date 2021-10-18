package com.example.weatherapp.utlis

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.data.entity.Current
import com.example.weatherapp.data.entity.Daily
import com.example.weatherapp.data.entity.relation.WeatherDataWithCurrentAndDaily
import com.example.weatherapp.data.source.remote.response.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

object Helper {

    fun convertDate(timeMilis: Long): String{
        val format = SimpleDateFormat("EEEE")
        val netDate = Date(timeMilis * 1000)
        return format.format(netDate)
    }

    fun convertDateFullFormat(timeMilis: Long): String{
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm aaa")
        val netDate = Date(timeMilis * 1000)
        return format.format(netDate)
    }

    @SuppressLint("MissingPermission")
    fun connectionIsActive(context: Context): Boolean {
        val connectionManager = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val info = connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)
            if (info != null){
                when{
                    info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    info.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
            else{
                return false
            }
        }
        else{
            val info = connectionManager.activeNetworkInfo
            return info != null && info.isConnected
        }
        return false
    }

    fun convertWeatherDataWithCurrentAndDailyToWeatherResponse(data: WeatherDataWithCurrentAndDaily?): WeatherResponse{
        val weatherResponse = WeatherResponse()

        val dailyData = ArrayList<Daily>()
        val current = Current()
        data?.listDaily?.forEach {
            val daily = Daily(
                id = it.daily.id,
                clouds = it.daily.clouds,
                dew_point = it.daily.dew_point,
                dt = it.daily.dt,
                feels_like = it.daily.feels_like,
                humidity = it.daily.humidity,
                moon_phase = it.daily.moon_phase,
                moonrise = it.daily.moonrise,
                moonset = it.daily.moonset,
                pop = it.daily.pop,
                pressure = it.daily.pressure,
                sunrise = it.daily.sunrise,
                sunset = it.daily.sunset,
                temp = it.daily.temp,
                uvi = it.daily.uvi,
                weather = it.weathers,
                wind_deg = it.daily.wind_deg,
                wind_gust = it.daily.wind_gust,
                wind_speed = it.daily.wind_speed,
                weatherDataId = it.daily.weatherDataId
            )
            dailyData.add(daily)
        }

        if (data?.currentWithWeathers?.current != null){
            current.id = data?.currentWithWeathers?.current.id
            current.clouds = data?.currentWithWeathers?.current.clouds
            current.dew_point = data?.currentWithWeathers?.current.dew_point
            current.dt = data?.currentWithWeathers?.current.dt
            current.feels_like = data?.currentWithWeathers?.current.feels_like
            current.humidity = data?.currentWithWeathers?.current.humidity
            current.pressure = data?.currentWithWeathers?.current.pressure
            current.sunrise = data?.currentWithWeathers?.current.sunrise
            current.sunset = data?.currentWithWeathers?.current.sunset
            current.temp = data?.currentWithWeathers?.current.temp
            current.uvi = data?.currentWithWeathers?.current.uvi
            current.visibility = data?.currentWithWeathers?.current.visibility
            current.weather = data?.currentWithWeathers?.weathers
            current.wind_deg = data?.currentWithWeathers?.current.wind_deg
            current.wind_gust = data?.currentWithWeathers?.current.wind_gust
            current.wind_speed = data?.currentWithWeathers?.current.wind_speed
            current.weatherDataId = data?.currentWithWeathers?.current.weatherDataId
        }

        weatherResponse.id = data?.weatherResponse!!.id
        weatherResponse.lat = data?.weatherResponse?.lat
        weatherResponse.lon = data?.weatherResponse?.lon
        weatherResponse.current = current
        weatherResponse.daily = dailyData
        weatherResponse.timezone_offset = data?.weatherResponse?.timezone_offset
        weatherResponse.timezone = data?.weatherResponse?.timezone

        return weatherResponse

    }

}