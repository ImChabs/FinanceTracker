package com.example.newfinancetracker.feature.recurring.data.local

import androidx.room.TypeConverter
import com.example.newfinancetracker.feature.recurring.domain.model.BillingFrequency
import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntryType

class RecurringEntryTypeConverters {
    @TypeConverter
    fun fromRecurringEntryType(value: RecurringEntryType): String = value.name

    @TypeConverter
    fun toRecurringEntryType(value: String): RecurringEntryType = RecurringEntryType.valueOf(value)

    @TypeConverter
    fun fromBillingFrequency(value: BillingFrequency): String = value.name

    @TypeConverter
    fun toBillingFrequency(value: String): BillingFrequency = BillingFrequency.valueOf(value)
}
