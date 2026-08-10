package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.dao.PatDao
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.PatState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPatRepository @Inject constructor(
    private val patDao: PatDao
) : PatRepository {
    override fun observeByState(state: CatPatState): Flow<List<PatState>> {
        return patDao.observeByState(state)
    }

    override suspend fun filterByState(state: CatPatState): List<PatState> {
        return patDao.getByState(state)
    }

    override suspend fun addPatState(id: String, state: CatPatState) {
        patDao.replacePatState(PatState(id, state, System.currentTimeMillis()))
    }
}
