package com.example.newfinancetracker.feature.currency.data.local

import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataDto
import com.example.newfinancetracker.feature.currency.domain.model.CurrencyMetadata
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyMetadataMappersTest {

    @Test
    fun `entity maps to matching domain model`() {
        val entity = CurrencyMetadataEntity(
            code = "USD",
            displayName = "United States Dollar"
        )

        val domainModel = entity.toDomain()

        assertEquals(
            CurrencyMetadata(
                code = "USD",
                displayName = "United States Dollar"
            ),
            domainModel
        )
    }

    @Test
    fun `remote dto maps to matching entity`() {
        val remoteDto = CurrencyMetadataDto(
            code = "EUR",
            displayName = "Euro"
        )

        val entity = remoteDto.toEntity()

        assertEquals(
            CurrencyMetadataEntity(
                code = "EUR",
                displayName = "Euro"
            ),
            entity
        )
    }
}
