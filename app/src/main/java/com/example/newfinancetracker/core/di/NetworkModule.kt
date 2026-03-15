package com.example.newfinancetracker.core.di

import com.example.newfinancetracker.feature.currency.data.remote.CurrencyMetadataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(CurrencyMetadataService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCurrencyMetadataService(
        retrofit: Retrofit
    ): CurrencyMetadataService = retrofit.create(CurrencyMetadataService::class.java)
}
