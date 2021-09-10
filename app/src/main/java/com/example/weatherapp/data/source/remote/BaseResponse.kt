package com.example.weatherapp.data.source.remote

import com.example.weatherapp.utlis.Resource
import retrofit2.Response

abstract class BaseResponse {

    suspend fun <T> safeCallApi(apiCall: suspend () -> Response<T>): Resource<T>{
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful){
                val body = response.body()
                body?.let {
                    return Resource.Success(it)
                }

            }
            return errorMessage("${response.code()}: ${response.message()}")
        }catch (e: Exception){
            return Resource.Error("${e.message}")
        }
    }

    private fun <T> errorMessage(message: String): Resource<T> =
        Resource.Error("Api call error $message")

}