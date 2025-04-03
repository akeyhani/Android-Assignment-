package com.example.expensetracker.model

import java.io.Serializable

data class ExpenseItem(
    val name: String,
    val amount: Double,
    val date: String,
    var currency: String = "CAD",
    var convertedCost: Double = 0.0,
    val currencyCode: String = "CAD"
) : Serializable

