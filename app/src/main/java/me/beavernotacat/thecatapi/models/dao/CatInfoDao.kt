package me.beavernotacat.thecatapi.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.beavernotacat.thecatapi.models.local.CatInfo

@Dao
interface CatInfoDao {
    @Query("SELECT * FROM catinfo WHERE name LIKE '%' || :name || '%'")
    suspend fun searchBreeds(name: String): List<CatInfo>

    @Query("SELECT * FROM catinfo WHERE id = :id")
    suspend fun getBreed(id: String): CatInfo?

    @Query("SELECT * FROM catinfo WHERE id IN (:ids)")
    suspend fun getBreeds(ids: List<String>): List<CatInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cats: CatInfo)

    @Query("DELETE FROM catinfo WHERE cachedAt <= 0 OR cachedAt < :cutoff")
    suspend fun deleteExpired(cutoff: Long): Int
}
