package com.example.converter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        SCHEMA
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    //удаление таблицы
    fun deleteTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
    }

    //Создание таблицы
    fun createTable(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE_AND_TIME
                    + " TEXT, " + COLUMN_COURSE
                    + " TEXT, " + COLUMN_SUM_START
                    + " TEXT, " + COLUMN_CURRENCY_START
                    + " TEXT, " + COLUMN_SUM_FIN
                    + " TEXT, " + COLUMN_CURRENCY_FIN + " TEXT);"
        )
    }

    //добавление данных в БД
    fun insertRecordDb(
        db: SQLiteDatabase,
        dateAndTimeValue: String,
        courseValue: String,
        sumStartValue: String,
        currencyStartValue: String,
        sumFinValue: String,
        currencyFinValue: String
    ) {
        //deleteTable(db)
        createTable(db)

        db.execSQL(
            "INSERT INTO $TABLE ($COLUMN_DATE_AND_TIME, $COLUMN_COURSE, $COLUMN_SUM_START, $COLUMN_CURRENCY_START, $COLUMN_SUM_FIN, $COLUMN_CURRENCY_FIN ) VALUES " +
                    "('$dateAndTimeValue', '$courseValue', '$sumStartValue', '$currencyStartValue', '$sumFinValue', '$currencyFinValue');"
        )

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    //константы
    companion object {
        private const val DATABASE_NAME = "history.db" // название бд
        private const val SCHEMA = 1 // версия базы данных
        const val TABLE = "historyTest" // название таблицы в бд

        // названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE_AND_TIME = "dateAndTime"
        const val COLUMN_COURSE = "course"
        const val COLUMN_SUM_START = "sumStart"
        const val COLUMN_CURRENCY_START = "currencyStart"
        const val COLUMN_SUM_FIN = "sumFin"
        const val COLUMN_CURRENCY_FIN = "currencyFin"
    }
}