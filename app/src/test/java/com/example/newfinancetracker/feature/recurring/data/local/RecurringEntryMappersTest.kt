package com.example.newfinancetracker.feature.recurring.data.local

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import org.junit.Assert.assertEquals
import org.junit.Test

class RecurringEntryMappersTest {

    @Test
    fun `entity maps to matching domain model`() {
        val entity = RecurringEntryEntity(
            id = 7L,
            name = "Rent",
            amount = 1450.0,
            billingFrequency = BillingFrequency.MONTHLY,
            nextPaymentDate = "2026-04-01",
            category = "Housing",
            type = RecurringEntryType.RECURRING_EXPENSE,
            isActive = true,
            notes = "Auto-paid"
        )

        val domainModel = entity.toDomain()

        assertEquals(
            RecurringEntry(
                id = 7L,
                name = "Rent",
                amount = 1450.0,
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-04-01",
                category = "Housing",
                type = RecurringEntryType.RECURRING_EXPENSE,
                isActive = true,
                notes = "Auto-paid"
            ),
            domainModel
        )
    }

    @Test
    fun `domain model maps to matching entity`() {
        val domainModel = RecurringEntry(
            id = 11L,
            name = "Music Streaming",
            amount = 10.99,
            billingFrequency = BillingFrequency.MONTHLY,
            nextPaymentDate = "2026-03-25",
            category = "Entertainment",
            type = RecurringEntryType.SUBSCRIPTION,
            isActive = true,
            notes = null
        )

        val entity = domainModel.toEntity()

        assertEquals(
            RecurringEntryEntity(
                id = 11L,
                name = "Music Streaming",
                amount = 10.99,
                billingFrequency = BillingFrequency.MONTHLY,
                nextPaymentDate = "2026-03-25",
                category = "Entertainment",
                type = RecurringEntryType.SUBSCRIPTION,
                isActive = true,
                notes = null
            ),
            entity
        )
    }
}
