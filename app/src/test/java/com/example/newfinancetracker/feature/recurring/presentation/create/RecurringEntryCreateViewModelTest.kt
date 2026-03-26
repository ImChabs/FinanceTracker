package com.example.newfinancetracker.feature.recurring.presentation.create

import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecurringEntryCreateViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cached currency metadata populates form options and save persists selected currency`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository()
        val viewModel = RecurringEntryCreateViewModel(
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository(
                initialMetadata = listOf(
                    CurrencyMetadata(code = "EUR", displayName = "Euro"),
                    CurrencyMetadata(code = "USD", displayName = "United States Dollar")
                )
            )
        )

        advanceUntilIdle()

        assertEquals("USD", viewModel.state.value.form.currencyCode)
        assertEquals(listOf("EUR", "USD"), viewModel.state.value.currencyOptions.map { it.code })

        viewModel.onAction(RecurringEntryCreateAction.NameChanged("Music Streaming"))
        viewModel.onAction(RecurringEntryCreateAction.AmountChanged("10.99"))
        viewModel.onAction(RecurringEntryCreateAction.CurrencyCodeChanged("USD"))
        viewModel.onAction(RecurringEntryCreateAction.CategoryChanged("Entertainment"))
        viewModel.onAction(RecurringEntryCreateAction.NextPaymentDateChanged("2026-05-01"))
        viewModel.onAction(
            RecurringEntryCreateAction.BillingFrequencyChanged(BillingFrequency.MONTHLY)
        )
        viewModel.onAction(
            RecurringEntryCreateAction.TypeChanged(RecurringEntryType.SUBSCRIPTION)
        )

        val effect = async { viewModel.effects.first() }
        viewModel.onAction(RecurringEntryCreateAction.SaveClicked)
        advanceUntilIdle()

        assertEquals(RecurringEntryCreateEffect.NavigateBack, effect.await())
        assertEquals("USD", repository.upsertedEntries.single().currencyCode)
    }

    @Test
    fun `missing cached metadata falls back to default currency option`() = runTest(testDispatcher) {
        val viewModel = RecurringEntryCreateViewModel(
            recurringEntryRepository = FakeRecurringEntryRepository(),
            currencyMetadataRepository = FakeCurrencyMetadataRepository()
        )

        advanceUntilIdle()

        assertEquals(DEFAULT_CURRENCY_CODE, viewModel.state.value.form.currencyCode)
        assertEquals(
            listOf(DEFAULT_CURRENCY_CODE),
            viewModel.state.value.currencyOptions.map { it.code }
        )
    }

    @Test
    fun `save accepts grouped amount input and persists numeric value`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository()
        val viewModel = RecurringEntryCreateViewModel(
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository()
        )

        advanceUntilIdle()

        viewModel.onAction(RecurringEntryCreateAction.NameChanged("Insurance"))
        viewModel.onAction(RecurringEntryCreateAction.AmountChanged("1,250.75"))
        viewModel.onAction(RecurringEntryCreateAction.CategoryChanged("Bills"))
        viewModel.onAction(RecurringEntryCreateAction.NextPaymentDateChanged("2026-05-12"))

        viewModel.onAction(RecurringEntryCreateAction.SaveClicked)
        advanceUntilIdle()

        assertEquals(1250.75, repository.upsertedEntries.single().amount, 0.0)
    }

    private class FakeRecurringEntryRepository : RecurringEntryRepository {
        private val entries = MutableStateFlow<List<RecurringEntry>>(emptyList())
        val upsertedEntries = mutableListOf<RecurringEntry>()

        override fun observeRecurringEntries(): Flow<List<RecurringEntry>> = entries

        override suspend fun getRecurringEntry(entryId: Long): RecurringEntry? = null

        override suspend fun upsertRecurringEntry(entry: RecurringEntry): Long {
            upsertedEntries += entry
            return entry.id
        }

        override suspend fun deleteRecurringEntry(entryId: Long) = Unit
    }

    private class FakeCurrencyMetadataRepository(
        initialMetadata: List<CurrencyMetadata> = emptyList()
    ) : CurrencyMetadataRepository {
        private val metadata = MutableStateFlow(initialMetadata)

        override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> = metadata

        override suspend fun syncCurrencyMetadata(): Result<Unit> = Result.success(Unit)
    }
}
