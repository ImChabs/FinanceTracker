package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val recurringEntryRepository: RecurringEntryRepository,
    private val currencyMetadataRepository: CurrencyMetadataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<DashboardEffect>()
    val effects: SharedFlow<DashboardEffect> = _effects.asSharedFlow()

    init {
        observeCurrencyMetadata()
        observeRecurringEntries()
        syncCurrencyMetadata(isRetry = false)
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.AddRecurringEntryClicked -> {
                viewModelScope.launch {
                    _effects.emit(DashboardEffect.NavigateToRecurringEntryCreate)
                }
            }

            DashboardAction.RetryCurrencyMetadataClicked -> {
                syncCurrencyMetadata(isRetry = true)
            }

            is DashboardAction.RecurringEntryClicked -> {
                viewModelScope.launch {
                    _effects.emit(DashboardEffect.NavigateToRecurringEntryEdit(action.entryId))
                }
            }
        }
    }

    private fun observeRecurringEntries() {
        viewModelScope.launch {
            recurringEntryRepository.observeRecurringEntries().collectLatest { entries ->
                val dashboardState = entries.toDashboardState()
                _state.update {
                    dashboardState.copy(
                        currencyMetadataCount = it.currencyMetadataCount,
                        hasCurrencySyncFailure = it.hasCurrencySyncFailure,
                        isCurrencySyncInProgress = it.isCurrencySyncInProgress
                    )
                }
            }
        }
    }

    private fun observeCurrencyMetadata() {
        viewModelScope.launch {
            currencyMetadataRepository.observeCurrencyMetadata().collectLatest { metadata ->
                _state.update {
                    it.copy(currencyMetadataCount = metadata.size)
                }
            }
        }
    }

    private fun syncCurrencyMetadata(isRetry: Boolean) {
        if (_state.value.isCurrencySyncInProgress) return

        _state.update { it.copy(isCurrencySyncInProgress = true) }
        viewModelScope.launch {
            val syncResult = currencyMetadataRepository.syncCurrencyMetadata()
            _state.update {
                it.copy(
                    hasCurrencySyncFailure = syncResult.isFailure,
                    isCurrencySyncInProgress = false
                )
            }

            if (isRetry) {
                _effects.emit(
                    if (syncResult.isSuccess) {
                        DashboardEffect.CurrencyMetadataRetrySucceeded
                    } else {
                        DashboardEffect.CurrencyMetadataRetryFailed
                    }
                )
            }
        }
    }
}
