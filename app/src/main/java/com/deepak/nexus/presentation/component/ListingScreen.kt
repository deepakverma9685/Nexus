package com.deepak.nexus.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deepak.nexus.domain.models.User
import com.deepak.nexus.domain.models.dummyUser

@Composable
fun ListingScreen(usersList: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(usersList) { user ->
            ListIngItemScreen(user)
        }
    }
}

@Preview
@Composable
fun ListingScreenPreview() {
    ListingScreen(dummyUser)
}
