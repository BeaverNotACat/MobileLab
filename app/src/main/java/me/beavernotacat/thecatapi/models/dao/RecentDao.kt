package me.beavernotacat.thecatapi.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.Recent

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent ORDER BY timestamp DESC LIMIT 10")
    fun observeRecent(): Flow<List<Recent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(recent: Recent)

    @Query("DELETE FROM recent WHERE id NOT IN (SELECT id FROM recent ORDER BY timestamp DESC LIMIT 10)")
    suspend fun trimRecent()
}
