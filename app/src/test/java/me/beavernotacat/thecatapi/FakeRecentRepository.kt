package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.beavernotacat.thecatapi.models.local.Recent
import me.beavernotacat.thecatapi.repositories.RecentRepository

class FakeRecentRepository : RecentRepository {
    private val recent = MutableStateFlow<List<Recent>>(emptyList())

    override fun getRecentFlow(): Flow<List<Recent>> {
        return recent
    }

    override suspend fun addRecent(id: String) {
        val current = recent.value.toMutableList()
        current.removeAll { it.id == id }
        current.add(0, Recent(id))
        recent.value = current.take(10)
    }
}
