package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.beavernotacat.thecatapi.models.local.Favorite
import me.beavernotacat.thecatapi.repositories.FavoriteRepository

class FakeFavoriteRepository : FavoriteRepository {
    val favorites = MutableStateFlow<List<Favorite>>(emptyList())

    override suspend fun getFavorites(): List<String> {
        return favorites.value.map { it.id }
    }

    override fun getFavoritesFlow(): Flow<List<Favorite>> {
        return favorites
    }

    override suspend fun toggleFavorite(id: String) {
        val value = getFavorites()
        if (value.contains(id)) {
            favorites.value = value.filter { it != id }.map { Favorite(it) }
        } else {
            favorites.value = value.plus(id).map { Favorite(it) }
        }
    }

}