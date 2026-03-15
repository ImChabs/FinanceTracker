package com.example.newfinancetracker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataDao
import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataEntity
import com.example.newfinancetracker.feature.recurring.domain.model.DEFAULT_CURRENCY_CODE
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryDao
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryEntity
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryTypeConverters

@Database(
    entities = [RecurringEntryEntity::class, CurrencyMetadataEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(RecurringEntryTypeConverters::class)
abstract class FinanceTrackerDatabase : RoomDatabase() {
    abstract fun recurringEntryDao(): RecurringEntryDao
    abstract fun currencyMetadataDao(): CurrencyMetadataDao

    companion object {
        const val DATABASE_NAME: String = "finance-tracker.db"

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `currency_metadata` (
                        `code` TEXT NOT NULL,
                        `display_name` TEXT NOT NULL,
                        PRIMARY KEY(`code`)
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE `recurring_entries`
                    ADD COLUMN `currency_code` TEXT NOT NULL DEFAULT '$DEFAULT_CURRENCY_CODE'
                    """.trimIndent()
                )
            }
        }
    }
}
