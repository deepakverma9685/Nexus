package com.deepak.nexus.data.dataSource.remoteDataSource

import com.deepak.nexus.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface UsersApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>
}
