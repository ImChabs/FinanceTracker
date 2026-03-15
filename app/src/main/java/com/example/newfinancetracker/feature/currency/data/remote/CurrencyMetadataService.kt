package com.example.newfinancetracker.feature.currency.data.remote

import retrofit2.http.GET

interface CurrencyMetadataService {
    @GET("currencies")
    suspend fun getSupportedCurrencies(): Map<String, String>

    companion object {
        const val BASE_URL: String = "https://api.frankfurter.dev/v1/"
    }
}
