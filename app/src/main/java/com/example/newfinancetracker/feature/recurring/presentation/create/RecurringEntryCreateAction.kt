package com.example.newfinancetracker.feature.recurring.presentation.create

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

sealed interface RecurringEntryCreateAction {
    data class NameChanged(val value: String) : RecurringEntryCreateAction
    data class AmountChanged(val value: String) : RecurringEntryCreateAction
    data class CurrencyCodeChanged(val value: String) : RecurringEntryCreateAction
    data class NextPaymentDateChanged(val value: String) : RecurringEntryCreateAction
    data class NotesChanged(val value: String) : RecurringEntryCreateAction
    data class TypeChanged(val value: RecurringEntryType) : RecurringEntryCreateAction
    data class BillingFrequencyChanged(val value: BillingFrequency) : RecurringEntryCreateAction
    data class ActiveChanged(val value: Boolean) : RecurringEntryCreateAction
    data object SaveClicked : RecurringEntryCreateAction
}
