package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.core.navigation.FinanceTrackerDestination
import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState
import com.example.newfinancetracker.feature.recurring.presentation.form.resolveRecurringEntryCurrencySelection
import com.example.newfinancetracker.feature.recurring.presentation.form.toFormState
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
class RecurringEntryEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recurringEntryRepository: RecurringEntryRepository,
    private val currencyMetadataRepository: CurrencyMetadataRepository
) : ViewModel() {

    private val entryId: Long = checkNotNull(
        savedStateHandle.get<Long>(FinanceTrackerDestination.RecurringEntryEdit.entryIdArg)
    ) {
        "Missing recurring entry id."
    }

    private var latestCurrencyMetadata: List<CurrencyMetadata> = emptyList()

    private val _state = MutableStateFlow(RecurringEntryEditState(entryId = entryId))
    val state: StateFlow<RecurringEntryEditState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<RecurringEntryEditEffect>()
    val effects: SharedFlow<RecurringEntryEditEffect> = _effects.asSharedFlow()

    init {
        observeCurrencyMetadata()
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

            is RecurringEntryEditAction.CurrencyCodeChanged -> {
                updateFormState { currentState ->
                    currentState.copy(currencyCode = action.value)
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

    private fun observeCurrencyMetadata() {
        viewModelScope.launch {
            currencyMetadataRepository.observeCurrencyMetadata().collect { metadata ->
                latestCurrencyMetadata = metadata
                _state.update { currentState ->
                    val currencySelection = resolveRecurringEntryCurrencySelection(
                        cachedMetadata = metadata,
                        currentCode = currentState.form.currencyCode
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
                    val loadedForm = entry.toFormState()
                    val currencySelection = resolveRecurringEntryCurrencySelection(
                        cachedMetadata = latestCurrencyMetadata,
                        currentCode = loadedForm.currencyCode
                    )

                    currentState.copy(
                        form = loadedForm.copy(currencyCode = currencySelection.selectedCode),
                        currencyOptions = currencySelection.options,
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

    constructor(
        entryId: Long,
        recurringEntryRepository: RecurringEntryRepository,
        currencyMetadataRepository: CurrencyMetadataRepository
    ) : this(
        savedStateHandle = SavedStateHandle(
            mapOf(FinanceTrackerDestination.RecurringEntryEdit.entryIdArg to entryId)
        ),
        recurringEntryRepository = recurringEntryRepository,
        currencyMetadataRepository = currencyMetadataRepository
    )
}
