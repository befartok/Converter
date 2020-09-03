package com.example.converter

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var curencies = arrayOf("USD", "GBP", "EUR", "RUR")

    var spinnerStart: Spinner? = null
    var spinnerFin: Spinner? = null

    var textView_msg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // val toolbar= findViewById<Toolbar>(R.id.toolbar)
       // setSupportActionBar(toolbar);


        //init()

        textView_msg = this.textView

        spinnerStart = this.startingSpinner
        spinnerStart!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, curencies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerStart!!.setAdapter(aa)


        spinnerFin = this.finishSpinner
        spinnerFin!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aaFin = ArrayAdapter(this, android.R.layout.simple_spinner_item, curencies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerFin!!.adapter = aaFin

    }



    fun init(){


    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        textView_msg!!.text = "Selected : "+curencies[position]
    }
    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    fun runConverting(view: View) {
        NewThread(this, "USD").execute()

    }


}



