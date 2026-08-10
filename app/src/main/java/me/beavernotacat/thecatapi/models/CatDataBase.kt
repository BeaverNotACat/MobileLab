package me.beavernotacat.thecatapi.models

import androidx.room.Database
import androidx.room.RoomDatabase
import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import me.beavernotacat.thecatapi.models.dao.FavoriteDao
import me.beavernotacat.thecatapi.models.dao.PatDao
import me.beavernotacat.thecatapi.models.dao.RecentDao
import me.beavernotacat.thecatapi.models.local.CatImageEntity
import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.models.local.Favorite
import me.beavernotacat.thecatapi.models.local.PatState
import me.beavernotacat.thecatapi.models.local.Recent

@Database(entities = [Favorite::class, CatInfo::class, CatImageEntity::class, Recent::class, PatState::class], version = 6)
abstract class CatDataBase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun catInfoDao(): CatInfoDao
    abstract fun catImageDao(): CatImageDao
    abstract fun recentDao(): RecentDao
    abstract fun patDao(): PatDao
}
