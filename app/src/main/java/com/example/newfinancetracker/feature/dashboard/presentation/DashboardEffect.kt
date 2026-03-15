package com.example.newfinancetracker.feature.dashboard.presentation

sealed interface DashboardEffect {
    data object NavigateToRecurringEntryCreate : DashboardEffect
}
