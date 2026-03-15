package com.example.newfinancetracker.feature.currency.data.repository

import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataDao
import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataEntity
import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataRemoteDataSource
import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataService
import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class OfflineFirstCurrencyMetadataRepositoryTest {

    @Test
    fun `sync stores remote currencies locally and exposes mapped values`() = runTest {
        val dao = FakeCurrencyMetadataDao(
            initialEntries = listOf(
                CurrencyMetadataEntity(
                    code = "JPY",
                    displayName = "Japanese Yen"
                )
            )
        )
        val repository = OfflineFirstCurrencyMetadataRepository(
            currencyMetadataDao = dao,
            currencyMetadataRemoteDataSource = CurrencyMetadataRemoteDataSource(
                currencyMetadataService = FakeCurrencyMetadataService(
                    supportedCurrencies = mapOf(
                        "USD" to "United States Dollar",
                        "EUR" to "Euro"
                    )
                )
            )
        )

        val syncResult = repository.syncCurrencyMetadata()

        assertTrue(syncResult.isSuccess)
        assertEquals(
            listOf(
                CurrencyMetadata(
                    code = "EUR",
                    displayName = "Euro"
                ),
                CurrencyMetadata(
                    code = "USD",
                    displayName = "United States Dollar"
                )
            ),
            repository.observeCurrencyMetadata().first()
        )
    }

    @Test
    fun `sync failure leaves cached currencies unchanged`() = runTest {
        val cachedCurrency = CurrencyMetadataEntity(
            code = "GBP",
            displayName = "British Pound Sterling"
        )
        val dao = FakeCurrencyMetadataDao(initialEntries = listOf(cachedCurrency))
        val repository = OfflineFirstCurrencyMetadataRepository(
            currencyMetadataDao = dao,
            currencyMetadataRemoteDataSource = CurrencyMetadataRemoteDataSource(
                currencyMetadataService = FakeCurrencyMetadataService(
                    error = IOException("Network unavailable")
                )
            )
        )

        val syncResult = repository.syncCurrencyMetadata()

        assertTrue(syncResult.isFailure)
        assertEquals(
            listOf(
                CurrencyMetadata(
                    code = "GBP",
                    displayName = "British Pound Sterling"
                )
            ),
            repository.observeCurrencyMetadata().first()
        )
    }

    private class FakeCurrencyMetadataDao(
        initialEntries: List<CurrencyMetadataEntity> = emptyList()
    ) : CurrencyMetadataDao {
        private val entries = MutableStateFlow(initialEntries.sortedBy(CurrencyMetadataEntity::code))

        override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadataEntity>> = entries

        override suspend fun upsertCurrencyMetadata(entries: List<CurrencyMetadataEntity>) {
            val updatedEntries = this.entries.value
                .associateBy(CurrencyMetadataEntity::code)
                .toMutableMap()

            entries.forEach { entry ->
                updatedEntries[entry.code] = entry
            }

            this.entries.value = updatedEntries.values.sortedBy(CurrencyMetadataEntity::code)
        }

        override suspend fun deleteCurrencyMetadataNotIn(codes: List<String>) {
            entries.value = entries.value
                .filter { it.code in codes }
                .sortedBy(CurrencyMetadataEntity::code)
        }
    }

    private class FakeCurrencyMetadataService(
        private val supportedCurrencies: Map<String, String> = emptyMap(),
        private val error: Throwable? = null
    ) : CurrencyMetadataService {
        override suspend fun getSupportedCurrencies(): Map<String, String> {
            error?.let { throw it }
            return supportedCurrencies
        }
    }
}
