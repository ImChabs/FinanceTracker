package com.example.newfinancetracker.feature.currency.domain.repository

import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import kotlinx.coroutines.flow.Flow

interface CurrencyMetadataRepository {
    fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>>

    suspend fun syncCurrencyMetadata(): Result<Unit>
}
