package com.example.converter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {

    val toolbar : Toolbar = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar);



        //button = (Button())findViewById(R.id.button);
        //textView = (TextView)findViewById(R.id.textView1);




/*        var url = "http://www.cbr.ru/scripts/XML_daily.asp?"
        val doc = Jsoup.connect(url).get()

        val body: Element = doc.getElementById("R01235")
        val value: Elements = body.getElementsByTag("Value")
        textView.text = value.toString();
        //textView.text = "5";*/

        NewThread(this).execute()

    }



}



