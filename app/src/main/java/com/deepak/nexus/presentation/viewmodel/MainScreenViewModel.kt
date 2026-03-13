package com.deepak.nexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.nexus.domain.usecases.GetUsersUseCase
import com.deepak.nexus.presentation.state.UsersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.deepak.nexus.core.coreNetwork.Result
import com.deepak.nexus.presentation.state.LoginUiState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainScreenViewModel @Inject constructor(val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    private val _state: MutableStateFlow<UsersState> = MutableStateFlow(UsersState.Loading)
    val state: StateFlow<UsersState> = _state

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {

            getUsersUseCase().collect { response ->
                when (response) {
                    is Result.Success -> {
                        _state.value = UsersState.Success(response.data)
                    }

                    is Result.Error -> {
                        _state.value = UsersState.Error(response.message)
                    }

                    is Result.Loading -> {
                        _state.value = UsersState.Loading
                    }
                }
            }
        }
    }
}
