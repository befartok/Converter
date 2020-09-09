package com.example.converter

import android.R
import android.R.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class HistoryActivity : AppCompatActivity() {

    private var userList: ListView? = null
    lateinit var header: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null
    private lateinit var userCursor: Cursor
    private var userAdapter: SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.converter.R.layout.activity_history)
        header = findViewById<TextView>(com.example.converter.R.id.textView3)
        userList= findViewById(com.example.converter.R.id.list1)
        databaseHelper = DatabaseHelper(applicationContext)
    }

    override fun onResume() {
        super.onResume()

        // открываем подключение
        db = databaseHelper.writableDatabase
        databaseHelper.createTable(db!!)

        userCursor = db!!.rawQuery("select * from " + DatabaseHelper.TABLE, null)

        // определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(DatabaseHelper.COLUMN_DATE_AND_TIME, DatabaseHelper.COLUMN_COURSE, DatabaseHelper.COLUMN_SUM_START,
                DatabaseHelper.COLUMN_CURRENCY_START, DatabaseHelper.COLUMN_SUM_FIN, DatabaseHelper.COLUMN_CURRENCY_FIN)
        //задаем массив для вывода
        val to = intArrayOf(com.example.converter.R.id.textView4, com.example.converter.R.id.textView5,
            com.example.converter.R.id.textView6,  com.example.converter.R.id.textView7,
            com.example.converter.R.id.textView8,  com.example.converter.R.id.textView9)

        // создаем адаптер, передаем в него курсор
        userAdapter = SimpleCursorAdapter(
            this, com.example.converter.R.layout.item, userCursor, headers, to, 0)
        header.text = "История конвертаций"
        userList?.adapter = userAdapter



/*        db = databaseHelper.writableDatabase
       // databaseHelper.deleteTable(db!!)
        val query: Cursor = db!!.rawQuery("SELECT * FROM historyTest4;", null)
        val textView = findViewById(com.example.converter.R.id.textView3) as TextView
        if (query.moveToFirst()) {
            do {
                val dateAndTime: String = query.getString(1)
                val course: String = query.getString(2)
                val sumStart: String = query.getString(3)
                val currencyStart: String = query.getString(4)
                val sumFin: String = query.getString(5)
                val currencyFin: String = query.getString(6)
                textView.append(" $dateAndTime Курс: $course $sumStart $currencyStart = $sumFin $currencyFin \n")
                textView.append("course=  $course \n")
                textView.append("currencyStart=  $currencyStart \n")

            } while (query.moveToNext())}*/


        }

    override fun onDestroy() {
        super.onDestroy()
        // Закрываем подключение и курсор
        db!!.close()
        userCursor.close()
    }






/*    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.converter.R.layout.activity_history)

        val dateNow = Date()
        val formatForDateNow =
            SimpleDateFormat("dd.MM.yyyy  hh:mm")

       // System.out.println("Текущая дата " + formatForDateNow.format(dateNow))
        val db = baseContext.openOrCreateDatabase(
            "app.db",
            Context.MODE_PRIVATE,
            null
        )

        db.execSQL("CREATE TABLE IF NOT EXISTS historyTest3 (_id INTEGER PRIMARY KEY AUTOINCREMENT, dateAndTime TEXT, course TEXT, sumStart TEXT, currencyStart TEXT, sumFin TEXT, currencyFin TEXT)")
        db.execSQL("INSERT INTO historyTest3 ( dateAndTime, course, sumStart, currencyStart,  sumFin, currencyFin ) VALUES ('07.09.2020', '76,06', '10', 'USD', '760,60', 'RUR');")

        val query: Cursor = db.rawQuery("SELECT * FROM historyTest3;", null)
        val textView = findViewById(com.example.converter.R.id.textView3) as TextView
        if (query.moveToFirst()) {
            do {
                val dateAndTime: String = query.getString(1)
                val course: String = query.getString(2)
                val sumStart: String = query.getString(3)
                val currencyStart: String = query.getString(4)
                val sumFin: String = query.getString(5)
                val currencyFin: String = query.getString(6)
                textView.append(" $dateAndTime Курс: $course $sumStart $currencyStart = $sumFin $currencyFin \n")
                textView.append("course=  $course \n")
                textView.append("currencyStart=  $currencyStart \n")

            } while (query.moveToNext())
        }
        query.close()
        db.close()

    }*/



}