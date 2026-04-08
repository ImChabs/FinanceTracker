package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerComponentDefaults
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerSpacing
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryDeleteButton
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormScreen
import com.example.newfinancetracker.feature.recurring.presentation.form.RecurringEntryFormState

@Composable
fun RecurringEntryEditScreenRoot(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RecurringEntryEditViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        val deleteDialogTitle = stringResource(R.string.recurring_edit_delete_confirm_title)
        val deleteDialogBody = stringResource(R.string.recurring_edit_delete_confirm_body)
        val deleteDialogSummary = listOf(deleteDialogTitle, deleteDialogBody)
            .joinToAccessibilitySummary()
        val deleteDialogStateDescription = stringResource(
            R.string.recurring_edit_delete_confirm_accessibility_state
        )
        val deleteConfirmActionLabel = stringResource(
            R.string.recurring_edit_delete_confirm_action_label
        )
        val deleteDismissActionLabel = stringResource(
            R.string.recurring_edit_delete_cancel_action_label
        )

        AlertDialog(
            onDismissRequest = { onAction(RecurringEntryEditAction.DeleteDismissed) },
            modifier = Modifier.semantics {
                paneTitle = deleteDialogTitle
                contentDescription = deleteDialogSummary
                stateDescription = deleteDialogStateDescription
            },
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(text = deleteDialogTitle)
            },
            text = {
                Text(text = deleteDialogBody)
            },
            confirmButton = {
                TextButton(
                    onClick = { onAction(RecurringEntryEditAction.DeleteConfirmed) },
                    modifier = Modifier.semantics {
                        onClick(label = deleteConfirmActionLabel, action = null)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.recurring_edit_delete_confirm_action),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(RecurringEntryEditAction.DeleteDismissed) },
                    modifier = Modifier.semantics {
                        onClick(label = deleteDismissActionLabel, action = null)
                    }
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
                currencyOptions = state.currencyOptions,
                showValidationErrors = state.showValidationErrors,
                hasSaveError = state.hasSaveError,
                isSaving = state.isSaving || state.isDeleting,
                onNavigateBack = onNavigateBack,
                onNameChanged = { onAction(RecurringEntryEditAction.NameChanged(it)) },
                onAmountChanged = { onAction(RecurringEntryEditAction.AmountChanged(it)) },
                onCurrencyCodeChanged = { onAction(RecurringEntryEditAction.CurrencyCodeChanged(it)) },
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
                        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact)
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

private fun List<String>.joinToAccessibilitySummary(): String = buildString {
    this@joinToAccessibilitySummary.forEachIndexed { index, segment ->
        append(segment)
        if (index == this@joinToAccessibilitySummary.lastIndex) return@forEachIndexed
        if (segment.endsWith(".") || segment.endsWith("!") || segment.endsWith("?")) {
            append(' ')
        } else {
            append(". ")
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RecurringEntryEditLoadingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accessibilitySummary = stringResource(R.string.recurring_edit_loading_accessibility_summary)
    val accessibilityStateDescription = stringResource(
        R.string.recurring_edit_loading_accessibility_state
    )

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = FinanceTrackerComponentDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = stringResource(R.string.recurring_edit_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = stringResource(R.string.recurring_edit_back),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Card(
            colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    contentDescription = accessibilitySummary
                    stateDescription = accessibilityStateDescription
                }
                .padding(innerPadding)
                .padding(horizontal = FinanceTrackerSpacing.heroCardPadding)
                .padding(top = FinanceTrackerSpacing.heroCardPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    FinanceTrackerSpacing.item,
                    Alignment.CenterVertically
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(FinanceTrackerSpacing.heroCardPadding)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.recurring_edit_loading),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RecurringEntryEditMissingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.recurring_edit_missing_title)
    val body = stringResource(R.string.recurring_edit_missing_body)
    val accessibilitySummary = listOf(title, body).joinToAccessibilitySummary()
    val accessibilityStateDescription = stringResource(
        R.string.recurring_edit_missing_accessibility_state
    )
    val backActionLabel = stringResource(R.string.recurring_edit_back_to_dashboard_action_label)

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = FinanceTrackerComponentDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = stringResource(R.string.recurring_edit_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = stringResource(R.string.recurring_edit_back),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Card(
            colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    contentDescription = accessibilitySummary
                    stateDescription = accessibilityStateDescription
                }
                .padding(innerPadding)
                .padding(
                    horizontal = FinanceTrackerSpacing.screenHorizontal,
                    vertical = FinanceTrackerSpacing.screenVertical
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
                modifier = Modifier.padding(FinanceTrackerSpacing.heroCardPadding)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            onClick(label = backActionLabel, action = null)
                        }
                ) {
                    Text(text = stringResource(R.string.recurring_edit_back_to_dashboard))
                }
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
                    currencyCode = "USD",
                    nextPaymentDate = "2026-04-01",
                    notes = "Due on the first"
                )
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
