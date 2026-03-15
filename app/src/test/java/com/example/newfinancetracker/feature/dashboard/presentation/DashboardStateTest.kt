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
        assertEquals(5, state.savedEntryCount)
        assertEquals(null, state.recurringEntries[3].notes)
    }

    @Test
    fun `dashboard state is empty when there are no saved entries`() {
        val state = emptyList<RecurringEntry>().toDashboardState()

        assertTrue(state.isEmpty)
        assertEquals(0.0, state.monthlyRecurringTotal, 0.0)
        assertEquals(0, state.activeEntryCount)
        assertEquals(0, state.savedEntryCount)
    }
}
