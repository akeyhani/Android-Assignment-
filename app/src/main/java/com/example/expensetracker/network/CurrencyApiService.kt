package com.example.expensetracker.network

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    @GET("currencies/{from}.json")
    suspend fun getRate(
        @Path("from") from: String
    ): Map<String, Any>


}


