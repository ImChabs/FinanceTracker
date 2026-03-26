package com.example.newfinancetracker.feature.recurring.presentation.form

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecurringEntryFormStateTest {

    @Test
    fun `domain entry maps to edit form fields`() {
        val formState = RecurringEntry(
            id = 42L,
            name = "Gym Membership",
            amount = 29.99,
            currencyCode = "USD",
            billingFrequency = BillingFrequency.MONTHLY,
            nextPaymentDate = "2026-05-10",
            category = "Health",
            type = RecurringEntryType.SUBSCRIPTION,
            isActive = false,
            notes = "Bring access card"
        ).toFormState()

        assertEquals(
            RecurringEntryFormState(
                name = "Gym Membership",
                amount = "29.99",
                currencyCode = "USD",
                nextPaymentDate = "2026-05-10",
                notes = "Bring access card",
                type = RecurringEntryType.SUBSCRIPTION,
                billingFrequency = BillingFrequency.MONTHLY,
                isActive = false
            ),
            formState
        )
    }

    @Test
    fun `form state trims fields and preserves id when converting back to domain`() {
        val recurringEntry = RecurringEntryFormState(
            name = "  Rent  ",
            amount = "1,450.00",
            currencyCode = " pyg ",
            nextPaymentDate = "2026-04-01",
            notes = "   ",
            type = RecurringEntryType.RECURRING_EXPENSE,
            billingFrequency = BillingFrequency.MONTHLY,
            isActive = true
        ).toRecurringEntry(id = 9L)

        assertEquals(9L, recurringEntry.id)
        assertEquals("Rent", recurringEntry.name)
        assertEquals(1450.0, recurringEntry.amount, 0.0)
        assertEquals("PYG", recurringEntry.currencyCode)
        assertEquals("", recurringEntry.category)
        assertNull(recurringEntry.notes)
    }

    @Test
    fun `form state can submit when required fields are valid`() {
        val formState = RecurringEntryFormState(
            name = "Netflix",
            amount = "1,250.99",
            nextPaymentDate = "2026-03-31"
        )

        assertTrue(formState.canSubmit)
    }

    @Test
    fun `form state falls back to default currency when conversion receives blank code`() {
        val recurringEntry = RecurringEntryFormState(
            name = "Cloud Storage",
            amount = "4.99",
            currencyCode = "   ",
            nextPaymentDate = "2026-06-01"
        ).toRecurringEntry()

        assertEquals(DEFAULT_CURRENCY_CODE, recurringEntry.currencyCode)
    }

    @Test
    fun `form state falls back to default currency when conversion receives unsupported code`() {
        val recurringEntry = RecurringEntryFormState(
            name = "Cloud Storage",
            amount = "4.99",
            currencyCode = "eur",
            nextPaymentDate = "2026-06-01"
        ).toRecurringEntry()

        assertEquals(DEFAULT_CURRENCY_CODE, recurringEntry.currencyCode)
    }

    @Test
    fun `amount formatting adds grouping separators while preserving decimals`() {
        assertEquals("1,234,567.80", formatAmountForDisplay("1234567.80"))
    }

    @Test
    fun `date picker millis convert back to matching iso date`() {
        val pickerMillis = isoDateToPickerMillis("2026-04-01")

        assertEquals("2026-04-01", pickerMillis?.let(::pickerMillisToIsoDate))
    }
}
