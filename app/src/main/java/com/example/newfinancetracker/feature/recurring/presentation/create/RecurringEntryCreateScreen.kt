package com.example.newfinancetracker.feature.recurring.presentation.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormScreen
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

@Composable
fun RecurringEntryCreateScreenRoot(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RecurringEntryCreateViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                RecurringEntryCreateEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    RecurringEntryCreateScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun RecurringEntryCreateScreen(
    state: RecurringEntryCreateState,
    onAction: (RecurringEntryCreateAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecurringEntryFormScreen(
        title = stringResource(R.string.recurring_create_title),
        navigationLabel = stringResource(R.string.recurring_create_cancel),
        saveLabel = stringResource(R.string.recurring_create_save),
        formState = state.form,
        currencyOptions = state.currencyOptions,
        showValidationErrors = state.showValidationErrors,
        hasSaveError = state.hasSaveError,
        isSaving = state.isSaving,
        onNavigateBack = onNavigateBack,
        onNameChanged = { onAction(RecurringEntryCreateAction.NameChanged(it)) },
        onAmountChanged = { onAction(RecurringEntryCreateAction.AmountChanged(it)) },
        onCurrencyCodeChanged = { onAction(RecurringEntryCreateAction.CurrencyCodeChanged(it)) },
        onCategoryChanged = { onAction(RecurringEntryCreateAction.CategoryChanged(it)) },
        onNextPaymentDateChanged = {
            onAction(RecurringEntryCreateAction.NextPaymentDateChanged(it))
        },
        onTypeChanged = { onAction(RecurringEntryCreateAction.TypeChanged(it)) },
        onBillingFrequencyChanged = {
            onAction(RecurringEntryCreateAction.BillingFrequencyChanged(it))
        },
        onActiveChanged = { onAction(RecurringEntryCreateAction.ActiveChanged(it)) },
        onNotesChanged = { onAction(RecurringEntryCreateAction.NotesChanged(it)) },
        onSaveClicked = { onAction(RecurringEntryCreateAction.SaveClicked) },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun RecurringEntryCreateScreenPreview() {
    FinanceTrackerTheme {
        RecurringEntryCreateScreen(
            state = RecurringEntryCreateState(
                form = RecurringEntryFormState(
                    name = "Netflix",
                    amount = "15.99",
                    currencyCode = "USD",
                    category = "Streaming",
                    nextPaymentDate = "2026-03-31",
                    notes = "Standard plan"
                )
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
