package com.example.newfinancetracker.feature.recurring.presentation.create

import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryCurrencyOption
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

data class RecurringEntryCreateState(
    val form: RecurringEntryFormState = RecurringEntryFormState(),
    val currencyOptions: List<RecurringEntryCurrencyOption> = recurringEntryDefaultCurrencyOptions(),
    val isSaving: Boolean = false,
    val showValidationErrors: Boolean = false,
    val hasSaveError: Boolean = false
) {
    val canSubmit: Boolean
        get() = form.canSubmit
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
