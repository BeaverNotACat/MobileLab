package me.beavernotacat.thecatapi.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun observeAll(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite")
    suspend fun getAll(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg cats: Favorite)

    @Delete
    suspend fun delete(cats: Favorite)
}

