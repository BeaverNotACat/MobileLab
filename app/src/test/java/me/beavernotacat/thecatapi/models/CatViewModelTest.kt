package me.beavernotacat.thecatapi.models

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.FakeCatApiRepository
import me.beavernotacat.thecatapi.FakeFavoriteRepository
import me.beavernotacat.thecatapi.FakePatRepository
import me.beavernotacat.thecatapi.FakeRecentRepository
import me.beavernotacat.thecatapi.FakeSettingsRepository
import me.beavernotacat.thecatapi.MainDispatcherRule
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.LoadingSearchStates
import me.beavernotacat.thecatapi.repositories.CatApiRepository
import me.beavernotacat.thecatapi.repositories.FavoriteRepository
import me.beavernotacat.thecatapi.repositories.PatRepository
import me.beavernotacat.thecatapi.repositories.RecentRepository
import me.beavernotacat.thecatapi.repositories.SettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var catApiRepository: CatApiRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var recentRepository: RecentRepository
    private lateinit var patRepository: PatRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: CatViewModel

    private fun TestScope.activateFlows() = launch {
        viewModel.uiState.collectLatest { }
    }

    @Before
    fun setup() {
        catApiRepository = FakeCatApiRepository()
        favoriteRepository = FakeFavoriteRepository()
        recentRepository = FakeRecentRepository()
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


    @Test
    fun `fetchList success updates state to OK`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.searchScreen is LoadingSearchStates.Ok)
        collectorJob.cancel()
    }

    @Test
    fun `fetchList failure updates state to ERROR`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()

        viewModel.setSearch("fail")
        advanceUntilIdle()

        assertEquals(
            LoadingSearchStates.Error("Failed by request"),
            viewModel.uiState.value.searchScreen
        )
        collectorJob.cancel()
    }

    @Test
    fun `setSearch after error reloads data`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        viewModel.setSearch("fail")
        advanceUntilIdle()

        viewModel.setSearch("Bengal")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.searchScreen is LoadingSearchStates.Ok)
        collectorJob.cancel()
    }

    @Test
    fun `fetchList after error reloads data`() = runTest {
        val collectorJob = activateFlows()
        viewModel.setSearch("fail")
        advanceUntilIdle()
        assertEquals(
            LoadingSearchStates.Error("Failed by request"),
            viewModel.uiState.value.searchScreen
        )

        viewModel.retryQuery()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.searchScreen is LoadingSearchStates.Empty)
        collectorJob.cancel()
    }

    @Test
    fun `fetchImage returns FailedCanImage on error`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        viewModel.setSearch("Ragdoll")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.searchScreen is LoadingSearchStates.Ok)
        assertEquals(
            CatImage.FailedCatImage("Failed by request"),
            viewModel.catImages.value["fail"]
        )
        collectorJob.cancel()
    }


    @Test
    fun `toggleFavorites updates favorites list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.toggleFavorites(catId)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favorites.contains(catId))

        viewModel.toggleFavorites(catId)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.favorites.contains(catId))
        collectorJob.cancel()
    }

    @Test
    fun `setSearch fetches empty list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        viewModel.setSearch("meow")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.searchScreen is LoadingSearchStates.Empty)
        collectorJob.cancel()
    }

    @Test
    fun `fetchBreed adds to recent list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.fetchBreed(catId)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.recentCats.any { it.id == catId })
        collectorJob.cancel()
    }

    @Test
    fun `favorite cats are loaded from repository`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.toggleFavorites(catId)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favoriteCats.any { it.id == catId })
        collectorJob.cancel()
    }

    @Test
    fun `updatePatState adds cat to found list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.updatePatState(catId, CatPatState.Found)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.foundCats.any { it.id == catId })
        assertFalse(viewModel.uiState.value.pattedCats.any { it.id == catId })
        collectorJob.cancel()
    }

    @Test
    fun `updatePatState moves cat to patted list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.updatePatState(catId, CatPatState.Found)
        advanceUntilIdle()
        viewModel.updatePatState(catId, CatPatState.Patted)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.pattedCats.any { it.id == catId })
        assertFalse(viewModel.uiState.value.foundCats.any { it.id == catId })
        collectorJob.cancel()
    }

    @Test
    fun `updatePatState adds cat to notFound list`() = runTest {
        val collectorJob = activateFlows()
        advanceUntilIdle()
        val catId = "bengal"

        viewModel.updatePatState(catId, CatPatState.NotFound)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.notFoundCats.any { it.id == catId })
        collectorJob.cancel()
    }

    @Test
    fun `setCacheTtlMinutes updates cacheTtlMinutesFlow`() = runTest {
        val collectorJob = activateFlows()
        val ttlJob = launch { viewModel.cacheTtlMinutesFlow.collectLatest { } }
        advanceUntilIdle()

        viewModel.setCacheTtlMinutes(120)
        advanceUntilIdle()

        assertEquals(120L, viewModel.cacheTtlMinutesFlow.value)
        ttlJob.cancel()
        collectorJob.cancel()
    }
}
