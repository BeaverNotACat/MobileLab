package me.beavernotacat.thecatapi.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.PatState
import me.beavernotacat.thecatapi.models.local.CatPatState

@Dao
interface PatDao {
    @Query("SELECT * FROM PatState WHERE patState = :state ORDER BY updatedAt DESC")
    fun observeByState(state: CatPatState): Flow<List<PatState>>

    @Query("SELECT * FROM PatState WHERE patState = :state ORDER BY updatedAt DESC")
    suspend fun getByState(state: CatPatState): List<PatState>

    @Query("DELETE FROM PatState WHERE id = :id")
    suspend fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cats: PatState)

    @Transaction
    suspend fun replacePatState(cat: PatState) {
        deleteById(cat.id)
        insertAll(cat)
    }
}
