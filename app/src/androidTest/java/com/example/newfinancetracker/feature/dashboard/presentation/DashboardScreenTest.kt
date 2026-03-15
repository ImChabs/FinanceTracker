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
import java.util.Locale

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

    @Test
    fun dashboardScreen_formatsSavedAndUpcomingAmountsWithEntryCurrency() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 130.48,
                            activeEntryCount = 2,
                            savedEntryCount = 3,
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
                                    isActive = true,
                                    notes = null
                                ),
                                DashboardRecurringEntryItem(
                                    id = 2L,
                                    name = "Phone plan",
                                    amount = 1200.0,
                                    currencyCode = "JPY",
                                    billingFrequency = BillingFrequency.MONTHLY,
                                    nextPaymentDate = "2026-03-18",
                                    category = "Utilities",
                                    type = RecurringEntryType.RECURRING_EXPENSE,
                                    isActive = true,
                                    notes = null
                                ),
                                DashboardRecurringEntryItem(
                                    id = 3L,
                                    name = "Legacy import",
                                    amount = 42.5,
                                    currencyCode = "US",
                                    billingFrequency = BillingFrequency.MONTHLY,
                                    nextPaymentDate = "2026-03-28",
                                    category = "Other",
                                    type = RecurringEntryType.RECURRING_EXPENSE,
                                    isActive = false,
                                    notes = null
                                )
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 1L,
                                    name = "Design tool",
                                    amount = 19.99,
                                    currencyCode = "EUR",
                                    nextPaymentDate = "2026-03-25",
                                    category = "Software"
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 2L,
                                    name = "Phone plan",
                                    amount = 1200.0,
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

            composeRule.onAllNodesWithText("€19.99").assertCountEquals(2)
            composeRule.onAllNodesWithText("¥1,200").assertCountEquals(2)
            composeRule.onAllNodesWithText("US 42.50").assertCountEquals(1)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }
}
