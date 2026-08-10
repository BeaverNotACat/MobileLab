package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.PatState

interface PatRepository {
    suspend fun filterByState(state: CatPatState): List<PatState>
    fun observeByState(state: CatPatState): Flow<List<PatState>>
    suspend fun addPatState(id: String, state: CatPatState)
}
