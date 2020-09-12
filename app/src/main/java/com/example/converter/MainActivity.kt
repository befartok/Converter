package com.example.converter

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var courseType: String
    private var currencies = arrayOf("USD", "GBP", "EUR", "RUR")

    private lateinit var spinnerStart: Spinner
    private lateinit var spinnerFin: Spinner

    private var currencyStart: String = ""
    private var currencyFin: String = ""

    companion object {
        const val TAG = "Log.d"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.converter.R.layout.activity_main)

        initToolbar()
        initSpinner()

    }

    //устанавливаем toolbar
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(com.example.converter.R.string.app_name)
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(com.example.converter.R.drawable.ic_money_48)
        }
    }

    //устанавливаем toolbar
    private fun initSpinner() {
        spinnerStart = findViewById(com.example.converter.R.id.startingSpinner)

        // Создаем ArrayAdapter из simple spinner layout и массива валют
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, currencies)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        //устанавливаем адаптер в спиннер
        spinnerStart.adapter = aa

        //устанавливаем зачение по умолчанию в спиннер
        spinnerStart.setSelection(2)

        spinnerFin = findViewById(com.example.converter.R.id.finishSpinner)
        spinnerFin.onItemSelectedListener = this

        spinnerFin.adapter = aa
        spinnerFin.setSelection(3)
        spinnerStart.onItemSelectedListener = this
    }

    //определяем код валюты установленной в спиннерах
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View?, position: Int, id: Long) {

        val startItem: Int = spinnerStart.selectedItemPosition
        val finishItem: Int = spinnerFin.selectedItemPosition

        currencyStart = currencies[startItem]
        currencyFin = currencies[finishItem]

        //устанавливаем иконку с флагом на спиннер
        when (currencyStart) {
            getString(com.example.converter.R.string.USD) -> imageView.setImageResource(com.example.converter.R.drawable.ic_united_states)
            getString(com.example.converter.R.string.EUR) -> imageView.setImageResource(com.example.converter.R.drawable.ic_european_union)
            getString(com.example.converter.R.string.GBP) -> imageView.setImageResource(com.example.converter.R.drawable.ic_uk)
            getString(com.example.converter.R.string.RUR) -> imageView.setImageResource(com.example.converter.R.drawable.ic_russia)
        }

        when (currencyFin) {
            getString(com.example.converter.R.string.USD) -> imageView2.setImageResource(com.example.converter.R.drawable.ic_united_states)
            getString(com.example.converter.R.string.EUR) -> imageView2.setImageResource(com.example.converter.R.drawable.ic_european_union)
            getString(com.example.converter.R.string.GBP) -> imageView2.setImageResource(com.example.converter.R.drawable.ic_uk)
            getString(com.example.converter.R.string.RUR) -> imageView2.setImageResource(com.example.converter.R.drawable.ic_russia)
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    //создание меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menu.add(getString(com.example.converter.R.string.history))
        menu.add(getString(com.example.converter.R.string.about))
        menu.add(getString(com.example.converter.R.string.exit))
        return super.onCreateOptionsMenu(menu)
    }

    //обработка нажатий меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.title) {
            getString(com.example.converter.R.string.about) -> startAboutActivity()
            getString(com.example.converter.R.string.history) -> startHistoryActivity()
            getString(com.example.converter.R.string.exit) -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //старт активити история конвертаций
    private fun startHistoryActivity() {
        val historyIntent = Intent(this, HistoryActivity::class.java)
        startActivity(historyIntent)
    }

    //запуск конвертации
    fun runConverting(view: View) {

        //Определяем тип курса
        when {
            currencyFin == getString(com.example.converter.R.string.RUR)
                    && currencyStart != getString(com.example.converter.R.string.RUR) -> courseType =
                "direct"
            currencyStart == currencyFin -> courseType = "self"
            currencyStart == getString(com.example.converter.R.string.RUR)
                    && currencyFin != getString(com.example.converter.R.string.RUR) -> courseType =
                "inverse"
            currencyStart != getString(com.example.converter.R.string.RUR)
                    && currencyFin != getString(com.example.converter.R.string.RUR) -> courseType =
                "cross"
        }

        NewThread(this, currencyStart, currencyFin, courseType).execute()
    }

    //старт активити о приложении
    private fun startAboutActivity() {
        val aboutIntent = Intent(this, AboutActivity::class.java)
        startActivity(aboutIntent)
    }

    //сохраняем данные от потери при повороте
    override fun onSaveInstanceState(outState: Bundle) {

        outState.run {
            putString("KEY_TEXT_VIEW", textView.text.toString())
        }
        outState.run {
            putString("KEY_EDIT_TEXT_VIEW", editTextNumberDecimal.text.toString())
        }

        outState.run {
            putString("SPINNER_START_VIEW", spinnerStart.selectedItemPosition.toString())
        }
        outState.run {
            putString("SPINNER_FINISH_VIEW", spinnerFin.selectedItemPosition.toString())
        }



        super.onSaveInstanceState(outState)

    }

    //считываем данные сохраненные при повороте
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        textView.text = savedInstanceState.get("KEY_TEXT_VIEW").toString()
        editTextNumberDecimal.setText(savedInstanceState.get("KEY_EDIT_TEXT_VIEW").toString())
        spinnerStart.setSelection(savedInstanceState.get("SPINNER_START_VIEW").toString().toInt())
        spinnerFin.setSelection(savedInstanceState.get("SPINNER_FINISH_VIEW").toString().toInt())

    }
}



