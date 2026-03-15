package com.example.newfinancetracker.core.di

import android.content.Context
import androidx.room.Room
import com.example.newfinancetracker.core.database.FinanceTrackerDatabase
import com.example.newfinancetracker.feature.currency.data.local.CurrencyMetadataDao
import com.example.newfinancetracker.feature.recurring.data.local.RecurringEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinanceTrackerDatabase(
        @ApplicationContext context: Context
    ): FinanceTrackerDatabase =
        Room.databaseBuilder(
            context,
            FinanceTrackerDatabase::class.java,
            FinanceTrackerDatabase.DATABASE_NAME
        )
            .addMigrations(FinanceTrackerDatabase.MIGRATION_1_2)
            .addMigrations(FinanceTrackerDatabase.MIGRATION_2_3)
            .build()

    @Provides
    fun provideRecurringEntryDao(
        database: FinanceTrackerDatabase
    ): RecurringEntryDao = database.recurringEntryDao()

    @Provides
    fun provideCurrencyMetadataDao(
        database: FinanceTrackerDatabase
    ): CurrencyMetadataDao = database.currencyMetadataDao()
}
