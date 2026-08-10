package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.local.Recent

interface RecentRepository {
    fun getRecentFlow(): Flow<List<Recent>>
    suspend fun addRecent(id: String)
}
