package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState
import com.example.newfinancetracker.feature.recurring.presentation.form.toFormState
import com.example.newfinancetracker.feature.recurring.presentation.form.toRecurringEntry
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecurringEntryEditViewModel(
    private val entryId: Long,
    private val recurringEntryRepository: RecurringEntryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringEntryEditState(entryId = entryId))
    val state: StateFlow<RecurringEntryEditState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<RecurringEntryEditEffect>()
    val effects: SharedFlow<RecurringEntryEditEffect> = _effects.asSharedFlow()

    init {
        loadRecurringEntry()
    }

    fun onAction(action: RecurringEntryEditAction) {
        when (action) {
            is RecurringEntryEditAction.ActiveChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(isActive = action.value)
                    )
                }
            }

            is RecurringEntryEditAction.AmountChanged -> {
                updateFormState { currentState ->
                    currentState.copy(amount = action.value)
                }
            }

            is RecurringEntryEditAction.BillingFrequencyChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(billingFrequency = action.value)
                    )
                }
            }

            is RecurringEntryEditAction.CategoryChanged -> {
                updateFormState { currentState ->
                    currentState.copy(category = action.value)
                }
            }

            RecurringEntryEditAction.DeleteClicked -> {
                _state.update { currentState ->
                    if (!currentState.canDelete) {
                        currentState
                    } else {
                        currentState.copy(
                            isDeleteConfirmationVisible = true,
                            hasDeleteError = false
                        )
                    }
                }
            }

            RecurringEntryEditAction.DeleteConfirmed -> deleteRecurringEntry()

            RecurringEntryEditAction.DeleteDismissed -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteConfirmationVisible = false)
                }
            }

            is RecurringEntryEditAction.NameChanged -> {
                updateFormState { currentState ->
                    currentState.copy(name = action.value)
                }
            }

            is RecurringEntryEditAction.NextPaymentDateChanged -> {
                updateFormState { currentState ->
                    currentState.copy(nextPaymentDate = action.value)
                }
            }

            is RecurringEntryEditAction.NotesChanged -> {
                updateFormState { currentState ->
                    currentState.copy(notes = action.value)
                }
            }

            RecurringEntryEditAction.SaveClicked -> saveRecurringEntry()

            is RecurringEntryEditAction.TypeChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(type = action.value)
                    )
                }
            }
        }
    }

    private fun updateFormState(
        transform: (RecurringEntryFormState) -> RecurringEntryFormState
    ) {
        _state.update { currentState ->
            currentState.copy(
                form = transform(currentState.form),
                hasSaveError = false,
                hasDeleteError = false
            )
        }
    }

    private fun loadRecurringEntry() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    hasSaveError = false,
                    hasDeleteError = false,
                    isMissingEntry = false,
                    isDeleteConfirmationVisible = false
                )
            }

            val entry = runCatching {
                recurringEntryRepository.getRecurringEntry(entryId)
            }.getOrNull()

            _state.update { currentState ->
                if (entry == null) {
                    currentState.copy(
                        isLoading = false,
                        isMissingEntry = true
                    )
                } else {
                    currentState.copy(
                        form = entry.toFormState(),
                        isLoading = false,
                        isMissingEntry = false,
                        isDeleteConfirmationVisible = false
                    )
                }
            }
        }
    }

    private fun saveRecurringEntry() {
        val currentState = _state.value.copy(showValidationErrors = true, hasSaveError = false)
        _state.value = currentState

        if (!currentState.canSubmit || currentState.isSaving) {
            return
        }

        viewModelScope.launch {
            _state.update { state ->
                state.copy(isSaving = true, hasSaveError = false)
            }

            runCatching {
                recurringEntryRepository.upsertRecurringEntry(
                    currentState.form.toRecurringEntry(id = entryId)
                )
            }.onSuccess {
                _state.update { state ->
                    state.copy(isSaving = false)
                }
                _effects.emit(RecurringEntryEditEffect.NavigateBack)
            }.onFailure {
                _state.update { state ->
                    state.copy(
                        isSaving = false,
                        hasSaveError = true
                    )
                }
            }
        }
    }

    private fun deleteRecurringEntry() {
        val currentState = _state.value
        if (!currentState.canDelete) {
            return
        }

        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isDeleting = true,
                    hasDeleteError = false,
                    isDeleteConfirmationVisible = false
                )
            }

            runCatching {
                recurringEntryRepository.deleteRecurringEntry(entryId)
            }.onSuccess {
                _state.update { state ->
                    state.copy(isDeleting = false)
                }
                _effects.emit(RecurringEntryEditEffect.NavigateBack)
            }.onFailure {
                _state.update { state ->
                    state.copy(
                        isDeleting = false,
                        hasDeleteError = true
                    )
                }
            }
        }
    }

    companion object {
        fun factory(
            entryId: Long,
            recurringEntryRepository: RecurringEntryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(RecurringEntryEditViewModel::class.java))
                return RecurringEntryEditViewModel(
                    entryId = entryId,
                    recurringEntryRepository = recurringEntryRepository
                ) as T
            }
        }
    }
}
