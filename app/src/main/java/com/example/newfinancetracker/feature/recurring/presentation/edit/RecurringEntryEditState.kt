package com.example.newfinancetracker.feature.recurring.presentation.edit

import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryCurrencyOption
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

data class RecurringEntryEditState(
    val entryId: Long,
    val form: RecurringEntryFormState = RecurringEntryFormState(),
    val currencyOptions: List<RecurringEntryCurrencyOption> = recurringEntryDefaultCurrencyOptions(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showValidationErrors: Boolean = false,
    val hasSaveError: Boolean = false,
    val hasDeleteError: Boolean = false,
    val isMissingEntry: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false
) {
    val canSubmit: Boolean
        get() = !isLoading && !isMissingEntry && !isDeleting && form.canSubmit

    val canDelete: Boolean
        get() = !isLoading && !isMissingEntry && !isSaving && !isDeleting
}

private fun recurringEntryDefaultCurrencyOptions(): List<RecurringEntryCurrencyOption> = listOf(
    RecurringEntryCurrencyOption(
        code = DEFAULT_CURRENCY_CODE,
        displayName = "United States Dollar"
    ),
    RecurringEntryCurrencyOption(
        code = "PYG",
        displayName = "Paraguayan Guarani"
    )
)
