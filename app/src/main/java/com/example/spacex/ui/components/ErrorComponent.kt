package com.example.spacex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorComponent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🌃", fontSize = 72.sp, modifier = Modifier.padding(8.dp))
        Text(text = error)
        Button(
            onClick = onRetry, modifier = Modifier.padding(16.dp)
        ) {
            Text("Try again")
        }
    }
}

@Preview
@Composable
fun ErrorComponentPreview() {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            ErrorComponent(error = "This is error message", onRetry = {})
        }
    }
}