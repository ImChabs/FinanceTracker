package com.example.newfinancetracker.core.app

import android.app.Application
import androidx.room.Room
import com.example.newfinancetracker.core.database.FinanceTrackerDatabase
import com.example.newfinancetracker.feature.recurring.data.repository.OfflineRecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository

class FinanceTrackerApplication : Application() {
    val database: FinanceTrackerDatabase by lazy {
        Room.databaseBuilder(this, FinanceTrackerDatabase::class.java, FinanceTrackerDatabase.DATABASE_NAME)
            .build()
    }

    val recurringEntryRepository: RecurringEntryRepository by lazy {
        OfflineRecurringEntryRepository(
            recurringEntryDao = database.recurringEntryDao()
        )
    }
}
