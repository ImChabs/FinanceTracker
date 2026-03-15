package com.example.newfinancetracker.feature.recurring.presentation.edit

import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

sealed interface RecurringEntryEditAction {
    data class NameChanged(val value: String) : RecurringEntryEditAction
    data class AmountChanged(val value: String) : RecurringEntryEditAction
    data class CategoryChanged(val value: String) : RecurringEntryEditAction
    data class NextPaymentDateChanged(val value: String) : RecurringEntryEditAction
    data class NotesChanged(val value: String) : RecurringEntryEditAction
    data class TypeChanged(val value: RecurringEntryType) : RecurringEntryEditAction
    data class BillingFrequencyChanged(val value: BillingFrequency) : RecurringEntryEditAction
    data class ActiveChanged(val value: Boolean) : RecurringEntryEditAction
    data object SaveClicked : RecurringEntryEditAction
    data object DeleteClicked : RecurringEntryEditAction
    data object DeleteDismissed : RecurringEntryEditAction
    data object DeleteConfirmed : RecurringEntryEditAction
}
