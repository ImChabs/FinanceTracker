package com.example.newfinancetracker.feature.currency.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_metadata")
data class CurrencyMetadataEntity(
    @PrimaryKey
    val code: String,
    @ColumnInfo(name = "display_name")
    val displayName: String
)
