package me.beavernotacat.thecatapi.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CatPatState {
    NotFound,
    ToFind,
    ToPat,
    Patted
}

@Entity
class PatState (
    @PrimaryKey val id: String,
    val patState: CatPatState,
    val updatedAt: Long = System.currentTimeMillis()
)