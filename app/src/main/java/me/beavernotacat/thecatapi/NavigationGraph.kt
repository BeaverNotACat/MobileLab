package me.beavernotacat.thecatapi

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import me.beavernotacat.thecatapi.models.CatViewModel
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.ui.views.SearchView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import me.beavernotacat.thecatapi.ui.views.DetailsView
import me.beavernotacat.thecatapi.ui.views.SettingsView

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Details : Screen("cat/{id}") {
        fun createRoute(id: String) = "cat/$id"
    }
}

fun NavGraphBuilder.splitMateGraph(
    navController: NavController, viewModel: CatViewModel
) {
    navigation(
        startDestination = Screen.Search.route, route = "root"
    ) {

        composable(Screen.Search.route) {
            val state = viewModel.uiState.collectAsState().value
            SearchView(
                searchState = state.searchScreen,
                onSearch = viewModel::setSearch,
                searchString = state.search,
                onRetry = viewModel::retryQuery,
                setSelectedCat = { i ->
                    navController.navigate(Screen.Details.createRoute(i))
                },
                images = viewModel.catImages.value,
                favoriteIds = state.favorites,
                favoriteCats = state.favoriteCats,
                recentCats = state.recentCats,
                pattedCats = state.pattedCats,
                toPatCats = state.toPatCats,
                toFindCats = state.toFindCats,
                onOpenSettings = { navController.navigate(Screen.Settings.route) },
            )
        }

        composable(Screen.Settings.route) {
            val ttlMinutes = viewModel.cacheTtlMinutesFlow.collectAsState().value
            val ttlHours = ((ttlMinutes + 59) / 60).coerceAtLeast(1L)
            SettingsView(
                currentCacheTtlHours = ttlHours,
                onSaveCacheTtlHours = {
                    viewModel.setCacheTtlMinutes(it * 60)
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.Details.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getString("id")
            val state = viewModel.uiState.collectAsState().value
            if (catId == null) return@composable
            LaunchedEffect(catId) {
                viewModel.fetchBreed(catId)
            }

            DetailsView(
                details = state.detailsScreen,
                favorite = state.favorites.contains(catId),
                patState = when {
                    state.pattedCats.any { it.id == catId } -> CatPatState.Patted
                    state.toPatCats.any { it.id == catId } -> CatPatState.ToPat
                    state.toFindCats.any { it.id == catId } -> CatPatState.ToFind
                    else -> CatPatState.NotFound
                },
                images = viewModel.catImages.value,
                onRetry = viewModel::retryQuery,
                addToFavorites = {
                    viewModel.toggleFavorites(catId)
                },
                onUpdatePatState = { s ->
                    viewModel.updatePatState(catId, s)
                }
            )
        }
    }
}
