package com.example.expensetracker

import java.io.Serializable

data class ExpenseItem(
    val name: String,
    val amount: Double,
    val date: String,
    var currency: String = "CAD",
    var convertedCost: Double = 0.0
) : Serializable

