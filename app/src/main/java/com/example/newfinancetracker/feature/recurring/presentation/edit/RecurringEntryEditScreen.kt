package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.app.FinanceTrackerApplication
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryDeleteButton
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormScreen
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

@Composable
fun RecurringEntryEditScreenRoot(
    entryId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as FinanceTrackerApplication
    val viewModel: RecurringEntryEditViewModel = viewModel(
        factory = RecurringEntryEditViewModel.factory(
            entryId = entryId,
            recurringEntryRepository = application.recurringEntryRepository
        )
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                RecurringEntryEditEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    RecurringEntryEditScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun RecurringEntryEditScreen(
    state: RecurringEntryEditState,
    onAction: (RecurringEntryEditAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isDeleteConfirmationVisible) {
        AlertDialog(
            onDismissRequest = { onAction(RecurringEntryEditAction.DeleteDismissed) },
            title = {
                Text(text = stringResource(R.string.recurring_edit_delete_confirm_title))
            },
            text = {
                Text(text = stringResource(R.string.recurring_edit_delete_confirm_body))
            },
            confirmButton = {
                TextButton(
                    onClick = { onAction(RecurringEntryEditAction.DeleteConfirmed) }
                ) {
                    Text(
                        text = stringResource(R.string.recurring_edit_delete_confirm_action),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(RecurringEntryEditAction.DeleteDismissed) }
                ) {
                    Text(text = stringResource(R.string.recurring_edit_delete_cancel))
                }
            }
        )
    }

    when {
        state.isLoading -> {
            RecurringEntryEditLoadingScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        state.isMissingEntry -> {
            RecurringEntryEditMissingScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        else -> {
            RecurringEntryFormScreen(
                title = stringResource(R.string.recurring_edit_title),
                navigationLabel = stringResource(R.string.recurring_edit_back),
                saveLabel = stringResource(R.string.recurring_edit_save),
                formState = state.form,
                showValidationErrors = state.showValidationErrors,
                hasSaveError = state.hasSaveError,
                isSaving = state.isSaving || state.isDeleting,
                onNavigateBack = onNavigateBack,
                onNameChanged = { onAction(RecurringEntryEditAction.NameChanged(it)) },
                onAmountChanged = { onAction(RecurringEntryEditAction.AmountChanged(it)) },
                onCategoryChanged = { onAction(RecurringEntryEditAction.CategoryChanged(it)) },
                onNextPaymentDateChanged = {
                    onAction(RecurringEntryEditAction.NextPaymentDateChanged(it))
                },
                onTypeChanged = { onAction(RecurringEntryEditAction.TypeChanged(it)) },
                onBillingFrequencyChanged = {
                    onAction(RecurringEntryEditAction.BillingFrequencyChanged(it))
                },
                onActiveChanged = { onAction(RecurringEntryEditAction.ActiveChanged(it)) },
                onNotesChanged = { onAction(RecurringEntryEditAction.NotesChanged(it)) },
                onSaveClicked = { onAction(RecurringEntryEditAction.SaveClicked) },
                destructiveAction = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (state.hasDeleteError) {
                            Text(
                                text = stringResource(R.string.recurring_edit_delete_error),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        RecurringEntryDeleteButton(
                            label = stringResource(R.string.recurring_edit_delete),
                            enabled = state.canDelete,
                            onClick = { onAction(RecurringEntryEditAction.DeleteClicked) }
                        )
                    }
                },
                modifier = modifier
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RecurringEntryEditLoadingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.recurring_edit_title))
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(text = stringResource(R.string.recurring_edit_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.recurring_edit_loading),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RecurringEntryEditMissingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.recurring_edit_title))
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(text = stringResource(R.string.recurring_edit_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.recurring_edit_missing_title),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.recurring_edit_missing_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recurring_edit_back_to_dashboard))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecurringEntryEditScreenPreview() {
    FinanceTrackerTheme {
        RecurringEntryEditScreen(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                form = RecurringEntryFormState(
                    name = "Rent",
                    amount = "1450.00",
                    category = "Housing",
                    nextPaymentDate = "2026-04-01",
                    notes = "Due on the first"
                )
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
