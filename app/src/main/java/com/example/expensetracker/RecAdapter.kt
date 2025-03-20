package com.example.expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecAdapter(var expenseList: MutableList<ExpenseItem>): RecyclerView.Adapter<RecView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)

        return RecView(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: RecView, position: Int) {
        holder.apply {
            nameItem.text = expenseList[position].name
            amountItem.text = expenseList[position].amount.toString()
            deleteButton.setOnClickListener {
                expenseList.removeAt(position)
            }
        }
    }
}