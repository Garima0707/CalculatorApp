package com.example.calculator2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // TextView to display results
    private lateinit var resultsTextView: TextView

    // TextView to display the last operator
    private lateinit var lastOperatorTextView: TextView

    // Current number being calculated
    private var currentNumber: Double = 0.0

    // Last operation performed
    private var lastOperation = '='

    // Flag to track if a new number is being entered
    private var isNewNumber = true

    // Flag to track if the decimal dot is used in the current number
    private var hasDot = false

    // List to store History
    private val calculationHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        resultsTextView = findViewById(R.id.results)
        lastOperatorTextView = findViewById(R.id.lastOperator)
    }

    // Function to handle number button clicks
    fun numberAction(view: View) {
        if (view is Button) {
            val buttonText: String = view.text.toString()

            // Update the resultsTextView based on whether it's a new number or not
            if (isNewNumber) {
                resultsTextView.text = buttonText
                isNewNumber = false
            } else {
                resultsTextView.append(buttonText)
            }
        }
    }

    // Function to handle operation button clicks
    fun operationAction(view: View) {
        if (view is Button) {
            // Determine the operation based on the button text
            when (val buttonText: String = view.text.toString()) {
                "+" -> performOperation('+', "+")
                "-" -> performOperation('-', "-")
                "x" -> performOperation('x', "x")
                "/" -> performOperation('/', "/")
                "." -> {
                    // Allow adding a decimal dot only once in the current number
                    if (!hasDot) {
                        resultsTextView.append(buttonText)
                        hasDot = true
                    }
                }
            }
        }
    }

    // Function to handle clear button click
    fun clearAction(view: View) {
        // Reset all variables and UI components
        resultsTextView.text = ""
        currentNumber = 0.0
        lastOperation = '='
        isNewNumber = true
        hasDot = false
        lastOperatorTextView.text = ""
    }

    // Function to handle equals button click
    fun equalsAction(view: View) {
        // Calculate and display the result
        calculate()
        isNewNumber = true
        hasDot = false
        lastOperatorTextView.text = "="
    }

    // Function to perform the selected operation
    private fun performOperation(operation: Char, operatorText: String) {
        // Calculate and update the UI with the selected operation
        calculate()
        lastOperation = operation
        isNewNumber = true
        hasDot = false
        lastOperatorTextView.text = operatorText
    }

    // Function to perform the calculation
    private fun calculate() {
        val newNumberText = resultsTextView.text.toString()

        if (TextUtils.isEmpty(newNumberText)) return

        try {
            val newNumber = newNumberText.toDouble()
            val previousNumber = currentNumber
            val operator = lastOperation
            val expression: String

            when (operator) {
                '=' -> {
                    currentNumber = newNumber
                    return
                }

                '+' -> currentNumber += newNumber
                '-' -> currentNumber -= newNumber
                'x' -> currentNumber *= newNumber
                '/' -> {
                    if (newNumber == 0.0) {
                        Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
                        return
                    }
                    currentNumber /= newNumber
                }
            }

            val formatter = DecimalFormat("#.######")
            val resultFormatted = formatter.format(currentNumber)
            expression = "$previousNumber $operator $newNumber = $resultFormatted"

            // Add to history
            calculationHistory.add(expression)

            // Display result
            resultsTextView.text = resultFormatted

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
        }
    }

    fun showHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putStringArrayListExtra("HISTORY_LIST", ArrayList(calculationHistory))
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val updatedHistory = data?.getStringArrayListExtra("CLEARED_HISTORY")
            if (updatedHistory != null) {
                calculationHistory.clear()
                calculationHistory.addAll(updatedHistory)
            }
        }
    }

};













