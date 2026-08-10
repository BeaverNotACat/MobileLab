package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.Favorite

interface FavoriteRepository {
    suspend fun getFavorites(): List<String>

    fun getFavoritesFlow(): Flow<List<Favorite>>

    suspend fun toggleFavorite(id: String)
}