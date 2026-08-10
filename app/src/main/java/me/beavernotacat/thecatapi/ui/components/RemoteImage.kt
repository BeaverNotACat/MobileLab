package me.beavernotacat.thecatapi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import me.beavernotacat.thecatapi.R
import me.beavernotacat.thecatapi.models.local.CatImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RemoteCatImage(
    image: CatImage?,
    modifier: Modifier
) {
    when (image) {
        is CatImage.UnloadedCatImage -> LoadingIndicator()
        is CatImage.LoadedCatImage -> {
            SubcomposeAsyncImage(
                image.url,
                contentDescription = null,
                modifier,
                loading = {
                    LoadingIndicator()
                },
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.error),
                        contentDescription = "Error"
                    )
                })
        }
        CatImage.LoadingCatImage -> LoadingIndicator()
        else -> {
            var reason = "No image"
            if (image != null && image is CatImage.FailedCatImage) {
                reason = image.reason
            }
            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = reason,
                modifier = Modifier.width(64.dp)
            )
        }

    }

}