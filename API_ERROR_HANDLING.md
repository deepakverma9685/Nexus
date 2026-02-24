# API Error Handling Guide

This project now uses a centralized API response wrapper to convert Retrofit responses and exceptions into UI-friendly messages.

## Purpose

- Handle HTTP status codes in one place.
- Return consistent success/error states to repositories and UI.
- Avoid repeating `try/catch` and message mapping in every repository.

## Files Added/Updated

1. Added: `app/src/main/java/com/deepak/nexus/core/coreNetwork/ApiResult.kt`
2. Added: `app/src/main/java/com/deepak/nexus/core/coreNetwork/ApiResponseHandler.kt`
3. Updated: `app/src/main/java/com/deepak/nexus/data/dataSource/remoteDataSource/UsersApiService.kt`
4. Updated: `app/src/main/java/com/deepak/nexus/data/repository/UserRepositoryImpl.kt`

## Core Wrapper

### `ApiResult`

`ApiResult` is a sealed class:

- `ApiResult.Success<T>(data, code)`
- `ApiResult.Error(code, message, throwable)`

This gives each API call structured success/error output including HTTP code when available.

### `safeApiCall`

`safeApiCall { apiService.call() }`:

- checks `response.isSuccessful`
- returns success with response body + code
- maps HTTP error codes to readable messages
- catches network/runtime exceptions and returns meaningful messages

Handled exceptions include:

- `UnknownHostException`
- `SocketTimeoutException`
- `IOException`
- generic `Exception`

## HTTP Message Mapping

Current mapping in `ApiResponseHandler.kt`:

- `400` -> Bad request
- `401` -> Unauthorized
- `403` -> Forbidden
- `404` -> Not found
- `408` -> Request timeout
- `429` -> Too many requests
- `500` -> Server error
- `502` -> Bad gateway
- `503` -> Service unavailable
- `504` -> Gateway timeout

Fallback: Retrofit response message or generic "Something went wrong."

## Repository Flow

Current flow in `UserRepositoryImpl`:

1. Emit `Result.Loading`
2. Execute API through `safeApiCall`
3. On success: map DTO -> domain and emit `Result.Success`
4. On error: emit `Result.Error` with friendly message (includes status code when present)

## API Interface Rule

To access status code and body safely, Retrofit service methods should return `Response<T>`.

Example:

```kotlin
@GET("users")
suspend fun getUsers(): Response<List<UserDto>>
```

## Reuse Pattern For New Endpoints

For any new API call:

1. Make service return `Response<T>`
2. Call it inside repository using `safeApiCall`
3. Map `ApiResult.Success` -> domain -> `Result.Success`
4. Map `ApiResult.Error` -> `Result.Error` with UI-friendly message

## Optional Future Improvements

- Add error codes/messages from server error body parsing.
- Add localization for user-facing messages.
- Move API/domain error models to dedicated contracts for larger apps.
