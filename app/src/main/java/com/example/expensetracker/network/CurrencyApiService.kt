package com.example.expensetracker.network

import retrofit2.http.GET

interface CurrencyApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest/currencies/{from}/{to}.json")
    suspend fun getRate(
        @retrofit2.http.Path("from") from: String,
        @retrofit2.http.Path("to") to: String
    ): Map<String, Any>
}

