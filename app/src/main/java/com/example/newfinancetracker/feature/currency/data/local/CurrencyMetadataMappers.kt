package com.example.newfinancetracker.feature.currency.data.local

import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataDto
import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata

fun CurrencyMetadataEntity.toDomain(): CurrencyMetadata =
    CurrencyMetadata(
        code = code,
        displayName = displayName
    )

fun CurrencyMetadataDto.toEntity(): CurrencyMetadataEntity =
    CurrencyMetadataEntity(
        code = code,
        displayName = displayName
    )
