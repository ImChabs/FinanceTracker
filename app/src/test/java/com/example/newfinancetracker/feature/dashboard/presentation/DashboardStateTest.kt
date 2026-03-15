package com.example.newfinancetracker.feature.dashboard.presentation

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardStateTest {

    @Test
    fun `dashboard state calculates monthly total from active entries across billing frequencies`() {
        val state = listOf(
            RecurringEntry(
                id = 1L,
                name = "Streaming",
                amount = 20.0,
                currencyCode = "USD",
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-03-31",
                category = "Entertainment",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = true,
                notes = null
            ),
            RecurringEntry(
                id = 2L,
                name = "Weekly commute",
                amount = 30.0,
                currencyCode = "EUR",
                billingFrequency = BillingFrequency.WEEKLY,
                nextPaymentDate = "2026-03-18",
                category = "Transport",
                type = RecurringEntryType.RECURRING_EXPENSE,
                isActive = true,
                notes = null
            ),
            RecurringEntry(
                id = 3L,
                name = "Insurance",
                amount = 90.0,
                currencyCode = "JPY",
                billingFrequency = BillingFrequency.QUARTERLY,
                nextPaymentDate = "2026-04-01",
                category = "Insurance",
                type = RecurringEntryType.RECURRING_EXPENSE,
                isActive = true,
                notes = null
            ),
            RecurringEntry(
                id = 4L,
                name = "Domain renewal",
                amount = 120.0,
                currencyCode = "GBP",
                billingFrequency = BillingFrequency.YEARLY,
                nextPaymentDate = "2026-08-12",
                category = "Software",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = true,
                notes = "  "
            ),
            RecurringEntry(
                id = 5L,
                name = "Archived membership",
                amount = 999.0,
                currencyCode = "CAD",
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-03-20",
                category = "Health",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = false,
                notes = "Paused"
            )
        ).toDashboardState()

        assertEquals(190.0, state.monthlyRecurringTotal, 0.0001)
        assertEquals(4, state.activeEntryCount)
        assertEquals(setOf("USD", "EUR", "JPY", "GBP"), state.activeCurrencyCodes)
        assertTrue(state.hasMixedActiveCurrencies)
        assertEquals(5, state.savedEntryCount)
        assertEquals(
            listOf("2026-03-18", "2026-03-31", "2026-04-01", "2026-08-12"),
            state.upcomingPayments.map { it.nextPaymentDate }
        )
        assertEquals(
            listOf("USD", "EUR", "JPY", "GBP", "CAD"),
            state.recurringEntries.map { it.currencyCode }
        )
        assertEquals(
            listOf("EUR", "USD", "JPY", "GBP"),
            state.upcomingPayments.map { it.currencyCode }
        )
        assertEquals(null, state.recurringEntries[3].notes)
    }

    @Test
    fun `dashboard state is empty when there are no saved entries`() {
        val state = emptyList<RecurringEntry>().toDashboardState()

        assertTrue(state.isEmpty)
        assertEquals(0.0, state.monthlyRecurringTotal, 0.0)
        assertEquals(0, state.activeEntryCount)
        assertTrue(state.activeCurrencyCodes.isEmpty())
        assertEquals(0, state.savedEntryCount)
        assertTrue(state.upcomingPayments.isEmpty())
    }

    @Test
    fun `dashboard upcoming payments only include active entries with valid dates`() {
        val state = listOf(
            RecurringEntry(
                id = 1L,
                name = "Water",
                amount = 45.0,
                currencyCode = "AUD",
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-03-19",
                category = "Utilities",
                type = RecurringEntryType.RECURRING_EXPENSE,
                isActive = true,
                notes = null
            ),
            RecurringEntry(
                id = 2L,
                name = "Legacy entry",
                amount = 12.0,
                currencyCode = "USD",
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-13-01",
                category = "Other",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = true,
                notes = null
            ),
            RecurringEntry(
                id = 3L,
                name = "Paused service",
                amount = 8.0,
                currencyCode = "CHF",
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-03-16",
                category = "Software",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = false,
                notes = null
            )
        ).toDashboardState()

        assertEquals(1, state.upcomingPayments.size)
        assertEquals("Water", state.upcomingPayments.single().name)
        assertEquals("AUD", state.upcomingPayments.single().currencyCode)
        assertEquals(setOf("AUD", "USD"), state.activeCurrencyCodes)
        assertTrue(state.hasMixedActiveCurrencies)
    }
}
