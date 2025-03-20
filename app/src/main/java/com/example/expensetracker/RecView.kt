package com.example.expensetracker


import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RecView(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var nameItem: TextView
    lateinit var amountItem: TextView
    lateinit var deleteButton: Button

    init {
        nameItem = itemView.findViewById(R.id.name_item)
        amountItem = itemView.findViewById(R.id.amount_item)
        deleteButton = itemView.findViewById(R.id.delete_item)
    }
}