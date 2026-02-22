package com.deepak.nexus.core.coreNetwork

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception) :
        Result<Nothing>()

    object Loading : Result<Nothing>()
}
