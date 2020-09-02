package com.example.converter

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import android.os.AsyncTask;
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class NewThread(private var activity: MainActivity, private val _codeOfCurrency: String) :
    AsyncTask<String, String, String>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    companion object {
        private val TAG = "Log.d"
    }

    override fun doInBackground(vararg p0: String?): String {
        val url = "http://www.cbr.ru/scripts/XML_daily.asp?"
        val codeOfCurrency: String = _codeOfCurrency
        val doc: Document
        var result = "ноль"
        try {

            doc = Jsoup.connect(url).get()
            val body: Element = doc.getElementById("R01235")
            val value: String? = body.getElementsByTag("Value").text()
            result = value.toString();
            result=result.replace(',','.',true)


        } catch (e: Exception) {
            e.printStackTrace()
            result = "ошибка"
        }

        return result
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)



        val enterredText = activity.editTextNumberDecimal.text.toString().toDouble()
        if (result != null) {
            activity.textView.text = ((result.toDouble() * enterredText).toString())
        }


       // Log.d(TAG, "result =,  $result, lenght = ${result.length}")



       // activity.textView.text =result.toString()
        //activity.textView.text =enterredText.toString()


    }
}