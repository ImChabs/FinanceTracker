package com.example.newfinancetracker.feature.dashboard.presentation

sealed interface DashboardEffect {
    data object NavigateToRecurringEntryCreate : DashboardEffect
    data class NavigateToRecurringEntryEdit(val entryId: Long) : DashboardEffect
    data object CurrencyMetadataRetrySucceeded : DashboardEffect
    data object CurrencyMetadataRetryFailed : DashboardEffect
}
