package com.deepak.nexus.presentation.state

import com.deepak.nexus.domain.models.User

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String) : UsersState()
}