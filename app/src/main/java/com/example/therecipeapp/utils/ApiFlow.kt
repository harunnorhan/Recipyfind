package com.example.therecipeapp.utils

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

val gson = Gson()

fun <T> apiFlow(
    call: suspend () -> Response<T>?
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)
    try {
        val response = call()
        response?.let {
            if (it.isSuccessful) {
                emit(ApiResult.Success(it.body()))
            } else {
                val errorBody = it.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                val errorMessage = errorResponse?.error ?: "An unknown error occurred"
                emit(ApiResult.Error(errorMessage))
            }
        } ?: run {
            emit(ApiResult.Error("Received null response from API"))
        }
    } catch (e: Exception) {
        val errorMessage = e.message ?: "An unexpected error occurred"
        emit(ApiResult.Error(errorMessage, e))
    }
}.flowOn(Dispatchers.IO)

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T?) : ApiResult<T>()
    data object Loading : ApiResult<Nothing>()
    data class Error(val message: String?, val cause: Throwable? = null) : ApiResult<Nothing>()
}

data class ApiErrorResponse(
    val error: String
)