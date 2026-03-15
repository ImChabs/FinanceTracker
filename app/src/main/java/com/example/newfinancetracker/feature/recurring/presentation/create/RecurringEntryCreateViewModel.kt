package com.example.newfinancetracker.feature.recurring.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState
import com.example.newfinancetracker.feature.recurring.presentation.form.resolveRecurringEntryCurrencySelection
import com.example.newfinancetracker.feature.recurring.presentation.form.toRecurringEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecurringEntryCreateViewModel @Inject constructor(
    private val recurringEntryRepository: RecurringEntryRepository,
    private val currencyMetadataRepository: CurrencyMetadataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringEntryCreateState())
    val state: StateFlow<RecurringEntryCreateState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<RecurringEntryCreateEffect>()
    val effects: SharedFlow<RecurringEntryCreateEffect> = _effects.asSharedFlow()

    init {
        observeCurrencyMetadata()
    }

    fun onAction(action: RecurringEntryCreateAction) {
        when (action) {
            is RecurringEntryCreateAction.ActiveChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(isActive = action.value)
                    )
                }
            }

            is RecurringEntryCreateAction.CurrencyCodeChanged -> {
                updateFormState { currentState ->
                    currentState.copy(currencyCode = action.value)
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
                        form = currentState.form.copy(billingFrequency = action.value)
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
                        form = currentState.form.copy(type = action.value)
                    )
                }
            }
        }
    }

    private fun observeCurrencyMetadata() {
        viewModelScope.launch {
            currencyMetadataRepository.observeCurrencyMetadata().collect { metadata ->
                _state.update { currentState ->
                    val currencySelection = resolveRecurringEntryCurrencySelection(
                        cachedMetadata = metadata,
                        currentCode = currentState.form.currencyCode,
                        preferFirstCachedOverDefault = true
                    )

                    currentState.copy(
                        form = currentState.form.copy(currencyCode = currencySelection.selectedCode),
                        currencyOptions = currencySelection.options
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
                hasSaveError = false
            )
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
                recurringEntryRepository.upsertRecurringEntry(currentState.form.toRecurringEntry())
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
}
