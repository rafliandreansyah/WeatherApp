package com.example.weatherapp.utlis


import java.text.SimpleDateFormat

object Helper {

    fun convertDate(timeMilis: Long): String{
        val format = SimpleDateFormat("ddd")
        return format.format(timeMilis)
    }

}