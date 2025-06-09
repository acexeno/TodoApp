package com.example.todomanager.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>() {
        override fun toString() = "Success[data=$data]"
    }
    
    data class Error(val exception: Exception) : Result<Nothing>() {
        override fun toString() = "Error[exception=${exception.message}]"
