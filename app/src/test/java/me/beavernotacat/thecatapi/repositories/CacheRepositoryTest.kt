package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.FakeSettingsRepository
import me.beavernotacat.thecatapi.data.abys
import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import me.beavernotacat.thecatapi.models.local.CatImageEntity
import me.beavernotacat.thecatapi.models.local.CatInfo
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheRepositoryTest {
    private lateinit var catInfoDao: FakeCatInfoDao
    private lateinit var catImageDao: FakeCatImageDao
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var repository: DefaultCacheRepository

    @Before
    fun setup() {
        catInfoDao = FakeCatInfoDao()
        catImageDao = FakeCatImageDao()
        settingsRepository = FakeSettingsRepository()
        runBlocking { settingsRepository.setCacheTtl(60) }
        repository = DefaultCacheRepository(catInfoDao, catImageDao, settingsRepository)
    }

    @Test
    fun `invalidateExpiredCache deletes stale breed and image rows`() = runTest {
        val staleTime = System.currentTimeMillis() - 2 * 60 * 60 * 1000
        catInfoDao.cats.add(abys.copy(cachedAt = staleTime))
        catImageDao.images.add(CatImageEntity("img1", "https://example.com/cat.jpg", cachedAt = staleTime))

        repository.invalidateExpiredCache()

        assertEquals(0, catInfoDao.cats.size)
        assertEquals(0, catImageDao.images.size)
    }

    @Test
    fun `invalidateExpiredCache keeps fresh rows`() = runTest {
        val freshTime = System.currentTimeMillis()
        catInfoDao.cats.add(abys.copy(cachedAt = freshTime))

        repository.invalidateExpiredCache()

        assertEquals(1, catInfoDao.cats.size)
    }

    @Test
    fun `invalidateExpiredCache uses cutoff from settings ttl`() = runTest {
        settingsRepository.setCacheTtl(30)
        val before = System.currentTimeMillis()

        repository.invalidateExpiredCache()

        val expectedCutoff = before - 30 * 60_000
        assertTrue(abs(catInfoDao.lastCutoff - expectedCutoff) < 5_000)
    }

    private class FakeCatInfoDao : CatInfoDao {
        val cats = mutableListOf<CatInfo>()
        var lastCutoff: Long = 0L

        override suspend fun searchBreeds(name: String): List<CatInfo> = cats

        override suspend fun getBreed(id: String): CatInfo? = cats.find { it.id == id }

        override suspend fun getBreeds(ids: List<String>): List<CatInfo> =
            cats.filter { it.id in ids }

        override suspend fun insertAll(vararg cats: CatInfo) {
            this.cats.addAll(cats)
        }

        override suspend fun deleteExpired(cutoff: Long): Int {
            lastCutoff = cutoff
            val removed = cats.count { it.cachedAt <= 0 || it.cachedAt < cutoff }
            cats.removeAll { it.cachedAt <= 0 || it.cachedAt < cutoff }
            return removed
        }
    }

    private class FakeCatImageDao : CatImageDao {
        val images = mutableListOf<CatImageEntity>()

        override suspend fun getImage(id: String): CatImageEntity? = images.find { it.id == id }

        override suspend fun insert(catImage: CatImageEntity) {
            images.add(catImage)
        }

        override suspend fun deleteExpired(cutoff: Long): Int {
            val removed = images.count { it.cachedAt <= 0 || it.cachedAt < cutoff }
            images.removeAll { it.cachedAt <= 0 || it.cachedAt < cutoff }
            return removed
        }
    }

}
