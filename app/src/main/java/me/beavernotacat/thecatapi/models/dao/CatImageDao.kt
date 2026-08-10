package me.beavernotacat.thecatapi.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.beavernotacat.thecatapi.models.local.CatImageEntity

@Dao
interface CatImageDao {
    @Query("SELECT * FROM catimageentity WHERE id = :id")
    suspend fun getImage(id: String): CatImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catImage: CatImageEntity)

    @Query("DELETE FROM catimageentity WHERE cachedAt <= 0 OR cachedAt < :cutoff")
    suspend fun deleteExpired(cutoff: Long): Int
}
