package com.example.newfinancetracker.feature.recurring.presentation.form

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import java.text.DateFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class RecurringEntryFormState(
    val name: String = "",
    val amount: String = "",
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val nextPaymentDate: String = "",
    val notes: String = "",
    val type: RecurringEntryType = RecurringEntryType.SUBSCRIPTION,
    val billingFrequency: BillingFrequency = BillingFrequency.MONTHLY,
    val isActive: Boolean = true
) {
    val canSubmit: Boolean
        get() = name.isNotBlank() &&
            parseAmount(amount) != null &&
            currencyCode.isNotBlank() &&
            isValidIsoDate(nextPaymentDate)
}

internal fun RecurringEntry.toFormState(): RecurringEntryFormState =
    RecurringEntryFormState(
        name = name,
        amount = amount.toString(),
        currencyCode = currencyCode,
        nextPaymentDate = nextPaymentDate,
        notes = notes.orEmpty(),
        type = type,
        billingFrequency = billingFrequency,
        isActive = isActive
    )

internal fun RecurringEntryFormState.toRecurringEntry(id: Long = 0L): RecurringEntry =
    RecurringEntry(
        id = id,
        name = name.trim(),
        amount = requireNotNull(parseAmount(amount)),
        currencyCode = currencyCode
            .trim()
            .uppercase(Locale.US)
            .takeIf(::isSupportedRecurringEntryCurrencyCode)
            ?: DEFAULT_CURRENCY_CODE,
        billingFrequency = billingFrequency,
        nextPaymentDate = nextPaymentDate.trim(),
        category = DEFAULT_RECURRING_ENTRY_CATEGORY,
        type = type,
        isActive = isActive,
        notes = notes.trim().ifBlank { null }
    )

internal fun parseAmount(rawAmount: String): Double? =
    sanitizeAmountInput(rawAmount)
        .takeIf { it.isNotEmpty() }
        ?.toDoubleOrNull()
        ?.takeIf { it > 0.0 }

internal fun sanitizeAmountInput(rawAmount: String): String {
    val trimmedAmount = rawAmount.trim()
    if (trimmedAmount.isEmpty()) {
        return ""
    }

    val decimalSeparator = DecimalFormatSymbols.getInstance(Locale.US).decimalSeparator
    val sanitized = StringBuilder()
    var hasDecimalSeparator = false

    trimmedAmount.forEach { character ->
        when {
            character.isDigit() -> sanitized.append(character)
            character == decimalSeparator && !hasDecimalSeparator -> {
                sanitized.append(character)
                hasDecimalSeparator = true
            }
        }
    }

    return sanitized.toString()
}

internal fun formatAmountForDisplay(rawAmount: String): String {
    val sanitizedAmount = sanitizeAmountInput(rawAmount)
    if (sanitizedAmount.isEmpty()) {
        return ""
    }

    val hasDecimalSeparator = sanitizedAmount.contains('.')
    val parts = sanitizedAmount.split('.', limit = 2)
    val integerPart = parts.firstOrNull().orEmpty()
    val fractionPart = parts.getOrNull(1).orEmpty()
    val normalizedIntegerPart = integerPart.trimStart('0').ifEmpty {
        if (integerPart.isNotEmpty()) "0" else ""
    }

    val formattedIntegerPart = formatIntegerPartWithGrouping(normalizedIntegerPart)
    return buildString {
        append(formattedIntegerPart)
        if (hasDecimalSeparator) {
            append('.')
            append(fractionPart)
        }
    }
}

internal fun isValidIsoDate(rawDate: String): Boolean {
    if (!ISO_DATE_REGEX.matches(rawDate.trim())) {
        return false
    }

    return try {
        isoDateFormatter().parse(rawDate.trim())
        true
    } catch (_: ParseException) {
        false
    }
}

internal fun formatIsoDateForDisplay(rawDate: String): String =
    parseIsoDate(rawDate)?.let { date ->
        DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).apply {
            timeZone = UTC_TIME_ZONE
        }.format(date)
    }.orEmpty()

internal fun isoDateToPickerMillis(rawDate: String): Long? = parseIsoDate(rawDate)?.time

internal fun pickerMillisToIsoDate(utcTimeMillis: Long): String =
    isoDateFormatter().format(Date(utcTimeMillis))

private fun formatIntegerPartWithGrouping(integerPart: String): String {
    if (integerPart.isEmpty()) {
        return ""
    }

    val reversedDigits = integerPart.reversed()
    return buildString(integerPart.length + (integerPart.length / 3)) {
        reversedDigits.forEachIndexed { index, character ->
            if (index > 0 && index % 3 == 0) {
                append(',')
            }
            append(character)
        }
    }.reversed()
}

private fun parseIsoDate(rawDate: String): Date? =
    runCatching { isoDateFormatter().parse(rawDate.trim()) }.getOrNull()

private fun isoDateFormatter(): SimpleDateFormat =
    SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
        isLenient = false
        timeZone = UTC_TIME_ZONE
    }

private const val ISO_DATE_PATTERN: String = "yyyy-MM-dd"
private const val DEFAULT_RECURRING_ENTRY_CATEGORY: String = ""
private val ISO_DATE_REGEX = Regex("""\d{4}-\d{2}-\d{2}""")
private val UTC_TIME_ZONE: TimeZone = TimeZone.getTimeZone("UTC")
