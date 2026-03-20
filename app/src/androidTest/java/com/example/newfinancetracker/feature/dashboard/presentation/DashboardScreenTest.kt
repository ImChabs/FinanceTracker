package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
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
        composeRule.setDashboardContent(
            state = DashboardState(
                isLoading = false,
                monthlyRecurringTotal = 29.98,
                activeEntryCount = 1,
                savedEntryCount = 2,
                recurringEntries = listOf(
                    savedEntry(
                        id = 1L,
                        name = "Design tool",
                        amount = 19.99,
                        currencyCode = "EUR",
                        nextPaymentDate = "2026-03-25",
                        category = "Software",
                        type = RecurringEntryType.SUBSCRIPTION,
                        isActive = false
                    ),
                    savedEntry(
                        id = 2L,
                        name = "Phone plan",
                        amount = 9.99,
                        currencyCode = "JPY",
                        nextPaymentDate = "2026-03-18"
                    )
                ),
                upcomingPayments = listOf(
                    upcomingPayment(
                        id = 2L,
                        name = "Phone plan",
                        amount = 9.99,
                        currencyCode = "JPY",
                        nextPaymentDate = "2026-03-18"
                    )
                )
            )
        )

        composeRule.onAllNodesWithText("Currency: EUR").assertCountEquals(1)
        composeRule.onAllNodesWithText("Currency: JPY").assertCountEquals(2)
    }

    @Test
    fun dashboardScreen_formatsSavedAndUpcomingAmountsWithEntryCurrency() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 130.48,
                            activeEntryCount = 2,
                            savedEntryCount = 3,
                            recurringEntries = listOf(
                                savedEntry(
                                    id = 1L,
                                    name = "Design tool",
                                    amount = 19.99,
                                    currencyCode = "EUR",
                                    nextPaymentDate = "2026-03-25",
                                    category = "Software",
                                    type = RecurringEntryType.SUBSCRIPTION
                                ),
                                savedEntry(
                                    id = 2L,
                                    name = "Phone plan",
                                    amount = 1200.0,
                                    currencyCode = "JPY",
                                    nextPaymentDate = "2026-03-18"
                                ),
                                savedEntry(
                                    id = 3L,
                                    name = "Legacy import",
                                    amount = 42.5,
                                    currencyCode = "US",
                                    nextPaymentDate = "2026-03-28",
                                    category = "Other",
                                    isActive = false
                                )
                            ),
                            upcomingPayments = listOf(
                                upcomingPayment(
                                    id = 1L,
                                    name = "Design tool",
                                    amount = 19.99,
                                    currencyCode = "EUR",
                                    nextPaymentDate = "2026-03-25",
                                    category = "Software"
                                ),
                                upcomingPayment(
                                    id = 2L,
                                    name = "Phone plan",
                                    amount = 1200.0,
                                    currencyCode = "JPY",
                                    nextPaymentDate = "2026-03-18"
                                )
                            )
                )
            )

            composeRule.onAllNodesWithText("€19.99").assertCountEquals(2)
            composeRule.onAllNodesWithText("¥1,200").assertCountEquals(2)
            composeRule.onAllNodesWithText("US 42.50").assertCountEquals(1)
        }
    }

    @Test
    fun dashboardScreen_showsMixedCurrencySummaryMessageWhenActiveCurrenciesDiffer() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 29.98,
                            activeEntryCount = 2,
                            activeCurrencyCodes = setOf("EUR", "JPY"),
                            savedEntryCount = 2,
                            recurringEntries = listOf(
                                savedEntry(
                                    id = 1L,
                                    name = "Design tool",
                                    amount = 19.99,
                                    currencyCode = "EUR",
                                    nextPaymentDate = "2026-03-25",
                                    category = "Software",
                                    type = RecurringEntryType.SUBSCRIPTION
                                ),
                                savedEntry(
                                    id = 2L,
                                    name = "Phone plan",
                                    amount = 9.99,
                                    currencyCode = "JPY",
                                    nextPaymentDate = "2026-03-18"
                                )
                            )
                )
            )

            composeRule.onAllNodesWithText("29.98").assertCountEquals(1)
            composeRule.onAllNodesWithText("\$29.98").assertCountEquals(0)
            composeRule.onAllNodesWithText(
                "Includes 2 active currencies. Total is an unconverted aggregate."
            ).assertCountEquals(1)
        }
    }

    @Test
    fun dashboardScreen_hidesMixedCurrencySummaryMessageWhenActiveCurrencyIsSingular() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 19.99,
                            activeEntryCount = 1,
                            activeCurrencyCodes = setOf("EUR"),
                            savedEntryCount = 1,
                            recurringEntries = listOf(
                                savedEntry(
                                    id = 1L,
                                    name = "Design tool",
                                    amount = 19.99,
                                    currencyCode = "EUR",
                                    nextPaymentDate = "2026-03-25",
                                    category = "Software",
                                    type = RecurringEntryType.SUBSCRIPTION
                                )
                            )
                )
            )

            composeRule.onAllNodesWithText("\$19.99").assertCountEquals(1)
            composeRule.onAllNodesWithText(
                "Includes 2 active currencies. Total is an unconverted aggregate."
            ).assertCountEquals(0)
        }
    }

    @Test
    fun dashboardScreen_formatsDisplayedDatesAndFallsBackForLegacyValues() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 35.98,
                            activeEntryCount = 2,
                            savedEntryCount = 2,
                            recurringEntries = listOf(
                                savedEntry(
                                    id = 1L,
                                    name = "Music",
                                    amount = 15.99,
                                    nextPaymentDate = "2026-03-31",
                                    category = "Streaming",
                                    type = RecurringEntryType.SUBSCRIPTION
                                ),
                                savedEntry(
                                    id = 2L,
                                    name = "Legacy import",
                                    amount = 20.0,
                                    nextPaymentDate = "31/03/2026",
                                    category = "Other"
                                )
                            ),
                            upcomingPayments = listOf(
                                upcomingPayment(
                                    id = 1L,
                                    name = "Music",
                                    amount = 15.99,
                                    nextPaymentDate = "2026-03-31",
                                    category = "Streaming"
                                )
                            )
                )
            )

            composeRule.onAllNodesWithText("Mar 31, 2026 - Streaming").assertCountEquals(1)
            composeRule.onAllNodesWithText("Next payment Mar 31, 2026").assertCountEquals(1)
            composeRule.onAllNodesWithText("Next payment 31/03/2026").assertCountEquals(1)
        }
    }

    @Test
    fun dashboardScreen_marksOverdueAndDueTodayPaymentsAsUrgent() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
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
                                upcomingPayment(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(
                                        daysOverdue = 1
                                    )
                                ),
                                upcomingPayment(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    nextPaymentDate = "2026-03-15",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                upcomingPayment(
                                    id = 3L,
                                    name = "Music",
                                    amount = 10.0,
                                    nextPaymentDate = "2026-03-17",
                                    category = "Streaming",
                                    relativeDueContext = DashboardRelativeDueContext.DueInDays(
                                        daysUntilDue = 2
                                    )
                                )
                            )
                )
            )

            composeRule.assertSingleUpcomingPaymentRow(
                "Rent, \$20.00, Mar 14, 2026, Housing, USD"
            ).assert(hasUpcomingPaymentUrgency("OVERDUE"))
            composeRule.assertSingleUpcomingPaymentRow(
                "Water, \$10.00, Mar 15, 2026, Utilities, USD"
            ).assert(hasUpcomingPaymentUrgency("DUE_TODAY"))
            composeRule.assertSingleUpcomingPaymentRow(
                "Music, \$10.00, Mar 17, 2026, Streaming, USD, Due in 2 days"
            ).assert(hasUpcomingPaymentUrgency("STANDARD"))
        }
    }

    @Test
    fun dashboardScreen_exposesUrgencyAccessibilityDescriptionsOnlyForUrgentUpcomingPayments() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
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
                                upcomingPayment(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(
                                        daysOverdue = 1
                                    )
                                ),
                                upcomingPayment(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    nextPaymentDate = "2026-03-15",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                upcomingPayment(
                                    id = 3L,
                                    name = "Music",
                                    amount = 10.0,
                                    nextPaymentDate = "2026-03-17",
                                    category = "Streaming",
                                    relativeDueContext = DashboardRelativeDueContext.DueInDays(
                                        daysUntilDue = 2
                                    )
                                )
                            )
                )
            )

            composeRule.upcomingPaymentRow(
                "Rent, \$20.00, Mar 14, 2026, Housing, USD"
            ).assert(hasStateDescription("Overdue payment"))
            composeRule.upcomingPaymentRow(
                "Water, \$10.00, Mar 15, 2026, Utilities, USD"
            ).assert(hasStateDescription("Payment due today"))
            composeRule.upcomingPaymentRow(
                "Music, \$10.00, Mar 17, 2026, Streaming, USD, Due in 2 days"
            ).assert(
                SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsProperties.StateDescription)
            )
        }
    }

    @Test
    fun dashboardScreen_exposesUpcomingPaymentAccessibilitySummariesWithoutDuplicatingUrgentDueText() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                            isLoading = false,
                            monthlyRecurringTotal = 45.0,
                            activeEntryCount = 3,
                            savedEntryCount = 3,
                            recurringEntries = listOf(
                                savedEntry(id = 1L, name = "Rent", nextPaymentDate = "2026-03-14"),
                                savedEntry(id = 2L, name = "Water", nextPaymentDate = "2026-03-15"),
                                savedEntry(
                                    id = 3L,
                                    name = "Legacy import",
                                    nextPaymentDate = "17/03/2026"
                                )
                            ),
                            upcomingPayments = listOf(
                                upcomingPayment(
                                    id = 1L,
                                    name = "Rent",
                                    amount = 20.0,
                                    nextPaymentDate = "2026-03-14",
                                    category = "Housing",
                                    relativeDueContext = DashboardRelativeDueContext.Overdue(
                                        daysOverdue = 1
                                    )
                                ),
                                upcomingPayment(
                                    id = 2L,
                                    name = "Water",
                                    amount = 10.0,
                                    nextPaymentDate = "2026-03-15",
                                    relativeDueContext = DashboardRelativeDueContext.DueToday
                                ),
                                upcomingPayment(
                                    id = 3L,
                                    name = "Legacy import",
                                    amount = 15.0,
                                    nextPaymentDate = "17/03/2026",
                                    category = "Other",
                                    relativeDueContext = DashboardRelativeDueContext.DueTomorrow
                                )
                            )
                )
            )

            composeRule.assertSingleUpcomingPaymentRow(
                "Rent, \$20.00, Mar 14, 2026, Housing, USD"
            )
            composeRule.assertSingleUpcomingPaymentRow(
                "Water, \$10.00, Mar 15, 2026, Utilities, USD"
            )
            composeRule.assertSingleUpcomingPaymentRow(
                "Legacy import, \$15.00, 17/03/2026, Other, USD, Due tomorrow"
            )
        }
    }

    @Test
    fun dashboardScreen_upcomingPaymentRowClickDispatchesRecurringEntryActionAndKeepsUrgentSemantics() {
        withLocale(Locale.US) {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setDashboardContent(
                state = DashboardState(
                    isLoading = false,
                    monthlyRecurringTotal = 20.0,
                    activeEntryCount = 1,
                    savedEntryCount = 1,
                    recurringEntries = listOf(
                        savedEntry(id = 7L, name = "Rent", nextPaymentDate = "2026-03-14")
                    ),
                    upcomingPayments = listOf(
                        upcomingPayment(
                            id = 7L,
                            name = "Rent",
                            amount = 20.0,
                            nextPaymentDate = "2026-03-14",
                            category = "Housing",
                            relativeDueContext = DashboardRelativeDueContext.Overdue(
                                daysOverdue = 1
                            )
                        )
                    )
                ),
                onAction = actions::add
            )

            composeRule.assertSingleUpcomingPaymentRow(
                "Rent, \$20.00, Mar 14, 2026, Housing, USD"
            ).assert(hasClickAction())
                .assert(hasClickLabel("Open recurring entry details"))
                .assert(hasStateDescription("Overdue payment"))
                .assert(hasUpcomingPaymentUrgency("OVERDUE"))
                .performClick()

            assertEquals(listOf(DashboardAction.RecurringEntryClicked(7L)), actions)
        }
    }

    @Test
    fun dashboardScreen_savedRecurringEntryCardExposesMergedAccessibilitySummaryAndClickAction() {
        withLocale(Locale.US) {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setDashboardContent(
                state = DashboardState(
                    isLoading = false,
                    monthlyRecurringTotal = 15.99,
                    activeEntryCount = 1,
                    savedEntryCount = 1,
                    recurringEntries = listOf(
                        savedEntry(
                            id = 5L,
                            name = "Netflix",
                            amount = 15.99,
                            nextPaymentDate = "2026-03-31",
                            category = "Streaming",
                            type = RecurringEntryType.SUBSCRIPTION,
                            notes = "Family plan"
                        )
                    )
                ),
                onAction = actions::add
            )

            composeRule.assertSingleSavedRecurringEntryCard(
                "Netflix, \$15.99, Subscription, Monthly, Mar 31, 2026, Streaming, USD, Active, Notes: Family plan"
            ).assert(hasClickAction())
                .assert(hasStateDescription("Active"))
                .assert(hasClickLabel("Edit saved recurring entry"))
                .performClick()

            assertEquals(listOf(DashboardAction.RecurringEntryClicked(5L)), actions)
        }
    }

    @Test
    fun dashboardScreen_summaryCardExposesMergedAccessibilitySummary() {
        withLocale(Locale.US) {
            composeRule.setDashboardContent(
                state = DashboardState(
                    isLoading = false,
                    monthlyRecurringTotal = 29.98,
                    activeEntryCount = 2,
                    activeCurrencyCodes = setOf("EUR", "JPY"),
                    savedEntryCount = 2,
                    currencyMetadataCount = 2
                )
            )

            composeRule.assertSingleStaticDashboardCard(
                "Monthly recurring total. 29.98. Across 2 active entries. Includes 2 active currencies. Total is an unconverted aggregate. Currency metadata cache size: 2"
            )
        }
    }

    @Test
    fun dashboardScreen_summaryCardKeepsRetryActionAvailableWhenCurrencyMetadataFails() {
        withLocale(Locale.US) {
            val actions = mutableListOf<DashboardAction>()

            composeRule.setDashboardContent(
                state = DashboardState(
                    isLoading = false,
                    monthlyRecurringTotal = 15.99,
                    activeEntryCount = 1,
                    activeCurrencyCodes = setOf("USD"),
                    savedEntryCount = 1,
                    hasCurrencySyncFailure = true
                ),
                onAction = actions::add
            )

            composeRule.assertSingleStaticDashboardCard(
                "Monthly recurring total. \$15.99. Across 1 active entries. Currency metadata unavailable right now."
            )
            composeRule.onNodeWithText("Retry currency sync")
                .assert(hasClickAction())
                .assert(hasClickLabel("Retry currency metadata sync"))
                .performClick()

            assertEquals(listOf(DashboardAction.RetryCurrencyMetadataClicked), actions)
        }
    }

    @Test
    fun dashboardScreen_emptyStateCardExposesMergedAccessibilitySummary() {
        composeRule.setDashboardContent(
            state = DashboardState(
                isLoading = false
            )
        )

        composeRule.assertSingleStaticDashboardCard(
            "No recurring entries yet. Add your first subscription or recurring expense to start tracking upcoming payments and monthly totals."
        )
    }

    @Test
    fun dashboardScreen_upcomingPaymentsEmptyStateExposesMergedAccessibilitySummary() {
        composeRule.setDashboardContent(
            state = DashboardState(
                isLoading = false,
                monthlyRecurringTotal = 15.99,
                activeEntryCount = 1,
                savedEntryCount = 1,
                recurringEntries = listOf(
                    savedEntry(
                        id = 1L,
                        name = "Streaming",
                        nextPaymentDate = "2026-03-31"
                    )
                ),
                upcomingPayments = emptyList()
            )
        )

        composeRule.assertSingleStaticDashboardCard(
            "No upcoming payments yet. Add or update an entry with a next payment date to populate this section."
        )
    }

    @Test
    fun dashboardScreen_addRecurringEntryButtonExposesAccessibilityActionLabelAndDispatchesAction() {
        val actions = mutableListOf<DashboardAction>()

        composeRule.setDashboardContent(
            state = DashboardState(isLoading = false),
            onAction = actions::add
        )

        composeRule.onNodeWithText("Add recurring entry")
            .assert(hasClickAction())
            .assert(hasClickLabel("Add a new recurring entry"))
            .performClick()

        assertEquals(listOf(DashboardAction.AddRecurringEntryClicked), actions)
    }

    @Test
    fun dashboardScreen_loadingStateExposesAccessibilityStateDescriptionWithoutChangingVisibleCopy() {
        composeRule.setDashboardContent(
            state = DashboardState(isLoading = true)
        )

        composeRule.onNodeWithText("Loading recurring overview...")
            .assert(hasStateDescription("Dashboard content is loading"))
    }

    private fun savedEntry(
        id: Long,
        name: String,
        nextPaymentDate: String,
        amount: Double = 10.0,
        currencyCode: String = "USD",
        category: String = "Utilities",
        type: RecurringEntryType = RecurringEntryType.RECURRING_EXPENSE,
        isActive: Boolean = true,
        notes: String? = null
    ): DashboardRecurringEntryItem = DashboardRecurringEntryItem(
        id = id,
        name = name,
        amount = amount,
        currencyCode = currencyCode,
        billingFrequency = BillingFrequency.MONTHLY,
        nextPaymentDate = nextPaymentDate,
        category = category,
        type = type,
        isActive = isActive,
        notes = notes
    )

    private fun upcomingPayment(
        id: Long,
        name: String,
        amount: Double = 10.0,
        currencyCode: String = "USD",
        nextPaymentDate: String,
        category: String = "Utilities",
        relativeDueContext: DashboardRelativeDueContext = DashboardRelativeDueContext.None
    ): DashboardUpcomingPaymentItem = DashboardUpcomingPaymentItem(
        id = id,
        name = name,
        amount = amount,
        currencyCode = currencyCode,
        nextPaymentDate = nextPaymentDate,
        category = category,
        relativeDueContext = relativeDueContext
    )

    private fun ComposeContentTestRule.setDashboardContent(
        state: DashboardState,
        onAction: (DashboardAction) -> Unit = {}
    ) {
        setContent {
            FinanceTrackerTheme {
                DashboardScreen(
                    state = state,
                    onAction = onAction,
                    snackbarHostState = remember { SnackbarHostState() }
                )
            }
        }
    }

    private fun withLocale(locale: Locale, block: () -> Unit) {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(locale)

        try {
            block()
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    private fun hasUpcomingPaymentUrgency(value: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(UpcomingPaymentUrgencySemanticsKey, value)

    private fun ComposeContentTestRule.assertSingleUpcomingPaymentRow(
        contentDescription: String
    ): SemanticsNodeInteraction {
        onAllNodes(hasContentDescription(contentDescription)).assertCountEquals(1)
        return upcomingPaymentRow(contentDescription)
    }

    private fun ComposeContentTestRule.upcomingPaymentRow(
        contentDescription: String
    ): SemanticsNodeInteraction = onNode(hasContentDescription(contentDescription))

    private fun ComposeContentTestRule.assertSingleSavedRecurringEntryCard(
        contentDescription: String
    ): SemanticsNodeInteraction {
        onAllNodes(hasContentDescription(contentDescription)).assertCountEquals(1)
        return savedRecurringEntryCard(contentDescription)
    }

    private fun ComposeContentTestRule.savedRecurringEntryCard(
        contentDescription: String
    ): SemanticsNodeInteraction = onNode(hasContentDescription(contentDescription))

    private fun ComposeContentTestRule.assertSingleStaticDashboardCard(
        contentDescription: String
    ): SemanticsNodeInteraction {
        onAllNodes(hasContentDescription(contentDescription)).assertCountEquals(1)
        return staticDashboardCard(contentDescription)
    }

    private fun ComposeContentTestRule.staticDashboardCard(
        contentDescription: String
    ): SemanticsNodeInteraction = onNode(hasContentDescription(contentDescription))

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
