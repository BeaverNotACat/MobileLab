package me.beavernotacat.thecatapi

import me.beavernotacat.thecatapi.data.breeds
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.repositories.CatApiRepository

class FakeCatApiRepository: CatApiRepository
{
    var lastFailedBreed = false
    var lastFailedImage = false

    override suspend fun searchBreeds(name: String): List<CatInfo> {
        if (name == "fail" && !lastFailedBreed) {
            lastFailedBreed = true
            error("Failed by request")
        }
        lastFailedBreed = false
        val eligibleBreeds = breeds.filter { it.name.contains(name) }
        return eligibleBreeds
    }

    override suspend fun fetchBreed(id: String): CatInfo {
        val breed = breeds.find { it.id == id }
        if (breed == null) error("No such breed")
        return breed
    }

    override suspend fun fetchImageUrl(id: String): CatImage {
        if (id == "fail" && !lastFailedImage) {
            lastFailedImage = true
            error("Failed by request")
        }
        lastFailedImage = false
        return CatImage.LoadedCatImage("https://http.cat/200")
    }

    override suspend fun getBreeds(ids: List<String>): List<CatInfo> {
        val breed = breeds.filter { ids.contains(it.id) }
        return breed
    }
}