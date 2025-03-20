package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.expenseList)
        nameExpense = findViewById(R.id.expenseName)
        amount = findViewById(R.id.amount)
        dateInput = findViewById(R.id.expenseDate)
        submitButton = findViewById(R.id.addExpense)

        var expenseList = mutableListOf(
            ExpenseItem("item1", 100.0, "2025-03-20")
        )
       val adapter = RecAdapter(expenseList)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        submitButton.setOnClickListener {
           // expenseList.add(ExpenseItem(nameExpense.text.toString(), amount.text.toString().toDouble(), dateInput.text.toString()))
            adapter.notifyDataSetChanged()
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()
            if(name.isNullOrEmpty() || (amt.isNullOrEmpty() || amt.toDoubleOrNull() == null) || date.isNullOrEmpty()){
                Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show()
            }else{

                expenseList.add(ExpenseItem(name, amt.toDouble(),date))
                adapter.notifyDataSetChanged()//notify change to the view
                nameExpense.setText("")
                amount.setText("")
                dateInput.setText("")
            }

        }
        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker: DatePickerDialog = DatePickerDialog(this,
                {_, selectedYear, selectedMonth, selectedDay ->
                    dateInput.setText("$selectedYear-${selectedMonth+1}-$selectedDay")}
                ,year,month,day
            )
            datePicker.show()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}