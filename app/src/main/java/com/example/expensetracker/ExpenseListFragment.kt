package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.network.RetrofitInstance
import kotlinx.coroutines.launch

class ExpenseListFragment : Fragment() {


    private lateinit var nameExpense: EditText
    private lateinit var amount: EditText
    private lateinit var dateInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var financialTip: Button
    private lateinit var expenseList: MutableList<ExpenseItem>
    private lateinit var adapter: RecycleAdapter
    private lateinit var currencySpinner: Spinner
    private lateinit var convertSwitch: Switch
    private lateinit var convertedCostText: TextView
    private var currencyList: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔌 View binding
        nameExpense = view.findViewById(R.id.expenseName)
        amount = view.findViewById(R.id.amount)
        dateInput = view.findViewById(R.id.expenseDate)
        submitButton = view.findViewById(R.id.addExpense)
        financialTip = view.findViewById(R.id.finsTips)
        recyclerView = view.findViewById(R.id.expenseList)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        convertSwitch = view.findViewById(R.id.convertSwitch)
        convertedCostText = view.findViewById(R.id.convertedCost)


        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrencies()
                Log.d("CurrencyDebug", "Currencies fetched: $response")
                currencyList = response.keys.sorted()

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                currencySpinner.adapter = adapter

                // Optionally select CAD by default
                val defaultIndex = currencyList.indexOf("cad")
                if (defaultIndex != -1) {
                    currencySpinner.setSelection(defaultIndex)
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load currencies", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        // 📦 Load expenses from file
        expenseList = FileHelper.readFromFile(requireContext()).toMutableList()

        // 🔁 Adapter with callback to update footer total
        adapter = RecycleAdapter(requireContext(), expenseList) {
            updateTotalExpense()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.notifyDataSetChanged() // 👈 wakes up the UI with actual data

        // ➕ Add expense
        submitButton.setOnClickListener {
            val name = nameExpense.text.toString().trim()
            val amt = amount.text.toString().trim()
            val date = dateInput.text.toString().trim()

            if (name.isEmpty() || amt.isEmpty() || amt.toDoubleOrNull() == null || date.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
            } else {
                expenseList.add(ExpenseItem(name, amt.toDouble(), date))
                FileHelper.writeToFile(requireContext(), expenseList)
                Log.d("FileHelper", "WRITE CALLED: ${expenseList.size}")
                adapter.notifyDataSetChanged()
                updateTotalExpense()

                nameExpense.setText("")
                amount.setText("")
                dateInput.setText("")
            }
        }

        // 📆 Date picker
        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(),
                { _, y, m, d -> dateInput.setText("$y-${m + 1}-$d") },
                year, month, day
            ).show()
        }

        // 💡 Navigate to details fragment
        financialTip.setOnClickListener {
            findNavController().navigate(R.id.action_expenseListFragment_to_expenseDetailsFragment)
        }


        updateTotalExpense()
    }

    // 📊 Update footer fragment total
    private fun updateTotalExpense() {
        val footer = childFragmentManager.findFragmentById(R.id.footerFragment) as? FooterFragment
        footer?.updateTotalExpensesDisplay(expenseList.sumOf { it.amount })
    }
}
