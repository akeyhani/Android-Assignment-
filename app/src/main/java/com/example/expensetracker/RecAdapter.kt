package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleAdapter(private val context: Context, var expenseList: MutableList<ExpenseItem>): RecyclerView.Adapter<RecView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)
        return RecView(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: RecView, position: Int) {
        val expense = expenseList[position] // Get correct item

        holder.apply {
            nameItem.text = expense.name
            amountItem.text = expense.amount.toString()

            deleteButton.setOnClickListener {
                // Remove item from list and update RecyclerView
                expenseList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
            showDetail.setOnClickListener {
                val item = expenseList[position]
                val intent = Intent(context, ExpenseDetailsActivity::class.java)
                intent.putExtra("DETAIL", item) // add the data
                context.startActivity(intent)
            }
        }
    }
}


