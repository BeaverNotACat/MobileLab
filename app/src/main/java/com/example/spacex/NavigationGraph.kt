package com.example.spacex

import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.spacex.ui.components.ErrorComponent
import com.example.spacex.ui.screens.DetailView
import com.example.spacex.ui.screens.ListScreen
import com.example.spacex.ui.viewmodel.CrewViewModel

sealed class Screen(val route: String) {
    object List : Screen("crew")
    object Details : Screen("crew/{id}") {
        fun createRoute(id: String) = "crew/$id"
    }
}

fun NavGraphBuilder.spacexNavGraph(
    navController: NavController, holder: CrewViewModel
) {
    navigation(
        startDestination = Screen.List.route, route = "root"
    ) {

        composable(Screen.List.route) {
            val state = holder.uiState
            ListScreen(
                crews = holder.items,
                retry = holder::list,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onSelect = {
                    navController.navigate(Screen.Details.createRoute(it))
                    holder.addRecent(it)
                },
                selectedFilter = state.selectedFilter,
                onSelectFilter = holder::setFilter
            )
        }

        composable(
            Screen.Details.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val person = if (id != null) holder.getItem(id) else null
            if (id != null && person != null) DetailView(
                person = person,
                openWikipedia = {
                    val openIntent: Intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = person.wikipedia.toUri()
                    }

                    val shareIntent = Intent.createChooser(openIntent, null)
                    navController.context.startActivity(shareIntent, null)
                }
            ) else {
                ErrorComponent(error = "You lost in stars", onRetry = {
                    navController.popBackStack()
                })
            }
        }
    }
}