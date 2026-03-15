package com.example.newfinancetracker.feature.dashboard.presentation

import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

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
    fun `init syncs currency metadata once and exposes cached count`() = runTest(testDispatcher) {
        val recurringRepository = FakeRecurringEntryRepository(
            entries = listOf(ACTIVE_ENTRY)
        )
        val currencyRepository = FakeCurrencyMetadataRepository(
            syncedMetadata = listOf(
                CurrencyMetadata(code = "EUR", displayName = "Euro"),
                CurrencyMetadata(code = "USD", displayName = "United States Dollar")
            )
        )

        val viewModel = DashboardViewModel(
            recurringEntryRepository = recurringRepository,
            currencyMetadataRepository = currencyRepository
        )

        advanceUntilIdle()

        assertEquals(1, currencyRepository.syncCallCount)
        assertEquals(1, viewModel.state.value.savedEntryCount)
        assertEquals(1, viewModel.state.value.activeEntryCount)
        assertEquals(2, viewModel.state.value.currencyMetadataCount)
        assertFalse(viewModel.state.value.hasCurrencySyncFailure)
    }

    @Test
    fun `currency sync failure leaves recurring dashboard content available`() = runTest(testDispatcher) {
        val recurringRepository = FakeRecurringEntryRepository(
            entries = listOf(ACTIVE_ENTRY)
        )
        val currencyRepository = FakeCurrencyMetadataRepository(
            initialMetadata = listOf(
                CurrencyMetadata(code = "JPY", displayName = "Japanese Yen")
            ),
            syncFailure = IOException("Offline")
        )

        val viewModel = DashboardViewModel(
            recurringEntryRepository = recurringRepository,
            currencyMetadataRepository = currencyRepository
        )

        advanceUntilIdle()

        assertEquals(1, currencyRepository.syncCallCount)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(1, viewModel.state.value.savedEntryCount)
        assertEquals(1, viewModel.state.value.currencyMetadataCount)
        assertTrue(viewModel.state.value.hasCurrencySyncFailure)
    }

    private class FakeRecurringEntryRepository(
        entries: List<RecurringEntry>
    ) : RecurringEntryRepository {

        private val recurringEntries = MutableStateFlow(entries)

        override fun observeRecurringEntries(): Flow<List<RecurringEntry>> = recurringEntries

        override suspend fun getRecurringEntry(entryId: Long): RecurringEntry? =
            recurringEntries.value.firstOrNull { it.id == entryId }

        override suspend fun upsertRecurringEntry(entry: RecurringEntry): Long = entry.id

        override suspend fun deleteRecurringEntry(entryId: Long) {
            recurringEntries.value = recurringEntries.value.filterNot { it.id == entryId }
        }
    }

    private class FakeCurrencyMetadataRepository(
        initialMetadata: List<CurrencyMetadata> = emptyList(),
        private val syncedMetadata: List<CurrencyMetadata> = initialMetadata,
        private val syncFailure: Throwable? = null
    ) : CurrencyMetadataRepository {

        private val metadata = MutableStateFlow(initialMetadata)

        var syncCallCount: Int = 0
            private set

        override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> = metadata

        override suspend fun syncCurrencyMetadata(): Result<Unit> {
            syncCallCount += 1
            syncFailure?.let { return Result.failure(it) }

            metadata.value = syncedMetadata
            return Result.success(Unit)
        }
    }

    private companion object {
        val ACTIVE_ENTRY = RecurringEntry(
            id = 12L,
            name = "Streaming",
            amount = 14.99,
            billingFrequency = BillingFrequency.MONTHLY,
            nextPaymentDate = "2026-03-20",
            category = "Entertainment",
            type = RecurringEntryType.SUBSCRIPTION,
            isActive = true,
            notes = null
        )
    }
}
