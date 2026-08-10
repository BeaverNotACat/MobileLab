package me.beavernotacat.thecatapi.models

import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.models.local.LoadingDetailsStates
import me.beavernotacat.thecatapi.models.local.LoadingSearchStates


data class UiState (
    val searchScreen: LoadingSearchStates = LoadingSearchStates.Loading,
    val search: String = "",
    val detailsScreen: LoadingDetailsStates = LoadingDetailsStates.Loading,
    val favorites: List<String> = emptyList(),
    val favoriteCats: List<CatInfo> = emptyList(),
    val recentCats: List<CatInfo> = emptyList(),
    val pattedCats: List<CatInfo> = emptyList(),
    val toPatCats: List<CatInfo> = emptyList(),
    val toFindCats: List<CatInfo> = emptyList()
)