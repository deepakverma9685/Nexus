package com.deepak.nexus.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.deepak.nexus.presentation.component.ListingScreen
import com.deepak.nexus.presentation.component.ProgressScreen
import com.deepak.nexus.presentation.state.UsersState
import com.deepak.nexus.presentation.viewmodel.MainScreenViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainScreenViewModel: MainScreenViewModel
) {
    val state by mainScreenViewModel.state.collectAsState()

    when (val data = state) {
        UsersState.Loading -> {
            ProgressScreen()
        }

        is UsersState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                ListingScreen(usersList = data.users)
            }
        }

        is UsersState.Error -> {
            Log.e("TAG", "MainScreen: ${data.message}")
        }
    }
}
