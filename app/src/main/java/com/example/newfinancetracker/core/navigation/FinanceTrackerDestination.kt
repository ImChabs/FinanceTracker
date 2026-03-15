package com.example.newfinancetracker.core.navigation

sealed interface FinanceTrackerDestination {
    val route: String

    data object Dashboard : FinanceTrackerDestination {
        override val route: String = "dashboard"
    }

    data object RecurringEntryCreate : FinanceTrackerDestination {
        override val route: String = "recurring-entry/create"
    }
}
