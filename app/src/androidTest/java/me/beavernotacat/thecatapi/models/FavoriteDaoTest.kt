package me.beavernotacat.thecatapi.models

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.FakeCatApiRepository
import me.beavernotacat.thecatapi.FakePatRepository
import me.beavernotacat.thecatapi.FakeSettingsRepository
import me.beavernotacat.thecatapi.MainDispatcherRule
import me.beavernotacat.thecatapi.models.dao.FavoriteDao
import me.beavernotacat.thecatapi.models.dao.RecentDao
import me.beavernotacat.thecatapi.models.local.Favorite
import me.beavernotacat.thecatapi.repositories.CatApiRepository
import me.beavernotacat.thecatapi.repositories.DefaultFavoriteRepository
import me.beavernotacat.thecatapi.repositories.DefaultRecentRepository
import me.beavernotacat.thecatapi.repositories.FavoriteRepository
import me.beavernotacat.thecatapi.repositories.PatRepository
import me.beavernotacat.thecatapi.repositories.RecentRepository
import me.beavernotacat.thecatapi.repositories.SettingsRepository
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: CatDataBase
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var recentDao: RecentDao
    private lateinit var catApiRepository: CatApiRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var recentRepository: RecentRepository
    private lateinit var patRepository: PatRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: CatViewModel

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CatDataBase::class.java)
            .setTransactionExecutor(mainDispatcherRule.dispatcher.asExecutor())
            .setQueryExecutor(mainDispatcherRule.dispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()
        favoriteDao = database.favoriteDao()
        recentDao = database.recentDao()
        catApiRepository = FakeCatApiRepository()
        favoriteRepository = DefaultFavoriteRepository(favoriteDao)
        recentRepository = DefaultRecentRepository(recentDao)
        patRepository = FakePatRepository()
        settingsRepository = FakeSettingsRepository()
        viewModel = CatViewModel(
            catApiRepository,
            favoriteRepository,
            recentRepository,
            patRepository,
            settingsRepository
        )
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `favorites can be added and removed`() = runTest {
        val catId = "bengal"
        favoriteDao.insertAll(Favorite(catId))
        var favorites = favoriteDao.getAll()
        assert(favorites.size == 1)

        favoriteDao.delete(Favorite(catId))
        favorites = favoriteDao.getAll()
        assert(favorites.isEmpty())
    }

    @Test
    fun `toggleFavorites updates favorites list`() = runTest {
        val catId = "bengal"

        advanceUntilIdle()

        viewModel.toggleFavorites(catId)
        advanceUntilIdle()

        viewModel.toggleFavorites(catId)
        advanceUntilIdle()

        backgroundScope.launch {
            val favorites = viewModel.uiState.toList().map { it.favorites }
            assertTrue(favorites[0].isEmpty())
            assertTrue(favorites[1].contains(catId))
            assertTrue(favorites[0].isEmpty())
        }
    }
}
