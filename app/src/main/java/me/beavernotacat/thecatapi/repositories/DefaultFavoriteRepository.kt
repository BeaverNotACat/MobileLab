package me.beavernotacat.thecatapi.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.dao.FavoriteDao
import me.beavernotacat.thecatapi.models.local.Favorite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
): FavoriteRepository {
    override suspend fun getFavorites(): List<String> {
        Log.i("FavoriteRepository", "Fetching all favorites")
        return favoriteDao.getAll().map { it.id }
    }

    override fun getFavoritesFlow(): Flow<List<Favorite>> {
        return favoriteDao.observeAll()
    }

    override suspend fun toggleFavorite(id: String) {
        val favorites = getFavorites()
        if (favorites.contains(id)) {
            favoriteDao.delete(Favorite(id))
            Log.i("FavoriteRepository", "Removed $id")
        } else {
            favoriteDao.insertAll(Favorite(id))
            Log.i("FavoriteRepository", "Added $id")
        }
    }
}