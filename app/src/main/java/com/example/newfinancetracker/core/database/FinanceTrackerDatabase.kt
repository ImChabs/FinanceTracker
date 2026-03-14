package com.example.newfinancetracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryDao
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryEntity
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryTypeConverters

@Database(
    entities = [RecurringEntryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecurringEntryTypeConverters::class)
abstract class FinanceTrackerDatabase : RoomDatabase() {
    abstract fun recurringEntryDao(): RecurringEntryDao

    companion object {
        const val DATABASE_NAME: String = "finance-tracker.db"
    }
}
