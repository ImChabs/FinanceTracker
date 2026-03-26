package com.example.newfinancetracker.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newfinancetracker.R
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerComponentDefaults
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerSpacing
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

@Composable
fun DashboardScreenRoot(
    onAddRecurringEntryClick: () -> Unit,
    onRecurringEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: DashboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currencyRetrySuccessMessage = stringResource(R.string.dashboard_currency_retry_success)
    val retryFailureMessage = stringResource(R.string.dashboard_currency_retry_failure)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                DashboardEffect.NavigateToRecurringEntryCreate -> onAddRecurringEntryClick()
                is DashboardEffect.NavigateToRecurringEntryEdit -> onRecurringEntryClick(effect.entryId)
                DashboardEffect.CurrencyMetadataRetrySucceeded -> {
                    snackbarHostState.showSnackbar(
                        message = currencyRetrySuccessMessage
                    )
                }
                DashboardEffect.CurrencyMetadataRetryFailed -> {
                    snackbarHostState.showSnackbar(
                        message = retryFailureMessage
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
    val addRecurringEntryActionLabel =
        stringResource(R.string.dashboard_add_recurring_entry_action_label)

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                    vertical = FinanceTrackerSpacing.screenVertical
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact)
            ) {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.dashboard_headline),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f)
                )
            }

            SummaryCard(
                state = state,
                onAction = onAction
            )
            UpcomingPaymentsSection(
                state = state,
                onAction = onAction
            )

            Button(
                onClick = { onAction(DashboardAction.AddRecurringEntryClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        onClick(label = addRecurringEntryActionLabel, action = null)
                    }
            ) {
                Text(text = stringResource(R.string.dashboard_add_recurring_entry))
            }

            when {
                state.isLoading -> {
                    LoadingStateCard()
                }

                state.isEmpty -> {
                    EmptyStateCard()
                }

                else -> {
                    Text(
                        text = stringResource(R.string.dashboard_active_entries_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(
                            R.string.dashboard_active_entries_count,
                            state.savedEntryCount
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                    )

                    if (state.recurringEntries.isEmpty()) {
                        DashboardStateMessageCard(
                            body = stringResource(R.string.dashboard_active_entries_empty)
                        )
                    } else {
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
}

@Composable
private fun UpcomingPaymentsSection(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item)
    ) {
        Text(
            text = stringResource(R.string.dashboard_upcoming_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (state.upcomingPayments.isEmpty()) {
            DashboardStateMessageCard(
                body = stringResource(R.string.dashboard_upcoming_empty)
            )
        } else {
            Card(
                colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = FinanceTrackerSpacing.cardPadding,
                        vertical = FinanceTrackerSpacing.compact
                    )
                ) {
                    state.upcomingPayments.forEachIndexed { index, payment ->
                        UpcomingPaymentRow(
                            payment = payment,
                            onClick = {
                                onAction(DashboardAction.RecurringEntryClicked(payment.id))
                            }
                        )
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
    payment: DashboardUpcomingPaymentItem,
    onClick: () -> Unit
) {
    val openActionLabel = stringResource(R.string.dashboard_upcoming_payment_open_action_label)
    val urgency = payment.relativeDueContext.toUpcomingPaymentUrgency()
    val urgencyStyle = rememberUpcomingPaymentUrgencyStyle(urgency = urgency)
    val urgencyAccessibilityDescription = urgency.toAccessibilityDescription()
    val accessibilitySummary = payment.toAccessibilitySummary(urgency = urgency)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = openActionLabel,
                onClick = onClick
            )
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilitySummary
                upcomingPaymentUrgency = urgency.name
                urgencyAccessibilityDescription?.let { description ->
                    stateDescription = description
                }
            }
            .background(
                color = urgencyStyle.containerColor,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = FinanceTrackerSpacing.item,
                    vertical = FinanceTrackerSpacing.item
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = payment.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = urgencyStyle.primaryTextColor
                )
                Text(
                    text = stringResource(
                        R.string.dashboard_upcoming_date_and_category,
                        payment.nextPaymentDate.toDashboardDisplayDate(),
                        payment.category
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = urgencyStyle.secondaryTextColor
                )
                payment.relativeDueContext?.let { relativeDueContext ->
                    Text(
                        text = relativeDueContext.toDisplayText(),
                        style = MaterialTheme.typography.bodySmall,
                        color = urgencyStyle.dueContextColor,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = stringResource(
                        R.string.dashboard_currency_code,
                        payment.currencyCode
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = urgencyStyle.secondaryTextColor
                )
            }
            Text(
                text = formatAmountForSavedCurrency(
                    amount = payment.amount,
                    currencyCode = payment.currencyCode
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = urgencyStyle.primaryTextColor
            )
        }
    }
}

@Composable
private fun rememberUpcomingPaymentUrgencyStyle(
    urgency: DashboardUpcomingPaymentUrgency
): DashboardUpcomingPaymentUrgencyStyle {
    val colorScheme = MaterialTheme.colorScheme
    return when (urgency) {
        DashboardUpcomingPaymentUrgency.OVERDUE -> DashboardUpcomingPaymentUrgencyStyle(
            containerColor = colorScheme.errorContainer.copy(alpha = 0.42f),
            primaryTextColor = colorScheme.onSurface,
            secondaryTextColor = colorScheme.onSurfaceVariant,
            dueContextColor = colorScheme.error
        )
        DashboardUpcomingPaymentUrgency.DUE_TODAY -> DashboardUpcomingPaymentUrgencyStyle(
            containerColor = colorScheme.primaryContainer.copy(alpha = 0.55f),
            primaryTextColor = colorScheme.onSurface,
            secondaryTextColor = colorScheme.onSurfaceVariant,
            dueContextColor = colorScheme.primary
        )
        DashboardUpcomingPaymentUrgency.STANDARD -> DashboardUpcomingPaymentUrgencyStyle(
            containerColor = colorScheme.surface,
            primaryTextColor = colorScheme.onSurface,
            secondaryTextColor = colorScheme.onSurfaceVariant,
            dueContextColor = colorScheme.primary
        )
    }
}

@Composable
private fun SummaryCard(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    val accessibilitySummary = summaryCardAccessibilitySummary(state = state)

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilitySummary
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
            modifier = Modifier.padding(FinanceTrackerSpacing.heroCardPadding)
        ) {
            Text(
                text = stringResource(R.string.dashboard_summary_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f)
            )
            Text(
                text = formatSummaryAmount(
                    amount = state.monthlyRecurringTotal,
                    hasMixedCurrencies = state.hasMixedActiveCurrencies
                ),
                style = MaterialTheme.typography.headlineMedium,
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
private fun summaryCardAccessibilitySummary(
    state: DashboardState
): String = buildList {
    add(stringResource(R.string.dashboard_summary_label))
    add(
        formatSummaryAmount(
            amount = state.monthlyRecurringTotal,
            hasMixedCurrencies = state.hasMixedActiveCurrencies
        )
    )
    add(
        if (state.activeEntryCount > 0) {
            stringResource(
                R.string.dashboard_summary_active_entries,
                state.activeEntryCount
            )
        } else {
            stringResource(R.string.dashboard_summary_no_active_entries)
        }
    )
    if (state.hasMixedActiveCurrencies) {
        add(
            stringResource(
                R.string.dashboard_summary_mixed_currencies,
                state.activeCurrencyCodes.size
            )
        )
    }
    currencyMetadataStatusText(state = state)?.let(::add)
}.joinToAccessibilitySummary()

@Composable
private fun CurrencyMetadataStatus(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    val statusText = currencyMetadataStatusText(state = state) ?: return
    val retryActionLabel = stringResource(R.string.dashboard_currency_retry_action_label)

    Column(
        verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact)
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
                onClick = { onAction(DashboardAction.RetryCurrencyMetadataClicked) },
                modifier = Modifier.semantics {
                    onClick(label = retryActionLabel, action = null)
                }
            ) {
                Text(text = stringResource(R.string.dashboard_currency_retry))
            }
        }
    }
}

@Composable
private fun currencyMetadataStatusText(
    state: DashboardState
): String? =
    when {
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
private fun LoadingStateCard() {
    DashboardStateMessageCard(
        body = stringResource(R.string.dashboard_loading),
        stateDescription = stringResource(R.string.dashboard_loading_accessibility_state),
        centered = true,
        leadingContent = {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun EmptyStateCard() {
    DashboardStateMessageCard(
        title = stringResource(R.string.dashboard_empty_title),
        body = stringResource(R.string.dashboard_empty_body)
    )
}

@Composable
private fun DashboardStateMessageCard(
    body: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    stateDescription: String? = null,
    centered: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null
) {
    val accessibilitySummary = listOfNotNull(title, body).joinToAccessibilitySummary()

    Card(
        colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilitySummary
                stateDescription?.let { this.stateDescription = it }
            }
    ) {
        Column(
            horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
            modifier = Modifier.padding(FinanceTrackerSpacing.heroCardPadding)
        ) {
            leadingContent?.invoke()
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = body,
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
    val accessibilitySummary = entry.toAccessibilitySummary()
    val accessibilityStateDescription = entry.accessibilityStatusLabel()
    val editActionLabel = stringResource(R.string.dashboard_active_entry_edit_action_label)

    Card(
        colors = FinanceTrackerComponentDefaults.surfaceCardColors(),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = editActionLabel,
                onClick = onClick
            )
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilitySummary
                stateDescription = accessibilityStateDescription
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.item),
            modifier = Modifier.padding(FinanceTrackerSpacing.cardPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(FinanceTrackerSpacing.compact),
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
                    entry.nextPaymentDate.toDashboardDisplayDate()
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

@Composable
private fun DashboardRelativeDueContext.toDisplayText(): String =
    when (this) {
        is DashboardRelativeDueContext.Overdue -> pluralStringResource(
            R.plurals.dashboard_due_overdue,
            daysOverdue,
            daysOverdue
        )
        DashboardRelativeDueContext.DueToday -> stringResource(R.string.dashboard_due_today)
        DashboardRelativeDueContext.DueTomorrow -> stringResource(R.string.dashboard_due_tomorrow)
        is DashboardRelativeDueContext.DueInDays -> pluralStringResource(
            R.plurals.dashboard_due_in_days,
            daysUntilDue,
            daysUntilDue
        )
    }

private data class DashboardUpcomingPaymentUrgencyStyle(
    val containerColor: androidx.compose.ui.graphics.Color,
    val primaryTextColor: androidx.compose.ui.graphics.Color,
    val secondaryTextColor: androidx.compose.ui.graphics.Color,
    val dueContextColor: androidx.compose.ui.graphics.Color
)

@Composable
private fun DashboardUpcomingPaymentUrgency.toAccessibilityDescription(): String? =
    when (this) {
        DashboardUpcomingPaymentUrgency.OVERDUE -> {
            stringResource(R.string.dashboard_upcoming_accessibility_overdue)
        }
        DashboardUpcomingPaymentUrgency.DUE_TODAY -> {
            stringResource(R.string.dashboard_upcoming_accessibility_due_today)
        }
        DashboardUpcomingPaymentUrgency.STANDARD -> null
    }

@Composable
private fun DashboardUpcomingPaymentItem.toAccessibilitySummary(
    urgency: DashboardUpcomingPaymentUrgency
): String {
    val formattedAmount = formatAmountForSavedCurrency(
        amount = amount,
        currencyCode = currencyCode
    )
    val formattedDate = nextPaymentDate.toDashboardDisplayDate()
    val relativeDueText = if (urgency == DashboardUpcomingPaymentUrgency.STANDARD) {
        relativeDueContext?.toDisplayText()
    } else {
        null
    }

    return if (relativeDueText == null) {
        stringResource(
            R.string.dashboard_upcoming_accessibility_summary,
            name,
            formattedAmount,
            formattedDate,
            category,
            currencyCode
        )
    } else {
        stringResource(
            R.string.dashboard_upcoming_accessibility_summary_with_due_context,
            name,
            formattedAmount,
            formattedDate,
            category,
            currencyCode,
            relativeDueText
        )
    }
}

@Composable
private fun DashboardRecurringEntryItem.toAccessibilitySummary(): String {
    val formattedAmount = formatAmountForSavedCurrency(
        amount = amount,
        currencyCode = currencyCode
    )
    val typeLabel = recurringEntryTypeLabel(type)
    val frequencyLabel = billingFrequencyLabel(billingFrequency)
    val formattedDate = nextPaymentDate.toDashboardDisplayDate()
    val statusLabel = accessibilityStatusLabel()

    return if (notes.isNullOrBlank()) {
        stringResource(
            R.string.dashboard_active_entry_accessibility_summary,
            name,
            formattedAmount,
            typeLabel,
            frequencyLabel,
            formattedDate,
            category,
            currencyCode,
            statusLabel
        )
    } else {
        stringResource(
            R.string.dashboard_active_entry_accessibility_summary_with_notes,
            name,
            formattedAmount,
            typeLabel,
            frequencyLabel,
            formattedDate,
            category,
            currencyCode,
            statusLabel,
            notes
        )
    }
}

@Composable
private fun DashboardRecurringEntryItem.accessibilityStatusLabel(): String =
    if (isActive) {
        stringResource(R.string.dashboard_status_active)
    } else {
        stringResource(R.string.dashboard_status_inactive)
    }

internal val UpcomingPaymentUrgencySemanticsKey =
    SemanticsPropertyKey<String>("UpcomingPaymentUrgency")

internal var androidx.compose.ui.semantics.SemanticsPropertyReceiver.upcomingPaymentUrgency
    by UpcomingPaymentUrgencySemanticsKey

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
                        category = "Streaming",
                        relativeDueContext = DashboardRelativeDueContext.DueToday
                    ),
                    DashboardUpcomingPaymentItem(
                        id = 2L,
                        name = "Rent",
                        amount = 190.0,
                        currencyCode = "EUR",
                        nextPaymentDate = "2026-04-01",
                        category = "Housing",
                        relativeDueContext = DashboardRelativeDueContext.Overdue(daysOverdue = 1)
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
                    )
                )
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
