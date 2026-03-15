package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as FinanceTrackerApplication
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.factory(
            recurringEntryRepository = application.recurringEntryRepository
        )
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                DashboardEffect.NavigateToRecurringEntryCreate -> onAddRecurringEntryClick()
            }
        }
    }

    DashboardScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
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

            SummaryCard(state = state)

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
                        RecurringEntryCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    state: DashboardState
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
    entry: DashboardRecurringEntryItem
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
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
                }
                Text(
                    text = formatCurrency(entry.amount),
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
                savedEntryCount = 3,
                recurringEntries = listOf(
                    DashboardRecurringEntryItem(
                        id = 1L,
                        name = "Netflix",
                        amount = 15.99,
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
                        billingFrequency = BillingFrequency.MONTHLY,
                        nextPaymentDate = "2026-04-05",
                        category = "Health",
                        type = RecurringEntryType.SUBSCRIPTION,
                        isActive = false,
                        notes = null
                    )
                )
            ),
            onAction = {}
        )
    }
}
