package com.example.newfinancetracker.feature.recurring.presentation.create

import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

data class RecurringEntryCreateState(
    val form: RecurringEntryFormState = RecurringEntryFormState(),
    val isSaving: Boolean = false,
    val showValidationErrors: Boolean = false,
    val hasSaveError: Boolean = false
) {
    val canSubmit: Boolean
        get() = form.canSubmit
}
