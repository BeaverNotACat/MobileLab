package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.PatState
import me.beavernotacat.thecatapi.repositories.PatRepository

class FakePatRepository : PatRepository {
    private val states = MutableStateFlow<List<PatState>>(emptyList())

    override suspend fun filterByState(state: CatPatState): List<PatState> {
        return states.value.filter { it.patState == state }
    }

    override fun observeByState(state: CatPatState): Flow<List<PatState>> {
        return states.map { list -> list.filter { it.patState == state } }
    }

    override suspend fun addPatState(id: String, state: CatPatState) {
        val current = states.value.toMutableList()
        current.removeAll { it.id == id }
        current.add(PatState(id = id, patState = state))
        states.value = current
    }
}

