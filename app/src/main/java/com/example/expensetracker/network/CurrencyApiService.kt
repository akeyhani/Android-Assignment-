package com.example.expensetracker.network

import retrofit2.http.GET

interface CurrencyApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest/currencies/cad.json")
    suspend fun getRates(): Map<String, Map<String, Double>>
}
