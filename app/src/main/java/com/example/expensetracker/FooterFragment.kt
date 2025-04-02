package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.expensetracker.R.*


class FooterFragment : Fragment() {
    private lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_footer, container, false)
        textView = view.findViewById(R.id.footerTextView)
        return view
    }
    companion object {
        fun newInstance(): Fragment {
            return FooterFragment()
        }
    }
    fun updateTotalExpensesDisplay(totalExpenses: Double) {
        textView.text = "Total Expenses: $%.2f".format(totalExpenses)
    }
}