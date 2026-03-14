package com.example.newfinancetracker.feature.recurring.domain.model

data class RecurringEntry(
    val id: Long = 0L,
    val name: String,
    val amount: Double,
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
