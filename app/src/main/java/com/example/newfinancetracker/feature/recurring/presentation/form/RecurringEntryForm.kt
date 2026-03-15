package com.example.newfinancetracker.feature.recurring.presentation.form

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.newfinancetracker.R
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecurringEntryFormScreen(
    title: String,
    navigationLabel: String,
    saveLabel: String,
    formState: RecurringEntryFormState,
    showValidationErrors: Boolean,
    hasSaveError: Boolean,
    isSaving: Boolean,
    onNavigateBack: () -> Unit,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onNextPaymentDateChanged: (String) -> Unit,
    onTypeChanged: (RecurringEntryType) -> Unit,
    onBillingFrequencyChanged: (BillingFrequency) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
    onNotesChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(text = navigationLabel)
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
                value = formState.name,
                onValueChange = onNameChanged,
                label = { Text(text = stringResource(R.string.recurring_create_name_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_name_placeholder)) },
                isError = showValidationErrors && formState.name.isBlank(),
                supportingText = {
                    if (showValidationErrors && formState.name.isBlank()) {
                        Text(text = stringResource(R.string.recurring_create_validation_name))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.amount,
                onValueChange = onAmountChanged,
                label = { Text(text = stringResource(R.string.recurring_create_amount_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_amount_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = showValidationErrors && parseAmount(formState.amount) == null,
                supportingText = {
                    if (showValidationErrors && parseAmount(formState.amount) == null) {
                        Text(text = stringResource(R.string.recurring_create_validation_amount))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.category,
                onValueChange = onCategoryChanged,
                label = { Text(text = stringResource(R.string.recurring_create_category_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_category_placeholder)) },
                isError = showValidationErrors && formState.category.isBlank(),
                supportingText = {
                    if (showValidationErrors && formState.category.isBlank()) {
                        Text(text = stringResource(R.string.recurring_create_validation_category))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.nextPaymentDate,
                onValueChange = onNextPaymentDateChanged,
                label = { Text(text = stringResource(R.string.recurring_create_next_payment_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_next_payment_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showValidationErrors && !isValidIsoDate(formState.nextPaymentDate),
                supportingText = {
                    if (showValidationErrors && !isValidIsoDate(formState.nextPaymentDate)) {
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
                    selectedOption = formState.type,
                    labelFor = ::recurringEntryTypeLabel,
                    onSelected = onTypeChanged
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
                        selectedOption = formState.billingFrequency,
                        labelFor = ::billingFrequencyLabel,
                        onSelected = onBillingFrequencyChanged
                    )
                    SelectionChipRow(
                        options = BillingFrequency.entries.drop(2),
                        selectedOption = formState.billingFrequency,
                        labelFor = ::billingFrequencyLabel,
                        onSelected = onBillingFrequencyChanged
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
                    checked = formState.isActive,
                    onCheckedChange = onActiveChanged
                )
            }

            OutlinedTextField(
                value = formState.notes,
                onValueChange = onNotesChanged,
                label = { Text(text = stringResource(R.string.recurring_create_notes_label)) },
                placeholder = { Text(text = stringResource(R.string.recurring_create_notes_placeholder)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (hasSaveError) {
                Text(
                    text = stringResource(R.string.recurring_create_save_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = onSaveClicked,
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = saveLabel)
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
