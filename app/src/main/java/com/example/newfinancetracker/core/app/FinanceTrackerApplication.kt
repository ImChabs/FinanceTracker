package com.example.newfinancetracker.core.app

import android.app.Application
import androidx.room.Room
import com.example.newfinancetracker.core.database.FinanceTrackerDatabase
import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataRemoteDataSource
import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataService
import com.example.newfinancetracker.feature.currency.data.repository.OfflineFirstCurrencyMetadataRepository
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.data.repository.OfflineRecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FinanceTrackerApplication : Application() {
    val database: FinanceTrackerDatabase by lazy {
        Room.databaseBuilder(this, FinanceTrackerDatabase::class.java, FinanceTrackerDatabase.DATABASE_NAME)
            .addMigrations(FinanceTrackerDatabase.MIGRATION_1_2)
            .build()
    }

    private val currencyMetadataService: CurrencyMetadataService by lazy {
        Retrofit.Builder()
            .baseUrl(CurrencyMetadataService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyMetadataService::class.java)
    }

    val recurringEntryRepository: RecurringEntryRepository by lazy {
        OfflineRecurringEntryRepository(
            recurringEntryDao = database.recurringEntryDao()
        )
    }

    val currencyMetadataRepository: CurrencyMetadataRepository by lazy {
        OfflineFirstCurrencyMetadataRepository(
            currencyMetadataDao = database.currencyMetadataDao(),
            currencyMetadataRemoteDataSource = CurrencyMetadataRemoteDataSource(
                currencyMetadataService = currencyMetadataService
            )
        )
    }
}
