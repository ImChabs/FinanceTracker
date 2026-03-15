package com.example.newfinancetracker.feature.dashboard.presentation

sealed interface DashboardAction {
    data object AddRecurringEntryClicked : DashboardAction
}
