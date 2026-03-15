package com.example.newfinancetracker.feature.currency.data.repository

import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataDao
import com.example.newfinancetracker.feature.currency.data.local.toDomain
import com.example.newfinancetracker.feature.currency.data.local.toEntity
import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataRemoteDataSource
import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstCurrencyMetadataRepository @Inject constructor(
    private val currencyMetadataDao: CurrencyMetadataDao,
    private val currencyMetadataRemoteDataSource: CurrencyMetadataRemoteDataSource
) : CurrencyMetadataRepository {

    override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> =
        currencyMetadataDao.observeCurrencyMetadata().map { entries ->
            entries.map { it.toDomain() }
        }

    override suspend fun syncCurrencyMetadata(): Result<Unit> =
        runCatching {
            val remoteCurrencyMetadata = currencyMetadataRemoteDataSource.fetchCurrencyMetadata()

            check(remoteCurrencyMetadata.isNotEmpty()) {
                "Currency metadata sync returned no supported currencies."
            }

            currencyMetadataDao.replaceCurrencyMetadata(
                remoteCurrencyMetadata.map { it.toEntity() }
            )
        }
}
