package com.example.newfinancetracker.feature.dashboard.presentation

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.absoluteValue

data class DashboardState(
    val isLoading: Boolean = true,
    val monthlyRecurringTotal: Double = 0.0,
    val activeEntryCount: Int = 0,
    val activeCurrencyCodes: Set<String> = emptySet(),
    val savedEntryCount: Int = 0,
    val recurringEntries: List<DashboardRecurringEntryItem> = emptyList(),
    val upcomingPayments: List<DashboardUpcomingPaymentItem> = emptyList(),
    val currencyMetadataCount: Int = 0,
    val hasCurrencySyncFailure: Boolean = false,
    val isCurrencySyncInProgress: Boolean = false
) {
    val isEmpty: Boolean
        get() = !isLoading && savedEntryCount == 0

    val hasMixedActiveCurrencies: Boolean
        get() = activeCurrencyCodes.size > 1
}

data class DashboardRecurringEntryItem(
    val id: Long,
    val name: String,
    val amount: Double,
    val currencyCode: String,
    val billingFrequency: BillingFrequency,
    val nextPaymentDate: String,
    val category: String,
    val type: RecurringEntryType,
    val isActive: Boolean,
    val notes: String?
)

data class DashboardUpcomingPaymentItem(
    val id: Long,
    val name: String,
    val amount: Double,
    val currencyCode: String,
    val nextPaymentDate: String,
    val category: String,
    val relativeDueContext: DashboardRelativeDueContext? = null
)

sealed interface DashboardRelativeDueContext {
    data class Overdue(val daysOverdue: Int) : DashboardRelativeDueContext
    data object DueToday : DashboardRelativeDueContext
    data object DueTomorrow : DashboardRelativeDueContext
    data class DueInDays(val daysUntilDue: Int) : DashboardRelativeDueContext
}

internal enum class DashboardUpcomingPaymentUrgency {
    STANDARD,
    DUE_TODAY,
    OVERDUE
}

internal fun DashboardRelativeDueContext?.toUpcomingPaymentUrgency(): DashboardUpcomingPaymentUrgency =
    when (this) {
        is DashboardRelativeDueContext.Overdue -> DashboardUpcomingPaymentUrgency.OVERDUE
        DashboardRelativeDueContext.DueToday -> DashboardUpcomingPaymentUrgency.DUE_TODAY
        else -> DashboardUpcomingPaymentUrgency.STANDARD
    }

internal fun List<RecurringEntry>.toDashboardState(
    referenceDate: Date = Date()
): DashboardState {
    val activeEntries = filter { it.isActive }
    val recurringEntryItems = activeEntries.map { entry ->
        DashboardRecurringEntryItem(
            id = entry.id,
            name = entry.name,
            amount = entry.amount,
            currencyCode = entry.currencyCode,
            billingFrequency = entry.billingFrequency,
            nextPaymentDate = entry.nextPaymentDate,
            category = entry.category,
            type = entry.type,
            isActive = entry.isActive,
            notes = entry.notes?.trim()?.ifBlank { null }
        )
    }
    val upcomingPayments = activeEntries
        .mapNotNull { entry ->
            entry.nextPaymentDate.toIsoSortKey()?.let {
                DashboardUpcomingPaymentItem(
                    id = entry.id,
                    name = entry.name,
                    amount = entry.amount,
                    currencyCode = entry.currencyCode,
                    nextPaymentDate = entry.nextPaymentDate,
                    category = entry.category,
                    relativeDueContext = entry.nextPaymentDate.toDashboardRelativeDueContext(referenceDate)
                )
            }
        }
        .sortedBy { item -> item.nextPaymentDate.toIsoSortKey() }

    return DashboardState(
        isLoading = false,
        monthlyRecurringTotal = activeEntries.sumOf { entry ->
            entry.amount.toMonthlyAmount(entry.billingFrequency)
        },
        activeEntryCount = activeEntries.size,
        activeCurrencyCodes = activeEntries.map { it.currencyCode }.toSet(),
        savedEntryCount = size,
        recurringEntries = recurringEntryItems,
        upcomingPayments = upcomingPayments
    )
}

private fun Double.toMonthlyAmount(
    billingFrequency: BillingFrequency
): Double =
    when (billingFrequency) {
        BillingFrequency.WEEKLY -> this * 52.0 / 12.0
        BillingFrequency.MONTHLY -> this
        BillingFrequency.QUARTERLY -> this / 3.0
        BillingFrequency.YEARLY -> this / 12.0
    }

private fun String.toIsoSortKey(): Long? =
    toIsoDate()?.time

internal fun String.toDashboardDisplayDate(
    locale: Locale = Locale.getDefault()
): String {
    val parsedDate = toIsoDate() ?: return this
    return SimpleDateFormat(DASHBOARD_DISPLAY_DATE_PATTERN, locale).apply {
        timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE_ID)
    }.format(parsedDate)
}

internal fun String.toDashboardRelativeDueContext(
    referenceDate: Date = Date()
): DashboardRelativeDueContext? {
    val dueDate = toIsoDate() ?: return null
    val dayDelta = ((dueDate.time - referenceDate.toUtcStartOfDayMillis()) / MILLIS_PER_DAY).toInt()

    return when {
        dayDelta < 0 -> DashboardRelativeDueContext.Overdue(daysOverdue = dayDelta.absoluteValue)
        dayDelta == 0 -> DashboardRelativeDueContext.DueToday
        dayDelta == 1 -> DashboardRelativeDueContext.DueTomorrow
        else -> DashboardRelativeDueContext.DueInDays(daysUntilDue = dayDelta)
    }
}

private fun String.toIsoDate(): Date? =
    try {
        SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE_ID)
        }.parse(trim())
    } catch (_: ParseException) {
        null
    }

private fun Date.toUtcStartOfDayMillis(): Long {
    val dateOnly = SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
        timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE_ID)
    }.format(this)
    return checkNotNull(
        SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE_ID)
        }.parse(dateOnly)
    ).time
}

private const val ISO_DATE_PATTERN: String = "yyyy-MM-dd"
private const val DASHBOARD_DISPLAY_DATE_PATTERN: String = "MMM d, yyyy"
private const val UTC_TIME_ZONE_ID: String = "UTC"
private const val MILLIS_PER_DAY: Long = 24L * 60L * 60L * 1000L
