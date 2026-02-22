package com.deepak.nexus.domain.repository

import com.deepak.nexus.core.coreNetwork.Result
import com.deepak.nexus.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers() : Flow<Result<List<User>>>
}