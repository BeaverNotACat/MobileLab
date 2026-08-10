package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.beavernotacat.thecatapi.models.local.Recent
import me.beavernotacat.thecatapi.repositories.RecentRepository

class FakeRecentRepository : RecentRepository {
    val recents = MutableStateFlow<List<Recent>>(emptyList())
    override fun getRecentFlow(): Flow<List<Recent>> {
        return recents
    }

    override suspend fun addRecent(id: String) {
        val value = recents.value
        recents.value = value.plus(Recent(id))
    }
}