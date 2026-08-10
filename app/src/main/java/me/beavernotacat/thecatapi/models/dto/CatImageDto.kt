package me.beavernotacat.thecatapi.models.dto

import kotlinx.serialization.Serializable
import me.beavernotacat.thecatapi.models.local.CatImage

@Serializable
data class CatImageDto(
    val id: String,
    val url: String,
    val breeds: List<CatInfoDto>,
)

fun CatImageDto.toLocal() = CatImage.LoadedCatImage(
    url = url,
)