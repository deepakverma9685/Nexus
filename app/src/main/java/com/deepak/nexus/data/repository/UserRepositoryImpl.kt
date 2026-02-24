package com.deepak.nexus.data.repository

import com.deepak.nexus.core.coreNetwork.ApiResult
import com.deepak.nexus.core.coreNetwork.Result
import com.deepak.nexus.core.coreNetwork.safeApiCall
import com.deepak.nexus.data.dataSource.remoteDataSource.UsersApiService
import com.deepak.nexus.data.mappers.toDomain
import com.deepak.nexus.domain.models.User
import com.deepak.nexus.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UsersApiService
) : UserRepository {

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        emit(Result.Loading)

        when (val apiResult = safeApiCall { apiService.getUsers() }) {
            is ApiResult.Success -> {
                val users = apiResult.data.map { it.toDomain() }
                emit(Result.Success(users))
            }

            is ApiResult.Error -> {
                val message = apiResult.code?.let { "${apiResult.message} (code: $it)" }
                    ?: apiResult.message
                val exception = apiResult.throwable as? Exception ?: Exception(message)
                emit(Result.Error(message, exception))
            }
        }
    }.flowOn(Dispatchers.IO)
}
