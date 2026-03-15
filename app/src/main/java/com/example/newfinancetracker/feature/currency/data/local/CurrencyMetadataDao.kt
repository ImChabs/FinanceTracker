package com.example.newfinancetracker.feature.currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyMetadataDao {
    @Query(
        """
        SELECT * FROM currency_metadata
        ORDER BY code ASC
        """
    )
    fun observeCurrencyMetadata(): Flow<List<CurrencyMetadataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCurrencyMetadata(entries: List<CurrencyMetadataEntity>)

    @Query("DELETE FROM currency_metadata WHERE code NOT IN (:codes)")
    suspend fun deleteCurrencyMetadataNotIn(codes: List<String>)

    @Transaction
    suspend fun replaceCurrencyMetadata(entries: List<CurrencyMetadataEntity>) {
        if (entries.isEmpty()) return

        upsertCurrencyMetadata(entries)
        deleteCurrencyMetadataNotIn(entries.map(CurrencyMetadataEntity::code))
    }
}
