package com.example.converter

import android.R.id
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private var userList: ListView? = null

    private lateinit var databaseHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null
    private lateinit var userCursor: Cursor
    private var userAdapter: SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initToolbar()

        databaseHelper = DatabaseHelper(applicationContext)
    }

    //устанавливаем toolbar
    private fun initToolbar() {

        setSupportActionBar(toolbarAH)
        supportActionBar?.apply {

            title = getString(R.string.history)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    //обработка нажатия кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        // открываем подключение
        db = databaseHelper.writableDatabase
        databaseHelper.createTable(db!!)

        //присваиваем курсору выборку из БД
        userCursor = db!!.rawQuery("select * from " + DatabaseHelper.TABLE, null)

        // определяем, какие столбцы из курсора будут выводиться в ListView
        val from =
            arrayOf(
                DatabaseHelper.COLUMN_DATE_AND_TIME,
                DatabaseHelper.COLUMN_COURSE,
                DatabaseHelper.COLUMN_SUM_START,
                DatabaseHelper.COLUMN_CURRENCY_START,
                DatabaseHelper.COLUMN_SUM_FIN,
                DatabaseHelper.COLUMN_CURRENCY_FIN
            )
        //задаем массив для вывода
        val to = intArrayOf(
            R.id.textView4, R.id.textView5,
            R.id.textView6, R.id.textView7,
            R.id.textView8, R.id.textView9
        )

        // создаем адаптер, передаем в него курсор
        userAdapter = SimpleCursorAdapter(
            this, R.layout.item, userCursor, from, to, 0
        )
        userList = findViewById(R.id.list1)
        userList?.adapter = userAdapter//устанавливаем адаптер в список
    }

    override fun onDestroy() {
        super.onDestroy()
        // Закрываем подключение и курсор
        db!!.close()
        userCursor.close()
    }
}