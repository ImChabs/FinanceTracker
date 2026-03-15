package com.example.newfinancetracker.feature.recurring.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecurringEntryCreateViewModel(
    private val recurringEntryRepository: RecurringEntryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringEntryCreateState())
    val state: StateFlow<RecurringEntryCreateState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<RecurringEntryCreateEffect>()
    val effects: SharedFlow<RecurringEntryCreateEffect> = _effects.asSharedFlow()

    fun onAction(action: RecurringEntryCreateAction) {
        when (action) {
            is RecurringEntryCreateAction.ActiveChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        isActive = action.value
                    )
                }
            }

            is RecurringEntryCreateAction.AmountChanged -> {
                updateFormState { currentState ->
                    currentState.copy(amount = action.value)
                }
            }

            is RecurringEntryCreateAction.BillingFrequencyChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        billingFrequency = action.value
                    )
                }
            }

            is RecurringEntryCreateAction.CategoryChanged -> {
                updateFormState { currentState ->
                    currentState.copy(category = action.value)
                }
            }

            is RecurringEntryCreateAction.NameChanged -> {
                updateFormState { currentState ->
                    currentState.copy(name = action.value)
                }
            }

            is RecurringEntryCreateAction.NextPaymentDateChanged -> {
                updateFormState { currentState ->
                    currentState.copy(nextPaymentDate = action.value)
                }
            }

            is RecurringEntryCreateAction.NotesChanged -> {
                updateFormState { currentState ->
                    currentState.copy(notes = action.value)
                }
            }

            RecurringEntryCreateAction.SaveClicked -> saveRecurringEntry()

            is RecurringEntryCreateAction.TypeChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        type = action.value
                    )
                }
            }
        }
    }

    private fun updateFormState(
        transform: (RecurringEntryCreateState) -> RecurringEntryCreateState
    ) {
        _state.update { currentState ->
            transform(currentState).copy(hasSaveError = false)
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

            val entry = RecurringEntry(
                name = currentState.name.trim(),
                amount = requireNotNull(parseAmount(currentState.amount)),
                billingFrequency = currentState.billingFrequency,
                nextPaymentDate = currentState.nextPaymentDate.trim(),
                category = currentState.category.trim(),
                type = currentState.type,
                isActive = currentState.isActive,
                notes = currentState.notes.trim().ifBlank { null }
            )

            runCatching {
                recurringEntryRepository.upsertRecurringEntry(entry)
            }.onSuccess {
                _state.update { state ->
                    state.copy(isSaving = false)
                }
                _effects.emit(RecurringEntryCreateEffect.NavigateBack)
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

    companion object {
        fun factory(
            recurringEntryRepository: RecurringEntryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(RecurringEntryCreateViewModel::class.java))
                return RecurringEntryCreateViewModel(
                    recurringEntryRepository = recurringEntryRepository
                ) as T
            }
        }
    }
}
