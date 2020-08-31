package com.example.converter

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import android.os.AsyncTask;
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class NewThread(private var activity: MainActivity) : AsyncTask<String, String, String>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: String?): String {
        val url = "http://www.cbr.ru/scripts/XML_daily.asp?"
        val doc: Document
        var result = "ноль"
        try {

            doc = Jsoup.connect(url).get()
            val body: Element = doc.getElementById("R01235")
            val value: String? = body.getElementsByTag("Value").text()
            //val value : Element = values[0]
            result = value.toString();

        } catch (e: Exception) {
            e.printStackTrace()
            result = "ошибка"
        }

/*        if (doc != null) {
            val body: Element = doc.getElementById("R01235")
            val value: Elements = body.getElementsByTag("Value")
            val result = value.toString();
        } else result = "ошибка"*/



        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        activity.textView.text = result

    }
}