package me.beavernotacat.splitmate

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
import me.beavernotacat.splitmate.models.ViewModel
import me.beavernotacat.splitmate.ui.theme.SplitMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplitMateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        App()
                    }
                }
            }
        }
    }
}

@Composable
fun App(
    viewModel: ViewModel = viewModel()
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "root"
    ) {
        splitMateGraph(
            navController = navController,
            viewModel = viewModel
        )
    }
}
