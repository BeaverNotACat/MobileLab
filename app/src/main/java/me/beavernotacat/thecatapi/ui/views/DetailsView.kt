package me.beavernotacat.thecatapi.ui.views

import androidx.compose.runtime.Composable
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.LoadingDetailsStates
import me.beavernotacat.thecatapi.ui.components.DetailsComponent
import me.beavernotacat.thecatapi.ui.components.ErrorComponent
import me.beavernotacat.thecatapi.ui.components.LoadingComponent

@Composable
fun DetailsView(
    details: LoadingDetailsStates,
    onRetry: () -> Unit,
    images: Map<String, CatImage>,
    favorite: Boolean,
    patState: CatPatState,
    addToFavorites: () -> Unit,
    onUpdatePatState: (CatPatState) -> Unit
) {
    when (details) {
        LoadingDetailsStates.Loading -> LoadingComponent()
        is LoadingDetailsStates.Error -> ErrorComponent(details.message, onRetry)
        is LoadingDetailsStates.Ok -> DetailsComponent(
            details.cat,
            images[details.cat.imageId],
            favorite,
            patState,
            addToFavorites,
            onUpdatePatState
        )
    }
}
