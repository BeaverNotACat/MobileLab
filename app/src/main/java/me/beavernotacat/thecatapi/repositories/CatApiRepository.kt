package me.beavernotacat.thecatapi.repositories

import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import me.beavernotacat.thecatapi.models.dto.toLocal
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatImageEntity
import me.beavernotacat.thecatapi.models.local.CatInfo
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


interface CatApiRepository {
    suspend fun searchBreeds(name: String): List<CatInfo>
    suspend fun fetchBreed(id: String): CatInfo
    suspend fun fetchImageUrl(id: String): CatImage
    suspend fun getBreeds(ids: List<String>): List<CatInfo>
}

@Singleton
class DefaultCatApiRepository @Inject constructor(
    private val catApiService: CatApiService,
    private val catInfoDao: CatInfoDao,
    private val catImageDao: CatImageDao,
    private val settingsRepository: SettingsRepository,
) : CatApiRepository {

    override suspend fun searchBreeds(name: String): List<CatInfo> {
        val now = System.currentTimeMillis()
        val localBreeds = catInfoDao.searchBreeds(name)
        if (localBreeds.isNotEmpty() && localBreeds.all() { it.cachedAt + settingsRepository.getCacheTtl() >= now }) {
            return localBreeds
        }

        try {
            val remoteBreeds =
                catApiService.searchBreeds(name).map { it.toLocal().copy(cachedAt = now) }
            catInfoDao.insertAll(*remoteBreeds.toTypedArray())
            return remoteBreeds
        }
        catch (ioe: IOException){
            if (localBreeds.isNotEmpty()) return localBreeds
            throw ioe
        }
    }

    override suspend fun fetchBreed(id: String): CatInfo {
        val now = System.currentTimeMillis()
        val localBreed = catInfoDao.getBreed(id)
        if (localBreed != null && localBreed.cachedAt + settingsRepository.getCacheTtl() > now) {
            return localBreed
        }
        try {
            val remoteBreed = catApiService.fetchBreed(id).toLocal().copy(cachedAt = now)
            catInfoDao.insertAll(remoteBreed)
            return remoteBreed
        }
        catch (ioe: IOException) {
            if (localBreed != null) return localBreed
            throw ioe
        }
    }

    override suspend fun fetchImageUrl(id: String): CatImage {
        val now = System.currentTimeMillis()
        val localImage = catImageDao.getImage(id)
        if (localImage != null && localImage.cachedAt + settingsRepository.getCacheTtl() > now) {
            return CatImage.LoadedCatImage(localImage.url)
        }
        try {
            val remoteImage = catApiService.fetchImageUrl(id).toLocal()
            catImageDao.insert(CatImageEntity(id, remoteImage.url, cachedAt = now))
            return remoteImage
        }
        catch (ioe: IOException) {
           if (localImage != null) return CatImage.LoadedCatImage(localImage.url)
            throw ioe
        }
    }

    override suspend fun getBreeds(ids: List<String>): List<CatInfo> {
        val now = System.currentTimeMillis()
        val localBreeds = catInfoDao.getBreeds(ids)
        if (ids.size == localBreeds.size && localBreeds.all { it.cachedAt + settingsRepository.getCacheTtl() >= now }) {
            return localBreeds.sortedBy { ids.indexOf(it.id) }
        }

        try {
            val remoteBreeds = ids.map{id ->  catApiService.fetchBreed(id).toLocal().copy(cachedAt = now)}
            catInfoDao.insertAll(*remoteBreeds.toTypedArray())
            return remoteBreeds.sortedBy { ids.indexOf(it.id) }
        }
        catch (ioe: IOException) {
            if (ids.size == localBreeds.size) return localBreeds.sortedBy { ids.indexOf(it.id) }
            throw ioe
        }
    }
}
