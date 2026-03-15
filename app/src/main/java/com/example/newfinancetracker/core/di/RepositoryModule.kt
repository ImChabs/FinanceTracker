package com.example.newfinancetracker.core.di

import com.example.newfinancetracker.feature.currency.data.repository.OfflineFirstCurrencyMetadataRepository
import com.example.newfinancetracker.feature.currency.domain.repository.CurrencyMetadataRepository
import com.example.newfinancetracker.feature.recurring.data.repository.OfflineRecurringEntryRepository
import com.example.newfinancetracker.feature.recurring.domain.repository.RecurringEntryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecurringEntryRepository(
        repository: OfflineRecurringEntryRepository
    ): RecurringEntryRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyMetadataRepository(
        repository: OfflineFirstCurrencyMetadataRepository
    ): CurrencyMetadataRepository
}
