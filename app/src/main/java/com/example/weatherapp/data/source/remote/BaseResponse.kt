package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.source.remote.response.WeatherCityResponse
import com.example.weatherapp.utlis.Resource
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

abstract class BaseResponse {

    suspend fun <T> safeCallApi(apiCall: () -> Response<T>): Resource<T>{
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful){
                val body= response.body()
                body?.let {
                    return Resource.Success(body)
                }

            }
            return errorMessage("${response.code()}: ${response.message()}")
        }catch (e: Exception){
            return errorMessage("${e.message} ?: ${e.toString()}")
        }
    }

    private fun <T> errorMessage(message: String): Resource<T> =
        Resource.Error("Api call error $message")

}