package com.example.expensetracker.utils

import android.content.Context
import android.util.Log
import com.example.expensetracker.model.ExpenseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FileHelper {
    private const val FILE_NAME = "expenses.json"

    fun writeToFile(context: Context, expenses: List<ExpenseItem>) {
        try {
            val json = Gson().toJson(expenses)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
            Log.d("FileHelper", "Expenses saved: ${expenses.size}")
        } catch (e: Exception) {
            Log.e("FileHelper", "Error writing to file", e)
        }
    }

    fun readFromFile(context: Context): List<ExpenseItem> {
        return try {
            val json = context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<ExpenseItem>>() {}.type
            val result = Gson().fromJson<List<ExpenseItem>>(json, type)
            Log.d("FileHelper", "Loaded expenses: ${result.size}")
            result
        } catch (e: Exception) {
            Log.e("FileHelper", "Error reading file", e)
            emptyList()
        }
    }
}
