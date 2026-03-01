package com.example.spacex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun RemoteImage(
    url: String,
    modifier: Modifier
) {
    SubcomposeAsyncImage(
        url,
        contentDescription = null,
        modifier,
        loading = {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            )
        },
        error = {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
            )
        })

}


@Preview
@Composable
fun RemoteImagePreview() {
    RemoteImage(
        url = "http://placehold.co/400", modifier = Modifier
            .width(400.dp)
            .height(400.dp)
    )
}