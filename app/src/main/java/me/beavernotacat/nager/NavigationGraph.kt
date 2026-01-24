package me.beavernotacat.nager

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import me.beavernotacat.nager.models.LoadingStates
import me.beavernotacat.nager.ui.components.ErrorComponent
import me.beavernotacat.nager.models.ViewModel
import me.beavernotacat.nager.ui.components.LoadingComponent
import me.beavernotacat.nager.ui.screens.CountryView
import me.beavernotacat.nager.ui.screens.ListView

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Details : Screen("country/{code}") {
        fun createRoute(code: String) = "country/$code"
    }
}

fun NavGraphBuilder.splitMateGraph(
    navController: NavController, viewModel: ViewModel
) {
    navigation(
        startDestination = Screen.Search.route, route = "root"
    ) {

        composable(Screen.Search.route) {
            val state by viewModel.uiState.collectAsState()
            ListView (
                countries = state.countries,
                loadingState = state.state,
                onRetry = viewModel::fetchCountries,
                openCountry = { i ->
                    viewModel.fetchCountry(i)
                    navController.navigate(Screen.Details.createRoute(i))
                },
                favorites = state.favoriteList
            )
        }

        composable(Screen.Details.route, arguments = listOf(
            navArgument("code") {
                type = NavType.StringType
            })) { backStackEntry ->
            val countryCode = backStackEntry.arguments?.getString("code")
            val state by viewModel.uiState.collectAsState()
            val holidays = state.countryHolidays[countryCode]
            if (state.state == LoadingStates.LOADING) LoadingComponent()
            else if (countryCode != null && holidays != null) CountryView(
                countryCode = countryCode,
                holidaysInfo = holidays,
                favorite = !state.favoriteList.contains(countryCode),
                addToFavorites = {
                    viewModel.addToFavorites(countryCode)
                },
                removeFromFavorites = {
                    viewModel.removeFromFavorites(countryCode)
                }
            ) else {
                ErrorComponent ( onRetry = {
                    navController.popBackStack()
                } )
            }
        }
    }
}