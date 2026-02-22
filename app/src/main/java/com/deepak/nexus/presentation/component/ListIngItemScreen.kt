package com.deepak.nexus.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deepak.nexus.domain.models.User
import com.deepak.nexus.domain.models.dummyUser

@Composable
fun ListIngItemScreen(user: User) {
    Column(
        modifier = Modifier.fillMaxWidth().border(
            border = BorderStroke(width = 1.dp, color = Color.LightGray),
            shape = RoundedCornerShape(8.dp)
        ).padding(16.dp)
    ) {
        with(user) {
            Text(text = name, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = email)
        }
    }
}


@Preview
@Composable
fun ListIngItemScreenPreview() {
    ListIngItemScreen(user = dummyUser[0])
}