package me.beavernotacat.thecatapi.repositories

import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import javax.inject.Inject
import javax.inject.Singleton

interface CacheRepository {
    suspend fun invalidateExpiredCache()
}

@Singleton
class DefaultCacheRepository @Inject constructor(
    private val catInfoDao: CatInfoDao,
    private val catImageDao: CatImageDao,
    private val settingsRepository: SettingsRepository,
) : CacheRepository {
    override suspend fun invalidateExpiredCache() {
        val now = System.currentTimeMillis()
        val ttlMillis = settingsRepository.getCacheTtl() * 60 * 60 * 1000
        val cutoff = now - ttlMillis
        catInfoDao.deleteExpired(cutoff)
        catImageDao.deleteExpired(cutoff)
    }
}
