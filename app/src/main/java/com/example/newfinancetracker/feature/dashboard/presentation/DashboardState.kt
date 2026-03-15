package com.example.newfinancetracker.feature.dashboard.presentation

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

data class DashboardState(
    val isLoading: Boolean = true,
    val monthlyRecurringTotal: Double = 0.0,
    val activeEntryCount: Int = 0,
    val savedEntryCount: Int = 0,
    val recurringEntries: List<DashboardRecurringEntryItem> = emptyList(),
    val upcomingPayments: List<DashboardUpcomingPaymentItem> = emptyList(),
    val currencyMetadataCount: Int = 0,
    val hasCurrencySyncFailure: Boolean = false,
    val isCurrencySyncInProgress: Boolean = false
) {
    val isEmpty: Boolean
        get() = !isLoading && recurringEntries.isEmpty()
}

data class DashboardRecurringEntryItem(
    val id: Long,
    val name: String,
    val amount: Double,
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
    val nextPaymentDate: String,
    val category: String
)

internal fun List<RecurringEntry>.toDashboardState(): DashboardState {
    val recurringEntryItems = map { entry ->
        DashboardRecurringEntryItem(
            id = entry.id,
            name = entry.name,
            amount = entry.amount,
            billingFrequency = entry.billingFrequency,
            nextPaymentDate = entry.nextPaymentDate,
            category = entry.category,
            type = entry.type,
            isActive = entry.isActive,
            notes = entry.notes?.trim()?.ifBlank { null }
        )
    }
    val activeEntries = filter { it.isActive }
    val upcomingPayments = activeEntries
        .mapNotNull { entry ->
            entry.nextPaymentDate.toIsoSortKey()?.let {
                DashboardUpcomingPaymentItem(
                    id = entry.id,
                    name = entry.name,
                    amount = entry.amount,
                    nextPaymentDate = entry.nextPaymentDate,
                    category = entry.category
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
        savedEntryCount = recurringEntryItems.size,
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
    try {
        SimpleDateFormat(ISO_DATE_PATTERN, Locale.US).apply {
            isLenient = false
        }.parse(trim())?.time
    } catch (_: ParseException) {
        null
    }

private const val ISO_DATE_PATTERN: String = "yyyy-MM-dd"
