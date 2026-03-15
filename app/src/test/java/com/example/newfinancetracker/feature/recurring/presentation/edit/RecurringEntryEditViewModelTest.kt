package com.example.newfinancetracker.feature.recurring.presentation.edit

import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecurringEntryEditViewModelTest {

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
    fun `delete clicked shows confirmation for existing entry`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository()
        val viewModel = RecurringEntryEditViewModel(
            entryId = EXISTING_ENTRY.id,
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository()
        )

        advanceUntilIdle()
        viewModel.onAction(RecurringEntryEditAction.DeleteClicked)

        assertTrue(viewModel.state.value.isDeleteConfirmationVisible)
        assertFalse(viewModel.state.value.hasDeleteError)
    }

    @Test
    fun `delete confirmed removes entry and navigates back`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository()
        val viewModel = RecurringEntryEditViewModel(
            entryId = EXISTING_ENTRY.id,
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository()
        )

        advanceUntilIdle()
        viewModel.onAction(RecurringEntryEditAction.DeleteClicked)
        val effect = async { viewModel.effects.first() }

        viewModel.onAction(RecurringEntryEditAction.DeleteConfirmed)
        advanceUntilIdle()

        assertEquals(listOf(EXISTING_ENTRY.id), repository.deletedEntryIds)
        assertEquals(RecurringEntryEditEffect.NavigateBack, effect.await())
        assertFalse(viewModel.state.value.isDeleteConfirmationVisible)
        assertFalse(viewModel.state.value.hasDeleteError)
        assertFalse(viewModel.state.value.isDeleting)
    }

    @Test
    fun `delete failure keeps user on edit screen and surfaces error`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository(shouldFailDelete = true)
        val viewModel = RecurringEntryEditViewModel(
            entryId = EXISTING_ENTRY.id,
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository()
        )

        advanceUntilIdle()
        viewModel.onAction(RecurringEntryEditAction.DeleteClicked)
        viewModel.onAction(RecurringEntryEditAction.DeleteConfirmed)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.hasDeleteError)
        assertFalse(viewModel.state.value.isDeleteConfirmationVisible)
        assertFalse(viewModel.state.value.isDeleting)
    }

    @Test
    fun `saved currency stays available when cached metadata does not contain it`() = runTest(testDispatcher) {
        val repository = FakeRecurringEntryRepository()
        val viewModel = RecurringEntryEditViewModel(
            entryId = EXISTING_ENTRY.id,
            recurringEntryRepository = repository,
            currencyMetadataRepository = FakeCurrencyMetadataRepository(
                initialMetadata = listOf(
                    CurrencyMetadata(code = "EUR", displayName = "Euro")
                )
            )
        )

        advanceUntilIdle()

        assertEquals("JPY", viewModel.state.value.form.currencyCode)
        assertEquals("JPY", viewModel.state.value.currencyOptions.first().code)
        assertEquals("EUR", viewModel.state.value.currencyOptions.last().code)
    }

    private class FakeRecurringEntryRepository(
        private val shouldFailDelete: Boolean = false
    ) : RecurringEntryRepository {

        private val entries = MutableStateFlow(listOf(EXISTING_ENTRY))
        val deletedEntryIds = mutableListOf<Long>()

        override fun observeRecurringEntries(): Flow<List<RecurringEntry>> = entries

        override suspend fun getRecurringEntry(entryId: Long): RecurringEntry? =
            entries.value.firstOrNull { it.id == entryId }

        override suspend fun upsertRecurringEntry(entry: RecurringEntry): Long = entry.id

        override suspend fun deleteRecurringEntry(entryId: Long) {
            if (shouldFailDelete) {
                error("Delete failed")
            }

            deletedEntryIds += entryId
            entries.value = entries.value.filterNot { it.id == entryId }
        }
    }

    private class FakeCurrencyMetadataRepository(
        initialMetadata: List<CurrencyMetadata> = emptyList()
    ) : CurrencyMetadataRepository {
        private val metadata = MutableStateFlow(initialMetadata)

        override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> = metadata

        override suspend fun syncCurrencyMetadata(): Result<Unit> = Result.success(Unit)
    }

    private companion object {
        val EXISTING_ENTRY = RecurringEntry(
            id = 7L,
            name = "Rent",
            amount = 1450.0,
            currencyCode = "JPY",
            billingFrequency = BillingFrequency.MONTHLY,
            nextPaymentDate = "2026-04-01",
            category = "Housing",
            type = RecurringEntryType.RECURRING_EXPENSE,
            isActive = true,
            notes = null
        )
    }
}
