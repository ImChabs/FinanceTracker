package com.example.newfinancetracker.feature.recurring.presentation.form

import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import java.util.Locale

data class RecurringEntryCurrencyOption(
    val code: String,
    val displayName: String? = null
)

data class RecurringEntryCurrencySelection(
    val selectedCode: String,
    val options: List<RecurringEntryCurrencyOption>
)

internal fun resolveRecurringEntryCurrencySelection(
    cachedMetadata: List<CurrencyMetadata>,
    currentCode: String
): RecurringEntryCurrencySelection {
    val normalizedCurrentCode = normalizeCurrencyCode(currentCode)
    val cachedMetadataByCode = cachedMetadata.associateBy { normalizeCurrencyCode(it.code) }
    val options = SUPPORTED_RECURRING_ENTRY_CURRENCIES.map { code ->
        RecurringEntryCurrencyOption(
            code = code,
            displayName = cachedMetadataByCode[code]?.displayName ?: recurringEntryCurrencyName(code)
        )
    }

    return RecurringEntryCurrencySelection(
        selectedCode = normalizedCurrentCode.takeIf(::isSupportedRecurringEntryCurrencyCode)
            ?: DEFAULT_CURRENCY_CODE,
        options = options
    )
}

internal fun formatRecurringEntryCurrencyOption(option: RecurringEntryCurrencyOption): String =
    option.displayName?.let { "${option.code} - $it" } ?: option.code

internal fun isSupportedRecurringEntryCurrencyCode(code: String): Boolean =
    normalizeCurrencyCode(code) in SUPPORTED_RECURRING_ENTRY_CURRENCIES

private fun normalizeCurrencyCode(code: String): String = code.trim().uppercase(Locale.US)

private fun recurringEntryCurrencyName(code: String): String =
    when (code) {
        "USD" -> "United States Dollar"
        "PYG" -> "Paraguayan Guarani"
        else -> code
    }

private val SUPPORTED_RECURRING_ENTRY_CURRENCIES = listOf("USD", "PYG")
