package com.example.converter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
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

    private val context: Context = activity.applicationContext

    override fun doInBackground(vararg p0: String?): String {

        //адрес ЦБ с курсами
        val url = "http://www.cbr.ru/scripts/XML_daily.asp?"
        val courseType: String = _courseType
        val doc: Document
        var result = ""

        //определяем коды валют
        findCodeOfCurrencies(currencyStart, currencyFin)

        //считываем введенную сумму
        try {
            sumStart = activity.editTextNumberDecimal.text.toString().toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
            result = context.getString(R.string.error)
        }

        //находим результат в зависимости от типа курса
        when (courseType) {
            //если начальная и конечная валюта совпадают
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
                    result = context.getString(R.string.error)
                }
            }
            //при обмене рублей на валюту
            "inverse" -> {
                try {
                    doc = Jsoup.connect(url).get()//содинение с URL
                    val body: Element = doc.getElementById(codeOfCurrencyFin)
                    val value: String? = body.getElementsByTag("Value").text()
                    var readCourse = value.toString()
                    readCourse = readCourse.replace(',', '.', true)
                    course = readCourse.toDouble()

                    try {
                        if (activity.editTextNumberDecimal != null) {

                            course = 1 / course //определяем курс обратный курсу ЦБ
                            //находим результат умножив курс на сумму
                            result = DecimalFormat("#0.00").format(
                                course * sumStart
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        result = context.getString(R.string.error)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    result = context.getString(R.string.error)
                }
                return result
            }
            //при обмене  валюты на рубли
            "direct" -> {
                try {

                    doc = Jsoup.connect(url).get()
                    val body: Element = doc.getElementById(codeOfCurrencyStart)
                    val value: String? = body.getElementsByTag("Value").text()
                    var readCourse = value.toString()
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
                        result = context.getString(R.string.error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result = context.getString(R.string.error)
                }
            }
            //при обмене  валюты на валюту
            "cross" -> try {

                doc = Jsoup.connect(url).get()
                val body1: Element = doc.getElementById(codeOfCurrencyStart)
                val value1: String? = body1.getElementsByTag("Value").text()
                var course1 = value1.toString()
                course1 = course1.replace(',', '.', true)

                val body2: Element = doc.getElementById(codeOfCurrencyFin)
                val value2: String? = body2.getElementsByTag("Value").text()
                var course2 = value2.toString()
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
                    result = context.getString(R.string.error)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                result = context.getString(R.string.error)
            }
        }
        return result
    }

    private fun findCodeOfCurrencies(currencyStart: String, currencyFin: String) {
        when (currencyStart) {
            context.getString(R.string.USD) -> codeOfCurrencyStart =
                context.getString(R.string.usdR01235)
            context.getString(R.string.GBP) -> codeOfCurrencyStart =
                context.getString(R.string.gbpR01035)
            context.getString(R.string.EUR) -> codeOfCurrencyStart =
                context.getString(R.string.eurR01239)
            context.getString(R.string.RUR) -> codeOfCurrencyStart = context.getString(R.string.RU)
        }
        when (currencyFin) {
            context.getString(R.string.USD) -> codeOfCurrencyFin =
                context.getString(R.string.usdR01235)
            context.getString(R.string.GBP) -> codeOfCurrencyFin =
                context.getString(R.string.gbpR01035)
            context.getString(R.string.EUR) -> codeOfCurrencyFin =
                context.getString(R.string.eurR01239)
            context.getString(R.string.RUR) -> codeOfCurrencyFin = context.getString(R.string.RU)
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        //вывод результата
        activity.textView.text = result

        //ЗАпись в ДБ
        if (sumStart != 0.0) {

            recordToDB(result)
        }
    }

    private fun recordToDB(_result: String) {
        //находим текущую дату
        val dateNow = Date()
        val formatForDateNow =
            SimpleDateFormat("dd.MM.yy")

        val dateTimeNow = formatForDateNow.format(dateNow)

        //форматируем курс для вывода
        val courseToPrint = DecimalFormat("#0.00").format(
            course
        )
        val sumStartToPrint = DecimalFormat("#0.00").format(
            sumStart
        )
        //Log.d(TAG,"formatForDateNow= $dateTimeNow, course=  $course, sumStart= $sumStart, currencyStart = $currencyStart, result= $result, currencyFin= $currencyFin")

        //запись в БД
        databaseHelper = DatabaseHelper(activity.applicationContext)
        db = databaseHelper.writableDatabase
        //databaseHelper.createTable(db!!)
        databaseHelper.insertRecordDb(
            db!!,
            dateTimeNow.toString(),
            courseToPrint,
            sumStartToPrint,
            currencyStart,
            _result,
            currencyFin
        )
    }


}