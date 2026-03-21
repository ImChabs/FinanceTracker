package com.example.newfinancetracker.feature.recurring.presentation.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerComponentDefaults
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerSpacing
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecurringEntryFormScreen(
    title: String,
    navigationLabel: String,
    saveLabel: String,
    formState: RecurringEntryFormState,
    currencyOptions: List<RecurringEntryCurrencyOption>,
    showValidationErrors: Boolean,
    hasSaveError: Boolean,
    isSaving: Boolean,
    onNavigateBack: () -> Unit,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencyCodeChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onNextPaymentDateChanged: (String) -> Unit,
    onTypeChanged: (RecurringEntryType) -> Unit,
    onBillingFrequencyChanged: (BillingFrequency) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
    onNotesChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    destructiveAction: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = FinanceTrackerComponentDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = navigationLabel,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.section),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(
                    horizontal = FinanceTrackerSpacing.screenHorizontal,
                    vertical = FinanceTrackerSpacing.screenVerticalCompact
                )
        ) {
            FormSectionCard {
                OutlinedTextField(
                    value = formState.name,
                    onValueChange = onNameChanged,
                    label = { Text(text = stringResource(R.string.recurring_create_name_label)) },
                    placeholder = {
                        Text(text = stringResource(R.string.recurring_create_name_placeholder))
                    },
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
                    placeholder = {
                        Text(text = stringResource(R.string.recurring_create_amount_placeholder))
                    },
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

                CurrencySelectionField(
                    selectedCurrencyCode = formState.currencyCode,
                    currencyOptions = currencyOptions,
                    onCurrencySelected = onCurrencyCodeChanged
                )

                OutlinedTextField(
                    value = formState.category,
                    onValueChange = onCategoryChanged,
                    label = { Text(text = stringResource(R.string.recurring_create_category_label)) },
                    placeholder = {
                        Text(text = stringResource(R.string.recurring_create_category_placeholder))
                    },
                    isError = showValidationErrors && formState.category.isBlank(),
                    supportingText = {
                        if (showValidationErrors && formState.category.isBlank()) {
                            Text(text = stringResource(R.string.recurring_create_validation_category))
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            FormSectionCard {
                OutlinedTextField(
                    value = formState.nextPaymentDate,
                    onValueChange = onNextPaymentDateChanged,
                    label = { Text(text = stringResource(R.string.recurring_create_next_payment_label)) },
                    placeholder = {
                        Text(text = stringResource(R.string.recurring_create_next_payment_placeholder))
                    },
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
                    contentPadding = PaddingValues(top = FinanceTrackerSpacing.compact)
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
                    contentPadding = PaddingValues(top = FinanceTrackerSpacing.compact)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact)
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
                    horizontalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
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
            }

            FormSectionCard {
                OutlinedTextField(
                    value = formState.notes,
                    onValueChange = onNotesChanged,
                    label = { Text(text = stringResource(R.string.recurring_create_notes_label)) },
                    placeholder = {
                        Text(text = stringResource(R.string.recurring_create_notes_placeholder))
                    },
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

                destructiveAction?.let {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    it()
                }
            }
        }
    }
}

@Composable
private fun FormSectionCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
            modifier = Modifier.padding(FinanceTrackerSpacing.cardPadding),
            content = content
        )
    }
}

@Composable
fun RecurringEntryDeleteButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = FinanceTrackerComponentDefaults.destructiveButtonBorder(),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label)
    }
}

@Composable
private fun CurrencySelectionField(
    selectedCurrencyCode: String,
    currencyOptions: List<RecurringEntryCurrencyOption>,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedOption = currencyOptions.firstOrNull { it.code == selectedCurrencyCode }
        ?: RecurringEntryCurrencyOption(code = selectedCurrencyCode)

    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(FinanceTrackerSpacing.cardPadding)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.recurring_create_currency_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedOption.code,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = selectedOption.displayName
                            ?: stringResource(R.string.recurring_create_currency_supporting),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = stringResource(R.string.recurring_create_currency_change),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            currencyOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = option.code,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            option.displayName?.let { displayName ->
                                Text(
                                    text = displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onClick = {
                        onCurrencySelected(option.code)
                        isExpanded = false
                    }
                )
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
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onSelected(option) },
                colors = FinanceTrackerComponentDefaults.formSelectionChipColors(),
                label = {
                    Text(
                        text = labelFor(option),
                        style = MaterialTheme.typography.labelLarge
                    )
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
        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
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
