package com.deepak.nexus.data.repository

import com.deepak.nexus.core.coreNetwork.Result
import com.deepak.nexus.data.dataSource.remoteDataSource.UsersApiService
import com.deepak.nexus.data.mappers.toDomain
import com.deepak.nexus.domain.models.User
import com.deepak.nexus.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val apiService: UsersApiService) : UserRepository {

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        try {
            emit(Result.Loading)
            val usersDto = apiService.getUsers()
            val users = usersDto.map { it.toDomain() } // Assuming UserDto has toUser() extension
            emit(Result.Success(users))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error", e))
        }
    }.flowOn(Dispatchers.IO)
}