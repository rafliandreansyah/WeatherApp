package com.example.weatherapp.utlis

import java.text.SimpleDateFormat
import java.util.*

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

}