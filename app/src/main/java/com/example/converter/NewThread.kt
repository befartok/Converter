package com.example.converter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class NewThread(
    private var activity: MainActivity,
    private val currencyStart: String,
    private val currencyFin: String,
    private val _courseType: String
) :
    AsyncTask<String, String, String>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    private var codeOfCurrencyStart: String = ""
    private var codeOfCurrencyFin: String = ""
    private var sumStart: Double = 0.0
    private var course: Double = 0.0


    private lateinit var databaseHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null

    companion object {
        const val TAG = "Log.d"
    }

    override fun doInBackground(vararg p0: String?): String {

        val url = "http://www.cbr.ru/scripts/XML_daily.asp?"

        findCodeOfCurrencies(currencyStart, currencyFin)

        val courseType: String = _courseType
        val doc: Document

/*
        if (activity.editTextNumberDecimal != null)
        {
            sumStart = activity.editTextNumberDecimal.text.toString().toDouble()
        }
*/
        var result = ""


        //считываем введенную сумму
        try {
            sumStart = activity.editTextNumberDecimal.text.toString().toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
            result = "ошибка"
        }

        when (courseType) {
            "self" -> {
                course = 1.00
                try {

                    if (activity.editTextNumberDecimal != null) {

                        result = DecimalFormat("#0.00").format(
                            course * sumStart
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result = "ошибка"
                }
            }
            "inverse" -> {
                try {

                    doc = Jsoup.connect(url).get()
                    val body: Element = doc.getElementById(codeOfCurrencyFin)
                    val value: String? = body.getElementsByTag("Value").text()
                    var readCourse = value.toString();
                    readCourse = readCourse.replace(',', '.', true)
                    course = readCourse.toDouble()

                    try {

                        if (activity.editTextNumberDecimal != null) {

                            course = 1 / course
                            result = DecimalFormat("#0.00").format(
                                course * sumStart
                            )


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        result = "ошибка"
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    result = "ошибка"
                }

                return result
            }

            "direct" -> {

                try {

                    doc = Jsoup.connect(url).get()
                    val body: Element = doc.getElementById(codeOfCurrencyStart)
                    val value: String? = body.getElementsByTag("Value").text()
                    var readCourse = value.toString();
                    readCourse = readCourse.replace(',', '.', true)
                    course = readCourse.toDouble()

                    try {

                        if (activity.editTextNumberDecimal != null) {

                            result = DecimalFormat("#0.00").format(
                                course * sumStart
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        result = "ошибка"
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    result = "ошибка"
                }


            }

            "cross" -> try {

                doc = Jsoup.connect(url).get()
                val body1: Element = doc.getElementById(codeOfCurrencyStart)
                val value1: String? = body1.getElementsByTag("Value").text()
                var course1 = value1.toString();
                course1 = course1.replace(',', '.', true)

                val body2: Element = doc.getElementById(codeOfCurrencyFin)
                val value2: String? = body2.getElementsByTag("Value").text()
                var course2 = value2.toString();
                course2 = course2.replace(',', '.', true)
                course = course1.toDouble() / course2.toDouble()

                try {

                    if (activity.editTextNumberDecimal != null) {

                        result = DecimalFormat("#0.00").format(
                            course * sumStart
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result = "ошибка"
                }

            } catch (e: Exception) {
                e.printStackTrace()
                result = "ошибка"
            }
        }
        return result
    }

    private fun findCodeOfCurrencies(currencyStart: String, currencyFin: String) {
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

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        var outputResult: String = ""

        activity.textView.text = result

        //Toast.makeText(this, course.toString(), Toast.LENGTH_SHORT).show()

        //if ((result != "ошибка")||(sumStart != 0.0)) {
        if (sumStart != 0.0) {

            val dateNow = Date()
            val formatForDateNow =
                SimpleDateFormat("dd.MM.yyyy")

            val dateTimeNow = formatForDateNow.format(dateNow)

            Log.d(
                TAG,
                "formatForDateNow= $dateTimeNow, course=  $course, sumStart= $sumStart, currencyStart = $currencyStart, result= $result, currencyFin= $currencyFin "
            )

            val courseToPrint = DecimalFormat("#0.00").format(
                course
            )
            val sumStartToPrint = DecimalFormat("#0.00").format(
                sumStart
            )

            databaseHelper = DatabaseHelper(activity.applicationContext)
            db = databaseHelper.writableDatabase
            databaseHelper.createTable(db!!)
            databaseHelper.insertRecordDb(
                db!!,
                dateTimeNow.toString(),
                courseToPrint,
                sumStartToPrint,
                currencyStart,
                result,
                currencyFin
            )
        }
    }
}