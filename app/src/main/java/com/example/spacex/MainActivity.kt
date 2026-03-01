package com.example.spacex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.spacex.ui.theme.SpaceXTheme
import com.example.spacex.ui.viewmodel.CrewViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        SpaceXApp()
                    }
                }
            }
        }
    }
}

@Composable
fun SpaceXApp() {
    val viewModel: CrewViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "root"
    ) {
        spacexNavGraph(
            navController = navController,
            holder = viewModel
        )
    }
}