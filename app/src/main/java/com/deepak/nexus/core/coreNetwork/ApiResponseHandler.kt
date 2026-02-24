package com.deepak.nexus.core.coreNetwork

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body, response.code())
            } else {
                ApiResult.Error(
                    code = response.code(),
                    message = "Empty response from server."
                )
            }
        } else {
            ApiResult.Error(
                code = response.code(),
                message = mapHttpStatusToMessage(response.code(), response.message())
            )
        }
    } catch (e: UnknownHostException) {
        ApiResult.Error(
            code = null,
            message = "No internet connection. Please check your network.",
            throwable = e
        )
    } catch (e: SocketTimeoutException) {
        ApiResult.Error(
            code = null,
            message = "Request timed out. Please try again.",
            throwable = e
        )
    } catch (e: IOException) {
        ApiResult.Error(
            code = null,
            message = "Network error. Please try again.",
            throwable = e
        )
    } catch (e: Exception) {
        ApiResult.Error(
            code = null,
            message = e.message ?: "Unexpected error occurred.",
            throwable = e
        )
    }
}

private fun mapHttpStatusToMessage(code: Int, fallbackMessage: String): String {
    return when (code) {
        400 -> "Bad request."
        401 -> "Unauthorized. Please login again."
        403 -> "You do not have permission to perform this action."
        404 -> "Requested resource not found."
        408 -> "Request timeout. Please try again."
        429 -> "Too many requests. Please try again later."
        500 -> "Server error. Please try again later."
        502 -> "Bad gateway from server."
        503 -> "Service unavailable. Please try again later."
        504 -> "Gateway timeout. Please try again."
        else -> if (fallbackMessage.isNotBlank()) fallbackMessage else "Something went wrong."
    }
}
