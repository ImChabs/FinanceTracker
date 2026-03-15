package com.example.newfinancetracker.feature.dashboard.presentation

sealed interface DashboardAction {
    data object AddRecurringEntryClicked : DashboardAction
    data object RetryCurrencyMetadataClicked : DashboardAction
    data class RecurringEntryClicked(val entryId: Long) : DashboardAction
}
