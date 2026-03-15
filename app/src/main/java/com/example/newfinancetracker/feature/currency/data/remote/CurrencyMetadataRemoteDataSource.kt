package com.example.newfinancetracker.feature.currency.data.remote

import javax.inject.Inject

class CurrencyMetadataRemoteDataSource @Inject constructor(
    private val currencyMetadataService: CurrencyMetadataService
) {
    suspend fun fetchCurrencyMetadata(): List<CurrencyMetadataDto> =
        currencyMetadataService.getSupportedCurrencies()
            .entries
            .sortedBy { it.key }
            .map { (code, displayName) ->
                CurrencyMetadataDto(
                    code = code,
                    displayName = displayName
                )
            }
}
