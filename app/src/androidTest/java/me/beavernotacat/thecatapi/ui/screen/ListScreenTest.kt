package me.beavernotacat.thecatapi.ui.screen

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.FakeCatApiRepository
import me.beavernotacat.thecatapi.FakeFavoriteRepository
import me.beavernotacat.thecatapi.FakePatRepository
import me.beavernotacat.thecatapi.FakeRecentRepository
import me.beavernotacat.thecatapi.FakeSettingsRepository
import me.beavernotacat.thecatapi.MainDispatcherRule
import me.beavernotacat.thecatapi.models.CatViewModel
import me.beavernotacat.thecatapi.ui.views.SearchView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ListScreenTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var catApiRepository: FakeCatApiRepository
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var recentRepository: FakeRecentRepository
    private lateinit var patRepository: FakePatRepository
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var viewModel: CatViewModel

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
    fun `retry button refreshes list`() = runTest(testDispatcher) {
        composeRule.setContent {
            val state by viewModel.uiState.collectAsState()
            Log.i("start", "hui")
            SearchView(
                searchState = state.searchScreen,
                images = viewModel.catImages.value,
                favoriteCats = state.favoriteCats,
                favoriteIds = state.favorites,
                searchString = state.search,
                onSearch = viewModel::setSearch,
                onRetry = viewModel::retryQuery,
                setSelectedCat = { },
                recentCats = state.recentCats,
                pattedCats = state.pattedCats,
                toPatCats = state.toPatCats,
                toFindCats = state.toFindCats,
                onOpenSettings = {}
            )
        }

        composeRule.onNodeWithTag("search field").performTextClearance()
        composeRule.onNodeWithTag("search field").performTextInput("fail")
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Failed by request").assertIsDisplayed()
        composeRule.onNodeWithText("Try again").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("No cat here").assertIsDisplayed()
    }
}
