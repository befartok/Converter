package com.example.converter

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Selection.setSelection
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

//import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private lateinit var courseType: String
    private var currencies = arrayOf("USD", "GBP", "EUR", "RUR")

    private lateinit var spinnerStart: Spinner
    private lateinit var spinnerFin: Spinner
    //private var spinnerFin: Spinner? = null

    //private var textViewMsg: TextView? = null

    companion object {
        const val TAG = "Log.d"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.converter.R.layout.activity_main)

        // val toolbar= findViewById<Toolbar>(R.id.toolbar)
        // setSupportActionBar(toolbar);


        //init()

        spinnerStart = findViewById(com.example.converter.R.id.startingSpinner)
        //spinnerStart = this.startingSpinner

        // Create an ArrayAdapter using a simple spinner layout and curencies array
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, currencies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerStart.adapter = aa
        spinnerStart.setSelection(2)

        spinnerFin = findViewById(com.example.converter.R.id.finishSpinner)
        spinnerFin.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and currencies array
        //   val aaFin = ArrayAdapter(this, R.layout.simple_spinner_item, currencies)
        // Set layout to use when the list of choices appear
        //aaFin.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerFin.adapter = aa
        // spinnerFin.adapter = aaFin
        spinnerFin.setSelection(3)
        spinnerStart.onItemSelectedListener = this

    }

    fun init() {

    }

    private var currencyStart: String = ""
    private var currencyFin: String = ""
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View?, position: Int, id: Long) {

        /* val i: Int? = spinnerStart?.selectedItemPosition
         val j: Int? = spinnerFin?.selectedItemPosition

         currencyStart = currencies[i!!]
         currencyFin = currencies[j!!]
 */
        val i: Int = spinnerStart.selectedItemPosition
        val j: Int = spinnerFin.selectedItemPosition

        currencyStart = currencies[i]
        currencyFin = currencies[j]


        Log.d(
            TAG,
            "currencyStart= $currencyStart, currencyFin=  $currencyFin"
        )

        when (currencyStart) {
            "USD" -> imageView.setImageResource(com.example.converter.R.drawable.ic_united_states)
            "EUR" -> imageView.setImageResource(com.example.converter.R.drawable.ic_european_union)
            "GBP" -> imageView.setImageResource(com.example.converter.R.drawable.ic_uk)
            "RUR" -> imageView.setImageResource(com.example.converter.R.drawable.ic_russia)
        }

        when (currencyFin) {
            "USD" -> imageView2.setImageResource(com.example.converter.R.drawable.ic_united_states)
            "EUR" -> imageView2.setImageResource(com.example.converter.R.drawable.ic_european_union)
            "GBP" -> imageView2.setImageResource(com.example.converter.R.drawable.ic_uk)
            "RUR" -> imageView2.setImageResource(com.example.converter.R.drawable.ic_russia)
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_converter, menu)
        return true
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menu.add(getString(com.example.converter.R.string.history))
        menu.add(getString(com.example.converter.R.string.about))
        menu.add(getString(com.example.converter.R.string.exit))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // TODO убрать потом
        Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        when (item.title) {
            getString(com.example.converter.R.string.about) -> startAboutActivity()
            getString(com.example.converter.R.string.history) -> startHistoryActivity()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun startHistoryActivity() {
        // Create an Intent to start the AboutI activity
        val historyIntent = Intent(this, HistoryActivity::class.java)

        // Start the new activity.
        startActivity(historyIntent)
    }


    fun runConverting(view: View) {

        when {
            currencyFin == "RUR" && currencyStart != "RUR" -> courseType = "direct"
            currencyStart == currencyFin -> courseType = "self"
            currencyStart == "RUR" && currencyFin != "RUR" -> courseType = "inverse"
            currencyStart != "RUR" && currencyFin != "RUR" -> courseType = "cross"
        }

        NewThread(this, currencyStart, currencyFin, courseType).execute()

    }

    private fun startAboutActivity() {
        val aboutIntent = Intent(this, AboutActivity::class.java)

        startActivity(aboutIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.run {
            putString("KEY_TEXT_VIEW", textView.text.toString())
        }
        outState.run {

            putString("KEY_EDIT_TEXT_VIEW", editTextNumberDecimal.text.toString())
        }
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        textView.text = savedInstanceState.get("KEY_TEXT_VIEW").toString()
        editTextNumberDecimal.setText(savedInstanceState.get("KEY_EDIT_TEXT_VIEW").toString())
    }

}



