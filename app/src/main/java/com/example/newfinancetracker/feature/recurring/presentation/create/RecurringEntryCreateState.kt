package com.example.newfinancetracker.feature.recurring.presentation.create

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

data class RecurringEntryCreateState(
    val name: String = "",
    val amount: String = "",
    val category: String = "",
    val nextPaymentDate: String = "",
    val notes: String = "",
    val type: RecurringEntryType = RecurringEntryType.SUBSCRIPTION,
    val billingFrequency: BillingFrequency = BillingFrequency.MONTHLY,
    val isActive: Boolean = true,
    val isSaving: Boolean = false,
    val showValidationErrors: Boolean = false,
    val hasSaveError: Boolean = false
) {
    val shouldShowNameError: Boolean
        get() = showValidationErrors && name.isBlank()

    val shouldShowAmountError: Boolean
        get() = showValidationErrors && parseAmount(amount) == null

    val shouldShowCategoryError: Boolean
        get() = showValidationErrors && category.isBlank()

    val shouldShowDateError: Boolean
        get() = showValidationErrors && !isValidIsoDate(nextPaymentDate)

    val canSubmit: Boolean
        get() = name.isNotBlank() &&
            parseAmount(amount) != null &&
            category.isNotBlank() &&
            isValidIsoDate(nextPaymentDate)
}

internal fun parseAmount(rawAmount: String): Double? =
    rawAmount
        .trim()
        .takeIf { it.isNotEmpty() }
        ?.toDoubleOrNull()
        ?.takeIf { it > 0.0 }

internal fun isValidIsoDate(rawDate: String): Boolean {
    if (!ISO_DATE_REGEX.matches(rawDate.trim())) {
        return false
    }

    return try {
        SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
            isLenient = false
        }.parse(rawDate.trim())
        true
    } catch (_: ParseException) {
        false
    }
}

private const val ISO_DATE_PATTERN: String = "yyyy-MM-dd"
private val ISO_DATE_REGEX = Regex("""\d{4}-\d{2}-\d{2}""")
