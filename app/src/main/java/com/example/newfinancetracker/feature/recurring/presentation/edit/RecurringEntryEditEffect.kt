package com.example.newfinancetracker.feature.recurring.presentation.edit

sealed interface RecurringEntryEditEffect {
    data object NavigateBack : RecurringEntryEditEffect
}
