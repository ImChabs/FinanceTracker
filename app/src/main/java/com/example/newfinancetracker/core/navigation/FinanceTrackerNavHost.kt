package com.example.newfinancetracker.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newfinancetracker.feature.dashboard.presentation.DashboardScreenRoot
import com.example.newfinancetracker.feature.recurring.presentation.create.RecurringEntryCreateScreenRoot
import com.example.newfinancetracker.feature.recurring.presentation.edit.RecurringEntryEditScreenRoot

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
            DashboardScreenRoot(
                onAddRecurringEntryClick = {
                    navController.navigate(FinanceTrackerDestination.RecurringEntryCreate.route)
                },
                onRecurringEntryClick = { entryId ->
                    navController.navigate(FinanceTrackerDestination.RecurringEntryEdit(entryId).route)
                }
            )
        }

        composable(FinanceTrackerDestination.RecurringEntryCreate.route) {
            RecurringEntryCreateScreenRoot(
                onNavigateBack = navController::popBackStack
            )
        }

        composable(
            route = FinanceTrackerDestination.RecurringEntryEdit.routePattern,
            arguments = listOf(
                navArgument(FinanceTrackerDestination.RecurringEntryEdit.entryIdArg) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getLong(
                FinanceTrackerDestination.RecurringEntryEdit.entryIdArg
            ) ?: return@composable

            RecurringEntryEditScreenRoot(
                entryId = entryId,
                onNavigateBack = navController::popBackStack
            )
        }
    }
}
