package com.example.expenseapp.util

import android.content.Context
import com.example.expenseapp.model.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FileHelper {
    private const val FILE_NAME = "expenses.json"

    fun writeToFile(context: Context, expenses: List<Expense>) {
        val json = Gson().toJson(expenses)
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun readFromFile(context: Context): List<Expense> {
        return try {
            val json = context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Expense>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
