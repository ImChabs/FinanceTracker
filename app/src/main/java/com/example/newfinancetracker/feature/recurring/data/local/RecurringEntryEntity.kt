package com.example.newfinancetracker.feature.recurring.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

@Entity(tableName = "recurring_entries")
data class RecurringEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Double,
    @ColumnInfo(name = "currency_code")
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    @ColumnInfo(name = "billing_frequency")
    val billingFrequency: BillingFrequency,
    @ColumnInfo(name = "next_payment_date")
    val nextPaymentDate: String,
    val category: String,
    val type: RecurringEntryType,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    val notes: String?
)
