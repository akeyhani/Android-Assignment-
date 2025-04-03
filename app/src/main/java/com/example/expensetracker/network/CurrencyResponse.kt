package com.example.expensetracker.network

import com.google.gson.annotations.SerializedName

class CurrencyResponse (
    val date: String,
    val rates: Map<String, Double>)