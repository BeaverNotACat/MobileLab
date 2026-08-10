package me.beavernotacat.thecatapi.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recent(
    @PrimaryKey val id: String,
    val timestamp: Long = System.currentTimeMillis()
)
