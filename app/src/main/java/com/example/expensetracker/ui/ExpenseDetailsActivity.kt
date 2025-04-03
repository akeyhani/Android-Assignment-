package com.example.expensetracker.ui
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker.model.ExpenseItem
import com.example.expensetracker.R


class ExpenseDetailsActivity : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var expenseAmount:TextView
    private lateinit var expenseDate:TextView
    private lateinit var backHome:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense_details)
        // Get the counter value from the intent
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("DETAIL", ExpenseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("DETAIL") as? ExpenseItem
        }
        //get text view and button
        name = findViewById(R.id.detailName)
        expenseAmount = findViewById(R.id.detailAmount)
        expenseDate = findViewById(R.id.detailDate)
        backHome = findViewById(R.id.backHome)

        name.setText("Expense Name: ${item?.name}")
        expenseAmount.setText("Expense Amount: ${item?.amount.toString()}")
        expenseDate.setText("Expense Date: ${item?.date.toString()}")

        backHome.setOnClickListener {
            finish()
        }

    }
}