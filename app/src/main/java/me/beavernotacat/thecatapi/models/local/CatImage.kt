package me.beavernotacat.thecatapi.models.local

sealed class CatImage {
    data class UnloadedCatImage(val id: String): CatImage()
    data object LoadingCatImage: CatImage()
    data class LoadedCatImage(val url: String): CatImage()
    data class FailedCatImage(val reason: String): CatImage()
}