package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dashboardScreen_showsCurrencyCodesInSavedCardsAndUpcomingPayments() {
        composeRule.setContent {
            FinanceTrackerTheme {
                DashboardScreen(
                    state = DashboardState(
                        isLoading = false,
                        monthlyRecurringTotal = 29.98,
                        activeEntryCount = 1,
                        savedEntryCount = 2,
                        recurringEntries = listOf(
                            DashboardRecurringEntryItem(
                                id = 1L,
                                name = "Design tool",
                                amount = 19.99,
                                currencyCode = "EUR",
                                billingFrequency = BillingFrequency.MONTHLY,
                                nextPaymentDate = "2026-03-25",
                                category = "Software",
                                type = RecurringEntryType.SUBSCRIPTION,
                                isActive = false,
                                notes = null
                            ),
                            DashboardRecurringEntryItem(
                                id = 2L,
                                name = "Phone plan",
                                amount = 9.99,
                                currencyCode = "JPY",
                                billingFrequency = BillingFrequency.MONTHLY,
                                nextPaymentDate = "2026-03-18",
                                category = "Utilities",
                                type = RecurringEntryType.RECURRING_EXPENSE,
                                isActive = true,
                                notes = null
                            )
                        ),
                        upcomingPayments = listOf(
                            DashboardUpcomingPaymentItem(
                                id = 2L,
                                name = "Phone plan",
                                amount = 9.99,
                                currencyCode = "JPY",
                                nextPaymentDate = "2026-03-18",
                                category = "Utilities"
                            )
                        )
                    ),
                    onAction = {},
                    snackbarHostState = remember { SnackbarHostState() }
                )
            }
        }

        composeRule.onAllNodesWithText("Currency: EUR").assertCountEquals(1)
        composeRule.onAllNodesWithText("Currency: JPY").assertCountEquals(2)
    }
}
