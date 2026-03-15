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
    currentCode: String,
    preferFirstCachedOverDefault: Boolean
): RecurringEntryCurrencySelection {
    val normalizedCurrentCode = normalizeCurrencyCode(currentCode)
    val cachedOptions = cachedMetadata.map { metadata ->
        RecurringEntryCurrencyOption(
            code = normalizeCurrencyCode(metadata.code),
            displayName = metadata.displayName
        )
    }

    if (cachedOptions.isEmpty()) {
        val fallbackCode = normalizedCurrentCode.ifBlank { DEFAULT_CURRENCY_CODE }
        return RecurringEntryCurrencySelection(
            selectedCode = fallbackCode,
            options = listOf(RecurringEntryCurrencyOption(code = fallbackCode))
        )
    }

    val matchedCode = cachedOptions.firstOrNull { it.code == normalizedCurrentCode }?.code
    if (matchedCode != null) {
        return RecurringEntryCurrencySelection(
            selectedCode = matchedCode,
            options = cachedOptions
        )
    }

    if (normalizedCurrentCode.isBlank() ||
        (preferFirstCachedOverDefault && normalizedCurrentCode == DEFAULT_CURRENCY_CODE)
    ) {
        return RecurringEntryCurrencySelection(
            selectedCode = cachedOptions.first().code,
            options = cachedOptions
        )
    }

    return RecurringEntryCurrencySelection(
        selectedCode = normalizedCurrentCode,
        options = listOf(RecurringEntryCurrencyOption(code = normalizedCurrentCode)) + cachedOptions
    )
}

internal fun formatRecurringEntryCurrencyOption(option: RecurringEntryCurrencyOption): String =
    option.displayName?.let { "${option.code} - $it" } ?: option.code

private fun normalizeCurrencyCode(code: String): String = code.trim().uppercase(Locale.US)
