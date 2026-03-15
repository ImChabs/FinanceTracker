package com.example.newfinancetracker.core.navigation

sealed interface FinanceTrackerDestination {
    val route: String

    data object Dashboard : FinanceTrackerDestination {
        override val route: String = "dashboard"
    }

    data object RecurringEntryCreate : FinanceTrackerDestination {
        override val route: String = "recurring-entry/create"
    }

    data class RecurringEntryEdit(val entryId: Long) : FinanceTrackerDestination {
        override val route: String = "$baseRoute/$entryId"

        companion object {
            const val entryIdArg: String = "entryId"
            const val baseRoute: String = "recurring-entry/edit"
            const val routePattern: String = "$baseRoute/{$entryIdArg}"
        }
    }
}
