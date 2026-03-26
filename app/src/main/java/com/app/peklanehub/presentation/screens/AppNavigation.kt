package com.app.peklanehub.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.peklanehub.presentation.screens.history.HistoryScreen
import com.app.peklanehub.presentation.screens.summary.SummaryScreen
import com.app.peklanehub.presentation.screens.summary.SummaryViewModel

@Composable
fun AppNavigation(viewModel: SummaryViewModel) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.navigateToSummary.collect {
            navController.navigate("summary_screen") {
                popUpTo("summary_screen") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "summary_screen") {
        composable("summary_screen") {
            SummaryScreen(
                viewModel = viewModel,
                onNavigateToHistory = { navController.navigate("history_screen") }
            )
        }

        composable("history_screen") {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}