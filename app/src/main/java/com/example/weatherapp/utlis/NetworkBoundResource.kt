package com.example.weatherapp.utlis

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = {true}
) = flow {

    val data = query().first()

    val flow = if (shouldFetch(data)){
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        }catch (throwable: Throwable){
            query().map { throwable.message?.let { it1 -> Resource.Error(it1, it) } }
        }

    }
    else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)


}