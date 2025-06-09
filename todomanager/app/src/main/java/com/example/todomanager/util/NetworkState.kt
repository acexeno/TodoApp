package com.example.todomanager.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A helper class that provides updates about the network state of the device.
 */
class NetworkState private constructor() {
    private val _networkStatus = MutableStateFlow<Status>(Status.AVAILABLE)
    val networkStatus: StateFlow<Status> = _networkStatus

    private val _connectionStatus = MutableLiveData<Status>()
    val connectionStatus: LiveData<Status> = _connectionStatus

    fun updateNetworkStatus(status: Status) {
        _networkStatus.value = status
        _connectionStatus.postValue(status)
    }

    enum class Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST
    }

    companion object {
        @Volatile
        private var INSTANCE: NetworkState? = null

        fun getInstance(): NetworkState {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkState().also { INSTANCE = it }
            }
        }
    }
}

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with message and statusCode
 */
sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>() {
        val isSuccess: Boolean = true
    }

    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val throwable: Throwable? = null
    ) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>() {
        val isLoading: Boolean = true
    }

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isError: Boolean get() = this is Error

    companion object {
        fun <T : Any> success(data: T): NetworkResult<T> = Success(data)
        fun error(code: Int? = null, message: String? = null, throwable: Throwable? = null): NetworkResult<Nothing> =
            Error(code, message, throwable)
        fun loading(): NetworkResult<Nothing> = Loading
    }
}
