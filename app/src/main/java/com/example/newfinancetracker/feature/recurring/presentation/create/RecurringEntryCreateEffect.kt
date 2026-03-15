package com.example.newfinancetracker.feature.recurring.presentation.create

sealed interface RecurringEntryCreateEffect {
    data object NavigateBack : RecurringEntryCreateEffect
}
