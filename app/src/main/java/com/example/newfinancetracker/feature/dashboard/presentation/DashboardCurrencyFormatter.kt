package com.example.newfinancetracker.feature.dashboard.presentation

import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

internal fun formatAmountForSavedCurrency(
    amount: Double,
    currencyCode: String,
    locale: Locale = Locale.getDefault()
): String {
    val normalizedCode = currencyCode.trim().uppercase(Locale.US)
    val resolvedCurrency = normalizedCode.toCurrencyOrNull() ?: return fallbackCurrencyAmount(
        amount = amount,
        currencyCode = normalizedCode.ifBlank { DEFAULT_CURRENCY_CODE },
        locale = locale
    )

    return NumberFormat.getCurrencyInstance(locale).apply {
        currency = resolvedCurrency
        resolvedCurrency.defaultFractionDigits
            .takeIf { it >= 0 }
            ?.let { fractionDigits ->
                minimumFractionDigits = fractionDigits
                maximumFractionDigits = fractionDigits
            }
    }.format(amount)
}

internal fun formatSummaryAmount(
    amount: Double,
    hasMixedCurrencies: Boolean,
    locale: Locale = Locale.getDefault()
): String =
    if (hasMixedCurrencies) {
        NumberFormat.getNumberInstance(locale).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(amount)
    } else {
        NumberFormat.getCurrencyInstance(locale).format(amount)
    }

private fun String.toCurrencyOrNull(): Currency? =
    runCatching {
        Currency.getInstance(this)
    }.getOrNull()

private fun fallbackCurrencyAmount(
    amount: Double,
    currencyCode: String,
    locale: Locale
): String {
    val numberText = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(amount)

    return "$currencyCode $numberText"
}
