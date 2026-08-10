package me.beavernotacat.thecatapi

import me.beavernotacat.thecatapi.data.breeds
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.repositories.CatApiRepository
import java.io.IOException

class FakeCatApiRepository : CatApiRepository {
    var shouldFail = false
    var hasLocalCache = true
    
    // To distinguish between "failed but returned cache" and "failed completely"
    var returnCacheOnFailure = true

    override suspend fun searchBreeds(name: String): List<CatInfo> {
        if (shouldFail) {
            if (returnCacheOnFailure && hasLocalCache) {
                return breeds.filter { it.name.contains(name, ignoreCase = true) }
            }
            throw IOException("Network error")
        }
        return breeds.filter { it.name.contains(name, ignoreCase = true) }
    }

    override suspend fun fetchBreed(id: String): CatInfo {
        if (shouldFail) {
            if (returnCacheOnFailure && hasLocalCache) {
                return breeds.find { it.id == id } ?: throw IOException("No such breed in cache")
            }
            throw IOException("Network error")
        }
        return breeds.find { it.id == id } ?: throw IOException("No such breed")
    }

    override suspend fun fetchImageUrl(id: String): CatImage {
        if (id == "fail" || (shouldFail && !returnCacheOnFailure)) {
            throw IOException("Network error")
        }
        if (shouldFail && hasLocalCache) {
             return CatImage.LoadedCatImage("https://http.cat/200-cached")
        }
        return CatImage.LoadedCatImage("https://http.cat/200")
    }

    override suspend fun getBreeds(ids: List<String>): List<CatInfo> {
        if (shouldFail && !returnCacheOnFailure) throw IOException("Network error")
        return breeds.filter { ids.contains(it.id) }
    }
}
