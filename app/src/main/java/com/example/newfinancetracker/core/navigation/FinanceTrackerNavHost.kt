package com.example.newfinancetracker.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newfinancetracker.feature.dashboard.presentation.DashboardScreen

@Composable
fun FinanceTrackerNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FinanceTrackerDestination.Dashboard.route,
        modifier = modifier
    ) {
        composable(FinanceTrackerDestination.Dashboard.route) {
            DashboardScreen()
        }
    }
}
