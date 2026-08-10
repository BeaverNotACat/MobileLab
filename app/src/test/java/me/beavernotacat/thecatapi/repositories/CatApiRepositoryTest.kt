package me.beavernotacat.thecatapi.repositories

import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.data.abys
import me.beavernotacat.thecatapi.data.abysDto
import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import me.beavernotacat.thecatapi.models.dto.CatImageDto
import me.beavernotacat.thecatapi.models.dto.CatInfoDto
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatImageEntity
import me.beavernotacat.thecatapi.models.local.CatInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CatApiRepositoryTest {

    private lateinit var repository: DefaultCatApiRepository
    private lateinit var fakeApiService: FakeCatApiService
    private lateinit var fakeCatInfoDao: FakeCatInfoDao
    private lateinit var fakeCatImageDao: FakeCatImageDao
    private lateinit var fakeSettingsRepository: FakeSettingsRepository

    @Before
    fun setup() {
        fakeApiService = FakeCatApiService()
        fakeCatInfoDao = FakeCatInfoDao()
        fakeCatImageDao = FakeCatImageDao()
        fakeSettingsRepository = FakeSettingsRepository()
        repository = DefaultCatApiRepository(
            fakeApiService,
            fakeCatInfoDao,
            fakeCatImageDao,
            fakeSettingsRepository
        )
    }

    @Test
    fun `searchBreeds returns remote data and caches it`() = runTest {
        val query = "Abys"
        fakeApiService.searchBreedsResult = listOf(abysDto)

        val result = repository.searchBreeds(query)

        assertEquals(1, result.size)
        assertEquals("abys", result[0].id)
        assertEquals(1, fakeCatInfoDao.insertedCats.size)
        assertEquals("abys", fakeCatInfoDao.insertedCats[0].id)
    }

    @Test
    fun `searchBreeds returns local data when remote fails`() = runTest {
        val query = "Abys"
        fakeApiService.shouldFail = true
        fakeCatInfoDao.searchBreedsResult = listOf(abys.copy(cachedAt = System.currentTimeMillis()))

        val result = repository.searchBreeds(query)

        assertEquals(1, result.size)
        assertEquals("Abyssinian", result[0].name)
    }

    @Test
    fun `fetchBreed returns remote data and caches it`() = runTest {
        val id = "abys"
        fakeApiService.fetchBreedResult = abysDto

        val result = repository.fetchBreed(id)

        assertEquals("abys", result.id)
        assertEquals(1, fakeCatInfoDao.insertedCats.size)
        assertEquals("abys", fakeCatInfoDao.insertedCats[0].id)
    }

    @Test
    fun `fetchBreed returns local data when remote fails`() = runTest {
        val id = "abys"
        fakeApiService.shouldFail = true
        fakeCatInfoDao.getBreedResult = abys.copy(cachedAt = System.currentTimeMillis())

        val result = repository.fetchBreed(id)

        assertEquals("Abyssinian", result.name)
    }

    @Test
    fun `fetchImageUrl returns remote data and caches it`() = runTest {
        val id = "img1"
        val url = "https://example.com/cat.jpg"
        fakeApiService.fetchImageUrlResult = CatImageDto(id, url, emptyList())

        val result = repository.fetchImageUrl(id)

        assertEquals(CatImage.LoadedCatImage(url), result)
        assertEquals(1, fakeCatImageDao.insertedImages.size)
        assertEquals(url, fakeCatImageDao.insertedImages[0].url)
    }

    @Test
    fun `fetchImageUrl returns local data when remote fails`() = runTest {
        val id = "img1"
        val url = "https://example.com/local.jpg"
        fakeApiService.shouldFail = true
        fakeCatImageDao.getImageResult = CatImageEntity(id, url, System.currentTimeMillis())

        val result = repository.fetchImageUrl(id)

        assertEquals(CatImage.LoadedCatImage(url), result)
    }

    @Test
    fun `searchBreeds returns fresh local data without calling remote`() = runTest {
        val query = "Abys"
        fakeCatInfoDao.searchBreedsResult = listOf(abys.copy(cachedAt = System.currentTimeMillis()))

        val result = repository.searchBreeds(query)

        assertEquals(1, result.size)
        assertEquals(0, fakeApiService.searchBreedsCallCount)
    }

    @Test
    fun `fetchBreed attempts remote call when cache is stale`() = runTest {
        val id = "abys"
        // Stale cache
        fakeCatInfoDao.getBreedResult = abys.copy(cachedAt = 0L)
        fakeApiService.fetchBreedResult = abysDto

        val result = repository.fetchBreed(id)

        assertEquals(1, fakeApiService.fetchBreedCallCount)
        assertNotEquals(0L, result.cachedAt)
    }

    @Test
    fun `fetchBreed falls back to stale cache on network failure`() = runTest {
        val id = "abys"
        val staleTime = 123L
        fakeCatInfoDao.getBreedResult = abys.copy(cachedAt = staleTime)
        fakeApiService.shouldFail = true

        val result = repository.fetchBreed(id)

        assertEquals(1, fakeApiService.fetchBreedCallCount)
        assertEquals(staleTime, result.cachedAt)
        assertEquals("Abyssinian", result.name)
    }

    @Test
    fun `searchBreeds attempts remote call when cache is stale`() = runTest {
        val query = "Abys"
        fakeCatInfoDao.searchBreedsResult = listOf(abys.copy(cachedAt = 0L))
        fakeApiService.searchBreedsResult = listOf(abysDto)

        repository.searchBreeds(query)

        assertEquals(1, fakeApiService.searchBreedsCallCount)
    }

    @Test
    fun `searchBreeds falls back to stale cache on network failure`() = runTest {
        val query = "Abys"
        fakeCatInfoDao.searchBreedsResult = listOf(abys.copy(cachedAt = 0L))
        fakeApiService.shouldFail = true

        val result = repository.searchBreeds(query)

        assertEquals(1, fakeApiService.searchBreedsCallCount)
        assertEquals(1, result.size)
    }

    // Fakes
    class FakeCatApiService : CatApiService {
        var shouldFail = false
        var searchBreedsResult: List<CatInfoDto> = emptyList()
        var fetchBreedResult: CatInfoDto? = null
        var fetchImageUrlResult: CatImageDto? = null
        var fetchBreedCallCount = 0
        var searchBreedsCallCount = 0

        override suspend fun searchBreeds(name: String): List<CatInfoDto> {
            searchBreedsCallCount++
            if (shouldFail) throw IOException("Network error")
            return searchBreedsResult
        }

        override suspend fun fetchBreed(id: String): CatInfoDto {
            fetchBreedCallCount++
            if (shouldFail) throw IOException("Network error")
            return fetchBreedResult ?: throw IOException("Not found")
        }

        override suspend fun fetchImageUrl(id: String): CatImageDto {
            if (shouldFail) throw IOException("Network error")
            return fetchImageUrlResult ?: throw IOException("Not found")
        }
    }

    class FakeCatInfoDao : CatInfoDao {
        val insertedCats = mutableListOf<CatInfo>()
        var searchBreedsResult: List<CatInfo> = emptyList()
        var getBreedResult: CatInfo? = null

        override suspend fun searchBreeds(name: String): List<CatInfo> = searchBreedsResult

        override suspend fun getBreed(id: String): CatInfo? = getBreedResult

        override suspend fun getBreeds(ids: List<String>): List<CatInfo> {
            return insertedCats.filter { it.id in ids }
        }

        override suspend fun insertAll(vararg cats: CatInfo) {
            insertedCats.addAll(cats)
        }

        override suspend fun deleteExpired(cutoff: Long): Int = 0
    }

    class FakeCatImageDao : CatImageDao {
        val insertedImages = mutableListOf<CatImageEntity>()
        var getImageResult: CatImageEntity? = null

        override suspend fun getImage(id: String): CatImageEntity? = getImageResult

        override suspend fun insert(catImage: CatImageEntity) {
            insertedImages.add(catImage)
        }

        override suspend fun deleteExpired(cutoff: Long): Int = 0
    }

    class FakeSettingsRepository : SettingsRepository {
        var ttlMinutes: Long = SettingsDefaults.DEFAULT_CACHE_TTL
        override val cacheTtlFlow = kotlinx.coroutines.flow.flowOf(ttlMinutes)

        override suspend fun getCacheTtl(): Long = ttlMinutes

        override suspend fun setCacheTtl(minutes: Long) {
            ttlMinutes = minutes
        }
    }
}
