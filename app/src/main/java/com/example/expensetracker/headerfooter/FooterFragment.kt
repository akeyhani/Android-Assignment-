package com.example.expensetracker.headerfooter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.expensetracker.R

class FooterFragment : Fragment() {

    private var textView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        textView = view.findViewById(R.id.footerTextView)
        return view
    }

    fun updateTotalExpensesDisplay(totalExpenses: Double) {
        textView?.text = "Total Expenses: $%.2f".format(totalExpenses)
    }

    companion object {
        fun newInstance(): FooterFragment = FooterFragment()
    }
}
