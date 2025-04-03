package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleAdapter(
    private val context: Context,
    private val expenseList: MutableList<ExpenseItem>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<RecView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)
        return RecView(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: RecView, position: Int) {
        val expense = expenseList[position]

        holder.apply {
            nameItem.text = expense.name
            amountItem.text = expense.amount.toString()

            deleteButton.setOnClickListener {
                expenseList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)

                // Save the updated list to file
                FileHelper.writeToFile(context, expenseList)

                onDataChanged() // update total, UI etc.
            }


            showDetail.setOnClickListener {
                val item = expenseList[position]
                val intent = Intent(context, ExpenseDetailsActivity::class.java)
                intent.putExtra("DETAIL", item)
                context.startActivity(intent)
            }
        }
    }
}
