package com.example.weatherapp.utlis

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
    fun checkConnection(context: Context): Boolean {
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

}