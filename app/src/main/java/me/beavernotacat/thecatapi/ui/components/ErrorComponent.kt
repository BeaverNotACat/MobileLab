package me.beavernotacat.thecatapi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.beavernotacat.thecatapi.R


@Composable
fun ErrorComponent(
    message: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = "Error"
        )

        if (message != null) Text(message, modifier = Modifier.testTag("error message"))

        Button(
            onClick = onRetry, modifier = Modifier.padding(16.dp).testTag("error retry")
        ) {
            Text("Try again")
        }
    }
}