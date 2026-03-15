package com.example.newfinancetracker.feature.recurring.domain.model

const val DEFAULT_CURRENCY_CODE: String = "USD"

data class RecurringEntry(
    val id: Long = 0L,
    val name: String,
    val amount: Double,
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val billingFrequency: BillingFrequency,
    val nextPaymentDate: String,
    val category: String,
    val type: RecurringEntryType,
    val isActive: Boolean,
    val notes: String?
)

enum class RecurringEntryType {
    SUBSCRIPTION,
    RECURRING_EXPENSE
}

enum class BillingFrequency {
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    YEARLY
}
