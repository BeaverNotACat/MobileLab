package me.beavernotacat.thecatapi.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.beavernotacat.thecatapi.models.CatDataBase
import me.beavernotacat.thecatapi.models.dao.CatImageDao
import me.beavernotacat.thecatapi.models.dao.CatInfoDao
import me.beavernotacat.thecatapi.models.dao.FavoriteDao
import me.beavernotacat.thecatapi.models.dao.PatDao
import me.beavernotacat.thecatapi.models.dao.RecentDao
import me.beavernotacat.thecatapi.repositories.CacheRepository
import me.beavernotacat.thecatapi.repositories.DefaultCacheRepository
import me.beavernotacat.thecatapi.repositories.DefaultFavoriteRepository
import me.beavernotacat.thecatapi.repositories.DefaultSettingsRepository
import me.beavernotacat.thecatapi.repositories.FavoriteRepository
import me.beavernotacat.thecatapi.repositories.DefaultRecentRepository
import me.beavernotacat.thecatapi.repositories.RecentRepository
import me.beavernotacat.thecatapi.repositories.DefaultPatRepository
import me.beavernotacat.thecatapi.repositories.PatRepository
import me.beavernotacat.thecatapi.repositories.SettingsRepository
import me.beavernotacat.thecatapi.work.CacheInvalidationScheduler
import me.beavernotacat.thecatapi.work.DefaultCacheInvalidationScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): CatDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            CatDataBase::class.java,
            "cats.db"
        ).build()
    }

    @Provides
    fun provideFavoritesDao(database: CatDataBase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideCatInfoDao(database: CatDataBase): CatInfoDao = database.catInfoDao()

    @Provides
    fun provideCatImageDao(database: CatDataBase): CatImageDao = database.catImageDao()

    @Provides
    fun provideRecentDao(database: CatDataBase): RecentDao = database.recentDao()

    @Provides
    fun providePatDao(database: CatDataBase): PatDao = database.patDao()
}


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindFavoriteRepository(dataSource: DefaultFavoriteRepository): FavoriteRepository

    @Singleton
    @Binds
    abstract fun bindRecentRepository(dataSource: DefaultRecentRepository): RecentRepository

    @Singleton
    @Binds
    abstract fun bindPatRepository(dataSource: DefaultPatRepository): PatRepository

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(dataSource: DefaultSettingsRepository): SettingsRepository

    @Singleton
    @Binds
    abstract fun bindCacheRepository(dataSource: DefaultCacheRepository): CacheRepository

    @Singleton
    @Binds
    abstract fun bindCacheInvalidationScheduler(
        dataSource: DefaultCacheInvalidationScheduler
    ): CacheInvalidationScheduler
}
