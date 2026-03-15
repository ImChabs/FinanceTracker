package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.app.FinanceTrackerApplication
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import java.text.NumberFormat

@Composable
fun DashboardScreenRoot(
    onAddRecurringEntryClick: () -> Unit,
    onRecurringEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as FinanceTrackerApplication
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.factory(
            recurringEntryRepository = application.recurringEntryRepository,
            currencyMetadataRepository = application.currencyMetadataRepository
        )
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                DashboardEffect.NavigateToRecurringEntryCreate -> onAddRecurringEntryClick()
                is DashboardEffect.NavigateToRecurringEntryEdit -> onRecurringEntryClick(effect.entryId)
                DashboardEffect.CurrencyMetadataRetrySucceeded -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.dashboard_currency_retry_success)
                    )
                }
                DashboardEffect.CurrencyMetadataRetryFailed -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.dashboard_currency_retry_failure)
                    )
                }
            }
        }
    }

    DashboardScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.dashboard_headline),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.86f)
            )

            SummaryCard(
                state = state,
                onAction = onAction
            )
            UpcomingPaymentsSection(state = state)

            Button(
                onClick = { onAction(DashboardAction.AddRecurringEntryClicked) }
            ) {
                Text(text = stringResource(R.string.dashboard_add_recurring_entry))
            }

            when {
                state.isLoading -> {
                    Text(
                        text = stringResource(R.string.dashboard_loading),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                    )
                }

                state.isEmpty -> {
                    EmptyStateCard()
                }

                else -> {
                    Text(
                        text = stringResource(R.string.dashboard_entries_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(
                            R.string.dashboard_entries_count,
                            state.savedEntryCount
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                    )

                    state.recurringEntries.forEach { entry ->
                        RecurringEntryCard(
                            entry = entry,
                            onClick = {
                                onAction(DashboardAction.RecurringEntryClicked(entry.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingPaymentsSection(
    state: DashboardState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.dashboard_upcoming_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (state.upcomingPayments.isEmpty()) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.dashboard_upcoming_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    state.upcomingPayments.forEachIndexed { index, payment ->
                        UpcomingPaymentRow(payment = payment)
                        if (index < state.upcomingPayments.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingPaymentRow(
    payment: DashboardUpcomingPaymentItem
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = payment.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(
                    R.string.dashboard_upcoming_date_and_category,
                    payment.nextPaymentDate,
                    payment.category
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(
                    R.string.dashboard_currency_code,
                    payment.currencyCode
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = formatAmountForSavedCurrency(
                amount = payment.amount,
                currencyCode = payment.currencyCode
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SummaryCard(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_summary_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f)
            )
            Text(
                text = formatCurrency(state.monthlyRecurringTotal),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = if (state.activeEntryCount > 0) {
                    stringResource(
                        R.string.dashboard_summary_active_entries,
                        state.activeEntryCount
                    )
                } else {
                    stringResource(R.string.dashboard_summary_no_active_entries)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
            )
            if (state.hasMixedActiveCurrencies) {
                Text(
                    text = stringResource(
                        R.string.dashboard_summary_mixed_currencies,
                        state.activeCurrencyCodes.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
                )
            }
            CurrencyMetadataStatus(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun CurrencyMetadataStatus(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    val statusText = when {
        state.isCurrencySyncInProgress && state.currencyMetadataCount > 0 -> {
            stringResource(
                R.string.dashboard_currency_status_refreshing_cached,
                state.currencyMetadataCount
            )
        }

        state.isCurrencySyncInProgress -> {
            stringResource(R.string.dashboard_currency_status_refreshing)
        }

        state.currencyMetadataCount > 0 && state.hasCurrencySyncFailure -> {
            stringResource(
                R.string.dashboard_currency_status_refresh_failed,
                state.currencyMetadataCount
            )
        }

        state.currencyMetadataCount > 0 -> {
            stringResource(
                R.string.dashboard_currency_status_ready,
                state.currencyMetadataCount
            )
        }

        state.hasCurrencySyncFailure -> stringResource(R.string.dashboard_currency_status_unavailable)
        else -> null
    } ?: return

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
        )

        if ((state.hasCurrencySyncFailure || state.currencyMetadataCount == 0) &&
            !state.isCurrencySyncInProgress
        ) {
            TextButton(
                onClick = { onAction(DashboardAction.RetryCurrencyMetadataClicked) }
            ) {
                Text(text = stringResource(R.string.dashboard_currency_retry))
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.dashboard_empty_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecurringEntryCard(
    entry: DashboardRecurringEntryItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = entry.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = entry.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(
                            R.string.dashboard_currency_code,
                            entry.currencyCode
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = formatAmountForSavedCurrency(
                        amount = entry.amount,
                        currencyCode = entry.currencyCode
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = buildString {
                    append(recurringEntryTypeLabel(entry.type))
                    append(" - ")
                    append(billingFrequencyLabel(entry.billingFrequency))
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(
                    R.string.dashboard_next_payment,
                    entry.nextPaymentDate
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = if (entry.isActive) {
                    stringResource(R.string.dashboard_status_active)
                } else {
                    stringResource(R.string.dashboard_status_inactive)
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (entry.isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )

            entry.notes?.let { notes ->
                Text(
                    text = stringResource(R.string.dashboard_notes, notes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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

private fun formatCurrency(amount: Double): String =
    NumberFormat.getCurrencyInstance().format(amount)

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    FinanceTrackerTheme {
        DashboardScreen(
            state = DashboardState(
                isLoading = false,
                monthlyRecurringTotal = 205.99,
                activeEntryCount = 2,
                activeCurrencyCodes = setOf("USD", "EUR"),
                savedEntryCount = 3,
                currencyMetadataCount = 2,
                upcomingPayments = listOf(
                    DashboardUpcomingPaymentItem(
                        id = 1L,
                        name = "Netflix",
                        amount = 15.99,
                        currencyCode = "USD",
                        nextPaymentDate = "2026-03-31",
                        category = "Streaming"
                    ),
                    DashboardUpcomingPaymentItem(
                        id = 2L,
                        name = "Rent",
                        amount = 190.0,
                        currencyCode = "EUR",
                        nextPaymentDate = "2026-04-01",
                        category = "Housing"
                    )
                ),
                recurringEntries = listOf(
                    DashboardRecurringEntryItem(
                        id = 1L,
                        name = "Netflix",
                        amount = 15.99,
                        currencyCode = "USD",
                        billingFrequency = BillingFrequency.MONTHLY,
                        nextPaymentDate = "2026-03-31",
                        category = "Streaming",
                        type = RecurringEntryType.SUBSCRIPTION,
                        isActive = true,
                        notes = "Family plan"
                    ),
                    DashboardRecurringEntryItem(
                        id = 2L,
                        name = "Rent",
                        amount = 190.0,
                        currencyCode = "EUR",
                        billingFrequency = BillingFrequency.MONTHLY,
                        nextPaymentDate = "2026-04-01",
                        category = "Housing",
                        type = RecurringEntryType.RECURRING_EXPENSE,
                        isActive = true,
                        notes = null
                    ),
                    DashboardRecurringEntryItem(
                        id = 3L,
                        name = "Gym Membership",
                        amount = 29.99,
                        currencyCode = "JPY",
                        billingFrequency = BillingFrequency.MONTHLY,
                        nextPaymentDate = "2026-04-05",
                        category = "Health",
                        type = RecurringEntryType.SUBSCRIPTION,
                        isActive = false,
                        notes = null
                    )
                )
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
