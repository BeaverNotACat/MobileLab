package me.beavernotacat.splitmate

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import me.beavernotacat.splitmate.models.ViewModel
import me.beavernotacat.splitmate.ui.screens.InputView
import me.beavernotacat.splitmate.ui.screens.MainView
import me.beavernotacat.splitmate.ui.screens.ResultView

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Input : Screen("input")
    object Result : Screen("result/{id}") {
        fun createRoute(id: Int) = "result/$id"
    }
}

fun NavGraphBuilder.splitMateGraph(
    navController: NavController, viewModel: ViewModel
) {
    navigation(
        startDestination = Screen.Home.route, route = "root"
    ) {
        composable(Screen.Home.route) {
            MainView(
                onStartClicked = {
                    navController.navigate(Screen.Input.route)
                })
        }

        composable(Screen.Input.route) {
            InputView(
                split = viewModel.splitState.collectAsState().value,
                onCalculateClicked = {
                    navController.navigate(Screen.Result.createRoute(1))
                },
                onTotalAmountChanged = viewModel::updateTotalAmount,
                onPeopleCountChanged = viewModel::updatePeopleCount,
                onTipPercentageChanged = viewModel::updateTipPercentage
            )
        }

        composable(
            route = Screen.Result.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                })
        ) {

            ResultView(split = viewModel.splitState.collectAsState().value, onBackToEdit = {
                navController.popBackStack()
            }, onNewCalculation = {
                navController.navigate(Screen.Input.route) {
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
                viewModel.startNewCalculation()
            })
        }
    }
}