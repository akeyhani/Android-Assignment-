package com.example.expensetracker

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseapp.util.FileHelper
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var recycleView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    private lateinit var expenseList: MutableList<ExpenseItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ExpenseTrackerLog", "onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recycleView = findViewById(R.id.expenseList)
        nameExpense = findViewById(R.id.expenseName)
        amount = findViewById(R.id.amount)
        dateInput = findViewById(R.id.expenseDate)
        submitButton = findViewById(R.id.addExpense)
        financialTip = findViewById(R.id.finsTips)

        expenseList = mutableListOf()

        val adapter = RecycleAdapter(this, this, expenseList)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        // Load from file
        expenseList.clear()
        expenseList.addAll(FileHelper.readFromFile(this))
        adapter.notifyDataSetChanged()

        submitButton.setOnClickListener {
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()

            if (name.isEmpty() || amt.isEmpty() || amt.toDoubleOrNull() == null || date.isEmpty()) {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show()
            } else {
                expenseList.add(ExpenseItem(name, amt.toDouble(), date))
                adapter.notifyDataSetChanged()
                FileHelper.writeToFile(this, expenseList) // Save to file here

                nameExpense.setText("")
                amount.setText("")
                dateInput.setText("")
            }

            updateTotalExpense()
        }

        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    dateInput.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
                },
                year, month, day
            )
            datePicker.show()
        }

        financialTip.setOnClickListener {
            val financialTipsUrl = "https://www.themuse.com/advice/50-personal-finance-tips-that-will-change-the-way-you-think-about-money"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(financialTipsUrl))
            startActivity(intent)
        }

        val headerFragment = HeaderFragment.newInstance()
        val footerFragment = FooterFragment.newInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.headerFragment, headerFragment)
        transaction.add(R.id.footerFragment, footerFragment)
        transaction.commit()

        val transaction2 = supportFragmentManager.beginTransaction()
        transaction2.replace(R.id.headerFragment, headerFragment)
        transaction2.addToBackStack(null)
        transaction2.commit()

        updateTotalExpense()
    }

    fun updateTotalExpense() {
        val footer = supportFragmentManager.findFragmentById(R.id.footerFragment) as FooterFragment?
        footer?.updateTotalExpensesDisplay(expenseList.sumOf { it.amount })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ExpenseTrackerLog", "onStart is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ExpenseTrackerLog", "onPause is called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ExpenseTrackerLog", "onResume is called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ExpenseTrackerLog", "onStop is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ExpenseTrackerLog", "onDestroy is called")
    }
}
