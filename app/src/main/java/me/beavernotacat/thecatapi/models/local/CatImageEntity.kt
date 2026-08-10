package me.beavernotacat.thecatapi.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatImageEntity(
    @PrimaryKey val id: String,
    val url: String,
    val cachedAt: Long = 0L,
)
