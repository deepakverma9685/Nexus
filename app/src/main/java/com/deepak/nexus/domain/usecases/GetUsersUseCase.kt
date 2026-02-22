package com.deepak.nexus.domain.usecases

import com.deepak.nexus.core.coreNetwork.Result
import com.deepak.nexus.domain.models.User
import com.deepak.nexus.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(val userRepository: UserRepository) {

     operator fun invoke() : Flow<Result<List<User>>> {
       return userRepository.getUsers()
    }
}