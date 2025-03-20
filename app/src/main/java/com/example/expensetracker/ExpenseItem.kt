package com.example.expensetracker

import java.io.Serializable


class ExpenseItem(val name: String, val amount: Double, val date: String): Serializable {}