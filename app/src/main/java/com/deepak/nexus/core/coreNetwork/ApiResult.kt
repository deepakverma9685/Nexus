package com.deepak.nexus.core.coreNetwork

sealed class ApiResult<out T> {
    data class Success<T>(val data: T, val code: Int) : ApiResult<T>()

    data class Error(
        val code: Int?,
        val message: String,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>()
}
