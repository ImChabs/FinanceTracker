package com.example.newfinancetracker.feature.dashboard.presentation

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class DashboardCurrencyFormatterTest {

    @Test
    fun `formats supported saved currency codes with their symbol`() {
        assertEquals(
            "€19.99",
            formatAmountForSavedCurrency(
                amount = 19.99,
                currencyCode = "EUR",
                locale = Locale.US
            )
        )
        assertEquals(
            "¥1,200",
            formatAmountForSavedCurrency(
                amount = 1200.0,
                currencyCode = "JPY",
                locale = Locale.US
            )
        )
    }

    @Test
    fun `falls back predictably when saved currency code is invalid`() {
        assertEquals(
            "US 42.50",
            formatAmountForSavedCurrency(
                amount = 42.5,
                currencyCode = "US",
                locale = Locale.US
            )
        )
    }
}
