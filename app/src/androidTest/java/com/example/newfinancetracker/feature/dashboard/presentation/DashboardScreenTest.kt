package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.semantics.SemanticsActions
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import org.junit.Assert.assertEquals
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

    @Test
    fun dashboardScreen_showsMixedCurrencySummaryMessageWhenActiveCurrenciesDiffer() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 29.98,
                            activeEntryCount = 2,
                            activeCurrencyCodes = setOf("EUR", "JPY"),
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
                                    isActive = true,
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
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodesWithText("29.98").assertCountEquals(1)
            composeRule.onAllNodesWithText("\$29.98").assertCountEquals(0)
            composeRule.onAllNodesWithText(
                "Includes 2 active currencies. Total is an unconverted aggregate."
            ).assertCountEquals(1)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_hidesMixedCurrencySummaryMessageWhenActiveCurrencyIsSingular() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 19.99,
                            activeEntryCount = 1,
                            activeCurrencyCodes = setOf("EUR"),
                            savedEntryCount = 1,
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
                                )
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodesWithText("\$19.99").assertCountEquals(1)
            composeRule.onAllNodesWithText(
                "Includes 2 active currencies. Total is an unconverted aggregate."
            ).assertCountEquals(0)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_formatsDisplayedDatesAndFallsBackForLegacyValues() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 35.98,
                            activeEntryCount = 2,
                            savedEntryCount = 2,
                            recurringEntries = listOf(
                                DashboardRecurringEntryItem(
                                    id = 1L,
                                    name = "Music",
                                    amount = 15.99,
                                    currencyCode = "USD",
                                    billingFrequency = BillingFrequency.MONTHLY,
                                    nextPaymentDate = "2026-03-31",
                                    category = "Streaming",
                                    type = RecurringEntryType.SUBSCRIPTION,
                                    isActive = true,
                                    notes = null
                                ),
                                DashboardRecurringEntryItem(
                                    id = 2L,
                                    name = "Legacy import",
                                    amount = 20.0,
                                    currencyCode = "USD",
                                    billingFrequency = BillingFrequency.MONTHLY,
                                    nextPaymentDate = "31/03/2026",
                                    category = "Other",
                                    type = RecurringEntryType.RECURRING_EXPENSE,
                                    isActive = true,
                                    notes = null
                                )
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 1L,
                                    name = "Music",
                                    amount = 15.99,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-31",
                                    category = "Streaming"
                                )
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodesWithText("Mar 31, 2026 - Streaming").assertCountEquals(1)
            composeRule.onAllNodesWithText("Next payment Mar 31, 2026").assertCountEquals(1)
            composeRule.onAllNodesWithText("Next payment 31/03/2026").assertCountEquals(1)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_marksOverdueAndDueTodayPaymentsAsUrgent() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 40.0,
                            activeEntryCount = 3,
                            savedEntryCount = 3,
                            recurringEntries = listOf(
                                savedEntry(id = 1L, name = "Rent", nextPaymentDate = "2026-03-14"),
                                savedEntry(id = 2L, name = "Water", nextPaymentDate = "2026-03-15"),
                                savedEntry(id = 3L, name = "Music", nextPaymentDate = "2026-03-17")
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(daysOverdue = 1)
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-15",
                                    category = "Utilities",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 3L,
                                    name = "Music",
                                    amount = 10.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-17",
                                    category = "Streaming",
                                    relativeDueContext = DashboardRelativeDueContext.DueInDays(daysUntilDue = 2)
                                )
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodes(
                hasContentDescription("Rent, \$20.00, Mar 14, 2026, Housing, USD")
            ).assertCountEquals(1)
            composeRule.onNode(
                hasContentDescription("Rent, \$20.00, Mar 14, 2026, Housing, USD")
            ).assert(hasUpcomingPaymentUrgency("OVERDUE"))
            composeRule.onAllNodes(
                hasContentDescription("Water, \$10.00, Mar 15, 2026, Utilities, USD")
            ).assertCountEquals(1)
            composeRule.onNode(
                hasContentDescription("Water, \$10.00, Mar 15, 2026, Utilities, USD")
            ).assert(hasUpcomingPaymentUrgency("DUE_TODAY"))
            composeRule.onAllNodes(
                hasContentDescription("Music, \$10.00, Mar 17, 2026, Streaming, USD, Due in 2 days")
            ).assertCountEquals(1)
            composeRule.onNode(
                hasContentDescription("Music, \$10.00, Mar 17, 2026, Streaming, USD, Due in 2 days")
            ).assert(hasUpcomingPaymentUrgency("STANDARD"))
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_exposesUrgencyAccessibilityDescriptionsOnlyForUrgentUpcomingPayments() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 40.0,
                            activeEntryCount = 3,
                            savedEntryCount = 3,
                            recurringEntries = listOf(
                                savedEntry(id = 1L, name = "Rent", nextPaymentDate = "2026-03-14"),
                                savedEntry(id = 2L, name = "Water", nextPaymentDate = "2026-03-15"),
                                savedEntry(id = 3L, name = "Music", nextPaymentDate = "2026-03-17")
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(daysOverdue = 1)
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-15",
                                    category = "Utilities",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 3L,
                                    name = "Music",
                                    amount = 10.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-17",
                                    category = "Streaming",
                                    relativeDueContext = DashboardRelativeDueContext.DueInDays(daysUntilDue = 2)
                                )
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onNode(
                hasContentDescription("Rent, \$20.00, Mar 14, 2026, Housing, USD")
            ).assert(hasStateDescription("Overdue payment"))
            composeRule.onNode(
                hasContentDescription("Water, \$10.00, Mar 15, 2026, Utilities, USD")
            ).assert(hasStateDescription("Payment due today"))
            composeRule.onNode(
                hasContentDescription("Music, \$10.00, Mar 17, 2026, Streaming, USD, Due in 2 days")
            ).assert(
                SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsProperties.StateDescription)
            )
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_exposesUpcomingPaymentAccessibilitySummariesWithoutDuplicatingUrgentDueText() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 45.0,
                            activeEntryCount = 3,
                            savedEntryCount = 3,
                            recurringEntries = listOf(
                                savedEntry(id = 1L, name = "Rent", nextPaymentDate = "2026-03-14"),
                                savedEntry(id = 2L, name = "Water", nextPaymentDate = "2026-03-15"),
                                savedEntry(id = 3L, name = "Legacy import", nextPaymentDate = "17/03/2026")
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(daysOverdue = 1)
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-15",
                                    category = "Utilities",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                DashboardUpcomingPaymentItem(
                                    id = 3L,
                                    name = "Legacy import",
                                    amount = 15.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "17/03/2026",
                                    category = "Other",
                                    relativeDueContext = DashboardRelativeDueContext.DueTomorrow
                                )
                            )
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodes(
                hasContentDescription("Rent, \$20.00, Mar 14, 2026, Housing, USD")
            ).assertCountEquals(1)
            composeRule.onAllNodes(
                hasContentDescription("Water, \$10.00, Mar 15, 2026, Utilities, USD")
            ).assertCountEquals(1)
            composeRule.onAllNodes(
                hasContentDescription("Legacy import, \$15.00, 17/03/2026, Other, USD, Due tomorrow")
            ).assertCountEquals(1)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_upcomingPaymentRowClickDispatchesRecurringEntryActionAndKeepsUrgentSemantics() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 20.0,
                            activeEntryCount = 1,
                            savedEntryCount = 1,
                            recurringEntries = listOf(
                                savedEntry(id = 7L, name = "Rent", nextPaymentDate = "2026-03-14")
                            ),
                            upcomingPayments = listOf(
                                DashboardUpcomingPaymentItem(
                                    id = 7L,
                                    name = "Rent",
                                    amount = 20.0,
                                    currencyCode = "USD",
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(daysOverdue = 1)
                                )
                            )
                        ),
                        onAction = actions::add,
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onNode(
                hasContentDescription("Rent, \$20.00, Mar 14, 2026, Housing, USD")
            ).assert(hasClickAction())
                .assert(hasStateDescription("Overdue payment"))
                .assert(hasUpcomingPaymentUrgency("OVERDUE"))
                .performClick()

            assertEquals(listOf(DashboardAction.RecurringEntryClicked(7L)), actions)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_savedRecurringEntryCardExposesMergedAccessibilitySummaryAndClickAction() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 15.99,
                            activeEntryCount = 1,
                            savedEntryCount = 1,
                            recurringEntries = listOf(
                                DashboardRecurringEntryItem(
                                    id = 5L,
                                    name = "Netflix",
                                    amount = 15.99,
                                    currencyCode = "USD",
                                    billingFrequency = BillingFrequency.MONTHLY,
                                    nextPaymentDate = "2026-03-31",
                                    category = "Streaming",
                                    type = RecurringEntryType.SUBSCRIPTION,
                                    isActive = true,
                                    notes = "Family plan"
                                )
                            )
                        ),
                        onAction = actions::add,
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onNode(
                hasContentDescription(
                    "Netflix, \$15.99, Subscription, Monthly, Mar 31, 2026, Streaming, USD, Active, Notes: Family plan"
                )
            ).assert(hasClickAction())
                .assert(hasStateDescription("Active"))
                .assert(hasClickLabel("Edit saved recurring entry"))
                .performClick()

            assertEquals(listOf(DashboardAction.RecurringEntryClicked(5L)), actions)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_summaryCardExposesMergedAccessibilitySummary() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 29.98,
                            activeEntryCount = 2,
                            activeCurrencyCodes = setOf("EUR", "JPY"),
                            savedEntryCount = 2,
                            currencyMetadataCount = 2
                        ),
                        onAction = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodes(
                hasContentDescription(
                    "Monthly recurring total. 29.98. Across 2 active entries. Includes 2 active currencies. Total is an unconverted aggregate. Currency metadata cache size: 2"
                )
            ).assertCountEquals(1)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_summaryCardKeepsRetryActionAvailableWhenCurrencyMetadataFails() {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        try {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setContent {
                FinanceTrackerTheme {
                    DashboardScreen(
                        state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 15.99,
                            activeEntryCount = 1,
                            activeCurrencyCodes = setOf("USD"),
                            savedEntryCount = 1,
                            hasCurrencySyncFailure = true
                        ),
                        onAction = actions::add,
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }

            composeRule.onAllNodes(
                hasContentDescription(
                    "Monthly recurring total. \$15.99. Across 1 active entries. Currency metadata unavailable right now."
                )
            ).assertCountEquals(1)
            composeRule.onNodeWithText("Retry currency sync")
                .assert(hasClickAction())
                .performClick()

            assertEquals(listOf(DashboardAction.RetryCurrencyMetadataClicked), actions)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    @Test
    fun dashboardScreen_emptyStateCardExposesMergedAccessibilitySummary() {
        composeRule.setContent {
            FinanceTrackerTheme {
                DashboardScreen(
                    state = DashboardState(
                        isLoading = false,
                        isEmpty = true
                    ),
                    onAction = {},
                    snackbarHostState = remember { SnackbarHostState() }
                )
            }
        }

        composeRule.onAllNodes(
            hasContentDescription(
                "No recurring entries yet. Add your first subscription or recurring expense to start tracking upcoming payments and monthly totals."
            )
        ).assertCountEquals(1)
    }

    private fun savedEntry(
        id: Long,
        name: String,
        nextPaymentDate: String
    ): DashboardRecurringEntryItem = DashboardRecurringEntryItem(
        id = id,
        name = name,
        amount = 10.0,
        currencyCode = "USD",
        billingFrequency = BillingFrequency.MONTHLY,
        nextPaymentDate = nextPaymentDate,
        category = "Utilities",
        type = RecurringEntryType.RECURRING_EXPENSE,
        isActive = true,
        notes = null
    )

    private fun hasUpcomingPaymentUrgency(value: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(UpcomingPaymentUrgencySemanticsKey, value)

    private fun hasClickLabel(label: String): SemanticsMatcher =
        SemanticsMatcher("has click label $label") { semanticsNode ->
            val config = semanticsNode.config
            if (SemanticsActions.OnClick !in config) {
                false
            } else {
                config[SemanticsActions.OnClick].label == label
            }
        }
}
