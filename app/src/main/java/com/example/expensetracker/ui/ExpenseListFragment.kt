package com.example.expensetracker.ui

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
import com.example.expensetracker.model.ExpenseItem
import com.example.expensetracker.headerfooter.FooterFragment
import com.example.expensetracker.R
import com.example.expensetracker.adapters.RecycleAdapter
import com.example.expensetracker.network.RetrofitInstance
import com.example.expensetracker.utils.FileHelper
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

                currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (convertSwitch.isChecked) updateConvertedAmount()
                    }



                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }




                val defaultIndex = currencyList.indexOf("cad")
                if (defaultIndex != -1) {
                    currencySpinner.setSelection(defaultIndex)
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load currencies", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
        amount.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                if (convertSwitch.isChecked) updateConvertedAmount()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        expenseList = FileHelper.readFromFile(requireContext()).toMutableList()

        adapter = RecycleAdapter(requireContext(), expenseList) {
            updateTotalExpense()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()

        submitButton.setOnClickListener {
            lifecycleScope.launch {
                val name = nameExpense.text.toString().trim()
                val amt = amount.text.toString().trim()
                val date = dateInput.text.toString().trim()

                if (name.isEmpty() || amt.isEmpty() || amt.toDoubleOrNull() == null || date.isEmpty()) {
                    Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                var amountValue = amt.toDouble()

                if (convertSwitch.isChecked) {
                    val selectedCurrency = currencySpinner.selectedItem.toString().lowercase()
                    try {
                        val response = RetrofitInstance.api.getRate(selectedCurrency)
                        val rateMap = response[selectedCurrency] as? Map<*, *>
                        val rate = rateMap?.get("cad") as? Double

                        if (rate == null) {
                            throw Exception("Conversion rate not found for CAD")
                        }

                        amountValue *= rate
                        convertedCostText.text = "≈ %.2f CAD".format(amountValue)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Conversion failed", Toast.LENGTH_SHORT).show()
                        Log.e("ConversionError", "Error: ${e.message}")
                        return@launch
                    }
                }

                expenseList.add(ExpenseItem(name, amountValue, date, "CAD"))
                FileHelper.writeToFile(requireContext(), expenseList)
                adapter.notifyDataSetChanged()
                updateTotalExpense()

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

            DatePickerDialog(requireContext(),
                { _, y, m, d -> dateInput.setText("$y-${m + 1}-$d") },
                year, month, day
            ).show()
        }

        financialTip.setOnClickListener {
            findNavController().navigate(R.id.action_expenseListFragment_to_expenseDetailsFragment)
        }

        convertSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                convertedCostText.text = ""
            }
        }

        updateTotalExpense()
    }

    private fun updateTotalExpense() {
        val footer = childFragmentManager.findFragmentById(R.id.footerFragment) as? FooterFragment
        footer?.updateTotalExpensesDisplay(expenseList.sumOf { it.amount })
    }


    private fun updateConvertedAmount() {
        val amountText = amount.text.toString().trim()
        val selectedCurrency = currencySpinner.selectedItem?.toString()?.lowercase() ?: return

        val amountValue = amountText.toDoubleOrNull() ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getRate(selectedCurrency)
                val rateMap = response[selectedCurrency] as? Map<*, *>
                val rate = rateMap?.get("cad") as? Double

                if (rate != null) {
                    val converted = rate * amountValue
                    convertedCostText.text = "≈ $converted CAD"
                } else {
                    convertedCostText.text = ""
                }
            } catch (e: Exception) {
                Log.e("ConversionLive", "Failed to convert: ${e.message}")
                convertedCostText.text = ""
            }
        }
    }

}


