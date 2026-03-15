package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val recurringEntryRepository: RecurringEntryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<DashboardEffect>()
    val effects: SharedFlow<DashboardEffect> = _effects.asSharedFlow()

    init {
        observeRecurringEntries()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.AddRecurringEntryClicked -> {
                viewModelScope.launch {
                    _effects.emit(DashboardEffect.NavigateToRecurringEntryCreate)
                }
            }
        }
    }

    private fun observeRecurringEntries() {
        viewModelScope.launch {
            recurringEntryRepository.observeRecurringEntries().collectLatest { entries ->
                _state.update {
                    entries.toDashboardState()
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
                require(modelClass.isAssignableFrom(DashboardViewModel::class.java))
                return DashboardViewModel(
                    recurringEntryRepository = recurringEntryRepository
                ) as T
            }
        }
    }
}
