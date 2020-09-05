package com.example.converter

import android.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private lateinit var courseType: String
    var curencies = arrayOf("USD", "GBP", "EUR", "RUR")

    var spinnerStart: Spinner? = null
    var spinnerFin: Spinner? = null

    var textView_msg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.converter.R.layout.activity_main)

        // val toolbar= findViewById<Toolbar>(R.id.toolbar)
        // setSupportActionBar(toolbar);


        //init()

        textView_msg = this.textView

        spinnerStart = this.startingSpinner
        spinnerStart!!.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and curencies array
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, curencies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerStart!!.setAdapter(aa)


        spinnerFin = this.finishSpinner
        spinnerFin!!.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and curencies array
        val aaFin = ArrayAdapter(this, R.layout.simple_spinner_item, curencies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerFin!!.adapter = aaFin

    }


    fun init() {


    }

    private var currencyStart: String = ""
    private var currencyFin: String = ""
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

        val i: Int? = spinnerStart?.selectedItemPosition
        val j: Int? = spinnerFin?.selectedItemPosition

        currencyStart = curencies[i!!]
        currencyFin = curencies[j!!]
        //textView_msg!!.text = "Selected 1 : " + strName1 + " Selected 2 : " + strName2

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_converter, menu)
        return true
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // TODO Auto-generated method stub
        menu.add(getString(com.example.converter.R.string.history))
        menu.add(getString(com.example.converter.R.string.about))
        menu.add(getString(com.example.converter.R.string.exit))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }


    fun runConverting(view: View) {
        var codeOfCurrencyStart = ""
        var codeOfCurrencyFin = ""
// TODO: 05.09.2020 упростить 
        when {
            currencyStart == currencyFin -> courseType = "self"
            currencyFin == "RUR" -> {
                courseType = "direct"
                when (currencyStart) {
                    "USD" -> codeOfCurrencyStart = "R01235"
                    "GBP" -> codeOfCurrencyStart = "R01035"
                    "EUR" -> codeOfCurrencyStart = "R01239"
                    "RUR" -> codeOfCurrencyStart = "RU"
                }
            }
            currencyStart == "RUR" -> {
                courseType = "inverse"
                when (currencyFin) {
                    "USD" -> codeOfCurrencyFin = "R01235"
                    "GBP" -> codeOfCurrencyFin = "R01035"
                    "EUR" -> codeOfCurrencyFin = "R01239"
                    "RUR" -> codeOfCurrencyFin = "RU"
                }
            }
            currencyStart != "RUR" && currencyFin != "RUR" -> {
                courseType = "cross"
                when (currencyStart) {
                    "USD" -> codeOfCurrencyStart = "R01235"
                    "GBP" -> codeOfCurrencyStart = "R01035"
                    "EUR" -> codeOfCurrencyStart = "R01239"
                    "RUR" -> codeOfCurrencyStart = "RU"
                }
                when (currencyFin) {
                    "USD" -> codeOfCurrencyFin = "R01235"
                    "GBP" -> codeOfCurrencyFin = "R01035"
                    "EUR" -> codeOfCurrencyFin = "R01239"
                    "RUR" -> codeOfCurrencyFin = "RU"
                }

            }
        }

        NewThread(this, codeOfCurrencyStart, codeOfCurrencyFin, courseType).execute()

    }


}



