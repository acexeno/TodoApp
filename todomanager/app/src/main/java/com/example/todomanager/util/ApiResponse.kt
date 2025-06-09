package com.example.todomanager.util

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.io.IOException

data class ApiError(
    @SerializedName("message") val message: String?,
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("error") val error: String?
) {
    constructor() : this(null, null, null)
}

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>() {
        val isSuccess: Boolean = true
    }
    
    data class Error(
        val exception: Throwable,
        val code: Int? = null,
        val errorMessage: String? = null
    ) : ApiResult<Nothing>() {
        constructor(throwable: Throwable) : this(
            exception = throwable,
            code = null,
            errorMessage = throwable.message
        )
    }
    
    val isSuccess: Boolean get() = this is Success
    
    companion object {
        fun <T> success(data: T): ApiResult<T> = Success(data)
        
        fun <T> error(exception: Throwable): ApiResult<T> = Error(exception)
        
        fun <T> error(code: Int, errorMessage: String?): ApiResult<T> =
            Error(Exception(errorMessage), code, errorMessage)
    }
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.error("Empty response body")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                // You can parse errorBody to your ApiError class if needed
                errorBody ?: "Unknown error"
            } catch (e: Exception) {
                "Error parsing error response"
            }
            ApiResult.error(response.code(), errorMessage)
        }
    } catch (e: Exception) {
        when (e) {
            is IOException -> ApiResult.error("No internet connection")
            else -> ApiResult.error(e)
        }
    }
}

private fun <T> ApiResult.error(message: String): ApiResult<T> {
    return ApiResult.Error(
        exception = Exception(message),
        errorMessage = message
    )
}
