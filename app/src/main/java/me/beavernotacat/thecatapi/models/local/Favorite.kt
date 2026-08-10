package me.beavernotacat.thecatapi.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey val id: String,
)
