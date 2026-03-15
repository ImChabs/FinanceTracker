package com.example.newfinancetracker.feature.recurring.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.app.FinanceTrackerApplication
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

@Composable
fun RecurringEntryCreateScreenRoot(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as FinanceTrackerApplication
    val viewModel: RecurringEntryCreateViewModel = viewModel(
        factory = RecurringEntryCreateViewModel.factory(
            recurringEntryRepository = application.recurringEntryRepository
        )
    )
    val state by viewModel.state.collectAsState()

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
@OptIn(ExperimentalMaterial3Api::class)
fun RecurringEntryCreateScreen(
    state: RecurringEntryCreateState,
    onAction: (RecurringEntryCreateAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.recurring_create_title))
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(text = stringResource(R.string.recurring_create_cancel))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onAction(RecurringEntryCreateAction.NameChanged(it)) },
                label = { Text(text = stringResource(R.string.recurring_create_name_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_name_placeholder)) },
                isError = state.shouldShowNameError,
                supportingText = {
                    if (state.shouldShowNameError) {
                        Text(text = stringResource(R.string.recurring_create_validation_name))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = { onAction(RecurringEntryCreateAction.AmountChanged(it)) },
                label = { Text(text = stringResource(R.string.recurring_create_amount_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_amount_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.shouldShowAmountError,
                supportingText = {
                    if (state.shouldShowAmountError) {
                        Text(text = stringResource(R.string.recurring_create_validation_amount))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.category,
                onValueChange = { onAction(RecurringEntryCreateAction.CategoryChanged(it)) },
                label = { Text(text = stringResource(R.string.recurring_create_category_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_category_placeholder)) },
                isError = state.shouldShowCategoryError,
                supportingText = {
                    if (state.shouldShowCategoryError) {
                        Text(text = stringResource(R.string.recurring_create_validation_category))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.nextPaymentDate,
                onValueChange = { onAction(RecurringEntryCreateAction.NextPaymentDateChanged(it)) },
                label = { Text(text = stringResource(R.string.recurring_create_next_payment_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_next_payment_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.shouldShowDateError,
                supportingText = {
                    if (state.shouldShowDateError) {
                        Text(text = stringResource(R.string.recurring_create_validation_date))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SelectionSection(
                title = stringResource(R.string.recurring_create_type_label),
                contentPadding = PaddingValues(top = 4.dp)
            ) {
                SelectionChipRow(
                    options = RecurringEntryType.entries,
                    selectedOption = state.type,
                    labelFor = ::recurringEntryTypeLabel,
                    onSelected = {
                        onAction(RecurringEntryCreateAction.TypeChanged(it))
                    }
                )
            }

            SelectionSection(
                title = stringResource(R.string.recurring_create_frequency_label),
                contentPadding = PaddingValues(top = 4.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SelectionChipRow(
                        options = BillingFrequency.entries.take(2),
                        selectedOption = state.billingFrequency,
                        labelFor = ::billingFrequencyLabel,
                        onSelected = {
                            onAction(RecurringEntryCreateAction.BillingFrequencyChanged(it))
                        }
                    )
                    SelectionChipRow(
                        options = BillingFrequency.entries.drop(2),
                        selectedOption = state.billingFrequency,
                        labelFor = ::billingFrequencyLabel,
                        onSelected = {
                            onAction(RecurringEntryCreateAction.BillingFrequencyChanged(it))
                        }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.recurring_create_active_label),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = stringResource(R.string.recurring_create_active_supporting),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = state.isActive,
                    onCheckedChange = {
                        onAction(RecurringEntryCreateAction.ActiveChanged(it))
                    }
                )
            }

            OutlinedTextField(
                value = state.notes,
                onValueChange = { onAction(RecurringEntryCreateAction.NotesChanged(it)) },
                label = { Text(text = stringResource(R.string.recurring_create_notes_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_notes_placeholder)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.hasSaveError) {
                Text(
                    text = stringResource(R.string.recurring_create_save_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = { onAction(RecurringEntryCreateAction.SaveClicked) },
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.recurring_create_save))
            }
        }
    }
}

@Composable
private fun <T> SelectionChipRow(
    options: List<T>,
    selectedOption: T,
    labelFor: @Composable (T) -> String,
    onSelected: (T) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onSelected(option) },
                label = {
                    Text(text = labelFor(option))
                }
            )
        }
    }
}

@Composable
private fun SelectionSection(
    title: String,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(contentPadding)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        content()
    }
}

@Composable
private fun recurringEntryTypeLabel(type: RecurringEntryType): String =
    when (type) {
        RecurringEntryType.SUBSCRIPTION -> stringResource(R.string.recurring_create_type_subscription)
        RecurringEntryType.RECURRING_EXPENSE -> stringResource(R.string.recurring_create_type_expense)
    }

@Composable
private fun billingFrequencyLabel(frequency: BillingFrequency): String =
    when (frequency) {
        BillingFrequency.WEEKLY -> stringResource(R.string.recurring_create_frequency_weekly)
        BillingFrequency.MONTHLY -> stringResource(R.string.recurring_create_frequency_monthly)
        BillingFrequency.QUARTERLY -> stringResource(R.string.recurring_create_frequency_quarterly)
        BillingFrequency.YEARLY -> stringResource(R.string.recurring_create_frequency_yearly)
    }

@Preview(showBackground = true)
@Composable
private fun RecurringEntryCreateScreenPreview() {
    FinanceTrackerTheme {
        RecurringEntryCreateScreen(
            state = RecurringEntryCreateState(
                name = "Netflix",
                amount = "15.99",
                category = "Streaming",
                nextPaymentDate = "2026-03-31",
                notes = "Standard plan"
            ),
            onAction = {},
            onNavigateBack = {}
        )
    }
}
