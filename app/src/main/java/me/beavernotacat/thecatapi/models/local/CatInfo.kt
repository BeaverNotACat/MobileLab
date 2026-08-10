package me.beavernotacat.thecatapi.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatInfo(
    @PrimaryKey val id: String,
    val weight: String,
    val name: String,
    val countryCode: String,
    val description: String,
    val lifeSpan: String,
    val indoor: Int,
    val adaptability: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    var imageId: String? = null,
    val catFriendly: Int? = null,
    val cachedAt: Long = 0L,
)
