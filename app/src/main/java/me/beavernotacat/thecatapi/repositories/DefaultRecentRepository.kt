package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.flow.Flow
import me.beavernotacat.thecatapi.models.dao.RecentDao
import me.beavernotacat.thecatapi.models.local.Recent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecentRepository @Inject constructor(
    private val recentDao: RecentDao
): RecentRepository {
    override fun getRecentFlow(): Flow<List<Recent>> {
        return recentDao.observeRecent()
    }

    override suspend fun addRecent(id: String) {
        recentDao.insertRecent(Recent(id))
        recentDao.trimRecent()
    }
}
