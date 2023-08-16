package com.task.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request

class MainActivity : AppCompatActivity() {

    private lateinit var etFrom: EditText
    private lateinit var txtResult: TextView
    private lateinit var spnOne: Spinner
    private lateinit var spnTwo: Spinner
    private lateinit var btnConversionButton: Button
    private var baseCurrency = "USD"
    private var targetCurrency = "INR"
    private var conversionRate = 0f
    private val apiKey = "5da210b8ba5e1f992452aff7"
    private val apiUrl = "https://v6.exchangerate-api.com/v6/$apiKey/latest/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etFrom = findViewById(R.id.etFrom)
        txtResult = findViewById(R.id.txtResult)
        spnOne = findViewById(R.id.spnOne)
        spnTwo = findViewById(R.id.spnTwo)
        btnConversionButton = findViewById(R.id.btnConversionButton)

        // Populate the spinners with currency options
        val currencyOptions = resources.getStringArray(R.array.currencyCodesOne)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnOne.adapter = adapter


        val currencyOptionsTwo = resources.getStringArray(R.array.currencyCodesTwo)
        val adapterTwo = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyOptionsTwo)
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTwo.adapter = adapterTwo

        spnOne.setSelection(adapter.getPosition(baseCurrency))
        spnTwo.setSelection(adapterTwo.getPosition(targetCurrency))

        // Set up conversion button click listener
        btnConversionButton.setOnClickListener {
            baseCurrency = spnOne.selectedItem.toString()
            targetCurrency = spnTwo.selectedItem.toString()
            convertCurrency()
        }




    }

    private fun convertCurrency() {
        val requestUrl = "$apiUrl$baseCurrency"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, requestUrl, null,
            { response ->
                val rates = response.getJSONObject("conversion_rates")
                if (rates.has(targetCurrency)) {
                    conversionRate = rates.getDouble(targetCurrency).toFloat()
                    val inputValue = etFrom.text.toString().toFloat()
                    val convertedValue = inputValue * conversionRate
                    txtResult.setText(convertedValue.toString())
                }
            },
            { error ->
                Toast.makeText(this, "Currency conversion failed", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}